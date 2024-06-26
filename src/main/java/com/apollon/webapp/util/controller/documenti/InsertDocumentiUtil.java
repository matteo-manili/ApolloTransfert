package com.apollon.webapp.util.controller.documenti;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.apollon.Constants;
import com.apollon.dao.AutistaDao;
import com.apollon.dao.AutistaSottoAutistiDao;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.dao.DocumentiCapDao;
import com.apollon.dao.DocumentiIscrizioneRuoloDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.model.DocumentiCap;
import com.apollon.model.DocumentiIscrizioneRuolo;
import com.apollon.model.GestioneApplicazione;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.AmazonStorageFiles;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.email.InviaEmail;
import com.lowagie.text.DocumentException;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class InsertDocumentiUtil extends ApplicationUtils{
	
	private static final Log log = LogFactory.getLog(InsertDocumentiUtil.class);
	private static AutistaDao autistaDao = (AutistaDao) contextDao.getBean("AutistaDao");
	private static DocumentiIscrizioneRuoloDao documentiIscrizioneRuoloDao = (DocumentiIscrizioneRuoloDao) contextDao.getBean("DocumentiIscrizioneRuoloDao");
	private static DocumentiCapDao documentiCapDao = (DocumentiCapDao) contextDao.getBean("DocumentiCapDao");
	private static AutistaSottoAutistiDao autistaSottoAutistiDao = (AutistaSottoAutistiDao) contextDao.getBean("AutistaSottoAutistiDao");
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	public static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	public static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	
	
	public String StreamPDFContratto(Autista autista, VelocityEngine velocityEngine, OutputStream baos, HttpServletRequest request) throws DocumentException {
		String filename = ""; String templateFile = "";
        if(autista.isAzienda()){
        	templateFile = Constants.VM_SCRITTURA_PRIVATA_AZIENDA;
        	filename = "contratto_azienda_"+UtilString.RimuoviCaratteriIllegaliFileName_e_ReplaceSpace(autista.getUser().getLastName())+".pdf";
        }else{
        	templateFile = Constants.VM_SCRITTURA_PRIVATA_AUTISTA;
        	filename = "contratto_autista_"+UtilString.RimuoviCaratteriIllegaliFileName_e_ReplaceSpace(autista.getUser().getLastName())+".pdf";
        }
        Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("nomeCognome", autista.getUser().getFullName());
        modelVelocity.put("autista", autista);
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put("prezzoRitardoCliete", new BigDecimal(gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueString())
        	.divide(new BigDecimal(2)));
        modelVelocity.put("numMaxOreAttesaRitornoSconto", gestioneApplicazioneDao.getName("NUMERO_MAX_ORE_ATTESA_RITORNO_PER_SCONTO").getValueNumber());
        modelVelocity.put("percentualeRitornoSconto", gestioneApplicazioneDao.getName("PERCENTUALE_SCONTO_RITORNO").getValueNumber());
        final GestioneApplicazione MaggNott_GestApp = gestioneApplicazioneDao.getName("MAGGIORAZIONE_NOTTURNA");
		final String[] MaggNott_OrarioNutturno = UtilString.RimuoviTuttiGliSpazi(MaggNott_GestApp.getValueString()).split("-");
		modelVelocity.put("maggNotturnaPrezzo", (int)(long)MaggNott_GestApp.getValueNumber());
		modelVelocity.put("inizioOraNotturno", Integer.parseInt(MaggNott_OrarioNutturno[0]));
		modelVelocity.put("fineOraNotturno", Integer.parseInt(MaggNott_OrarioNutturno[1]));
        String xhtml = (velocityEngine != null) ? 
        		VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFile, Constants.ENCODING_UTF_8, modelVelocity) : 
        			DammiTemplateVELOCITY(templateFile, modelVelocity);
		ITextRenderer renderer = new ITextRenderer();
		System.out.println(xhtml);
		renderer.setDocumentFromString( xhtml );
		renderer.layout();
		renderer.createPDF( baos );
		return filename;
	}
	
	
	/**
	 * Se tutti i documenti sono Approvati invia una email all'Autista con il contratto in allegato
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public boolean CheckInviaEmailContrattoAutista(Autista autista, VelocityEngine velocityEngine, HttpServletRequest request) throws Exception {
		DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
		if(docUtil.documentiCompletatiEsclusoContratto && docUtil.documentiApprovatiEsclusoContratto && !autista.getAutistaDocumento().isApprovatoGenerale()){
			//TUTTI DOCUMENTI COMPLETATI E APPROVATI
			String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
			// Testo Dinamico della email
			Map<String, Object> modelVelocity = new HashMap<String, Object>();
			modelVelocity.put("nomeCognome", autista.getUser().getFullName());
			modelVelocity.put("linkInsertDocumenti", "<a href=\""+ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", request.getLocale())+"/insert-documenti\">Inserisci Documento Contratto</a>");
			modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
			modelVelocity.put(Constants.VM_ATTRIBUTE_AUTISTA_INFO, InviaEmail.InfoAutistaExtraEmail(Parametri, autista, velocityEngine, request) );
			modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", request.getLocale()));
			// Allegato
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			String fileName = StreamPDFContratto(autista, velocityEngine, baos, request);
			InviaEmail.InviaEmail_HTML_User(autista.getUser(), Constants.VM_EMAIL_CONTRATTO_AUTISTA, "Contratto ApolloTransfert", velocityEngine, 
					modelVelocity, baos, fileName, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
			return true;
			
		}else{
			//TUTTI DOCUMENTI NON COMPLETATI E APPROVATI
			return false;
		}
	}
	

	
	/**
	 * Se tutti i documenti sono Approvati invia una email all'autista con il contratto in allegato
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public boolean InviaEmailAutistaProfiloApprovato(Autista autista, VelocityEngine velocityEngine, HttpServletRequest request) throws Exception {
		if( autista.getAutistaDocumento().isApprovatoGenerale() ){
			//PROFILO APPROVATO GENERALE
			String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
			// Testo Dinamico della email
			Map<String, Object> mapForVelocity = new HashMap<String, Object>();
			mapForVelocity.put("autista", autista);
	        mapForVelocity.put(Constants.VM_ATTRIBUTE_AUTISTA_INFO, InviaEmail.InfoAutistaExtraEmail(Parametri, autista, velocityEngine, request) );
	        mapForVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, FooterEmail(velocityEngine, mapForVelocity, "context.name.apollotransfert", request.getLocale()));
			InviaEmail.InviaEmail_HTML_User(autista.getUser(), Constants.VM_EMAIL_AUTISTA_PROFILO_APPROVATO, "Profilo Autista Approvato", 
					velocityEngine, mapForVelocity, null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
			return true;
		}else{
			//TUTTI DOCUMENTI NON COMPLETATI E APPROVATI
			return false;
		}
	}

	
	
	
	
	/**
	 * Da Autista privato --> Azienda
	 * Elimino eventuale: documento Iscrizione Ruolo e Documento Cap e metto Approvazione Generale False
	 */
	public static String AbilitaAzienda(HttpServletRequest request, Autista autista) throws Exception{
		log.debug("AbilitaAzienda Da Privato ---> Azienda");
		DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
		if( !autista.getAutistaDocumento().isApprovatoGenerale() 
				&& docUtil.tabDocCheck.get("collaboratoreTab-approv") != null && docUtil.tabDocCheck.get("collaboratoreTab-approv") == false ){ // PROFILO NON APPROVATO
			autista.setAzienda(true);
			boolean eliminato = false;
			// rimuovo gli eventuali Dati di IscrizioneRuolo e DocumentoCAP
			if( autista.getAutistaDocumento().getDocumentiIscrizioneRuolo() != null ){
				long idDocIscrizioneRuoloRemove = autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getId();
				DocumentiIscrizioneRuolo docIscrizioneRuolo = documentiIscrizioneRuoloDao.get(idDocIscrizioneRuoloRemove);
				AmazonStorageFiles.EliminareFile_AmazonWebService(request, docIscrizioneRuolo, Constants.AMAZON_FILE_RUOLO_CONDUCENTI);
				autista.getAutistaDocumento().setDocumentiIscrizioneRuolo(null);
				autistaDao.saveAutista(autista);
				documentiIscrizioneRuoloDao.remove(docIscrizioneRuolo);
				eliminato = true;
			}
			if(autista.getAutistaDocumento().getDocumentiCap() != null){
				long idDocCapRemove = autista.getAutistaDocumento().getDocumentiCap().getId();
				DocumentiCap documetoCap = documentiCapDao.get(idDocCapRemove);
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, documetoCap, Constants.AMAZON_FILE_CAP);
	    		autista.getAutistaDocumento().setDocumentiCap(null);
	    		autistaDao.saveAutista(autista);
	    		documentiCapDao.remove(documetoCap);
	    		eliminato = true;
			}
			if(eliminato) {
				return "Documento Iscrizione Ruolo e Cap ELIMINATI";
			}
		}
		return null;
	}

	/**
	 * Da Azienda --> Autista privato
	 * Elimino tutti gli eventuali sotto autisti e metto Approvazione Generale False
	 */
	public static String DisabilitaAzienda(HttpServletRequest request, Autista autista) throws IOException{
		log.debug("DisabilitaAzienda Da Azienda ---> Privato");
		DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
		if( !autista.getAutistaDocumento().isApprovatoGenerale() 
				&& docUtil.tabDocCheck.get("dipendentiTab-approv") != null && docUtil.tabDocCheck.get("dipendentiTab-approv") == false ){ // PROFILO NON APPROVATO
			autista.setAzienda(false);
			boolean eliminato = false;
			// rimuovo gli eventuali dipendenti e i file
			List<AutistaSottoAutisti> dipendentiList = autistaSottoAutistiDao.getAutistaSottoAutisti_By_Autista(autista.getId());
			for(AutistaSottoAutisti dipendentiList_ite : dipendentiList){
				EliminaSottoAutistae_e_Files(request, dipendentiList_ite.getId());
				eliminato = true;
			}
			autista.getAutistaDocumento().setApprovatoDocumenti(false);
			autistaDao.saveAutista(autista);
			if(eliminato) {
				return "Documenti Dipendenti ELIMINATI";
			}
		}
		return null;
	}
	
	/**
	 * rimuovo singolo SottoAutista e i file su Amazon
	 */
	public static void EliminaSottoAutistae_e_Files(HttpServletRequest request, Long idAutistaSottoAutistaRemove) throws IOException{
		if(idAutistaSottoAutistaRemove != null){
			AutistaSottoAutisti autistaSottoAutisti = autistaSottoAutistiDao.get( idAutistaSottoAutistaRemove );
			if(!autistaSottoAutisti.isApprovato()) {
				List<KeyVersion> keys = new ArrayList<KeyVersion>();
				keys.add(new KeyVersion( autistaSottoAutisti.getDocumentiIscrizioneRuolo().getClass().getName() + "/" + autistaSottoAutisti.getDocumentiIscrizioneRuolo().getId().toString() +"/"+ Constants.AMAZON_FILE_RUOLO_CONDUCENTI ));
				keys.add(new KeyVersion( autistaSottoAutisti.getDocumentiPatente().getClass().getName() + "/" + autistaSottoAutisti.getDocumentiPatente().getId().toString() +"/"+ Constants.AMAZON_FILE_PATENTE_F ));
				keys.add(new KeyVersion( autistaSottoAutisti.getDocumentiPatente().getClass().getName() + "/" + autistaSottoAutisti.getDocumentiPatente().getId().toString() +"/"+ Constants.AMAZON_FILE_PATENTE_R ));
				keys.add(new KeyVersion( autistaSottoAutisti.getDocumentiCap().getClass().getName() + "/" + autistaSottoAutisti.getDocumentiCap().getId().toString() +"/"+ Constants.AMAZON_FILE_CAP ));
				AmazonStorageFiles.EliminareMultipleFile_AmazonWebService(request, keys);
				autistaSottoAutistiDao.remove(autistaSottoAutisti); 
			}
		}
	}
	
	
	
}

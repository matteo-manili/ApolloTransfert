package com.apollon.webapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.amazonaws.services.s3.model.S3Object;
import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.model.Autoveicolo;
import com.apollon.model.DocumentiCap;
import com.apollon.model.DocumentiIscrizioneRuolo;
import com.apollon.model.DocumentiPatente;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.model.User;
import com.apollon.service.AutistaLicenzeNccManager;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutistaSottoAutistiManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.ClasseAutoveicoloManager;
import com.apollon.service.DocumentiCapManager;
import com.apollon.service.DocumentiIscrizioneRuoloManager;
import com.apollon.service.DocumentiPatenteManager;
import com.apollon.service.RegioniManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RitardiManager;
import com.apollon.service.SupplementiManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.AmazonStorageFiles;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.DownloadFile;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.documenti.InsertDocumentiUtil;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.fatturazione.BeanInfoFattura_Corsa;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf.Footer_Header_Fattura;
import com.google.maps.errors.ApiException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * vedere: http://developers.itextpdf.com/examples/itext-action-second-edition/chapter-9
 *
 */

@Controller
public class DownloadFileController extends BaseFormController {

	private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
	
    private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	private DocumentiPatenteManager documentiPatenteManager;
    @Autowired
    public void setDocumentiPatenteManager(final DocumentiPatenteManager documentiPatenteManager) {
        this.documentiPatenteManager = documentiPatenteManager;
    }
    
    private DocumentiCapManager documentiCapManager;
    @Autowired
    public void setDocumentiCapManager(final DocumentiCapManager documentiCapManager) {
        this.documentiCapManager = documentiCapManager;
    }
    
    private AutistaLicenzeNccManager autistaLicenzeNccManager;
    @Autowired
    public void setAutistaLicenzeNccManager(final AutistaLicenzeNccManager autistaLicenzeNccManager) {
        this.autistaLicenzeNccManager = autistaLicenzeNccManager;
    }
	
    private DocumentiIscrizioneRuoloManager documentiIscrizioneRuoloManager;
    @Autowired
    public void setDocumentiIscrizioneRuoloManager(final DocumentiIscrizioneRuoloManager documentiIscrizioneRuoloManager) {
        this.documentiIscrizioneRuoloManager = documentiIscrizioneRuoloManager;
    }
    
    private AutistaSottoAutistiManager autistaSottoAutistiManager;
    @Autowired
    public void setAutistaSottoAutistiManager(final AutistaSottoAutistiManager autistaSottoAutistiManager) {
        this.autistaSottoAutistiManager = autistaSottoAutistiManager;
    }

    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
    
    private ClasseAutoveicoloManager classeAutoveicoloManager;
    @Autowired
    public void setClasseAutoveicoloManager(final ClasseAutoveicoloManager classeAutoveicoloManager) {
        this.classeAutoveicoloManager = classeAutoveicoloManager;
    }
	
    private RegioniManager regioniManager;
	@Autowired
	public void setRegioniManager(RegioniManager regioniManager) {
		this.regioniManager = regioniManager;
	}
	
	private RitardiManager ritardiManager;
	@Autowired
	public void setRitardiManager(RitardiManager ritardiManager) {
		this.ritardiManager = ritardiManager;
	}
	
	private SupplementiManager supplementiManager;
	@Autowired
	public void setSupplementiManager(SupplementiManager supplementiManager) {
		this.supplementiManager = supplementiManager;
	}
	
    /**
     * Scarico il Contratto dal PDF. 
     * Lo prende da tmp_email_scrittura_privata_autista.vm e tmp_email_scrittura_privata_azienda.vm
     */
	@RequestMapping(value = "/pdfDownloadTariffeTransferVenditore", method = RequestMethod.GET)
	public void pdfDownloadTariffeTransferVenditore(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(required = true, value = "url") String urlSelezionato) throws IOException  {
		log.info("sono in pdfDownloadTariffeTransferVenditore");
		try{
			User userVenditore = getUserManager().getUserByUsername(request.getRemoteUser());
			if(request.isUserInRole(Constants.VENDITORE_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
				System.out.println("URL_TRANSFER_TARIFFE: "+urlSelezionato);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				String filename = "tariffe_transfer_"+urlSelezionato+"_"+UtilString.RimuoviCaratteriIllegaliFileName_e_ReplaceSpace(userVenditore.getLastName())+".pdf";
				Map<String, Object> modelVelocity = new HashMap<String, Object>();
				if(urlSelezionato.equals( Constants.PAGE_VENDITORE_TARIFFE_TRANSFER )){
					modelVelocity.put("menuTerrTariffeTransfer", MenuTariffeTransfer.
							CaricaMenuTerrTariffeTransfer(request.getLocale(), null, InfoUserConnectAddressMain.DammiDeviceType(request), userVenditore.getId()));
				}else{
					String Url = urlSelezionato.replace(Constants.URL_VENDITORE_TARIFFE_TRANSFER, "");
					System.out.println("URL_TRANSFER_TARIFFE: "+Url);
					Object obj = regioniManager.dammiMenuTerrTariffeTransfer_LIKE_Url( Url );
					modelVelocity.put("menuTerrTariffeTransfer", MenuTariffeTransfer.
							CaricaMenuTerrTariffeTransfer(request.getLocale(), (String)obj, InfoUserConnectAddressMain.DammiDeviceType(request), userVenditore.getId()));
				}
				modelVelocity.put("nomeCognome", userVenditore.getFullName());modelVelocity.put("codiceVenditore", userVenditore.getCodiceVenditore());
				modelVelocity.put("telefonoVenditore", userVenditore.getPhoneNumber());modelVelocity.put("emailVenditore", userVenditore.getEmail());
				modelVelocity.put("descrizioneCategorieAutoList", AutoveicoloUtil.DammiDescrizioneCategorieAutoList(request.getLocale()));
		        String xhtml = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, Constants.VM_TARIFFE_TRANSFER_VENDITORE, Constants.ENCODING_UTF_8, modelVelocity);
				ITextRenderer renderer = new ITextRenderer();
				//System.out.println(xhtml);
				renderer.setDocumentFromString( xhtml );
				renderer.layout();
				renderer.createPDF( baos );
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentLength(baos.size());
				OutputStream os;
				os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
				baos.close();
			}else{
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch(NullPointerException | com.lowagie.text.DocumentException aa ){
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
    
    /**
     * Scarico il Contratto dal PDF. 
     * Lo prende da tmp_email_scrittura_privata_autista.vm e tmp_email_scrittura_privata_azienda.vm
     */
	@RequestMapping(value = "/pdfDownloadContratto", method = RequestMethod.GET)
	public void pdfDownloadContratto(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(required = false, value = "idAutista") String idAutista) throws IOException  {
		log.info("sono in pdfDownloadContratto");
		try{
			User user = getUserManager().getUserByUsername(request.getRemoteUser());
			if(idAutista != null && !idAutista.equals("") &&
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)
						|| autistaManager.getAutistaByUser( user.getId() ).getId() == Long.parseLong(idAutista))){
				Autista autista = autistaManager.get(Long.parseLong(idAutista));
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				InsertDocumentiUtil docUtil = new InsertDocumentiUtil();
				String filename = docUtil.StreamPDFContratto(autista, velocityEngine, baos, request);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentLength(baos.size());
				OutputStream os;
				os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
				baos.close();
			}else{
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch(NullPointerException | IOException | com.lowagie.text.DocumentException nP){
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
    
	private boolean CheckDocumento_Autista_SottoAutista(final User user, final String objectModel, final String objectModelId) {
		if( DocumentiPatente.class.getSimpleName().equals(objectModel) ) {
			if( autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiPatente() != null 
					&& autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiPatente().getId() == Long.parseLong(objectModelId) ) {
				return true;
			}else if( documentiPatenteManager.get( Long.parseLong(objectModelId)) != null ) {
				List<AutistaSottoAutisti> aa = autistaSottoAutistiManager.getAutistaSottoAutisti_By_Autista( autistaManager.getAutistaByUser(user.getId()).getId() );
				for(AutistaSottoAutisti ite: aa ) {
					if( ite.getDocumentiPatente().getId() == Long.parseLong(objectModelId) ) {
						return true;
					}
				}
			}
			
		}else if( DocumentiCap.class.getSimpleName().equals(objectModel) ) {
			if( autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiCap() != null 
					&& autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiCap().getId() == Long.parseLong(objectModelId) ) {
				return true;
			}else if( documentiCapManager.get( Long.parseLong(objectModelId)) != null ) {
				List<AutistaSottoAutisti> aa = autistaSottoAutistiManager.getAutistaSottoAutisti_By_Autista( autistaManager.getAutistaByUser(user.getId()).getId() );
				for(AutistaSottoAutisti ite: aa ) {
					if( ite.getDocumentiCap().getId() == Long.parseLong(objectModelId) ) {
						return true;
					}
				}
			}
			
		}else if( DocumentiIscrizioneRuolo.class.getSimpleName().equals(objectModel) ) {
			if( autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiIscrizioneRuolo() != null 
					&& autistaManager.getAutistaByUser( user.getId() ).getAutistaDocumento().getDocumentiIscrizioneRuolo().getId() == Long.parseLong(objectModelId) ) {
				return true;
			}else if( documentiIscrizioneRuoloManager.get( Long.parseLong(objectModelId)) != null ) {
				List<AutistaSottoAutisti> aa = autistaSottoAutistiManager.getAutistaSottoAutisti_By_Autista( autistaManager.getAutistaByUser(user.getId()).getId() );
				for(AutistaSottoAutisti ite: aa ) {
					if( ite.getDocumentiIscrizioneRuolo().getId() == Long.parseLong(objectModelId) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
    /**
     * Scarico il file dal Amazon S3
     * @throws IOException 
     */
	@RequestMapping(value = "/getFileAmazonStore", method = RequestMethod.GET)
	public void getFileAmazonStore(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "objectModel", required = false) final String objectModel, 
				@RequestParam(value = "objectModelId", required = false) final String objectModelId, 
					@RequestParam(value = "objectModelFileName", required = false) final String objectModelFileName) throws IOException {
		log.info("sono in getFileAmazonStore GET");
    	try{
			User user = getUserManager().getUserByUsername(request.getRemoteUser());
			String filename = "";
			
			//getFileAmazonStore?objectModel=DocumentiPatente&objectModelId=2&objectModelFileName=nomeFilePatenteFronte
			
			/**
			 * Download file com.apollon.model.DocumentiPatente
			 */
			if( objectModel != null && objectModelId != null && DocumentiPatente.class.getSimpleName().equals(objectModel) && 
					( request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)
							|| CheckDocumento_Autista_SottoAutista(user, objectModel, objectModelId)) ){
				DocumentiPatente documentiPatente = documentiPatenteManager.get( Long.parseLong(objectModelId) );
				S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, documentiPatente, objectModelFileName);
				if(objectModelFileName.equals(Constants.AMAZON_FILE_PATENTE_F )){
					filename = documentiPatente.getNomeFilePatenteFronte();
				}
				if(objectModelFileName.equals(Constants.AMAZON_FILE_PATENTE_R )){
					filename = documentiPatente.getNomeFilePatenteRetro();
				}
				ScaricaStreamFile(response, s3object, filename);
				
			/**
			 * Download file com.apollon.model.DocumentiCap
			 */
			}else if( objectModel != null && objectModelId != null && DocumentiCap.class.getSimpleName().equals(objectModel) && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE) 
							|| CheckDocumento_Autista_SottoAutista(user, objectModel, objectModelId)) ){
			DocumentiCap documentiCap = documentiCapManager.get( Long.parseLong(objectModelId) );
			S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, documentiCap, objectModelFileName);
			if(objectModelFileName.equals(Constants.AMAZON_FILE_CAP )){
				filename = documentiCap.getNomeFileCAP();
			}
			ScaricaStreamFile(response, s3object, filename);
			
			/**
			 * Download file com.apollon.model.AutistaLicenzeNcc
			 */
			}else if(objectModel != null && objectModelId != null && AutistaLicenzeNcc.class.getSimpleName().equals(objectModel) && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE) 
						|| autistaLicenzeNccManager.get( Long.parseLong(objectModelId) ).getAutista().getUser().getId() == user.getId())){
				AutistaLicenzeNcc autistaLicenzeNcc = autistaLicenzeNccManager.get( Long.parseLong(objectModelId) );
				S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, autistaLicenzeNcc, objectModelFileName);
				if(objectModelFileName.equals(Constants.AMAZON_FILE_LICENZA_NCC )){
					filename = autistaLicenzeNcc.getNomeFileDocumentoLicenza();
				}
				ScaricaStreamFile(response, s3object, filename);
			
			/**
			 * Download file com.apollon.model.DocumentiIscrizioneRuolo
			 */
			}else if( objectModel != null && objectModelId != null && DocumentiIscrizioneRuolo.class.getSimpleName().equals(objectModel) && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)
							|| CheckDocumento_Autista_SottoAutista(user, objectModel, objectModelId)) ){
				DocumentiIscrizioneRuolo documentiIscrizioneRuolo = documentiIscrizioneRuoloManager.get( Long.parseLong(objectModelId) );
				S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, documentiIscrizioneRuolo, objectModelFileName);
				if(objectModelFileName.equals(Constants.AMAZON_FILE_RUOLO_CONDUCENTI )){
					filename = documentiIscrizioneRuolo.getNomeFileDocumentoRuoloConducenti();
				}
				ScaricaStreamFile(response, s3object, filename);
				
			/**
			 * Download file com.apollon.model.Autoveicolo
			 */
			}else if(objectModel != null && objectModelId != null && Autoveicolo.class.getSimpleName().equals(objectModel) && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE) 
						|| autoveicoloManager.getConInfoAutista( Long.parseLong(objectModelId) ).getAutista().getUser().getId() == user.getId())){
				Autoveicolo autoveicolo = autoveicoloManager.get( Long.parseLong(objectModelId) );
				S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, autoveicolo, objectModelFileName);
				
				if(objectModelFileName.equals(Constants.AMAZON_FILE_AUTOVEICOLO_CARTA_CIRCOLAZIONE)){
					filename = autoveicolo.getAutoveicoloCartaCircolazione().getNomeFileCartaCircolazione();
				}
				ScaricaStreamFile(response, s3object, filename);
				
			/**
			 * Download file com.apollon.model.Autista - Contratto
			 */
			}else if(objectModel != null && objectModelId != null && Autista.class.getSimpleName().equals(objectModel) && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE) 
							|| autistaManager.getAutistaByUser( user.getId() ).getId() == Long.parseLong(objectModelId))){
				Autista autista = autistaManager.get( Long.parseLong(objectModelId) );
				S3Object s3object = AmazonStorageFiles.GetFile_AmazonWebService(request, autista, objectModelFileName);
				
				if(objectModelFileName.equals(Constants.AMAZON_FILE_CONTRATTO)){
					filename = autista.getAutistaDocumento().getNomeFileContratto();
				}
				if(objectModelFileName.equals(Constants.AMAZON_FILE_CONTRATTO_2)){
					filename = autista.getAutistaDocumento().getNomeFileContratto_2();
				}
				if(objectModelFileName.equals(Constants.AMAZON_FILE_DOCUMENTO_AGGIUNTIVO)){
					filename = autista.getAutistaDocumento().getDocumentoAggiuntivo();
				}
				ScaricaStreamFile(response, s3object, filename);
				
			}else{
				DownloadFile.Response404FileNonDisponibile(response);
			}
    	}catch (Exception ex) {
    		ex.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
	private void ScaricaStreamFile(HttpServletResponse response, S3Object s3object, String filename) throws IOException{
		try{
			byte[] bytes = IOUtils.toByteArray( s3object.getObjectContent() );
			ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
			baos.write(bytes, 0, bytes.length);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentLength(baos.size());
			OutputStream os;
			os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
			os.close();
		}catch(NullPointerException nP){
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
	
	@RequestMapping(value = "/checkFatturaSupplementoDisponibile", method = RequestMethod.POST)
	public void checkFatturaSupplementoDisponibile(HttpServletRequest request, HttpServletResponse response) {
		log.debug("checkFatturaSupplementoDisponibile");
		try{
			String idSupplemento = request.getParameter("idSupplemento");
			JSONObject fatturaDisponibile = new JSONObject();
			Supplementi supplemento = supplementiManager.get( Long.parseLong(idSupplemento) ) ;
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( supplemento.getRicercaTransfert().getId() );
			if( VerificaDownloadFattura( fattCorsa, supplemento )){
				fatturaDisponibile.put("fatturaDisponibile", true);
			}else{
				fatturaDisponibile.put("fatturaDisponibile", false);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(fatturaDisponibile.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	/**
	 * Scarico la fattura in PDF
	 * https://localhost:8443/apollon/pdfDownloadFatturaSupplemento?idSupplemento=1234
	 * @throws IOException 
	 */
	@RequestMapping(value = "/pdfDownloadFatturaSupplemento", method = RequestMethod.GET)
	public void pdfDownloadFatturaSupplemento(final HttpServletRequest request, final HttpServletResponse response, 
			@RequestParam(value = "idSupplemento", required = false) final String idSupplemento) throws IOException  {
		log.debug("DownloadFileController pdfDownloadFatturaSupplemento");
		try {
			Supplementi supplemento = supplementiManager.get( Long.parseLong(idSupplemento) ) ;
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( supplemento.getRicercaTransfert().getId() );
			if( VerificaDownloadFattura( fattCorsa, supplemento )){
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter writer = PdfWriter.getInstance (document, baos);
				writer.setPageEvent( new Footer_Header_Fattura() );
				document.open();
				GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
				fattPdf.Corpo_FatturaSupplemento(document, supplemento, request.getLocale());
				document.close();
				String filename = (ApplicationUtils.CheckDomainTranfertClienteVenditore(supplemento.getRicercaTransfert())) ?
						getText("webapp.ncctransferonline.name", request.getLocale()) +"_supplemento_"+ fattCorsa.getRicTransfert().getId()+".pdf" : 
							getText("webapp.apollotransfert.name", request.getLocale()) +"_supplemento_"+ fattCorsa.getRicTransfert().getId()+".pdf";
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				// setting the content type
				response.setContentType("application/pdf");
				// the contentlength
				response.setContentLength(baos.size());
				// write ByteArrayOutputStream to the ServletOutputStream
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			}else{
				//Fattura non disponibile
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (IOException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (Exception e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
	@RequestMapping(value = "/checkFatturaRitardoDisponibile", method = RequestMethod.POST)
	public void checkFatturaRitardoDisponibile(HttpServletRequest request, HttpServletResponse response) {
		log.debug("DownloadFileController checkFatturaRitardoDisponibile");
		try{
			String idcorsa = request.getParameter("idcorsa");
			JSONObject fatturaDisponibile = new JSONObject();
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( Long.parseLong(idcorsa) );
			Ritardi ritardo = ritardiManager.getRitardoBy_IdRicercaTransfert( Long.parseLong(idcorsa) ) ;
			if( VerificaDownloadFattura( fattCorsa, ritardo )){
				fatturaDisponibile.put("fatturaDisponibile", true);
			}else{
				fatturaDisponibile.put("fatturaDisponibile", false);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(fatturaDisponibile.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	/**
	 * Scarico la fattura in PDF
	 * https://localhost:8443/apollon/pdfDownloadFatturaRitardo?courseId=1234
	 * @throws IOException 
	 */
	@RequestMapping(value = "/pdfDownloadFatturaRitardo", method = RequestMethod.GET)
	public void pdfDownloadFatturaRitardo(final HttpServletRequest request, final HttpServletResponse response, 
			@RequestParam(value = "courseId", required = false) final String courseId) throws IOException  {
		log.debug("DownloadFileController pdfDownloadFatturaRitardo");
		try {
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( Long.parseLong(courseId) );
			Ritardi ritardo = ritardiManager.getRitardoBy_IdRicercaTransfert( Long.parseLong(courseId) ) ;
			if( VerificaDownloadFattura( fattCorsa, ritardo )){
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter writer = PdfWriter.getInstance (document, baos);
				writer.setPageEvent( new Footer_Header_Fattura() );
				document.open();
				GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
				fattPdf.Corpo_FatturaRitardo(document, ritardo, request.getLocale());
				document.close();
				String filename = (ApplicationUtils.CheckDomainTranfertClienteVenditore(ritardo.getRicercaTransfert())) ?
						getText("webapp.ncctransferonline.name", request.getLocale()) +"_ritardo_"+ fattCorsa.getRicTransfert().getId()+".pdf" : 
							getText("webapp.apollotransfert.name", request.getLocale()) +"_ritardo_"+ fattCorsa.getRicTransfert().getId()+".pdf";
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				// setting the content type
				response.setContentType("application/pdf");
				// the contentlength
				response.setContentLength(baos.size());
				// write ByteArrayOutputStream to the ServletOutputStream
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			}else{
				//Fattura non disponibile
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (IOException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (Exception e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
	private boolean VerificaDownloadFattura(BeanInfoFattura_Corsa fattCorsa, Object object){
		if(fattCorsa != null && fattCorsa.getRicTransfert() != null && fattCorsa.getRicTransfert().getUser() != null 
				&& (fattCorsa.getAgendaAutistaScelta() != null || fattCorsa.getAutista() != null || fattCorsa.getAutistaServizioMultiplo() != null ) && object != null){
			return true;
		}else{
			return false;
		}
	}
	
	@RequestMapping(value = "/checkFatturaDisponibile", method = RequestMethod.POST)
	public void checkFatturaDisponibile(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController checkFatturaDisponibile");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
			JSONObject fatturaDisponibile = new JSONObject();
			if(ricTransfert.getUser().getFirstName() == null || ricTransfert.getUser().getFirstName().equals("") ||
					ricTransfert.getUser().getLastName() == null || ricTransfert.getUser().getLastName().equals("")){
				String NomeClienteByProvider = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert).getNomeCliente();
				if(NomeClienteByProvider == null || NomeClienteByProvider.equals("")) {
					fatturaDisponibile.put("nomeCliente", false);
				}else {
					fatturaDisponibile.put("nomeCliente", true);
				}
			}else{
				fatturaDisponibile.put("nomeCliente", true);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(fatturaDisponibile.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	/**
	 * Scarico la fattura in PDF
	 * https://localhost:8443/apollon/pdfDownloadFattura?courseId=1234
	 * @throws IOException 
	 */
	@RequestMapping(value = "/pdfDownloadFattura", method = RequestMethod.GET)
	public void pdfDownloadFattura(final HttpServletRequest request, final HttpServletResponse response, 
			@RequestParam(value = "courseId", required = false) final String courseId) throws IOException {
		log.info("sono in pdfDownloadFattura");
		try {
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( Long.parseLong(courseId) );
			if(fattCorsa != null) {
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter writer = PdfWriter.getInstance(document, baos);
				writer.setPageEvent( new Footer_Header_Fattura() );
				document.open();
				String NomeFile = "";
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(courseId) );
				GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
				if(	(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) && ricTransfert.getAgendaAutista_RimborsoCliente() != null 
						&& ricTransfert.getAgendaAutista_RimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
					||
					(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) && ricTransfert.getRichiestaMediaScelta().getRimborsoCliente() != null 
						&& ricTransfert.getRichiestaMediaScelta().getRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
					|| 
					(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) && ricTransfert.getRichiestaAutistaParticolareAcquistato().getRimborsoCliente() != null 
						&& ricTransfert.getRichiestaAutistaParticolareAcquistato().getRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
					||
					(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) && ricTransfert.getRichiestaAutistaMultiploRimborsoCliente() != null 
						&& ricTransfert.getRichiestaAutistaMultiploRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
				){
					fattPdf.Corpo_FatturaCorsaRimborso(document, request.getLocale());
					NomeFile = "_rimborso_corsa_";
				}else{
					fattPdf.Corpo_FatturaCorsa(document, request.getLocale());
					NomeFile = "_corsa_";
				}
				document.close();
				String filename = (ApplicationUtils.CheckDomainTranfertClienteVenditore(ricTransfert)) ?
						getText("webapp.ncctransferonline.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf" :
							getText("webapp.apollotransfert.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf";
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				// setting the content type
				response.setContentType("application/pdf");
				// the contentlength
				response.setContentLength(baos.size());
				// write ByteArrayOutputStream to the ServletOutputStream
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			}else{
				//Fattura non disponibile
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (IOException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (Exception e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
	/**
	 * Scarico la fattura in PDF
	 * https://localhost:8443/apollon/pdfDownloadFattura?courseId=1234
	 * @throws IOException 
	 */
	@RequestMapping(value = "/pdfDownloadFatturaVenditoreTest", method = RequestMethod.GET)
	public void pdfDownloadFatturaVenditoreTest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		log.info("sono in pdfDownloadFatturaVenditoreTest");
		try {
			RicercaTransfert ricercaTransfert = ricercaTransfertManager.get(Constants.ID_RICERCA_TRANSFERT_TEST);
			RichiestaMedia richiestaMedia = new RichiestaMedia();
			richiestaMedia.setPrezzoCommissioneServizioIva(new BigDecimal(request.getParameter("prezzoCommissioneServizioIva")));
			richiestaMedia.setPrezzoTotaleCliente(new BigDecimal(request.getParameter("prezzoTotaleCliente")));
			richiestaMedia.setClasseAutoveicolo(classeAutoveicoloManager.get(Long.parseLong(request.getParameter("idClasseAutoveicolo"))));
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa_VenditoreTest(richiestaMedia, ricercaTransfert);
			if(fattCorsa != null){
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter writer = PdfWriter.getInstance(document, baos);
				writer.setPageEvent( new Footer_Header_Fattura() );
				document.open();
				String NomeFile = "";
				GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
				fattPdf.Corpo_FatturaCorsa(document, request.getLocale());
				NomeFile = "_corsa_";
				document.close();
				String filename = (ApplicationUtils.CheckAmbienteVenditore(request.getServletContext())) ?
						getText("webapp.ncctransferonline.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf" : 
							getText("webapp.apollotransfert.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf";
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				// setting the content type
				response.setContentType("application/pdf");
				// the contentlength
				response.setContentLength(baos.size());
				// write ByteArrayOutputStream to the ServletOutputStream
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			}else{
				//Fattura non disponibile
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (IOException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (Exception e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}

	/**
	 * Scarico la fattura in PDF
	 * https://localhost:8443/apollon/pdfDownloadFatturaCommercialista?fatturaId=1234
	 * @throws IOException 
	 * 
	 * Il commercialista deve scaricare anche la fattura originale prima del rimborso
	 */
	@RequestMapping(value = "/pdfDownloadFatturaCommercialista", method = RequestMethod.GET)
	public void pdfDownloadFatturaCommercialista(final HttpServletRequest request, final HttpServletResponse response, 
			@RequestParam(value = "fatturaId", required = false) final String fatturaId) throws IOException {
		log.info("sono in pdfDownloadFatturaCommercialista");
		try {
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa_Commercialista( Long.parseLong(fatturaId) );
			if(fattCorsa != null && (request.isUserInRole(Constants.COMMERCIALISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE))){
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter writer = PdfWriter.getInstance(document, baos);
				writer.setPageEvent( new Footer_Header_Fattura() );
				document.open();
				GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
				String NomeFile = "";
				if(fattCorsa.getRimborsoCliente() != null && new BigDecimal(fattCorsa.getRimborsoCliente()).compareTo(BigDecimal.ZERO) > 0){
					fattPdf.Corpo_FatturaCorsaRimborso(document, request.getLocale());
					NomeFile = "_rimborso_corsa_";
				}else{
					fattPdf.Corpo_FatturaCorsa(document, request.getLocale());
					NomeFile = "_corsa_";
				}
				document.close();
				String filename = (ApplicationUtils.CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
						getText("webapp.ncctransferonline.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf" :
							getText("webapp.apollotransfert.name", request.getLocale()) + NomeFile + fattCorsa.getRicTransfert().getId()+".pdf";
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				// setting the content type
				response.setContentType("application/pdf");
				// the contentlength
				response.setContentLength(baos.size());
				// write ByteArrayOutputStream to the ServletOutputStream
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			}else{
				//Fattura non disponibile
				DownloadFile.Response404FileNonDisponibile(response);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (IOException e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}catch (Exception e) {
			e.printStackTrace();
			DownloadFile.Response404FileNonDisponibile(response);
		}
	}
	
}
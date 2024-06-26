package com.apollon.webapp.util.email;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import com.apollon.Constants;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RegioniDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Province;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.home.HomeMarketingUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.TariffeTransferProvincieUtil;
import com.apollon.webapp.util.email.InviaEmail;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class EmailMarketing_AgenzieViaggi extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(EmailMarketing_AgenzieViaggi.class);
	private static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	public static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	private static String EMAIL_PROVA = "matteo.manili@gmail.com"; 
	
	/**
	 * - Invia blocchi di Email Pubblicitarie con le Teriffe, di grandezza definita: TotaleNumeroEmailDaInviare
	 * <br>- In ordine ciclico alle province disponibili, esempio: Una a Roma, Una a Napoli, Una Firenze... (e poi ricomincia da Roma)
	 * <br>- alle email della tabella AgenzieViaggioBit.
	 * <br>Se passo parametro 0 a TotaleNumeroEmailDaInviare mi invia un'unica email a matteo.manili@gmail.com
	 */
	public static List<String> inviaEmailMarketing_AgenzieViaggi_FieraMilano(Locale locale, VelocityEngine velocityEngine, final int TotaleNumeroEmailDaInviare) 
			throws InterruptedException, MessagingException, IOException {
		log.debug("inviaEmailMarketing_AgenzieViaggi_FieraMilano");
		List<String> IndirizziEmailInviate = new ArrayList<String>();
		try {
			List<String> SigleProvinciaList_AutistiDisponibiliList = TerritorioUtil.ProvinceAutistiDisponibili_SiglaProvincia();
			List<AgenzieViaggioBit> agenzieViaggioBitList;
			if(TotaleNumeroEmailDaInviare > 0) {
				agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzieViaggioBit_DESC();
			}else {
				agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzieViaggioBy_LIKE( EMAIL_PROVA );
			}
			String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
			List<String> descrizioneCategorieAutoList = AutoveicoloUtil.DammiDescrizioneCategorieAutoList(locale);
			AutistaTerritorio AutistiTerritorio = TerritorioUtil.ProvinciaAutistaList();
	        int EmailInviate = 0;
	    	while( true ){
	    		int contaEmailPerCicloProvince = 0;
	    		for(int cicloProv = 0; cicloProv < SigleProvinciaList_AutistiDisponibiliList.size(); cicloProv++){
	    			String SiglaProvinciaCoda = SigleProvinciaList_AutistiDisponibiliList.get(cicloProv);
			    	for(AgenzieViaggioBit agenzia_ite: agenzieViaggioBitList) {
						if( (TotaleNumeroEmailDaInviare == 0 && agenzia_ite.getEmail() != null && PrendiProvinciaDaIndirizzo(agenzia_ite.getCitta_e_indirizzo(), SiglaProvinciaCoda) != null)
							||
							(EmailInviate < TotaleNumeroEmailDaInviare && agenzia_ite.getEmail() != null && PrendiProvinciaDaIndirizzo(agenzia_ite.getCitta_e_indirizzo(), SiglaProvinciaCoda) != null && agenzia_ite.getNumeroEmailInviate() < 1 && !agenzia_ite.isUnsubscribe())
							){
							System.out.println("getCitta_e_indirizzo: "+PrendiProvinciaDaIndirizzo(agenzia_ite.getCitta_e_indirizzo(), SiglaProvinciaCoda));
			    			Province provincia = provinceDao.getProvinciaBy_SiglaProvincia( PrendiProvinciaDaIndirizzo(agenzia_ite.getCitta_e_indirizzo(), SiglaProvinciaCoda) );
					    	Map<String, Object> modelVelocity = new HashMap<String, Object>();
					    	// ATTRIBUTI PER EMAIL: Constants.VM_EMAIL_AGENZIA_VIAGGIO_FIERA_MILANO_CODICE_SCONTO
					    	modelVelocity.put("codiceSconto", agenzia_ite.getCodiceSconto());
					    	modelVelocity.put("ricercaTransfert", HomeMarketingUtil.CaricaInfoMarketingInfrastruttura(provincia.getUrl(), null));
					    	// ---
					    	// ATTRIBUTI PER EMAIL: Constants.VM_EMAIL_AGENZIA_VIAGGIO_FIERA_MILANO
					    	modelVelocity.put("descrizioneCategorieAutoList", descrizioneCategorieAutoList);
					    	modelVelocity.put("transferTariffeProvince", TariffeTransferProvincieUtil.DammiTransferTariffeProvince(
					    			MenuTariffeTransfer.TrasfrormaInOggetto(regioniDao.Menu_Lista_ProvinciaOrderByAbitanti_MaxResult(provincia.getId(), Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE))
					    			/*new ArrayList<Province>(){{add(provincia);}}*/, 
					    			locale, Parametri, null));
							String HttpsUrlPaginaTariffe = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)+"/"+Constants.PAGE_TARIFFE_TRANSFER;
							String urlPaginaTariffe = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale)+"/"+Constants.PAGE_TARIFFE_TRANSFER;
							String linkPaginaTariffe = "<a href=\""+HttpsUrlPaginaTariffe+"\">"+urlPaginaTariffe+"</a>";
							modelVelocity.put( "linkPaginaTariffe", linkPaginaTariffe);
							modelVelocity.put( "totaleAutisti", AutistiTerritorio.getTotaleAutisti());
							// --
							String HttpsUrlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale);
							String urlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale);
							String linkPaginaHome = "<a href=\""+HttpsUrlPaginaHome+"\">"+urlPaginaHome+"</a>";
							modelVelocity.put( "linkPaginaHome", linkPaginaHome);
							String urlHttpsUnsubscribe = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)+"/unsubscribe?email="+agenzia_ite.getEmail();
							String urlUnsubscribe = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale)+"/unsubscribe?email="+agenzia_ite.getEmail();
							String linkUnsubscribe = "<a href=\""+urlHttpsUnsubscribe+"\">"+urlUnsubscribe+"</a>";
							modelVelocity.put( "linkUnsubscribe" , linkUnsubscribe);
							EmailInviate += InviaEmailAgenziaViaggi(modelVelocity, agenzia_ite, velocityEngine, locale, provincia.getNomeProvincia(), IndirizziEmailInviate);
							contaEmailPerCicloProvince++;
							break;
						}
					}
	    		}
	    		if(contaEmailPerCicloProvince == 0 || TotaleNumeroEmailDaInviare == 0 ){
	    			break;
	    		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IndirizziEmailInviate;
	}
	
	
	/**
	 * prende quello che cè dentro la prima parentesi Aperta e Chiusa, cioè la sigla provincia es: "(BA) via degli alberi (FR) n. 89" - Risutato: "BA"
	 */
	private static String PrendiProvinciaDaIndirizzo(String Indirizzo, String SiglaProvinciaCoda){
		try {
			Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(Indirizzo); 
			if(m.find() && SiglaProvinciaCoda.equals(m.group(1)) ){
				return m.group(1);
			}else{
				return null;
			}
		}catch(NullPointerException nullExc) {
			return null;
		}catch(Exception exc) {
			System.out.println(exc.getMessage());
			return null;
		}
	}

	private static int InviaEmailAgenziaViaggi(Map<String, Object> modelVelocity, AgenzieViaggioBit agenzia_ite, VelocityEngine velocityEngine,
			Locale locale, String NomeProvincia, List<String> IndirizziEmailInviate) throws Exception {
		modelVelocity.put("nomeCognome", DammiNomeAzienda(agenzia_ite) );
		modelVelocity.put("webAppName", ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale));
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", null));
		
		boolean esito = false;
		try {
			esito = InviaEmail.InviaEmail_HTML_Other(agenzia_ite.getEmail(), agenzia_ite.getNome(), 
					Constants.VM_EMAIL_AGENZIA_VIAGGIO_FIERA, "ApolloTransfert | Diventa nostro Cliente", velocityEngine, 
					modelVelocity, null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
		}catch(IOException e) {
			log.debug("IOException: "+e.getMessage());
			esito = false;
		}catch(AddressException e) {
			log.debug("AddressException: "+e.getMessage());
			esito = true;
		}catch(SendFailedException e) {
			log.debug("SendFailedException: "+e.getMessage());
			esito = true;
		}catch(MessagingException e) {
			log.debug("MessagingException: "+e.getMessage());
			esito = true;
		}
		if( esito ){
			int numeroEmailInviate = agenzia_ite.getNumeroEmailInviate() + 1;
			agenzia_ite.setNumeroEmailInviate(numeroEmailInviate);
			agenzia_ite.setDataInvioLastEmail(new Date());
			agenzieViaggioBitDao.saveAgenzieViaggioBit(agenzia_ite);
			IndirizziEmailInviate.add( agenzia_ite.getEmail() );
			return 1;
		}else{
			return 0;
		}
	}
	
	private static String DammiNomeAzienda(AgenzieViaggioBit ite) {
		String risultato = "";
		if(ite.getNome() != null && !ite.getNome().equals("") && ite.getNome().length() <= 60 && !StringUtils.containsIgnoreCase(ite.getNome(), "home") 
				&& !StringUtils.containsIgnoreCase(ite.getNome(), "news") && !StringUtils.containsIgnoreCase(ite.getNome(), "Chi Siamo")  ) {
			risultato = ite.getNome();
		}else if(ite.getSitoWeb() != null && !ite.getSitoWeb().equals("")){
			try {
				URL url = new URL( ite.getSitoWeb() );
				risultato = url.getHost();

			}catch(java.net.MalformedURLException aa) {
				risultato = ite.getEmail();
			}

		}else if( ite.getNome() != null && !ite.getNome().equals("") ){
			risultato = ite.getNome();

		}else {
			risultato = ite.getEmail();
		}
		System.out.println("NOME: "+risultato +" | NOME OLD: "+ite.getNome() +" | SITOWEB: "+ite.getSitoWeb() +" | EMAIL: "+ite.getEmail());
		return risultato;
	}
	
	
}

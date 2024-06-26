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
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.RecensioneTransferUtil;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.controller.home.HomeMarketingUtil;
import com.apollon.webapp.util.email.InviaEmail;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class EmailMarketing_ClientiRecencioneSconto extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(EmailMarketing_ClientiRecencioneSconto.class);
	private static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	private static String USERNAME_PROVA = "admin"; 
	
	/**
	 * - Invia blocchi di Email Pubblicitarie con le Teriffe, di grandezza definita: TotaleNumeroEmailDaInviare
	 * <br>- In ordine ciclico alle province disponibili, esempio: Una a Roma, Una a Napoli, Una Firenze... (e poi ricomincia da Roma)
	 * <br>- alle email della tabella AgenzieViaggioBit.
	 * <br>Se passo parametro 0 a TotaleNumeroEmailDaInviare mi invia un'unica email a matteo.manili@gmail.com
	 */
	public static boolean inviaEmailMarketing_ClientiRecencioneSconto(Locale locale, VelocityEngine velocityEngine, final long idUser) 
			throws InterruptedException, MessagingException, IOException {
		log.debug("inviaEmailMarketing_ClientiRecencioneSconto");
		boolean esitoInvio = false;
		try {
			User user;
			if(idUser == 0) {
				user = (User) userDao.loadUserByUsername( USERNAME_PROVA ) ;
			}else {
				user = userDao.get(idUser);
			}
			System.out.println( user.getFullName() );
			System.out.println( user.getFullName()+" | https://localhost:8443/apollon/"+RecensioneTransferUtil.URL_PAGE_TOKEN_RECENSIONE+"="+user.getRecensioneTransfer().getUrlTockenPageScriviRecensone() 
					+" | sconto:"+ user.getRecensioneTransfer().getPercentualeSconto()+"%" );
			
			List<RicercaTransfert> listaTransfrAcquistati = user.getRecensioneTransfer().getRicercaTransfertList_Approvati();
			
			List<String> listaTransfrAcquistatiString = new ArrayList<String>();
			for(RicercaTransfert ite_ric: listaTransfrAcquistati) {
				String corsa = "Id Transfer: "+ ite_ric.getId() +" "+ ((ite_ric.isRitorno())?"(Andata e Ritorno)":"(Solo Andata)") 
						+" Giorno: "+ DateUtil.FORMATO_GIORNO_MESE_ANNO_ESTESO(locale).format(ite_ric.getDataOraPrelevamentoDate())+ " "
							+ite_ric.getPartenzaRequest() +" &#8594; " +ite_ric.getArrivoRequest();
				listaTransfrAcquistatiString.add(corsa);
				System.out.println( "corsa: "+corsa );
			}

			if(listaTransfrAcquistatiString != null && listaTransfrAcquistatiString.size() > 0) {
				
				Map<String, Object> modelVelocity = new HashMap<String, Object>();
				
				// nome e cognome
		    	modelVelocity.put("nomeCognome", (user.getFirstName() == null || user.getLastName() == null) ? user.getEmail() : user.getFullName() );
				
				// listaTransfrAcquistati
		    	modelVelocity.put("listaTransfrAcquistatiString", listaTransfrAcquistatiString ); // non lo uso
		    	
		    	// ultimoTransferAcquistato
		    	modelVelocity.put("ultimoTransferAcquistato", (listaTransfrAcquistatiString != null && listaTransfrAcquistatiString.size() > 0) 
		    			? listaTransfrAcquistatiString.get(0) : null );
				
		    	// pagina link token recensione
				String httpsUrlPaginaScriviRecensione = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)
						+"/"+Constants.PAGE_SCRIVI_RECENSIONE+"?"+RecensioneTransferUtil.URL_PAGE_TOKEN_RECENSIONE+"="+user.getRecensioneTransfer().getUrlTockenPageScriviRecensone();
				modelVelocity.put( "httpsUrlPaginaScriviRecensione", httpsUrlPaginaScriviRecensione);
				
				// codice sconto e percentuale
				modelVelocity.put("codiceSconto", user.getRecensioneTransfer().getCodiceSconto() );
				
				// codice sconto e percentuale
				modelVelocity.put("percentualeSconto", user.getRecensioneTransfer().getPercentualeSconto() );
		    	
		    	// esempi transfer (ultimo transfer acquisto approvato)
				Province provincia = provinceDao.getProvinciaBy_SiglaProvincia( (listaTransfrAcquistatiString != null && listaTransfrAcquistatiString.size() > 0) 
						? listaTransfrAcquistati.get(0).getSiglaProvicia_Partenza() : null );
				
		    	modelVelocity.put("ricercaTransfert", (provincia != null) ? HomeMarketingUtil.CaricaInfoMarketingInfrastruttura(provincia.getUrl(), null) : null);
		    	
		    	// numero autisti
		    	modelVelocity.put( "totaleAutisti", TerritorioUtil.ProvinciaAutistaList().getTotaleAutisti());
		    	
		    	// homepage
				String HttpsUrlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale);
				String urlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale);
				String linkPaginaHome = "<a href=\""+HttpsUrlPaginaHome+"\">"+urlPaginaHome+"</a>";
				modelVelocity.put( "linkPaginaHome", linkPaginaHome);
		    	
		    	// pagina tariffe 
				String HttpsUrlPaginaTariffe = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)+"/"+Constants.PAGE_TARIFFE_TRANSFER;
				String urlPaginaTariffe = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale)+"/"+Constants.PAGE_TARIFFE_TRANSFER;
				String linkPaginaTariffe = "<a href=\""+HttpsUrlPaginaTariffe+"\">"+urlPaginaTariffe+"</a>";
				modelVelocity.put( "linkPaginaTariffe", linkPaginaTariffe);
	
				// unsubscribe
				String urlHttpsUnsubscribe = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)+"/unsubscribe?email="+user.getEmail();
				String urlUnsubscribe = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale)+"/unsubscribe?email="+user.getEmail();
				String linkUnsubscribe = "<a href=\""+urlHttpsUnsubscribe+"\">"+urlUnsubscribe+"</a>";
				modelVelocity.put( "linkUnsubscribe" , linkUnsubscribe);
				
				esitoInvio = InviaEmailRecensioneTransfer(modelVelocity, user, velocityEngine, locale);
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return esitoInvio;
	}
	
	
	private static boolean InviaEmailRecensioneTransfer(Map<String, Object> modelVelocity, User user, VelocityEngine velocityEngine, Locale locale) throws Exception {
		modelVelocity.put("webAppName", ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale));
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", null));
		boolean esitoInvio = false;
		try {
			esitoInvio = InviaEmail.InviaEmail_HTML_Other(user.getEmail(), user.getFullName(), 
					Constants.VM_EMAIL_RECENSIONE_TRANSFER_CLIENTE_CODICE_SCONTO, "ApolloTransfert | Offerta Sconto Recensione Transfer", velocityEngine, 
					modelVelocity, null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
		}catch(IOException e) {
			log.debug("IOException: "+e.getMessage());
		}catch(AddressException e) {
			log.debug("AddressException: "+e.getMessage());
		}catch(SendFailedException e) {
			log.debug("SendFailedException: "+e.getMessage());
		}catch(MessagingException e) {
			log.debug("MessagingException: "+e.getMessage());

		}
		return esitoInvio;
	}
	
	
	
	
}

package com.apollon.webapp.util.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import com.apollon.Constants;
import com.apollon.dao.ComunicazioniDao;
import com.apollon.dao.ComunicazioniUserDao;
import com.apollon.model.Autista;
import com.apollon.model.Comunicazioni;
import com.apollon.model.ComunicazioniUser;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class EmailMarketing_Autisti_Comunicazioni extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(EmailMarketing_Autisti_Comunicazioni.class);
	private static ComunicazioniDao comunicazioniDao = (ComunicazioniDao) contextDao.getBean("ComunicazioniDao");
	private static ComunicazioniUserDao comunicazioniUserDao = (ComunicazioniUserDao) contextDao.getBean("ComunicazioniUserDao");
	private final static long ID_USER_AUTISTA_PROVA = -2l; // Matteo Manili 
	
	/**
	 * - Invia blocchi di Email, di grandezza definita: TotaleNumeroEmailDaInviare
	 * <br> Invia email dalle 09:00 alle 21:00
	 * <br> Se passo parametro 0 a TotaleNumeroEmailDaInviare mi invia un'unica email a matteo.manili@gmail.com
	 */
	public static List<String> inviaEmailMarketing_Autisti(Locale locale, VelocityEngine velocityEngine, 
			final int TotaleNumeroEmailDaInviare, final String TemplateEmailComunicazione) throws InterruptedException, MessagingException, IOException {
		log.debug("inviaEmailMarketing_Autisti"); 
		List<String> IndirizziEmailInviate = new ArrayList<String>();
		try {
			List<Autista> autistaList = new ArrayList<Autista>();
			if(TotaleNumeroEmailDaInviare > 0) {
				autistaList = autistaDao.ListAutista_Approvati();
				/*
				for(Autista ite : autistaList) {
					System.out.println("autista: "+ ite.getId() +" | "+ite.getUser().getEmail() );
				}
				*/
			}else {
				Autista autistaProva = autistaDao.getAutistaByUser( ID_USER_AUTISTA_PROVA );
				autistaList.add(autistaProva);
			}
	        int EmailInviate = 0;
	        while( true ) {
	    		int contaEmail = 0;
		    	for(Autista autista_ite: autistaList) {
		    		ComunicazioniUser comunicazioniUser = comunicazioniUserDao.ComunicazioneUser_By_Comunicazione_e_User(TemplateEmailComunicazione, autista_ite.getUser().getId()); 
		    		int oraAttuale = Integer.parseInt( DateUtil.FORMATO_ORA_SOLO_ORA.format(new Date()) );
		    		if( (TotaleNumeroEmailDaInviare == 0 )
							||
						(EmailInviate < TotaleNumeroEmailDaInviare && (comunicazioniUser == null || comunicazioniUser.getNumEmailInviate() < 1) 
								&& ( oraAttuale >= 9 && oraAttuale <= 21 ) // invia le email dalle 09:00 alle 21:00
								// && ( autista_ite.getUser().getId().longValue() == ID_USER_AUTISTA_PROVA || autista_ite.getUser().getId() == -3l )
						)
					){
						System.out.println("nomeCognome: "+autista_ite.getUser().getFullName()+" Email: "+autista_ite.getUser().getEmail()
								+" Tel: "+autista_ite.getUser().getPhoneNumber() );
				    	Map<String, Object> modelVelocity = new HashMap<String, Object>();
						EmailInviate += InviaEmail_Autista(modelVelocity, autista_ite, TemplateEmailComunicazione, velocityEngine, locale, IndirizziEmailInviate);
						contaEmail++;
						break;
					}
		    	}
		    	if(contaEmail == 0 || TotaleNumeroEmailDaInviare == 0 ){
	    			break;
	    		}
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return IndirizziEmailInviate;
	}
	
	
	private static int InviaEmail_Autista(Map<String, Object> modelVelocity, Autista autista_ite, String TemplateEmailComunicazione, 
			VelocityEngine velocityEngine, Locale locale, List<String> IndirizziEmailInviate) throws Exception {
		modelVelocity.put("nomeCognome", autista_ite.getUser().getFullName());
		modelVelocity.put("webAppName", ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale));
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", null));
		
		String HttpsUrlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale)+"/"+Constants.URL_AGENDA_AUTISTA;
		String urlPaginaHome = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale)+"/"+Constants.URL_AGENDA_AUTISTA;
		String linkPaginaHome = "<a href=\""+HttpsUrlPaginaHome+"\">"+urlPaginaHome+"</a>";
		modelVelocity.put( "linkPagina_AgendaAutista", linkPaginaHome);
		
		boolean esito = false;
		try {
			esito = InviaEmail.InviaEmail_HTML_Other(autista_ite.getUser().getEmail(), autista_ite.getUser().getFullName(), 
					TemplateEmailComunicazione, ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale)+" | Agenda-Tariffario Autista", 
					velocityEngine, modelVelocity, null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
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
			Comunicazioni comunicazione = comunicazioniDao.Counicazione_By_Comunicazione( TemplateEmailComunicazione );
			if( comunicazione == null ) {
				// creo una nuova Comunicazione
				Comunicazioni comunicazioni = new Comunicazioni();
				comunicazioni.setComunicazione(TemplateEmailComunicazione);
				comunicazione = comunicazioniDao.saveComunicazioni(comunicazioni);
			}
			ComunicazioniUser comunicazioniUser = comunicazioniUserDao.ComunicazioneUser_By_Comunicazione_e_User(TemplateEmailComunicazione, autista_ite.getUser().getId());
			if( comunicazioniUser != null ) {
				comunicazioniUser.setNumEmailInviate( comunicazioniUser.getNumEmailInviate() + 1 );
				comunicazioniUser.setDataInvioLastEmail( new Date() );
				comunicazioniUserDao.saveComunicazioniUser(comunicazioniUser);
			}else {
				// Salvo la Comunicazione - User
				comunicazioniUser = new ComunicazioniUser();   
				comunicazioniUser.setComunicazioni(comunicazione);
				comunicazioniUser.setUser(autista_ite.getUser());
				comunicazioniUser.setNumEmailInviate( 1 );
				comunicazioniUser.setDataInvioLastEmail( new Date() );
				comunicazioniUserDao.saveComunicazioniUser(comunicazioniUser);
			}
			IndirizziEmailInviate.add( autista_ite.getUser().getEmail() );
			
			String testoSms = "Salve "+autista_ite.getUser().getFullName()+", Video Funzionamento AGENDA AUTISTA www.youtube.com/playlist?list=PLQ1aJ0zetbCIbZKtChcEeTW8c14MHMleI "
					+ "Ti abbiamo inviato una email a "+autista_ite.getUser().getEmail() +" con maggiori informazioni. "
					+ "link Agenda Autista "+urlPaginaHome;
			InvioSms.Crea_SMS_Gateway(autista_ite.getUser().getPhoneNumber(), testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
			
			return 1;
		}else{
			return 0;
		}
	}
	

	
	
}

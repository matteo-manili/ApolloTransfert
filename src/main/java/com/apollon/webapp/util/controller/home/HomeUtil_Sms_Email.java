package com.apollon.webapp.util.controller.home;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import com.apollon.Constants;
import com.apollon.dao.ComuniDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.util.DateUtil;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.HttpServletRequest_Util;
import com.apollon.webapp.util.OperazioniThread_B;
import com.apollon.webapp.util.ApplicationUtils.ApplicationMessagesUtil;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class HomeUtil_Sms_Email extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(HomeUtil_Sms_Email.class);
	private static  GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static  RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	private static  RichiestaAutistaMedioDao richiestaAutistaMedioDao = (RichiestaAutistaMedioDao) contextDao.getBean("RichiestaAutistaMedioDao");
	private static 	ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");


	/**
	 * Security_InviaSmsEmailPreventivoAutistaInviato_Cliente
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static void Security_InviaSmsEmailPreventivoAutistaInviato_Cliente(RichiestaAutistaParticolare richAutistPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException{
		String telefonoDestinatario = richAutistPart.getRicercaTransfert().getPhoneNumberCustomer();
		String testoSms = TestoSmsPreventivoInviatoAutista_Cliente(request.getLocale(), richAutistPart);
		InvioSms.Crea_SMS_Gateway(richAutistPart.getRicercaTransfert(), telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
		log.debug("testoSms: "+testoSms );
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richAutistPart.getRicercaTransfert().getRicTransfert_Email()) ){
			InviaEmail.InviaEmailPreventivoAutistaInviato_Cliente(richAutistPart, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailPreventivoAutistaInviato_Cliente: "+richAutistPart.getRicercaTransfert().getRicTransfert_Email());
        }
	}
	
	/**
	 * Security_InviaEmailListaAutistiPreventivoCliente
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static void Security_InviaEmailListaAutistiPreventivoCliente(RicercaTransfert ricercaTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(ricercaTransfert.getRicTransfert_Email()) ){
			InviaEmail.InviaEmailListaAutistiPreventivoCliente(ricercaTransfert, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailListaAutistiPreventivoCliente: "+ricercaTransfert.getRicTransfert_Email());
        }
	}
	
	
	public static void InviaSmsEmailPreventiviAutisti(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		HttpServletRequest_Util httpServletRequest_Util = new HttpServletRequest_Util(request.getServerName(), request.getLocale(), request.getServletContext()); 
		OperazioniThread_B run = new OperazioniThread_B(ricTransfert, httpServletRequest_Util, velocityEngine);
		new Thread(run).start();
	}
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static void Security_InviaEmailRichiestaPreventivoAutista(RichiestaAutistaParticolare richAutPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richAutPart.getAutoveicolo().getAutista().getUser().getEmail()) ){
			InviaEmail.InviaEmailRichiestaPreventivoAutista(richAutPart, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailRichiestaPreventivoAutista: "+richAutPart.getAutoveicolo().getAutista().getUser().getFullName());
        }
	}
	
	/**
	 * INVIO SMS ed EMAIL ALL'AUTISTA CHE CONFERMA L'ACQUISTO DEL PREVENTIVO DA PARTE DEL CLIENTE 
	 * @param richAutPart
	 * @param request
	 * @throws Exception
	 */
	public static void InviaEmailRichiestaPreventivoAcquistato_Autista(RichiestaAutistaParticolare richAutPart, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		log.debug("inviaSmsCorsaConfermataPart");
		if( richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
			EseguiOperazioni_InviaEmailRichiestaPreventivoAcquistato_Autista(richAutPart, request, velocityEngine);
		}else if( richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
			for(RichiestaAutistaParticolare ite: richAutPart.getRichiestaAutistaParticolareMultiploList()) {
				EseguiOperazioni_InviaEmailRichiestaPreventivoAcquistato_Autista(ite, request, velocityEngine);
			}
		}
    }
	
	private static String Dammi_ComuneParteza_ComuneArrivo_TotKilometri(RicercaTransfert ricTransfert, String MarcaModelloAutoTarga) {
		long totaleMetri_Andata_e_Ritorno = ricTransfert.getDistanzaValue() + ricTransfert.getDistanzaValueRitorno();
		int kilometri = (int)(long)(totaleMetri_Andata_e_Ritorno/1000);
		String DataoraAndata = DateUtil.FORMATO_DATA_ORA.format(ricTransfert.getDataOraPrelevamentoDate());
		String DataoraRitorno = (ricTransfert.getDataOraRitornoDate() != null ? DateUtil.FORMATO_DATA_ORA.format(ricTransfert.getDataOraRitornoDate()) : null);
		return "- "+ricTransfert.getComune_Partenza()+" -> "+ricTransfert.getComune_Arrivo()
			+" "+(ricTransfert.isRitorno() ? "ANDATA "+DataoraAndata+ " RITORNO "+DataoraRitorno : "SOLO ANDATA "+DataoraAndata)
			+" "+kilometri+"km"+ (MarcaModelloAutoTarga != null ? " Auto: "+MarcaModelloAutoTarga : "")+" -";
	}
	
	/**
	 * INVIO SMS ed EMAIL ALL'AUTISTA CHE CONFERMA L'ACQUISTO DEL PREVENTIVO DA PARTE DEL CLIENTE 
	 * @param richAutPart
	 * @param request
	 * @throws Exception
	 */
	private static void EseguiOperazioni_InviaEmailRichiestaPreventivoAcquistato_Autista(RichiestaAutistaParticolare richAutPart, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		String telefonoDestinatario = richAutPart.getAutoveicolo().getAutista().getUser().getPhoneNumber();
    	String linkVerificaSms = request.getServerName() + request.getContextPath() + "/"+Constants.URL_PREVENTIVO_CORSA+"?token="+richAutPart.getToken();
        String testoSms = "Salve "+richAutPart.getAutoveicolo().getAutista().getUser().getFirstName() + 
        		" Il Preventivo "+richAutPart.getRicercaTransfert().getId()+" è stato Acquistato al PREZZO di "+ richAutPart.getPrezzoTotaleAutista() +" euro fai click qui per i dettagli "+linkVerificaSms;
        String result = "";
        log.debug(testoSms);
        long invioSmsAbilitato = gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber();
        if(invioSmsAbilitato == 1l){
	        while( !richAutPart.isInvioSmsCorsaConfermata() ){
	        	result = InvioSms.Crea_SMS_Gateway(telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
	        	if(result.equals(Constants.SMS_STATUS_SUCCESS)){
	        		richAutPart.setInvioSmsCorsaConfermata(true);
	        		richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare( richAutPart );
	        	}
	        }
        }else{
        	richAutPart.setInvioSmsCorsaConfermata(true);
        	richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare( richAutPart );
        }
        // INVIO EMAIL
        Security_InviaEmailRichiestaPreventivoAcquistato_Autista(richAutPart, request, velocityEngine);
		log.debug("....INVIO UN MESSAGGIO AUTISTA PREVENTIVO....");
	}
	
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static void Security_InviaEmailRichiestaPreventivoAcquistato_Autista(RichiestaAutistaParticolare richAutPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richAutPart.getAutoveicolo().getAutista().getUser().getEmail()) ){
			InviaEmail.InviaEmailRichiestaPreventivoAcquistato_Autista(richAutPart, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailRichiestaPreventivoAutista: "+richAutPart.getAutoveicolo().getAutista().getUser().getFullName());
        }
	}
	
	/**
	 * Controllo Sicurezza invio email per non inviare email ad altre persone quando faccio i test su localhost
	 * @throws Exception 
	 */
	public static void Security_InviaEmailCorsaAcquistata_Cliente(HttpServletRequest request, RicercaTransfert ricTransfert, VelocityEngine velocityEngine) throws Exception {
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(ricTransfert.getUser().getEmail()) ){
			InviaEmail.InviaEmailCorsaAcquistata_Cliente(ricTransfert, request, velocityEngine, true);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailCorsaAcquistata_Cliente: "+ricTransfert.getUser().getFullName());
        }
		
		// INVIO EMAIL AD Alessandro Faraci: faraci.a87@gmail.com | email.matteo.manili.gmail | email.alessandro.faraci
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(ApplicationMessagesUtil.DammiMessageSource("email.alessandro.faraci")) ){
			long idRegioneLombardia = 19l; //solo lombardia
			if( provinceDao.getProvinciaBy_SiglaProvincia(ricTransfert.getSiglaProvicia_Partenza()).getRegioni().getId() == idRegioneLombardia
					|| provinceDao.getProvinciaBy_SiglaProvincia(ricTransfert.getSiglaProvicia_Arrivo()).getRegioni().getId() == idRegioneLombardia) {
				InviaEmail.InviaEmailCorsaAcquistata_Cliente(ricTransfert, ApplicationMessagesUtil.DammiMessageSource("email.alessandro.faraci"), "Alessandro Faraci", request, velocityEngine, false);
			}
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailCorsaAcquistata_Cliente: "+ApplicationMessagesUtil.DammiMessageSource("email.alessandro.faraci"));
        }
		
		// INVIO LA EMAIL AL VENDITORE DI CORSA VENDUTA !!
		if( ricTransfert.getUserVenditore() != null && ricTransfert.getUserVenditore().getEmail() != null && (ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || 
				CheckEmailTesting(ricTransfert.getUserVenditore().getEmail())) ){
			InviaEmail.InviaEmailCorsaVenduta_Venditore(ricTransfert, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailCorsaVenduta_Venditore: "+ricTransfert.getUser().getFullName());
        }
	}
	
	
	/**
	 * INVIO EMAIL E SMS ALL'AUTITA A CUI è STATA COMPRATA LA CORSA ATTRAVERSO IL TARIFFARIO
	 */
	public static void Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Sms_Email_CorsaConfermataAgendaAutista");
		AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
		if( agendaAutistaScelta.getAgendaAutista_AutistaAndata() != null ) {
			Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista(agendaAutistaScelta.getAgendaAutista_AutistaAndata(), request, velocityEngine);
		}
		if( agendaAutistaScelta.getAgendaAutista_AutistaRitorno() != null ) {
			Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista(agendaAutistaScelta.getAgendaAutista_AutistaRitorno(), request, velocityEngine);
		}
    }
	

	private static void Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista(AgendaAutista_Autista agendaAutista, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Sms_Email_CorsaConfermataAgendaAutistaAndata");
    	String telefonoDestinatario = agendaAutista.getPhoneNumber();
    	String linkVerificaSms = request.getServerName() + request.getContextPath()+"/"+Constants.URL_CORSA_AGENDA_AUTISTA+"?idTariffario="+agendaAutista.getIdTariffario();
    	String testoSms = "Salve "+agendaAutista.getFirstName() +" La Corsa "+agendaAutista.getRicercaTransfert().getId()
    			+" è stata acquistata dalla tua AGENDA al prezzo di "+agendaAutista.getPrezzoCorsa()+" euro fai click qui per i dettagli "+linkVerificaSms;
        String result = InvioSms.Crea_SMS_Gateway(telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
        Security_Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista_Check(agendaAutista, request, velocityEngine);
    }
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws Exception 
	 */
	private static void Security_Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista_Check(AgendaAutista_Autista agendaAutista, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(agendaAutista.getEmail()) ){
			InviaEmail.InviaEmailAgendaAutistaCorsaAcquistata_Autista(agendaAutista, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA Security_Invia_Sms_Email_AgendaAutistaCorsaAcquistata_AutistaAndata_Check: "+agendaAutista.getFullName());
        }
	}
	
	
	
	
	public static void Invia_Sms_Email_CorsaConfermataMedio(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Sms_Email_CorsaConfermataMedio");
    	String telefonoDestinatario = richAutMedio.getAutista().getUser().getPhoneNumber();
    	String linkVerificaSms = request.getServerName() + request.getContextPath()+"/"+Constants.URL_PRENOTA_CORSA+"?token="+richAutMedio.getTokenAutista();
        String testoSms = "Salve "+richAutMedio.getAutista().getUser().getFirstName() +" La Corsa "+richAutMedio.getRichiestaMedia().getRicercaTransfert().getId()
        		+" è stata CONFERMATA al PREZZO di " + richAutMedio.getPrezzoTotaleAutista() + " euro fai click qui per i dettagli "+linkVerificaSms;
        String result = "";
        boolean InvioSmsConfermato = false;
        while( InvioSmsConfermato == false ){
        	result = InvioSms.Crea_SMS_Gateway(telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
        	if(result.equals(Constants.SMS_STATUS_SUCCESS)){
        		richAutMedio.setInvioSmsCorsaConfermata(true);
        		InvioSmsConfermato = true ;
        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richAutMedio );
        		
        	}else if( !ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ){
        		// setto cmq. a true per fare i test
        		richAutMedio.setInvioSmsCorsaConfermata(true);
        		InvioSmsConfermato = true ;
        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richAutMedio );
        	}
        }
        Security_Invia_Sms_Email_CorsaConfermataMedio_Check(richAutMedio, request, velocityEngine);
    }
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws Exception 
	 */
	private static void Security_Invia_Sms_Email_CorsaConfermataMedio_Check(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richAutMedio.getAutista().getUser().getEmail()) ){
			InviaEmail.InviaEmailCorsaConfermataAutista(richAutMedio, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA Invia_Sms_Email_CorsaConfermataMedio_Check: "+richAutMedio.getAutista().getUser().getFullName());
        }
	}
	
	/**
	 * Inivio Email avviso a Cliente che un autista ha preso in carico la corsa
	 * (serve a fidelizzare e rassicurare il cliente)
	 */
	public static void Invia_Email_AvvisoCorsaConfermataCliente(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Email_AvvisoCorsaConfermataCliente");
		Security_Invia_Email_AvvisoCorsaConfermataCliente_Check(richAutMedio, request, velocityEngine);
    }
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws Exception 
	 */
	private static void Security_Invia_Email_AvvisoCorsaConfermataCliente_Check(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || 
				CheckEmailTesting(richAutMedio.getRichiestaMedia().getRicercaTransfert().getUser().getEmail()) ){
			InviaEmail.Invia_Email_AvvisoCorsaConfermataCliente(richAutMedio, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA Invia_Email_AvvisoCorsaConfermataCliente_Check: "+richAutMedio.getAutista().getUser().getFullName());
        }
	}
	
	public static String TestoSmsPreventivoInviatoAutista_Cliente(Locale locale, RichiestaAutistaParticolare richiestaAutistaParticolare){
		return "Salve "+richiestaAutistaParticolare.getRicercaTransfert().getRicTransfert_Nome() +" un Autista le ha inviato il Preventivo per la Corsa "+richiestaAutistaParticolare.getRicercaTransfert().getId()
				+" al prezzo di "+richiestaAutistaParticolare.getPrezzoTotaleCliente()+" euro fai click qui per vedere i Preventivi "+UrlSmsListaPreventivi_Cliente(locale, richiestaAutistaParticolare);
	}
	
	public static String UrlSmsListaPreventivi_Cliente(Locale locale, RichiestaAutistaParticolare richiestaAutistaParticolare) {
		return ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale) 
				+"/"+Constants.URL_LISTA_PREVENTIVI_CLIENTE+"?courseId="+richiestaAutistaParticolare.getRicercaTransfert().getId()
					+"&tokenRicTransfert="+richiestaAutistaParticolare.getRicercaTransfert().getRicTransfert_Token();
	}
	
	public static String TestoSmsRichiestaPreventivo_Autista(Locale locale, RichiestaAutistaParticolare richAutistaPart){
		return "Salve "+richAutistaPart.getAutoveicolo().getAutista().getUser().getFullName()
				+" Un Cliente richiede il Preventivo per la Corsa"+" "+richAutistaPart.getRicercaTransfert().getId()
				+" "+Dammi_ComuneParteza_ComuneArrivo_TotKilometri(richAutistaPart.getRicercaTransfert(), richAutistaPart.getAutoveicolo().getMarcaModelloTarga())
				+" fai click qui per inviare il preventivo "+UrlSmsRichiestaPreventivo_Autista(locale, richAutistaPart);
	}
	
	public static String UrlSmsRichiestaPreventivo_Autista(Locale locale, RichiestaAutistaParticolare richiestaAutistaParticolare) {
		return ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale) 
				+"/"+Constants.URL_PREVENTIVO_CORSA+"?token="+richiestaAutistaParticolare.getToken();
	}
	
	public static String TestoSmsCorsaDisponbile_Autista(Locale locale, RichiestaMediaAutista richAutistMed){
		return "Salve "+richAutistMed.getAutista().getUser().getFullName() +" è disponibile la Corsa "+richAutistMed.getRichiestaMedia().getRicercaTransfert().getId()
				+" "+Dammi_ComuneParteza_ComuneArrivo_TotKilometri(richAutistMed.getRichiestaMedia().getRicercaTransfert(), null)
				+" al PREZZO di "+richAutistMed.getPrezzoTotaleAutista()+" euro fai click qui per confermarla "+UrlSmsCorsaDisponibileAutista(locale, richAutistMed);
	}
	
	public static String UrlSmsCorsaDisponibileAutista(Locale locale, RichiestaMediaAutista richMediaAutista) {
		return ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale) 
				+"/"+Constants.URL_PRENOTA_CORSA+"?token="+richMediaAutista.getTokenAutista();
	}
	
	public static void InviaSmsAvvisoMatteoCorsaVenduta(HttpServletRequest request, RicercaTransfert ricTransfert, String prezzoCommissioneApollo) throws UnknownHostException {
		String linkCorse = request.getServerName() + request.getContextPath() + "/admin/admin-gestioneCorse"; 
		String testoSms = "Corsa Venduta!!! Commissione: "+prezzoCommissioneApollo+" euro ID: "+ricTransfert.getId()+" "+linkCorse;
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ) {
			InvioSms.Crea_SMS_Gateway(ricTransfert, ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo", request.getLocale()), testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_MATTEO_CORSA_VENDUTA);
		}
	}
	
	public static void Invia_Sms_Email_CorsaDisponibileAutistiMedio(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		
		HttpServletRequest_Util httpServletRequest_Util = new HttpServletRequest_Util(request.getServerName(), request.getLocale(), request.getServletContext()); 
		OperazioniThread_B runn = new OperazioniThread_B(ricTransfert, httpServletRequest_Util, velocityEngine);
		new Thread(runn).start();
	}
	
	public static void Invia_Sms_Email_CorsaDisponibileAutistiMedioAdmin(RicercaTransfert ricTransfert, Long idAutista, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Sms_Email_CorsaDisponibileAutistiMedioAdmin");
		// PASSO UNA LISTA MA IN PRATICA è SEMPRE UN SOLO AUTISTA
		List<RichiestaMediaAutista> richiestaMediaAutista = richiestaAutistaMedioDao.getRichiestaAutista_by_RichiestaMediaScelta_Autista(ricTransfert.getId(), idAutista);
		boolean InvioSmsConfermato = false;
		for(RichiestaMediaAutista richiestaAutistaMedioIte: richiestaMediaAutista){
			while( InvioSmsConfermato == false ){ // ripeti il ciclo fino quando InvioSmsConfermato diventa true
				String telefonoDestinatario = richiestaAutistaMedioIte.getAutista().getUser().getPhoneNumber();
				String testoSms = TestoSmsCorsaDisponbile_Autista(request.getLocale(), richiestaAutistaMedioIte);
				// INVIO SMS
				String result = InvioSms.Crea_SMS_Gateway(telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
	        	if(result.equals(Constants.SMS_STATUS_SUCCESS)){
	        		InvioSmsConfermato = true ;
	        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richiestaAutistaMedioIte );
	        		
	        	}else if( !ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ){
	        		// setto cmq. a true per fare i test
	        		InvioSmsConfermato = true ;
	        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richiestaAutistaMedioIte );
	        	}
			}
			// INVIO EMAIL
			Security_InviaEmailCorsaDisponibile(richiestaAutistaMedioIte, request, velocityEngine);
		}
	}
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static void Security_InviaEmailCorsaDisponibile(RichiestaMediaAutista richiestaAutistaMedioIte, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richiestaAutistaMedioIte.getAutista().getUser().getEmail()) ){
			InviaEmail.InviaEmailCorsaDisponibileAutista(richiestaAutistaMedioIte, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailCorsaDisponibileAutista: "+richiestaAutistaMedioIte.getAutista().getUser().getFullName());
        }
	}
	

}

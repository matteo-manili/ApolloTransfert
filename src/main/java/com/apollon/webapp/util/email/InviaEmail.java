package com.apollon.webapp.util.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.context.NoSuchMessageException;
import org.springframework.ui.velocity.VelocityEngineUtils;
import com.apollon.Constants;
import com.apollon.dao.AutistaDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.model.User;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.Tariffe_AutoveicoloTariffa;
import com.apollon.webapp.util.bean.Tariffe_Zone;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultMedio;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;
import com.apollon.webapp.util.fatturazione.BeanInfoFattura_Corsa;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf.Footer_Header_Fattura;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Questa classe funziona solo se l'applicazione sta girando su server, altrimenti non manda le email
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class InviaEmail extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(InviaEmail.class);
	
	public static AutistaDao autistaDao = (AutistaDao) contextDao.getBean("AutistaDao");
	private static final String OggettoCorsaAcquistata = "Corsa Acquistata";
	private static final String OggettoCorsaVenduta = "Corsa Venduta";
	
	/**
	 * invio email da form Contatti e la spedisco a info@apollotransfert.com
	 * @throws IOException 
	 * @throws MessagingException 
	 * @throws NoSuchMessageException 
	 */
	public static void InviaEmailContatti(String NomeMittente, String EmailMittente, String textMessaggio, HttpServletRequest request, 
			VelocityEngine velocityEngine) throws NoSuchMessageException, SendFailedException, AddressException, MessagingException, IOException {
		final Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("NomeMittente", NomeMittente);
        modelVelocity.put("EmailMittente", EmailMittente);
        modelVelocity.put("textMessaggio", textMessaggio);
        String FromDomain = (CheckAmbienteVenditore(request.getServletContext())) ? 
        		ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", request.getLocale()) :
        			ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", request.getLocale());
        InviaEmail_Contatti_HTML(Constants.VM_EMAIL_CONTATTI, "Da "+ FromDomain +" | "+NomeMittente+", "+EmailMittente, 
        		NomeMittente, EmailMittente, velocityEngine, modelVelocity, 
        		(CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT);
    }
	
	public static void sendUserEmailPasswordRecovery(final User user, final String url, VelocityEngine velocityEngine, HttpServletRequest request) 
			throws NoSuchMessageException, Exception {
        Locale locale = request.getLocale();
        final Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("user", user);
        modelVelocity.put("applicationURL", url);
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put("locale", request.getLocale());
        modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
    	if( !user.getEmail().contains(Constants.FAKE_EMAIL) ){
    		InviaEmail_HTML_User(user, Constants.VM_EMAL_PASSWORD_RECOVERY, ApplicationMessagesUtil.DammiMessageSource("email.password.recovery.subject", locale), 
    				velocityEngine, modelVelocity, null, null, false, (CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    	}else{
    		//email non inviata perke l'utente ha registrato solo il telefono
    	}
    }
	
	public static void sendUserEmailPasswordUpdate(final User user, final String url, VelocityEngine velocityEngine, HttpServletRequest request) 
			throws NoSuchMessageException, Exception {
        Locale locale = request.getLocale();
        //final Map<String, Serializable> model = new HashMap<String, Serializable>();
        final Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("user", user);
        modelVelocity.put("applicationURL", url);
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put("locale", request.getLocale());
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
    	if( !user.getEmail().contains(Constants.FAKE_EMAIL) ){
    		InviaEmail_HTML_User(user, Constants.VM_EMAL_PASSWORD_UPDATE, ApplicationMessagesUtil.DammiMessageSource("email.password.update.subject", locale), 
            		velocityEngine, modelVelocity, null, null, false, (CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    	}else{
    		//email non inviata perke l'utente ha registrato solo il telefono
    	}
    }
	
	public static void sendUserEmailAccountCreated(final User user, String password, VelocityEngine velocityEngine, HttpServletRequest request) 
			throws NoSuchMessageException, Exception {
        Locale locale = request.getLocale();
        String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
        String Message_Domain = (CheckAmbienteVenditore(request.getServletContext())) ? 
        		ApplicationMessagesUtil.DammiMessageSource("signup.email.message.ncctransferonline", locale) : 
        			ApplicationMessagesUtil.DammiMessageSource("signup.email.message.apollotransfert", locale);
        Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("message", Message_Domain);
        modelVelocity.put("user", user);
        modelVelocity.put("password", password);
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put("applicationURL", ApplicationMessagesUtil.DammiMessageSource("signup.email.link.home", new String[]{ControllerUtil.getAppURL(request) +"/home-user"}, locale));
        modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
        Autista autista = autistaDao.getAutistaByUser( user.getId() );
        if( autista != null ){
        	modelVelocity.put(Constants.VM_ATTRIBUTE_AUTISTA_INFO, InfoAutistaExtraEmail(Parametri, autista, velocityEngine, request) );
        }
        String SubJect_Domain = (CheckAmbienteVenditore(request.getServletContext())) ? 
        		ApplicationMessagesUtil.DammiMessageSource("signup.email.subject.ncctransferonline", request.getLocale()) : 
        			ApplicationMessagesUtil.DammiMessageSource("signup.email.subject.apollotransfert", request.getLocale());
        InviaEmail_HTML_User(user, Constants.VM_EMAL_ACCUNT_CREATED, SubJect_Domain, velocityEngine, 
        		modelVelocity, null, null, false, (CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    }
	
	public static void sendUserEmailAccountCreatedFromAdmin(final User user, String url, VelocityEngine velocityEngine, HttpServletRequest request) 
			throws NoSuchMessageException, Exception {
        String Message_Domain = (CheckAmbienteVenditore(request.getServletContext())) ?
        		ApplicationMessagesUtil.DammiMessageSource("newuser.email.message.ncctransferonline", request.getLocale()) : 
        			ApplicationMessagesUtil.DammiMessageSource("newuser.email.message.apollotransfert", request.getLocale());
        Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("user", user);
        modelVelocity.put("message", Message_Domain);
        modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(request.getLocale()));
        modelVelocity.put("applicationURL", ApplicationMessagesUtil.DammiMessageSource("newuser.email.link.home", new String[]{url}, request.getLocale()));
        modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
        Autista autista = autistaDao.getAutistaByUser( user.getId() );
        if( autista != null ){
        	String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
        	modelVelocity.put(Constants.VM_ATTRIBUTE_AUTISTA_INFO, InfoAutistaExtraEmail(Parametri, autista, velocityEngine, request));
        }
        String SubJect_Domain = (CheckAmbienteVenditore(request.getServletContext())) ?
        	ApplicationMessagesUtil.DammiMessageSource("signup.email.subject.ncctransferonline", request.getLocale()) : 
        		ApplicationMessagesUtil.DammiMessageSource("signup.email.subject.apollotransfert", request.getLocale());
        InviaEmail_HTML_User(user, Constants.VM_EMAL_ACCUNT_CREATED, SubJect_Domain, velocityEngine, 
        		modelVelocity, null, null, false, (CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    }
	
	/*
	 * -------------------------------- INVIO EMAIL CORSA --------------------------------
	 */
	public static void InviaEmailCorsaVenduta_Venditore(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> infoCorsa = DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(ricTransfert, (request != null) ? request.getLocale() : null, velocityEngine);
		infoCorsa.put("linkGestioneCorseVenditore", "<a href=\"https://"+ ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", (request != null) ? request.getLocale() : null) 
				+"/gestione-corse-venditore\"><strong>Gestione Corse Vendute</strong></a>");
		InviaEmail.InviaEmail_HTML_User(ricTransfert.getUserVenditore(), Constants.VM_EMAIL_CORSA_VENDUTA_VENDITORE, OggettoCorsaVenduta+" "+ricTransfert.getId(), 
				velocityEngine, infoCorsa, AllegatoFattura_TipoObject(ricTransfert, request), AllegatoFatturaNomeFile_TipoObject(ricTransfert), 
				false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    }
	
	public static void InviaEmailCorsaAcquistata_Cliente(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine, boolean trustpilot) throws Exception {
		InviaEmail.InviaEmail_HTML_User(ricTransfert.getUser(), Constants.VM_EMAIL_CORSA_VENDUTA_CLIENTE, OggettoCorsaAcquistata+" "+ricTransfert.getId(), 
				velocityEngine, DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(ricTransfert, (request!=null)?request.getLocale():null, velocityEngine), 
				AllegatoFattura_TipoObject(ricTransfert, request), AllegatoFatturaNomeFile_TipoObject(ricTransfert), false, 
				(CheckDomainTranfertClienteVenditore(ricTransfert)) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, trustpilot);
    }
	
	public static boolean InviaEmailCorsaAcquistata_Cliente(RicercaTransfert ricTransfert, String Email, String FullName, HttpServletRequest request, VelocityEngine velocityEngine, boolean trustpilot) throws Exception {
		return InviaEmail.InviaEmail_HTML_Other(Email, FullName, Constants.VM_EMAIL_CORSA_VENDUTA_CLIENTE, OggettoCorsaAcquistata+" "+ricTransfert.getId(), 
				velocityEngine, DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(ricTransfert, request.getLocale(), velocityEngine), 
				AllegatoFattura_TipoObject(ricTransfert, request), AllegatoFatturaNomeFile_TipoObject(ricTransfert), false,
				(CheckDomainTranfertClienteVenditore(ricTransfert)) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, trustpilot);
    }
	
	public static boolean InviaEmailCorsaAcquistata_Cliente(RicercaTransfert ricTransfert, String Email, HttpServletRequest request, VelocityEngine velocityEngine, boolean trustpilot) throws Exception {
		return InviaEmail.InviaEmail_HTML_Other(Email, Constants.VM_EMAIL_CORSA_VENDUTA_CLIENTE, OggettoCorsaAcquistata+" "+ricTransfert.getId(), 
				velocityEngine, DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(ricTransfert, request.getLocale(), velocityEngine), 
				AllegatoFattura_TipoObject(ricTransfert, request), AllegatoFatturaNomeFile_TipoObject(ricTransfert), false,
				(CheckDomainTranfertClienteVenditore(ricTransfert)) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, trustpilot);
    }
	
	public static void Invia_Email_AvvisoCorsaConfermataCliente(RichiestaMediaAutista richAutistMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> infoCorsa = DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(richAutistMedio.getRichiestaMedia().getRicercaTransfert(), request.getLocale(), velocityEngine);
		infoCorsa.put("nomeAustiaInCarico", richAutistMedio.getAutista().getUser().getFullName());
		System.out.println(richAutistMedio.getRichiestaMedia().getRicercaTransfert().getUser().getEmail());
		InviaEmail.InviaEmail_HTML_User(richAutistMedio.getRichiestaMedia().getRicercaTransfert().getUser(), Constants.VM_EMAIL_AVVISO_CORSA_PRENOTATA_CLIENTE, 
				"Corsa Presa in Carico Da Autista", velocityEngine, infoCorsa, AllegatoFattura_TipoObject(richAutistMedio.getRichiestaMedia().getRicercaTransfert(), request), 
				AllegatoFatturaNomeFile_TipoObject(richAutistMedio.getRichiestaMedia().getRicercaTransfert()), false, 
				(CheckDomainTranfertClienteVenditore(richAutistMedio.getRichiestaMedia().getRicercaTransfert())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}

	
	private static Map<String, Object> DammiModelVelocity_InformazioniCorsaVendutaClienteEmail(RicercaTransfert ricTransfert, Locale locale, 
			VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		if(ricTransfert.getUserVenditore() != null){
			modelVelocity.put("nomeCognomeVenditore", ricTransfert.getUserVenditore().getFullName());
		}
		modelVelocity.put("nomeCognome", ricTransfert.getUser().getFullName());
		modelVelocity.put("corsaId", ricTransfert.getId());
		
		String infoCorsa = ""; String Prezzo = ""; String CategoriaAuto = ""; String InfoCategoriaAuto = "";
		
		if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
			AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
			List<AgendaAutista_Autista> AgendaAutista_AutistaTotList = new ArrayList<AgendaAutista_Autista>();
			if( agendaAutistaScelta.getAgendaAutista_AutistaAndata() != null ) {
				AgendaAutista_AutistaTotList.add(agendaAutistaScelta.getAgendaAutista_AutistaAndata());
			}
			if( agendaAutistaScelta.getAgendaAutista_AutistaRitorno() != null ) {
				AgendaAutista_AutistaTotList.add(agendaAutistaScelta.getAgendaAutista_AutistaRitorno());
			}
			Map<Long, String> descrizioneCategorieAutoMap = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(locale);
			for(AgendaAutista_Autista ite: AgendaAutista_AutistaTotList) {
				String AndataRitorno = ite.isRitornoCorsa() ? "RITORNO" : "ANDATA"; infoCorsa = infoCorsa + "<p>"
				+"<strong>AUTISTA "+AndataRitorno+": </strong> "+ite.getFullName()+"<br>"+
				"<strong>Modello e Classe Autoveicolo:</strong> "+
					ite.getNomeMarcaAuto()+" "+ite.getNomeModelloAuto()+" "+"("+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicoloReale().getNome(), locale)+")"+
					"<strong>Targa:</strong> "+ite.getTarga()+" "+"<br>"+
				"<strong>Desc. Classe Autoveicolo: </strong>"+descrizioneCategorieAutoMap.get(ite.getClasseAutoveicoloReale().getId())+"<br>"+
				"<strong>Prezzo:</strong> "+ite.getPrezzoCliente().setScale(2, RoundingMode.HALF_EVEN)+"&euro;"+
				"</p>";
			}
			infoCorsa = infoCorsa + "<p><strong>PREZZO TOTALE:</strong> "+agendaAutistaScelta.getPrezzoTotaleCliente()+"&euro;</p>";
			
		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
			Prezzo = ricTransfert.getRichiestaMediaScelta().getPrezzoTotaleCliente().toString()+"&euro;";
			if(ricTransfert.isPagamentoParziale()) {
				Prezzo = ApplicationMessagesUtil.DammiMessageSource("prezzo.cliente.parziale.piu.prezzo.autista", new String[] {
						ricTransfert.getRichiestaMediaScelta().getPrezzoCommissioneServizio().toString(), 
						ricTransfert.getRichiestaMediaScelta().getPrezzoTotaleAutista().toString()}, locale);
				Prezzo = StringEscapeUtils.escapeHtml(Prezzo);
			}
			CategoriaAuto = ApplicationMessagesUtil.DammiMessageSource(ricTransfert.getRichiestaMediaScelta().getClasseAutoveicolo().getNome(), locale);
			InfoCategoriaAuto = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(locale).get(ricTransfert.getRichiestaMediaScelta().getClasseAutoveicolo().getId());
			if(ricTransfert.getRichiestaMediaScelta() != null && ricTransfert.getRichiestaMediaScelta().getMaggiorazioneNotturna() != null){
				Prezzo = Prezzo + StringEscapeUtils.escapeHtml(ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", 
						new String[]{ricTransfert.getRichiestaMediaScelta().getMaggiorazioneNotturna().toString()}, locale)); 
			}
			infoCorsa = "<strong>PREZZO:</strong> "+Prezzo+"<br><strong>CATEGORIA AUTO:</strong> "+CategoriaAuto+"<br><strong>INFO CATEGORIA AUTO:</strong> "+InfoCategoriaAuto;
			
		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
			RichiestaAutistaParticolare RichiestaAutistaPartAcquistato = ricTransfert.getRichiestaAutistaParticolareAcquistato();
			Prezzo = RichiestaAutistaPartAcquistato.getPrezzoTotaleCliente().toString();
			CategoriaAuto = ApplicationMessagesUtil.DammiMessageSource(RichiestaAutistaPartAcquistato.getClasseAutoveicoloScelta().getNome(), locale);
			InfoCategoriaAuto = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(locale).get(RichiestaAutistaPartAcquistato.getClasseAutoveicoloScelta().getId());
			infoCorsa = "<strong>PREZZO:</strong> "+Prezzo+"&euro;<br><strong>CATEGORIA AUTO:</strong> "+CategoriaAuto+"<br><strong>INFO CATEGORIA AUTO:</strong> "+InfoCategoriaAuto;
		
		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
			List<RichiestaAutistaParticolare> richiestaAutistaParticolareMultiplo = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
			Map<Long, String> descrizioneCategorieAutoMap = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(locale);
			BigDecimal prezzoTotaleClienteMultiplo = new BigDecimal(0);
			infoCorsa = "<ul>";
			for(RichiestaAutistaParticolare ite: richiestaAutistaParticolareMultiplo) {
				infoCorsa = infoCorsa + "<li>"+
				"<strong>Nome Autista:</strong> "+ite.getAutoveicolo().getAutista().getUser().getFullName()+"<br>"+
				"<strong>Modello e Classe Autoveicolo:</strong> "+
						ite.getAutoveicolo().getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName()+" "+
						ite.getAutoveicolo().getModelloAutoNumeroPosti().getModelloAutoScout().getName()+" "+
						"("+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicoloScelta().getNome(), locale)+")"+
						"<strong>Targa:</strong> "+ite.getAutoveicolo().getTarga()+" "+"<br>"+
				"<strong>Desc. Classe Autoveicolo: </strong>"+descrizioneCategorieAutoMap.get(ite.getClasseAutoveicoloScelta().getId())+"<br>"+
				"<strong>Numero Max Posti Passeggeri:</strong> "+ite.getAutoveicolo().getNumeroPostiPasseggeri()+"<br>"+
				"<strong>Prezzo:</strong> "+ite.getPrezzoTotaleCliente().setScale(2, RoundingMode.HALF_EVEN)+"&euro;"+
				"</li>";
				prezzoTotaleClienteMultiplo = prezzoTotaleClienteMultiplo.add(ite.getPrezzoTotaleCliente());
			}
			infoCorsa = infoCorsa + "</ul>";
			infoCorsa = infoCorsa + "<p><strong>PREZZO TOTALE:</strong> "+prezzoTotaleClienteMultiplo.setScale(2, RoundingMode.HALF_EVEN)+"&euro;</p>";
		}

		modelVelocity.put("infoCorsa", infoCorsa);
		modelVelocity.put("corsaPartenzaArrivo", CorsaAndataRitornoModelVelocity(ricTransfert, locale));
		modelVelocity.put("notePerAutista", ricTransfert.getNotePerAutista());
		String numOreInfoAutistaCliente = ApplicationMessagesUtil.DammiMessageSource("numero.ore.visualizzazione.contatto.cliente", new String[]{ 
				gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber().toString()}, locale);
		modelVelocity.put("numOreInfoAutistaCliente", StringEscapeUtils.escapeHtml(numOreInfoAutistaCliente));
		modelVelocity.put("linkVisualizzaCorsaCliente", "<a href=\"https://"+ 
				DammiUrl_InfoCorsa_Cliente(ricTransfert, locale) +"\"><strong><big>Visualizza Info Corsa e Info Autista</big></strong></a>");
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, ricTransfert, locale));
		return modelVelocity;
    }
	
	public static String DammiUrl_InfoCorsa_Cliente(RicercaTransfert ricTransfert, Locale locale) {
		if( ricTransfert.getUser() != null ) {
			String Domain = (ApplicationUtils.CheckDomainTranfertClienteVenditore(ricTransfert)) ?
					ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", locale) :
						ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale);
			return Domain +"/"+Constants.URL_VISUALZZA_CORSA_CLIENTE+"?token="+ricTransfert.getUser().getUsername()+"@"+ricTransfert.getId();  
		}else {
			return null;
		}
	}
	
	public static void Invia_Email_SollecitoRitardoCliente(Ritardi ritardo, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		modelVelocity.put("nomeCognome", ritardo.getRicercaTransfert().getUser().getFullName());
		modelVelocity.put("corsaId", ritardo.getRicercaTransfert().getId());
		String Domain = (ApplicationUtils.CheckDomainTranfertClienteVenditore(ritardo.getRicercaTransfert())) ?
				DammiLinkPagamentoRitardo(ritardo, ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", (request!=null)?request.getLocale():null)) : 
					DammiLinkPagamentoRitardo(ritardo, ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", (request!=null)?request.getLocale():null));
		modelVelocity.put("linkPagamento", Domain);
		modelVelocity.put("corsaPartenzaArrivo", InviaEmail.CorsaAndataRitornoModelVelocity(ritardo.getRicercaTransfert(), (request!=null)?request.getLocale():null));
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, ritardo.getRicercaTransfert(), (request!=null)?request.getLocale():null));
		InviaEmail.InviaEmail_HTML_User(ritardo.getRicercaTransfert().getUser(), Constants.VM_EMAIL_SOLLECITO_RITARDO_CLIENTE, 
				"Fattura Ritardo Prelevamento Passeggero", velocityEngine, modelVelocity, AllegatoFattura_TipoObject(ritardo, request), 
				AllegatoFatturaNomeFile_TipoObject(ritardo), false, (CheckDomainTranfertClienteVenditore(ritardo.getRicercaTransfert())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}
	
	private static String DammiLinkPagamentoRitardo(Ritardi ritardo, String Domain){
		return "<a href=\"https://"+ Domain +"/pagamentoRitardo?courseId="+ritardo.getRicercaTransfert().getId()+"\">Pagamento Ritardo</a>";
	}
	
	public static void Invia_Email_SollecitoSupplementoCliente(Supplementi supplemento, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		modelVelocity.put("nomeCognome", supplemento.getRicercaTransfert().getUser().getFullName());
		modelVelocity.put("corsaId", supplemento.getRicercaTransfert().getId());
		String Domain = (ApplicationUtils.CheckDomainTranfertClienteVenditore(supplemento.getRicercaTransfert())) ?
				DammiLinkPagamentoSupplemento(supplemento, ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", (request!=null)?request.getLocale():null)) : 
					DammiLinkPagamentoSupplemento(supplemento, ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", (request!=null)?request.getLocale():null));
		modelVelocity.put("linkPagamento", Domain);
		modelVelocity.put("corsaPartenzaArrivo", InviaEmail.CorsaAndataRitornoModelVelocity(supplemento.getRicercaTransfert(), (request!=null)?request.getLocale():null));
		modelVelocity.put("prezzoSupplemento", supplemento.getPrezzo());
		modelVelocity.put("descrizioneSupplemento", supplemento.getDescrizione());
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, supplemento.getRicercaTransfert(), (request!=null)?request.getLocale():null));
		InviaEmail.InviaEmail_HTML_User(supplemento.getRicercaTransfert().getUser(), Constants.VM_EMAIL_SOLLECITO_SUPPLEMENTO_CLIENTE, 
				"Fattura Supplemento Extra Corsa", velocityEngine, modelVelocity, AllegatoFattura_TipoObject(supplemento, request), 
				AllegatoFatturaNomeFile_TipoObject(supplemento), false, (CheckDomainTranfertClienteVenditore(supplemento.getRicercaTransfert())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}
	
	private static String DammiLinkPagamentoSupplemento(Supplementi supplemento, String Domain){
		return "<a href=\"https://"+ Domain +"/pagamentoSupplemento?idSupplemento="+supplemento.getId()+"\">Pagamento Supplemento</a>";
	}
	
	public static void Invia_Email_ConfermaPagamentoRitardoCliente(Ritardi ritardo, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		String Oggetto = "Conferma Pagamento Ritardo Prelevamento Passeggero";
		modelVelocity.put("title", Oggetto);
		modelVelocity.put("nomeCognome", ritardo.getRicercaTransfert().getUser().getFullName());
		modelVelocity.put("corsaId", ritardo.getRicercaTransfert().getId());
		modelVelocity.put("corsaPartenzaArrivo", InviaEmail.CorsaAndataRitornoModelVelocity(ritardo.getRicercaTransfert(), request.getLocale()));
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, ritardo.getRicercaTransfert(), request.getLocale()));
		InviaEmail.InviaEmail_HTML_User(ritardo.getRicercaTransfert().getUser(), Constants.VM_EMAIL_CONFERMA_PAGAMENTO_RITARDO_CLIETE, 
				Oggetto, velocityEngine, modelVelocity, AllegatoFattura_TipoObject(ritardo, request), AllegatoFatturaNomeFile_TipoObject(ritardo), 
				false, (CheckDomainTranfertClienteVenditore(ritardo.getRicercaTransfert())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}
	
	public static void Invia_Email_ConfermaPagamentoSupplementoCliente(Supplementi supplemento, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		String Oggetto = "Conferma Pagamento Supplemento Extra Corsa";
		modelVelocity.put("title", Oggetto);
		modelVelocity.put("nomeCognome", supplemento.getRicercaTransfert().getUser().getFullName());
		modelVelocity.put("corsaId", supplemento.getRicercaTransfert().getId());
		modelVelocity.put("corsaPartenzaArrivo", InviaEmail.CorsaAndataRitornoModelVelocity(supplemento.getRicercaTransfert(), request.getLocale()));
		modelVelocity.put("prezzoSupplemento", supplemento.getPrezzo());
		modelVelocity.put("descrizioneSupplemento", supplemento.getDescrizione());
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, supplemento.getRicercaTransfert(), request.getLocale()));
		InviaEmail.InviaEmail_HTML_User(supplemento.getRicercaTransfert().getUser(), Constants.VM_EMAIL_CONFERMA_PAGAMENTO_SUPPLEMENTO_CLIETE, 
				Oggetto, velocityEngine, modelVelocity, AllegatoFattura_TipoObject(supplemento, request), AllegatoFatturaNomeFile_TipoObject(supplemento), 
				false, (CheckDomainTranfertClienteVenditore(supplemento.getRicercaTransfert())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}
	
	
	/*
	private static ByteArrayOutputStream AllegatoFatturaCorsaCliente(RicercaTransfert ricTransfert, HttpServletRequest request) throws Exception {
		BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( ricTransfert.getId() );
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance (document, baos);
		writer.setPageEvent( new Footer_Header_Fattura() );
		document.open();
		GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
		fattPdf.Corpo_FatturaCorsa(document, (request != null)?request.getLocale():null);
		document.close();
		return baos;
	}
	*/
	
	
	public static ByteArrayOutputStream AllegatoFattura_TipoObject(Object tipoObject, HttpServletRequest request) throws Exception {
		BeanInfoFattura_Corsa fattCorsa = null;
		RicercaTransfert ricTransfert = null; Ritardi ritardo = null; Supplementi supplemento = null;
		if(tipoObject instanceof RicercaTransfert) {
			ricTransfert = (RicercaTransfert) tipoObject;
			fattCorsa = Fatturazione.Informazioni_FatturaCorsa( ricTransfert.getId() );
		
		}else if(tipoObject instanceof Ritardi) {
			ritardo = (Ritardi) tipoObject;
			fattCorsa = Fatturazione.Informazioni_FatturaCorsa( ritardo.getRicercaTransfert().getId() );

		}else if(tipoObject instanceof Supplementi) {
			supplemento = (Supplementi) tipoObject;
			fattCorsa = Fatturazione.Informazioni_FatturaCorsa( supplemento.getRicercaTransfert().getId() );
		}
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		writer.setPageEvent( new Footer_Header_Fattura() );
		document.open();
		GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
		if(tipoObject instanceof RicercaTransfert) {
			fattPdf.Corpo_FatturaCorsa(document, (request != null) ? request.getLocale() : null);
		
		}else if(tipoObject instanceof Ritardi) {
			fattPdf.Corpo_FatturaRitardo(document, ritardo, (request!=null) ? request.getLocale() : null);

		}else if(tipoObject instanceof Supplementi) {
			fattPdf.Corpo_FatturaSupplemento(document, supplemento, (request!=null) ? request.getLocale() : null);
		}
		document.close();
		return baos;
	}
	
	public static String AllegatoFatturaNomeFile_TipoObject(Object tipoObject) throws DocumentException {
		RicercaTransfert ricTransfert = null; Ritardi ritardo = null; Supplementi supplemento = null;
		if(tipoObject instanceof RicercaTransfert) {
			ricTransfert = (RicercaTransfert) tipoObject;
			return "fattura_cliente_corsa_"+ricTransfert.getId()+".pdf";
		}else if(tipoObject instanceof Ritardi) {
			ritardo = (Ritardi) tipoObject;
			return "ritardo_prelev_passegg_corsa_"+ritardo.getRicercaTransfert().getId()+".pdf";
		}else if(tipoObject instanceof Supplementi) {
			supplemento = (Supplementi) tipoObject;
			return "supplemento_extra_corsa_"+supplemento.getRicercaTransfert().getId()+".pdf";
		}else {
			return "fattura.pdf";
		}
	}
	
	

	
	public static void InviaEmailAgendaAutistaCorsaAcquistata_Autista(AgendaAutista_Autista agendaAutista, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		Map<String, Object> modelVelocity = DammiModelVelocity_InviaEmailAgendaAutistaCorsaAcquistata_Autista(agendaAutista, request, velocityEngine);
		modelVelocity.put("datiFatturazioneEmail", DatiFatturazioneEmail(request) );
		InviaEmail.InviaEmail_HTML_Other(agendaAutista.getEmail(), agendaAutista.getFullName(), Constants.VM_EMAIL_AGENDA_AUTISTA_CORSA_ACQUISTA, 
				 "Agenda Autista Corsa Acquistata "+agendaAutista.getRicercaTransfert().getId() +" ("+agendaAutista.getMarcaModelloTarga()+")", 
		velocityEngine, modelVelocity, null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	
	public static void InviaEmailCorsaCancellataAutista(RichiestaMediaAutista richAutistMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		InviaEmail.InviaEmail_HTML_User(richAutistMedio.getAutista().getUser(), Constants.VM_EMAIL_CORSA_CANCELLATA_AUTISTA, "Corsa Cancellata "+richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId(), 
				velocityEngine, DammiModelVelocity_InformazioniCorsaAutistaEmail(richAutistMedio, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}
	
	public static void InviaEmailCorsaConfermataAutista(RichiestaMediaAutista richAutistMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> modelVelocity = DammiModelVelocity_InformazioniCorsaAutistaEmail(richAutistMedio, request, velocityEngine);
		modelVelocity.put("datiFatturazioneEmail", DatiFatturazioneEmail(request) );
		InviaEmail.InviaEmail_HTML_User(richAutistMedio.getAutista().getUser(), Constants.VM_EMAIL_CORSA_CONFERMATA_AUTISTA, "Corsa Confermata "+richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId(), 
				velocityEngine, modelVelocity, AllegatoFattura_TipoObject(richAutistMedio.getRichiestaMedia().getRicercaTransfert(), request), 
				AllegatoFatturaNomeFile_TipoObject(richAutistMedio.getRichiestaMedia().getRicercaTransfert()), false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	}

	public static void InviaEmailCorsaDisponibileAutista(RichiestaMediaAutista richAutistMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		 InviaEmail.InviaEmail_HTML_User(richAutistMedio.getAutista().getUser(), Constants.VM_EMAIL_CORSA_DISPONIBILE_AUTISTA, "Corsa Disponibile "+richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId(), 
				 velocityEngine, DammiModelVelocity_InformazioniCorsaAutistaEmail(richAutistMedio, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	public static void InviaEmailRichiestaPreventivoAutista(RichiestaAutistaParticolare richiestaAutistaPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		 InviaEmail.InviaEmail_HTML_User(richiestaAutistaPart.getAutoveicolo().getAutista().getUser(), Constants.VM_EMAIL_RICHIESTA_PREVENTIVO_AUTISTA, "Richiesta Preventivo Corsa "
				 +richiestaAutistaPart.getRicercaTransfert().getId() +" ("+richiestaAutistaPart.getAutoveicolo().getMarcaModelloTarga()+")", 
				 velocityEngine, DammiModelVelocity_InformazioniRichiestaPreventivoAutistaEmail(richiestaAutistaPart, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	public static void InviaEmailRichiestaPreventivoAcquistato_Autista(RichiestaAutistaParticolare richiestaAutistaPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		Map<String, Object> modelVelocity = DammiModelVelocity_InviaEmailRichiestaPreventivoAcquistato_Autista(richiestaAutistaPart, request, velocityEngine);
		modelVelocity.put("datiFatturazioneEmail", DatiFatturazioneEmail(request) );
		InviaEmail.InviaEmail_HTML_User(richiestaAutistaPart.getAutoveicolo().getAutista().getUser(), Constants.VM_EMAIL_PREVENTIVO_ACQUISTATO_AUTISTA, "Preventivo Acquistato Corsa "
				 +richiestaAutistaPart.getRicercaTransfert().getId() +" ("+richiestaAutistaPart.getAutoveicolo().getMarcaModelloTarga()+")", velocityEngine, modelVelocity, null, 
				 null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	public static void InviaEmailListaAutistiPreventivoCliente(RicercaTransfert ricercaTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		 InviaEmail.InviaEmail_HTML_Other(ricercaTransfert.getRicTransfert_Email(), ricercaTransfert.getRicTransfert_Nome()+" "+ricercaTransfert.getRicTransfert_Cognome(), Constants.VM_EMAIL_LISTA_PREVENTIVI_CLIENTE, 
				 "Richiesta Preventivi Corsa "+ricercaTransfert.getId(), velocityEngine, DammiModelVelocity_ListaAutistiPreventivo_Cliente(ricercaTransfert, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	public static void InviaEmailListaAutistiPreventivoCliente_AvvisoMatteo(RicercaTransfert ricercaTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		 InviaEmail.InviaEmail_HTML_Other( ApplicationMessagesUtil.DammiMessageSource("email.info.apollotransfert", request.getLocale()) , ricercaTransfert.getRicTransfert_Nome()
				 +" "+ricercaTransfert.getRicTransfert_Cognome(), Constants.VM_EMAIL_LISTA_PREVENTIVI_CLIENTE, "(Avviso Ricerca Corsa Partcolare) Richiesta Preventivi Corsa "+ricercaTransfert.getId(), 
				 velocityEngine, DammiModelVelocity_ListaAutistiPreventivo_Cliente(ricercaTransfert, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	public static void InviaEmailPreventivoAutistaInviato_Cliente(RichiestaAutistaParticolare richAutistPart, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		 InviaEmail.InviaEmail_HTML_Other(richAutistPart.getRicercaTransfert().getRicTransfert_Email(), richAutistPart.getRicercaTransfert().getRicTransfert_Nome()+" "+richAutistPart.getRicercaTransfert().getRicTransfert_Cognome(), 
				 Constants.VM_EMAIL_PREVENTIVO_AUTISTA_INVIATO_CLIENTE, "Un Autista ti ha inviato il Preventivo per la Corsa "+richAutistPart.getRicercaTransfert().getId(), 
				 velocityEngine, DammiModelVelocity_PreventivoAutistaInviato_Cliente(richAutistPart, request, velocityEngine), null, null, false, Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
	 }
	
	
	/**
     * Invio Email Lista Autisti Preventivo Cliente 
     */
    private static Map<String, Object> DammiModelVelocity_PreventivoAutistaInviato_Cliente(RichiestaAutistaParticolare richAutistPart, HttpServletRequest request, VelocityEngine velocityEngine){
    	Map<String, Object> modelVelocity = new HashMap<String, Object>();
    	modelVelocity.putAll( DammiModelVelocity_ListaAutistiPreventivo_Cliente(richAutistPart.getRicercaTransfert(), request, velocityEngine) );
    	Map<Long, String> descrizioneCategorieAutoMap = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale());
    	String infoAutistaPreventivo = "<strong>Nome Autista: </strong>"+richAutistPart.getAutoveicolo().getAutista().getUser().getFirstName()
    			+" "+richAutistPart.getAutoveicolo().getAutista().getUser().getLastName().substring(0, 1)+"."
    			+"<br><strong>Modello e Classe Autoveicolo: </strong>"+richAutistPart.getAutoveicolo().getMarcaModello()
    			+" ("+ApplicationMessagesUtil.DammiMessageSource(richAutistPart.getClasseAutoveicoloScelta().getNome(), request.getLocale())+")"
    			+"<br><strong>Desc. Classe Autoveicolo: </strong>"+descrizioneCategorieAutoMap.get(richAutistPart.getClasseAutoveicoloScelta().getId())
    			+"<br><ins><strong>Numero Posti Passeggeri: </strong>"+richAutistPart.getAutoveicolo().getNumeroPostiPasseggeri()+"</ins>"
    			+"<br><ins><strong>Prezzo Totale (Prezzo tutto compreso iva inclusa): </strong>"+richAutistPart.getPrezzoTotaleCliente()+"&euro;</ins>"
    			+"<br><ins><strong>Tempo validit&agrave; Preventivo (Tempo Massimo Acquisto Corsa): </strong>"+DateUtil.FORMATO_GIORNO_SETTIMANA_DATA_ORA(request.getLocale()).format(richAutistPart.getPreventivo_validita_data())+"</ins>";
    	modelVelocity.put("infoAutistaPreventivoAutista", infoAutistaPreventivo);
    	String Domain = CheckAmbienteVenditore(request.getServletContext()) ? ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", request.getLocale()) 
				: ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale());
		modelVelocity.put("linkPaginaAcquistaPreventivoAutista", "<a href=\"https://"+ Domain +"/?courseId="+richAutistPart.getRicercaTransfert().getId()
				+"&tokenRicTransfert="+richAutistPart.getRicercaTransfert().getRicTransfert_Token()+"&richPartTokenAutista="+richAutistPart.getToken()+"\">"
				+ Domain +"/?courseId="+richAutistPart.getRicercaTransfert().getId()+"&tokenRicTransfert="+richAutistPart.getRicercaTransfert().getRicTransfert_Token()
				+"&richPartTokenAutista="+richAutistPart.getToken()+"</a>");
		return modelVelocity;
    }

    
	/**
     * Invio Email Lista Autisti Preventivo Cliente 
     */
    private static Map<String, Object> DammiModelVelocity_ListaAutistiPreventivo_Cliente(RicercaTransfert ricercaTransfert, HttpServletRequest request, VelocityEngine velocityEngine){
    	Map<String, Object> modelVelocity = new HashMap<String, Object>();
    	//https://localhost:8443/apollon/lista-preventivi-cliente?courseId=9924&tokenRicTransfert=9924EiFUhQpxKBWH2JB
		String Domain = CheckAmbienteVenditore(request.getServletContext()) ? ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", request.getLocale()) 
				: ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale());
		modelVelocity.put("linkPaginaListaPreventiviCliente", "<a href=\"https://"+ Domain +"/"+Constants.URL_LISTA_PREVENTIVI_CLIENTE
				+"?courseId="+ricercaTransfert.getId()+"&tokenRicTransfert="+ricercaTransfert.getRicTransfert_Token()+"\">"
				+Domain+"/"+Constants.URL_LISTA_PREVENTIVI_CLIENTE+"?courseId="+ricercaTransfert.getId()+"&tokenRicTransfert="+ricercaTransfert.getRicTransfert_Token()+ "</a>");
		modelVelocity.put("linkPaginaRicerca", "<a href=\"https://"+ Domain +"/"+"\">"+Domain+"/"+"</a>");
		modelVelocity.put("corsaPartenzaArrivo", CorsaAndataRitornoModelVelocity(ricercaTransfert, request.getLocale()));
		modelVelocity.put("corsaId", ricercaTransfert.getId());
		modelVelocity.put("nomeCognomeCliente", ricercaTransfert.getRicTransfert_Nome()+" "+ricercaTransfert.getRicTransfert_Cognome());
		
		Map<Long, String> descrizioneCategorieAutoMap = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale());
		String listaAutistiPreventivo = "<ul>";
		for(RichiestaAutistaParticolare ite: ricercaTransfert.getRichiestaAutistaParticolare()) {
			listaAutistiPreventivo = listaAutistiPreventivo 
					+"<li>"+ite.getAutoveicolo().getAutista().getUser().getFirstName()+" "+ite.getAutoveicolo().getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName()
					+" "+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicoloScelta().getNome(), request.getLocale())+"<br>"
					+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicoloScelta().getNome()+".desc", request.getLocale())+"<br>"
					+descrizioneCategorieAutoMap.get(ite.getClasseAutoveicoloScelta().getId())+"</li>";
		}
		listaAutistiPreventivo = listaAutistiPreventivo + "</ul>";
		modelVelocity.put("listaAutistiPreventivo", listaAutistiPreventivo);
		modelVelocity.put("notePerAutista", ricercaTransfert.getNotePerAutista());
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
		return modelVelocity;
    }
	

    private static Map<String, Object> DammiModelVelocity_InviaEmailAgendaAutistaCorsaAcquistata_Autista(AgendaAutista_Autista agendaAutista, 
    		HttpServletRequest request, VelocityEngine velocityEngine){
    	Map<String, Object> modelVelocity = new HashMap<String, Object>(); 
    	String infoAgendaAutistaCorsaAcquistata = "<strong>Compenso Autista (Compresa IVA):</strong> "+ agendaAutista.getPrezzoCorsa() +"&euro;";
		modelVelocity.put("infoAgendaAutistaCorsaAcquistata", infoAgendaAutistaCorsaAcquistata);

		modelVelocity.put("nomeCognome", agendaAutista.getFullName());
		modelVelocity.put("corsaId", agendaAutista.getRicercaTransfert().getId());
		String infoCorsa = "<strong>AUTOVEICOLO RICHIESTO:</strong> "+ AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(Arrays.asList( autoveicoloDao.get(agendaAutista.getIdAutoveicolo())), request);
		modelVelocity.put("infoCorsa", infoCorsa);
		modelVelocity.put("corsaPartenzaArrivo", (agendaAutista.isRitornoCorsa() == false ? 
				CorsaAndataAgendaAutistaModelVelocity(agendaAutista.getRicercaTransfert(), request.getLocale()) 
				: CorsaRitornoAgendaAutistaModelVelocity(agendaAutista.getRicercaTransfert(), request.getLocale())) );
		modelVelocity.put("notePerAutista", agendaAutista.getRicercaTransfert().getNotePerAutista());
		String numOreInfoAutistaCliente = ApplicationMessagesUtil.DammiMessageSource("numero.ore.visualizzazione.contatto.autista", new String[]{ 
				gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber().toString()}, request.getLocale());
		modelVelocity.put("numOreInfoAutistaCliente", StringEscapeUtils.escapeHtml(numOreInfoAutistaCliente));
		String TestoMessaggioLinkGestioneCorsa =  "Fai Click qui per Visualizzare i Contatti del Passeggero";
		modelVelocity.put("linkGestioneCorsaAutista", "<a href=\"https://"+ ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale())
			+"/"+Constants.URL_CORSA_AGENDA_AUTISTA+"?idTariffario="+agendaAutista.getIdTariffario()+"\"><strong><big>"+TestoMessaggioLinkGestioneCorsa+"</big></strong></a>");
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", request.getLocale()));
		return modelVelocity;
    }
    
    
    private static Map<String, Object> DammiModelVelocity_InviaEmailRichiestaPreventivoAcquistato_Autista(RichiestaAutistaParticolare richiestaAutistaPart, HttpServletRequest request, VelocityEngine velocityEngine) {
    	Map<String, Object> modelVelocity = new HashMap<String, Object>(); 
    	RicercaTransfert ricTransfert = ricercaTransfertDao.get(richiestaAutistaPart.getRicercaTransfert().getId());
    	if( ricTransfert.getRichiestaAutistaParticolareAcquistato() != null || ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo() != null ) {
    		String infoPreventivo = "<strong>Compenso Autista (Compresa IVA):</strong> "+ richiestaAutistaPart.getPrezzoTotaleAutista() +"&euro;"
    				+"<br><strong>Data Validità Preventivo:</strong> "+ DateUtil.FORMATO_GIORNO_SETTIMANA_DATA_ORA(request.getLocale()).format(richiestaAutistaPart.getPreventivo_validita_data_Date()); 
    		modelVelocity.put("infoPreventivoAcquistato", infoPreventivo);
    	}
    	modelVelocity.putAll( DammiModelVelocity_InformazioniRichiestaPreventivoAutistaEmail(richiestaAutistaPart, request, velocityEngine) );
    	return modelVelocity;
    }
	
    /**
	 * Informazioni Richiesta Preventivo Autista Email
	 */
    private static Map<String, Object> DammiModelVelocity_InformazioniRichiestaPreventivoAutistaEmail(RichiestaAutistaParticolare richiestaAutistaPart, 
    		HttpServletRequest request, VelocityEngine velocityEngine){
    	// Velocity Model
    	Map<String, Object> modelVelocity = new HashMap<String, Object>(); 
		modelVelocity.put("nomeCognome", richiestaAutistaPart.getAutoveicolo().getAutista().getUser().getFullName());
		modelVelocity.put("corsaId", richiestaAutistaPart.getRicercaTransfert().getId());
		String infoCorsa = "<strong>AUTOVEICOLO RICHIESTO:</strong> "+ AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(Arrays.asList(richiestaAutistaPart.getAutoveicolo()), request);
		if(richiestaAutistaPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
			infoCorsa = infoCorsa + "<br>"+ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo.email.autista", new String[]{ 
					ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo", request.getLocale()) }, request.getLocale());
			infoCorsa = infoCorsa + "<br>"+ApplicationMessagesUtil.DammiMessageSource("servizio.multiplo.max.passeggeri.email.autista", new String[]{ 
					ApplicationMessagesUtil.DammiMessageSource("servizio.multiplo.max.passeggeri.autista", new String[]{ 
							richiestaAutistaPart.getAutoveicolo().getNumeroPostiPasseggeri().toString()}, request.getLocale()) }, request.getLocale());
		}
		
		modelVelocity.put("infoCorsa", infoCorsa);
		modelVelocity.put("corsaPartenzaArrivo", CorsaAndataRitornoModelVelocity(richiestaAutistaPart.getRicercaTransfert(), request.getLocale()));
		modelVelocity.put("notePerAutista", richiestaAutistaPart.getRicercaTransfert().getNotePerAutista());
		String numOreInfoAutistaCliente = ApplicationMessagesUtil.DammiMessageSource("numero.ore.visualizzazione.contatto.autista", new String[]{ 
				gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber().toString()}, request.getLocale());
		modelVelocity.put("numOreInfoAutistaCliente", StringEscapeUtils.escapeHtml(numOreInfoAutistaCliente));
		
		String TestoMessaggioLinkGestioneCorsa = richiestaAutistaPart.getPreventivo_inviato_cliente() == null || richiestaAutistaPart.getPreventivo_inviato_cliente() == false ? 
				"Fai Click qui per Compilare e Inviare il Preventivo al Cliente" : "Fai Click qui per Visualizzare i Contatti del Passeggero";
		modelVelocity.put("linkGestioneCorsaAutista", "<a href=\"https://"+ ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale())+"/"+Constants.URL_PREVENTIVO_CORSA+"?token="
				+richiestaAutistaPart.getToken()+"\"><strong><big>"+TestoMessaggioLinkGestioneCorsa+"</big></strong></a>");
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", request.getLocale()));
		return modelVelocity;
    }
	
	/**
	 * Informazioni Corsa Autista Email
	 */
    private static Map<String, Object> DammiModelVelocity_InformazioniCorsaAutistaEmail(RichiestaMediaAutista richAutistMedio, HttpServletRequest request, 
    		VelocityEngine velocityEngine){
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		modelVelocity.put("nomeCognome", richAutistMedio.getAutista().getUser().getFullName());
		modelVelocity.put("corsaId", richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId());
		String Prezzo = richAutistMedio.getPrezzoTotaleAutista() +"&euro;";
		
		if(richAutistMedio.getRichiestaMedia().getRicercaTransfert().isPagamentoParziale()) {
			Prezzo = ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.contanti", new String[]{
					richAutistMedio.getPrezzoTotaleAutista().toString()}, request.getLocale());
			Prezzo = StringEscapeUtils.escapeHtml(Prezzo);
		}
		if(richAutistMedio.getRichiestaMedia().getMaggiorazioneNotturna() != null) {
			Prezzo = Prezzo + StringEscapeUtils.escapeHtml(ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", new String[]{
					richAutistMedio.getRichiestaMedia().getMaggiorazioneNotturna().toString()}, request.getLocale())); 
		}
		String infoCorsa = "<strong>PREZZO:</strong> "+ Prezzo
		+"<br><strong>AUTOVEICOLO/I RICHIESTO/I:</strong> "+ AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(richAutistMedio.getRichiestaMediaAutistaAutoveicolo(), request);
		modelVelocity.put("infoCorsa", infoCorsa);
		modelVelocity.put("corsaPartenzaArrivo", CorsaAndataRitornoModelVelocity(richAutistMedio.getRichiestaMedia().getRicercaTransfert(), request.getLocale()));
		modelVelocity.put("notePerAutista", richAutistMedio.getRichiestaMedia().getRicercaTransfert().getNotePerAutista());
		String numOreInfoAutistaCliente = ApplicationMessagesUtil.DammiMessageSource("numero.ore.visualizzazione.contatto.autista", new String[]{ 
				gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber().toString()}, request.getLocale());
		modelVelocity.put("numOreInfoAutistaCliente", StringEscapeUtils.escapeHtml(numOreInfoAutistaCliente));
		modelVelocity.put("linkGestioneCorsaAutista", "<a href=\"https://"+ ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale())+"/"+Constants.URL_PRENOTA_CORSA+"?token="
				+richAutistMedio.getTokenAutista()+"\"><strong><big>Fai Click qui per Confermare/Cancellare la Corsa e Visualizzare i Contatti del Passeggero</big></strong></a>");
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, "context.name.apollotransfert", request.getLocale()));
		return modelVelocity;
    }
    
    
    public static String DatiFatturazioneEmail(HttpServletRequest request) {
    	return "<p>Eseguito il Servizio inviare Fattura a "+ApplicationMessagesUtil.DammiMessageSource("email.info.apollotransfert", request.getLocale())+"</p>"	
    	+"<p>"
		+"<b>INFORMAZIONI DI FATTURAZIONE</b><br>" 
		+"<b>Partita Iva:</b> "+ApplicationMessagesUtil.DammiMessageSource("partita.iva", request.getLocale())+"<br>"
		+"<b>Regime Fiscale:</b> "+ApplicationMessagesUtil.DammiMessageSource("regime.fiscale", request.getLocale()).toUpperCase()+"<br>"
		+"<b>Denominazione p.iva:</b> "+ApplicationMessagesUtil.DammiMessageSource("intestatario.ditta", request.getLocale()).toUpperCase()+"<br>"
		+"<b>Codice Fiscale:</b> "+ApplicationMessagesUtil.DammiMessageSource("codice.fiscale.matteo.manili", request.getLocale())+"<br>"
		+"<b>Indirizzo Sede:</b> "+ApplicationMessagesUtil.DammiMessageSource("indirizzo.sede", request.getLocale())+"<br>"
		+"<b>Email:</b> "+ApplicationMessagesUtil.DammiMessageSource("email.info.apollotransfert", request.getLocale())+"<br>"
		+"<b>Email PEC:</b> "+ApplicationMessagesUtil.DammiMessageSource("email.pec.info.apollotransfert", request.getLocale())
		+"</p>";
    }
    
    
    
    public static void InviaEmailConsigliaCorsa(String Email, RicercaTransfert ricercaTransfert, HttpServletRequest request, VelocityEngine velocityEngine) 
    		throws MessagingException, IOException {
		InviaEmail.InviaEmail_HTML(Email, "", Constants.VM_EMAIL_CONSIGLIA_CORSA, "Corsa Segnalata", 
			velocityEngine, DammiModelVelocity_InviaEmailCorsaSegnalataMarketing(ricercaTransfert, request, velocityEngine), null, null, false, 
			(CheckAmbienteVenditore(request.getServletContext())) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
    }
    
    /**
     * Invio Email Corsa Segnalata 
     */
    private static Map<String, Object> DammiModelVelocity_InviaEmailCorsaSegnalataMarketing(RicercaTransfert ricercaTransfert, HttpServletRequest request, 
    		VelocityEngine velocityEngine){
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
		String Domain = CheckAmbienteVenditore(request.getServletContext()) ?
				ApplicationMessagesUtil.DammiMessageSource("domain.ncctransferonline.name", request.getLocale()) :
					ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale());
		modelVelocity.put("linkPaginaCorsa", "<a href=\"https://"+ Domain +"/?courseId="+ricercaTransfert.getId()+"\">"
				+Domain+"/?courseId="+ricercaTransfert.getId()+ "</a>");
		modelVelocity.put("linkPaginaRicerca", "<a href=\"https://"+ Domain +"/"+"\">"+Domain+"/"+"</a>");
		modelVelocity.put("corsaPartenzaArrivo", CorsaAndataRitornoModelVelocity(ricercaTransfert, request.getLocale()));
		List<ResultMedio> richMed = HomeUtil.getResultAutistaTariffePrezzo(ricercaTransfert.getId()).getResultMedio();
		String prezziCategorie = "<ul>";
		Map<Long, String> descrizioneCategorieAutoMap = AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale());
		String DescrizioneCategorie = "";
		for(ResultMedio ite: richMed){
			if(ite.isShowClasseAutoveicolo()) {
				prezziCategorie = prezziCategorie 
						+"<li>"+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicolo().getNome(), request.getLocale())+", "
						+ApplicationMessagesUtil.DammiMessageSource(ite.getClasseAutoveicolo().getNome()+".desc", request.getLocale())+" "
						+ite.getPrezzoTotaleCliente()+"&euro;</li>";
				DescrizioneCategorie += "*"+descrizioneCategorieAutoMap.get(ite.getClasseAutoveicolo().getId())+"<br>";
			}
		}
		prezziCategorie = prezziCategorie + "</ul>";
		modelVelocity.put("prezziCategorie", prezziCategorie);
		modelVelocity.put("notePerAutista", ricercaTransfert.getNotePerAutista());
		modelVelocity.put("descrizioneCategorieAuto", DescrizioneCategorie);
		modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, request));
		return modelVelocity;
    }
    
    public static String CorsaAndataRitornoModelVelocity(RicercaTransfert ricercaTranfert, Locale locale) {
    	String corsaAndataRitorno = TextCorsaAndata("ANDATA:", ricercaTranfert, locale);
    	if(ricercaTranfert.isRitorno()) {
			corsaAndataRitorno = corsaAndataRitorno + TextCorsaRitorno("RITORNO:", ricercaTranfert, locale);
		}
		corsaAndataRitorno = corsaAndataRitorno + TextNumeroPasseggeri(ricercaTranfert, locale);
		return corsaAndataRitorno;
    }
    
    public static String CorsaAndataAgendaAutistaModelVelocity(RicercaTransfert ricercaTranfert, Locale locale) {
		return TextCorsaAndata("SOLO ANDATA:", ricercaTranfert, locale) + TextNumeroPasseggeri(ricercaTranfert, locale);
    }
    
    public static String CorsaRitornoAgendaAutistaModelVelocity(RicercaTransfert ricercaTranfert, Locale locale) {
		return TextCorsaRitorno("SOLO ANDATA:", ricercaTranfert, locale) + TextNumeroPasseggeri(ricercaTranfert, locale);
    }
    
    private static String TextCorsaAndata(String Title, RicercaTransfert ricercaTranfert, Locale locale){
    	return "<p><strong>"+Title+"</strong> "+ ricercaTranfert.getFormattedAddress_Partenza() +" &#8680; "+ ricercaTranfert.getFormattedAddress_Arrivo()
			+"<br><strong>Giorno Ora prelevamento:</strong> "+DateUtil.FORMATO_GIORNO_SETTIMANA_DATA_ORA(locale).format(ricercaTranfert.getDataOraPrelevamentoDate())
			+"<br><strong>Durata transfer prevista:</strong> "+ ricercaTranfert.getDurataConTrafficoText()
			+"<br><strong>Distanza:</strong> "+ ricercaTranfert.getDistanzaText()+"</p>";
    }
    
    private static String TextCorsaRitorno(String Title, RicercaTransfert ricercaTranfert, Locale locale){
		return "<p><strong>"+Title+"</strong> " + ricercaTranfert.getFormattedAddress_Arrivo() +" &#8680; "+ ricercaTranfert.getFormattedAddress_Partenza()
			+"<br><strong>Giorno Ora prelevamento:</strong> "+DateUtil.FORMATO_GIORNO_SETTIMANA_DATA_ORA(locale).format(ricercaTranfert.getDataOraRitornoDate())
			+"<br><strong>Durata transfer Ritorno prevista:</strong> "+ ricercaTranfert.getDurataConTrafficoTextRitorno()
			+"<br><strong>Distanza Ritorno:</strong> "+ ricercaTranfert.getDistanzaTextRitorno() +"</p>";
    }
    
    private static String TextNumeroPasseggeri(RicercaTransfert ricercaTranfert, Locale locale){
		return "<p><strong>Numero Passeggeri:</strong> "+ ricercaTranfert.getNumeroPasseggeri()+"</p>";
    }
    
    /**
     * InfoAutistaExtraEmail <br>
	 * Mi ritorna l'HTML da includere nel velocity con le informazioni di Tariffe, transfer venduti e transfer ricercati 
	 */
	public static String InfoAutistaExtraEmail(String[] Parametri, Autista autista, VelocityEngine velocityEngine, HttpServletRequest request) {
		Map<String, Object> modelVelocity = new HashMap<String, Object>();
        modelVelocity.put("linkInsertTariffe", "<a href=\""+ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", request.getLocale())+"/insert-tariffe\">"
        		+ "Per maggiori informazioni inerenti le Tariffe e le Categorie Auto visita la Pagina Tariffe.</a>" );
		// - Tariffe - new
        List<Autoveicolo> autoList = autoveicoloDao.getAutoveicoloByAutista(autista.getId(), false);
        List<AutistaZone> zoneList = autistaDao.lazyAutistaZone(autista.getId());
		List<Tariffe_Zone> tariffe_Zone_List = TariffeUtil.getTariffe_Zone_List_New(Parametri, zoneList, autoList, Constants.FASCE_KILOMETRICHE, Constants.KILOMETRI_CORSE);
        // mi assicuro che ci sia almento una tariffa inserita, altrimenti non passo al velocity il tariffe_Zone_List
        for(Tariffe_Zone tariffeZone : tariffe_Zone_List ){
        	for(Tariffe_AutoveicoloTariffa bb : tariffeZone.getTariffe_AutoveicoliTariffeList() ){
        		if(tariffeZone.getAutistaZona().isServizioAttivo() ){
        			modelVelocity.put("tariffe_Zone_List", tariffe_Zone_List);
        			break;
        		}
        		if(tariffeZone.getAutistaZona().getZoneLungaPercorrenza().isServizioAttivo() ){
        			modelVelocity.put("tariffe_Zone_List", tariffe_Zone_List);
        			break;
        		}
        	}
        }
        // - Ultimi 10 Transfer Venduti
        modelVelocity.put("ricercheTransfertVenduti", ricercaTransfertDao.getRicercaTransfertVenduti(10));
		// - Ultime 10 Ricerche Transfer nella sua provincia
        List<String> listSiglaProvincieAutista = new ArrayList<String>();
        for(Tariffe_Zone aa : tariffe_Zone_List){
        	if( aa.getAutistaZona() != null && aa.getAutistaZona().getProvince() != null ){
        	listSiglaProvincieAutista.add( aa.getAutistaZona().getProvince().getSiglaProvincia() );
        	}
        }
        log.debug("listSiglaProvincieAutista: "+listSiglaProvincieAutista);
        List<RicercaTransfert> ricercaTransfertRicercatiInProvinciaAutistaList = null;
        if( listSiglaProvincieAutista != null && listSiglaProvincieAutista.size() > 0 ){
        	ricercaTransfertRicercatiInProvinciaAutistaList =  
        		ricercaTransfertDao.getRicercaTransfertRicercatiInProvinciaAutista(listSiglaProvincieAutista);
        }
        if(ricercaTransfertRicercatiInProvinciaAutistaList != null
        		&& ricercaTransfertRicercatiInProvinciaAutistaList.size() > 0){
        	 modelVelocity.put("ricercheTransfertAutistaProvincia", ricercaTransfertRicercatiInProvinciaAutistaList);
        }else{
        	// - Ultime 10 Ricerche Transfer
        	modelVelocity.put("utimerRicercheTransfert", ricercaTransfertDao.UtimerRicercheTransfert());
        }
        // serve per usare la funzione date e quindi potere fare il format date
        modelVelocity.put("date", new DateTool());
		if(velocityEngine != null){
			return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, Constants.VM_EMAIL_AUTISTA_INFO_INCLUDE, Constants.ENCODING_UTF_8, modelVelocity);
		}else{
			return DammiTemplateVELOCITY(Constants.VM_EMAIL_AUTISTA_INFO_INCLUDE, modelVelocity); 
		}
	}
	
	
}

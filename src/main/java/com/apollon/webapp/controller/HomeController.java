package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.model.Aeroporti;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.GestioneApplicazione;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.TipoRuoli;
import com.apollon.model.User;
import com.apollon.service.AgA_TariffariManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RichiestaAutistaParticolareManager;
import com.apollon.service.RichiestaMediaManager;
import com.apollon.service.TipoRuoliManager;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.UtilString;
import com.apollon.util.customexception.GoogleMatrixException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.LoginGoogle;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.LoginFacebook_Profile_Bean;
import com.apollon.webapp.util.LoginFacebook_Profile_Modal;
import com.apollon.webapp.util.VerifyRecaptcha;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.MessaggioEsitoRicerca;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.home.HomeMarketingUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Aga;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.apollon.webapp.util.login.LoginAutomatic;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class HomeController extends BaseFormController {
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	private AgA_TariffariManager agA_TariffariManager;
	@Autowired
	public void setAgA_TariffariManager(AgA_TariffariManager agA_TariffariManager) {
		this.agA_TariffariManager = agA_TariffariManager;
	}
	
	private RichiestaAutistaParticolareManager richiestaAutistaParticolareManager;
	@Autowired
	public void setRichiestaAutistaParticolareManager(RichiestaAutistaParticolareManager richiestaAutistaParticolareManager) {
		this.richiestaAutistaParticolareManager = richiestaAutistaParticolareManager;
	}
	
	private RichiestaMediaManager richiestaMediaManager;
	@Autowired
	public void setRichiestaMediaManager(RichiestaMediaManager richiestaMediaManager) {
		this.richiestaMediaManager = richiestaMediaManager;
	}
	
	private TipoRuoliManager tipoRuoliManager;
    @Autowired
    public void setTipoRuoliManager(final TipoRuoliManager tipoRuoliManager) {
        this.tipoRuoliManager = tipoRuoliManager;
    }
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
	@Autowired
	public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
	
	private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	

	@RequestMapping(value={"/{lang}", "/{lang}/" }, method = RequestMethod.POST)
    public ModelAndView HomePOST(@ModelAttribute("ricercaTransfert") final RicercaTransfert ricercaTransfertForm, BindingResult errors, final HttpServletRequest request, 
    		final HttpServletResponse response, final String idRicTransfertDuplic, @PathVariable("lang") final String language) {
		long startTime = System.nanoTime(); 
		ModelAndView mav = new ModelAndView("home");
		log.info("sono in HomePost POST");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) { }
		try {
			RicercaTransfert ricTransfert = HomeUtil.CaricaDatiDaForm(request, idRicTransfertDuplic);
			ricTransfert.setRicercaRiuscita(false);
			ricTransfert.setPrenotazione(false);
			User user = null;
			if(request.getRemoteUser() != null) {
    			user = getUserManager().getUserByUsername(request.getRemoteUser());
			}
			
			/* System.out.println("------AMBIENTE VENDITORE----");
    		if(  ApplicationUtils.CheckAmbienteVenditore(getServletContext()) ) {
    			if( request.getSession().getAttribute(Constants.VENDITORE_ATTRIBUTE_NAME) != null ){ 
    				ricTransfert.setUserVenditore( getUserManager().get((Long)request.getSession().getAttribute(Constants.VENDITORE_ATTRIBUTE_NAME)) );
    			}else{
    				return new ModelAndView("redirect:/"+Constants.PAGE_HOME);
    			}
    		} */
			
			if(ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request)) {
				// RECAPTCHA request param
				String gRecaptchaResponse = request.getParameter(Constants.RECAPTCHA_RESPONSE);
				log.debug("g-recaptcha-response: "+gRecaptchaResponse);
				if( !VerifyRecaptcha.Verify(gRecaptchaResponse) && request.getParameter("ricerca-transfert") != null ){ //non togliere ricerca-transfert serve a mandare avanti gli step successivi
					RicercaTransfert ricercaTransfert = new RicercaTransfert();
					ricercaTransfert.setRicercaRiuscita(false);
					return CaricaFormAcquistoCorsa(mav, ricercaTransfert, null, request);
				}
			}
			
			if(request.getParameter("acquista-agenda-autista") != null) {
				String String0 = request.getParameter("acquista-agenda-autista").split("-")[0];
				String String1 = request.getParameter("acquista-agenda-autista").split("-")[1];
				Long idTariffarioAndata = !String0.equals("") && StringUtils.isNumeric(String0) ? Long.parseLong(String0) : null;
				Long idTariffarioRitorno = !String1.equals("") && StringUtils.isNumeric(String1)? Long.parseLong(String1) : null;
				
				ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
				// Nelle ricerch di andata e ritorno, nel caso in cui il cliente visualizza solo il ritorno perché l'andata non ci cono autisti, allora gli permetto 
				// di acquistarlo ma devo fare diventare ricTransfert una corsa di sola andata e spostare i parametri di ritorno in quelli di andata.
				// Potrei anche non modificare il ricTransfert senza invertire la andata e il ritorno ma poi devo fare molti test perchè ad esempio le email
				// e le varie pagine di frontend si aspettano la andata sempre valorizzata.
				if( ricTransfert.isRitorno() && idTariffarioAndata == null && idTariffarioRitorno != null ) {
					idTariffarioAndata = idTariffarioRitorno; idTariffarioRitorno = null;
					ricTransfert = HomeUtil_Aga.AgendaAutista_InvertiAndataRitorno_CancellaRitorno(ricTransfert);
				// Faccio questo controllo perché quando il cliente fa il login dalla pagina di ricerca, dopo il login fa un submit e idTariffarioAndata lo rimette a null 
				}else if( !ricTransfert.isRitorno() && idTariffarioAndata == null && idTariffarioRitorno != null  ) {
					idTariffarioAndata = idTariffarioRitorno; idTariffarioRitorno = null;
				}else if( ricTransfert.isRitorno() && idTariffarioRitorno == null  ) {
					ricTransfert.setRitorno(false);
				}
				AgendaAutistaScelta agendaAutistaScelta = HomeUtil_Aga.ResultAgendaAutista_From_Tariffari_CalcolaPercentuale(ricTransfert.getId(), idTariffarioAndata, idTariffarioRitorno);
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
				infoDatiPasseggero.put(Constants.AgendaAutista_TariffarioId_Andata, idTariffarioAndata);
				infoDatiPasseggero.put(Constants.AgendaAutista_TariffarioId_Ritorno, idTariffarioRitorno);
				infoDatiPasseggero.put(Constants.AgendaAutista_Percentuale_Servizio, agendaAutistaScelta.getPercentualeServizio());
				infoDatiPasseggero.put(Constants.AgendaAutista_PrezzoTotaleCliente_Temp, agendaAutistaScelta.getPrezzoTotaleCliente());
				ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
				ricTransfert.setTipoServizio(Constants.SERVIZIO_AGENDA_AUTISTA);
				ricercaTransfertManager.saveRicercaTransfert(ricTransfert);
				if(user != null) {
					ricTransfert.setPhoneNumberCustomer( user.getPhoneNumber() );
					ricTransfert.setVerificatoCustomer( true );
					ricTransfert = ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
				}
				if( !ricTransfert.isPagamentoEseguitoMedio() ){
					// info pagina
					ricTransfert.setTipoServizio( ricTransfert.getTipoServizio() );
					ricTransfert.setRicercaRiuscita(true);
					ricTransfert.setPrenotazione(true);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}else{
					// info pagina
					ricTransfert.setRicercaRiuscita(true);
					ricTransfert.setPrenotazione(true);
					ricTransfert.setRiepilogo(true);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}
			}
			
			if(request.getParameter("prenota-medio") != null) {
				long idClasseAutoveicolo = Long.parseLong( request.getParameter("prenota-medio").split("-")[0] );
				boolean pagamentoParziale = Boolean.parseBoolean( request.getParameter("prenota-medio").split("-")[1] );
				// ripulisco la classe autoveicolo scelta nel caso l'utente abbia cliccato indietro la pagina
				ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
				for(RichiestaMedia richMedia_ite: ricTransfert.getRichiestaMedia()){
					if(richMedia_ite.getClasseAutoveicolo().getId() == idClasseAutoveicolo){
						richMedia_ite.setClasseAutoveicoloScelta( true );
					}else{
						richMedia_ite.setClasseAutoveicoloScelta( false );
					}
					richiestaMediaManager.saveRichiestaMedia(richMedia_ite);
				}
				ricTransfert.setPagamentoParziale(pagamentoParziale);
				ricercaTransfertManager.saveRicercaTransfert(ricTransfert);
				if( !ControlloDateRicerca.ControlloDataRicercercaSuperioreDi24OreDaAdesso( ricTransfert.getDataRicerca()) ){
    				saveMessage(request, getText("errors.dataRicercaScaduta", request.getLocale()));
    				ricTransfert.setRicercaRiuscita(false);
    				ricTransfert.setPrenotazione(false);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
    			}
				Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraPrelevamento(), ricTransfert.getOraPrelevamento() );
				if( !ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(dataPrelevamento, Constants.SERVIZIO_STANDARD) ) {
					final long NUM_ORE = gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber();
					saveMessage(request, getText("errors.dataPartenzaDeveEssereUnOraSuccessivaDaAdesso", new Object[] { NUM_ORE },  request.getLocale() ) );
					ricTransfert.setRicercaRiuscita(false);
					ricTransfert.setPrenotazione(false);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}
				if(user != null){
					ricTransfert.setPhoneNumberCustomer( user.getPhoneNumber() );
					ricTransfert.setVerificatoCustomer( true );
					ricTransfert = ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
				}
				if( !ricTransfert.isPagamentoEseguitoMedio() ){
					// info pagina
					ricTransfert.setTipoServizio( ricTransfert.getTipoServizio() );
					ricTransfert.setRicercaRiuscita(true);
					ricTransfert.setPrenotazione(true);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}else{
					// info pagina
					ricTransfert.setRicercaRiuscita(true);
					ricTransfert.setPrenotazione(true);
					ricTransfert.setRiepilogo(true);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}
			}
			
			if(request.getParameter("richiedi-preventivi") != null) {
				log.debug("richiedi-preventivi");
				ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
				ricTransfert.setNotePerAutista( UtilString.TagliaVarChar1000( request.getParameter("noteAutista") )  );
				if ( HomeUtil.RichiediPreventivi(ricTransfert, request, user, velocityEngine) ) {
					saveMessage(request, "Richiesta Preventivi eseguita, riceverai i preventivi via email e via SMS");
				}else {
					saveMessage(request, "Preventvi NON inviati");
				}
				ricTransfert.setRicercaRiuscita(true);
				ricTransfert.setPrenotazione(false);
				return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
			}

			
			if(request.getParameter("google-signin-token") != null || request.getParameter("facebook-signin-token") != null) {
				ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
				if(ricTransfert.isVerificatoCustomer() == false ){
					String fullName = null, nome = null, cognome = null, email = null;
					if(request.getParameter("google-signin-token") != null) {
						GoogleIdToken.Payload payLoad = LoginGoogle.getPayload(request.getParameter("google-signin-token"));
						fullName = (String) payLoad.get("name"); // nome e cognome
						nome = (String) payLoad.get("given_name");
						cognome = (String) payLoad.get("family_name");
						email = payLoad.getEmail();
						
					}else if(request.getParameter("facebook-signin-token") != null) {
						String access_token = (String)request.getParameter("facebook-signin-token");
			    		LoginFacebook_Profile_Modal obj_Profile_Modal = new LoginFacebook_Profile_Modal();
			    		LoginFacebook_Profile_Bean obj_Profile_Bean = obj_Profile_Modal.call_me(access_token);
			    		fullName = obj_Profile_Bean.getName(); // nome e cognome
						nome = obj_Profile_Bean.getFirst_name();
						cognome = obj_Profile_Bean.getLast_name();
						email = obj_Profile_Bean.getEmail();
					}
		            if( email != null ) {
		            	JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
			            try {
			            	user = getUserManager().getUserByEmail( email );
			            	ricTransfert.setPhoneNumberCustomer( user.getPhoneNumber() );
		            		infoDatiPasseggero.put(Constants.RicTransfert_IdUser, user.getId());
		        			infoDatiPasseggero.put(Constants.RicTransfert_Email, user.getEmail());
		        			infoDatiPasseggero.put(Constants.RicTransfert_Nome, user.getFirstName());
		        			infoDatiPasseggero.put(Constants.RicTransfert_Cognome, user.getLastName());
		        			LoginAutomatic.loginAutomatic(user, request, request.getLocale());
			            	
			            }catch(UsernameNotFoundException unfe) {
		        			infoDatiPasseggero.put(Constants.RicTransfert_Nome, nome); // nome
		        			infoDatiPasseggero.put(Constants.RicTransfert_Cognome, cognome); // cognome
		        			infoDatiPasseggero.put(Constants.RicTransfert_Email, email);
			            }
		            	ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
		            	ricTransfert.setVerificatoCustomer( true );
		            	ricTransfert = ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
		            }
				}
				ricTransfert.setRicercaRiuscita(true);
				ricTransfert.setPrenotazione(true);
				return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
			}
			
			if(request.getParameter("verifica-sms-customer") != null) {
				//RichiestaAutistaParticolare richAutPart = null;
				ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
				String codiceVerificaSmsCustomer = request.getParameter("codice-verifica-sms-customer");
				String numTelCustomer = request.getParameter("number-tel-customer");
				numTelCustomer = numTelCustomer.replace(" ", "");
				if(ricTransfert.isVerificatoCustomer() == false ){
					if( ! ControlloDateRicerca.ControlloDataRicercercaSuperioreDi24OreDaAdesso( ricTransfert.getDataRicerca() ) ){
						// non posso mettere il errors.rejectValue perché nella pagina non sono visualizzati i campi
						saveMessage(request, getText("errors.dataRicercaScaduta",  locale ) );
						ricTransfert.setRicercaRiuscita(false);
						ricTransfert.setPrenotazione(false);
						return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
	    			}
					if(user == null){
						if(numTelCustomer.length() > 7 && ricTransfert.getPhoneNumberCustomer() != null && ricTransfert.getPhoneNumberCustomer().contains(numTelCustomer) &&
								ricTransfert.getTokenCustomer() != null && ricTransfert.getTokenCustomer().equals(codiceVerificaSmsCustomer)){
							ricTransfert.setVerificatoCustomer( true );
							ricTransfert = ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
							saveMessage(request, "Numero Confermato."  );
						}else{
							saveMessage(request, "Conferma numero Telefono.");
						}
					}else{
						ricTransfert.setVerificatoCustomer( true );
						ricTransfert.setPhoneNumberCustomer( user.getPhoneNumber() );
						ricTransfert = ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
					}
				}
				ricTransfert.setRicercaRiuscita(true);
				ricTransfert.setPrenotazione(true);
				return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
			}
			
			if(request.getParameter("applica-sconto") != null) {
				System.out.println("applica sconto");
				RichiestaAutistaParticolare richAutPart = null; String idAutistPart = request.getParameter("id-autista-part");
				if(idAutistPart == null || idAutistPart.equals("")) {
					ricTransfert = ricercaTransfertManager.get( Long.parseLong(request.getParameter("id")) );
					System.out.println("ricTransfert.getInfoPasseggero(): "+ricTransfert.getInfoPasseggero() + " ricTransfert.getVecchioPrezzo(): "+ricTransfert.getVecchioPrezzo());
					ricTransfert = HomeUtil.ElaboraSconto(ricTransfert, request.getParameter("codice-sconto"));
				}else{
					richAutPart = richiestaAutistaParticolareManager.get( Long.parseLong( idAutistPart ) );
					ricTransfert = richAutPart.getRicercaTransfert();
					// fare codice sconto per RichiestaAutistaParticolare...
				}
				
				ricTransfert.setRicercaRiuscita(true);
				ricTransfert.setPrenotazione(true);
				if(richAutPart == null){
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}else{
					return CaricaFormAcquistoCorsa(mav, richAutPart.getRicercaTransfert(), richAutPart, request);
				}
			}
			
			if(request.getParameter("pagamento-servizio") != null) {
				return PagamentoServizio(ricTransfert, request, user, mav);
			}
			
			DammiTempoOperazione.DammiSecondi(startTime, "HomeController-1");
			// CONTROLLI
			errors = HomeUtil.Controlli_Ricerca(ricTransfert, errors);

			DammiTempoOperazione.DammiSecondi(startTime, "HomeController-2");
			try{
				mav = HomeUtil.Controlli_Transfert(mav, ricTransfert, errors, locale);
				if(errors != null && errors.getErrorCount() > 0 ){
					mav.addAllObjects(errors.getModel());
					ricTransfert.setRicercaRiuscita(false);
					ricTransfert.setTipoServizio(Constants.SERVIZIO_STANDARD);
					return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
				}
			}
			catch (final GoogleMatrixException googleMatrixException) {
	        	googleMatrixException.printStackTrace();
	    		saveError(request, getText("errors.googlemaps.matrix.temporary.unavailable", locale));
	    		return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
	    	}catch (final UnknownHostException UnknownHostExc) {
	    		UnknownHostExc.printStackTrace();
	    		saveError(request, "Connessione Assente");
	    		return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
	    	}
			
			DammiTempoOperazione.DammiSecondi(startTime, "HomeController-3");
			
			ricTransfert = HomeUtil.Calcolo_e_Salvataggio_Ricerca(ricTransfert);
			
			DammiTempoOperazione.DammiSecondi(startTime, "HomeController-4");
			
			if(ricTransfert.getMessaggiEsitoRicerca() != null && ricTransfert.getMessaggiEsitoRicerca().size() > 0) {
				for(MessaggioEsitoRicerca mess : ricTransfert.getMessaggiEsitoRicerca()) {
					saveMessage(request, HomeUtil.DammiMessaggiEsistoTransfert(mess, locale));
				}
				//ricTransfert.setRicercaRiuscita(false);
			}
			return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);

		}catch(final NumberFormatException numberFormatExce) {
			numberFormatExce.printStackTrace();
    		saveError(request, getText("errors.save", locale));
    		return new ModelAndView("redirect:/");
    	}catch(final DataIntegrityViolationException dataIntegrViolException) {
    		dataIntegrViolException.printStackTrace();
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return new ModelAndView("redirect:/");
        }catch(final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
    		return new ModelAndView("redirect:/");
        }
    }

	
	private ModelAndView CaricaFormAcquistoCorsa(ModelAndView mav, RicercaTransfert ricercaTransfert, RichiestaAutistaParticolare richiestaAutParticolare, HttpServletRequest request) throws Exception{
		long startTime = System.nanoTime();
		mav.addObject("AMBIENTE_PRODUZIONE", ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request)); 
		mav.addObject("RECAPTCHA_PUBLIC", ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ? Constants.RECAPTCHA_PUBLIC : null);
		
		User user = null;
		if(request.getRemoteUser() != null) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		}
		GMaps_Api GMaps_Api = new GMaps_Api();
		mav = GMaps_Api.AddAttribute_GoogleApiMap_JS(mav);
		// questo mi serve a non il recaptcha in ambiente di test 
		
		mav.addObject("FB_APP_ID", Constants.FB_APP_ID); // login facebook
		mav.addObject("GOOGLE_CLIENT_ID", Constants.GOOGLE_CLIENT_ID); // login google
		
		
		mav.addObject("user", user);
		if(ricercaTransfert.isPrenotazione() == false) {
			mav.addObject("recensioniApprovate", ricercaTransfertManager.Recencioni_Approvate(false) ); 
		}
        mav.addObject("ricercaTransfert", ricercaTransfert);
        mav.addObject("richAutistPart", richiestaAutParticolare);
       
        List<GestioneApplicazione> gestApplicazList = gestioneApplicazioneManager.getGestioneApplicazione_senzaCommenti();
        
        //mav.addObject("margineOreMinimoCorsaMedia", gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber());
        mav.addObject("margineOreMinimoCorsaMedia", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber());
        
        //mav.addObject("numAutistiCorsaMedia", gestioneApplicazioneManager.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber());
        mav.addObject("numAutistiCorsaMedia", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber());
        
        //mav.addObject("numOreInfoAutistaCliente", gestioneApplicazioneManager.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber());
        mav.addObject("numOreInfoAutistaCliente", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber());
       
        //mav.addObject("maxOreDisdettaCliente", gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber());
        mav.addObject("maxOreDisdettaCliente", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber());
        
        //mav = RitardiUtil.AddAttribute_Ritardi(mav);
		mav = RitardiUtil.AddAttribute_Ritardi(mav, 
				new BigDecimal( GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueString() ),
				GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueNumber());
		
        mav.addObject("descrizioneCategorieAutoMap", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()));
		String[] progressTracker = new String[20];
		if(ricercaTransfert.isRicercaRiuscita() ){
			progressTracker[1] = "step-active";
			progressTracker[2] = "";
			progressTracker[3] = "";
		}
		if(ricercaTransfert.isPrenotazione()) {
			String CredenzialiStripe = GestioneApplicazioneUtil
					.CredenzialiPublishableStripe(GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber());
			mav.addObject("STRIPE_PUBLISCHABLE_KEY", CredenzialiStripe);

			String CredenzialiPayPal = GestioneApplicazioneUtil
					.CredenzialiWebButtonPayPal(GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber());
			mav.addObject("ID_CLIENT", CredenzialiPayPal.split("#")[0]); 
			mav.addObject("TYPE_ENV", CredenzialiPayPal.split("#")[1]);
			progressTracker[1] = "step-active";
			progressTracker[2] = "step-active";
			progressTracker[3] = "";
		}
		if(ricercaTransfert.isRiepilogo()) {
			progressTracker[1] = "step-active";
			progressTracker[2] = "step-active";
			progressTracker[3] = "step-active";
		}
		mav.addObject("progressTracker", progressTracker);
		long smsSkebbyAbilitato = GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "INVIO_SMS_ABILITATO").getValueNumber();
		if(smsSkebbyAbilitato == 0l){
			mav.addObject("smsSkebbyAbilitato", false);
		}else{
			mav.addObject("smsSkebbyAbilitato", true);
		}
		long stripeSWITCH = GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		if(stripeSWITCH == 0l){
			mav.addObject("stripeSWITCH", false);  
		}else{
			mav.addObject("stripeSWITCH", true);  
		}
		long payPalSWITCH = GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		if(payPalSWITCH == 0l){
			mav.addObject("payPalSWITCH", false);
		}else{
			mav.addObject("payPalSWITCH", true);
		}
		if(ricercaTransfert.isRitorno() && ricercaTransfert.getDataOraPrelevamentoDate() != null && ricercaTransfert.getDurataConTrafficoValue() != 0){
			mav.addObject("dataScontoAppicabile", ControlloDateRicerca.ControlloDataRitornoOrarioSconto(ricercaTransfert, 
					(int) (long) GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUMERO_MAX_ORE_ATTESA_RITORNO_PER_SCONTO").getValueNumber()));
			mav.addObject("numMaxOreAttesaRitorno", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "NUMERO_MAX_ORE_ATTESA_RITORNO_PER_SCONTO").getValueNumber());  
			mav.addObject("percentualeScontoRitorno", GestioneApplicazioneUtil.getNameGestApplicaz(gestApplicazList, "PERCENTUALE_SCONTO_RITORNO").getValueNumber());
		}
		DammiTempoOperazione.DammiSecondi(startTime, "CaricaFormAcquistoCorsa-1");
    	return mav;
    }
    
	
	@RequestMapping(value={"/{lang}", "/{lang}/"}, method = RequestMethod.GET)
    public ModelAndView HomeGET( final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(value = "courseId", required = false) final String courseId,
    		@RequestParam(value = "tokenRicTransfert", required = false) final String tokenRicTransfert,
    		@RequestParam(value = "richPartTokenAutista", required = false) final String richPartTokenAutista,
    		@RequestParam(value = "richPartTokenAutistaMultiplo", required = false) final String richPartTokenAutistaMultiplo,
    		@RequestParam(value = "placeId", required = false) final String placeId,
    		@PathVariable("lang") final String language) throws Exception {
    	log.info("sono in HomeGET GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("home");
    	try{
    		RicercaTransfert ricercaTransfertGet = new RicercaTransfert();
    		Long idVenditore = (Long)request.getSession().getAttribute(Constants.VENDITORE_ATTRIBUTE_NAME);
    		if( idVenditore != null ){
    			User userVenditore = getUserManager().get( idVenditore );
    			ricercaTransfertGet.setUserVenditore(userVenditore);
    		}
    		ricercaTransfertGet.setRicercaRiuscita(false);
    		ricercaTransfertGet.setTipoServizio(Constants.SERVIZIO_STANDARD);

    		// esempio: https://localhost:8443/apollon/?courseId=9924&tokenRicTransfert=9924EiFUhQpxKBWH2JB&richPartTokenAutista=parSYztAmkOOevFfOU
    		if( courseId != null ) {
    			ricercaTransfertGet = ricercaTransfertManager.get(Long.parseLong(courseId));
    			if( ricercaTransfertGet != null 
    				&& (ricercaTransfertGet.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricercaTransfertGet.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) 
    				&& tokenRicTransfert != null && ricercaTransfertGet.getRicTransfert_Token() != null && ricercaTransfertGet.getRicTransfert_Token().equals(tokenRicTransfert) 
    				&& ricercaTransfertGet.getRichiestaPreventivi_Inviata() != null && ricercaTransfertGet.getRichiestaPreventivi_Inviata() == true ) {
    				if( ricercaTransfertGet.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
	    				RichiestaAutistaParticolare richAutPart = richiestaAutistaParticolareManager.getRichiestaAutista_by_token(richPartTokenAutista);
	    				if(richAutPart != null && HomeUtil.Controllo_Preventivo_validita_data(richAutPart)
	    						&& richAutPart.getRicercaTransfert().getId() == Long.parseLong(courseId) 
	    						&& richAutPart.getPreventivo_inviato_cliente() != null && richAutPart.getPreventivo_inviato_cliente() == true) {
		    				ricercaTransfertGet.setRicercaRiuscita( true );
		    				ricercaTransfertGet.setPrenotazione(true);
		    				if( ricercaTransfertGet.isPagamentoEseguitoMedio() ) {
		    					ricercaTransfertGet.setRiepilogo(true);
		    					saveMessage(request, "Corsa Acquistata");
		    				}
		    				return CaricaFormAcquistoCorsa(mav, ricercaTransfertGet, richAutPart, request);
	    				}else if( richAutPart != null && ! HomeUtil.Controllo_Preventivo_validita_data(richAutPart) ) {
	    					saveMessage(request, getText("richiesta.part.preventivo.scaduto.data.superata", request.getLocale()) );
	    				}else {
	    					saveMessage(request, "Preventivo non Valido");
	    				}
    				} else if( ricercaTransfertGet.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
    					Integer numeroPasseggeriCoperti = 0;
    					String[] parts = richPartTokenAutistaMultiplo.split("-");
    					RichiestaAutistaParticolare richAutistaPart = new RichiestaAutistaParticolare();
    					List<RichiestaAutistaParticolare> richAutistaPartMultiploList = new ArrayList<RichiestaAutistaParticolare>();
    					BigDecimal prezzoTotaleClienteMultiplo = new BigDecimal(0);
    					boolean acquistoValido = false;
    					for(String ite: parts) {
    						System.out.println("ite:"+ ite );
    						RichiestaAutistaParticolare richAutPart = richiestaAutistaParticolareManager.getRichiestaAutista_by_token(ite);
    						if(richAutPart != null && HomeUtil.Controllo_Preventivo_validita_data(richAutPart)
    	    						&& richAutPart.getRicercaTransfert().getId() == Long.parseLong(courseId) 
    	    						&& richAutPart.getPreventivo_inviato_cliente() != null && richAutPart.getPreventivo_inviato_cliente() == true) {
    							numeroPasseggeriCoperti = numeroPasseggeriCoperti + richAutPart.getAutoveicolo().getNumeroPostiPasseggeri();
    							richAutistaPartMultiploList.add(richAutPart);
    							prezzoTotaleClienteMultiplo = prezzoTotaleClienteMultiplo.add(richAutPart.getPrezzoTotaleCliente());
    							acquistoValido = true;
    						}else {
    							if( richAutPart != null && ! HomeUtil.Controllo_Preventivo_validita_data(richAutPart) ) {
    		    					saveMessage(request, getText("richiesta.part.preventivo.scaduto.data.superata", request.getLocale()));
    		    				}else {
    		    					saveMessage(request, "Preventivo non Valido");
    		    				}
    							acquistoValido = false;
    							break;
    						}
    					}
    					if( acquistoValido && numeroPasseggeriCoperti >= ricercaTransfertGet.getNumeroPasseggeri() ) {
    						ricercaTransfertGet.setRicercaRiuscita(true);
		    				ricercaTransfertGet.setPrenotazione(true);
		    				if( ricercaTransfertGet.isPagamentoEseguitoMedio() ) {
		    					ricercaTransfertGet.setRiepilogo(true);
		    					saveMessage(request, "Corsa Acquistata");
		    				}
		    				richAutistaPart.setRichiestaAutistaParticolareMultiploList(richAutistaPartMultiploList);
		    				mav.addObject("prezzoTotaleClienteMultiplo", prezzoTotaleClienteMultiplo);  
		    				return CaricaFormAcquistoCorsa(mav, ricercaTransfertGet, richAutistaPart, request);
    					}else {
    						saveMessage(request, "Numero Passeggeri non Raggiunto");
    						return CaricaFormAcquistoCorsa(mav, ricercaTransfertGet, new RichiestaAutistaParticolare(), request);
    					}
    				}
    			}
    			
    		}else if(placeId != null && !placeId.equals("")) {
    			Aeroporti aero = null; PortiNavali porto = null; Musei museo = null;
    			RicercaTransfert_GoogleMaps_Info RicTransf_GM_info = new RicercaTransfert_GoogleMaps_Info();
    			Object obj = ricercaTransfertManager.getInfrastrutturaBy_PlaceId( placeId );
    			if(obj != null){
	    			if(obj.getClass().equals(Aeroporti.class)){
	    				aero = (Aeroporti) obj;
	    				ricercaTransfertGet.setArrivoRequest(aero.getNomeAeroporto());
	    				ricercaTransfertGet.setPlace_id_Arrivo( aero.getPlaceId() );
	    			}
	    			if(obj.getClass().equals(PortiNavali.class)){
	    				porto = (PortiNavali) obj;
	    				ricercaTransfertGet.setArrivoRequest(porto.getNomePorto());
	    				ricercaTransfertGet.setPlace_id_Arrivo( porto.getPlaceId() );
	    			}
	    			if(obj.getClass().equals(Musei.class)){
	    				museo = (Musei) obj;
	    				ricercaTransfertGet.setArrivoRequest(museo.getNomeMuseo());
	    				ricercaTransfertGet.setPlace_id_Arrivo( museo.getPlaceId() );
	    			}
    			}else{
		    		RicTransf_GM_info.setPlace_id(placeId);
		    		GMaps_Api GMaps_Api = new GMaps_Api();
		    		RicTransf_GM_info = GMaps_Api.GoogleMaps_PlaceDetails(RicTransf_GM_info, locale.getLanguage());
		    		ricercaTransfertGet.setArrivoRequest( RicTransf_GM_info.getName() );
		    		ricercaTransfertGet.setPlace_id_Arrivo( RicTransf_GM_info.getPlace_id() );
		    		//ricercaTransfertGet.setArrivoRequest( RicTransf_GM_info.getFormattedAddress() );
    			}
    		}
    		return CaricaFormAcquistoCorsa(mav, ricercaTransfertGet, null, request);

    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return CaricaFormAcquistoCorsa(mav, new RicercaTransfert(), null, request);
    	}
    }
    
    
    
    /**
     * Pagamento Stripe Agenda Autista
     */
    private ModelAndView PagamentoServizio(RicercaTransfert ricTransfert, HttpServletRequest request, User user, ModelAndView mav) throws Exception{
    	log.debug("PagamentoAgendaAutistaStripe");
		String idRicercaTransfert = request.getParameter("id-ricerca-transfert");
		String paymentIntentId = request.getParameter("paymentIntentId");
		String payPalPaymentId = request.getParameter("payment-paypal-id");
		String payPalPaymentError = request.getParameter("payment-paypal-error");
		try{
			ricTransfert = ricercaTransfertManager.get(Long.parseLong(idRicercaTransfert));
			RichiestaAutistaParticolare richAutPart = new RichiestaAutistaParticolare(); 
			BigDecimal totBigDecimal = null; BigDecimal prezzoCommissioneApollo = null;
			
			if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA )) {
				AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
				totBigDecimal = agendaAutistaScelta.getPrezzoTotaleCliente();
				prezzoCommissioneApollo = agendaAutistaScelta.getPrezzoCommissioneServizioTotale();
	    		
	    	}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD )) {
	    		RichiestaMedia richMedia = richiestaMediaManager.getRichiestaMedia_by_IdRicercaTransfert( ricTransfert.getId() );
				totBigDecimal = richMedia.getPrezzoTotaleCliente();
				String totaleEuro = richMedia.getPrezzoTotaleCliente().toString();
				if(ricTransfert.isPagamentoParziale()){
					totBigDecimal = richMedia.getPrezzoCommissioneServizio();
					totaleEuro = richMedia.getPrezzoCommissioneServizio().toString();
				}
				prezzoCommissioneApollo = richMedia.getPrezzoCommissioneServizio();
				totaleEuro = totaleEuro.replace(".", ""); // questo perche stripe prende le valute in centesimi, per esempio 3.30 euro sono 330 centesimi
	    		
	    	}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
				richAutPart = richiestaAutistaParticolareManager.get(Long.parseLong(request.getParameter("id-autista-part")));
				totBigDecimal = richAutPart.getPrezzoTotaleCliente();
				prezzoCommissioneApollo = richAutPart.getPrezzoCommissioneServizio().add( richAutPart.getPrezzoCommissioneServizioIva() );
				
			}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
				String[] parts = request.getParameter("id-autista-part").split("-");
				List<RichiestaAutistaParticolare> richAutistaPartMultiploList = new ArrayList<RichiestaAutistaParticolare>();
				for(String ite: parts) {
					RichiestaAutistaParticolare richAutPart_Multiplo = richiestaAutistaParticolareManager.getRichiestaAutista_by_token(ite);
					totBigDecimal = totBigDecimal.add(richAutPart_Multiplo.getPrezzoTotaleCliente());
					prezzoCommissioneApollo = prezzoCommissioneApollo.add(richAutPart_Multiplo.getPrezzoCommissioneServizio().add(richAutPart_Multiplo.getPrezzoCommissioneServizioIva())) ;
					richAutistaPartMultiploList.add(richAutPart_Multiplo);
				}
				richAutPart.setRicercaTransfert(ricTransfert);
				richAutPart.setRichiestaAutistaParticolareMultiploList(richAutistaPartMultiploList);
			}
			// ERRORE PAYPAL
			if( /*true ||*/ payPalPaymentError != null && (payPalPaymentId == null || payPalPaymentId.equals("")) ){
				ricTransfert.setRicercaRiuscita(true);
				ricTransfert.setPrenotazione(true);
				ricTransfert.setRiepilogo(false);
				saveMessage(request, "pagamento PayPal non eseguito: "+request.getParameter("payment-error")  );
				return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
			}
			if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				ricTransfert.setInfoPasseggero( HomeUtil.RicercaTransfert_SetRichiestaAutistaParticolare_Id(ricTransfert, richAutPart) );
			}
			
			// MEMORIZZO DATI CLINTE
			HomeUtil.SalvaDatiCliente(ricTransfert, request);
			saveMessage(request, "Corsa Confermata, Pagamento Eseguito: "+totBigDecimal+"€");
			ricTransfert.setPagamentoEseguitoMedio(true);
			
			// MEMORIZZO DATI ID E TIPO PROVIDER PAGAMENTO
			JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
			if( paymentIntentId != null ) {
				infoDatiPasseggero.put(Constants.PaymentProviderIdJSON, paymentIntentId);
				infoDatiPasseggero.put(Constants.PaymentProviderTipoJSON, Constants.TIPO_PAYMENT_STRIPE_2);
			}else if( payPalPaymentId != null ) {
				infoDatiPasseggero.put(Constants.PaymentProviderIdJSON, payPalPaymentId);
				infoDatiPasseggero.put(Constants.PaymentProviderTipoJSON, Constants.TIPO_PAYMENT_PAYPAL_2);
			}
			ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
			
			ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
			if( ricTransfert.getAgendaAutista_TariffarioAndata_Id() != null ) {
				AgA_Tariffari tariffarioAndata = agA_TariffariManager.get( ricTransfert.getAgendaAutista_TariffarioAndata_Id() );
				tariffarioAndata.setRicercaTransfertAcquistato( ricTransfert );
				agA_TariffariManager.saveAgA_Tariffari(tariffarioAndata);
			}
			if( ricTransfert.getAgendaAutista_TariffarioRitorno_Id() != null ) {
				AgA_Tariffari tariffarioRitorno = agA_TariffariManager.get( ricTransfert.getAgendaAutista_TariffarioRitorno_Id() );
				tariffarioRitorno.setRicercaTransfertAcquistato( ricTransfert );
				agA_TariffariManager.saveAgA_Tariffari(tariffarioRitorno);
			}
			if(user == null){
				// --------------- CREO UTENTE ---------------
				if( !getUserManager().userTelephoneExist( ricTransfert.getPhoneNumberCustomer() ) ){
					user = HomeUtil.CreaUtente(ricTransfert, request.getLocale());
					//salvo l'utente
					user = getUserManager().saveUser(user); 
					// log user in automatically
					request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);
					LoginAutomatic.loginAutomatic(user, request, request.getLocale());
					saveMessage(request, getText("user.registered.post.payment", request.getLocale()) );
				}else{
					user = (User)getUserManager().loadUserByTelephone(ricTransfert.getPhoneNumberCustomer());
					// se non contiente CLIENTE_ROLE, allora lo aggiungo e salvo
					Set<TipoRuoli> tipoRuoliUser = user.getTipoRuoli();
					if(! tipoRuoliUser.contains( tipoRuoliManager.getTipoRuoliByName( Constants.CLIENTE ) )){
						user.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE) );
						user = getUserManager().saveUser(user);
					}
					LoginAutomatic.loginAutomatic(user, request, request.getLocale());
				}
			}else{
				// se non contiente CLIENTE_ROLE, allora lo aggiungo e salvo
				Set<TipoRuoli> tipoRuoliUser = user.getTipoRuoli();
				if(! tipoRuoliUser.contains(tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE))){
					user.addTipoRuoli(tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE));
					user = getUserManager().saveUser(user);
				}
			}
			ricTransfert.setUser(user);
			ricercaTransfertManager.saveRicercaTransfert( ricTransfert );
			ricTransfert.setRicercaRiuscita(true);
			ricTransfert.setPrenotazione(true);
			ricTransfert.setRiepilogo(true);
			Opereazione_Post_Vendita(request, ricTransfert, richAutPart, prezzoCommissioneApollo);
			return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request)
					.addObject("googleConvPagamentoEseguito", true).addObject("prezzoCommissioneApollo", prezzoCommissioneApollo);
			
		}catch (Exception e) {
			saveError(request,  "errore Generale.");
			e.printStackTrace();
			idRicercaTransfert = request.getParameter("id-ricerca-transfert");
			ricTransfert = ricercaTransfertManager.get( Long.parseLong( idRicercaTransfert ) );
			ricTransfert.setRicercaRiuscita(true);ricTransfert.setPrenotazione(true);ricTransfert.setRiepilogo(false);
			return CaricaFormAcquistoCorsa(mav, ricTransfert, null, request);
		}
    }
    
   
    private void Opereazione_Post_Vendita(HttpServletRequest request, RicercaTransfert ricTransfert, RichiestaAutistaParticolare richAutPart, BigDecimal prezzoCommissioneApollo) 
    		throws Exception {
    	if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA )) {
        	HomeUtil.SetTokenCodiceSconto_Usato(ricTransfert.getCodiceSconto());
        	Fatturazione.CreaFattura( ricTransfert );
        	HomeUtil_Sms_Email.InviaSmsAvvisoMatteoCorsaVenduta(request, ricTransfert, prezzoCommissioneApollo.toString());
        	HomeUtil_Sms_Email.Invia_Sms_Email_AgendaAutistaCorsaAcquistata_Autista(ricTransfert, request, velocityEngine);
    		HomeUtil_Sms_Email.Security_InviaEmailCorsaAcquistata_Cliente(request, ricTransfert, velocityEngine);
    		//Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_SMS_Cliente_Autista(ricTransfert, request.getLocale());
    		Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_EMAIL_ScriviRecensioneTransfer(ricTransfert, request.getLocale());
    		
    	}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD )) {
    		HomeUtil.SetTokenCodiceSconto_Usato(ricTransfert.getCodiceSconto());
        	Fatturazione.CreaFattura( ricTransfert );
        	HomeUtil_Sms_Email.InviaSmsAvvisoMatteoCorsaVenduta(request, ricTransfert, prezzoCommissioneApollo.toString());
        	HomeUtil_Sms_Email.Invia_Sms_Email_CorsaDisponibileAutistiMedio(ricTransfert, request, velocityEngine);
    		HomeUtil_Sms_Email.Security_InviaEmailCorsaAcquistata_Cliente(request, ricTransfert, velocityEngine);
    		Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_SMS_Cliente_Autista(ricTransfert, request.getLocale());
    		Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_EMAIL_ScriviRecensioneTransfer(ricTransfert, request.getLocale());
    		
    	}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
    		Fatturazione.CreaFattura( ricTransfert );
        	HomeUtil_Sms_Email.InviaSmsAvvisoMatteoCorsaVenduta(request, ricTransfert, prezzoCommissioneApollo.toString());
        	HomeUtil_Sms_Email.InviaEmailRichiestaPreventivoAcquistato_Autista(richAutPart, request, velocityEngine);
        	HomeUtil_Sms_Email.Security_InviaEmailCorsaAcquistata_Cliente(request, ricTransfert, velocityEngine);
        	//TODO DA FARE !!!!! NELLE CORSE PARTICOLARI E MULTIPLE GLI SMS DI AVVISO AI CLIENTI E AGLI AUTISTI NON LI INVIA (DA FAREEEEEE)
    		//Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_SMS_Cliente_Autista(ricTransfert, request.getLocale());
    		//Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_EMAIL_ScriviRecensioneTransfer(ricTransfert, request.getLocale());
    		
    	}
    
    }
    
} //fine classe

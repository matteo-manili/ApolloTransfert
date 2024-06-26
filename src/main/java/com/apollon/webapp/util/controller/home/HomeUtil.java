package com.apollon.webapp.util.controller.home;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.dao.ComuniDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.NazioniDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RegioniDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.dao.RoleDao;
import com.apollon.dao.TipoRuoliDao;
import com.apollon.dao.UserDao;
import com.apollon.dao.VisitatoriDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Autista;
import com.apollon.model.Comuni;
import com.apollon.model.Nazioni;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.model.User;
import com.apollon.model.Visitatori;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilBukowski;
import com.apollon.util.UtilString;
import com.apollon.util.customexception.GoogleMatrixException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.OperazioniThread_C;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
import com.apollon.webapp.util.bean.RisultatoAutistiParticolare;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.MessaggioEsitoRicerca;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class HomeUtil extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(HomeUtil.class);
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	private static RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	private static RichiestaMediaDao richiestaMediaDao = (RichiestaMediaDao) contextDao.getBean("RichiestaMediaDao");
	private static UserDao userDao = (UserDao) contextDao.getBean("userDao");
	private static RoleDao roleDao = (RoleDao) contextDao.getBean("RoleDao");
	private static TipoRuoliDao tipoRuoliDao = (TipoRuoliDao) contextDao.getBean("TipoRuoliDao");
	private static VisitatoriDao visitatoriDao = (VisitatoriDao) contextDao.getBean("VisitatoriDao");
	private static ComuniDao comuniDao = (ComuniDao) contextDao.getBean("ComuniDao");
	private static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	private static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	private static NazioniDao nazioniDao = (NazioniDao) contextDao.getBean("NazioniDao");
	
	/**
	 * Mi ritorna TRUE se la data attuale è inferiore alla data "Validita Preventivo" inserita dall'autista (SIGNIFICA CHE IL PREVENTIVO è VALIDO PER ESSERE ACQUISTATO)
	 * @param richAutPart
	 * @return
	 */
	public static boolean Controllo_Preventivo_validita_data(RichiestaAutistaParticolare richAutPart) {
		if(richAutPart.getPreventivo_validita_data() != null && richAutPart.getPreventivo_validita_data() > new Date().getTime()) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String RicercaTransfert_SetRichiestaAutistaParticolare_Id(RicercaTransfert ricTransfert, RichiestaAutistaParticolare richAutPart) {
		JSONObject infoDatiPasseggero = GetInfoDatiPasseggero(ricTransfert);
		if(richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
			infoDatiPasseggero.put(Constants.RichiestaAutistaParticolare_Id, richAutPart.getId());
			return infoDatiPasseggero.toString();
		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
			JSONArray jsArray = new JSONArray();
			for(RichiestaAutistaParticolare ite: richAutPart.getRichiestaAutistaParticolareMultiploList()) {
				jsArray.put( ite.getId() );
			}
			infoDatiPasseggero.put(Constants.RichiestaAutistaMultiplo_Id, jsArray);
			return infoDatiPasseggero.toString();
		}else {
			return null;
		}
	}
	
	public static boolean RichiediPreventivi(RicercaTransfert ricTransfert, HttpServletRequest request, User user, VelocityEngine velocityEngine) throws Exception {
		boolean preventiviInviati = false;
		JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
		if(user != null) {
			infoDatiPasseggero.put(Constants.RicTransfert_IdUser, user.getId());
			infoDatiPasseggero.put(Constants.RicTransfert_Email, user.getEmail());
			infoDatiPasseggero.put(Constants.RicTransfert_Nome, user.getFirstName());
			infoDatiPasseggero.put(Constants.RicTransfert_Cognome, user.getLastName());
			ricTransfert.setPhoneNumberCustomer( user.getPhoneNumber() );
		}else if(ricTransfert.isVerificatoCustomer()) {
			infoDatiPasseggero.put(Constants.RicTransfert_Email, UtilString.RimuoviTuttiGliSpazi(request.getParameter("cliente-email")));
			infoDatiPasseggero.put(Constants.RicTransfert_Nome, request.getParameter("cliente-firstName"));
			infoDatiPasseggero.put(Constants.RicTransfert_Cognome, request.getParameter("cliente-lastName"));
			if( ricTransfert.getPhoneNumberCustomer() == null && request.getParameter("cliente-telefono") != null && !request.getParameter("cliente-telefono").equals("") ) {
				ricTransfert.setPhoneNumberCustomer( request.getParameter("cliente-telefono") );
			}
		}else {
			return false;
		}
		// controllo se il (telefonoCustomer o la email o l'indirizzo IP) OPPURE IdUser (LOGGATO) ha già richiesto più di 2 preventivo dalle ultime 24 ore
		final int NumeroTentativiMassimo = 2;
		if( 
			( ricercaTransfertDao.CheckRichiestaPreventivi_MAXinvii_IN24Ore(ricTransfert.getDataRicerca(), ricTransfert.getPhoneNumberCustomer(), 
					UtilString.RimuoviTuttiGliSpazi(request.getParameter("cliente-email")), (user!=null) ? user.getId():null, InfoUserConnectAddressMain.DammiIPaddress(request), NumeroTentativiMassimo)
			&& ( ricTransfert.getRichiestaPreventivi_Inviata() == null || ricTransfert.getRichiestaPreventivi_Inviata() == false) )
			|| 
			request.isUserInRole(Constants.ADMIN_ROLE)) {
			if(ricTransfert.getRicTransfert_Token() == null) {
				String tokenRandom = ricTransfert.getId().toString()+UtilBukowski.getRandomToken__LettPiccole_LettGrandi_Numeri(Constants.LUNGHEZZA_URL_TOKEN_GENERALE);
				infoDatiPasseggero.put(Constants.RicTransfert_Token, tokenRandom);
			}
			if(ricTransfert.getRicTransfert_IpAddress() == null) {
				infoDatiPasseggero.put(Constants.RicTransfert_IpAddress, InfoUserConnectAddressMain.DammiIPaddress(request));
			}
			// salvo tutto
			infoDatiPasseggero.put(Constants.RichiestaPreventivi_Inviata, true);
			ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
			ricercaTransfertDao.saveRicercaTransfert( ricTransfert );
			HomeUtil_Sms_Email.InviaSmsEmailPreventiviAutisti(ricTransfert, request, velocityEngine);
			HomeUtil_Sms_Email.Security_InviaEmailListaAutistiPreventivoCliente(ricTransfert, request, velocityEngine);
			InviaEmail.InviaEmailListaAutistiPreventivoCliente_AvvisoMatteo(ricTransfert, request, velocityEngine);
			
			log.debug("RICHIESTE PREVENTIVI INVIATI OK");
			preventiviInviati = true;
		}else {
			log.debug("RICHIESTE PREVENTIVI INVIATI ESAURITI");
			preventiviInviati = false;
		}
		return preventiviInviati;
	}
	
	
	public static RicercaTransfert ElaboraSconto(RicercaTransfert ricTransfert, String TokenCodiceSconto) {
		if( ricTransfert.getCodiceSconto() == null && TokenCodiceSconto != null && !TokenCodiceSconto.equals("") ) {
			if(agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(TokenCodiceSconto) != null 
					&& agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(TokenCodiceSconto).getCodiceScontoUsato() == false) {
				AgenzieViaggioBit agenziaViaggio = agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(TokenCodiceSconto);
				System.out.println("Email SCONTO MARKETING: "+agenziaViaggio.getEmail());
				HomeUtil.ApplicaSconto_RicercaTransfert(ricTransfert, TokenCodiceSconto, agenziaViaggio.getPercentualeSconto());
				
			}else if(userDao.getUser_by_TokenRecensioneCodiceSconto(TokenCodiceSconto) != null 
					&& userDao.getUser_by_TokenRecensioneCodiceSconto(TokenCodiceSconto).getRecensioneTransfer().getCodiceScontoUsato() == false) {
				User userRecensioneSconto = userDao.getUser_by_TokenRecensioneCodiceSconto( TokenCodiceSconto );
				System.out.println("Email SCONTO RECENSIONE: "+userRecensioneSconto.getEmail());
				HomeUtil.ApplicaSconto_RicercaTransfert(ricTransfert, TokenCodiceSconto, userRecensioneSconto.getRecensioneTransfer().getPercentualeSconto());
			}
		}
		return ricercaTransfertDao.get( ricTransfert.getId() ); // faccio il refresh del nuovo valore modificato (altrimenti non aggiorna i valori sulla pagina)
	}
	
	private static void ApplicaSconto_RicercaTransfert(RicercaTransfert ricTransfert, String TokenCodiceSconto, int percentualeSconto) {
		BigDecimal PrezzoVecchio = ricTransfert.getRichiestaMediaScelta().getPrezzoTotaleCliente();
		BigDecimal CifraDaScontare = PrezzoVecchio.divide( new BigDecimal(100) ).multiply( 
				new BigDecimal( percentualeSconto ));
		BigDecimal NuovoPrezzo = PrezzoVecchio.subtract(CifraDaScontare);
		System.out.println( "NuovoPrezzo: "+NuovoPrezzo );
		// modifico il prezzo con quello scontato
		RichiestaMedia richMedia = richiestaMediaDao.getRichiestaMedia_by_IdRicercaTransfert(ricTransfert.getId());
		richMedia.setPrezzoTotaleCliente(NuovoPrezzo.setScale(2, RoundingMode.HALF_EVEN));
		richiestaMediaDao.saveRichiestaMedia(richMedia);
		// aggiorno infoDatiPasseggero con il prezzo vecchio
		JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
		infoDatiPasseggero.put(Constants.CodiceScontoJSON, TokenCodiceSconto);
		infoDatiPasseggero.put(Constants.PercentualeScontoJSON, percentualeSconto);
		infoDatiPasseggero.put(Constants.VecchioPrezzoJSON, PrezzoVecchio);
		ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
		ricercaTransfertDao.saveRicercaTransfert( ricTransfert );
	}
	
	
	public static void SetTokenCodiceSconto_Usato(String TokenCodiceSconto) {
		AgenzieViaggioBit agenziaViaggio = agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(TokenCodiceSconto);
		if( agenziaViaggio != null ) {
			JSONObject parametriSconto = new JSONObject( agenziaViaggio.getParametriSconto() );
			parametriSconto.put(Constants.CodiceScontoUsatoJSON, true);
			agenziaViaggio.setParametriSconto( parametriSconto.toString() );
			agenzieViaggioBitDao.saveAgenzieViaggioBit(agenziaViaggio);
		}else{
			User userRecensioneSconto = userDao.getUser_by_TokenRecensioneCodiceSconto( TokenCodiceSconto );
			if( userRecensioneSconto != null ) {
				JSONObject parametriSconto = new JSONObject( userRecensioneSconto.getWebsite() );
				parametriSconto.put(Constants.CodiceScontoUsatoJSON, true);
				userRecensioneSconto.setWebsite( parametriSconto.toString() );
				userDao.saveUser(userRecensioneSconto);
			}
		}
	}

	public static String DammiMessaggiEsistoTransfert(MessaggioEsitoRicerca mess, Locale locale){
		
		if(mess.getPropertiesMess().equals( "messaggio.autisti.non.disponibili.provincia" )){
			List<String> provDisponibili = TerritorioUtil.ProvinceAutistiDisponibiliCorsa_NomeProvincia( mess.getTipoServizo() );
			return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), new Object[] { provDisponibili }, locale );
			
			
		}else if(mess.getPropertiesMess().equals( "messaggio.autisti.non.disponibili.provare.numero.passeggeri.diverso" )){
			return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), mess.getArgs(), locale );
		
		}else if(mess.getPropertiesMess().equals( "errors.ora.partenza.corsa" )){
			final int NUM_ORE = mess.getTipoServizo().equals(Constants.SERVIZIO_STANDARD) ? 
					(int)(long)gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber() 
						: mess.getTipoServizo().equals(Constants.SERVIZIO_PARTICOLARE) ? 
							(int)(long)gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber() : null;
			Date date = DateUtil.AggiungiOre_a_DataAdesso(NUM_ORE);
			return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), new Object[] { 
					DateUtil.FORMATO_ORA.format(date), 
						DateUtil.FORMATO_GIORNO_MESE_ESTESO(locale).format(date) }, locale );
			
		}else if(mess.getPropertiesMess().equals( "messaggio.transfert.localita.non.riconosciuta" )){
			return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), new Object[] { mess.getArgs()[0] }, locale );
			
		}else{
			if( mess.getArgs() != null && mess.getArgs().length > 0 ) {
				return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), mess.getArgs(), locale );
			}else {
				return ApplicationMessagesUtil.DammiMessageSource( mess.getPropertiesMess(), locale );
			}
		}
	}
	
	public static String DammiMonetaStripe(BigDecimal totBigDecimal) {
		String totaleEuro = totBigDecimal.toString();
		return totaleEuro.replace(".", ""); // questo perche stripe prende le valute in centesimi, per esempio 3.30 euro sono 330 centesimi
	}
	
	/**
	 * Questo è un metodo secondario per fare i pagamenti con Stripe con la  Costumer Authentication
	 * il pagamento viene fatto sul server dopo che è stato fatto il submit alla pagina di pagamento
	 *  (Vedi documentazione Stripe)
	 */
	public static Charge SalvaInfoStripe_StrongCostumerAuthentication(String stripeId_PaymentIntent, Object ObjRicTransfert) throws StripeException  {
    	long stripeSWITCH = gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		if(stripeSWITCH == 0l){
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // TEST
		}else{
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_LIVE; // LIVE
		}
		PaymentIntent intent = PaymentIntent.retrieve( stripeId_PaymentIntent );
		List<Charge> charges = intent.getCharges().getData(); String descrizione = "";  String nomeCliente = "";
		if(ObjRicTransfert instanceof RicercaTransfert) {
			RicercaTransfert ricercaTransfert = (RicercaTransfert) ObjRicTransfert;
			nomeCliente = ricercaTransfert.getUser().getFullName();
			descrizione = "Tipo Sevizio: "+ricercaTransfert.getTipoServizio()+" RicercaTransfert id: "+ String.valueOf(ricercaTransfert.getId());
		}
		if(ObjRicTransfert instanceof Ritardi) {
			Ritardi ritardo = (Ritardi) ObjRicTransfert;
			descrizione = "Ritardo RicercaTransfert id: " + String.valueOf(ritardo.getRicercaTransfert().getId());
			nomeCliente = ritardo.getRicercaTransfert().getUser().getFullName();
		}
		if(ObjRicTransfert instanceof Supplementi) {
			Supplementi supplemento = (Supplementi) ObjRicTransfert;
			descrizione = "Supplemento RicercaTransfert id: " + String.valueOf(supplemento.getRicercaTransfert().getId());
			nomeCliente = supplemento.getRicercaTransfert().getUser().getFullName();
		}
		Map<String, Object> paramsCharge = new HashMap<String, Object>();
		paramsCharge.put("description", "pagamento eseguito da: "+nomeCliente);
		charges.get(0).update(paramsCharge);
		Map<String, Object> paramsPaymentIntent = new HashMap<String, Object>();
		paramsPaymentIntent.put("description", descrizione);
		intent.update(paramsPaymentIntent);
		return charges.get(0);
    }
	
	
	public static void SalvaDatiCliente(RicercaTransfert ricTransfert, HttpServletRequest request){
		JSONObject infoDatiPasseggero = GetInfoDatiPasseggero(ricTransfert);
		infoDatiPasseggero.put(Constants.RicTransfert_Nome, request.getParameter("cliente-firstName"));
		infoDatiPasseggero.put(Constants.RicTransfert_Cognome, request.getParameter("cliente-lastName"));
		infoDatiPasseggero.put(Constants.RicTransfert_Email, UtilString.RimuoviTuttiGliSpazi(request.getParameter("cliente-email")));
		infoDatiPasseggero.put(Constants.RicTransfert_Address, request.getParameter("address"));
		if( ricTransfert.getPhoneNumberCustomer() == null && request.getParameter("cliente-telefono") != null && !request.getParameter("cliente-telefono").equals("") ) {
			ricTransfert.setPhoneNumberCustomer( request.getParameter("cliente-telefono") );
		}
		ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
		ricercaTransfertDao.saveRicercaTransfert( ricTransfert );
    }
	
	public static User CreaUtente(RicercaTransfert ricTransfert, Locale locale) throws Exception {
		log.debug("CreaUtente");
		User user = new User();
		String usernameGenerata = UtilBukowski.GeneraUsername(0);
		if(ricTransfert.getRicTransfert_Nome() != null && !ricTransfert.getRicTransfert_Nome().equals("")){
			user.setFirstName( UtilString.PrimaLetteraMaiuscola(ricTransfert.getRicTransfert_Nome()) );
		}else{
			user.setFirstName(null);
		}
		if(ricTransfert.getRicTransfert_Cognome() != null && !ricTransfert.getRicTransfert_Cognome().equals("")){
			user.setLastName( UtilString.PrimaLetteraMaiuscola(ricTransfert.getRicTransfert_Cognome()) );
		}else{
			user.setLastName(null);
		}
		if(ricTransfert.getRicTransfert_Address() != null && !ricTransfert.getRicTransfert_Address().equals("")){
			user.getBillingInformation().setAddress(ricTransfert.getRicTransfert_Address());
		}else{
			user.getBillingInformation().setAddress("");
		}
		while( userDao.userUsernameExist(usernameGenerata) == true ){
			usernameGenerata = UtilBukowski.GeneraUsername(0);
		}
		user.setUsername( usernameGenerata );
		user.setPhoneNumber( ricTransfert.getPhoneNumberCustomer() );
		if(ricTransfert.getRicTransfert_Email() != null && !ricTransfert.getRicTransfert_Email().equals("")){
			if( userDao.userEmailExist( ricTransfert.getRicTransfert_Email() )) {
				user.setEmail( usernameGenerata+Constants.FAKE_EMAIL );
			}else{
				user.setEmail( ricTransfert.getRicTransfert_Email() );
			}
		}else{
			user.setEmail( usernameGenerata+Constants.FAKE_EMAIL );
		}
		
		String password = ricTransfert.getTokenCustomer() != null && !ricTransfert.getTokenCustomer().contentEquals("") 
				? ricTransfert.getTokenCustomer() : UtilBukowski.getUserPasswordCustomer(5);
		user.setPassword( password ); // richAutPart.getTokenCustomer()
		user.addRole(roleDao.getRoleByName(Constants.USER_ROLE));
		user.addTipoRuoli( tipoRuoliDao.getTipoRuoliByName(Constants.CLIENTE) );
		user.setEnabled(true);
		user.setSignupDate(new Date());
		return user;
	}
	
	public static RicercaTransfert CaricaDatiDaForm(HttpServletRequest request, String idRicTransfertDuplic) throws Exception {
		log.debug("CaricaDatiDaForm");
		RicercaTransfert ricTransfert = new RicercaTransfert();
		if(idRicTransfertDuplic == null || idRicTransfertDuplic.equals("")) {
			if( request.getParameter("id-visitatore") != null && !request.getParameter("id-visitatore").equals("") ){
				Visitatori visitatore = visitatoriDao.get( Long.parseLong(request.getParameter("id-visitatore")) );
				ricTransfert.setVisitatore(visitatore);
			}
			
			if( request.getParameter("radioTipoServizio") != null ) {
				String radioTipoServizio = request.getParameter("radioTipoServizio") ;
				String TipoServizio = (radioTipoServizio.equals("ST") ? "ST" : radioTipoServizio.equals("PART") ? "PART" : null);
				ricTransfert.setTipoServizio(TipoServizio);
			}
			
			ricTransfert.setPartenzaRequest( request.getParameter("partenzaRequest") );
			ricTransfert.setArrivoRequest( request.getParameter("arrivoRequest") );
			ricTransfert.setPlace_id_Partenza( request.getParameter("place_id_Partenza") );
			ricTransfert.setPlace_id_Arrivo( request.getParameter("place_id_Arrivo") );
			ricTransfert.setDataOraPrelevamento( request.getParameter("dataOraPrelevamento") );
			ricTransfert.setOraPrelevamento( request.getParameter("oraPrelevamento") );
			boolean ritorno;
			if(request.getParameter("ritorno") != null){
				ritorno = true;
			}else{
				ritorno = false; 
			}
			ricTransfert.setRitorno( ritorno );
			ricTransfert.setDataOraRitorno( request.getParameter("dataOraRitorno") );
			ricTransfert.setOraRitorno( request.getParameter("oraRitorno") );
			if( request.getParameter("numeroPasseggeri") != null && !request.getParameter("numeroPasseggeri").equals("") ){
				try {
					ricTransfert.setNumeroPasseggeri( Integer.parseInt(request.getParameter("numeroPasseggeri")) );
				}catch(NumberFormatException nfe) {
					ricTransfert.setNumeroPasseggeri( 0 );
				}
			}else{
				ricTransfert.setNumeroPasseggeri( 0 );
			}
			ricTransfert.setNotePerAutista( UtilString.TagliaVarChar1000( request.getParameter("noteAutista") )  );
		}else{
			RicercaTransfert ricercaTransfertExist = ricercaTransfertDao.get( Long.parseLong(idRicTransfertDuplic) );
			ricTransfert.setVisitatore( ricercaTransfertExist.getVisitatore() );
			ricTransfert.setTipoServizio( ricercaTransfertExist.getTipoServizio() );
			ricTransfert.setPartenzaRequest( ricercaTransfertExist.getPartenzaRequest() );
			ricTransfert.setArrivoRequest( ricercaTransfertExist.getArrivoRequest() );
			ricTransfert.setPlace_id_Partenza( ricercaTransfertExist.getPlace_id_Partenza() );
			ricTransfert.setPlace_id_Arrivo( ricercaTransfertExist.getPlace_id_Arrivo() );
			ricTransfert.setNumeroPasseggeri( ricercaTransfertExist.getNumeroPasseggeri() );
			//ricTransfert.setNotePerAutista( ricercaTransfertExist.getNotePerAutista() );
			
			ricTransfert.setOraPrelevamento( ricercaTransfertExist.getOraPrelevamento() );
			Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(ricercaTransfertExist.getDataOraPrelevamento(),ricercaTransfertExist.getOraPrelevamento());
			if( ! ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdesso(dataPrelevamento) ){
				ricTransfert.setDataOraPrelevamento( ricercaTransfertExist.getDataOraPrelevamento() );
			}else{
				String newDate = String.valueOf( new Date().getTime() );
				ricTransfert.setDataOraPrelevamento( newDate );
			}
			ricTransfert.setRitorno( ricercaTransfertExist.isRitorno() );
			if(ricercaTransfertExist.isRitorno()){
				ricTransfert.setOraRitorno( ricercaTransfertExist.getOraRitorno() );
				Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca(ricercaTransfertExist.getDataOraRitorno(),ricercaTransfertExist.getOraRitorno());
				if( ! ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdesso(dataRitorno) ){
					ricTransfert.setDataOraRitorno( ricercaTransfertExist.getDataOraRitorno() );
				}else{
					String newDate = String.valueOf( new Date().getTime() )  ;
					ricTransfert.setDataOraRitorno( newDate );
				}
			}
		}
		return ricTransfert;
	}
	
	public static BindingResult Controlli_Ricerca(RicercaTransfert ricercaTransfertMod, BindingResult errors) throws Exception {
		// Controllo che ci siamo almeno 10 SMS SKEBBY rimanenti. altrimenti blocco tutto
		if(errors != null) {
			long invioSmsAbilitato = gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber();
			long smsSkebbyRimanenti = gestioneApplicazioneDao.getName("SMS_SKEBBY_RIMANENTI").getValueNumber();
			if(smsSkebbyRimanenti <= 10 && invioSmsAbilitato == 1){
				errors.rejectValue(null, "errors.10SmsSkebbyRimanenti"); // home messo 10 per poter usarli nell'ultima operazione di transfert
				return errors;
			}
			// control Partenza
			if(ricercaTransfertMod.getPartenzaRequest().equals("")) {
				errors.rejectValue("partenzaRequest", "errors.inserirePartenzaRequest");
				return errors;
			}
			// control Arrivo
			if(ricercaTransfertMod.getArrivoRequest().equals("")) {
				errors.rejectValue("arrivoRequest", "errors.inserireArrivoRequest");
				return errors;
			}
			// control Passeggeri
			if(ricercaTransfertMod.getNumeroPasseggeri() < 1) {
				errors.rejectValue("numeroPasseggeri", "errors.numeroPasseggeri");
				return errors;
			}
			// control Data Partenza
			if(ricercaTransfertMod.getDataOraPrelevamento() == null || ricercaTransfertMod.getDataOraPrelevamento().equals("")) {
				errors.rejectValue("dataOraPrelevamento", "errors.inserireDataOraPrelevamento");
				return errors;
			}
			// control Ora Partenza
			if(ricercaTransfertMod.getOraPrelevamento() == null || ricercaTransfertMod.getOraPrelevamento().equals("")) {
				errors.rejectValue("oraPrelevamento", "errors.inserireOraPrelevamento");
				return errors;
			}
			
			Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(ricercaTransfertMod.getDataOraPrelevamento(),ricercaTransfertMod.getOraPrelevamento());
			if( ! ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(dataPrelevamento, Constants.SERVIZIO_PARTICOLARE) ){
				long numOre = gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber();
				errors.rejectValue(null, "errors.dataPartenzaDeveEssereUnOraSuccessivaDaAdesso", new Object[] { numOre }, "");
			}
			try{
				if( ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdesso(dataPrelevamento) ){
					errors.rejectValue("dataOraPrelevamento", "errors.dataPartenzaPrecedenteOggi");
					return errors;
				}else if( ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdessoTime(dataPrelevamento) ){
					errors.rejectValue("oraPrelevamento", "errors.oraPartenzaPrecedenteAdesso");
					return errors;
				}
			}catch (final ParseException e) {
				errors.rejectValue("dataOraPrelevamento", "errors.dataPartenzaNonValida");
				return errors;
	        }
			// control Data Ritorno
			if(ricercaTransfertMod.isRitorno()){
				if(ricercaTransfertMod.getDataOraRitorno().equals("")){
					errors.rejectValue("dataOraRitorno", "errors.inserireDataOraRitorno");
					return errors;
				}
				if(ricercaTransfertMod.getOraRitorno().equals("")){
					errors.rejectValue("oraRitorno", "errors.inserireOraRitorno");
					return errors;
				}
				Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca(ricercaTransfertMod.getDataOraRitorno(),ricercaTransfertMod.getOraRitorno());
				if( ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdesso(dataRitorno) ){
					errors.rejectValue("dataOraRitorno", "errors.dataRitornoPrecedenteOggi");
					return errors;
				}else if( ControlloDateRicerca.ControlloDataPrelevamentoSuperioreInferioreDaAdessoTime(dataRitorno) ){
					errors.rejectValue("oraRitorno", "errors.oraRitornoPrecedenteAdesso");
					return errors;
				}
				//Long.parseLong(ricercaTransfertMod.getDataOraPrelevamento())
				if( Long.parseLong(ricercaTransfertMod.getDataOraPrelevamento()) == Long.parseLong(ricercaTransfertMod.getDataOraRitorno()) ){
					if(dataPrelevamento.getTime() == dataRitorno.getTime()){
						errors.rejectValue("oraRitorno", "errors.dataPartenzaUgualeRitorno");
						//errors.rejectValue("dataOraRitorno", "errors.null");
						return errors;
					}else if( dataPrelevamento.getTime() > dataRitorno.getTime() ){
						errors.rejectValue("oraRitorno", "errors.dataPartenzaSuperioreRitorno");
					}
				}
				if( Long.parseLong(ricercaTransfertMod.getDataOraPrelevamento()) > Long.parseLong(ricercaTransfertMod.getDataOraRitorno()) ){
					errors.rejectValue("dataOraPrelevamento", "errors.dataPartenzaSuperioreRitorno");
					return errors;
				}
			}
			return errors;
		}else{
			return null;
		}
	}
	
	public static ModelAndView Controlli_Transfert(ModelAndView mav, RicercaTransfert ricTransfert, BindingResult errors, Locale locale) 
			throws GoogleMatrixException, UnknownHostException, Exception {
		long startTime = System.nanoTime(); 
		if(errors == null || errors.getErrorCount() == 0 ) {
			RicercaTransfert_GoogleMaps_Info PARTENZA = null; RicercaTransfert_GoogleMaps_Info ARRIVO = null; 
			GMaps_Api GMaps_Api = new GMaps_Api();
			PARTENZA = GMaps_Api.GoogleMaps_PlaceTextSearch(ricTransfert.getPartenzaRequest(), locale.getLanguage());
			if(PARTENZA == null) {
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(ricTransfert.getPartenzaRequest(), locale.getLanguage());
				if(predictions1 != null){
					PARTENZA = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, locale.getLanguage());
				}
			}
			PARTENZA = CercaComuneGoogleMaps(PARTENZA, locale);
			if(PARTENZA != null){
				ricTransfert.setPlace_id_Partenza( PARTENZA.getPlace_id() );
				ricTransfert.setFormattedAddress_Partenza( BuildFormattedAddress(PARTENZA, ricTransfert.getPartenzaRequest()) );
				ricTransfert.setName_Partenza(PARTENZA.getName());
				ricTransfert.setLat_Partenza(PARTENZA.getLat());
				ricTransfert.setLng_Partenza(PARTENZA.getLng());
				ricTransfert.setComune_Partenza( PARTENZA.getComune() );
				ricTransfert.setSiglaProvicia_Partenza( PARTENZA.getSiglaProvicia() );
				ricTransfert.setListTypes_Partenza(PARTENZA.getListTypes());
			}

			ARRIVO = GMaps_Api.GoogleMaps_PlaceTextSearch(ricTransfert.getArrivoRequest(), locale.getLanguage());
			if(ARRIVO == null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(ricTransfert.getArrivoRequest(), locale.getLanguage());
				if(predictions1 != null){
					ARRIVO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, locale.getLanguage());
				}
			}
			ARRIVO = CercaComuneGoogleMaps(ARRIVO, locale);
			if(ARRIVO != null){
				ricTransfert.setPlace_id_Arrivo( ARRIVO.getPlace_id() );
				ricTransfert.setFormattedAddress_Arrivo( BuildFormattedAddress(ARRIVO, ricTransfert.getArrivoRequest()) );
				ricTransfert.setName_Arrivo(ARRIVO.getName());
				ricTransfert.setLat_Arrivo(ARRIVO.getLat());
				ricTransfert.setLng_Arrivo(ARRIVO.getLng());	
				ricTransfert.setComune_Arrivo( ARRIVO.getComune() );
				ricTransfert.setSiglaProvicia_Arrivo( ARRIVO.getSiglaProvicia() );
				ricTransfert.setListTypes_Arrivo(ARRIVO.getListTypes());
			}
			DammiTempoOperazione.DammiSecondi(startTime, "Controlli_Transfert-1");
			
			// control Comune
			Comuni comunePartenza = null; Comuni comuneArrivo = null;
			if(ricTransfert.getComune_Partenza() == null) {
				errors.rejectValue(null, "messaggio.transfert.localita.partenza.non.riconosciuta", new Object[] { ricTransfert.getPartenzaRequest() }, "");
				mav.addAllObjects(errors.getModel());
			}else {
				comunePartenza = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Partenza(), ricTransfert.getSiglaProvicia_Partenza() );
			}
			if(ricTransfert.getComune_Arrivo() == null) {
				errors.rejectValue(null, "messaggio.transfert.localita.arrivo.non.riconosciuta", new Object[] { ricTransfert.getArrivoRequest() }, "");
				mav.addAllObjects(errors.getModel());
			}else {
				comuneArrivo = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Arrivo(), ricTransfert.getSiglaProvicia_Arrivo() );
			}
			if( comunePartenza != null && comuneArrivo != null ) {
				// control Traghetto
				if((!comunePartenza.isIsola() && comuneArrivo.isIsola()) || (comunePartenza.isIsola() && !comuneArrivo.isIsola())){
					errors.rejectValue(null, "messaggio.transfert.traghetto");
					mav.addAllObjects(errors.getModel());
					
				}else if(comunePartenza.isIsola()  && comuneArrivo.isIsola()){
					errors.rejectValue(null, "messaggio.transfert.traghetto");
					mav.addAllObjects(errors.getModel());
					
				}else if((!comunePartenza.getProvince().isIsola() && comuneArrivo.getProvince().isIsola()) || 
						(comunePartenza.getProvince().isIsola() && !comuneArrivo.getProvince().isIsola())){
					errors.rejectValue(null, "messaggio.transfert.traghetto");
					mav.addAllObjects(errors.getModel());
				}
			}

			DammiTempoOperazione.DammiSecondi(startTime, "Controlli_Transfert-2");
			
			if(ricTransfert.getPartenzaRequest() == null){
				errors.rejectValue("partenzaRequest", "errors.partenzaDa");
				mav.addAllObjects(errors.getModel());
			}
			if(ricTransfert.getArrivoRequest() == null){
				errors.rejectValue("arrivoRequest", "errors.arrivoA");
				mav.addAllObjects(errors.getModel());
			}
			if(ricTransfert.getPlace_id_Partenza() == null || ricTransfert.getPlace_id_Partenza().equals("")){
				errors.rejectValue("partenzaRequest", "errors.partenzaDa");
				mav.addAllObjects(errors.getModel());
				
			}else if(ricTransfert.getPlace_id_Arrivo() == null || ricTransfert.getPlace_id_Arrivo().equals("")){
				errors.rejectValue("arrivoRequest", "errors.arrivoA");
				mav.addAllObjects(errors.getModel());
				
			}else if(ricTransfert.getPlace_id_Partenza().equals(ricTransfert.getPlace_id_Arrivo())){
				errors.rejectValue("partenzaRequest", "errors.partenzaArrivo.uguali");
				errors.rejectValue("arrivoRequest", "errors.null");
				mav.addAllObjects(errors.getModel());
			}else{
				Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(ricTransfert.getDataOraPrelevamento(), ricTransfert.getOraPrelevamento()); 
				ricTransfert.setDataOraPrelevamentoDate( dataPrelevamento );
				
				if(ricTransfert.isRitorno() && ricTransfert.getDataOraRitorno() != null && !ricTransfert.getDataOraRitorno().equals("")){
					Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraRitorno(),ricTransfert.getOraRitorno() );
					ricTransfert.setDataOraRitornoDate(dataRitorno);
				}
				ricTransfert = GMaps_Api.GoogleMaps_DistanceMatrix( ricTransfert, locale.getLanguage(), false );
				
				if( ricTransfert.isRitorno() && 
						ricTransfert.getDataOraRitorno() != null && ricTransfert.getOraRitorno() != null &&
							!ricTransfert.getDataOraRitorno().equals("") && !ricTransfert.getOraRitorno().equals("")){
					ricTransfert = GMaps_Api.GoogleMaps_DistanceMatrix( ricTransfert, locale.getLanguage(), true );
				}
			}
			/**
			 *  controllo data ritorno superiore alla data di arrivo prevista
			 */
			if(ricTransfert.isRitorno() && ricTransfert.getDataOraRitorno() != null && !ricTransfert.getDataOraRitorno().equals("")){
				Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraPrelevamento(),ricTransfert.getOraPrelevamento() );
				long SECONDI = ricTransfert.getDurataConTrafficoValue();
				long datasommata =  dataPrelevamento.getTime() + TimeUnit.SECONDS.toMillis( SECONDI );
				Date dataArrivoPrevisto = new Date( datasommata );
				Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraRitorno(),ricTransfert.getOraRitorno() );
				// controllo che la data di ritorno sia oltre la data prevista di arrivo
				if(dataRitorno.getTime() <= dataArrivoPrevisto.getTime() ){
					String dataEstesa = DateUtil.FORMATO_GIORNO_MESE_ANNO_ESTESO(locale).format(dataArrivoPrevisto);
					String ora = DateUtil.FORMATO_ORA.format(dataArrivoPrevisto);
					errors.rejectValue("dataOraRitorno", "errors.dataRitornoSuperioreAllaDataArrivoPrevista", new Object[] { 
							ora, dataEstesa  }, "");
					errors.rejectValue("oraRitorno", "errors.null");
					mav.addAllObjects(errors.getModel());
				}
			}
		}
		
		DammiTempoOperazione.DammiSecondi(startTime, "Controlli_Transfert-3");
		
		return mav;
	}
	
	/**
	 * Costruisce il dormattedAddress_Partenza|Arrivo nel meglio modo possibile
	 * @param Rt_Gm_I
	 * @param ricTransfert_Request
	 * @return
	 */
	private static String BuildFormattedAddress(RicercaTransfert_GoogleMaps_Info Rt_Gm_I, String ricTransfert_Request) {
		String formattedAddess = (Rt_Gm_I.getFormattedAddress() != null && !Rt_Gm_I.getFormattedAddress().equals("")) 
				? Rt_Gm_I.getFormattedAddress() : ricTransfert_Request;
		if( Rt_Gm_I.getName() != null && !Rt_Gm_I.equals("") ) {
			if( !formattedAddess.contains(Rt_Gm_I.getName()) ) {
				return formattedAddess +" ("+Rt_Gm_I.getName()+")";
			}
		}
		return formattedAddess;
	}
	
	private static RicercaTransfert_GoogleMaps_Info SalvaNuovoComune(RicercaTransfert_GoogleMaps_Info googleMaps_info) {
		try {
			Comuni nuovoComune = new Comuni(); 
			if( googleMaps_info.getSiglaNazione().equals(Constants.ITALIA) ) {
				Province provincia = provinceDao.getProvinciaBy_SiglaProvincia(googleMaps_info.getSiglaProvicia());
				nuovoComune.setProvince(provincia);
				nuovoComune.setRegioni(provincia.getRegioni());
				nuovoComune.setNazione(provincia.getRegioni().getNazione());
				nuovoComune.setIsola(false);
				nuovoComune.setNomeComune(googleMaps_info.getComune());
				nuovoComune = comuniDao.saveComuni(nuovoComune);
				return googleMaps_info;
			} else {
				Nazioni nazioneStraniera = nazioniDao.getNazioneBy_SiglaNazione(googleMaps_info.getSiglaNazione());
				if( nazioneStraniera == null ) {
					nazioneStraniera = new Nazioni(); 
					nazioneStraniera.setIsola(false); 
					nazioneStraniera.setSiglaNazione(googleMaps_info.getSiglaNazione()); 
					Locale loc = new Locale(Constants.ITALIA, googleMaps_info.getSiglaNazione());
					System.out.println(loc.getDisplayCountry());
					nazioneStraniera.setUrl(CreaFriendlyUrl_Slugify.creaUrl(loc.getDisplayCountry()));
					nazioneStraniera = nazioniDao.saveNazioni(nazioneStraniera);
				}
				String NomeRegione = googleMaps_info.getNomeRegione() != null ? googleMaps_info.getNomeRegione() : googleMaps_info.getNomeNazione();
				String SiglaRegione = googleMaps_info.getSiglaRegione() != null ? googleMaps_info.getSiglaRegione() : googleMaps_info.getSiglaNazione();
				Regioni regioneStraniera = regioniDao.getRegioneBy_NomeRegione_e_SiglaNazione(NomeRegione, googleMaps_info.getSiglaNazione());
				if(regioneStraniera == null) {
					regioneStraniera = new Regioni(); 
					regioneStraniera.setCapoluogoProvincia(null);
					regioneStraniera.setIsola(false); 
					regioneStraniera.setMacroRegioni(null); 
					regioneStraniera.setNazione(nazioneStraniera); 
					regioneStraniera.setNomeRegione(NomeRegione);
					regioneStraniera.setUrl(CreaFriendlyUrl_Slugify.creaUrl(NomeRegione));
					regioniDao.saveRegioni(regioneStraniera);
				}
				
				String NomeProvincia = googleMaps_info.getNomeProvicia() != null ? googleMaps_info.getNomeProvicia() : NomeRegione;
				String SiglaProvincia = googleMaps_info.getSiglaProvicia() != null ? googleMaps_info.getSiglaProvicia() : SiglaRegione;
				Province provinciaStraniera = provinceDao.getProvinciaBy_SiglaProvincia_e_idRegione(SiglaProvincia+"_"+SiglaRegione, regioneStraniera.getId());
				if( provinciaStraniera == null ) {
					provinciaStraniera = new Province(); 
					provinciaStraniera.setIsola(false); 
					provinciaStraniera.setLat(googleMaps_info.getLat());
					provinciaStraniera.setLng(googleMaps_info.getLng());
					provinciaStraniera.setNomeProvincia(NomeProvincia);
					provinciaStraniera.setNumeroAbitanti(0);
					provinciaStraniera.setPercentualeServizio((int)(long)provinceDao.getPercentualeServizioMediaProvincia());
					provinciaStraniera.setRegioni(regioneStraniera);
					provinciaStraniera.setSiglaProvincia(SiglaProvincia);
					BigDecimal tariffaBaseMedia = new BigDecimal(provinceDao.getTariffaBaseMediaProvincia());
					provinciaStraniera.setTariffaBase( tariffaBaseMedia );
					provinciaStraniera.setUrl(CreaFriendlyUrl_Slugify.creaUrl(NomeProvincia));
					if(provinciaStraniera.getRegioni().getNazione().getSiglaNazione().equals(Constants.SAN_MARINO)) {
						provinciaStraniera.addProvinceStraniere_provinceItaliane( provinceDao.getProvinciaBy_SiglaProvincia(Constants.PESARO_URBINO) );
						provinciaStraniera.addProvinceStraniere_provinceItaliane( provinceDao.getProvinciaBy_SiglaProvincia(Constants.RIMINI) );
					}
					if(provinciaStraniera.getRegioni().getNazione().getSiglaNazione().equals(Constants.VATICANO)) {
						provinciaStraniera.addProvinceStraniere_provinceItaliane( provinceDao.getProvinciaBy_SiglaProvincia(Constants.ROMA) );
					}
					provinciaStraniera = provinceDao.saveProvince(provinciaStraniera);
				}
				String NomeComune = googleMaps_info.getComune() != null ? googleMaps_info.getComune() : NomeProvincia;
				nuovoComune.setProvince(provinciaStraniera);
				nuovoComune.setRegioni(regioneStraniera);
				nuovoComune.setNazione(nazioneStraniera);
				nuovoComune.setIsola(false);
				nuovoComune.setNomeComune(NomeComune);
				nuovoComune = comuniDao.saveComuni(nuovoComune);
				googleMaps_info.setComune(NomeComune); googleMaps_info.setSiglaProvicia(SiglaProvincia);
				return googleMaps_info;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static RicercaTransfert_GoogleMaps_Info CercaComuneGoogleMaps(RicercaTransfert_GoogleMaps_Info googleMaps_INFO, Locale locale) {
		try {
			if( googleMaps_INFO.getComune() != null && googleMaps_INFO.getSiglaProvicia() != null && 
					comuniDao.getComuniByNomeComune_Equal(googleMaps_INFO.getComune(), googleMaps_INFO.getSiglaProvicia()) != null) {
				//return comuniDao.getComuniByNomeComune_Equal(googleMaps_INFO.getComune(), googleMaps_INFO.getSiglaProvicia()).getNomeComune();
				return googleMaps_INFO;
			
			}else {
				GMaps_Api GMaps_Api = new GMaps_Api();
				RicercaTransfert_GoogleMaps_Info aaa = GMaps_Api.GoogleMaps_Geocode_LatLng(googleMaps_INFO);
				if( aaa.getComune() != null && aaa.getSiglaProvicia() != null && 
						comuniDao.getComuniByNomeComune_Equal(aaa.getComune(), aaa.getSiglaProvicia()) != null) {
					//return comuniDao.getComuniByNomeComune_Equal(googleMaps_INFO.getComune(), googleMaps_INFO.getSiglaProvicia()).getNomeComune();
					return aaa;
					
				}else if(aaa.getComune() != null && aaa.getSiglaProvicia() != null ) {
					return SalvaNuovoComune(aaa);
					
				}else {
					RicercaTransfert_GoogleMaps_Info bbb = GMaps_Api.GoogleMaps_PlaceDetails(googleMaps_INFO, locale.getLanguage());
					if( bbb.getComune() != null && bbb.getSiglaProvicia() != null && 
							comuniDao.getComuniByNomeComune_Equal(bbb.getComune(), bbb.getSiglaProvicia()) != null) {
						//return comuniDao.getComuniByNomeComune_Equal(googleMaps_INFO.getComune(), googleMaps_INFO.getSiglaProvicia()).getNomeComune();
						return bbb;
					
					}else {
						String NomeComune = null;
						if( googleMaps_INFO.getComune() != null ) {
							NomeComune = googleMaps_INFO.getComune();
						}else if(googleMaps_INFO.getNomeProvicia() != null) {
							NomeComune = googleMaps_INFO.getNomeProvicia();
						}else if(googleMaps_INFO.getNomeRegione() != null) {
							NomeComune = googleMaps_INFO.getNomeRegione();
						}else if(googleMaps_INFO.getNomeNazione() != null) {
							NomeComune = googleMaps_INFO.getNomeNazione();
						}
						String SiglaProvincia = null;
						if(googleMaps_INFO.getSiglaProvicia() != null) {
							SiglaProvincia = googleMaps_INFO.getSiglaProvicia();
						}else if(googleMaps_INFO.getSiglaRegione() != null) {
							SiglaProvincia = googleMaps_INFO.getSiglaRegione();
						}else if(googleMaps_INFO.getSiglaNazione() != null) {
							SiglaProvincia = googleMaps_INFO.getSiglaNazione();
						}
						if( NomeComune != null && SiglaProvincia != null && comuniDao.getComuniByNomeComune_Equal(NomeComune, SiglaProvincia) != null) {
							googleMaps_INFO.setComune(NomeComune); googleMaps_INFO.setSiglaProvicia(SiglaProvincia);
							return googleMaps_INFO;
						}else {
							return SalvaNuovoComune(googleMaps_INFO);
						}
					}
				}
			}
		}catch(Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}
	
	
	
	
	/**
	 * CARICAMENTO DATI RICHIESTE BEAN --> DATABASE
	 */
	public static RicercaTransfert Calcolo_e_Salvataggio_Ricerca(RicercaTransfert ricTransfert) throws Exception {
		long startTime = System.nanoTime(); 
		CalcoloTariffe calc = new CalcoloTariffe();
		ResultRicerca_Autista_Tariffe RIS = calc.CalcoloTariffe_Main(ricTransfert);
		ricTransfert.setDataRicerca( new Date() );
		ricTransfert.setScontoRitorno( RIS.isScontoRitorno() );
		ricTransfert.setTipoServizio( RIS.getTipoServizio() );
		ricTransfert.setCollapsePanelCorseAdmin(false); // per far vedere il pannello di GestioneCorse tirato giu o su
		if(ricTransfert.isRitorno() && ricTransfert.getDataOraRitorno() != null && !ricTransfert.getDataOraRitorno().equals("")){
			ricTransfert.setApprovazioneAndata(Constants.IN_APPROVAZIONE);
			ricTransfert.setApprovazioneRitorno(Constants.IN_APPROVAZIONE);
		}else{
			ricTransfert.setApprovazioneAndata(Constants.IN_APPROVAZIONE);
			ricTransfert.setApprovazioneRitorno(0);
		}
		JSONObject infoDatiPasseggero = GetInfoDatiPasseggero(ricTransfert);
		JSONArray jsArray = new JSONArray();
		for(Long ite: RIS.getProvinceTragitto_Id()) {
			jsArray.put( ite );
		}
		infoDatiPasseggero.put(Constants.ProvinceTragitto_Id, jsArray);
		ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
		ricTransfert.setFormattedAddress_Partenza(UtilString.RimuoviCaratteriNonUTF8( ricTransfert.getFormattedAddress_Partenza()));
		ricTransfert.setFormattedAddress_Arrivo(UtilString.RimuoviCaratteriNonUTF8(ricTransfert.getFormattedAddress_Arrivo()));
		RicercaTransfert ricTransfertNew = ricercaTransfertDao.saveRicercaTransfert(ricTransfert);
		DammiTempoOperazione.DammiSecondi(startTime, "Calcolo_e_Salvataggio_Ricerca-1");
		
		if( ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
			OperazioniThread_C runn = new OperazioniThread_C(ricTransfertNew, RIS);
			new Thread(runn).start();
			ricTransfertNew = ricercaTransfertDao.get(ricTransfertNew.getId());
			ricTransfertNew.setRicercaRiuscita( true );
			ricTransfertNew.setMessaggiEsitoRicerca( RIS.getMessaggiEsitoRicerca() );
			ricTransfertNew.setResultAutistaTariffePrezzo(RIS);
			
			DammiTempoOperazione.DammiSecondi(startTime, "Calcolo_e_Salvataggio_Ricerca-2");
			return ricTransfertNew;

		}else if(ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
			OperazioniThread_C runn = new OperazioniThread_C(ricTransfertNew, RIS);
			new Thread(runn).start();
			ricTransfertNew = ricercaTransfertDao.get(ricTransfertNew.getId());
			ricTransfertNew.setRicercaRiuscita( RIS.isRicercaRiuscita() );
			ricTransfertNew.setMessaggiEsitoRicerca( RIS.getMessaggiEsitoRicerca() );
			ricTransfertNew.setResultAutistaTariffePrezzo(RIS);
			
			DammiTempoOperazione.DammiSecondi(startTime, "Calcolo_e_Salvataggio_Ricerca-3");
			return ricTransfertNew;
		}
		
		return null;
	}
	
	/**
	 * CARICAMENTO DATI PARTICOLARE DATABASE --> BEAN
	 */
	public static RisultatoAutistiParticolare getRisultatoAutistiParticolare(long idRiceraTransfert) {
		try {
			List<RichiestaAutistaParticolare> richiestaAutistaParticolare = richiestaAutistaParticolareDao.getRichiestaAutistaParticolare_by_idRicercaTransfert(idRiceraTransfert);
			RisultatoAutistiParticolare resultAutistiParticolare = new RisultatoAutistiParticolare();
			List<RisultatoAutistiParticolare.ResultParticolare> resultParticolareList = new ArrayList<RisultatoAutistiParticolare.ResultParticolare>();
			for(RichiestaAutistaParticolare richiestaPart_ite: richiestaAutistaParticolare) {
				RisultatoAutistiParticolare.ResultParticolare resultParticolare = new RisultatoAutistiParticolare.ResultParticolare();
				resultParticolare.setClasseAutoveicoloScelta(richiestaPart_ite.getClasseAutoveicoloScelta());
				resultParticolare.setAutoveicolo(richiestaPart_ite.getAutoveicolo());
				resultParticolare.setRimborsoCliente(richiestaPart_ite.getRimborsoCliente());
				resultParticolare.setPercentualeServizio(richiestaPart_ite.getPercentualeServizio());
				resultParticolare.setPrezzoCommissioneServizio(richiestaPart_ite.getPrezzoCommissioneServizio());
				resultParticolare.setPrezzoCommissioneServizioIva(richiestaPart_ite.getPrezzoCommissioneServizioIva());
				resultParticolare.setPrezzoTotaleAutista(richiestaPart_ite.getPrezzoTotaleAutista());
				resultParticolare.setPrezzoTotaleCliente(richiestaPart_ite.getPrezzoTotaleCliente());
				resultParticolare.setInvioSmsCorsaConfermata(richiestaPart_ite.isInvioSmsCorsaConfermata());
				resultParticolare.setDataChiamataPrenotata(richiestaPart_ite.getDataChiamataPrenotata());
				resultParticolare.setInvioSms(richiestaPart_ite.isInvioSms());
				resultParticolare.setToken(richiestaPart_ite.getToken());
				resultParticolareList.add(resultParticolare);
			}
			List<Autista> autistiEffettiviList = new ArrayList<Autista>();
			List<Long> idAutistiList = new LinkedList<Long>();
			for(RisultatoAutistiParticolare.ResultParticolare ite: resultParticolareList) {
				if( !idAutistiList.contains( ite.getAutoveicolo().getAutista().getId() ) ) {
					autistiEffettiviList.add( ite.getAutoveicolo().getAutista() );
					idAutistiList.add( ite.getAutoveicolo().getAutista().getId() );
				}
			}
			resultAutistiParticolare.setResultParticolare(resultParticolareList);
			resultAutistiParticolare.setAutistiEffettivi( autistiEffettiviList );
			return resultAutistiParticolare;
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}
	
	/**
	 * CARICAMENTO DATI RICHIESTA_MEDIA DATABASE --> BEAN
	 */
	public static ResultRicerca_Autista_Tariffe getResultAutistaTariffePrezzo(long idRiceraTransfert) {
		try {
			List<RichiestaMedia> richiestaMedia = richiestaMediaDao.getListRichiestaMedia_by_IdRicercaTransfert(idRiceraTransfert);
			ResultRicerca_Autista_Tariffe resultRicerca_Autista_Tariffe = new ResultRicerca_Autista_Tariffe();
			if( richiestaMedia != null && richiestaMedia.size() > 0 ) {
				final int NumMinimoAutistiCorsaMedia = ApplicationUtils.DammiNumMinimoAutistiCorsaMedia();
				boolean showClasseAutoveicolo = false;
				int totaleShowClasseAutoveicolo = 0;
				List<ResultRicerca_Autista_Tariffe.ResultMedio> AAA_LIST = new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio>();
				for(RichiestaMedia richiestaMedia_ite: richiestaMedia) {
					ResultRicerca_Autista_Tariffe.ResultMedio AAA = new ResultRicerca_Autista_Tariffe.ResultMedio();
					AAA.setClasseAutoveicoloScelta(richiestaMedia_ite.isClasseAutoveicoloScelta());
					AAA.setClasseAutoveicolo(richiestaMedia_ite.getClasseAutoveicolo());
					AAA.setPrezzoTotaleCliente(richiestaMedia_ite.getPrezzoTotaleCliente());
					AAA.setTariffaPerKm( richiestaMedia_ite.getTariffaPerKm() );
					AAA.setMaggiorazioneNotturna( richiestaMedia_ite.getMaggiorazioneNotturna() );
					AAA.setPrezzoTotaleAutista(richiestaMedia_ite.getPrezzoTotaleAutista());
					AAA.setPrezzoCommissioneServizio(richiestaMedia_ite.getPrezzoCommissioneServizio());
					AAA.setPrezzoCommissioneServizioIva(richiestaMedia_ite.getPrezzoCommissioneServizioIva());
					AAA.setPrezzoCommissioneVenditore(richiestaMedia_ite.getPrezzoCommissioneVenditore());
					AAA.setRimborsoCliente(richiestaMedia_ite.getRimborsoCliente());
					List<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista> BBB_LIST = new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista>();  
					List<Long> autoList_id = new ArrayList<Long>();
					for(RichiestaMediaAutista richiestaMediaAutista_ite: richiestaMedia_ite.getRichiestaMediaAutista()) {
						ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista BBB = new ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista();
						BBB.setAutista(richiestaMediaAutista_ite.getAutista());
						BBB.setPrezzoTotaleAutista(richiestaMediaAutista_ite.getPrezzoTotaleAutista());
						BBB.setTariffaPerKm(richiestaMediaAutista_ite.getTariffaPerKm());
						BBB.setPrezzoCommissioneServizio(richiestaMediaAutista_ite.getPrezzoCommissioneServizio());
						BBB.setPrezzoCommissioneServizioIva(richiestaMediaAutista_ite.getPrezzoCommissioneServizioIva());
						BBB.setPrezzoCommissioneVenditore(richiestaMediaAutista_ite.getPrezzoCommissioneVenditore());
						if(!autoList_id.contains(richiestaMediaAutista_ite.getAutista().getId())) {
							autoList_id.add( richiestaMediaAutista_ite.getAutista().getId() );
						}
						List<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo> CCC_LIST = 
								new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo>();
						for(RichiestaMediaAutistaAutoveicolo listAutoveicolo_ite: richiestaMediaAutista_ite.getRichiestaMediaAutistaAutoveicolo()) {
							ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo CCC = 
									new ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo();
							CCC.setAutoveicolo( listAutoveicolo_ite.getAutoveicolo() );
							CCC_LIST.add(CCC);
						}
						BBB.setRisultAutistaMedioAutoveicolo(CCC_LIST);
						BBB_LIST.add( BBB );
					}
					if(autoList_id.size() >= NumMinimoAutistiCorsaMedia) {
						AAA.setShowClasseAutoveicolo( true );
						showClasseAutoveicolo = true;
						totaleShowClasseAutoveicolo ++;
					}else{
						AAA.setShowClasseAutoveicolo( false );
					}
					AAA.setResultMedioAutista(BBB_LIST);
					AAA_LIST.add(AAA);
				}
				resultRicerca_Autista_Tariffe.setResultMedio( AAA_LIST );
				if(showClasseAutoveicolo){
					resultRicerca_Autista_Tariffe.setRicercaRiuscita(true);
				}else{
					resultRicerca_Autista_Tariffe.setRicercaRiuscita(false);
				}
				resultRicerca_Autista_Tariffe.setTotaleShowClasseAutoveicolo(totaleShowClasseAutoveicolo);
				return resultRicerca_Autista_Tariffe;
			}
			resultRicerca_Autista_Tariffe.setRicercaRiuscita(false);
			return resultRicerca_Autista_Tariffe;
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}
	

	public static int ContaNumeroChiamateSessione(HttpSession session) {
		// CONTROLLO NUMERO CHIAMATE SESSIONE
		session.getId();
		log.debug("sessin: "+session.getId());
		int numeroChimateSessione = 0;
		if(session.getAttribute( "numeroChimate" ) != null) {
			numeroChimateSessione = Integer.parseInt( session.getAttribute( "numeroChimate" ).toString() );
			numeroChimateSessione ++;
			session.setAttribute("numeroChimate", numeroChimateSessione);
		}else{
			numeroChimateSessione = 1;
			session.setAttribute("numeroChimate", numeroChimateSessione);
		}
		log.debug("numero chiamate sessione: "+ numeroChimateSessione );
		return numeroChimateSessione;
	}
	
	public static JSONObject GetInfoDatiPasseggero(RicercaTransfert ricTransf) {
		return ricTransf.getInfoPasseggero() != null && !ricTransf.getInfoPasseggero().contentEquals("") ? new JSONObject(ricTransf.getInfoPasseggero()) : new JSONObject() ;
	}
	
	public static JSONObject GetInfoCorsa(RichiestaAutistaParticolare richAutistPart) {
		return (richAutistPart.getInfoCorsa() != null && !richAutistPart.getInfoCorsa().contentEquals("") ? new JSONObject(richAutistPart.getInfoCorsa()) : new JSONObject() );
	}
	
}

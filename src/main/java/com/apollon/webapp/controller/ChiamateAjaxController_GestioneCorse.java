package com.apollon.webapp.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.apollon.Constants;
import com.apollon.model.Autoveicolo;
import com.apollon.model.Disponibilita;
import com.apollon.model.DisponibilitaDate;
import com.apollon.model.GestioneApplicazione;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.User;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMedia;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.DisponibilitaDateManager;
import com.apollon.service.DisponibilitaManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.ProvinceManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RichiestaAutistaMedioManager;
import com.apollon.service.RichiestaAutistaParticolareManager;
import com.apollon.service.RichiestaMediaManager;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilBukowski;
import com.apollon.util.firebase.FirebaseCloudMessaging;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultMedio;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeVenditoreUtil;
import com.apollon.webapp.util.corse.PanelMainCorseAdmin;
import com.apollon.webapp.util.corse.ValiditaPreventivo;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class ChiamateAjaxController_GestioneCorse extends BaseFormController {
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(final GestioneApplicazioneManager gestioneApplicazioneManager) {
        this.gestioneApplicazioneManager = gestioneApplicazioneManager;
    }
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	private RichiestaAutistaParticolareManager richiestaAutistaParticolareManager;
	@Autowired
	public void setRichiestaAutistaParticolareManager(RichiestaAutistaParticolareManager richiestaAutistaParticolareManager) {
		this.richiestaAutistaParticolareManager = richiestaAutistaParticolareManager;
	}
	
	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private RichiestaMediaManager richiestaMediaManager;
	@Autowired
	public void setRichiestaMediaManager(RichiestaMediaManager richiestaMediaManager) {
		this.richiestaMediaManager = richiestaMediaManager;
	}
	
	private RichiestaAutistaMedioManager richiestaAutistaMedioManager;
	@Autowired
	public void setRichiestaAutistaMedioManager(RichiestaAutistaMedioManager richiestaAutistaMedioManager) {
		this.richiestaAutistaMedioManager = richiestaAutistaMedioManager;
	}
    
    private DisponibilitaManager disponibilitaManager;
    @Autowired
    public void setDisponibilitaManager(final DisponibilitaManager disponibilitaManager) {
        this.disponibilitaManager = disponibilitaManager;
    }
    
    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
    
    private DisponibilitaDateManager disponibilitaDateManager;
    @Autowired
    public void setDisponibilitaDateManager(final DisponibilitaDateManager disponibilitaDateManager) {
        this.disponibilitaDateManager = disponibilitaDateManager;
    }

    private ProvinceManager provinceManager;
    @Autowired
    public void setProvinceManager(final ProvinceManager provinceManager) {
        this.provinceManager = provinceManager;
    }
    
    private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@RequestMapping(value = "/cancellaAttivaRicezionePreventiviCliente", method = RequestMethod.POST)
	public void cancellaAttivaRicezionePreventiviCliente(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse cancellaAttivaRicezionePreventiviCliente");
		try{
			String idRicercaTransfert = request.getParameter("idRicercaTransfert");
			String cancellaRicezionePreventivi = request.getParameter("cancellaRicezionePreventivi");
			String ricTransfert_Token = request.getParameter("ricTransfert_Token");
			RicercaTransfert ricercaTransfert = ricercaTransfertManager.get(Long.parseLong(idRicercaTransfert));
			JSONObject mainCorse = new JSONObject();
			log.debug("ricercaTransfert.getId:"+ ricercaTransfert.getId() );
			if( ricercaTransfert != null && ricercaTransfert.getRicTransfert_Token() != null && ricercaTransfert.getRicTransfert_Token().equals(ricTransfert_Token) 
					&& cancellaRicezionePreventivi != null ) {
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricercaTransfert);
				infoDatiPasseggero.put(Constants.CancellaRicezionePreventiviCliente, Boolean.parseBoolean(cancellaRicezionePreventivi));
				ricercaTransfert.setInfoPasseggero( infoDatiPasseggero.toString());
				ricercaTransfertManager.saveRicercaTransfert(ricercaTransfert);
				mainCorse.put("esito", Boolean.parseBoolean(cancellaRicezionePreventivi));
			}else {
				mainCorse.put("esito", false);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/inviaPreventivoClienteCorsaParticolare", method = RequestMethod.POST)
	public void inviaPreventivoClienteCorsaParticolare(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse inviaPreventivoClienteCorsaParticolare");
		try{
			String idcorsaParticolare = request.getParameter("idcorsaParticolare");
			RichiestaAutistaParticolare richAutistPart = richiestaAutistaParticolareManager.get(Long.parseLong(idcorsaParticolare));
			JSONObject mainCorse = new JSONObject();
			log.debug("richAutistPart.getId:"+ richAutistPart.getId() );
			if( richAutistPart.getPrezzoTotaleAutista() != null  && richAutistPart.getPrezzoTotaleAutista().compareTo(BigDecimal.ZERO) > 0 
					&& (richAutistPart.getPreventivo_inviato_cliente() == null || richAutistPart.getPreventivo_inviato_cliente() == false)
					&& richAutistPart.getPreventivo_validita_data() != null ) {
				if( richAutistPart.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato() == null
						&& richAutistPart.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato_Multiplo() == null
						&& (richAutistPart.getRicercaTransfert().getCancellaRicezionePreventiviCliente() == null || 
							richAutistPart.getRicercaTransfert().getCancellaRicezionePreventiviCliente() == false) ) {
					JSONObject infoCorsa = HomeUtil.GetInfoCorsa(richAutistPart);
					infoCorsa.put(Constants.Preventivo_inviato_cliente, true);
					richAutistPart.setInfoCorsa(infoCorsa.toString());
					richAutistPart = richiestaAutistaParticolareManager.saveRichiestaAutistaParticolare(richAutistPart);
					// INVIA SMS E EMAIL PREVENTIVO AL CLIENTE
					HomeUtil_Sms_Email.Security_InviaSmsEmailPreventivoAutistaInviato_Cliente(richAutistPart, request, velocityEngine);
					mainCorse.put("esito", true);
					String Messaggio = "Preventivo Inviato"; mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
				}else if( richAutistPart.getRicercaTransfert().getCancellaRicezionePreventiviCliente() != null 
						&& richAutistPart.getRicercaTransfert().getCancellaRicezionePreventiviCliente() == true ) {
					mainCorse.put("esito", false);
					String Messaggio = "Preventivo Non Inviato, Il Cliente non vuole più ricevere preventivi";
					mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
				}else {
					mainCorse.put("esito", false);
					String Messaggio = "Preventivo Non Inviato, Il Cliente ha già acquistato un Preventivo da un tuo collega";
					mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
				}
			}else {
				mainCorse.put("esito", false);
				String Messaggio = "Preventivo Non Inviato Inserire i Dati"; mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/setPreventivoPrezzo", method = RequestMethod.POST)
	public void setPreventivoPrezzo(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse setPreventivoPrezzo");
		try{
			String idcorsaParticolare = request.getParameter("idcorsaParticolare");
			String prezzoReq = request.getParameter("prezzo");
			RichiestaAutistaParticolare richAutistPart = richiestaAutistaParticolareManager.get(Long.parseLong(idcorsaParticolare));
			JSONObject mainCorse = new JSONObject(); BigDecimal prezzo = BigDecimal.ZERO; String Messaggio = "";
			try {
				prezzo = new BigDecimal( prezzoReq.replace(",", ".") ).setScale(2, RoundingMode.HALF_EVEN);
				if( (richAutistPart.getPreventivo_inviato_cliente() == null || richAutistPart.getPreventivo_inviato_cliente() == false) 
						&& prezzo.compareTo(BigDecimal.ZERO) > 0 ) {
					final Integer MaxPercentualeRibasso = (int)(long)gestioneApplicazioneManager.getName("NO_GUERRA_PREZZO_AL_RIBASSO_NCC").getValueNumber();
					BigDecimal PrezzoMinimoPreventivo = CalcoloPrezzi.DammiPrezzo_NO_GUERRA_PREZZO_AL_RIBASSO_NCC(richAutistPart, MaxPercentualeRibasso);
					PrezzoMinimoPreventivo = null;
					if(PrezzoMinimoPreventivo == null || PrezzoMinimoPreventivo.compareTo(prezzo) < 0) {
						richAutistPart.setPrezzoTotaleAutista(prezzo);
						BigDecimal PrezzoCommissioneServizio = CalcoloPrezzi.CalcolaPercentuale(prezzo, (int)(long)richAutistPart.getPercentualeServizio());
						richAutistPart.setPrezzoCommissioneServizio(PrezzoCommissioneServizio);
						richAutistPart = richiestaAutistaParticolareManager.saveRichiestaAutistaParticolare(richAutistPart);
						mainCorse.put("esito", true); Messaggio = "Prezzo Inserito";
						mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
					}else {
						mainCorse.put("esito", false); Messaggio = getText("messaggio.prezzo.basso", new Object[] { PrezzoMinimoPreventivo }, request.getLocale());
						mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
					}
				}else {
					mainCorse.put("esito", false); Messaggio = getText("errors.invalid", new Object[] { prezzoReq }, request.getLocale());
					mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
				}
			}catch(Exception exc) {
				mainCorse.put("esito", false); Messaggio = getText("errors.invalid", new Object[] { prezzoReq }, request.getLocale());
				mainCorse.put("messaggio", Messaggio); saveMessage(request, Messaggio);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/setPreventivoPeriodoValidita", method = RequestMethod.POST)
	public void setPreventivoPeriodoValidita(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse setPreventivoPeriodoValidita");
		try{
			String idcorsaParticolare = request.getParameter("idcorsaParticolare");
			String idPeriodo = request.getParameter("idPeriodo");
			RichiestaAutistaParticolare richAutistPart = richiestaAutistaParticolareManager.get(Long.parseLong(idcorsaParticolare));
			JSONObject mainCorse = new JSONObject();
			System.out.println("richAutistPart.getId:"+ richAutistPart.getId() );
			System.out.println("idPeriodo:"+ idPeriodo );
			if( richAutistPart.getPreventivo_inviato_cliente() == null || richAutistPart.getPreventivo_inviato_cliente() == false) {
				JSONObject infoCorsa = (richAutistPart.getInfoCorsa() != null && !richAutistPart.getInfoCorsa().contentEquals("") 
						? new JSONObject(richAutistPart.getInfoCorsa()) : new JSONObject() );
				Calendar calendar = Calendar.getInstance();
				calendar.add( 
						ValiditaPreventivo.DammiPeriodo_by_Id(Integer.parseInt(idPeriodo)).getTipoPeriodoCalendar(), 
						ValiditaPreventivo.DammiPeriodo_by_Id(Integer.parseInt(idPeriodo)).getValorePeriodoCalendar());
				infoCorsa.put(Constants.Preventivo_validita_data, calendar.getTime().getTime());
				richAutistPart.setInfoCorsa(infoCorsa.toString());
				richAutistPart = richiestaAutistaParticolareManager.saveRichiestaAutistaParticolare(richAutistPart);
				mainCorse.put("esito", "ok");
				saveMessage(request, "Preventivo Aggiornato");
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	

	
	@RequestMapping(value = "/impostaPercentualeProvinciaVenditore", method = RequestMethod.POST)
    public void impostaPercentualeProvinciaVenditore(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JSONObject esito = new JSONObject();
    	try {
    		response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    		String idElement = request.getParameter("idElement");
    		String tariffaStr = request.getParameter("tariffaVal");
    		Map<String, Integer> mapPercentualiVenditore = ApplicationUtils.DammiParametriPercentualiVenditori();
    		User user = getUserManager().getUserByUsername(request.getRemoteUser());
    		boolean esitoSalvataggio = TariffeVenditoreUtil.SalvaVenditorePercServProvincia(mapPercentualiVenditore, 
    				user, provinceManager.get(Long.parseLong(idElement)), tariffaStr);
    		esito.put("esito", esitoSalvataggio); 
    		esito.put("message", "ok");
    	    response.getWriter().write(esito.toString());
    	}catch(Exception e){
    		esito.put("esito", false);
    		esito.put("message", e.getMessage());
    	    response.getWriter().write(esito.toString());
    	}
	}
	
	@RequestMapping(value = "/modificaTransfer", method = RequestMethod.POST)
	public void modificaTransfer(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse modificaTransfer");
		try{
			//System.out.println( request.getParameter("data") );
			Map<String, String> mapParameters = new LinkedHashMap<String, String>();
		    String query = request.getParameter("data");
		    String[] pairs = query.split("&");
		    for(String pair: pairs) {
		        int idx = pair.indexOf("=");
		        mapParameters.put(URLDecoder.decode(pair.substring(0, idx), Constants.ENCODING_UTF_8), 
		        		URLDecoder.decode(pair.substring(idx + 1), Constants.ENCODING_UTF_8));
		    }
		    /*
		    for (Map.Entry<String, String> entry : mapParameters.entrySet()) {
		        System.out.println("Modifica Transfer "+entry.getKey() + "/" + entry.getValue());
		    }
		    */
		    boolean esitoModifica = false;
		    RicercaTransfert ricTransf = ricercaTransfertManager.get(Long.parseLong( mapParameters.get("idTransfer")));
		    JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransf);
		    if(mapParameters.get("check-ricezione-preventivi") != null){
				infoDatiPasseggero.put(Constants.CancellaRicezionePreventiviCliente, true);
				ricTransf.setInfoPasseggero( infoDatiPasseggero.toString());
				esitoModifica = true;
		    }else if( mapParameters.get("check-ricezione-preventivi") == null && ricTransf.getCancellaRicezionePreventiviCliente() != null)  {
				infoDatiPasseggero.put(Constants.CancellaRicezionePreventiviCliente, false);
				ricTransf.setInfoPasseggero( infoDatiPasseggero.toString());
				esitoModifica = true;
		    }
		    if( !ricTransf.getFormattedAddress_Partenza().equals(mapParameters.get("formattedAddress_Partenza")) ){
		    	ricTransf.setFormattedAddress_Partenza( mapParameters.get("formattedAddress_Partenza") );
		    	esitoModifica = true;
		    }
		    if( !ricTransf.getFormattedAddress_Arrivo().equals(mapParameters.get("formattedAddress_Arrivo")) ){
		    	ricTransf.setFormattedAddress_Arrivo( mapParameters.get("formattedAddress_Arrivo") );
		    	esitoModifica = true;
		    }
		    if( !ricTransf.getPlace_id_Partenza().equals(mapParameters.get("place_id_Partenza")) ){
		    	ricTransf.setPlace_id_Partenza( mapParameters.get("place_id_Partenza") );
		    	esitoModifica = true;
		    }
		    if( !ricTransf.getPlace_id_Arrivo().equals(mapParameters.get("place_id_Arrivo")) ){
		    	ricTransf.setPlace_id_Arrivo( mapParameters.get("place_id_Arrivo") );
		    	esitoModifica = true;
		    }
		    //System.out.println("dataOraPrelevamento: "+mapParameters.get("dataOraPrelevamento") +" oraPrelevamento: "+ mapParameters.get("oraPrelevamento"));
		    if( (ricTransf.getDataOraPrelevamentoDate().getTime() != Long.parseLong(mapParameters.get("dataOraPrelevamento")))
		    		|| !ricTransf.getOraPrelevamento().equals(mapParameters.get("oraPrelevamento")) ){
		    	Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(mapParameters.get("dataOraPrelevamento"), mapParameters.get("oraPrelevamento"));
		    	ricTransf.setDataOraPrelevamentoDate( dataPrelevamento );
		    	ricTransf.setDataOraPrelevamento( String.valueOf(dataPrelevamento.getTime()) );
		    	ricTransf.setOraPrelevamento( mapParameters.get("oraPrelevamento") );
		    	esitoModifica = true;
		    }
		    // Modifica Ritorno
		    if(ricTransf.isRitorno() && mapParameters.get("ritorno") != null && mapParameters.get("ritorno").equals("on") && 
		    	( mapParameters.get("dataOraRitorno") != null && !mapParameters.get("dataOraRitorno").equals("") 
		    		&& ricTransf.getDataOraRitornoDate().getTime() != Long.parseLong(mapParameters.get("dataOraRitorno"))) 
		    	|| 
		    	( mapParameters.get("oraRitorno") != null && !mapParameters.get("oraRitorno").equals("")
				    && !ricTransf.getOraRitorno().equals("") && !ricTransf.getOraRitorno().equals(mapParameters.get("oraRitorno"))) ){
		    	SalvaRitorno(mapParameters, ricTransf);
		    	esitoModifica = true;
		    }
		    // Elimina Ritorno
		    if(ricTransf.isRitorno() && mapParameters.get("ritorno") == null){
		    	ricTransf.setDataOraRitornoDate( null );
		    	ricTransf.setDataOraRitorno( "" );
		    	ricTransf.setOraRitorno( "" );
		    	ricTransf.setRitorno(false);
		    	esitoModifica = true;
		    }
		    // Crea Ritorno
		    if(!ricTransf.isRitorno() && mapParameters.get("ritorno") != null && mapParameters.get("ritorno").equals("on") && 
		    	mapParameters.get("dataOraRitorno") != null && !mapParameters.get("dataOraRitorno").equals("") && ricTransf.getDataOraRitornoDate() == null 
		    	&& mapParameters.get("oraRitorno") != null && !mapParameters.get("oraRitorno").equals("") && ricTransf.getOraRitorno().equals("") ){
		    	SalvaRitorno(mapParameters, ricTransf);
		    	esitoModifica = true;
		    }
		    if(ricTransf.getNumeroPasseggeri() != Integer.parseInt(mapParameters.get("numeroPasseggeri"))){
		    	ricTransf.setNumeroPasseggeri( Integer.parseInt(mapParameters.get("numeroPasseggeri")) );
		    	esitoModifica = true;
		    }
		    if(ricTransf.getNotePerAutista() != null && !ricTransf.getNotePerAutista().equals(mapParameters.get("noteAutista"))){
		    	ricTransf.setNotePerAutista(mapParameters.get("noteAutista"));
		    	esitoModifica = true;
		    }
		    if(ricTransf.getNote() != null && !ricTransf.getNote().equals(mapParameters.get("note"))){
		    	ricTransf.setNote(mapParameters.get("note"));
		    	esitoModifica = true;
		    }
		    if(esitoModifica == true){
		    	ricercaTransfertManager.saveRicercaTransfert(ricTransf);
		    }
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", esitoModifica);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static RicercaTransfert SalvaRitorno(Map<String, String> mapParameters, RicercaTransfert ricTransf) throws ParseException {
		Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca(mapParameters.get("dataOraRitorno"), mapParameters.get("oraRitorno"));
    	ricTransf.setDataOraRitornoDate( dataRitorno );
    	ricTransf.setDataOraRitorno( String.valueOf(dataRitorno.getTime()) );
    	ricTransf.setOraRitorno( mapParameters.get("oraRitorno") );
    	ricTransf.setRitorno(true);
    	return ricTransf;
	}
	
	@RequestMapping(value = "/inviaEmailCorsaAcquistataCliente", method = RequestMethod.POST)
	public void inviaEmailCorsaAcquistataCliente(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse inviaEmailCorsaAcquistataCliente");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			String EmailCliente = request.getParameter("emailCliente");
			RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
			boolean esito = InviaEmail.InviaEmailCorsaAcquistata_Cliente(ricTransf, EmailCliente, request, velocityEngine, false);
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", esito);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/modificaDatiPasseggeroCorsa", method = RequestMethod.POST)
	public void modificaDatiPasseggeroCorsa(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse modificaDatiPasseggeroCorsa");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			boolean modificaDati = Boolean.parseBoolean(request.getParameter("modificaDati"));
			String nomePasseggero = request.getParameter("nomePasseggero");
			String telefonoPasseggero = request.getParameter("telefonoPasseggero");
			JSONObject infoDatiPasseggero;
			if(modificaDati){
				RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransf);
				infoDatiPasseggero.put("esito", true);
				infoDatiPasseggero.put(Constants.NomePasseggeroJSON, nomePasseggero);
				infoDatiPasseggero.put(Constants.TelefonoPasseggeroJSON, telefonoPasseggero);
				ricTransf.setInfoPasseggero(infoDatiPasseggero.toString());
				ricTransf = ricercaTransfertManager.saveRicercaTransfert(ricTransf); 
			}else{
				RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				infoDatiPasseggero = new JSONObject(ricTransf.getInfoPasseggero());
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(infoDatiPasseggero.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
    @RequestMapping(value = "/impostaTariffaProvincia", method = RequestMethod.POST)
    public void impostaTariffaProvincia(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JSONObject esito = new JSONObject();
    	try {
    		response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    		String idElement = request.getParameter("idElement");
    		String tariffaStr = request.getParameter("tariffaVal");
    		Province provincia = provinceManager.get( Long.parseLong(idElement) );
    		provincia.setTariffaBase( new BigDecimal(tariffaStr) );
    		provinceManager.saveProvince(provincia);
    		esito.put("esito", true); 
    		esito.put("message", "ok");
    	    response.getWriter().write(esito.toString());
    	}catch(Exception e){
    		esito.put("esito", false);
    		esito.put("message", e.getMessage());
    	    response.getWriter().write(esito.toString());
    	}
	}
	
    
    @RequestMapping(value = "GetClientSecretStripe", method = RequestMethod.POST)
	public void GetClientSecretStripe(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse GetClientSecretStripe");
		String idRicTransfert = request.getParameter("idRicTransfert");
		String idRichAutistaPart = request.getParameter("idRichAutistaPart");
		try {
			RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
			String telefonoCustomer = ricTransfert.getPhoneNumberCustomer();
			String totaleEuro = "";
			if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) ) {
				AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
				totaleEuro = agendaAutistaScelta.getPrezzoTotaleCliente().toString();
				telefonoCustomer = ricTransfert.getPhoneNumberCustomer();
				
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {
				RichiestaMedia richMedia = richiestaMediaManager.getRichiestaMedia_by_IdRicercaTransfert( ricTransfert.getId() );
				totaleEuro = ricTransfert.isPagamentoParziale() ? richMedia.getPrezzoCommissioneServizio().toString() : richMedia.getPrezzoTotaleCliente().toString();

			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
				RichiestaAutistaParticolare richAutPart = richiestaAutistaParticolareManager.get(Long.parseLong(idRichAutistaPart));
				totaleEuro = richAutPart.getPrezzoTotaleCliente().toString();
				
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)  ) {
				String[] parts = idRichAutistaPart.split("-");
				BigDecimal totBigDecimal = BigDecimal.ZERO;
				for(String ite: parts) {
					RichiestaAutistaParticolare richAutPart_Multiplo = richiestaAutistaParticolareManager.getRichiestaAutista_by_token(ite);
					totBigDecimal = totBigDecimal.add(richAutPart_Multiplo.getPrezzoTotaleCliente());
				}
				totaleEuro = totBigDecimal.toString();
			}
			JSONObject mainJson = new JSONObject();
			long stripeSWITCH = gestioneApplicazioneManager.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
			if(stripeSWITCH == 0l) {
				Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST;  } else { Stripe.apiKey = Constants.STRIPE_SECRET_KEY_LIVE; 
				mainJson.put("GetClientSecretStripe", Constants.STRIPE_SECRET_KEY_LIVE);
			}
			totaleEuro = totaleEuro.replace(".", ""); // questo perche stripe prende le valute in centesimi, per esempio 3.30 euro sono 330 centesimi
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("eur").setAmount(Long.parseLong(totaleEuro))
					.setDescription("tel. customer: "+telefonoCustomer).build();
			PaymentIntent intent = PaymentIntent.create(createParams);
			mainJson.put("GetClientSecretStripe", intent.getClientSecret());
			response.setContentType("application/json");
		    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
		    response.getWriter().write(mainJson.toString());		 
		}catch(Exception ee){
			ee.printStackTrace();
		}
    }
    
	@RequestMapping(value = "/controlloPrePayment", method = RequestMethod.POST)
	public void controlloPrePayment(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse controlloPrePayment");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			String idRichAutistaPart = request.getParameter("idRichAutistaPart");
			RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
			// SET NUMERO ORE VALIDITA' TIPO CORSE
			String resultTipoCorsaValApp = "";
			if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)  ){
				resultTipoCorsaValApp = "NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART";
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ){
				resultTipoCorsaValApp = "NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA";
			}
			// ERRORI GENERICI PER TUTTI I TIPO CORSA
			JSONObject errore = new JSONObject();
			Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(ricTransfert.getDataOraPrelevamento(),ricTransfert.getOraPrelevamento());
			if( !ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(dataPrelevamento, ricTransfert.getTipoServizio()) ) {
				long numOre = gestioneApplicazioneManager.getName( resultTipoCorsaValApp ).getValueNumber();
				saveMessage(request, getText("errors.dataPartenzaDeveEssereUnOraSuccessivaDaAdesso", new Object[] { numOre },  request.getLocale() ) );
				errore.put("tipoErrore", "dataPartenzaDeveEssereUnOraSuccessivaDaAdesso");
				errore.put("messErrore", getText("errors.dataPartenzaDeveEssereUnOraSuccessivaDaAdesso", new Object[] { numOre },  request.getLocale() ));
			}
			if(ricTransfert.isPagamentoEseguitoMedio() == true) {
				saveMessage(request, "pagamento già eseguito, Corsa Confermata.");
				errore.put("tipoErrore", "pagamentoNonAutorizzato");
				errore.put("messErrore", "pagamento già eseguito, Corsa Confermata");
			}
			// ERRRORI PER TIPO SERVIZIO 
			if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
				if( agendaAutistaScelta.getPrezzoTotaleCliente().compareTo(ricTransfert.getAgendaAutista_PrezzoTotaleCliente_Temp()) != 0 ) {
					saveMessage(request, "il prezzo è cambiato: "+agendaAutistaScelta.getPrezzoTotaleCliente()+"euro, ripetere la ricerca");
					errore.put("tipoErrore", "pagamentoNonAutorizzato");
					errore.put("messErrore", "il prezzo è cambiato, ripetere la ricerca");
				}
				if(!ricTransfert.isVerificatoCustomer()){
					saveMessage(request, "pagamento non autorizzato.");
					errore.put("tipoErrore", "pagamentoNonAutorizzato");
					errore.put("messErrore", "pagamento non autorizzato");
				}
			}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
				if( !ControlloDateRicerca.ControlloDataRicercercaSuperioreDi24OreDaAdesso(ricTransfert.getDataRicerca()) ) {
					saveMessage(request, getText("errors.dataRicercaScaduta", request.getLocale() ) );
					errore.put("tipoErrore", "dataRicercaScaduta");
					errore.put("messErrore", getText("errors.dataRicercaScaduta", request.getLocale() ));
				}
				
				/*
				// CONTROLLO CORSE SOVRAPPOSTE AUTISTA DISPONIBILE
				List<Long> AutistiDisponibiliList = new ArrayList<Long>();
				ControlloDateRicerca ControlloDateRicerca = new ControlloDateRicerca();
				for(Long listTariffeAutisti_ite : ricTransfert.getAutistiClasseAutoveicoloScelto()) {
					if( ControlloDateRicerca.ControlloAutistaCorseSovrapposte_MAIN(ricTransfert, listTariffeAutisti_ite) ) {
						AutistiDisponibiliList.add( listTariffeAutisti_ite );
					}
				}
				if( AutistiDisponibiliList.size() < 1) {
					saveMessage(request, "pagamento non eseguito: Autista non disponibile, modificare la Data o Ora di Andata e/o di Ritorno"  );
					errore.put("tipoErrore", "autistiNonDisponibili");
					errore.put("messErrore", "pagamento non eseguito: Autista non disponibile, modificare la Data o Ora di Andata e/o di Ritorno");
				}
				*/
				
				if(!ricTransfert.isVerificatoCustomer()) {
					saveMessage(request, "pagamento non autorizzato.");
					errore.put("tipoErrore", "pagamentoNonAutorizzato");
					errore.put("messErrore", "pagamento non autorizzato");
				}
			}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
				RichiestaAutistaParticolare richAutPart = new RichiestaAutistaParticolare(); 
				if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
					richAutPart = richiestaAutistaParticolareManager.get(Long.parseLong(idRichAutistaPart));
				}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
					String[] parts = idRichAutistaPart.split("-");
					List<RichiestaAutistaParticolare> richAutistaPartMultiploList = new ArrayList<RichiestaAutistaParticolare>();
					for(String ite: parts) {
						RichiestaAutistaParticolare richAutPart_Multiplo = richiestaAutistaParticolareManager.getRichiestaAutista_by_token(ite);
						richAutistaPartMultiploList.add(richAutPart_Multiplo);
					}
					richAutPart.setRicercaTransfert(ricTransfert);
					richAutPart.setRichiestaAutistaParticolareMultiploList(richAutistaPartMultiploList);
				}
				boolean preventivoValiditaData = false;
				if(richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
					preventivoValiditaData = HomeUtil.Controllo_Preventivo_validita_data(richAutPart);
				}else if(richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
					for(RichiestaAutistaParticolare ite: richAutPart.getRichiestaAutistaParticolareMultiploList()) {
						preventivoValiditaData = HomeUtil.Controllo_Preventivo_validita_data(ite);
						if( !preventivoValiditaData ) { break; }
					}
				}
				if(preventivoValiditaData == false) {
					saveMessage(request, getText("richiesta.part.preventivo.scaduto.data.superata", request.getLocale()));
					errore.put("tipoErrore", "pagamentoNonAutorizzato");
					errore.put("messErrore", getText("richiesta.part.preventivo.scaduto.data.superata", request.getLocale()));
				}
				/*
				// CONTROLLO CORSE SOVRAPPOSTE AUTISTA DISPONIBILE
				boolean AutistaCorsaDisponibile = false;
				ControlloDateRicerca ControlloDateRicerca = new ControlloDateRicerca();
				if(richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
					AutistaCorsaDisponibile = ControlloDateRicerca.ControlloAutistaCorseSovrapposte_MAIN(ricTransfert, richAutPart.getAutoveicolo().getAutista().getId());
					
				}else if(richAutPart.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
					for(RichiestaAutistaParticolare ite: richAutPart.getRichiestaAutistaParticolareMultiploList()) {
						AutistaCorsaDisponibile = ControlloDateRicerca.ControlloAutistaCorseSovrapposte_MAIN(ricTransfert, ite.getAutoveicolo().getAutista().getId());
						if( !AutistaCorsaDisponibile ) { break; }
					}
				}
				if(AutistaCorsaDisponibile == false) {
					saveMessage(request, "pagamento non eseguito: Autista non disponibile, modificare la Data o Ora di Andata e/o di Ritorno" );
					errore.put("tipoErrore", "autistiNonDisponibili");
					errore.put("messErrore", "pagamento non eseguito: Autista non disponibile, modificare la Data o Ora di Andata e/o di Ritorno" );
				}
				*/
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(errore.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	

	/**
	 * Controllo che il token è corretto perché la chiamata ajax può essere fatta da chiunque abbia un browser.
	 * Il controllo avviene inviando un messaggio con il tokene aggiungendo il restricted_package_name. 
	 * Se il messaggio viene inviato correttamente allora il token proviene da una fonte sicura
	 */
	@RequestMapping(value = "/setTokenDeviceFcm", method = RequestMethod.POST)
	public void setTokenFcm(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse setTokenDeviceFcm");
		String tokenDeviceFcm = request.getParameter("tokenDeviceFcm");
		try{
			System.out.println("Nuovo tokenDeviceFcm: "+tokenDeviceFcm);
			JSONObject mainCorsa = new JSONObject();
			int responseCode = FirebaseCloudMessaging.sendCommonMessageConfirmToken(tokenDeviceFcm);
			mainCorsa.put("responseCode", responseCode);
			if(responseCode == FirebaseCloudMessaging.RESPONSE_CODE_MESSAGGIO_INVIATO) {
				GestioneApplicazione gestioneApp_TokenDeviceFcm = gestioneApplicazioneManager.getName("TOKEN_DEVICE_FCM");
				System.out.println("Old tokenDeviceFcm: "+gestioneApp_TokenDeviceFcm.getValueString());
				gestioneApp_TokenDeviceFcm.setValueString(tokenDeviceFcm);
				gestioneApplicazioneManager.saveGestioneApplicazione(gestioneApp_TokenDeviceFcm);
				mainCorsa.put("esito", "Nuovo tokenDeviceFcm settato");
			}else{
				mainCorsa.put("esito", "Token non settato");
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorsa.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/dammiInfoAutista", method = RequestMethod.POST)
	public void dammiInfoAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse dammiInfoAutista");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			Integer NumOreInfoAutistaCliente = (int)(long)gestioneApplicazioneManager.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			Object tipoServizioObj = ricercaTransfertManager.DammiTipoServizio(Long.parseLong(idRicTransfert));
			JSONObject mainCorsa = new JSONObject();
			if(tipoServizioObj.equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				RicercaTransfert ricTransfert = ricercaTransfertManager.getInfoAutista_by_ClientePanel_AgendaAutista(Long.parseLong(idRicTransfert), NumOreInfoAutistaCliente);
				if( ricTransfert != null ) {
					AgendaAutistaScelta ageScelta = ricTransfert.getAgendaAutistaScelta();
					String FullName = "";
					if( ageScelta.getAgendaAutista_AutistaRitorno() != null && 
							ageScelta.getAgendaAutista_AutistaAndata().getIdAutista() != ageScelta.getAgendaAutista_AutistaRitorno().getIdAutista() ) {
						FullName = "ANDATA: "+ageScelta.getAgendaAutista_AutistaAndata().getFullName()+" RITORNO: "+ageScelta.getAgendaAutista_AutistaRitorno().getFullName();
						mainCorsa.put("telefonoRitorno", ageScelta.getAgendaAutista_AutistaRitorno().getPhoneNumber());
					}else {
						FullName = ageScelta.getAgendaAutista_AutistaAndata().getFullName();
					}
					mainCorsa.put("esitoCorsaAgendaAutista", true);
					mainCorsa.put("telefonoAndata", ageScelta.getAgendaAutista_AutistaAndata().getPhoneNumber());
					mainCorsa.put("fullName", FullName);
					mainCorsa.put("esitoCorsa", true);
				}
			}else if(tipoServizioObj.equals(Constants.SERVIZIO_STANDARD)) {
				RichiestaMediaAutista richiestaMediaAutista = richiestaAutistaMedioManager.getInfoAutista_by_ClientePanel(Long.parseLong(idRicTransfert), NumOreInfoAutistaCliente);
				if(richiestaMediaAutista != null) {
					mainCorsa.put("esitoCorsaMedio", true);
					mainCorsa.put("telefono", richiestaMediaAutista.getAutista().getUser().getPhoneNumber());
					mainCorsa.put("fullName", richiestaMediaAutista.getAutista().getUser().getFullName());
					mainCorsa.put("autoveicoloRichiesto", AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(richiestaMediaAutista.getRichiestaMediaAutistaAutoveicolo(), request));
					mainCorsa.put("esitoCorsa", true);
				}
			}else if(tipoServizioObj.equals(Constants.SERVIZIO_PARTICOLARE)) {
				RichiestaAutistaParticolare richPart = richiestaAutistaParticolareManager.getInfoAutista_by_ClientePanel(Long.parseLong(idRicTransfert), NumOreInfoAutistaCliente);
				if(richPart != null) {
					mainCorsa.put("esitoCorsaParticolare", true);
					mainCorsa.put("telefono", richPart.getAutoveicolo().getAutista().getUser().getPhoneNumber());
					mainCorsa.put("fullName", richPart.getAutoveicolo().getAutista().getUser().getFullName());
					mainCorsa.put("esitoCorsa", true);
				}
			}else if(tipoServizioObj.equals(Constants.SERVIZIO_MULTIPLO)) {
				// TODO CONTINUARE DA QUI
				List<RichiestaAutistaParticolare> richiestaAutistaParticolareMultiplo = richiestaAutistaParticolareManager.getInfoAutista_by_ClientePanel_CorsaMultipla(Long.parseLong(idRicTransfert), NumOreInfoAutistaCliente);
				if(richiestaAutistaParticolareMultiplo != null) {
					mainCorsa.put("esitoCorsaMultipla", true);
					List<String[]> AutistiList = new ArrayList<String[]>();
					for(int i=0; i < richiestaAutistaParticolareMultiplo.size(); i++ ) {
						String[] NomeTelefonoAutista_Array = new String[2];
						NomeTelefonoAutista_Array[0] = richiestaAutistaParticolareMultiplo.get(i).getAutoveicolo().getAutista().getUser().getFullName();
						NomeTelefonoAutista_Array[1] = richiestaAutistaParticolareMultiplo.get(i).getAutoveicolo().getAutista().getUser().getPhoneNumber();
						AutistiList.add(NomeTelefonoAutista_Array);
					}
					mainCorsa.put("AutistiList", AutistiList );
					mainCorsa.put("esitoCorsa", true);
				}
			}else {
				mainCorsa.put("esitoCorsa", false);
			}
			mainCorsa.put("NumOreInfoAutistaCliente", NumOreInfoAutistaCliente);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorsa.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}

	
	private static JSONObject DammiInfoClienteMessaggioJSON(JSONObject mainCorsa, boolean chiamataConfermata, Date oraDataPrelevamento, String nomeCliente, String telefonoCliente, 
			String nomePasseggero, String telefonoPasseggero, Integer NumOreInfoAutistaCliente){
		String messaggio = "";
		boolean esito = false;
	    Date date = DateUtil.AggiungiOre_a_DataAdesso( NumOreInfoAutistaCliente );
		if(chiamataConfermata && oraDataPrelevamento.before(date)){
			esito = true;
			if(nomePasseggero != null && !nomePasseggero.trim().equals("") && telefonoPasseggero != null && !telefonoPasseggero.trim().equals("")){
				messaggio = "<div class='form-group col-sm-12 text-center'>"
						+"<a class='btn btn-sm btn-primary' href='tel:+"+telefonoPasseggero+"'><span class='glyphicon glyphicon-phone'></span> (Passeggero) "+nomePasseggero+" "+telefonoPasseggero+"</a>"
						+"</div>";
			}else if( telefonoPasseggero != null && !telefonoPasseggero.trim().equals("") && (nomePasseggero == null || nomePasseggero.trim().equals("")) ){
				messaggio = "<div class='form-group col-sm-12 text-center'>"
						+"<a class='btn btn-sm btn-primary' href='tel:+"+telefonoPasseggero+"'><span class='glyphicon glyphicon-phone'></span> (Passeggero) "+telefonoPasseggero+"</a>"
						+"</div>";
			}else if( nomePasseggero != null && !nomePasseggero.trim().equals("") && (telefonoPasseggero == null || telefonoPasseggero.trim().equals("")) ){
				messaggio = "<div class='form-group col-sm-12 text-center'>"
						+"<a class='btn btn-sm btn-primary'><span class='glyphicon glyphicon-user'></span> (Passeggero) "+nomePasseggero+"</a>"
						+"</div>";
			}
			messaggio += "<div class='form-group col-sm-12 text-center'>"
				+"<a class='btn btn-sm btn-primary' href='tel:+"+telefonoCliente+"'><span class='glyphicon glyphicon-phone'></span> (Cliente) "+nomeCliente+" "+telefonoCliente+"</a>"
				+"</div>";
		}else{
			if(chiamataConfermata){
				messaggio = "<big>E' possibile visualizzare il Contatto Telefonico del Cliente a partire da "+NumOreInfoAutistaCliente+" ore prima del prelevamento!</big>";
			}else{
				messaggio = "<big>Per visualizzare le info Cliente deve essere Prenotata la Corsa</big>";
			}
		}
		mainCorsa.put("esito", esito);
		mainCorsa.put("titolo", "Info Cliente");
		mainCorsa.put("messaggio", messaggio);
		return mainCorsa;
	}
	
	
	@RequestMapping(value = "/dammiInfoCliente", method = RequestMethod.POST)
	public void dammiInfoCliente(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse dammiInfoCliente");
		try{
			String idRicTransfert = request.getParameter("idRicTransfert");
			String tokenAutista = request.getParameter("tokenAutista");
			System.out.println(tokenAutista);
			Integer NumOreInfoAutistaCliente = (int)(long)gestioneApplicazioneManager.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			JSONObject mainCorsa = new JSONObject();
			if( tokenAutista != null && !tokenAutista.contentEquals("") ) {
				RichiestaMediaAutista richMediaAutista = richiestaAutistaMedioManager.getInfoCliente_by_AutistaPanel(Long.parseLong(idRicTransfert), tokenAutista, NumOreInfoAutistaCliente);
				if(richMediaAutista != null){
					DammiInfoClienteMessaggioJSON(mainCorsa, richMediaAutista.isCorsaConfermata(), 
							richMediaAutista.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate(), 
							richMediaAutista.getRichiestaMedia().getRicercaTransfert().getUser().getFullName(), 
							richMediaAutista.getRichiestaMedia().getRicercaTransfert().getUser().getPhoneNumber(), 
							richMediaAutista.getRichiestaMedia().getRicercaTransfert().getNomePasseggero(), 
							richMediaAutista.getRichiestaMedia().getRicercaTransfert().getTelefonoPasseggero(), NumOreInfoAutistaCliente);
				}else{
					RichiestaAutistaParticolare RichiestaParticAutista = richiestaAutistaParticolareManager.getInfoCliente_by_AutistaPanel(Long.parseLong(idRicTransfert), tokenAutista, NumOreInfoAutistaCliente);
					if(RichiestaParticAutista != null){
						DammiInfoClienteMessaggioJSON(mainCorsa, RichiestaParticAutista.isInvioSmsCorsaConfermata(),
								RichiestaParticAutista.getRicercaTransfert().getDataOraPrelevamentoDate(), 
								RichiestaParticAutista.getRicercaTransfert().getUser().getFullName(), 
								RichiestaParticAutista.getRicercaTransfert().getUser().getPhoneNumber(), 
								RichiestaParticAutista.getRicercaTransfert().getNomePasseggero(), 
								RichiestaParticAutista.getRicercaTransfert().getTelefonoPasseggero(), NumOreInfoAutistaCliente);
					}
				}
			}else {
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) ) {
					DammiInfoClienteMessaggioJSON(mainCorsa, true, ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getUser().getFullName(), 
							ricTransfert.getUser().getPhoneNumber(), ricTransfert.getNomePasseggero(), ricTransfert.getTelefonoPasseggero(), NumOreInfoAutistaCliente);
				}
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorsa.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/togliPrenotaCorsaMediaAutista", method = RequestMethod.POST)
	public void togliPrenotaCorsaMediaAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse togliPrenotaCorsaMediaAutista");
		try{
			String idcorsaMedia = request.getParameter("idcorsaMedia");
			RichiestaMediaAutista richAutistMedio =
					richiestaAutistaMedioManager.get(Long.parseLong(idcorsaMedia));
			JSONObject mainCorse = new JSONObject();
			richAutistMedio.setChiamataPrenotata(false);
			richAutistMedio.setDataChiamataPrenotata(null);
			richAutistMedio = richiestaAutistaMedioManager.saveRichiestaAutistaMedio( richAutistMedio );
			mainCorse.put("esito", true);
			saveMessage(request, "Tolta Riserva dalla Corsa"/*getText("user.saved", locale )*/ );
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/prenotaCorsaMediaAutista", method = RequestMethod.POST)
	public void prenotaCorsaMediaAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse prenotaCorsaMediaAutista");
		try{
			String idcorsaMedia = request.getParameter("idcorsaMedia");
			RichiestaMediaAutista richAutistMedio = richiestaAutistaMedioManager.get(Long.parseLong(idcorsaMedia));
			JSONObject mainCorse = new JSONObject();
			List<RichiestaMediaAutista> richAutistMedioPrenotata = richiestaAutistaMedioManager
					.getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata( richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId() );
			if(richAutistMedioPrenotata == null || richAutistMedioPrenotata.size() == 0){
				richAutistMedio.setChiamataPrenotata(true);
				richAutistMedio.setDataChiamataPrenotata(new Date());
				richAutistMedio.setCorsaConfermata(true);
				richAutistMedio.setDataCorsaConfermata(new Date());
				richAutistMedio = richiestaAutistaMedioManager.saveRichiestaAutistaMedio( richAutistMedio );
				HomeUtil_Sms_Email.Invia_Sms_Email_CorsaConfermataMedio(richAutistMedio, request, velocityEngine);
				HomeUtil_Sms_Email.Invia_Email_AvvisoCorsaConfermataCliente(richAutistMedio, request, velocityEngine);
				mainCorse.put("esito", "corsaConfermata");
				mainCorse.put("prezzoAutista", richAutistMedio.getPrezzoTotaleAutista().toString());
				saveMessage(request, "Corsa Confermata"/*getText("user.saved", locale )*/ );
			}else{
				richAutistMedio.setChiamataPrenotata(true);
				richAutistMedio.setDataChiamataPrenotata(new Date());
				richAutistMedio = richiestaAutistaMedioManager.saveRichiestaAutistaMedio( richAutistMedio );
				final Integer ORE = (int) (long) gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
				mainCorse.put("esito", "corsaPrenotata");
				mainCorse.put("numMaxOreDisdettaCorsa", ORE);
				saveMessage(request, "Corsa Prenotata"/*getText("user.saved", locale )*/);
			}
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/disdiciCorsaMediaAutista", method = RequestMethod.POST)
	public void disdiciCorsaMediaAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse disdiciCorsaMediaAutista");
		try{
			String idcorsaMedia = request.getParameter("idcorsaMedia");
			RichiestaMediaAutista richAutistMedio = richiestaAutistaMedioManager.get(Long.parseLong(idcorsaMedia));
			boolean esito = false;
			if( ControlloDateRicerca.ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(richAutistMedio.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate()) ){
				richAutistMedio.setChiamataPrenotata(false);
				richAutistMedio.setDataChiamataPrenotata(null);
				richAutistMedio.setCorsaConfermata(false);
				richAutistMedio.setDataCorsaConfermata(null);
				richAutistMedio.setInvioSmsCorsaConfermata(false);
				richAutistMedio = richiestaAutistaMedioManager.saveRichiestaAutistaMedio( richAutistMedio );
				saveMessage(request, "Corsa disdetta"/*getText("user.saved", locale )*/ );
				/**
				 * confermo la corsa al successivo (in ordine di tempo) autista che ha prenotato la corsa e gli invio l'sms di conferma corsa
				 */
				List<RichiestaMediaAutista> richAutistMedioPrenotata = richiestaAutistaMedioManager
						.getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata( richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId() );
				if(richAutistMedioPrenotata != null && richAutistMedioPrenotata.size() > 0){
					RichiestaMediaAutista richAutistMedioNew = richAutistMedioPrenotata.get(0);
					richAutistMedioNew.setCorsaConfermata(true);
					richAutistMedioNew.setDataCorsaConfermata(new Date());
					richAutistMedioNew = richiestaAutistaMedioManager.saveRichiestaAutistaMedio( richAutistMedioNew );
					HomeUtil_Sms_Email.Invia_Sms_Email_CorsaConfermataMedio(richAutistMedioNew, request, velocityEngine);
				}
				esito = true;
			}else{
				esito = false;
			}
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", esito);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/controlloDisdettaCorsaCliente", method = RequestMethod.POST)
	public void controlloDisdettaCorsaCliente(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse controlloDisdettaCorsaCliente");
		try{
			String idcorsa = request.getParameter("idcorsa");
			RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idcorsa) );
			boolean esito = ControlloDateRicerca.ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(ricTransfert.getDataOraPrelevamentoDate());
			final Integer ORE = (int)(long)gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", esito);
			mainCorse.put("numMaxOreDisdettaCorsa", ORE);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/controlloDisdettaCorsaMediaAutista", method = RequestMethod.POST)
	public void controlloDisdettaCorsaMediaAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse controlloDisdettaCorsaMediaAutista");
		try{
			String idcorsaMedia = request.getParameter("idcorsaMedia");
			RicercaTransfert ricTransfert = richiestaAutistaMedioManager.get( Long.parseLong(idcorsaMedia) ).getRichiestaMedia().getRicercaTransfert();
			boolean esito = ControlloDateRicerca.ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(ricTransfert.getDataOraPrelevamentoDate());
			final Integer ORE = (int)(long)gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", esito);
			mainCorse.put("numMaxOreDisdettaCorsa", ORE);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/inserisciRitardoCorsaAdmin", method = RequestMethod.POST)
	public void inserisciRitardoCorsaAdmin(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse inserisciRitardoCorsaAdmin");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				String idRicTransfert = request.getParameter("idRicTransfert");
				String ritardoAndataRitorno = request.getParameter("ritardoAndataRitorno");
				String numeroMezzore = request.getParameter("numeroMezzore");
				RitardiUtil.GestioneRitardo(Long.parseLong(idRicTransfert), ritardoAndataRitorno, Integer.parseInt(numeroMezzore));
			}
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", true);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/scriviNoteCorseAdmin", method = RequestMethod.POST)
	public void scriviNoteCorseAdmin(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse scriviNoteCorseAdmin");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				String idRicTransfert = request.getParameter("idRicTransfert");
				String noteCorsa = request.getParameter("noteCorsa");
				RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				ricTransf.setNote(noteCorsa);
				ricercaTransfertManager.saveRicercaTransfert(ricTransf);
			}
			JSONObject mainCorse = new JSONObject();
			mainCorse.put("esito", true);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(mainCorse.toString());
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/assegnaDisdiciCorsaAutistaAdmin", method = RequestMethod.POST)
	public void assegnaDisdiciCorsaAutistaAdmin(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse assegnaDisdiciCorsaAutistaAdmin");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				String idRicTransfert = request.getParameter("idRicTransfert");
				String idAutista = request.getParameter("idAutista");
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				//ricTransfert.setApprovazioneAndata(Constants.IN_APPROVAZIONE);
				//ricTransfert.setApprovazioneRitorno(Constants.IN_APPROVAZIONE);
				//ricercaTransfertManager.saveRicercaTransfert(ricTransfert);
				ricercaTransfertManager.updateApprovazioneCorsaAndataAdmin(Long.parseLong(idRicTransfert), Constants.IN_APPROVAZIONE);
				if(ricTransfert.isRitorno()){
					ricercaTransfertManager.updateApprovazioneCorsaRitornoAdmin(Long.parseLong(idRicTransfert), Constants.IN_APPROVAZIONE);
				}
				RichiestaMediaAutista richAutistMedio = richiestaAutistaMedioManager.getRichiestaAutista_by_IdRicerca_and_IdAutista(Long.parseLong(idRicTransfert), Long.parseLong(idAutista));
				if(richAutistMedio.isChiamataPrenotata()){
					richAutistMedio.setChiamataPrenotata(false);
					richAutistMedio.setDataChiamataPrenotata(null);
					richAutistMedio.setCorsaConfermata(false);
					richAutistMedio.setDataCorsaConfermata(null);
					richAutistMedio.setInvioSmsCorsaConfermata(false);
					richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richAutistMedio);
				}else{
					List<RichiestaMediaAutista> richAutistMedioList = richiestaAutistaMedioManager.getRichiestaAutista_by_IdRicerca(Long.parseLong(idRicTransfert));
					for(RichiestaMediaAutista richAutistMedioList_ite: richAutistMedioList){
						if(richAutistMedioList_ite.getAutista().getId() != Long.parseLong(idAutista)){
							richAutistMedioList_ite.setChiamataPrenotata(false);
							richAutistMedioList_ite.setDataChiamataPrenotata(null);
							richAutistMedioList_ite.setCorsaConfermata(false);
							richAutistMedioList_ite.setDataCorsaConfermata(null);
							richAutistMedioList_ite.setInvioSmsCorsaConfermata(false);
							richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richAutistMedioList_ite);
						}else{
							richAutistMedioList_ite.setChiamataPrenotata(true);
							richAutistMedioList_ite.setDataChiamataPrenotata(new Date());
							richAutistMedioList_ite.setCorsaConfermata(true);
							richAutistMedioList_ite.setDataCorsaConfermata(new Date());
							richAutistMedioList_ite.setInvioSmsCorsaConfermata(true);
							// INVIO SMS E EMAIL ALL'AUTISTA DI CONFERMA CORSA
							HomeUtil_Sms_Email.Invia_Sms_Email_CorsaConfermataMedio(richAutistMedioList_ite, request, velocityEngine);
							richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richAutistMedioList_ite);
						}
					}
				}
			}
    	    //response.setContentType("application/json");
    	    response.setContentType("text");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write("ok");
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/inviaSMSCorsaDisponibileAutista", method = RequestMethod.POST)
	public void inviaSMSCorsaDisponibileAutista(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse inviaSMSCorsaDisponibileAutista");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				String idRicTransfert = request.getParameter("idRicTransfert");
				Long idAutista = Long.parseLong(request.getParameter("idAutista"));
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				HomeUtil_Sms_Email.Invia_Sms_Email_CorsaDisponibileAutistiMedioAdmin(ricTransfert, idAutista, request, velocityEngine);
			}
    	    //response.setContentType("application/json");
    	    response.setContentType("text");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write("ok");
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/impostaCollapsePanelCorsaAdmin", method = RequestMethod.POST)
	public void impostaCollapsePanelCorsaAdmin(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse impostaCollapsePanelCorsaAdmin");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				String idRicTransfert = request.getParameter("idRicTransfert");
				RicercaTransfert ricTransfert = ricercaTransfertManager.getCollapsePanelCorsaAdmin( Long.parseLong(idRicTransfert) );
				if(ricTransfert.isCollapsePanelCorseAdmin()){
					//ricTransfert.setCollapsePanelCorseAdmin(false);
					ricercaTransfertManager.updateCollapsePanelCorsaAdmin(Long.parseLong(idRicTransfert), false);
				}else{
					//ricTransfert.setCollapsePanelCorseAdmin(true);
					ricercaTransfertManager.updateCollapsePanelCorsaAdmin(Long.parseLong(idRicTransfert), true);
				}
			}
    	    //response.setContentType("application/json");
    	    response.setContentType("text");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write("ok");
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/impostaApprovazioneCorsaAdmin", method = RequestMethod.POST)
	public void impostaApprovazioneCorsaAdmin(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse impostaApprovazioneCorsaAdmin");
		try{
			if(request.isUserInRole(Constants.ADMIN_ROLE)) {
				String idRicTransfert = request.getParameter("idRicTransfert");
				String andataRitorno = request.getParameter("andataRitorno");
				String numApprov = request.getParameter("numApprov");
				String idAutista = request.getParameter("idAutista");
				if(andataRitorno.equals("A")) {
					ricercaTransfertManager.updateApprovazioneCorsaAndataAdmin(Long.parseLong(idRicTransfert), Integer.parseInt(numApprov));
				}
				if(andataRitorno.equals("R")) {
					ricercaTransfertManager.updateApprovazioneCorsaRitornoAdmin(Long.parseLong(idRicTransfert), Integer.parseInt(numApprov));
				}
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( Long.parseLong(idRicTransfert) );
				if( (ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ||  ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE))
						&& idAutista != null && !idAutista.equals("") && !idAutista.equals("0")) {
					int numCorseApprovate = autistaManager.getCalcolaNumeroCorseApprovate(Long.parseLong(idAutista));
					autistaManager.updateNumeroCorseEseguite(Long.parseLong(idAutista), numCorseApprovate);
				}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) && ricTransfert.getRichiestaAutistaMultiplo_Id() != null ){
					List<RichiestaAutistaParticolare> listRichiestaAutistaParticolareList = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
					for(RichiestaAutistaParticolare ite: listRichiestaAutistaParticolareList) {
						int numCorseApprovate = autistaManager.getCalcolaNumeroCorseApprovate(ite.getAutoveicolo().getAutista().getId());
						autistaManager.updateNumeroCorseEseguite(ite.getAutoveicolo().getAutista().getId(), numCorseApprovate);
					}
				}
			}
    	    //response.setContentType("application/json");
    	    response.setContentType("text");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write("ok");
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/caricaGestioneCorseAdmin", method = RequestMethod.POST)
	public void caricaGestioneCorseAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//log.debug("ChiamateAjaxController_GestioneCorse caricaGestioneCorseAdmin");
		try{
			String dateFrom = request.getParameter("dateFrom");
			String dateTo = request.getParameter("dateTo");
			String inApprovStr = request.getParameter("inApprov");
			String approvStr = request.getParameter("approv");
			String nonApprovStr = request.getParameter("nonApprov");
			String codCorsa = request.getParameter("codCorsa");
			
			final Locale locale = request.getLocale();
			Date fromDate = null; Date toDate = null;
			if(dateFrom != null && !dateFrom.equals("")){
				fromDate = new Date( Long.parseLong(dateFrom) );
			}
			if(dateTo != null && !dateTo.equals("")){
				toDate = new Date( Long.parseLong(dateTo) );
				// aggiungo un giorno per includere le corse dell'ultimo giorno selezionato
				Calendar c = Calendar.getInstance(); 
				c.setTime(toDate); 
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}
			Boolean inApprov = Boolean.valueOf( request.getParameter("inApprov") );
			Boolean approv = Boolean.valueOf( request.getParameter("approv") );
			Boolean nonApprov = Boolean.valueOf( request.getParameter("nonApprov") );
			
			Long idRic = null;
			try{
				idRic = Long.parseLong(request.getParameter("codCorsa"));
			}catch(NumberFormatException nfe){
				idRic = null;
			}
			PanelMainCorseAdmin aa = new PanelMainCorseAdmin(inApprov, approv, nonApprov, fromDate, toDate, idRic, locale, request);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(aa.getMainCorse().toString());
		}catch(Exception ee){
			ee.printStackTrace();
			response.setContentType("text/html");
		    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
		    response.sendRedirect("/home-user");
		}
	}
	

	/**
	 * invio corsa marketing email
	 */
	@RequestMapping(value = "/inviaEmailCorsaCliente", method = RequestMethod.POST)
	public void inviaEmailCorsaCliente(HttpServletRequest request, HttpServletResponse response, final HttpSession session) {
		log.debug("ChiamateAjaxController_GestioneCorse inviaEmailCorsaCliente");
		try{
			String Email = request.getParameter("email");
			String idRichTransfert = request.getParameter("idRichTransfert");
			String result = "";
			EmailValidator validator = EmailValidator.getInstance();
			if(validator.isValid(Email)){
				if(HomeUtil.ContaNumeroChiamateSessione(session) < 10 || request.isUserInRole(Constants.ADMIN_ROLE)){ 
					RicercaTransfert ricercaTransfert = ricercaTransfertManager.get( Long.parseLong( idRichTransfert ) );
					InviaEmail.InviaEmailConsigliaCorsa(Email, ricercaTransfert, request, velocityEngine);
					result = "success";
				}else{
					result = "email-esaurite";
				}
			}else{
				result = "email-invalida";
			}
			Map<String, Serializable> valueJson = new HashMap<String, Serializable>(); 
    		valueJson.put("EsitoInvio", result );
    		String stringJson = new Gson().toJson( valueJson );
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson);
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	/**
	 * invio corsa marketing sms
	 */
	@RequestMapping(value = "/inviaSmsCorsaCliente", method = RequestMethod.POST)
	public void inviaSmsCorsaCliente(HttpServletRequest request, HttpServletResponse response, final HttpSession session) {
		log.debug("ChiamateAjaxController_GestioneCorse inviaSmsCorsaCliente");
		try{
			String numberTelCustomer = request.getParameter("numberTelCustomer");
			String idRichTransfert = request.getParameter("idRichTransfert");
			String result = "";
			if(HomeUtil.ContaNumeroChiamateSessione(session) < 5 || request.isUserInRole(Constants.ADMIN_ROLE)){
				RicercaTransfert ric = ricercaTransfertManager.get( Long.parseLong( idRichTransfert ) );
				List<ResultMedio> richMedi = HomeUtil.getResultAutistaTariffePrezzo(ric.getId()).getResultMedio();
				String prezziCategorie = "";
				for(ResultMedio ite: richMedi){
					if(ite.isShowClasseAutoveicolo()){
						prezziCategorie = prezziCategorie + ite.getClasseAutoveicolo().getDescription() +" "+ite.getPrezzoTotaleCliente()+"€ ";
					}
				}
				String testoSms = "Transfer da "+ric.getFormattedAddress_Partenza().toUpperCase() +" a "+ric.getFormattedAddress_Arrivo().toUpperCase()+" "
						+((ric.isRitorno()) ? "(ANDATA e RITORNO)" : "(SOLO ANDATA)") + ", Passegg: "+ric.getNumeroPasseggeri()+", "
						+prezziCategorie +" Fai click qui: "+ request.getServerName() + request.getContextPath() + "/?courseId="+ric.getId(); 
				result = InvioSms.Crea_SMS_Gateway(numberTelCustomer, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
			}
			Map<String, Serializable> valueJson = new HashMap<String, Serializable>(); 
    		valueJson.put("EsitoInvio", result );
    		String stringJson = new Gson().toJson( valueJson );
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson);
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
    @RequestMapping(value = "/inviaSMSCustomer", method = RequestMethod.POST)
	public void inviaSMSCustomer(HttpServletRequest request, HttpServletResponse response, final HttpSession session) {
    	log.debug("ChiamateAjaxController_GestioneCorse inviaSMSCustomer");
		try{
			String numberTelCustomer = request.getParameter("numberTelCustomer");
			String idRichTransfert = request.getParameter("idRichTransfert");
			String result = "";
			String erroreDate = "";
			String token = UtilBukowski.getRandomToken_Numeri( 4 );
			String []destinatario = new String[]{ numberTelCustomer , "" };
	        String testoSms = "codice: "+token;
			if(HomeUtil.ContaNumeroChiamateSessione(session) < 10 || request.isUserInRole(Constants.ADMIN_ROLE)){
				RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong( idRichTransfert ) );
				if( ControlloDateRicerca.ControlloDataRicercercaSuperioreDi24OreDaAdesso(ricTransf.getDataRicerca()) ){
		        	result = InvioSms.Invio_SMS_Skebby(request, destinatario, testoSms,
		        			(ApplicationUtils.CheckAmbienteVenditore(request.getServletContext())) ? Constants.SMS_TITLE_NCCTRANSFERONLINE : Constants.SMS_TITLE_APOLLOTRANSFERT);
			        if(result.equals(Constants.SMS_STATUS_SUCCESS)){
			        	ricTransf.setVerificatoCustomer( false );
			        	ricTransf.setPhoneNumberCustomer( numberTelCustomer );
			        	ricTransf.setTokenCustomer( token );
			        	ricTransf.setInvioSmsCustomer( true );
			        	ricercaTransfertManager.saveRicercaTransfert(ricTransf);
			        }
				}else{
					erroreDate = "OK";
				}
			}
			Map<String, Serializable> valueJson = new HashMap<String, Serializable>(); 
    		valueJson.put("esitoInvioSms", result );
    		valueJson.put("erroreDate", erroreDate);
    		String stringJson = new Gson().toJson( valueJson );
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson);
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
    
	

	@RequestMapping(value = "/impostaDisponibilita", method = RequestMethod.POST)
    public @ResponseBody String impostaDisponibilita(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController_GestioneCorse impostaDisponibilita");
    	final Locale locale = request.getLocale();
    	try {
    		String date = request.getParameter("date");
    		String idAutoStr = request.getParameter("idAuto");
    		if(date != null && !date.equals("") && idAutoStr != null){
    			long idAuto = Long.parseLong(idAutoStr);
    			Autoveicolo autoveicolo = autoveicoloManager.get( idAuto );
    			Disponibilita disponibilita = disponibilitaManager.getDisponibilitaByAutoveicolo(autoveicolo.getId());
    			if(disponibilita == null){
    				disponibilita = new Disponibilita();
    				disponibilita.setAutoveicolo(autoveicolo);
    				disponibilita = disponibilitaManager.saveDisponibilita(disponibilita);
    				String[] parts = date.split(",");
    				Date data = DateUtil.FormatoData_2.parse(parts[0]);
    				DisponibilitaDate dispDate = new DisponibilitaDate();
    				dispDate.setDisponibilita(disponibilita);
    				dispDate.setData(data);
    				disponibilitaDateManager.saveDisponibilitaDate(dispDate);
    			}
    			disponibilita = disponibilitaManager.getDisponibilitaByAutoveicolo(autoveicolo.getId());
				for (DisponibilitaDate lisDispDateITE : disponibilita.getDisponibilitaDate()) {
					if ( !ListDateNuoveLong(date).contains( lisDispDateITE.getData().getTime() ) ){
						disponibilitaDateManager.removeDisponibilitaDate( lisDispDateITE.getId() );
						//ELIMINO
					}
				}
				Set<DisponibilitaDate> dateDispDate = disponibilita.getDisponibilitaDate();
				String[] parts = date.split(",");
				for( String dataString : parts ) {
					Date data = DateUtil.FormatoData_2.parse(dataString);
					if(!ListDateEsistentiLong(dateDispDate).contains( data.getTime() )){
						DisponibilitaDate disDateNew = new DisponibilitaDate();
						disDateNew.setData(data);
						disDateNew.setDisponibilita(disponibilita);
						disponibilitaDateManager.saveDisponibilitaDate(disDateNew);
						// AGGIUGO
					}
				}
    		}else if(date.equals("") && idAutoStr != null){
    			long idAuto = Long.parseLong(idAutoStr);
    			Autoveicolo autoveicolo = autoveicoloManager.get( idAuto );
    			Disponibilita disponibilita = disponibilitaManager.getDisponibilitaByAutoveicolo(autoveicolo.getId());
    			disponibilitaManager.removeDisponibilita(disponibilita.getId());
    		}
    		return "ok";
    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return "ok";
        }catch(Exception e) {
            e.printStackTrace();
            return "ok";
        }
    }
    		
	private List<Long> ListDateNuoveLong(String date) throws ParseException{
		String[] parts = date.split(",");
		List<Long> dateLong = new ArrayList<Long>();
		for( String dataString : parts ) {
			Date data = DateUtil.FormatoData_2.parse(dataString);
			dateLong.add( data.getTime() );
		}
		return dateLong;
	}
	
	private List<Long> ListDateEsistentiLong(Set<DisponibilitaDate> dateDispDate){
		List<Long> dateEsistentiLong = new ArrayList<Long>();
		for( DisponibilitaDate dateDispDateITE : dateDispDate ) {
			dateEsistentiLong.add( dateDispDateITE.getData().getTime() );
		}
		return dateEsistentiLong;
	}

	


	
}
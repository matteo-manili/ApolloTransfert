package com.apollon.webapp.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
//import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apollon.Constants;
import com.apollon.model.Aeroporti;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Comuni;
import com.apollon.model.MarcaAutoScout;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.model.User;
import com.apollon.model.Visitatori;
import com.apollon.service.AeroportiManager;
import com.apollon.service.AgenzieViaggioBitManager;
import com.apollon.service.ComuniManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.MarcaAutoScoutManager;
import com.apollon.service.ModelloAutoScoutManager;
import com.apollon.service.MuseiManager;
import com.apollon.service.PortiNavaliManager;
import com.apollon.service.ProvinceManager;
import com.apollon.service.RegioniManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RoleManager;
import com.apollon.util.CatturaEmailMarketing_UTIL;
import com.apollon.util.customexception.UserExistsException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.VerifyRecaptcha;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.AutistaTerritorio.AutistiProvincia;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ChiamateAjaxController_GestioneUser extends BaseFormController {
	
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
	
	private AeroportiManager aeroportiManager;
    @Autowired
    public void setAeroportiManager(final AeroportiManager aeroportiManager) {
        this.aeroportiManager = aeroportiManager;
    }
    
    private PortiNavaliManager portiNavaliManager;
    @Autowired
    public void setPortiNavaliManager(final PortiNavaliManager portiNavaliManager) {
        this.portiNavaliManager = portiNavaliManager;
    }
    
    private MuseiManager museiManager;
    @Autowired
    public void setMuseiManager(final MuseiManager museiManager) {
        this.museiManager = museiManager;
    }
    
    private RegioniManager regioniManager;
    @Autowired
    public void setRegioniManager(final RegioniManager regioniManager) {
        this.regioniManager = regioniManager;
    }
    
    private ProvinceManager provinceManager;
    @Autowired
    public void setProvinceManager(final ProvinceManager provinceManager) {
        this.provinceManager = provinceManager;
    }
    
    private ComuniManager comuniManager;
    @Autowired
    public void setComuniManager(final ComuniManager comuniManager) {
        this.comuniManager = comuniManager;
    }
    
    private MarcaAutoScoutManager marcaAutoScoutManager;
    @Autowired
    public void setMarcaAutoScoutManager(final MarcaAutoScoutManager marcaAutoScoutManager) {
        this.marcaAutoScoutManager = marcaAutoScoutManager;
    }
    
    private ModelloAutoScoutManager modelloAutoScoutManager;
    @Autowired
    public void setModelloAutoScoutManager(final ModelloAutoScoutManager modelloAutoScoutManager) {
        this.modelloAutoScoutManager = modelloAutoScoutManager;
    }
    
    private RoleManager roleManager;
    @Autowired
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    private AgenzieViaggioBitManager agenzieViaggioBitManager;
    @Autowired
    public void setAgenzieViaggioBitManager(final AgenzieViaggioBitManager agenzieViaggioBitManager) {
        this.agenzieViaggioBitManager = agenzieViaggioBitManager;
    }
    
    
    @RequestMapping(value = "/submit-news-letter", method = RequestMethod.POST)
    public void SubmitNewsLetter(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("------SUBMIT NEWSLETTER------");
    	JSONObject json = new JSONObject(); final String Message = "message";
	    String emailNewsLetter = request.getParameter("emailNewsLetter");
		String gRecaptchaResponse = request.getParameter("captcha");
		try {
 			if( !VerifyRecaptcha.Verify(gRecaptchaResponse) ){
 				System.out.println("Recaptcha Errata");
 				json.put(Message, "Recaptcha Errata");
 			}else{
 				EmailValidator validator = EmailValidator.getInstance();
 				if( validator.isValid(emailNewsLetter) ){
 					AgenzieViaggioBit hotel = new AgenzieViaggioBit();
 					hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
 					hotel.setSitoWebScraping(null);
 					hotel.setEmail( emailNewsLetter );
 					hotel.setNome( null );
 					hotel.setCitta_e_indirizzo( "(RM)" ); // TODO per il momento
 					hotel.setSitoWeb( "NEWSLETTER" );
 					agenzieViaggioBitManager.saveAgenzieViaggioBit(hotel);
 					json.put(Message, "Iscrizione avvenuta Correttamente: "+emailNewsLetter);
 				}else{
 					json.put(Message, "Indirizzo Email non Valido: "+emailNewsLetter); 
 				}
 			}
		}catch (final DataIntegrityViolationException dive) {
	    		json.put(Message, "Email già registrata: "+emailNewsLetter);
    	}catch(Exception e){
    		json.put(Message, e.getMessage());
    	} finally {
    		response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(json.toString());
	    }
	}
    
    @RequestMapping(value = "/check-codice-venditore", method = RequestMethod.POST)
    public void CeckCodiceVenditore(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JSONObject json = new JSONObject();
    	try {
    	    System.out.println("------VENDITORE----"); 
    	    System.out.println( "CodiceVenditore: "+ request.getParameter("CodiceVenditore") );
    	    System.out.println( "idVenditoreCorsa: "+ request.getParameter("idVenditoreCorsa") );
    	    String CodiceVenditore = request.getParameter("codiceVenditore");
    	    String IdVenditoreCorsa = request.getParameter("idVenditoreCorsa");
    	    if(request.getParameter("disconnettiVenditore") != null && request.getParameter("disconnettiVenditore").equals("true")  ){
    	    	request.getSession().setAttribute(Constants.VENDITORE_ATTRIBUTE_NAME, null);
    	    }else{
	    		if( ApplicationUtils.CheckAmbienteVenditore(getServletContext()) 
	    				&& (!CodiceVenditore.equals("") || !IdVenditoreCorsa.equals("")) ){
	    			Long idUserVenditore = (!CodiceVenditore.equals("")) ? getUserManager().getUserIdVenditore_by_CodiceVenditore(CodiceVenditore) :
	    				Long.parseLong( IdVenditoreCorsa );

	    			TimeUnit.SECONDS.sleep(2); //metto questa pausa per prevenire attacchi esterni
	        		if( idUserVenditore != null && getUserManager().get(idUserVenditore).getRoles().contains(roleManager.getRole(Constants.VENDITORE_ROLE)) ){
	        			request.getSession().setAttribute(Constants.VENDITORE_ATTRIBUTE_NAME, idUserVenditore);
	        			json.put("esito", true); 
	        		}else{
	        			json.put("esito", false); 
	            		json.put("message", "Codice Venditore non Riconosciuto");
	            		json.put("class", "text-danger"); 
	        		}
	    		}else{
	    			json.put("esito", false); 
	        		json.put("message", "Inserisci Codice Venditore");
	        		json.put("class", "text-primary"); 
	    		}
    	    }
    	}catch(Exception e){
    		json.put("esito", false);
    		json.put("message", e.getMessage());
    	    response.getWriter().write(json.toString());
    	} finally {
    		response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(json.toString());
    	}
	}
    
    
    
    /**
     * Questo metodo è deprecrato, funziona solo quando ho gli utente con le email fake nomeUtente@apollotransfert.com
     * @throws IOException 
     * 
     */
    @Deprecated
    @RequestMapping(value = "/SalvaEmailClienteRiepilogo", method = RequestMethod.POST)
	public void SalvaEmailClienteRiepilogo(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	log.debug("ChiamateAjaxController SalvaEmailClienteRiepilogo");
		JSONObject esito = new JSONObject();
		try{
			String emailCustomer = request.getParameter("emailCustomer");
			EmailValidator validator = EmailValidator.getInstance();
			if(validator.isValid(emailCustomer)) {
				User user = getUserManager().getUserByUsername(request.getRemoteUser());
				user.setEmail( emailCustomer );
				getUserManager().saveUser(user);
				esito.put("esitoSalvaEmail", true);
				esito.put("esitoSalvaEmail-mesage", getText("user.saved", request.getLocale()));
			}else{
				esito.put("esitoSalvaEmail", false);
	            esito.put("esitoSalvaEmail-mesage", getText("errors.format.email", new Object[] { emailCustomer }, request.getLocale()));
			}
		}catch(final UserExistsException e) {
			esito.put("esitoSalvaEmail", false);
			esito.put("esitoSalvaEmail-mesage", getText("errors.existing.email.user", request.getLocale()));
	    }catch (final Exception e) {
	    	esito.put("esitoSalvaEmail", false);
	    	esito.put("esitoSalvaEmail-mesage", e.getMessage());
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write( esito.toString() );
		}
	}

    

    
    @RequestMapping(value = "/memorizzaIpUtente", method = RequestMethod.POST)
	public void memorizzaIpUtente(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	log.debug("ChiamateAjaxController memorizzaIpUtente");
    	JSONObject ipAddress = new JSONObject();
		try{
			Visitatori visitatore = InfoUserConnectAddressMain.SalvaVisitatore(request);
			if(visitatore != null){
				ipAddress.put("ipAddress", visitatore.getIpAddress());
				ipAddress.put("idVisitatore", visitatore.getId());
			}else{
				ipAddress.put("ipAddress", 0);
				ipAddress.put("idVisitatore", "");
			}
		}catch(ConnectException cE){
			System.out.println(cE.getMessage());
		}catch(Exception ee){
			System.out.println(ee.getMessage());
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(ipAddress.toString());
		}
	}
    
    @RequestMapping(value = "/CollaboratoriInfoCorsaMedia", method = RequestMethod.POST)
	public void CollaboratoriInfoCorsaMedia(HttpServletResponse response) throws IOException {
    	log.debug("ChiamateAjaxController CollaboratoriInfoCorsaMedia");
    	JSONObject info = new JSONObject();
		try{
			info.put("numAutistiCorsaMedia", gestioneApplicazioneManager.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber().toString());
			info.put("margineOreMinimoCorsaMedia", gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber().toString());
			info.put("percentualeServizio", provinceManager.getPercentualeServizioMediaProvincia());
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write( info.toString() );
		}
	}
    
    @RequestMapping(value = "/CollaboratoriProvinciaAutistaList", method = RequestMethod.POST)
	public void CollaboratoriProvinciaAutistaList(HttpServletResponse response) throws IOException {
    	log.debug("ChiamateAjaxController CollaboratoriProvinciaAutistaList");
    	AutistaTerritorio aaa = TerritorioUtil.ProvinciaAutistaList();
    	String json = new Gson(). toJson( aaa );
		try{
			List<AutistiProvincia> provAutistilist = new ArrayList<AutistiProvincia>();
			provAutistilist = aaa.getAutistiProvincia();
			int totaleConferma = 0;
			for(AutistiProvincia provAutistilist_ite: provAutistilist){
				if(provAutistilist_ite.getAutisti().size() >= 1){
					provAutistilist_ite.getNomeProvincia();
					totaleConferma = totaleConferma + provAutistilist_ite.getAutisti().size();
				}
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write( json );
		}
	}
    
    @RequestMapping(value = "/CollaboratoriRegioneAutistaList", method = RequestMethod.POST)
	public void CollaboratoriRegioneAutistaList(HttpServletResponse response) throws IOException {
    	log.debug("ChiamateAjaxController CollaboratoriRegioneAutistaList");
    	String json = "";
		try{
			json = new Gson().toJson( TerritorioUtil.RegioneAutistaList() );
		}catch(Exception ee){
			ee.printStackTrace();
		}finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write( json );
		}
	}
    
	@RequestMapping(value = "/localizzaPosizione", method = RequestMethod.POST) 
	public @ResponseBody String localizzaPosizione(HttpServletRequest request, HttpServletResponse response) {
		log.debug("ChiamateAjaxController localizzaPosizione");
	    try {
	    	String mylat = request.getParameter("mylat");
			String mylong = request.getParameter("mylong");
			// così funziona ma meglio il metodo tradizionale con il @ResponseBody
			//response.setContentType("text");
		    //response.setCharacterEncoding(Constants.ENCODING_UTF_8);
			//response.getWriter().write(mylat);

			RicercaTransfert_GoogleMaps_Info RicTrans_info = new RicercaTransfert_GoogleMaps_Info();
			RicTrans_info.setLat( Double.parseDouble(mylat) );
			RicTrans_info.setLng( Double.parseDouble(mylong) );
			GMaps_Api GMaps_Api = new GMaps_Api();
			RicercaTransfert_GoogleMaps_Info RicTrans = GMaps_Api.GoogleMaps_Geocode_LatLng(RicTrans_info);
			return RicTrans.getGeolocationHtml5Address();
		} catch (Exception ee) {
			ee.printStackTrace();
			return "";
		}
	}
	
	@RequestMapping(value = "/homeTableAeroporti", method = RequestMethod.POST)
	public void homeTableAeroporti(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController homeTableAeroporti");
		String stringJson2 = "";
		try{
			// il vecchio...
			//RicercaTransfert_GeoPlugin_Connect ricTrans_GeoPlugin = new RicercaTransfert_GeoPlugin_Connect();
			//RicercaTransfert_GeoPlugin_Info resultOld = new RicercaTransfert_GeoPlugin_Info();
			//resultOld = ricTrans_GeoPlugin.dammiINFO_Geo_Plugin( /* request.getRemoteAddr() //(lavorando in localHost non ritorna l'IP)*/ "94.37.246.57" );
    		//Visitatori result = Info_IP_Address_Connect.DammiInfoIpRequest_ip_api_com(request);
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
    		List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		List<Aeroporti> listAeroportiSuggeriti = null;
    		
    		// il vecchio....
    		//if(result != null && result.getStatus().equals("200")){
    		//if(result != null){
    		if(!latitude.equals("") && !longitude.equals("")){
				listAeroportiSuggeriti = ricercaTransfertManager.OrdinaAeroportiBy_Lat_Lng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    		}else{
    			listAeroportiSuggeriti = aeroportiManager.getAeroporti();
    		}
    		int i = 1;
			for(Aeroporti aero_ite: listAeroportiSuggeriti){
				valueJson = new HashMap<String, Serializable>(); 
				valueJson.put("distance", i++ );
	    		valueJson.put("nomeAero", aero_ite.getNomeAeroporto() );
	    		valueJson.put("siglaAero", aero_ite.getSiglaAeroporto());
	    		String indirizzo = (aero_ite.getIndirizzo() != null) ? aero_ite.getIndirizzo() : "";
	    		valueJson.put("indirizzo", indirizzo);
	    		valueJson.put("comune", aero_ite.getComuni().getNomeComune());
	    		valueJson.put("provincia", aero_ite.getComuni().getProvince().getNomeProvincia());
	    		valueJson.put("placeId", aero_ite.getPlaceId());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
    	    
		}catch(Exception ee){
			ee.printStackTrace();
		}finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	
	@RequestMapping(value = "/homeTablePorti", method = RequestMethod.POST)
	public void homeTablePorti(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController homeTablePorti");
		String stringJson2 = "";
		try{
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		List<PortiNavali> listAeroportiSuggeriti = null;
    		if(!latitude.equals("") && !longitude.equals("")){
				listAeroportiSuggeriti = ricercaTransfertManager.OrdinaPortiNavaliBy_Lat_Lng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    		}else{
    			listAeroportiSuggeriti = portiNavaliManager.getPortiNavali();
    		}
    		int i = 1;
			for(PortiNavali porto_ite: listAeroportiSuggeriti){
				valueJson = new HashMap<String, Serializable>();
				valueJson.put("distance", i++ );
	    		valueJson.put("nomePorto", porto_ite.getNomePorto() );
	    		String indirizzo = (porto_ite.getIndirizzo() != null) ? porto_ite.getIndirizzo() : "";
	    		valueJson.put("indirizzo", indirizzo);
	    		valueJson.put("comune", porto_ite.getComuni().getNomeComune());
	    		valueJson.put("provincia", porto_ite.getComuni().getProvince().getNomeProvincia());
	    		valueJson.put("placeId", porto_ite.getPlaceId());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
    	    
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	
	@RequestMapping(value = "/homeTableMusei", method = RequestMethod.POST)
	public void homeTableMusei(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController homeTableMusei");
		String stringJson2 = "";
		try{
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		List<Musei> listAeroportiSuggeriti = null;
    		if(!latitude.equals("") && !longitude.equals("")){
				listAeroportiSuggeriti = ricercaTransfertManager.OrdinaMuseiBy_Lat_Lng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    		}else{
    			listAeroportiSuggeriti = museiManager.getMusei();
    		}
    		int i = 1;
			for(Musei museo_ite: listAeroportiSuggeriti){
				valueJson = new HashMap<String, Serializable>();
				valueJson.put("distance", i++ );
	    		valueJson.put("nomeMuseo", museo_ite.getNomeMuseo() );
	    		String indirizzo = (museo_ite.getIndirizzo() != null) ? museo_ite.getIndirizzo() : "";
	    		valueJson.put("indirizzo", indirizzo);
	    		String desc = (museo_ite.getDescrizione() != null) ? museo_ite.getDescrizione() : "";
	    		valueJson.put("desc", desc);
	    		valueJson.put("comune", museo_ite.getComuni().getNomeComune());
	    		valueJson.put("provincia", museo_ite.getComuni().getProvince().getNomeProvincia());
	    		valueJson.put("placeId", museo_ite.getPlaceId());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	
	@RequestMapping(value = "/autocompleteRegioni", method = RequestMethod.POST)
	public void autocompleteRegioni(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController autocompleteRegioni");
		String stringJson2 = "";
		try{
			String term = request.getParameter("term");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
			Iterator<Regioni> regioni_ite = regioniManager.getNomeRegioneBy_Like(term) .iterator();
			while (regioni_ite.hasNext()) {
				Regioni regione = regioni_ite.next();
				
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", regione.getId() );
	    		valueJson.put("text", regione.getNomeRegione());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}

	@RequestMapping(value = "/autocompleteProvince", method = RequestMethod.POST)
	public void autocompleteProvince(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController autocompleteProvince");
		String stringJson2 = "";
		try{
			String term = request.getParameter("term");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
			Iterator<Province> province_ite = provinceManager.getNomeProvinciaBy_Like(term) .iterator();
			while (province_ite.hasNext()) {
				Province provincia = province_ite.next();
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", provincia.getId() );
	    		valueJson.put("text", provincia.getNomeProvincia());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}

	@RequestMapping(value = "/autocompleteComuni", method = RequestMethod.POST)
	public void autocompleteComuni(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController autocompleteComuni");
		String stringJson2 = "";
		try{
			String term = request.getParameter("term");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		
			Iterator<Comuni> comuni_ite = comuniManager.getNomeComuneByLikeNome_Chosen(term).iterator();
			while (comuni_ite.hasNext()) {
				Comuni comune = comuni_ite.next();
				
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", comune.getId() );
	    		valueJson.put("text", comune.getNomeComune());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	
	
	@RequestMapping(value = "/autocompleteMarcheAutoScout", method = RequestMethod.POST)
	public void autocompleteMarcheAutoScout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController autocompleteMarcheAutoScout");
		String stringJson2 = "";
		try{
			String term = request.getParameter("term");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
			List<MarcaAutoScout> marcaAutoScoutList = marcaAutoScoutManager.getMarcaAutoScoutDescrizione(term);
			for(MarcaAutoScout ite : marcaAutoScoutList){
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", ite.getIdAutoScout() );
	    		valueJson.put("text", ite.getName());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	
	
	@RequestMapping(value = "/autocompleteModelliAutoScout", method = RequestMethod.POST)
	public void autocompleteModelliAutoScout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController autocompleteModelliAutoScout");
		String stringJson2 = "";
		try{
			String term = request.getParameter("term");
			String idMarcaAutoScout = request.getParameter("idMarcaAutoScout");
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
			List<ModelloAutoScout> modelloAutoScoutList = modelloAutoScoutManager.getModelloAutoScoutDescrizione(term, Long.parseLong(idMarcaAutoScout));
			for(ModelloAutoScout ite : modelloAutoScoutList){
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", ite.getId() );
	    		valueJson.put("text",  ite.getMarcaAutoScout().getName() +" "+ ite.getName());
	    		list.add(valueJson);
			}
			stringJson2 = new Gson().toJson( list );
    	    
		}catch(Exception ee){
			ee.printStackTrace();
		} finally {
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(stringJson2);
		}
	}
	

	
	@RequestMapping(value = "/listaZoneChosen", method = RequestMethod.POST, produces = "text/plain;charset="+Constants.ENCODING_UTF_8)
    public void listaZoneChosen(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("ChiamateAjaxController listaZoneChosen");
		String json = "";
    	try {
    		request.setCharacterEncoding(Constants.ENCODING_UTF_8);
    		String term = java.net.URLDecoder.decode(request.getParameter("term"), Constants.ENCODING_UTF_8);
    		//request.setCharacterEncoding(Constants.ENCODING_UTF_8);
    		//String term = request.getParameter("term");
    		/*
    		List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		Iterator<Comuni> comuni_ite = getComuniManager().getNomeComuneByLikeNome_Chosen( term ).iterator();
			while(comuni_ite.hasNext()){
				Comuni comuni = comuni_ite.next();
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", "COM#"+comuni.getId() );
	    		valueJson.put("text", comuni.getNomeComune());
	    		list.add(valueJson);
			}
			*/
    		/*
    		valueJson = new HashMap<String, Serializable>();
    		valueJson.put("value", "2");
    		valueJson.put("text", "ciao porco");
    		list.add(valueJson);
    		valueJson = new HashMap<String, Serializable>();
    		valueJson.put("value", "3");
    		valueJson.put("text", "ciao bello");
    		list.add(valueJson);
    		*/
    		ObjectMapper mapper = new ObjectMapper();
    		json = mapper.writeValueAsString(creaTendinaMista_Reg_Prov_Com(term, term, term));
    	} catch(Exception e) {
            e.printStackTrace();
        } finally {
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write( json );
		}
    }
	

	// METODI SUPPORTO
	private List<Map<String, Serializable>> creaTendinaMista_Reg_Prov_Com(String com, String prov, String reg){
		List<Map<String, Serializable>> list4JSON = new LinkedList<Map<String, Serializable>>();
		Map<String, Serializable> valueJson = new HashMap<String, Serializable>();
		//List<LabelValue> listZoneMiste = new ArrayList<LabelValue>();
		//listZoneMiste.add(new LabelValue("ITALIA (TUTTO IL TERRITORIO)", "ITALIA#"));
		/*
		Iterator<Comuni> comuni_ite = comuniManager.getNomeComuneByLikeNome_Chosen( com ).iterator();
		while(comuni_ite.hasNext()){
			Comuni comuni = comuni_ite.next();
			valueJson = new HashMap<String, Serializable>();
    		valueJson.put("value", "COM#"+comuni.getId());
    		valueJson.put("text", WordUtils.capitalize(comuni.getNomeComune()+" (COMUNE)"));
    		list4JSON.add( valueJson );
		}
		*/
		Iterator<Province> province_ite = provinceManager.getNomeProvinceByLikeNome_Chosen( prov ).iterator();
		while(province_ite.hasNext()){
			Province province = province_ite.next();
			valueJson = new HashMap<String, Serializable>();
    		valueJson.put("value", "PRO#"+province.getId() );
    		valueJson.put("text", WordUtils.capitalize(province.getNomeProvincia())+" (PROVINCIA)");
    		list4JSON.add( valueJson );
		}
		/*
		Iterator<Regioni> regioni_ite = regioniManager.getNomeRegioneByLikeNome_Chosen( reg ).iterator();
		while(regioni_ite.hasNext()){
			Regioni regioni = regioni_ite.next();
			valueJson = new HashMap<String, Serializable>();
    		valueJson.put("value", "REG#"+regioni.getId() );
    		valueJson.put("text", WordUtils.capitalize(regioni.getNomeRegione())+" (REGIONE)");
    		list4JSON.add( valueJson );
			listZoneMiste.add(new LabelValue( WordUtils.capitalize(regioni.getNomeRegione())+" (REGIONE)", "REG#"+regioni.getId()));
		}
		*/
		//listZoneMiste.addAll(comuniAppo1) ;
		
		return list4JSON;
	}

}
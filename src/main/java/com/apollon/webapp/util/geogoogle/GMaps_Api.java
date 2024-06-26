package com.apollon.webapp.util.geogoogle;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.dao.CoordGeoProvinceDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.CoordGeoProvince;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.NumberUtil;
import com.apollon.util.UrlConnection;
import com.apollon.util.UtilBukowski;
import com.apollon.util.customexception.FerrysTraghettiException;
import com.apollon.util.customexception.GoogleMatrixException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * questa classe restitusce informazioni per mezzo delle API di google 
 * 
 * vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
 * vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
 * 
 * vedere: https://developers.google.com/maps/documentation/distance-matrix/intro#Introduction
 * vedere https://developers.google.com/places/web-service/search#TextSearchRequests
 * per autocomplete vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
 * 
 * - - - - - - - - - - - - - -  - - - - - - - - - - -
 * - - - I - M - P - O - R - T - A - N - T - E - - -
 * - - - - - - - - - - - - - -  - - - - - - - - - - -
 * Per fare il ciclo completo di una ricBolognaerca Transfert ABILITARE LE SEGUENTI API in https://console.cloud.google.com
 * - Google Places API Web Service Private API
 * - Google Maps Geocoding API
 * - Google Maps Distance Matrix API
 * - Google Maps Directions API
 * 
 * Per le ricerche autocomplete nella JSP essere abilitate queste due librerie:
 * - Google Places API Web Service Private API
 * - Google Maps JavaScript API (per fare le ricerche sulla JSP con Javascript)
 *
 */
public class GMaps_Api extends ApplicationUtils implements Serializable {
	private static final long serialVersionUID = -4578083622025588686L;
	private static final Log log = LogFactory.getLog(GMaps_Api.class);
	private  ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	private  CoordGeoProvinceDao coordGeoProvinceDao = (CoordGeoProvinceDao) contextDao.getBean("CoordGeoProvinceDao");
	
	public ModelAndView AddAttribute_GoogleApiMap_JS(ModelAndView mav) {
		mav.addObject("GOOGLE_API_MAP_JS", gestioneApplicazioneDao.getName("GOOGLE_API_MAP_JS").getValueString());
		mav.addObject("NAZIONI", Constants.NAZIONI);
		return mav;
	}
	
	/**
	 * RITORNANO LE PROVINCE DI MEZZO E LANCIA UNA ECCEZZIONE SE PASSA ATTRAVERSO UN TRAGHETTO
	 */
	public List<Long> GoogleMaps_Directions(RicercaTransfert ricercaTransfert, String language) throws IOException, ParseException, UnknownHostException, Exception {
		long startTime = System.nanoTime();
		log.debug("GoogleMaps_Directions");
		String TYPE_QUERY_DIRECTIONS = "/directions";
		String OUT_JSON = "/json";
        StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_QUERY_DIRECTIONS + OUT_JSON );
        sb.append("?origin="+ricercaTransfert.getLat_Partenza()+","+ricercaTransfert.getLng_Partenza() );
		sb.append("&destination="+ricercaTransfert.getLat_Arrivo()+","+ricercaTransfert.getLng_Arrivo() );
		sb.append("&mode=driving");
		long oraPartenza = ricercaTransfert.getDataOraPrelevamentoDate().getTime() / 1000;
		sb.append("&departure_time="+oraPartenza);
		sb.append("&language="+language);
		sb.append("&key=" + Dammi_KEY_GOOGLE_API());
        JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
        DammiTempoOperazione.DammiSecondi(startTime, "GoogleMaps_Directions-1");
        //System.out.println(jb);
        String status = (String) jb.get("status");
        if(status.equals("OK")) {
        	try{
        		JSONArray routes = (JSONArray) jb.get("routes");
        		JSONObject route = (JSONObject) routes.get(0); 
        		JSONArray legs = (JSONArray) route.get("legs");
        		JSONObject leg = (JSONObject) legs.get(0); 
		        JSONArray steps = leg.getJSONArray("steps");
		        steps.length();
		        //int risultato = steps.length() / 10;
		        List<String> listProvinceInfra = new ArrayList<String>(); 
		        DammiTempoOperazione.DammiSecondi(startTime, "GoogleMaps_Directions-2");
		        for(int i = 0; i < steps.length(); i++  /*i += risultato*/) {
		        	JSONObject stepJB = (JSONObject) steps.get( i );
		        	JSONObject startLocation = (JSONObject) stepJB.get("start_location");
		        	double lat = startLocation.getDouble("lat");
		        	double lng = startLocation.getDouble("lng");
		        	//----------
		        	int roundCoordinate = 2; CoordGeoProvince coordGeoProvince = 
		        			coordGeoProvinceDao.getCoordGeoProvince_by_LatLng(NumberUtil.round(lat, roundCoordinate), NumberUtil.round(lng, roundCoordinate), roundCoordinate);
		        	if( coordGeoProvince == null ) {
						RicercaTransfert_GoogleMaps_Info googleMapsInfo = GoogleMaps_Geocode_LatLng( new RicercaTransfert_GoogleMaps_Info(lat, lng) );
						if(googleMapsInfo != null && googleMapsInfo.getSiglaProvicia() != null ){
							try{
								Province prov = provinceDao.getProvinciaBy_SiglaProvincia(googleMapsInfo.getSiglaProvicia());
								coordGeoProvince = new CoordGeoProvince(new Date(), lat, lng, prov);
								coordGeoProvinceDao.saveCoordGeoProvince(coordGeoProvince);
								listProvinceInfra.add( googleMapsInfo.getSiglaProvicia() );
						    }catch(DataIntegrityViolationException duplic) {
						    	log.debug( duplic.getMessage() );
						    }
						}
		        	}else {
		        		listProvinceInfra.add( coordGeoProvince.getSiglaProvincia() );
		        	}
					// controllo che ci siano traghetti: "maneuver": "ferry",
					try{
						if( stepJB.get("maneuver").equals("ferry") ){
							throw new FerrysTraghettiException();
						}
					}catch(JSONException ee){ }
		        }
		        DammiTempoOperazione.DammiSecondi(startTime, "GoogleMaps_Directions-3");
		        listProvinceInfra = new ArrayList<String>(new LinkedHashSet<String>(listProvinceInfra));
		        List<Long> listProvinceInfraLong = new ArrayList<Long>();
		        for(String ite: listProvinceInfra) {
		        	if(!ricercaTransfert.getSiglaProvicia_Partenza().equals( ite ) && !ricercaTransfert.getSiglaProvicia_Arrivo().equals( ite )) {
			        	Province prov = provinceDao.getProvinciaBy_SiglaProvincia( ite );
			        	if(prov != null){
			        		listProvinceInfraLong.add( prov.getId() );
			        	}
		        	}
		        }
		        DammiTempoOperazione.DammiSecondi(startTime, "GoogleMaps_Directions-4");
		        return listProvinceInfraLong;
	        }catch(JSONException jsonExc ){
	        	log.debug("JSONException: "+jsonExc.getMessage());
	    	}
        }else if(status.equals("ZERO_RESULTS")){
        	throw new GoogleMatrixException( status );
        }else if(status.equals("REQUEST_DENIED")){
        	throw new GoogleMatrixException( status );
        }else{
        	log.debug("status: "+status);
        }
		return null;
	}
	
	public RicercaTransfert_GoogleMaps_Info GoogleMaps_PlaceTextSearch_Easy(String query, String language) 
			throws JSONException, NullPointerException, UnknownHostException, Exception{
		log.debug("GoogleMaps_PlaceTextSearch_Easy");
        String TYPE_SEARCH = "/place/textsearch";
		String OUT_JSON = "/json";
        StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_SEARCH + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&language="+language);
		sb.append("&query=" + URLEncoder.encode(query, "utf8"));
        JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
        String status = (String) jb.get("status");
        if(status.equals("OK")){
        	try{
		        JSONArray results = (JSONArray) jb.get("results");
		        JSONObject results_0 = (JSONObject)results.get(0); //il primo elemento (dovrebbe essere sempre 1)
		        String name = (String)results_0.get("name");
		        String place_id = (String)results_0.get("place_id");
		        JSONArray jsonObjectArrayTypes = (JSONArray) results_0.get("types");
		        ArrayList<String> listTypes = new ArrayList<String>();     
		        if(jsonObjectArrayTypes != null) { 
		           int len = jsonObjectArrayTypes.length();
		           for (int i=0;i<len;i++){ 
		        	   listTypes.add(jsonObjectArrayTypes.get(i).toString());
		           } 
		        } 
		        JSONObject jsonObject3 = (JSONObject)results_0.get("geometry");
		        JSONObject location = (JSONObject) jsonObject3.get("location");
		        double lat =  Double.parseDouble( location.get("lat").toString() );
		        double lng =  Double.parseDouble( location.get("lng").toString() );
		        RicercaTransfert_GoogleMaps_Info psg = new RicercaTransfert_GoogleMaps_Info();
		        psg.setPlace_id(place_id);
		        psg.setListTypes(listTypes);
		        psg.setName( name );
		        psg.setLat(lat);
		        psg.setLng(lng);
		        return psg;
	        }catch(JSONException jsonExc ){
	        	log.debug("JSONException: "+jsonExc.getMessage());
	    		return null;
	    	}
        }else if(status.equals("ZERO_RESULTS")){
        	log.debug("status: "+status);
        	return null;
        }else{
        	log.debug("status: "+status);
        	return null;
        }
	}
	
	/**
	 * dammiInfoGoogle_by_Query
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	public RicercaTransfert_GoogleMaps_Info GoogleMaps_PlaceTextSearch(String query, String language) 
			throws JSONException, NullPointerException, UnknownHostException, Exception {
		log.debug("GoogleMaps_PlaceTextSearch");
		/*
		 * vedere https://developers.google.com/places/web-service/search#TextSearchRequests
		 * per autocomplete vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
		 */
        String TYPE_SEARCH = "/place/textsearch";
        //String TYPE_AUTOCOMPLETE = "/place/autocomplete";
		String OUT_JSON = "/json";
        StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_SEARCH + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&language="+language);
		// Roma - specificando queste coordinate le ricerche saranno circoscritte da questo punto al raggio del radious
		// ho messo roma perché si trova nel centro dell'italia.
		sb.append("&location=41.90278349999999,12.4963655"); 
		sb.append("&radius=5000");
		sb.append("&query=" + URLEncoder.encode(query, "utf8"));
        JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
        String status = (String) jb.get("status");
        if(status.equals("OK")){
	        JSONArray results = (JSONArray) jb.get("results");
	        JSONObject results_0 = (JSONObject)results.get(0); //il primo elemento (dovrebbe essere sempre 1)
	        String formatted_address = (String)results_0.get("formatted_address");
	        String name = (String)results_0.get("name");
	        String place_id = (String)results_0.get("place_id");
	        RicercaTransfert_GoogleMaps_Info psg = new RicercaTransfert_GoogleMaps_Info();
	        try {
		        JSONArray jsonObjectArrayTypes = (JSONArray) results_0.get("types");
		        ArrayList<String> listTypes = new ArrayList<String>();     
		        if(jsonObjectArrayTypes != null) { 
		           int len = jsonObjectArrayTypes.length();
		           for (int i=0;i<len;i++){ 
		        	   listTypes.add(jsonObjectArrayTypes.get(i).toString());
		           } 
		        } 
		        psg.setListTypes(listTypes);
		        if( listTypes.contains("street_address") ){
		        	psg.setFormattedAddress( formatted_address ); //formatted_address
		        }else{
		        	psg.setFormattedAddress( formatted_address +" "+ "("+name+")" ); //formatted_address
		        }
	    	}catch(JSONException jsonExc ){
	        	log.debug("JSONException: "+jsonExc.getMessage());
	    	}
	        JSONObject jsonObject3 = (JSONObject)results_0.get("geometry");
	        JSONObject location = (JSONObject) jsonObject3.get("location");
	        double lat =  Double.parseDouble( location.get("lat").toString() );
	        double lng =  Double.parseDouble( location.get("lng").toString() );
	        psg.setPlace_id(place_id);
	        psg.setName( name );
	        psg.setLat(lat);
	        psg.setLng(lng);
	        psg.setComune(null);
	        psg.setSiglaProvicia(null);
	        if(place_id != null && !place_id.equals("")){
	        	//psg = dammiINFO_PlaceID_Details_Google( psg, "it" );
	        	if(psg.getComune() == null || psg.getComune().equals("")){
	    			psg = GoogleMaps_Geocode_LatLng(psg);
	        	}
	        }
	        return psg;
        }else if(status.equals("ZERO_RESULTS")){
        	log.debug("status: "+status);
        	return null;
        }else{
        	log.debug("status: "+status);
        	return null;
        }
	}

	/**
	 * dammiInfoGoogle_by_LatLng
	 * vedere: https://developers.google.com/maps/documentation/geocoding/intro
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	public RicercaTransfert_GoogleMaps_Info GoogleMaps_Geocode_LatLng(RicercaTransfert_GoogleMaps_Info psg) 
			throws JSONException, NullPointerException, UnknownHostException, Exception {
		log.debug("GoogleMaps_Geocode_LatLng");
		// http://maps.googleapis.com/maps/api
		// /geocode/json?latlng=44.4647452,7.3553838&sensor=true
		String TYPE_SEARCH = "/geocode";
		String OUT_JSON = "/json";
		StringBuilder sb = new StringBuilder( Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_SEARCH + OUT_JSON);
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&latlng=" + String.valueOf( psg.getLat() ) + "," + String.valueOf( psg.getLng() ));
		sb.append("&language=it"); // potrei anche mettere EN ma è meglio IT. un turista straniero leggendo la via in italiano ha più fiducia.
		sb.append("&region=it");
		//sb.append("&components=country:IT");
		JSONObject jb = new JSONObject(UrlConnection.HttpConnection(sb));
		String status = (String) jb.get("status");
		if(status.equals("OK")) {
			JSONArray results = (JSONArray) jb.get("results");
			/**
			 * il primoelemento(dovrebbeessere sempre 1)
			 */
			JSONObject results_0 = (JSONObject) results.get(0); 
			String formatted_address = (String) results_0.get("formatted_address");
			//String place_id = (String) results_0.get("place_id");
			// psg.setPlace_id( place_id );
			JSONArray address_components = (JSONArray) results_0.get("address_components");
			if( !GoogleMapsTypesAccettato(psg.getListTypes()) ){
				String route = ""; String postal_code = ""; String comune = "";
				for(int i = 0; i < address_components.length(); i++) {
					JSONObject address_components_ite = (JSONObject) address_components.get(i);
					JSONArray types = (JSONArray) address_components_ite.get("types");
					if(types != null) {
						int len = types.length();
						for (int ite = 0; ite < len; ite++) {
							if(types.get(ite).equals("administrative_area_level_3")) {
								comune = (String) address_components_ite.get("long_name");
								psg.setComune_2(comune);
							}
							if(types.get(ite).equals("route")) {
								route = (String) address_components_ite.get("long_name") + ", ";
							}
							if(types.get(ite).equals("postal_code")) {
								postal_code = (String) address_components_ite.get("long_name") + ", ";;
							}
							if(types.get(ite).equals("country")) {
								/*
								if( !address_components_ite.get("short_name").equals(Constants.ITALIA) ){
									psg.setFormattedAddress(formatted_address);
									psg.setComune( Constants.COMUNE_STRANIERO );
									psg.setSiglaProvicia( Constants.PROVINCIA_STRANIERA );
									return psg;
								}
								*/
								psg.setSiglaNazione( address_components_ite.get("short_name").toString()  );
								psg.setNomeNazione( address_components_ite.get("long_name").toString()  );
							}
						}
					}
				}
				formatted_address = route + postal_code + comune;
				psg.setFormattedAddress(formatted_address);
			}
			psg.setGeolocationHtml5Address(formatted_address);
			
			RicercaTransfert_GoogleMaps_Info aaa = DammiComune_e_SiglaProvincia(address_components, psg);
			
			return aaa;
		}
		return null;
	}
	
	
	/**
	 * dammiInfoGoogle_by_PlaceID
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
    public RicercaTransfert_GoogleMaps_Info GoogleMaps_PlaceDetails(RicercaTransfert_GoogleMaps_Info psg, String language) 
    		throws JSONException, NullPointerException, UnknownHostException, Exception{
    	log.debug("GoogleMaps_PlaceDetails");
		/*
		 * NON E' SEMPRE AFFIDAILE, USARE SOLO PER RISALIRE AI PLACE_ID DI AEROPORTI, PORTI E MUSEI !!!!!!
		 * 
		 * vedere https://developers.google.com/places/web-service/search#TextSearchRequests
		 * per autocomplete vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
		 */
        String TYPE_PLACE_DETAILS = "/place/details";
		String OUT_JSON = "/json";
        StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_PLACE_DETAILS + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&placeid="+psg.getPlace_id());
		sb.append("&language="+language);
        JSONObject jb = new JSONObject( UrlConnection.HttpConnection( sb ) );
        String status = (String) jb.get("status");
        if(status.equals("OK")){
        	try{
        		JSONObject result = (JSONObject) jb.get("result");
        		String name = result.getString("name");
        		String formatted_address = result.getString("formatted_address");
        		psg.setName(name);
        		psg.setFormattedAddress(formatted_address);
        		JSONArray address_components = (JSONArray) result.get("address_components");
        		address_components.length();
        		try{
	        		String webSite = result.getString("website");
	        		psg.setWebSite(webSite);
	        	}catch(JSONException jsonExc ){
	        		psg.setWebSite(null);
		    	}
        		return DammiComune_e_SiglaProvincia(address_components, psg);
	        
	        }catch(JSONException jsonExc ){
	        	log.debug("JSONException: "+jsonExc.getMessage());
	    		return null;
	    	}
        }else if(status.equals("ZERO_RESULTS")){
        	log.debug("status: "+status);
        	return null;
        }else{
        	log.debug("status: "+status);
        	return null;
        }

	}
    
    
    private RicercaTransfert_GoogleMaps_Info DammiComune_e_SiglaProvincia(JSONArray address_components, RicercaTransfert_GoogleMaps_Info psg){
    	boolean comuneTrovato = false;
		for (int i=0; i < address_components.length(); i++){
			JSONObject address_components_ite = (JSONObject)address_components.get(i);
			JSONArray types = (JSONArray) address_components_ite.get("types");
			boolean exitFor = false;
	        if (types != null) {
	           int len = types.length();
	           for (int ite=0; ite<len; ite++){
	        	   if(types.get(ite).equals("locality")){
		        	   exitFor = true;
		        	   comuneTrovato = true;
		        	   String longName = (String)address_components_ite.get("long_name");
		        	   psg.setComune( longName );
		        	   //shortName = (String)address_components_ite.get("short_name");
	        	   }
	           }
	        } 
	        if(exitFor){
	        	break;
	        }
		}
		if(!comuneTrovato){
			for (int i=0; i < address_components.length(); i++){
    			JSONObject address_components_ite = (JSONObject)address_components.get(i);
    			JSONArray types = (JSONArray) address_components_ite.get("types");
    			boolean exitFor = false;
		        if (types != null) {
		           int len = types.length();
		           for (int ite=0; ite<len; ite++){
		        	   if( types.get(ite).equals("administrative_area_level_3") ){
    		        	   exitFor = true;
    		        	   
    		        	   String longName = (String)address_components_ite.get("long_name");
    		        	   psg.setComune( longName );
    		        	   //shortName = (String)address_components_ite.get("short_name");
		        	   }
		           }
		        } 
		        if(exitFor){
		        	break;
		        }
    		}
		}
		for(int i=0; i < address_components.length(); i++) {
			JSONObject address_components_ite = (JSONObject)address_components.get(i);
			JSONArray types = (JSONArray) address_components_ite.get("types");
			boolean exitFor = false;
	        if (types != null) {
	           int len = types.length();
	           for (int ite=0; ite<len; ite++){
	        	   if( types.get(ite).equals("administrative_area_level_2") ){
		        	   exitFor = true;
		        	   comuneTrovato = true;
		        	   String shortName = (String)address_components_ite.get("short_name");
		        	   String longName = (String)address_components_ite.get("long_name");
		        	   psg.setSiglaProvicia( shortName );
		        	   psg.setNomeProvicia( longName );
		        	   //shortName = (String)address_components_ite.get("short_name");
	        	   }
	           }
	        } 
	        if(exitFor){
	        	break;
	        }
		}
		for(int i=0; i < address_components.length(); i++) {
			JSONObject address_components_ite = (JSONObject)address_components.get(i);
			JSONArray types = (JSONArray) address_components_ite.get("types");
			boolean exitFor = false;
	        if (types != null) {
	           int len = types.length();
	           for (int ite=0; ite<len; ite++){
	        	   if( types.get(ite).equals("administrative_area_level_1") ){
		        	   exitFor = true;
		        	   comuneTrovato = true;
		        	   String shortName = (String)address_components_ite.get("short_name");
		        	   String longName = (String)address_components_ite.get("long_name");
		        	   psg.setSiglaRegione( shortName );
		        	   psg.setNomeRegione( longName );
		        	   //shortName = (String)address_components_ite.get("short_name");
	        	   }
	           }
	        } 
	        if(exitFor){
	        	break;
	        }
		}
		
		
		
		
		return psg;
    }
			
	/**
	 * dammiInfoGoogle_by_Address
	 * vedere: https://developers.google.com/maps/documentation/geocoding/intro
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 * 
	 */
	public RicercaTransfert_GoogleMaps_Info GoogleMaps_Geocode_Address(String address) 
			throws JSONException, NullPointerException, UnknownHostException, Exception {
		log.debug("GoogleMaps_Geocode_Address");
		String TYPE_SEARCH = "/geocode";
		String OUT_JSON = "/json";
		StringBuilder sb = new StringBuilder( Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_SEARCH + OUT_JSON);
		// "1600 Amphitheatre Parkway, Mountain View, CA"
		//https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
		address = address.replace(" ", "+");
		sb.append("?address=" + address );
		sb.append("&language=it"); // potrei anche mettere EN ma è meglio IT. un turista straniero leggendo la via in italiano ha più fiducia.
		sb.append("&key=" + Dammi_KEY_GOOGLE_API());
		JSONObject jb = new JSONObject(UrlConnection.HttpConnection(sb));
		String status = (String) jb.get("status");
		if (status.equals("OK")) {
			//JSONArray results = (JSONArray) jb.get("results");
			//JSONObject results_0 = (JSONObject) results.get(0); 
			//non lo uso ancora non mi serve
		}
		return null;
	}
	
	
    /**
     * dammiDistanzaMatrix_LAT_LNG
     */
	public RicercaTransfert GoogleMaps_DistanceMatrix(RicercaTransfert ricercaTransfert, String language, boolean ritorno) 
			throws IOException, ParseException, UnknownHostException, Exception {
		log.debug("GoogleMaps_DistanceMatrix");
		/*
		 * vedere: https://developers.google.com/maps/documentation/distance-matrix/intro#Introduction
		 * 
		 * https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEY
		 */
		String TYPE_MATRIX = "/distancematrix";
		String OUT_JSON = "/json";
		StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_MATRIX + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&mode=driving");
		sb.append("&language="+language);
		if(!ritorno){
			sb.append("&origins="+ricercaTransfert.getLat_Partenza()+","+ricercaTransfert.getLng_Partenza() );
			sb.append("&destinations="+ricercaTransfert.getLat_Arrivo()+","+ricercaTransfert.getLng_Arrivo() );
		}else{
			sb.append("&origins="+ricercaTransfert.getLat_Arrivo()+","+ricercaTransfert.getLng_Arrivo() );
			sb.append("&destinations="+ricercaTransfert.getLat_Partenza()+","+ricercaTransfert.getLng_Partenza() );
		}
		//sb.append("&origins=place_id:"+ricercaTransfert.getPlace_id_Partenza() );
		//sb.append("&destinations=place_id:"+ricercaTransfert.getPlace_id_Arrivo() );
		//per stablire l'orario di partenza e di arrivo, vedere: https://developers.google.com/maps/documentation/distance-matrix/intro#Introduction
		//sb.append("&arrival_time="+language); 
		Date dataPrelevamento;
		if(!ritorno){
			if(ricercaTransfert.getDataOraPrelevamento() != null 
					&& !ricercaTransfert.getDataOraPrelevamento().equals("")){
				dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca( ricercaTransfert.getDataOraPrelevamento(),ricercaTransfert.getOraPrelevamento() );
			}else{
				dataPrelevamento = ricercaTransfert.getDataOraPrelevamentoDate();
			}
		}else{
			if(ricercaTransfert.getDataOraRitorno() != null && !ricercaTransfert.getDataOraRitorno().equals("")){
				dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca( ricercaTransfert.getDataOraRitorno(),ricercaTransfert.getOraRitorno() );
			}else{
				dataPrelevamento = ricercaTransfert.getDataOraRitornoDate();
			}
		}
		if(dataPrelevamento != null  && dataPrelevamento.getTime() > new Date().getTime() ){
			long oraPartenza = dataPrelevamento.getTime() / 1000;
			sb.append("&departure_time="+oraPartenza);
		}else{
			// aggiungo 3 giorni alla data di adesso perché così viene calcolata una distanza non sottoposta al miglior tragitto in base al traffico di adesso
			Calendar c = Calendar.getInstance(); 
			c.setTime(new Date()); 
			c.add(Calendar.DATE, 3);
			Date dt = c.getTime();
			sb.append("&departure_time="+dt.getTime() / 1000 );
		}
		try{
			JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
	        String status = (String) jb.get("status");
	        if(status.equals("OK")){
	        	JSONArray rows = (JSONArray) jb.get("rows");
				JSONObject rows_1 = (JSONObject)rows.get(0); //il primo elemento (dovrebbe essere sempre 1)
				JSONArray elements = (JSONArray) rows_1.get("elements");
				JSONObject esito = (JSONObject) elements. get(0);
				if( !esito.getString("status").equals("ZERO_RESULTS") ){
					JSONObject elements_1 = (JSONObject)elements.get(0); //il primo elemento (dovrebbe essere sempre 1)
					JSONObject distance = (JSONObject) elements_1.get("distance");
					String distanzaText = distance.get("text").toString();
					long distanzaValue =  Long.parseLong( distance.get("value").toString() );
					JSONObject duration = (JSONObject) elements_1.get("duration");
					String durataText = duration.get("text").toString();
					long durataValue =  Long.parseLong( duration.get("value").toString() );
					JSONObject durationInTraffic = (JSONObject) elements_1.get("duration_in_traffic");
					String durataConTrafficText = durationInTraffic.get("text").toString();
					long durataConTrafficValue =  Long.parseLong( durationInTraffic.get("value").toString() );
					if(!ritorno){
					    ricercaTransfert.setDistanzaText(distanzaText);
					    ricercaTransfert.setDistanzaValue(distanzaValue);
					    ricercaTransfert.setDurataText(durataText);
					    ricercaTransfert.setDurataValue(durataValue);
					    ricercaTransfert.setDurataConTrafficoText(durataConTrafficText);
					    ricercaTransfert.setDurataConTrafficoValue(durataConTrafficValue);
					}else{
						ricercaTransfert.setDistanzaTextRitorno(distanzaText);
					    ricercaTransfert.setDistanzaValueRitorno(distanzaValue);
					    ricercaTransfert.setDurataTextRitorno(durataText);
					    ricercaTransfert.setDurataValueRitorno(durataValue);
					    ricercaTransfert.setDurataConTrafficoTextRitorno(durataConTrafficText);
					    ricercaTransfert.setDurataConTrafficoValueRitorno(durataConTrafficValue);
					}
					return ricercaTransfert;
				}else{
					throw new GoogleMatrixException( status );
				}
				
	        }else if(status.equals( "ZERO_RESULTS" )){
	        	throw new GoogleMatrixException( status );
	        	
	        }else if(status.equals("INVALID_REQUEST")){
	        	throw new GoogleMatrixException( status );
	       
	        }else if(status.equals("REQUEST_DENIED")){
	        	throw new GoogleMatrixException( status );
	        	
	        }else{
	        	log.debug("status: "+status);
	        	throw new GoogleMatrixException( status );
	        }
		}catch(JSONException jsonExc ){
			log.debug("JSONException: "+jsonExc.getMessage());
			return null;
		}
	}
    
	
	/**
	 * dammiAUTOCOMPLETE_by_InputString
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	public String GoogleMaps_PlaceAutocomplete(String input, String language) 
			throws JSONException, NullPointerException, UnknownHostException, Exception{
		log.debug("GoogleMaps_PlaceAutocomplete");
		/*
		 * vedere https://developers.google.com/places/web-service/search#TextSearchRequests
		 * per autocomplete vedere: https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests
		 */
		String TYPE_AUTOCOMPLETE = "/place/autocomplete";
		//String TYPE_QUERY_AUTOCOMPLETE = "/place/queryautocomplete";
		//String TYPE_SEARCH = "/place/textsearch";
		String OUT_JSON = "/json";
		StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		sb.append("&components=country:it");
		sb.append("&language="+language);
		sb.append("&input=" + URLEncoder.encode(input, "utf8"));
        JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
        String status = (String) jb.get("status");
        if(status.equals("OK")){
        	try{
	        	JSONArray jsonObject1 = (JSONArray) jb.get("predictions");
		        JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0); //il primo elemento (dovrebbe essere sempre 1)
		        String description = (String)jsonObject2.get("description");
		        return description;
	        }catch(JSONException jsonExc ){
	        	log.debug("JSONException: "+jsonExc.getMessage());
	    		return null;
	    	}
        }else if(status.equals("ZERO_RESULTS")){
        	log.debug("status: "+status);
        	return null;
        }else{
        	log.debug("status: "+status);
        	return null;
        }
	      
	}
    
	
	/**
	 * GoogleMaps_DistanceMatrixDurata (convertita usando le API GOOGLE.MAPS ufficiali)
	 */
	public long GoogleMaps_DistanceMatrixDurata(double getLat_Partenza, double getLng_Partenza, double getLat_Arrivo, double getLng_Arrivo, Date dataPrelevamento) 
			throws UnknownHostException, Exception{
		log.debug("GoogleMaps_DistanceMatrixDurata");
		GeoApiContext context = new GeoApiContext.Builder().apiKey( Dammi_KEY_GOOGLE_API() ).build();
		DistanceMatrix reqDistance1 = DistanceMatrixApi.newRequest(context)
			.origins(new LatLng(getLat_Partenza, getLng_Partenza))
			.destinations(new LatLng(getLat_Arrivo, getLng_Arrivo))
			.mode(TravelMode.DRIVING).departureTime(new DateTime(dataPrelevamento))
			//.trafficModel(TrafficModel.PESSIMISTIC)
			.await();
		return reqDistance1.rows[0].elements[0].durationInTraffic.inSeconds;
	}
	
	
	/**
	 * GoogleMaps_DistanceMatrixDistanza (convertita usando le API GOOGLE.MAPS ufficiali)
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ApiException 
	 */
	public long GoogleMaps_DistanceMatrixDistanza(double getLat_Partenza, double getLng_Partenza, double getLat_Arrivo, 
			double getLng_Arrivo, Date dataPrelevamento) throws ApiException, InterruptedException, IOException, NullPointerException {
		log.debug("GoogleMaps_DistanceMatrixDistanza");
		GeoApiContext context = new GeoApiContext.Builder().apiKey( Dammi_KEY_GOOGLE_API() ).build();
		DistanceMatrix reqDistance1 = DistanceMatrixApi.newRequest(context)
			.origins(new LatLng(getLat_Partenza, getLng_Partenza))
			.destinations(new LatLng(getLat_Arrivo, getLng_Arrivo))
			.mode(TravelMode.DRIVING).departureTime(new DateTime(dataPrelevamento))
			//.trafficModel(TrafficModel.PESSIMISTIC)
			.await();
	        return reqDistance1.rows[0].elements[0].distance.inMeters;
	}
	
	
	/**
	 * GoogleMaps_DistanceMatrixDurataOld
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	public long GoogleMaps_DistanceMatrixDurataOld(double getLat_Partenza, double getLng_Partenza, double getLat_Arrivo, double getLng_Arrivo, Date dataPrelevamento) 
			throws JSONException, NullPointerException, UnknownHostException, Exception{
		log.debug("GoogleMaps_DistanceMatrixDurataOld");
		/*
		 * vedere: https://developers.google.com/maps/documentation/distance-matrix/intro#Introduction
		 * 
		 * https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEY
		 */
		String TYPE_MATRIX = "/distancematrix";
		String OUT_JSON = "/json";
		StringBuilder sb = new StringBuilder(Constants.URL_MAP_GOOGLE_PLACES_API_BASE + TYPE_MATRIX + OUT_JSON );
		sb.append("?key=" + Dammi_KEY_GOOGLE_API());
		//sb.append("&language="+language);
		sb.append("&origins="+getLat_Partenza+","+getLng_Partenza );
		sb.append("&destinations="+getLat_Arrivo+","+getLng_Arrivo );
		long oraPartenza = dataPrelevamento.getTime() / 1000;
		//&departure_time=1343641500&mode=transit&key=YOUR_API_KEY
		sb.append("&mode=driving");
		sb.append("&departure_time="+oraPartenza);
        JSONObject jb = new JSONObject(UrlConnection.HttpConnection( sb ));
        String status = (String) jb.get("status");
        if(status.equals("OK")){
        	try{
		        JSONArray rows = (JSONArray) jb.get("rows");
		        JSONObject rows_1 = (JSONObject)rows.get(0); //il primo elemento (dovrebbe essere sempre 1)
		        JSONArray elements = (JSONArray) rows_1.get("elements");
		        JSONObject elements_1 = (JSONObject)elements.get(0); //il primo elemento (dovrebbe essere sempre 1)
		        //JSONObject distance = (JSONObject) elements_1.get("distance");
		        //String distanzaText = distance.get("text").toString();
		        //long distanzaValue =  Long.parseLong( distance.get("value").toString() );
		        //JSONObject duration = (JSONObject) elements_1.get("duration");
		        //String durataText = duration.get("text").toString();
		        //long durataValue =  Long.parseLong( duration.get("value").toString() );
		        JSONObject durationInTraffic = (JSONObject) elements_1.get("duration_in_traffic");
		        //String durataConTrafficText = durationInTraffic.get("text").toString();
		        long durataConTrafficValueOLD =  Long.parseLong( durationInTraffic.get("value").toString() );
		        System.out.println("durataConTrafficValueOLD: "+durataConTrafficValueOLD);
		        return durataConTrafficValueOLD;
        	}catch(JSONException jsonExc ){
        		log.debug("JSONException: "+jsonExc.getMessage());
        		return 0;
        	}
        }else if(status.equals("ZERO_RESULTS")){
        	log.debug("status: "+status);
        	return 0;
        }else{
        	log.debug("status: "+status);
        	return 0;
        }
	}
	
	

	/**
	 * lO FACCIO SOLO PER CONTROLLARE LA PARTENZA, ALTRIMENTI ANCHE PER IL RITORNO è TROPPO FASTIDIOSO PER IL CLIENTE
	 * 
	 * googleapis.com/maps/api ritorna un json di informazioni tra i quali il "types" che definisce il tipo del luogo ricercato.
	 * Ad esempio se si ricerca "via orbassano 1" allora ritorna "street_address" se si inserisce "Roma", allora ritorna "politicy" 
	 * che nel mio caso non lo devo accettare.
	 * Quelli che consento sono: street_address, route, point_of_interest, airport
	 */
	public boolean GoogleMapsTypesAccettato(List<String> typesInput){
		boolean esito = false;
		if(typesInput != null){
			for(String typeInput: typesInput ) {
				if(Constants.GOOGLE_MAPS_TYPES_ACCETTATI.contains( typeInput )){
					esito = true;
					break;
				}
			}
		}
		return esito;
	}
	
	
}

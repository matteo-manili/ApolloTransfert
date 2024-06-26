package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.model.Autoveicolo;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.JwtTokenUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*") 
public class Api_Menu_e_Mappa extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_Menu_e_Mappa.class);
	
	private AgA_GiornateManager agA_GiornateManager;
    @Autowired
    public void setAgA_GiornateManager(AgA_GiornateManager agA_GiornateManager) {
		this.agA_GiornateManager = agA_GiornateManager;
	}
	
    @RequestMapping(value="/api_AreaGeografica_Set_LatLngRaggioAddress", method=RequestMethod.POST) 
    public ResponseEntity<String> api_AreaGeografica_Set_LatLngRaggioAddress(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_AreaGeografica_Set_LatLngRaggioAddress");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				Double lat = requestBody.get(AgA_General.JN_AreaGeog_Lat) != null ? Double.parseDouble(requestBody.get( AgA_General.JN_AreaGeog_Lat ).toString()) : null;
				Double lng = requestBody.get(AgA_General.JN_AreaGeog_Lng) != null ? Double.parseDouble(requestBody.get( AgA_General.JN_AreaGeog_Lng ).toString()) : null;
				Double kmRaggioArea = requestBody.get(AgA_General.JN_raggio) != null ? Double.parseDouble(requestBody.get( AgA_General.JN_raggio ).toString()) : null;
				String Address = requestBody.get(AgA_General.JN_AreaGeog_Address) != null && !requestBody.get(AgA_General.JN_AreaGeog_Address).equals("") 
						? UtilString.RimuoviCaratteriNonUTF8(requestBody.get(AgA_General.JN_AreaGeog_Address).toString()) : null;
						
				Autoveicolo autoveicolo = autoveicoloManager.get(idAutoveicolo);
				
				//----------------
				//System.out.println("OOOOOOOOOOOOOOOOOOLD: "+autoveicolo.getAgA_AreaGeografica_Lat() +" NEW:"+lat);
				//System.out.println("OOOOOOOOOOOOOOOOOOLD: "+autoveicolo.getAgA_AreaGeografica_Lng() +" NEW:"+lng);
				//---------------
				
				JSONObject infoAutoveicoloJson = AgA_General.GetInfoAutoveicoloJson(autoveicolo);
				if(lat != null) { infoAutoveicoloJson.put(AgA_General.JN_AreaGeog_Lat, new Double(lat)); }
				if(lng != null) { infoAutoveicoloJson.put(AgA_General.JN_AreaGeog_Lng, new Double(lng)); }
				if(kmRaggioArea != null) { 
					kmRaggioArea = kmRaggioArea > AgA_General.RaggioArea_Max ? AgA_General.RaggioArea_Max : kmRaggioArea;
					infoAutoveicoloJson.put(AgA_General.JN_raggio, new Double(kmRaggioArea)); 
				}
				if(Address != null) { infoAutoveicoloJson.put(AgA_General.JN_AreaGeog_Address, Address); }
				if( lat != null || lng != null || kmRaggioArea != null || Address != null ) {
					autoveicolo.setInfo( infoAutoveicoloJson.toString() );
					autoveicolo = autoveicoloManager.saveAutoveicolo(autoveicolo);
					mainObj = AgA_General.GetValues_AreaGeografica(autoveicolo);
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
				}else {
					mainObj = AgA_General.GetValues_AreaGeografica(autoveicolo);
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED);
				}
				//log.debug("mainObj: "+mainObj);
				return ResponseEntity.ok().body(mainObj.toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    @RequestMapping(value="/api_AreaGeografica_Get_LatLngRaggioAddress", method=RequestMethod.POST) 
    public ResponseEntity<String> api_AreaGeografica_Get_LatLngRaggioAddress(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_AreaGeografica_Get_LatLngRaggioAddress");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				Autoveicolo autoveicolo = autoveicoloManager.get(idAutoveicolo);
				mainObj = AgA_General.GetValues_AreaGeografica(autoveicolo);
	    		mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
	    		//log.debug("mainObj: "+mainObj);
				return ResponseEntity.ok().body(mainObj.toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    /**
     * alert del menu:
     * Crea Area Geografica 
     * Crea almeno un Modello Tariffario 
     * Crea almeno un Modello Giornata
     */
	@RequestMapping(value="/api_Menu", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Menu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Menu");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				mainObj = agA_GiornateManager.Menu_Data(idAutoveicolo);
				if(mainObj.length() == 0) {
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NO_CONTENT);
				}else {
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
				}
				return ResponseEntity.ok().body(mainObj.toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    

}

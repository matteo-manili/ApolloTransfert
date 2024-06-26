package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.model.Autoveicolo;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.JwtTokenUtil;
import com.apollon.webapp.rest.JwtTokenUtil.InfoJwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*") 
public class Api_Login_Home extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_Login_Home.class);
	
	private AgA_GiornateManager agA_GiornateManager;
    @Autowired
    public void setAgA_GiornateManager(AgA_GiornateManager agA_GiornateManager) {
		this.agA_GiornateManager = agA_GiornateManager;
	}
	
	@RequestMapping(value ="/api_Autista_ListaAutoveicoliAutista", method = RequestMethod.POST)
    public ResponseEntity<String> api_Autista_ListaAutoveicoliAutista(HttpServletRequest request) throws IOException {
		log.debug("api_Autista_ListaAutoveicoliAutista");
    	JSONObject mainObj = new JSONObject();
    	Long idAutista = null;
    	try{
	    	idAutista = JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)).getIdAutista();
    	}catch(Exception ee) {
			log.error(ee.getClass().getSimpleName());
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_MESSAGE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    	try{
	    	idAutista = JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)).getIdAutista();
	    	if( idAutista != null ) {
	    		
	    		//List<Autoveicolo> autoveicoliList = autoveicoloManager.getAutoveicoloByAutista(idAutista, false);
	    		List<Autoveicolo> autoveicoliList = autoveicoloManager.getAutoveicoloByAutista_Agenda(idAutista);
	    		
	    		JSONArray jArray = new JSONArray();
	    		for(Autoveicolo auto: autoveicoliList) {
	    			if( auto.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() && !auto.isAutoveicoloCancellato() && !auto.isAutoveicoloSospeso() ) {
	    				JSONObject json = new JSONObject();
		    			json.put(AgA_General.JN_idAutoveicolo, auto.getId());
		    			json.put(AgA_General.JN_marcaModelloTarga, auto.getMarcaModelloTarga());
		    			json.put(AgA_General.JN_classeAutoveicolo, getText(auto.getClasseAutoveicoloReale().getNome(), request.getLocale())); // LA APP "AGENDA AUTISTA" NON LO USA
		    			json.put(AgA_General.JN_anno, auto.getAnnoImmatricolazione());
		    			json.put(AgA_General.JN_marca, auto.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName());
		    			json.put(AgA_General.JN_modello, auto.getModelloAutoNumeroPosti().getModelloAutoScout().getName());
		    			json.put(AgA_General.JN_targa, auto.getTarga()); 
		    			json.put(AgA_General.JN_Calendario_GiornoColore, agA_GiornateManager.AutoveicoloDisponbileVendita(auto.getId()) 
		    					? AgA_General.COLORE_GREEN : AgA_General.COLORE_DEFAULT); 
		    			jArray.put(json);
	    			}
	    		}
	    		mainObj.put(AgA_General.JN_listaAutoveicoli, jArray);
	    		//log.debug("mainObj: "+mainObj);
	    		return ResponseEntity.ok().body(mainObj.toString());
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_MESSAGE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
		}catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_MESSAGE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
	
    @RequestMapping(value ="/api_Authentication_VerificaJwtInfoJwt", method = RequestMethod.POST)
    public ResponseEntity<String> api_Authentication_VerificaJwtInfoJwt(HttpServletRequest request) throws IOException {
		log.debug("api_Authentication_VerificaJwtInfoJwt");
    	JSONObject mainObj = new JSONObject();
	    try {
	    	InfoJwt infoJwt = JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION));
	    	if( infoJwt != null ) {
	    		mainObj.put(AgA_General.JN_jwtCreated, infoJwt.getJwtCreated());
	    		mainObj.put(AgA_General.JN_jwtExpired, infoJwt.getJwtExpired());
	    		mainObj.put(AgA_General.JN_username, infoJwt.getUsername());
	    		mainObj.put(AgA_General.JN_idAutista, infoJwt.getIdAutista());
	    		mainObj.put(AgA_General.JN_fullNameAutista, infoJwt.getFullNameAutista());
	    		mainObj.put(AgA_General.JN_denominazioneAziendaAutista, infoJwt.getDenominazioneAziendaAutista());
	    		// il cacheControl(CacheControl.noCache()) comunica al browser che non deve salvare dati nella cache
	    		return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(mainObj.toString());
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_MESSAGE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
		}catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_MESSAGE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
	

	
	


}

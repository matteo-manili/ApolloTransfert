package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.service.AgA_AutoveicoloModelliTariffariManager;
import com.apollon.service.AgA_ModelliTariffariManager;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_ModelTariffario;
import com.apollon.webapp.rest.AgA_Tariffario;
import com.apollon.webapp.rest.JwtTokenUtil;
import com.apollon.webapp.rest.AgA_ModelTariffario.TabellaTariffarioAutista;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
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
public class Api_ModelTariffario extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_ModelTariffario.class);
	
    private AgA_AutoveicoloModelliTariffariManager agA_AutoveicoloModelTariffManager;
    @Autowired
    public void setAgA_AutoveicoloModelliTariffariManager(final AgA_AutoveicoloModelliTariffariManager agA_AutoveicoloModelTariffManager) {
        this.agA_AutoveicoloModelTariffManager = agA_AutoveicoloModelTariffManager;
    }
    
    private AgA_ModelliTariffariManager agA_ModelTariffManager;
    @Autowired
    public void setAgA_ModelliTariffariManager(final AgA_ModelliTariffariManager agA_ModelTariffManager) {
        this.agA_ModelTariffManager = agA_ModelTariffManager;
    }
    
    @RequestMapping(value ="/api_ModelTariffario_TabellaKilometriModello", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelTariffario_TabellaKilometriModello(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_TabellaKilometriModello");
		JSONObject mainObj = new JSONObject();
		try {
	    	if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
	    		log.debug( "requestBody: "+requestBody );
	    		Long idModelloTariffario = Long.parseLong(requestBody.get(AgA_General.JN_idModelloTariffario).toString());
	    		List<AgA_ModelliTariffari> modelTarifList = agA_ModelTariffManager.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(idModelloTariffario);
	    		JSONArray jArray = new JSONArray();
	    		for(TabellaTariffarioAutista tabTariffIte: AgA_ModelTariffario.InserisciModelloTariffarioAutista_in_TabellaTariffarioAutista(modelTarifList)) {
	    			JSONObject json = new JSONObject();
	    			json.put(AgA_General.JN_fromKm, tabTariffIte.getFromKm());
	    			json.put(AgA_General.JN_toKm, tabTariffIte.getToKm());
	    			json.put(AgA_General.JN_eseguiCorse, tabTariffIte.getEseguiCorse());
	    			json.put(AgA_General.JN_prezzo, tabTariffIte.getEuro());
	    			json.put(AgA_General.JN_raggio, tabTariffIte.getRaggio());
	    			jArray.put(json);
	    		}
	    		mainObj.put(AgA_General.JN_tabellaTariffario, jArray);
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
     * Il campo eseguiCorse deve essere false quando il campo Prezzo o il campo Raggio: non sono valorizzati, sono a ZERO, caratteri diversi da numeri.
     * @param requestBody
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/api_ModelTariffario_ModelTariff_UpdateKmCorsa", method=RequestMethod.POST 
    		/*,consumes=MediaType.APPLICATION_JSON_VALUE ,produces=MediaType.APPLICATION_JSON_VALUE*/ ) 
    public ResponseEntity<String> api_ModelTariffario_ModelTariff_UpdateKmCorsa(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_ModelTariff_UpdateKmCorsa");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idModelloTariffario = Long.parseLong(requestBody.get( AgA_General.JN_idModelloTariffario ).toString()) ;
				Integer kmCorsaFrom = Integer.parseInt(requestBody.get( AgA_General.JN_fromKm ).toString());	
				Integer kmCorsaTo = Integer.parseInt(requestBody.get( AgA_General.JN_toKm ).toString());
				Boolean eseguiCorse = requestBody.get( AgA_General.JN_eseguiCorse ) != null ? (Boolean)requestBody.get( AgA_General.JN_eseguiCorse ) : false;
				BigDecimal prezzoCorsa = null;
				Double kmRaggioArea = null;
				try { prezzoCorsa = requestBody.get( AgA_General.JN_prezzo ) != null ? new BigDecimal(requestBody.get( AgA_General.JN_prezzo ).toString()) : null;
					}catch(Exception ee) { prezzoCorsa = null; }
				try { 
					kmRaggioArea = requestBody.get( AgA_General.JN_raggio ) != null ? Double.parseDouble(requestBody.get( AgA_General.JN_raggio ).toString()) : null;
				}catch(Exception ee) { kmRaggioArea = null; }
				eseguiCorse = (prezzoCorsa == null || prezzoCorsa.compareTo(BigDecimal.ZERO) == 0 || kmRaggioArea == null || kmRaggioArea == 0) ? false : eseguiCorse;
				AgA_AutoveicoloModelliTariffari agA_AutoveicoloModTariff = agA_AutoveicoloModelTariffManager.get(idModelloTariffario);
				kmRaggioArea = AgA_Tariffario.Check_KilometroReggioArea( agA_AutoveicoloModTariff.getAutoveicolo(), kmRaggioArea );
				AgA_ModelliTariffari modTariff = null;
				if( (prezzoCorsa != null && prezzoCorsa.compareTo(BigDecimal.ZERO) > 0) || (kmRaggioArea != null && kmRaggioArea > 0) ) {
					for(int ite = kmCorsaFrom; ite <= kmCorsaTo; ite++) {
						modTariff = agA_ModelTariffManager.getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(idModelloTariffario, ite);
						if(modTariff != null) {
							modTariff.setEseguiCorse(eseguiCorse);
							modTariff.setPrezzoCorsa(prezzoCorsa);
							modTariff.setKmRaggioArea(kmRaggioArea);
						}else {
							modTariff = new AgA_ModelliTariffari(ite, eseguiCorse, prezzoCorsa, kmRaggioArea, agA_AutoveicoloModTariff);
						}
						modTariff = agA_ModelTariffManager.saveAgA_ModelliTariffari(modTariff);
					}
				}else if( (prezzoCorsa == null || prezzoCorsa.compareTo(BigDecimal.ZERO) == 0) && (kmRaggioArea == null || kmRaggioArea == 0) ) {
					for(int ite = kmCorsaFrom; ite <= kmCorsaTo; ite++) {
						modTariff = agA_ModelTariffManager.getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(idModelloTariffario, ite);
						if( modTariff != null ) {
							agA_ModelTariffManager.removeAgA_ModelliTariffari(modTariff.getId());
						}
					}
				}
				mainObj.put(AgA_General.JN_idModelloTariffario, idModelloTariffario);
				mainObj.put(AgA_General.JN_fromKm, kmCorsaFrom);
				mainObj.put(AgA_General.JN_toKm, kmCorsaTo);
				mainObj.put(AgA_General.JN_eseguiCorse, eseguiCorse); 
				mainObj.put(AgA_General.JN_prezzo, prezzoCorsa); 
				mainObj.put(AgA_General.JN_raggio, kmRaggioArea);
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
    
    
    @RequestMapping(value ="/api_ModelTariffario_ModelTariff_ModificaNome", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelTariffario_ModelTariff_ModificaNome(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_ModelTariff_ModificaNome");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				Long idModelloTariffario = Long.parseLong(requestBody.get(AgA_General.JN_idModelloTariffario).toString());
				String nomeModelloTariffario = requestBody.get(AgA_General.JN_nomeModelloTariffario).toString();
				AgA_AutoveicoloModelliTariffari autoveicoloModelTarif = agA_AutoveicoloModelTariffManager.get(idModelloTariffario);
				if( nomeModelloTariffario == null || nomeModelloTariffario.equals("") ) {
					mainObj.put(AgA_General.JN_nomeModelloTariffario, autoveicoloModelTarif.getNomeTariffario());
				}else {
					autoveicoloModelTarif.setNomeTariffario(nomeModelloTariffario);
					autoveicoloModelTarif = agA_AutoveicoloModelTariffManager.saveAgA_AutoveicoloModelliTariffari(autoveicoloModelTarif);
					mainObj.put(AgA_General.JN_nomeModelloTariffario, autoveicoloModelTarif.getNomeTariffario());
				}
				mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
				return ResponseEntity.ok().body(mainObj.toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
	    	ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    @RequestMapping(value ="/api_ModelTariffario_ModelTariff_Nuovo", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelTariffario_ModelTariff_Nuovo(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_ModelTariff_Nuovo");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				int NumMaxTariffari = 5;
	    		Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
	    		List<AgA_AutoveicoloModelliTariffari> modelliTariffari_list = agA_AutoveicoloModelTariffManager.getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(idAutoveicolo);
	    		if(modelliTariffari_list != null && modelliTariffari_list.size() < NumMaxTariffari) {
	    			String nomeModelloTariffario = modelliTariffari_list != null ? "Tariffario "+(modelliTariffari_list.size() + 1) : "Tariffario 1";
		    		AgA_AutoveicoloModelliTariffari nuovoModelloTariff = new AgA_AutoveicoloModelliTariffari();
		    		nuovoModelloTariff.setAutoveicolo(autoveicoloManager.get(idAutoveicolo));
		    		nuovoModelloTariff.setNomeTariffario(nomeModelloTariffario);
					nuovoModelloTariff = agA_AutoveicoloModelTariffManager.saveAgA_AutoveicoloModelliTariffari(nuovoModelloTariff);
					mainObj.put(AgA_General.JN_idModelloTariffario, nuovoModelloTariff.getId());
					mainObj.put(AgA_General.JN_nomeModelloTariffario, nuovoModelloTariff.getNomeTariffario());
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_CREATED);
					return ResponseEntity.ok().body(mainObj.toString());
	    		}else {
	    			mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED);
	    			mainObj.put(Constants.JSON_MESSAGE, "Non puoi creare più di "+NumMaxTariffari+" Modelli Tariffari" );
	    			return ResponseEntity.ok().body(mainObj.toString());
	    		}
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
	    	ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		} 
    }
    
    
    @RequestMapping(value ="/api_ModelTariffario_ModelTariff_Elimina", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelTariffario_ModelTariff_Elimina(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_ModelTariff_Elimina");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				try {
		    		Long idAutoModelTarif = Long.parseLong(requestBody.get(AgA_General.JN_idModelloTariffario).toString());
		    		
		    		//agA_AutoveicoloModelTariffManager.removeAgA_AutoveicoloModelliTariffari(idAutoModelTarif);
		    		
		    		agA_AutoveicoloModelTariffManager.EliminaModelliTariffari(idAutoModelTarif);
		    		
					
		    		mainObj.put(AgA_General.JN_idModelloTariffarioRimosso, idAutoModelTarif);
		    		mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
		    		return ResponseEntity.ok().body(mainObj.toString());
				}catch(IllegalArgumentException illArg ) {
					return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED).toString());
				}
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    @RequestMapping(value ="/api_ModelTariffario_ModelTariff_Lista", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelTariffario_ModelTariff_Lista(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelTariffario_ModelTariff_Lista");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
	    		Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
	    		List<AgA_AutoveicoloModelliTariffari> modelliTariffari_list = agA_AutoveicoloModelTariffManager.getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(idAutoveicolo);
	    		if(modelliTariffari_list.size() > 0) {
	    			JSONArray jArray = new JSONArray();
		    		for(AgA_AutoveicoloModelliTariffari ite: modelliTariffari_list) {
		    			JSONObject json = new JSONObject();
		    			json.put(AgA_General.JN_idModelloTariffario, ite.getId());
		    			json.put(AgA_General.JN_nomeModelloTariffario, ite.getNomeTariffario());
		    			jArray.put(json);
		    		}
		    		mainObj.put(AgA_General.JN_listaModelliTariffari, jArray);
		    		return ResponseEntity.ok().body(mainObj.toString());
	    		}else {
	    			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, HttpServletResponse.SC_NO_CONTENT).toString());
	    		}
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }


}

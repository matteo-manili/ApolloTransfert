package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.service.AgA_AutoveicoloModelliGiornateManager;
import com.apollon.service.AgA_AutoveicoloModelliTariffariManager;
import com.apollon.service.AgA_ModelliGiornateManager;
import com.apollon.service.AgA_ModelliTariffariManager;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.rest.AgA_ModelGiornata;
import com.apollon.webapp.rest.AgA_Tariffario;
import com.apollon.webapp.rest.JwtTokenUtil;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
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
public class Api_ModelGiornata extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_ModelGiornata.class);
	

    private AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager;
    @Autowired
    public void setAgA_AutoveicoloModelliGiornateManager(final AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager) {
        this.agA_AutoveicoloModelliGiornateManager = agA_AutoveicoloModelliGiornateManager;
    }
    
    private AgA_ModelliGiornateManager agA_ModelliGiornateManager;
    @Autowired
    public void setAgA_ModelliGiornateManager(final AgA_ModelliGiornateManager agA_ModelliGiornateManager) {
        this.agA_ModelliGiornateManager = agA_ModelliGiornateManager;
    }
    
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
    
    
    /**
	 * Ritorna la lista delle voci del Modal al click su una GiornataOrario della tabella
     */
	@RequestMapping(value="/api_ModelGiornata_Tariffario_Menu", method=RequestMethod.POST) 
    public ResponseEntity<String> api_ModelGiornata_Tariffario_Menu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_Tariffario_Menu");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				Long idModelloGiornata = Long.parseLong(requestBody.get(AgA_General.JN_idModelloGiornata).toString());
				Integer orario = Integer.parseInt(requestBody.get(AgA_General.JN_orario).toString());
				List<AgA_AutoveicoloModelliTariffari> modelliTariffari_list = agA_AutoveicoloModelTariffManager.getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(idAutoveicolo);
				AgA_ModelliGiornate modelGiornata = agA_ModelliGiornateManager.getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(idModelloGiornata, orario);
				mainObj = AgA_ModelGiornata.Dammi_MenuOrarioModelGiornata_Tariffario(modelliTariffari_list, modelGiornata);
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
    
    
    @RequestMapping(value ="/api_ModelGiornata_TabellaGiornataModello", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelGiornata_TabellaGiornataModello(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_TabellaGiornataModello");
		JSONObject mainObj = new JSONObject();
		try {
	    	if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
	    		log.debug( "requestBody: "+requestBody );
	    		Long idModelloGiornata = Long.parseLong(requestBody.get(AgA_General.JN_idModelloGiornata).toString());
	    		List<AgA_ModelliGiornate> modelGiornateList = agA_ModelliGiornateManager.getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(idModelloGiornata);
	    		JSONArray jArray = new JSONArray();
	    		for(TabellaGiornata tabGiornataIte: AgA_ModelGiornata.InserisciModelloGiornataAutista_in_TabellaModelGiornata(modelGiornateList)) {
	    			JSONObject json = new JSONObject();
	    			json.put(AgA_General.JN_orario, tabGiornataIte.getOrario());
	    			json.put(AgA_General.JN_orarioFormatEsteso, tabGiornataIte.getOrarioFormatEsteso());
	    			json.put(AgA_General.JN_attivo, tabGiornataIte.getAttivo());
	    			json.put(AgA_General.JN_idModelloTariffario, tabGiornataIte.getIdTariffario() != null ? tabGiornataIte.getIdTariffario() : JSONObject.NULL);
	    			json.put(AgA_General.JN_nomeModelloTariffario, tabGiornataIte.getTariffarioDesc());
	    			jArray.put(json);
	    		}
	    		mainObj.put(AgA_General.JN_tabellaGiornata, jArray);
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
     * Un orario può essere modificato solo se viene passato l'idTariffario
     * Il field attivo può essere modificato solo se è passato anche l'idTariffario
     * Per cancellare il tariffario: non passare l'idTariffario oppure passarlo a null
     */
    @RequestMapping(value="/api_ModelGiornata_ModelGiornata_UpdateOrario", method=RequestMethod.POST ) 
    public ResponseEntity<String> api_ModelGiornata_ModelGiornata_UpdateOrario(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_ModelGiornata_UpdateOrario");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idModelloGiornata = Long.parseLong(requestBody.get( AgA_General.JN_idModelloGiornata ).toString()) ;
				Integer orario = Integer.parseInt(requestBody.get(AgA_General.JN_orario).toString());	
				Boolean attivo = requestBody.get(AgA_General.JN_attivo) != null ? (Boolean)requestBody.get(AgA_General.JN_attivo) : false;
				Long idModelloTariffario = requestBody.get(AgA_General.JN_idModelloTariffario) != null 
						? Long.parseLong(requestBody.get(AgA_General.JN_idModelloTariffario).toString()) : null;
				AgA_ModelliGiornate modelGiornata = agA_ModelliGiornateManager.getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(idModelloGiornata, orario);
				if(modelGiornata != null) {
					AgA_AutoveicoloModelliTariffari modelTariffario = idModelloTariffario != null ? agA_AutoveicoloModelTariffManager.get(idModelloTariffario) : null;
					// modifica - se passo NULL a idModelloTariffario significa che devono eliminare il modelGiornata
					if(modelTariffario != null) {
						modelGiornata.setAgA_AutoveicoloModelliTariffari(modelTariffario);
						List<AgA_ModelliTariffari> modelTariffari_List = agA_ModelTariffManager.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(modelTariffario.getId());
						attivo = AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(modelTariffari_List) ? attivo : false;
						modelGiornata.setAttivo(attivo);
						modelGiornata = agA_ModelliGiornateManager.saveAgA_ModelliGiornate(modelGiornata);
					}else if(idModelloTariffario == null) { // nel caso in cui il idModelloTariffario ha un id inesistente
						agA_ModelliGiornateManager.removeAgA_ModelliGiornate(modelGiornata.getId());
						mainObj = AgA_Giornata.Dammi_GiornataOrario_Vuota_Json(orario);
						mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
						//log.debug("mainObj: "+mainObj);
						return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK).toString());
					}
				// inserimento nuovo
				}else if( modelGiornata == null && idModelloTariffario != null ) {
					AgA_AutoveicoloModelliGiornate autoveicoloModelGiornata = agA_AutoveicoloModelliGiornateManager.get(idModelloGiornata);
					AgA_AutoveicoloModelliTariffari modelTariffario = agA_AutoveicoloModelTariffManager.get(idModelloTariffario);
					if(modelTariffario != null) {
						List<AgA_ModelliTariffari> modelTariffari_List = agA_ModelTariffManager.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(modelTariffario.getId());
						attivo = AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(modelTariffari_List) ? attivo : false;
						modelGiornata = new AgA_ModelliGiornate(orario, attivo, modelTariffario, autoveicoloModelGiornata);
						modelGiornata = agA_ModelliGiornateManager.saveAgA_ModelliGiornate(modelGiornata);
					}
				}
				if( modelGiornata != null ) {
					mainObj = AgA_Giornata.Dammi_GiornataOrario_Json(modelGiornata);
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
					//log.debug("mainObj: "+mainObj);
					return ResponseEntity.ok().body(mainObj.toString());
				}
				return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED).toString());
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    @RequestMapping(value ="/api_ModelGiornata_ModelGiornata_ModificaNome", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelGiornata_ModelGiornata_ModificaNome(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_ModelGiornata_ModificaNome");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idModelloGiornata = Long.parseLong(requestBody.get(AgA_General.JN_idModelloGiornata).toString());
				String nomeModelloGiornata = requestBody.get(AgA_General.JN_nomeModelloGiornata).toString();
				AgA_AutoveicoloModelliGiornate autoveicoloModelTarif = agA_AutoveicoloModelliGiornateManager.get(idModelloGiornata);
				autoveicoloModelTarif.setNomeGiornata(nomeModelloGiornata);
				autoveicoloModelTarif = agA_AutoveicoloModelliGiornateManager.saveAgA_AutoveicoloModelliGiornate(autoveicoloModelTarif);
				mainObj.put(AgA_General.JN_nomeModelloGiornata, autoveicoloModelTarif.getNomeGiornata());
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
    

    @RequestMapping(value ="/api_ModelGiornata_ModelGiornata_Nuovo", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelGiornata_ModelGiornata_Nuovo(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_ModelGiornata_Nuovo");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				int NumMaxGiornate = 5;
	    		Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
	    		List<AgA_AutoveicoloModelliGiornate> modelliGiornate_list = agA_AutoveicoloModelliGiornateManager.getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(idAutoveicolo);
	    		if(modelliGiornate_list != null && modelliGiornate_list.size() < NumMaxGiornate) {
	    			String nomeModelloGiornata = modelliGiornate_list != null ? "Giornata "+(modelliGiornate_list.size() + 1) : "Giornata 1";
		    		AgA_AutoveicoloModelliGiornate nuovoModelloGiornata = new AgA_AutoveicoloModelliGiornate();
		    		nuovoModelloGiornata.setAutoveicolo(autoveicoloManager.get(idAutoveicolo));
		    		nuovoModelloGiornata.setNomeGiornata(nomeModelloGiornata);
		    		nuovoModelloGiornata = agA_AutoveicoloModelliGiornateManager.saveAgA_AutoveicoloModelliGiornate(nuovoModelloGiornata);
					mainObj.put(AgA_General.JN_idModelloGiornata, nuovoModelloGiornata.getId());
					mainObj.put(AgA_General.JN_nomeModelloGiornata, nuovoModelloGiornata.getNomeGiornata());
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_CREATED);
		    		return ResponseEntity.ok().body(mainObj.toString());
	    		}else {
	    			mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED);
	    			mainObj.put(Constants.JSON_MESSAGE, "Non puoi creare più di "+NumMaxGiornate+" Modelli Giornate");
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
    
    
    @RequestMapping(value ="/api_ModelGiornata_ModelGiornata_Elimina", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelGiornata_ModelGiornata_Elimina(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_ModelGiornata_Elimina");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
	    		try {
					Long idModelloGiornata = Long.parseLong(requestBody.get(AgA_General.JN_idModelloGiornata).toString());
		    		agA_AutoveicoloModelliGiornateManager.removeAgA_AutoveicoloModelliGiornate(idModelloGiornata);
					mainObj.put(AgA_General.JN_idModelloGiornataRimosso, idModelloGiornata);
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
		    		return ResponseEntity.ok().body(mainObj.toString());
				}catch(IllegalArgumentException illArg ) {
					return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NOT_MODIFIED).toString());
				}
	    	}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			//ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
    
    @RequestMapping(value ="/api_ModelGiornata_ModelGiornata_Lista", method = RequestMethod.POST)
    public ResponseEntity<String> api_ModelGiornata_ModelGiornata_Lista(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_ModelGiornata_ModelGiornata_Lista");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
	    		Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
	    		List<AgA_AutoveicoloModelliGiornate> modelliGiornate_list = agA_AutoveicoloModelliGiornateManager.getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(idAutoveicolo);
	    		if(modelliGiornate_list.size() > 0) {
	    			JSONArray jArray = new JSONArray();
		    		for(AgA_AutoveicoloModelliGiornate ite: modelliGiornate_list) {
		    			JSONObject json = new JSONObject();
		    			json.put(AgA_General.JN_idModelloGiornata, ite.getId());
		    			json.put(AgA_General.JN_nomeModelloGiornata, ite.getNomeGiornata());
		    			jArray.put(json);
		    		}
		    		mainObj.put(AgA_General.JN_listaModelliGiornate, jArray);
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

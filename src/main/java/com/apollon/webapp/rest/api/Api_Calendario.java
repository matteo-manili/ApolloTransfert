package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.model.Autoveicolo;
import com.apollon.service.AgA_AutoveicoloModelliGiornateManager;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.webapp.rest.AgA_Calendario;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.rest.JwtTokenUtil;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;
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
public class Api_Calendario extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_Calendario.class);
	
	private AgA_GiornateManager agA_GiornateManager;
    @Autowired
    public void setAgA_GiornateManager(AgA_GiornateManager agA_GiornateManager) {
		this.agA_GiornateManager = agA_GiornateManager;
	}
    
    private AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager;
    @Autowired
    public void setAgA_AutoveicoloModelliGiornateManager(AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager) {
		this.agA_AutoveicoloModelliGiornateManager = agA_AutoveicoloModelliGiornateManager;
	}
    
    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(AutoveicoloManager autoveicoloManager) {
		this.autoveicoloManager = autoveicoloManager;
	}

    
    @RequestMapping(value="/api_Calendario_Set_AutoClearProssimeOreGiornate", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Calendario_Set_AutoClearProssimeOreGiornate(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Calendario_Set_AutoClearProssimeOreGiornate");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				Integer autoClearProssimeOreGiornate = requestBody.get(AgA_General.JN_AutoClearProssimeOreGiornate) != null 
						? Integer.parseInt(requestBody.get( AgA_General.JN_AutoClearProssimeOreGiornate ).toString()) : null;
					Autoveicolo autoveicolo = autoveicoloManager.get(idAutoveicolo);
					JSONObject infoAutoveicoloJson = AgA_General.GetInfoAutoveicoloJson(autoveicolo);
					if(autoClearProssimeOreGiornate != null) { infoAutoveicoloJson.put(AgA_General.JN_AutoClearProssimeOreGiornate, new Integer(autoClearProssimeOreGiornate)); }
					if( autoClearProssimeOreGiornate != null ) {
						autoveicolo.setInfo( infoAutoveicoloJson.toString() );
						autoveicolo = autoveicoloManager.saveAutoveicolo(autoveicolo);
						mainObj = AgA_General.GetValues_AutoClearProssimeOreGiornate(autoveicolo);
						mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
					}else {
						mainObj = AgA_General.GetValues_AutoClearProssimeOreGiornate(autoveicolo);
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
    
    
    @RequestMapping(value="/api_Calendario_Get_AutoClearProssimeOreGiornate", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Calendario_Get_AutoClearProssimeOreGiornate(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Calendario_Get_AutoClearProssimeOreGiornate");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				Autoveicolo autoveicolo = autoveicoloManager.get(idAutoveicolo);
				mainObj = AgA_General.GetValues_AutoClearProssimeOreGiornate(autoveicolo);
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
    
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/api_Calendario_GiornoCalendario", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Calendario_GiornoCalendario(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Calendario_GiornoCalendario");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString();
				Calendario_FrontEnd calendario_FrontEnd = AgA_Calendario.DammiGiornoCalendario(giorno);
				List<Object> listObject = agA_GiornateManager.ListaTariffariCalendario_ListaModelliTariffari(calendario_FrontEnd, idAutoveicolo);
				TabellaCalendario TabellaCalendario = AgA_Giornata.Dammi_TabellaCalendario(calendario_FrontEnd, 
						(List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaAutoveicoloModelloTariffario>) listObject.get(1) );
				List<TabellaModelloGiornata> tabellaModelloGiornataList = agA_GiornateManager.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
				calendario_FrontEnd = AgA_Calendario.InserisciGiornate_in_MeseCalendario(calendario_FrontEnd, TabellaCalendario, tabellaModelloGiornataList);
				mainObj = AgA_Calendario.DammiJSON_Calendario(calendario_FrontEnd, idAutoveicolo);
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
    
    
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api_Calendario_MeseCalendario", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Calendario_MeseCalendario(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Calendario_MeseCalendario");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String meseAnno = requestBody.get(AgA_General.JN_meseAnno).toString();
				Calendario_FrontEnd calendario_FrontEnd = AgA_Calendario.DammiMeseCalendario(meseAnno);
				List<Object> listObject = agA_GiornateManager.ListaTariffariCalendario_ListaModelliTariffari(calendario_FrontEnd, idAutoveicolo);
				TabellaCalendario TabellaCalendario = AgA_Giornata.Dammi_TabellaCalendario(calendario_FrontEnd, 
						(List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaAutoveicoloModelloTariffario>) listObject.get(1) );
				List<TabellaModelloGiornata> tabellaModelloGiornataList = agA_GiornateManager.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
				calendario_FrontEnd = AgA_Calendario.InserisciGiornate_in_MeseCalendario(calendario_FrontEnd, TabellaCalendario, tabellaModelloGiornataList);
				mainObj = AgA_Calendario.DammiJSON_Calendario(calendario_FrontEnd, idAutoveicolo);
				//System.out.println("mainObj: "+mainObj);
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
	 * Ritorna la lista delle voci del Modal al click su una Giornata della tabella Calendario
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api_Calendario_Giornata_Menu", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Calendario_Giornata_Menu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Calendario_Giornata_Menu");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString();
				
				List<Object> listObject = agA_GiornateManager.ListaTariffariGiornata_ListaModelliTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno), idAutoveicolo);
				
				List<TabellaGiornata> tabellaGiornataList = 
						AgA_Giornata.Dammi_TabellaGiornata((List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaAutoveicoloModelloTariffario>) listObject.get(1));
				
				List<TabellaModelloGiornata> tabellaModelloGiornataList = agA_GiornateManager.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
				
				
				List<AgA_AutoveicoloModelliGiornate> agA_AutoveicoloModelliGiornate = agA_AutoveicoloModelliGiornateManager.AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(idAutoveicolo);
				
				mainObj = AgA_Calendario.Dammi_MenuCalendario_Giornata(giorno, tabellaGiornataList, tabellaModelloGiornataList, agA_AutoveicoloModelliGiornate);
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

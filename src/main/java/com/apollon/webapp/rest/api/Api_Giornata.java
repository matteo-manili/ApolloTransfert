package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
import com.apollon.service.AgA_AutoveicoloModelliGiornateManager;
import com.apollon.service.AgA_AutoveicoloModelliTariffariManager;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.service.AgA_ModelliTariffariManager;
import com.apollon.service.AgA_TariffariManager;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.rest.AgA_Tariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
import com.apollon.webapp.rest.JwtTokenUtil;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*") 
public class Api_Giornata extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_Giornata.class);
	
    private AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager;
    @Autowired
    public void setAgA_AutoveicoloModelliGiornateManager(AgA_AutoveicoloModelliGiornateManager agA_AutoveicoloModelliGiornateManager) {
		this.agA_AutoveicoloModelliGiornateManager = agA_AutoveicoloModelliGiornateManager;
	}
    
    private AgA_GiornateManager agA_GiornateManager;
    @Autowired
    public void setAgA_GiornateManager(AgA_GiornateManager agA_GiornateManager) {
		this.agA_GiornateManager = agA_GiornateManager;
	}
	
    private AgA_AutoveicoloModelliTariffariManager agA_AutoveicoloModelliTariffariManager;
    @Autowired
    public void setAgA_AutoveicoloModelliTariffariManager(AgA_AutoveicoloModelliTariffariManager agA_AutoveicoloModelliTariffariManager) {
		this.agA_AutoveicoloModelliTariffariManager = agA_AutoveicoloModelliTariffariManager;
	}
    
    private AgA_ModelliTariffariManager agA_ModelTariffManager;
    @Autowired
    public void setAgA_ModelliTariffariManager(final AgA_ModelliTariffariManager agA_ModelTariffManager) {
        this.agA_ModelTariffManager = agA_ModelTariffManager;
    }
    
    private AgA_TariffariManager agA_TariffariManager;
    @Autowired
    public void setAgA_TariffariManager(AgA_TariffariManager agA_TariffariManager) {
		this.agA_TariffariManager = agA_TariffariManager;
	}

    /**
	 * Ritorna la lista dei Modelli Giornata per il Modal al click di "APPLICA MODELLO GIORNATA"
     */
	@RequestMapping(value="/api_Giornata_ModelliGiornata_Menu", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_ModelliGiornata_Menu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_ModelliGiornata_Menu");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				mainObj = AgA_Giornata.Dammi_MenuModelliGiornata(agA_AutoveicoloModelliGiornateManager.AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(idAutoveicolo));
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
	 * Cancello la Giornata e Tariffari e Copio il Model Giornata e Model Tariffari di un Model Giornata 
     */
	@RequestMapping(value="/api_Giornata_ApplicaModelloGiornata", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_ApplicaModelloGiornata(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_ApplicaModelloGiornata");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString();
				Long idModelloGiornata = Long.parseLong(requestBody.get(AgA_General.JN_idModelloGiornata).toString());
				agA_GiornateManager.EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno), idModelloGiornata, idAutoveicolo);
				return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK).toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
    
	
	/**
	 * Cancello la Giornata e relativi tariffari
     */
	@RequestMapping(value="/api_Giornata_CancellaGiornata", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_CancellaGiornata(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_CancellaGiornata");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString();
				// elimino tutte le Giornate e Tariffari
				agA_GiornateManager.EliminaGiornataListaTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno), idAutoveicolo); 
				return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK).toString());
			}else {
	    		return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_UNAUTHORIZED).toString());
	    	}
	    }catch(Exception ee) {
			ee.printStackTrace();
			return ResponseEntity.ok().body(mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_INTERNAL_SERVER_ERROR).toString());
		}
    }
	
    
    /**
	 * Ritorna la lista delle voci del Modal al click su una GiornataOrario della tabella
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/api_Giornata_GiornataOrario_Menu", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_GiornataOrario_Menu(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_GiornataOrario_Menu");
		JSONObject mainObj = new JSONObject();
	    try {
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString(); String ora = requestBody.get(AgA_General.JN_orario).toString();
				List<Object> listObject = agA_GiornateManager.ListaTariffariGiornataOrario(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), idAutoveicolo);
				mainObj = AgA_Giornata.Dammi_MenuOrarioGiornata_Tariffario(Integer.parseInt(ora), (List<TabellaGiornataTariffario>) listObject.get(0), 
						(List<TabellaAutoveicoloModelloTariffario>) listObject.get(1));
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
	@RequestMapping(value="/api_Giornata_GiornataOrario_ListaGiornata", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_GiornataOrario_ListaGiornata(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_GiornataOrario_ListaGiornata");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString();
				List<Object> listObject = agA_GiornateManager.ListaTariffariGiornata_ListaModelliTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno), idAutoveicolo);
				List<TabellaGiornata> tabellaGiornataList = 
						AgA_Giornata.Dammi_TabellaGiornata((List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaAutoveicoloModelloTariffario>) listObject.get(1));
				JSONArray jArray = new JSONArray();
	    		for(TabellaGiornata tabGiornataIte: tabellaGiornataList) {
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
	 * Se idModelloTariffario è null allora cancellare il ModelloTariffario presente
	 * Possono essere passati solamente ModelliTariffari con almeno un record con eseguiCorse a true
     */
    @RequestMapping(value="/api_Giornata_GiornataOrario_UpdateOrario", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Giornata_GiornataOrario_UpdateOrario(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Giornata_GiornataOrario_UpdateOrario");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString(); String ora = requestBody.get(AgA_General.JN_orario).toString();
				Long idAutoveicoloModelloTariffario = requestBody.get(AgA_General.JN_idModelloTariffario) != null ? 
						Long.parseLong(requestBody.get(AgA_General.JN_idModelloTariffario).toString()) : null ;
				AgA_Giornate agA_Giornate = agA_GiornateManager
						.getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), idAutoveicolo);
				Boolean attivo = requestBody.get(AgA_General.JN_attivo) != null ? (Boolean)requestBody.get(AgA_General.JN_attivo) : false;
				Boolean cancellaOrarioGiornata = requestBody.get(AgA_General.JN_cancellaOrarioGiornata) != null ? 
						(Boolean)requestBody.get(AgA_General.JN_cancellaOrarioGiornata) : false;
						
				if( idAutoveicoloModelloTariffario != null && cancellaOrarioGiornata == false) {
					
					List<AgA_ModelliTariffari> modelTariffari_List = agA_ModelTariffManager.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(idAutoveicoloModelloTariffario);
					if( modelTariffari_List != null && modelTariffari_List.size() > 0 ) {
						attivo = AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(modelTariffari_List) ? attivo : false;
						if( agA_Giornate != null ) {
							// rimuovi tutto il tariffario 
							agA_TariffariManager.removeAgA_Tariffari_by_idGiornata(agA_Giornate.getId());
						}else {
							agA_Giornate = new AgA_Giornate(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), attivo, autoveicoloManager.get(idAutoveicolo));
							agA_Giornate = agA_GiornateManager.saveAgA_Giornate(agA_Giornate);
						}
						
						// li inserisco in tariffari
						if(agA_Giornate != null && AgA_Tariffario
								.Check_TransferAcquistato_TariffeList(agA_TariffariManager.getAgA_Tariffari_by_idGiornata(agA_Giornate.getId())) ) {
							for( AgA_ModelliTariffari ite: modelTariffari_List ) {
								try {
									AgA_Tariffari newAgA_Tariffari = new AgA_Tariffari(ite.getKmCorsa(), ite.isEseguiCorse(), ite.getPrezzoCorsa(), ite.getKmRaggioArea(), agA_Giornate);
									agA_TariffariManager.saveAgA_Tariffari(newAgA_Tariffari);
								}catch (final DataIntegrityViolationException dataIntegrViolException) {
									log.debug("DataIntegrityViolationException");
								}
							}
							
						}else {
							AgA_Tariffari newAgA_Tariffari = new AgA_Tariffari(agA_Giornate, agA_AutoveicoloModelliTariffariManager.get(idAutoveicoloModelloTariffario));
							agA_TariffariManager.saveAgA_Tariffari(newAgA_Tariffari);
						}
						

						agA_Giornate.setAttivo(attivo);
						agA_Giornate = agA_GiornateManager.saveAgA_Giornate(agA_Giornate);
						mainObj.put(AgA_General.JN_giorno, giorno);
						mainObj.put(AgA_General.JN_orario, ora);
						mainObj.put(AgA_General.JN_attivo, attivo);
						mainObj.put(AgA_General.JN_idModelloTariffario, idAutoveicoloModelloTariffario); 
						mainObj.put(AgA_General.JN_nomeModelloTariffario, modelTariffari_List.get(0).getAgA_AutoveicoloModelliTariffari().getNomeTariffario()); 
						mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
					}

				}else if( agA_Giornate != null && cancellaOrarioGiornata == false) {
					List<AgA_Tariffari> tariffariList = agA_TariffariManager.getAgA_Tariffari_by_idGiornata(agA_Giornate.getId());
					attivo = AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(tariffariList) ? attivo : false;
					
					if( attivo == false ) {
						attivo = AgA_Tariffario.Check_TransferAcquistato_TariffeList(tariffariList);
					}


					agA_Giornate.setAttivo(attivo);
					agA_Giornate = agA_GiornateManager.saveAgA_Giornate(agA_Giornate);
					mainObj.put(AgA_General.JN_giorno, giorno);
					mainObj.put(AgA_General.JN_orario, ora);
					mainObj.put(AgA_General.JN_attivo, attivo);
					mainObj.put(AgA_General.JN_idModelloTariffario, JSONObject.NULL); 
					mainObj.put(AgA_General.JN_nomeModelloTariffario, AgA_Giornata.PERSONALIZZATO); 
					mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
				}else if( agA_Giornate != null && cancellaOrarioGiornata == true) {
					boolean TransfertAcquistatoPresente = AgA_Tariffario
							.Check_TransferAcquistato_TariffeList(agA_TariffariManager.getAgA_Tariffari_by_idGiornata(agA_Giornate.getId()));
					if(TransfertAcquistatoPresente == true) {
						agA_TariffariManager.removeAgA_Tariffari_by_idGiornata(agA_Giornate.getId());
						mainObj.put(AgA_General.JN_giorno, giorno);
						mainObj.put(AgA_General.JN_orario, ora);
						mainObj.put(AgA_General.JN_attivo, true);
						mainObj.put(AgA_General.JN_idModelloTariffario, JSONObject.NULL); 
						mainObj.put(AgA_General.JN_nomeModelloTariffario, AgA_Giornata.PERSONALIZZATO); 
						mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
						
					}else if( TransfertAcquistatoPresente == false ) {
						agA_TariffariManager.removeAgA_Tariffari_by_idGiornata(agA_Giornate.getId());
						agA_GiornateManager.removeAgA_Giornate(agA_Giornate.getId());
						mainObj.put(AgA_General.JN_giorno, giorno);
						mainObj.put(AgA_General.JN_orario, ora);
						mainObj.put(AgA_General.JN_attivo, false);
						mainObj.put(AgA_General.JN_idModelloTariffario, JSONObject.NULL); 
						mainObj.put(AgA_General.JN_nomeModelloTariffario, AgA_Giornata.SELEZIONA); 
						mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
					}
					
					
				}else {
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
    
    

}

package com.apollon.webapp.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autoveicolo;
import com.apollon.model.RicercaTransfert;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.service.AgA_TariffariManager;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
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
public class Api_Tariffario extends Api_Base {
	private static final Log log = LogFactory.getLog(Api_Tariffario.class);
	
	private AgA_GiornateManager agA_GiornateManager;
    @Autowired
    public void setAgA_GiornateManager(AgA_GiornateManager agA_GiornateManager) {
		this.agA_GiornateManager = agA_GiornateManager;
	} 
	
    private AgA_TariffariManager agA_TariffariManager;
    @Autowired
    public void setAgA_TariffariManager(AgA_TariffariManager agA_TariffariManager) {
		this.agA_TariffariManager = agA_TariffariManager;
	}
    
    
    @RequestMapping(value ="/api_Tariffario_GiornataOrario_TabellaKilometri", method = RequestMethod.POST)
    public ResponseEntity<String> api_Tariffario_GiornataOrario_TabellaKilometri(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Tariffario_GiornataOrario_TabellaKilometri");
		JSONObject mainObj = new JSONObject();
		try {
	    	if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
	    		log.debug( "requestBody: "+requestBody );
	    		Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString(); String ora = requestBody.get(AgA_General.JN_orario).toString();
				List<AgA_Tariffari> tariffariList = agA_TariffariManager
						.ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), idAutoveicolo);
				JSONArray jArray = new JSONArray();
	    		for(TabellaTariffarioAutista tabTariffIte: AgA_ModelTariffario.InserisciModelloTariffarioAutista_in_TabellaTariffarioAutista(tariffariList)) {
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
    @RequestMapping(value="/api_Tariffario_GiornataOrario_UpdateKmCorsa", method=RequestMethod.POST) 
    public ResponseEntity<String> api_Tariffario_GiornataOrario_UpdateKmCorsa(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
		log.debug("api_Tariffario_GiornataOrario_UpdateKmCorsa");
		JSONObject mainObj = new JSONObject();
	    try{
			if( JwtTokenUtil.Check_e_InfoAutista_Jwt(request.getHeader(Constants.JWT_HEADER_AUTHORIZATION)) != null ) {
				log.debug( "requestBody: "+requestBody );
				Long idAutoveicolo = Long.parseLong(requestBody.get(AgA_General.JN_idAutoveicolo).toString());
				String giorno = requestBody.get(AgA_General.JN_giorno).toString(); String ora = requestBody.get(AgA_General.JN_orario).toString();
				Integer kmCorsaFrom = Integer.parseInt(requestBody.get(AgA_General.JN_fromKm).toString());	
				Integer kmCorsaTo = Integer.parseInt(requestBody.get(AgA_General.JN_toKm).toString());
				Boolean eseguiCorse = requestBody.get(AgA_General.JN_eseguiCorse) != null ? (Boolean)requestBody.get(AgA_General.JN_eseguiCorse) : false;
				BigDecimal prezzo = null; Double kmRaggioArea = null;
				try { prezzo = requestBody.get(AgA_General.JN_prezzo) != null ? new BigDecimal(requestBody.get(AgA_General.JN_prezzo).toString()) : null;
					}catch(Exception ee) { prezzo = null; }
				try { 
					kmRaggioArea = requestBody.get(AgA_General.JN_raggio) != null ? Double.parseDouble(requestBody.get(AgA_General.JN_raggio).toString()) : null;
					Autoveicolo autoveicolo = autoveicoloManager.get(idAutoveicolo);
					kmRaggioArea = AgA_Tariffario.Check_KilometroReggioArea( autoveicolo, kmRaggioArea );
				}catch(Exception ee) { kmRaggioArea = null; }
				eseguiCorse = (prezzo == null || prezzo.compareTo(BigDecimal.ZERO) == 0 || kmRaggioArea == null || kmRaggioArea == 0) ? false : eseguiCorse;
				AgA_Giornate agA_Giornate = agA_GiornateManager
						.getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), idAutoveicolo);
				if( agA_Giornate == null ) {
					agA_Giornate = new AgA_Giornate(AgA_Giornata.convertiDataString_Date_Giorno(giorno, ora), false, autoveicoloManager.get(idAutoveicolo)); 
					agA_Giornate = agA_GiornateManager.saveAgA_Giornate(agA_Giornate);
				}
				
				boolean ricTransfertAcquistatoPresente = false;
				List<AgA_Tariffari> tariffariList = agA_TariffariManager.getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(agA_Giornate.getId(), kmCorsaFrom, kmCorsaTo);

				for(AgA_Tariffari iteTariff: tariffariList ) {
					if(iteTariff.getRicercaTransfertAcquistato() != null) {
						eseguiCorse = iteTariff.isEseguiCorse();
						prezzo = iteTariff.getPrezzoCorsa();
						kmRaggioArea = iteTariff.getKmRaggioArea();
						break;
					}
				}

				for(int ite = kmCorsaFrom; ite <= kmCorsaTo; ite++) {
					//tariffario = agA_TariffariManager.getAgA_Tariffari_by_IdGiornata_e_KmCorsa(agA_Giornate.getId(), ite);
					AgA_Tariffari tariffario = null;
					for(AgA_Tariffari iteTariff: tariffariList ) {
						if(iteTariff.getKmCorsa() == ite) {
							tariffario = iteTariff;
							break;
						}
					}
					if(tariffario != null) {
						if( tariffario.getRicercaTransfertAcquistato() == null) {
							tariffario.setEseguiCorse(eseguiCorse);
							tariffario.setPrezzoCorsa(prezzo);
							tariffario.setKmRaggioArea(kmRaggioArea);
						}
					}else {
						tariffario = new AgA_Tariffari(ite, eseguiCorse, prezzo, kmRaggioArea, agA_Giornate);
					}
					tariffario = agA_TariffariManager.saveAgA_Tariffari(tariffario);
				}
				
				// controlla se almeno un tariffario ha eseguiCorse a true. Altrimenti metti "attivo" di Giornata a false
				List<AgA_Tariffari> tariffariListGiornata = agA_TariffariManager.getAgA_Tariffari_by_idGiornata(agA_Giornate.getId());
				if(AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(tariffariListGiornata) == false) {
					agA_Giornate.setAttivo(false);
					agA_Giornate = agA_GiornateManager.saveAgA_Giornate(agA_Giornate);
				}
				mainObj.put(AgA_General.JN_fromKm, kmCorsaFrom); 
				mainObj.put(AgA_General.JN_toKm, kmCorsaTo); 
				mainObj.put(AgA_General.JN_eseguiCorse, eseguiCorse ); 
				mainObj.put(AgA_General.JN_prezzo, prezzo); 
				mainObj.put(AgA_General.JN_raggio, kmRaggioArea);
				mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_OK);
				
				System.out.println( mainObj );
				
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

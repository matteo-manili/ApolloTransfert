package com.apollon.webapp.rest;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_ModelGiornata extends AgA_General {
	
	public static JSONObject Dammi_MenuOrarioModelGiornata_Tariffario(List<AgA_AutoveicoloModelliTariffari> modelliTariffari_list, AgA_ModelliGiornate modelGiornata) { 
		JSONObject mainObj = new JSONObject();
		if(modelliTariffari_list.size() > 0) {
			JSONArray jArray = new JSONArray();
    		for(AgA_AutoveicoloModelliTariffari ite: modelliTariffari_list) {
    			JSONObject json = new JSONObject();
    			json.put(AgA_General.JN_idModelloTariffario, ite.getId());
    			json.put(AgA_General.JN_nomeModelloTariffario, ite.getNomeTariffario());
    			jArray.put(json);
    		}
    		mainObj.put(AgA_General.JN_listaModelliTariffari, jArray);
		}
		if( modelGiornata != null ) {
			mainObj.put(JN_MenuOrarioGiornata_Text_CancellaTariffario, MENU_CANCELLA_TARIFFARIO);
		}
		if(mainObj.length() == 0) {
			mainObj.put(Constants.JSON_MESSAGE, "Non ci sono Modelli Tariffari Salvati. Crea un Modello Tariffario per applicarlo all'Orario Giornataaaaa.");
			mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NO_CONTENT);
		}
		//log.debug("mainObj: "+mainObj.toString());
		return mainObj;
	}
	
	
	public static List<TabellaGiornata> InserisciModelloGiornataAutista_in_TabellaModelGiornata(List<AgA_ModelliGiornate> modelGiornateList) {
		List<TabellaGiornata> tabellaGiornataAutistaList = Dammi_TabellaGiornata_Vuota();
		for(TabellaGiornata ite_tabellaOrariGiornata: tabellaGiornataAutistaList) {
			for(AgA_ModelliGiornate ite_modelGiornate: modelGiornateList) {
				if (ite_tabellaOrariGiornata.getOrario() == ite_modelGiornate.getOrario() ) {
					ite_tabellaOrariGiornata.setAttivo(ite_modelGiornate.isAttivo());
					ite_tabellaOrariGiornata.setIdTariffario( 
							ite_modelGiornate.getAgA_AutoveicoloModelliTariffari() != null ? ite_modelGiornate.getAgA_AutoveicoloModelliTariffari().getId() : null);
					ite_tabellaOrariGiornata.setTariffarioDesc( 
							ite_modelGiornate.getAgA_AutoveicoloModelliTariffari() != null ? ite_modelGiornate.getAgA_AutoveicoloModelliTariffari().getNomeTariffario() : null);
					break;
				}
			}
		}
		return tabellaGiornataAutistaList;
	}
	
}

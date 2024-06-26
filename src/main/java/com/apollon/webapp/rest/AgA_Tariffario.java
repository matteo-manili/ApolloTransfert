package com.apollon.webapp.rest;

import java.util.List;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autoveicolo;
import com.apollon.webapp.rest.AgA_ModelTariffario.TabellaTariffarioAutista;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_Tariffario extends AgA_General {
	
	public static boolean Check_TransferAcquistato_TariffeList(List<AgA_Tariffari> TariffariList) {
		for(AgA_Tariffari ite: TariffariList ) {
			if( ite.getRicercaTransfertAcquistato() != null ) {
				return true;
			}
		}
		return false;
	}
	
	
	public static Double Check_KilometroReggioArea(Autoveicolo autoveicolo, Double RaggioArea) {
		if( autoveicolo.getAgA_AreaGeografica_Raggio() != null && RaggioArea > autoveicolo.getAgA_AreaGeografica_Raggio() ) {
			return autoveicolo.getAgA_AreaGeografica_Raggio();
		
		}else if( RaggioArea <= AgA_General.RaggioArea_Max ){
			return RaggioArea;
		
		} else {
			return AgA_General.RaggioArea_Max;
		}
	}
	
	
	public static Double Get_KilometroReggioArea(Autoveicolo autoveicolo) {
		if( autoveicolo != null && autoveicolo.getAgA_AreaGeografica_Raggio() != null  ) {
			return autoveicolo.getAgA_AreaGeografica_Raggio();
		}else {
			return RaggioArea_Default;
		}

	}
	
	
	/**
	 * Passando la ListaTariffari (AgA_Tariffari o AgA_ModelliTariffari) di uno orario giornata deve tornarmi true se, 
	 * rispetto ai gruppi di kilometri della TabellaTariffarioAutista, tutti i valori di EseguiCorse sono true.
	 */
	public static boolean Controlla_SePresente_AlmenoUn_EseguiCorseTrue(List<?> tarifList) {
		boolean eseguiCorseGeneral = false;
		for(TabellaTariffarioAutista ite_AAA: AgA_ModelTariffario.Dammi_TabellaTariffarioAutista()) {
			boolean eseguiCorse = false; 
			for(int kilometro = ite_AAA.getFromKm(); kilometro <= ite_AAA.getToKm(); kilometro++ ) {
				for(Object obj_ite: tarifList) {
					if (obj_ite instanceof AgA_Tariffari) {
						AgA_Tariffari tariffari_ite = (AgA_Tariffari) obj_ite;
						if( tariffari_ite.getKmCorsa() == kilometro ) {
							if( tariffari_ite.isEseguiCorse() == false  ) {
								eseguiCorse = false;
								break;
							}else {
								eseguiCorse = true;
							}
						}
					}else if (obj_ite instanceof AgA_ModelliTariffari) {
						AgA_ModelliTariffari tariffari_ite = (AgA_ModelliTariffari) obj_ite;
						if( tariffari_ite.getKmCorsa() == kilometro ) {
							if( tariffari_ite.isEseguiCorse() == false  ) {
								eseguiCorse = false;
								break;
							}else {
								eseguiCorse = true;
							}
						}
					}
				}
				if( eseguiCorse == false ) {
					break;
				}else {
					eseguiCorse = true;
				}
			}
			if( eseguiCorse == true ) {
				eseguiCorseGeneral = true;
				break;
			}else {
				eseguiCorseGeneral = false;
			}
		}
		return eseguiCorseGeneral;
	}
	
	
	
}

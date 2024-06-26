package com.apollon.webapp.util.controller.tariffe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Tariffe;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.bean.Tariffe_Aeroporti_Porti;
import com.apollon.webapp.util.bean.Tariffe_AutoveicoloTariffa;
import com.apollon.webapp.util.bean.Tariffe_AutoveicoloTariffa.CompensoAutistaCorse;
import com.apollon.webapp.util.bean.Tariffe_Zone;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TariffeUtil extends ApplicationUtils{

	private static final Log log = LogFactory.getLog(TariffeUtil.class);
	
	public static List<Tariffe_Zone> getTariffe_Zone_List_New(String[] Parametri, List<AutistaZone> autistaZoneList, List<Autoveicolo> autoveicoloList, String Fasce, int[] kilometriCorse) {
		List<Tariffe_Zone> tariffeZoneList = new ArrayList<Tariffe_Zone>();
		for(AutistaZone autistaZonaITE : autistaZoneList) {
			Tariffe_Zone tariffa_Zona = new Tariffe_Zone();
			tariffa_Zona.setAutistaZona(autistaZonaITE);
			if(autistaZonaITE.isServizioAttivo() || autistaZonaITE.getZoneLungaPercorrenza().isServizioAttivo() || 
					autistaZonaITE.getZoneGiornataCompleta().isServizioAttivo() || autistaZonaITE.getZoneMatrimoni().isServizioAttivo()){
				tariffeZoneList.add(AutoveicoliTariffe_ZONE_NEW(Parametri, autoveicoloList, autistaZonaITE, tariffa_Zona, Fasce, kilometriCorse));
			}
		}
		return tariffeZoneList;
	}
	
	private static Tariffe_Zone AutoveicoliTariffe_ZONE_NEW(String[] Parametri, List<Autoveicolo> autoveicoloList, AutistaZone autistaZoneITE, Tariffe_Zone tariffa_Zona, String Fasce, int[] kilometriCorse ){
		List<Tariffe_AutoveicoloTariffa> autoveicoliTariffeList = new ArrayList<Tariffe_AutoveicoloTariffa>();
		for (Autoveicolo autoveicoloITE : autoveicoloList) {
			if(!autoveicoloITE.isAutoveicoloCancellato()){
				Tariffe_AutoveicoloTariffa autoveicoloTariffa = new Tariffe_AutoveicoloTariffa();
				autoveicoloTariffa.setAutoveicolo(autoveicoloITE);
				if (autistaZoneITE != null){
					if(autistaZoneITE.isServizioAttivo()){
						autoveicoloTariffa = DammiTariffa_ZONE(Parametri, autoveicoloTariffa, autoveicoloITE, autistaZoneITE, Constants.SERVIZIO_STANDARD, Fasce, kilometriCorse);
					}
				}
				autoveicoliTariffeList.add(autoveicoloTariffa);
				tariffa_Zona.setTariffe_AutoveicoliTariffeList(autoveicoliTariffeList);
			}
		}
		return tariffa_Zona;
	}
	
	private static Tariffe_AutoveicoloTariffa DammiTariffa_ZONE(String[] Parametri, Tariffe_AutoveicoloTariffa autoveicoloTariffa, Autoveicolo auto, 
			AutistaZone autistaZoneITE, String tipoServizio, String Fasce, int[] kilometriCorse){
		BigDecimal valoreProvincia;
		if(autistaZoneITE.getProvince() != null){
			valoreProvincia = autistaZoneITE.getProvince().getTariffaBase();
		}else{
			valoreProvincia = new BigDecimal("0");
		}
		if(tipoServizio.equals(Constants.SERVIZIO_STANDARD)) {
			List<CompensoAutistaCorse> compensoAutistaCorseList = new ArrayList<CompensoAutistaCorse>();
			for(int kilometri: kilometriCorse){
				double tariffaKm = DammiTariffa_ValoreAuto(Parametri, auto.getClasseAutoveicoloReale(), valoreProvincia, Fasce, kilometri);
				double compensoAutista = kilometri * tariffaKm;
				CompensoAutistaCorse compensoAutistaTariffa = new CompensoAutistaCorse();
				compensoAutistaTariffa.setCompensoAutista(new BigDecimal(compensoAutista).setScale(2, RoundingMode.HALF_EVEN));
				compensoAutistaTariffa.setKilometri(kilometri);
				compensoAutistaTariffa.setTariffaPerKm(new BigDecimal(tariffaKm).setScale(2, RoundingMode.HALF_EVEN));
				compensoAutistaCorseList.add(compensoAutistaTariffa);
			}
			autoveicoloTariffa.setCompensoAutistaCorse(compensoAutistaCorseList);
		}
		return autoveicoloTariffa;
	}
	
	/**
	 * NUOVO CALCOLO DELLE TARIFFE
	 */
	public static double DammiTariffa_ValoreAuto(String[] Parametri, ClasseAutoveicolo classeAutoveicoloReale, BigDecimal valoreProvincia, String Fasce, int kilometri){
		int TotalePunteggioPercentuale = 0;
		/*
		 * CLASSE AUTOVEICOLO
		 */
		if( classeAutoveicoloReale.getId() == Constants.AUTO_LUXURY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_LUXURY]); // Luxory
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_PRIMA_CLASSE]); // Prima Classe
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_ECONOMY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_ECONOMY]); // Economy
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_PRIMA_CLASSE]); // Van Prima Classe
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_ECONOMY){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_ECONOMY]); // Van Economy
		}
		/*
		 * VALORE PROVINCIA E CALCOLO
		 */
		//System.out.println("--- valoreProvincia.doubleValue(): "+valoreProvincia.doubleValue());
		//System.out.println("--- TotalePunteggioPercentuale: "+TotalePunteggioPercentuale);
		double valoreProvicia_Per_PunteggioTotale = CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(valoreProvincia.doubleValue(), TotalePunteggioPercentuale);
		//System.out.println("--- valoreProvicia_Per_PunteggioTotale: "+valoreProvicia_Per_PunteggioTotale);
		float valoreFascia = GetFascia(GetMapFasce(Fasce), kilometri).getValue();
		//System.out.println("--- valoreFascia: "+valoreFascia);
		return valoreProvicia_Per_PunteggioTotale * valoreFascia;
		//System.out.println("--- result: "+result);
		//return result;
	}
	
	
	private static Entry<Integer, Float> GetFascia(NavigableMap<Integer, Float> map, int kilometri){
		// float value = map.floorEntry(950).getValue(); // ricerca per chiave
		//int kilometri = 66;
		Entry<Integer, Float> Valore = null;
		if(kilometri < map.firstEntry().getKey()) {
			Valore = map.firstEntry();
			return Valore;
		}
		for(Map.Entry<Integer, Float> item : map.entrySet()){
			if(item.getKey() > kilometri){
				Valore = map.lowerEntry(item.getKey());
				break;
			}else if(item.getKey() == kilometri){
				Valore = item;
				break;
			}
			Valore = item;
		}
		//System.out.println(Valore.getKey()+" | "+Valore.getValue());
		return Valore;
	}
	
	
	/**
	 * Serve a settare il valore di una fascia
	 */
	public static NavigableMap<Integer, Float> SetValueKilometroFascia(NavigableMap<Integer, Float> map, int kilometri, Float valoreFascia){
		for(Map.Entry<Integer, Float> item : map.entrySet()){
			if(item.getKey() <= kilometri && map.higherEntry(item.getKey()).getKey() > kilometri ){
				item.setValue(valoreFascia);
			}
		}
		return map;
	}
	
	/**
	 * mi ritornano le fascie kilometriche in formato Map
	 */
	public static NavigableMap<Integer, Float> GetMapFasce(String Fasce){
		NavigableMap<Integer, Float> map = new TreeMap<Integer, Float>();
		StringTokenizer stringTokens = new StringTokenizer(Fasce,"|");
		while(stringTokens.hasMoreTokens()){
			String[] partsFascie = stringTokens.nextToken().split(":");
			String part1 = partsFascie[0]; // 004
			String part2 = partsFascie[1]; // 034556
			//System.out.println(part1+":"+part2);
			map.put(Integer.parseInt(part1), Float.parseFloat(part2));
		}
		return map;
	}
	
	/**
	 * mi ritornano le fascie kilometriche in formato String
	 */
	public static String GetStringMapFasce(NavigableMap<Integer, Float> map){
		String Fasce = "|";
		for(Map.Entry<Integer, Float> item : map.entrySet()){
			Fasce = Fasce + item.getKey().toString() +":"+ item.getValue().toString() +"|";
		}
		//System.out.println( Fasce );
		return Fasce;
	}
	
	
	// ----------------------------------------------- ROBA VECCHIA ---------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------- TARIFFE OLD ----------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * START COMPOSIZIONE PAGINA TARIFFE OLD
	 */
	@Deprecated
	public static List<Tariffe_Zone> getTariffe_Zone_List(List<AutistaZone> autistaZoneList, List<Autoveicolo> autoveicoloList) {
		List<Tariffe_Zone> tariffeZoneList = new ArrayList<Tariffe_Zone>();
		for(AutistaZone autistaZonaITE : autistaZoneList) {
			Tariffe_Zone tariffa_Zona = new Tariffe_Zone();
			tariffa_Zona.setAutistaZona(autistaZonaITE);
			if(autistaZonaITE.isServizioAttivo() || autistaZonaITE.getZoneLungaPercorrenza().isServizioAttivo() || 
					autistaZonaITE.getZoneGiornataCompleta().isServizioAttivo() || autistaZonaITE.getZoneMatrimoni().isServizioAttivo()){
				
				tariffeZoneList.add(AutoveicoliTariffe_ZONE(autoveicoloList, autistaZonaITE, tariffa_Zona));
			}
		}
		return tariffeZoneList;
	}
	
	@Deprecated
	private static Tariffe_Zone AutoveicoliTariffe_ZONE(List<Autoveicolo> autoveicoloList, AutistaZone autistaZoneITE, Tariffe_Zone tariffa_Zona ){
		List<Tariffe_AutoveicoloTariffa> autoveicoliTariffeList = new ArrayList<Tariffe_AutoveicoloTariffa>();
		List<Tariffe> TariffeVar = new ArrayList<Tariffe>(autistaZoneITE.getTariffe());
		for (Autoveicolo autoveicoloITE : autoveicoloList) {
			if(!autoveicoloITE.isAutoveicoloCancellato()){
				Tariffe_AutoveicoloTariffa autoveicoloTariffa = new Tariffe_AutoveicoloTariffa();
				autoveicoloTariffa.setAutoveicolo(autoveicoloITE);
				if (autistaZoneITE != null){
					if(autistaZoneITE.isServizioAttivo()){
						autoveicoloTariffa = DammiTariffaZONE(autoveicoloTariffa, autoveicoloITE.getId(), autistaZoneITE.getId(), Constants.SERVIZIO_STANDARD, TariffeVar);
					}
				}
				autoveicoliTariffeList.add(autoveicoloTariffa);
				tariffa_Zona.setTariffe_AutoveicoliTariffeList(autoveicoliTariffeList);
			}
		}
		return tariffa_Zona;
	}
	
	@Deprecated
	private static Tariffe_AutoveicoloTariffa DammiTariffaZONE(Tariffe_AutoveicoloTariffa autoveicoloTariffa, long idAuto, long idZona, String tipoServizio, List<Tariffe> TariffeVar){
		for (Tariffe tariffeITE : TariffeVar){
			if (tariffeITE.getAutistaZone() != null){
				if (tariffeITE.getAutistaZone().getId() == idZona && tariffeITE.getAutoveicolo().getId() == idAuto && tipoServizio.equals(Constants.SERVIZIO_STANDARD)){
					autoveicoloTariffa.setTariffaST( tariffeITE.getTariffeValori().getTariffaST() );
				}
			}
		}
		return autoveicoloTariffa;
	}
	
	
	
	// ---------------------------------------- ROBA VECCHIA -----------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------
	// --------------------------------------- INFRASTRTTURE -----------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------

	/**
	 * LISTA ZONE INFRASTRUTTURE TERRITORIO 
	 * Mi restuisce le infrastutture scelte dall'utente nella Zona Lavoro
	 */
	@Deprecated
	public static List<Tariffe_Aeroporti_Porti> getTariffe_Aeroporti_Porti_List(List<AutistaAeroporti> autistaAeroporti, 
			List<AutistaPortiNavali> autistaPortiNavali, List<Autoveicolo> autoveicoloVar) {
		log.debug("getTariffe_Aeroporti_Porti_List");
		List<Tariffe_Aeroporti_Porti> tariffeAeroportiPorti = new ArrayList<Tariffe_Aeroporti_Porti>();
		Tariffe_Aeroporti_Porti oggetto = null; 
		List <Tariffe_AutoveicoloTariffa> autoveicoliTariffeList;
		
		// AutistaAeroporti
		for(AutistaAeroporti autistaAeroportiITE : autistaAeroporti) {
			oggetto = new Tariffe_Aeroporti_Porti();
			oggetto.setAutistaAeroporto(autistaAeroportiITE);
			autoveicoliTariffeList = new ArrayList<Tariffe_AutoveicoloTariffa>();
			if(autistaAeroportiITE.isServizioAttivo())
				tariffeAeroportiPorti.add( AutoveicoliTariffe_AEROPORTI( autoveicoliTariffeList, autistaAeroportiITE, oggetto, autoveicoloVar) );
		}
		// AutistaPortiNavali
		if(autistaPortiNavali != null) {
			for(AutistaPortiNavali autistaPortiNavaliITE : autistaPortiNavali) {
				oggetto = new Tariffe_Aeroporti_Porti();
				oggetto.setAutistaPortoNavale(autistaPortiNavaliITE);
				autoveicoliTariffeList = new ArrayList<Tariffe_AutoveicoloTariffa>();
				if(autistaPortiNavaliITE.isServizioAttivo())
					tariffeAeroportiPorti.add( AutoveicoliTariffe_PORTI( autoveicoliTariffeList, autistaPortiNavaliITE, oggetto, autoveicoloVar) );
			}
		}
		return tariffeAeroportiPorti;
	}

	@Deprecated
	private static Tariffe_Aeroporti_Porti AutoveicoliTariffe_AEROPORTI(List <Tariffe_AutoveicoloTariffa> autoveicoliTariffeList, 
			AutistaAeroporti autistaAeroportiITE, Tariffe_Aeroporti_Porti tariffeAeroportiPorti, List<Autoveicolo> autoveicoloVar){
		for(Autoveicolo autoveicoloITE : autoveicoloVar) {
			if(!autoveicoloITE.isAutoveicoloCancellato()){
				Tariffe_AutoveicoloTariffa autoveicoloTariffa = new Tariffe_AutoveicoloTariffa();
				autoveicoloTariffa.setAutoveicolo(autoveicoloITE);
				if(autistaAeroportiITE != null){
					autoveicoloTariffa = DammiTariffa_AERO(autoveicoloTariffa, autoveicoloITE.getId(), autistaAeroportiITE.getId(), Constants.SERVIZIO_AEROPORTO, autistaAeroportiITE);
				}
				autoveicoliTariffeList.add(autoveicoloTariffa);
				tariffeAeroportiPorti.setTariffe_AutoveicoliTariffeList(autoveicoliTariffeList);
			}
		}
		return tariffeAeroportiPorti;
	}
	
	@Deprecated
	private static Tariffe_AutoveicoloTariffa DammiTariffa_AERO(Tariffe_AutoveicoloTariffa autoveicoloTariffa, long idAuto, long idAero, String tipoServizio, AutistaAeroporti autistaAeroportiITE){
		List<Tariffe> TariffeAeroVar = new ArrayList<Tariffe>(autistaAeroportiITE.getTariffe()); 
		for(Tariffe tariffeITE : TariffeAeroVar) {
			if(tariffeITE.getAutistaAeroporti() != null){
				if(tariffeITE.getAutistaAeroporti().getId() == idAero && tariffeITE.getAutoveicolo().getId() == idAuto && tipoServizio.equals(Constants.SERVIZIO_AEROPORTO)){
					autoveicoloTariffa.setTariffaAERO( tariffeITE.getTariffeValori().getTariffaAERO() );
				}
			}
		}
		return autoveicoloTariffa;
	}
	
	@Deprecated
	private static Tariffe_Aeroporti_Porti AutoveicoliTariffe_PORTI(List <Tariffe_AutoveicoloTariffa> autoveicoliTariffeList, 
		AutistaPortiNavali autistaPortiNavaliITE, Tariffe_Aeroporti_Porti tariffeAeroportiPorti, List<Autoveicolo> autoveicoloVar){
		for(Autoveicolo autoveicoloITE : autoveicoloVar) {
			if(!autoveicoloITE.isAutoveicoloCancellato()){
				Tariffe_AutoveicoloTariffa autoveicoloTariffa = new Tariffe_AutoveicoloTariffa();
				autoveicoloTariffa.setAutoveicolo(autoveicoloITE);
				if(autistaPortiNavaliITE != null){
					autoveicoloTariffa = DammiTariffa_PORTO(autoveicoloTariffa, autoveicoloITE.getId(), autistaPortiNavaliITE.getId(), Constants. SERVIZIO_PORTO_NAVALE, autistaPortiNavaliITE);
				}
				autoveicoliTariffeList.add(autoveicoloTariffa);
				tariffeAeroportiPorti.setTariffe_AutoveicoliTariffeList(autoveicoliTariffeList);
			}
		}
		return tariffeAeroportiPorti;
	}
	
	@Deprecated
	private static Tariffe_AutoveicoloTariffa DammiTariffa_PORTO(Tariffe_AutoveicoloTariffa autoveicoloTariffa, long idAuto, long idPorto, String tipoServizio, AutistaPortiNavali autistaPortiNavaliITE){
		List<Tariffe> TariffePortiVar = new ArrayList<Tariffe>(autistaPortiNavaliITE.getTariffe()); 
		for(Tariffe tariffeITE : TariffePortiVar) {
			if(tariffeITE.getAutistaPortiNavali() != null){
				if(tariffeITE.getAutistaPortiNavali() .getId() == idPorto && tariffeITE.getAutoveicolo().getId() == idAuto && tipoServizio.equals(Constants.SERVIZIO_PORTO_NAVALE)){
					autoveicoloTariffa.setTariffaPORTO( tariffeITE.getTariffeValori().getTariffaPORTO() );
				}
			}
		}
		return autoveicoloTariffa;
	}


	/**
	 * sostituito
	 */
	@Deprecated
	public static double DammiTariffa_ValoreAuto_OLD(String[] Parametri, ClasseAutoveicolo classeAutoveicoloReale, BigDecimal valoreProvincia, String tipoServizio, String Fasce, int kilometri){
		//String[] Parametri = DammiParametriCalcoloTariffaAuto();
		//String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		int TotalePunteggioPercentuale = 0;
		/*
		 * TIPO SERVIZIO
		 */
		if(tipoServizio.equals(Constants.SERVIZIO_STANDARD)){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_CORSA_STANDARD]); // TARIFFA STANDARD
		}
		/*
		 * CLASSE AUTOVEICOLO
		 */
		if( classeAutoveicoloReale.getId() == Constants.AUTO_LUXURY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_LUXURY]); // Luxory
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_PRIMA_CLASSE]); // Prima Classe
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_ECONOMY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_ECONOMY]); // Economy
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_PRIMA_CLASSE]); // Van Prima Classe
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_ECONOMY){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_ECONOMY]); // Van Economy
		}
		/*
		 * VALORE PROVINCIA E CALCOLO
		 */
		return CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(valoreProvincia.doubleValue(), TotalePunteggioPercentuale);
	}
	
	/**
	 * sostituito
	 */
	@Deprecated
	public static double DammiTariffa_ValoreAuto_OLD_OLD(String[] Parametri, ClasseAutoveicolo classeAutoveicoloReale, BigDecimal valoreProvincia, String tipoServizio){
		//String[] Parametri = DammiParametriCalcoloTariffaAuto();
		//String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		int TotalePunteggioPercentuale = 0;
		/*
		 * TIPO SERVIZIO
		 */
		if(tipoServizio.equals(Constants.SERVIZIO_STANDARD)){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_CORSA_STANDARD]); // TARIFFA STANDARD
		}
		/*
		 * CLASSE AUTOVEICOLO
		 */
		if(classeAutoveicoloReale.getId() == Constants.AUTO_LUXURY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_LUXURY]); // Luxory
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_PRIMA_CLASSE]); // Prima Classe
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_ECONOMY){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_ECONOMY]); // Economy
			
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_PRIMA_CLASSE){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_PRIMA_CLASSE]); // Van Prima Classe
		
		}else if(classeAutoveicoloReale.getId() == Constants.AUTO_VAN_ECONOMY){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_ECONOMY]); // Van Economy
		}
		/*
		 * VALORE PROVINCIA E CALCOLO
		 */
		double valoreProvicia_Per_PunteggioTotale = CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(valoreProvincia.doubleValue(), TotalePunteggioPercentuale);
		return valoreProvicia_Per_PunteggioTotale;
	}
}

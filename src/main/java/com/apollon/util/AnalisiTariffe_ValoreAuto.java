package com.apollon.util;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.apollon.Constants;
import com.apollon.dao.MarcaAutoScoutDao;
import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.TariffeDao;
import com.apollon.model.Autoveicolo;
import com.apollon.model.Province;
import com.apollon.model.Tariffe;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 *
 */
public class AnalisiTariffe_ValoreAuto extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	public static MarcaAutoScoutDao marcaAutoScoutDao = (MarcaAutoScoutDao) contextDao.getBean("MarcaAutoScoutDao");
	public static ModelloAutoScoutDao modelloAutoScoutDao = (ModelloAutoScoutDao) contextDao.getBean("ModelloAutoScoutDao");
	public static TariffeDao tariffeDao = (TariffeDao) contextDao.getBean("TariffeDao");
	
	
	static public class OggettoTest {
		Integer Azzeccate;
		Double TariffaCiclo;
		long IdProvincia;
		String NomeProvincia;
		public OggettoTest(Integer azzeccate, Double tariffaCiclo, String nomeProvincia, long idProvincia) {
			super();
			TariffaCiclo = tariffaCiclo;
			Azzeccate = azzeccate;
			IdProvincia = idProvincia;
			NomeProvincia = nomeProvincia;
		}
	}
	
	
	private static <K, V extends Comparable<? super V>> Map<K, V> SortByValue(Map<K, V> unsortMap) {
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());
	    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
	        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    });
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
	
	private static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
    }
	
	
	private static boolean TariffaAzzeccata(double result, double tariffa){
		if(result >= tariffa /*&& (result - tariffa <= 0.30)*/ ){
			return true;
		}else if(tariffa >= result && (tariffa - result <= 0.30) ){
			return true;
		}else{
			return false;
		}
	}
	
	
	private static int DammiAnniMaxImmatricPrimaClasse(String[] Parametri){
		int NumeroAnniMaxImmatricPrimaClasse = Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO_PRIMA_CLASSE]);
		return Calendar.getInstance().get(Calendar.YEAR) - NumeroAnniMaxImmatricPrimaClasse;
	}
	
	
	private static double DammiTariffa_ValoreAuto(Autoveicolo auto, Province provincia, double tariffaCiclo, String[] Parametri){
		int TotalePunteggioPercentuale = 0;
		// TARIFFA STANDARD
		if(true){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_CORSA_STANDARD]); // TARIFFA STANDARD
		}
		
		/**
		 * AUTOVEICOLO FINO A 5 POSTI CON AUTISTA
		 */
		if( auto.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId() == 3l 
				&& Integer.parseInt(auto.getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri) ){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_LUXURY]); // Luxory
			
		}else if(auto.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId() == 2l 
				&& Integer.parseInt(auto.getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri)){
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_PRIMA_CLASSE]); // Prima Classe
			
		}else if( auto.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId() == 1l){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_ECONOMY]); // Economy
			
		}else{ // Tutte le altre non Classificate
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_ECONOMY]); // Economy
		}
		
		/*
		 * AUTOVEICOLO VAN FINO A 9 POSTI CON AUTISTA
		 */
		if(auto.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId() == 5l
				&& Integer.parseInt(auto.getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri)){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_PRIMA_CLASSE]); // Van Prima Classe
			
		}else if( auto.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId() == 4l){ 
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_ECONOMY]); // Van Economy
			
		}else{ // Tutte le altre non Classificate
			TotalePunteggioPercentuale = TotalePunteggioPercentuale += Integer.parseInt(Parametri[Constants.PARAM_AUTO_VAN_ECONOMY]); // Van Economy
		}
		if(tariffaCiclo > 0){
			return CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(tariffaCiclo, TotalePunteggioPercentuale);
		}else{
			return CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(provincia.getTariffaBase().doubleValue(), TotalePunteggioPercentuale);
		}
	}
	
	
	private static boolean SuperCondizione(Tariffe tariffe_ite, Province province_ite, String[] Parametri){
		if( tariffe_ite.getAutoveicolo().getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() 
				 && tariffe_ite.getTariffeValori().getTariffaST() != null
					&& tariffe_ite.getAutistaZone().getProvince() != null
						&& tariffe_ite.getAutista().getAutistaDocumento().isApprovatoGenerale() 
							&& tariffe_ite.getAutistaZone().getProvince().getId() == province_ite.getId()
								//&& province_ite.getTariffaBase().compareTo(BigDecimal.ZERO) > 0
								//&& tariffe_ite.getAutistaZone().getProvince().getRegioni().getId() == 19 //Lombardia
								//&& tariffe_ite.getAutistaZone().getProvince().getId() == 97 //Milano
								//&& tariffe_ite.getAutistaZone().getProvince().getId() == 42 //Roma
							
									// AUTO NORMALI LUSSO - 0
									//&& tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 1l
									//&& tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 3l
									//&& Integer.parseInt(tariffe_ite.getAutoveicolo().getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri)
									// AUTO NORMALI PRIMA CLASSE - 50
									//&& tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 1l
									//&& tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 2l
									//&& Integer.parseInt(tariffe_ite.getAutoveicolo().getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri)
									// AUTO NORMALI ECONOMY - 28
									//&& tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 1l
									//&& (tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 1l 
										//|| (tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 2l || tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 3l) 
										//&& Integer.parseInt(tariffe_ite.getAutoveicolo().getAnnoImmatricolazione().trim()) < DammiAnniMaxImmatricPrimaClasse(Parametri))
									// AUTO NORMALI TUTTE - 78
									//&& tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 1l
									//--------------------------------------------------------------------------------------------
									// AUTO VAN PRIMA CLASSE - 28
									//&& (tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 2l || tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 3l)
									//&& tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 5l
									//&& Integer.parseInt(tariffe_ite.getAutoveicolo().getAnnoImmatricolazione().trim()) >= DammiAnniMaxImmatricPrimaClasse(Parametri)
									// AUTO VAN ECONOMY - 39
									//&& (tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 2l || tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 3l)
									//&& (tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 4l
										//|| (tariffe_ite.getAutoveicolo().getModelloAutoScout().getClasseAutoveicolo().getId() == 5l && 
											//Integer.parseInt(tariffe_ite.getAutoveicolo().getAnnoImmatricolazione().trim()) < DammiAnniMaxImmatricPrimaClasse(Parametri)))
									//AUTO VAN TUTTI - 67
									//&& (tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 2l || tariffe_ite.getAutoveicolo().getTipoAutoveicolo().getId() == 3l)
			){	
			return true;
			
		}else{
			return false;
		}
	}
	
	
	public static void StartAnalisi(){
		// ------------------------------------ ELENCO AUTOVEICOLO PER TARIFFA ------------------------------------
		List<Tariffe> tariffeList = tariffeDao.getTariffe_by_OrderProvincia();
		List<Province> provinceList = provinceDao.getProvince();
		String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		List<AnalisiTariffe_ValoreAuto.OggettoTest> OggettoTestList = new ArrayList<AnalisiTariffe_ValoreAuto.OggettoTest>();
		
		//for(int ValoreClasseAuto = 15; ValoreClasseAuto <= 100; ValoreClasseAuto = ValoreClasseAuto + 1){
		int AzzeccateTotali = 0;

			for(Province province_ite : provinceList){
				OggettoTestList = new ArrayList<AnalisiTariffe_ValoreAuto.OggettoTest>();
				
				//for(double tariffaCiclo = 0.001; tariffaCiclo <= 10.0; tariffaCiclo = tariffaCiclo + 0.001){ // <-- Ricorda di mettere 0.001 viene più preciso
					double tariffaCiclo = 0; 
					if(tariffaCiclo == 0){
						tariffaCiclo = province_ite.getTariffaBase().doubleValue();
					}
					int Azzeccate = 0;
					for(Tariffe tariffe_ite : tariffeList){
						if( SuperCondizione(tariffe_ite, province_ite, Parametri) ){
							
							Double resultTariffa = DammiTariffa_ValoreAuto(tariffe_ite.getAutoveicolo(), 
									tariffe_ite.getAutistaZone().getProvince(), tariffaCiclo, Parametri);
							
							if(TariffaAzzeccata(resultTariffa, tariffe_ite.getTariffeValori().getTariffaST().doubleValue())){
								Azzeccate++;
							}
							if(Azzeccate > 0){
								OggettoTestList.add( new AnalisiTariffe_ValoreAuto.OggettoTest(Azzeccate, tariffaCiclo, province_ite.getNomeProvincia(), province_ite.getId()) );
							}
						}
					} // fine Tariffe
				//} // fine TariffeCiclo
				
				Collections.sort( OggettoTestList, new Comparator<OggettoTest>() {
			        public int compare(OggettoTest a, OggettoTest b) {
			        	int c;
			        	Integer valIntA = new Integer(0);
			        	Integer valIntB = new Integer(0);
			        	valIntA = a.Azzeccate;
			        	valIntB = b.Azzeccate;
		                c =  valIntA.compareTo(valIntB);
		                if (c == 0){
			                Double valDoubleA = new Double(0);
		                	Double valDoubleB = new Double(0);
		                	valDoubleA = a.TariffaCiclo;
		                	valDoubleB = b.TariffaCiclo;
			                c =  valDoubleB.compareTo(valDoubleA);
		                }
		                return c;
			        }
			    });
				
				if( OggettoTestList.size() > 0 ){
					AnalisiTariffe_ValoreAuto.OggettoTest e = OggettoTestList.get(OggettoTestList.size() - 1);
					AzzeccateTotali = AzzeccateTotali += e.Azzeccate;
					//Province provincia = provinceDao.get( e.IdProvincia );
					//provincia.setTariffaBase( new BigDecimal(e.TariffaCiclo) );
					//provinceDao.saveProvince(provincia);
				}
			}

		System.out.println("Acceccate Totali: "+AzzeccateTotali /* +" Valore Classe: "+ValoreClasseAuto*/);

		//} // Fine
		
		
		//------------------------------------------------------------------------------
		//------------------------------------------------------------------------------
		//------------------------------------------------------------------------------
		System.out.println("----------------- TOTALE TARIFFE AUTO -------------------");
		Map<String, Integer> MAP_Totali_Tariffe_TOT = new HashMap<String, Integer>(); int Totali = 0;
		for(Province province_ite : provinceList){
			int totaliTariffe = 0;
			for(Tariffe tariffe_ite : tariffeList){
				if( SuperCondizione(tariffe_ite, province_ite, Parametri) ){
					totaliTariffe++;
					Totali++;
					MAP_Totali_Tariffe_TOT.put(tariffe_ite.getAutistaZone().getProvince().getNomeProvincia(), totaliTariffe);
				}
			}
		}
		// ordino per value
		MAP_Totali_Tariffe_TOT = SortByValue(MAP_Totali_Tariffe_TOT);
		printMap(MAP_Totali_Tariffe_TOT);
		System.out.println("Totali: "+Totali);
	}
	
	
	
}

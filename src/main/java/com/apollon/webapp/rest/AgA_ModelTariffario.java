package com.apollon.webapp.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 *
 */
public class AgA_ModelTariffario extends AgA_General {
	

	public static class TabellaTariffarioAutista {
		private Integer fromKm;
		private Integer toKm;
		private Boolean eseguiCorse;
		private String euro;
		private Double raggio;
		
		public TabellaTariffarioAutista(Integer fromKm, Integer toKm) {
			super();
			this.fromKm = fromKm;
			this.toKm = toKm;
			this.eseguiCorse = false;
			this.euro = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString(); //.setScale(2, RoundingMode.HALF_EVEN);
			this.raggio = 0d;
		}
		public Integer getFromKm() {
			return fromKm;
		}
		public void setFromKm(Integer fromKm) {
			this.fromKm = fromKm;
		}
		public Integer getToKm() {
			return toKm;
		}
		public void setToKm(Integer toKm) {
			this.toKm = toKm;
		}
		public String getEuro() {
			return euro;
		}
		public void setEuro(String euro) {
			this.euro = euro;
		}
		public Boolean getEseguiCorse() {
			return eseguiCorse;
		}
		public void setEseguiCorse(Boolean eseguiCorse) {
			this.eseguiCorse = eseguiCorse;
		}
		public Double getRaggio() {
			return raggio;
		}
		public void setRaggio(Double raggio) {
			this.raggio = raggio;
		}
	}
	
	
	/**
	 * Se vengono modificati gli intervalli di TabellaTariffarioAutista, 
	 * quando vengono copiati i valori ModelliTariffari nella TabellaTariffarioAutista i valori non valorizzati (prezzi e raggi e eseguiCorsa) 
	 * dell'intervallo che compongono un blocco di kilomentri, vengono inseriti i valori medi sulla base di quelli di ModelliTariffari.
	 * 
	 * @param modelTarifList
	 * @return
	 * @throws ParseException 
	 */
	public static List<TabellaTariffarioAutista> InserisciModelloTariffarioAutista_in_TabellaTariffarioAutista(List<?> modelTarifList) {
		
		List<TabellaTariffarioAutista> kmTariff_list = Dammi_TabellaTariffarioAutista();
		for(TabellaTariffarioAutista ite_AAA: kmTariff_list) {
			double RangeKilometri = ite_AAA.getToKm() - ite_AAA.getFromKm() + 1;
			int NumeroElementi = 0; double TotEseguiCorse = 0; BigDecimal TotEuro = BigDecimal.ZERO; double TotRaggio = 0d;
			
			boolean RicTransfertAcquistato = false;
			BigDecimal EuroRicTransfertAcquistato = BigDecimal.ZERO; double TotRaggioRicTransfertAcquistato = 0d;
			
			for(Object obj_ite: modelTarifList) {
				if (obj_ite instanceof AgA_ModelliTariffari) {
					AgA_ModelliTariffari ite_modelTarif = (AgA_ModelliTariffari) obj_ite;
					if( ite_modelTarif.getKmCorsa() >= ite_AAA.getFromKm() && ite_modelTarif.getKmCorsa() <= ite_AAA.getToKm() ) {
						TotEseguiCorse = TotEseguiCorse + ( ite_modelTarif.isEseguiCorse() ? 1 : 0 );
						TotEuro = TotEuro.add( ite_modelTarif.getPrezzoCorsa() );
						TotRaggio = TotRaggio + ite_modelTarif.getKmRaggioArea();
						NumeroElementi++;
					}
				}else if (obj_ite instanceof AgA_Tariffari) {
					AgA_Tariffari ite_Tarifari = (AgA_Tariffari) obj_ite;
					if( ite_Tarifari.getKmCorsa() >= ite_AAA.getFromKm() && ite_Tarifari.getKmCorsa() <= ite_AAA.getToKm() ) {
						if( RicTransfertAcquistato == false && ite_Tarifari.getRicercaTransfertAcquistato() != null ) {
							RicTransfertAcquistato = true;
							EuroRicTransfertAcquistato = ite_Tarifari.getPrezzoCorsa();
							TotRaggioRicTransfertAcquistato = ite_Tarifari.getKmRaggioArea();
						}
						TotEseguiCorse = TotEseguiCorse + ( ite_Tarifari.isEseguiCorse() ? 1 : 0 );
						TotEuro = TotEuro.add( ite_Tarifari.getPrezzoCorsa() );
						TotRaggio = TotRaggio + ite_Tarifari.getKmRaggioArea();
						NumeroElementi++;
					}
				}
			}
			
			/*
			for(AgA_ModelliTariffari ite_modelTarif: modelTarifList) {
				if( ite_modelTarif.getKmCorsa() >= ite_AAA.getFromKm() && ite_modelTarif.getKmCorsa() <= ite_AAA.getToKm() ) {
					TotEseguiCorse = TotEseguiCorse + ( ite_modelTarif.isEseguiCorse() ? 1 : 0 );
					TotEuro = TotEuro.add( ite_modelTarif.getPrezzoCorsa() );
					TotRaggio = TotRaggio + ite_modelTarif.getKmRaggioArea();
					NumeroElementi++;
				}
			}
			*/
			
			if(NumeroElementi > 0) {
   				if(TotEseguiCorse > RangeKilometri / 2 || RicTransfertAcquistato) {
   					ite_AAA.setEseguiCorse( true );
				}else {
					ite_AAA.setEseguiCorse( false );
				}
   				
   				if( RicTransfertAcquistato ) {
   					ite_AAA.setEuro( EuroRicTransfertAcquistato.setScale(2, RoundingMode.HALF_EVEN).toString() );
   					ite_AAA.setRaggio( TotRaggioRicTransfertAcquistato  );
   					
   				}else {
   					ite_AAA.setEuro( TotEuro.divide(new BigDecimal(NumeroElementi), BigDecimal.ROUND_HALF_EVEN).toString());
   					ite_AAA.setRaggio( Math.floor(TotRaggio / NumeroElementi)  );
   				}
				

			}
		}
		/* for(Tabella_TariffarioAutista ite_AAA: kmTariff_list) {
			System.out.println("FROM: "+ite_AAA.getFromKm()+" | TO:"+ite_AAA.getToKm()
			+" | ESEGUI_CORSE: "+ite_AAA.getEseguiCorse()+" | PREZZO: "+ite_AAA.getEuro()+" | RAGGIO: "+ite_AAA.getRaggio());
		} */
		return kmTariff_list;
	}
	
	
	/**
	 * Fare molti test dopo aver modificato questo algoritmo, è facile perdersi i kilometri
	 * 
	 * Con questa lista viene definita la tabella Tariffario Autista, 
	 * il valore kilometro e il valore intervallo definiscono un elemento della lista.
	 * Il valore intervallo del primo elemento viene usato fino al successivo elemento... e così il secondo per il terzo... ecc ecc
	 * 
	 * NON ANDARE OLTRE LE 110 RIGHE DI TABELLA TARIFFARI: è SCOMODO PER L'AUTISTA
	 */
	public static List<TabellaTariffarioAutista> Dammi_TabellaTariffarioAutista() {
		
		List<Kilometri_Intervalli> KilometriIntervalli_List_OLD = new ArrayList<Kilometri_Intervalli>(Arrays.asList(
				new Kilometri_Intervalli(1,1)
				,new Kilometri_Intervalli(30,2)
				,new Kilometri_Intervalli(60,3)
				,new Kilometri_Intervalli(200,8)
				,new Kilometri_Intervalli(300,12)
				,new Kilometri_Intervalli(400,18)
				,new Kilometri_Intervalli(500,26)
				,new Kilometri_Intervalli(600,30)
				,new Kilometri_Intervalli(700,34)
				,new Kilometri_Intervalli(800,null)));
		
		List<Kilometri_Intervalli> KilometriIntervalli_List = new ArrayList<Kilometri_Intervalli>(Arrays.asList(
				new Kilometri_Intervalli(1,1)
				,new Kilometri_Intervalli(30,2)
				,new Kilometri_Intervalli(60,3)
				,new Kilometri_Intervalli(100,6)
				,new Kilometri_Intervalli(200,10)
				,new Kilometri_Intervalli(300,15)
				,new Kilometri_Intervalli(400,25)
				,new Kilometri_Intervalli(500,30)
				,new Kilometri_Intervalli(600,null)));
		
		List<TabellaTariffarioAutista> listKFM = new ArrayList<TabellaTariffarioAutista>();
		for(int indexArray = 0; indexArray <= KilometriIntervalli_List.size(); ) {
			if( indexArray >= KilometriIntervalli_List.size() ) {
				break;
			}
			if( indexArray < KilometriIntervalli_List.size()) {
				Integer km_Attuale = null; Integer intervallo_Attuale = null; Integer km_Successivo = null; Integer intervallo_Successivo = null;
				km_Attuale = KilometriIntervalli_List.get(indexArray).getKilometro();
				intervallo_Attuale = KilometriIntervalli_List.get(indexArray).getIntervallo();
				indexArray++;
				if( indexArray < KilometriIntervalli_List.size() ) {
					km_Successivo = KilometriIntervalli_List.get(indexArray).getKilometro(); 
					intervallo_Successivo = KilometriIntervalli_List.get(indexArray).getIntervallo();
				}
				Integer lastToKm = listKFM.size() > 0 ? listKFM.get(listKFM.size()-1).getToKm() : null;
				km_Attuale = lastToKm != null && km_Attuale < lastToKm ? lastToKm : km_Attuale;

				for( int iteKm = ( indexArray == 1 ? km_Attuale : km_Attuale+1 ); iteKm <= (km_Successivo != null?km_Successivo:km_Attuale); iteKm = iteKm + intervallo_Attuale ) {
					Integer iteIntervallo = null;
					if(iteKm < km_Successivo) {
						iteIntervallo = (intervallo_Attuale == 1 ? iteKm : iteKm + intervallo_Attuale);
					}else {
						intervallo_Successivo = intervallo_Successivo != null ? intervallo_Successivo : intervallo_Attuale;
						iteIntervallo = (intervallo_Attuale == 1 ? iteKm : iteKm + intervallo_Successivo);
					}
					TabellaTariffarioAutista kft = new TabellaTariffarioAutista( iteKm, iteIntervallo ); 
					listKFM.add(kft);
					iteKm = (intervallo_Attuale == 1 ? iteKm : iteKm +1);
				}
			}
		}
		/*
		System.out.println("-------------------------------------");
		for(Tabella_TariffarioAutista ite_listKFM: listKFM)	{
			System.out.println(ite_listKFM.getFromKm() +" | "+ite_listKFM.getToKm());
		}
		System.out.println("---------size: "+listKFM.size());
		*/
		return listKFM;
	}
	
	private static class Kilometri_Intervalli {
		private Integer kilometro;
		private Integer intervallo;
		public Kilometri_Intervalli(Integer kilometro, Integer intervallo) {
			super();
			this.kilometro = kilometro;
			this.intervallo = intervallo;
		}
		public Integer getKilometro() {
			return kilometro;
		}
		public Integer getIntervallo() {
			return intervallo;
		}
	}
	
	

}

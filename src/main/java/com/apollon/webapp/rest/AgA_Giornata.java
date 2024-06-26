package com.apollon.webapp.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.util.DateUtil;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd.GiorniMeseCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaCalendario.GiornoCalendario;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_Giornata extends AgA_General {
	private static final Log log = LogFactory.getLog(AgA_Giornata.class);
	
	public static JSONObject Dammi_MenuModelliGiornata(List<AgA_AutoveicoloModelliGiornate> agA_AutoveicoloModelliGiornate) { 
		JSONObject mainObj = new JSONObject();
		JSONArray jArray = new JSONArray();
		if( agA_AutoveicoloModelliGiornate.size() > 0 ) {
			for(AgA_AutoveicoloModelliGiornate ite: agA_AutoveicoloModelliGiornate) {
				JSONObject json = new JSONObject();
				json.put(JN_idModelloGiornata, ite.getId());
				json.put(JN_nomeModelloGiornata, ite.getNomeGiornata());
				jArray.put(json);
			}
			return mainObj.put(JN_listaModelliGiornate, jArray);
		}else {
			mainObj.put(Constants.JSON_STATUS_CODE, AgA_General.JN_SC_NO_CONTENT);
			mainObj.put(Constants.JSON_MESSAGE, "Non ci sono Modelli Giornate Salvati. Crea un Modello Giornata per applicarlo alla Giornata.");
			//log.debug("mainObj: "+mainObj);
			return mainObj;
		}
	}
	
	
	public static JSONObject Dammi_MenuOrarioGiornata_Tariffario(Integer orario, List<TabellaGiornataTariffario> Tabella_List_2, List<TabellaAutoveicoloModelloTariffario> Tabella_List_3) { 
		JSONObject mainObj = new JSONObject();
		mainObj.put(JN_orario, orario);
		mainObj.put(JN_MenuOrarioGiornata_Text_Orario, MENU_ORARIO + DammiOrarioFormatoEsteso(orario));
		mainObj.put(JN_MenuOrarioGiornata_Text_ModificaTariffario, MENU_MODIFICA_TARIFFARIO);
		mainObj.put(JN_MenuOrarioGiornata_Text_CancellaTariffario, MENU_CANCELLA_TARIFFARIO);
		if( Tabella_List_2.isEmpty() == false ) {
			TabellaGiornata tabellaGiornata = Elabora_TabellaGiornata(Dammi_TabellaGiornataOrario_Vuota(orario), Tabella_List_2.get(0), Tabella_List_3);
			mainObj.put(JN_MenuOrarioGiornata_Text_Nometariffario, tabellaGiornata.getTariffarioDesc() );
		}
		JSONArray jArray = new JSONArray();
		for(TabellaAutoveicoloModelloTariffario Tariffario_ite: Tabella_List_3) {
			JSONObject json = new JSONObject();
			json.put(JN_idModelloTariffario, Tariffario_ite.getIdModelloTariffario());
			json.put(JN_nomeModelloTariffario, Tariffario_ite.getNomeTariffario());
			jArray.put(json);
		}
		mainObj.put(JN_listaModelliTariffari, jArray);
		//log.debug("mainObj: "+mainObj.toString());
		return mainObj;
	}
	
	
	public static TabellaCalendario Dammi_TabellaCalendario(Calendario_FrontEnd calendario_FrontEndList, 
			List<TabellaGiornataTariffario> tabella_List_1, List<TabellaAutoveicoloModelloTariffario> Tabella_List_2) {
		
		TabellaCalendario tabellaCalendario = new TabellaCalendario();
		List<GiornoCalendario> giornoCalendarioList = new ArrayList<GiornoCalendario>();
		for(GiorniMeseCalendario ite: calendario_FrontEndList.getGiorniMeseCalendarioList()) {
			GiornoCalendario giornoCalendario = new GiornoCalendario();
			List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = new ArrayList<TabellaGiornataTariffario>();

			LocalDate localDate_ite = ite.getGiorno().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			for( TabellaGiornataTariffario tabella_1: tabella_List_1) {
				LocalDate localDate_tabella_1 = tabella_1.getDataGiornataOrario().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if( localDate_ite.getYear() == localDate_tabella_1.getYear() && localDate_ite.getMonthValue() == localDate_tabella_1.getMonthValue() 
						&& localDate_ite.getDayOfMonth() == localDate_tabella_1.getDayOfMonth() ) {
					tabellaGiornataTariffarioList.add(tabella_1);
				}
			}
			giornoCalendario.setTabellaGiornataList( Dammi_TabellaGiornata(tabellaGiornataTariffarioList, Tabella_List_2) );
			giornoCalendarioList.add(giornoCalendario);
		}
		tabellaCalendario.setGiornoCalendarioList(giornoCalendarioList);
		return tabellaCalendario;
	}
	
	
	public static List<TabellaGiornata> Dammi_TabellaGiornata(List<TabellaGiornataTariffario> Tabella_List_1, List<TabellaAutoveicoloModelloTariffario> Tabella_List_2) {
		List<TabellaGiornata> TabellaGiornata_List = Dammi_TabellaGiornata_Vuota();
		for(TabellaGiornata Tabella_ite: TabellaGiornata_List) {
			for( TabellaGiornataTariffario Tabella_1: Tabella_List_1) {
				if( Tabella_ite.getOrario() == DateUtil.DammiOra_da_Data(Tabella_1.getDataGiornataOrario()) ) {
					Tabella_ite = Elabora_TabellaGiornata(Tabella_ite, Tabella_1, Tabella_List_2);
				}
			}
		}
		/*
		System.out.println("-------------------");
		for(TabellaGiornata Tabella_1: Tabella_List_1) {
			System.out.println("getOrarioFormatEsteso: "+ Tabella_1.getOrarioFormatEsteso()+" | "+"getAttivo: "+ Tabella_1.getAttivo()
			+" | "+"getTariffarioDesc: "+ Tabella_1.getTariffarioDesc()+" | "+"getIdTariffario: "+ Tabella_1.getIdTariffario());
		}
		*/
		return TabellaGiornata_List;
	}
	

	private static TabellaGiornata Elabora_TabellaGiornata(TabellaGiornata Tabella_1, TabellaGiornataTariffario tabella_2, List<TabellaAutoveicoloModelloTariffario> tabella_List_3) {
		boolean tuttoUguale = false;
		for( TabellaAutoveicoloModelloTariffario tabella_3: tabella_List_3) {
			if( tabella_2.getTabellaTariffarioList().get(0).getIdModelloTariffario() != null 
					&& tabella_2.getTabellaTariffarioList().get(0).getIdModelloTariffario().longValue() == tabella_3.getIdModelloTariffario().longValue() ) {
				tuttoUguale = true;
				ModelloTariffarioUguale(Tabella_1, tabella_2, tabella_3);
				break;
			}
		}
		if( tuttoUguale == false ) {
			for( TabellaAutoveicoloModelloTariffario tabella_3: tabella_List_3) {
				if( tabella_2.getTabellaTariffarioList().size() == tabella_3.getTabellaModelloTariffarioList().size()) {
					for(int ite = 0; ite < tabella_2.getTabellaTariffarioList().size(); ite++ ) {
						if( tabella_2.getTabellaTariffarioList().get(ite).getKmCorsa() == tabella_3.getTabellaModelloTariffarioList().get(ite).getKmCorsa()
							&& tabella_2.getTabellaTariffarioList().get(ite).isEseguiCorse() == tabella_3.getTabellaModelloTariffarioList().get(ite).isEseguiCorse()
							&& tabella_2.getTabellaTariffarioList().get(ite).getPrezzoCorsa().equals(tabella_3.getTabellaModelloTariffarioList().get(ite).getPrezzoCorsa())  
							&& tabella_2.getTabellaTariffarioList().get(ite).getKmRaggioArea() == tabella_3.getTabellaModelloTariffarioList().get(ite).getKmRaggioArea() ) {
							tuttoUguale = true;
						
						}else {
							tuttoUguale = false;
							break;
						}
					}
					if( tuttoUguale ) {
						ModelloTariffarioUguale(Tabella_1, tabella_2, tabella_3);
						break;
					}
				}
			}
		}
		if( tuttoUguale == false ) {
			Tabella_1.setTariffarioDesc(AgA_Giornata.PERSONALIZZATO);
			Tabella_1.setAttivo(tabella_2.isAttivo());
			Tabella_1.setIdTariffario(null);
		}
		return Tabella_1;
	}
	
	private static void ModelloTariffarioUguale(TabellaGiornata Tabella_1, TabellaGiornataTariffario tabella_2, TabellaAutoveicoloModelloTariffario tabella_3) {
		Tabella_1.setAttivo(tabella_2.isAttivo());
		Tabella_1.setIdTariffario( tabella_3.getIdModelloTariffario() );
		Tabella_1.setTariffarioDesc( tabella_3.getNomeTariffario() );
	}
	
	
	public static JSONObject Dammi_GiornataOrario_Json(AgA_ModelliGiornate modelGiornata) {
		JSONObject mainObj = new JSONObject();
		mainObj.put(AgA_General.JN_idModelloGiornata, modelGiornata.getId());
		mainObj.put(AgA_General.JN_orario, modelGiornata.getOrario());
		mainObj.put(AgA_General.JN_orarioFormatEsteso, AgA_General.DammiOrarioFormatoEsteso(modelGiornata.getOrario()));
		mainObj.put(AgA_General.JN_attivo, modelGiornata.isAttivo());
		mainObj.put(AgA_General.JN_idModelloTariffario, modelGiornata.getAgA_AutoveicoloModelliTariffari().getId());
		mainObj.put(AgA_General.JN_nomeModelloTariffario, modelGiornata.getAgA_AutoveicoloModelliTariffari().getNomeTariffario());
		return mainObj;
	}
	
	
	public static JSONObject Dammi_GiornataOrario_Vuota_Json(Integer orario) {
		JSONObject mainObj = new JSONObject();
		TabellaGiornata tabellaGiornata = Dammi_GiornataOrario_Vuota(orario);
		mainObj.put(AgA_General.JN_orario, tabellaGiornata.getOrario());
		mainObj.put(AgA_General.JN_orarioFormatEsteso, tabellaGiornata.getOrarioFormatEsteso());
		mainObj.put(AgA_General.JN_attivo, tabellaGiornata.getAttivo());
		mainObj.put(AgA_General.JN_idModelloTariffario, tabellaGiornata.getIdTariffario());
		mainObj.put(AgA_General.JN_nomeModelloTariffario, tabellaGiornata.getTariffarioDesc());
		return mainObj;
	}
	
	
	private static TabellaGiornata Dammi_TabellaGiornataOrario_Vuota(Integer orario) {
		for(Integer ora_ite: Orari_List) {
			if( (orario == null) || (orario != null && ora_ite.intValue() == orario.intValue()) ) {
				return Dammi_GiornataOrario_Vuota(orario);
			}
		}
		return null;
	}
	
	
	public static Date Set_Ora_a_Giorno(Date giorno, int ora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(giorno);
		cal.set(Calendar.HOUR_OF_DAY, ora);
		return cal.getTime();
	}

	
	/**
	 * 	passando: "01/01/2020" <br>
	 *	DataOreZero: 		Wed Jan 01 00:00:00 CET 2020 <br>
	 *	DataDomaniOreZero: 	Thu Jan 02 00:00:00 CET 2020 <br>
	 *	con get 0 prendi il DataOreZero <br>
	 *	con get 1 prendi il DataDomaniOreZero
	 * 
	 * @param giornata
	 * @return List<Date>
	 */
	public static List<Date> DammiDate_DataOreZero_DataDomaniOreZero(Date giornata) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(giornata);
		//System.out.println("giornata: "+cal.getTime());
		Date DataOreZero = cal.getTime();
		cal.setTime(giornata);
		cal.set(Calendar.HOUR_OF_DAY, 24);
		//System.out.println("giornata: "+cal.getTime());
		Date DataDomaniOreZero = cal.getTime();
		List<Date> giorniList = new ArrayList<Date>(Arrays.asList( DataOreZero, DataDomaniOreZero ));
		return giorniList;
	}
	
	
	/**
	 * passare string in questo formato: "01/01/2020 14"
	 */
	public static Date convertiDataString_Date_Giorno(String giorno, String ora) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH");
		Date date = sdf.parse(giorno+" "+ora);
		System.out.println("giornoOra: "+date);
		return date;
	}
	
	
	
}

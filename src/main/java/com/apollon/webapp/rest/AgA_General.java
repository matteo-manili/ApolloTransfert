package com.apollon.webapp.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.apollon.dao.AgA_GiornateDao;
import com.apollon.model.Autoveicolo;
import com.apollon.util.DateUtil;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_General {
	
	public static ApplicationContext contextDao = new ClassPathXmlApplicationContext("App-Database-Spring-Module-Web.xml");
	protected static AgA_GiornateDao agA_GiornateDao = (AgA_GiornateDao) contextDao.getBean("AgA_GiornateDao");
	
	// ----- CODE ERROR ----
	public static final int JN_SC_OK = HttpServletResponse.SC_OK; // esito positivo: {"status_code":200} 
	public static final int JN_SC_CREATED = HttpServletResponse.SC_CREATED; // nuovo elemento creato: {"status_code":201} 
	public static final int JN_SC_NO_CONTENT = HttpServletResponse.SC_NO_CONTENT; // elemento non trovato: {"status_code":204} 
	public static final int JN_SC_NOT_MODIFIED = HttpServletResponse.SC_NOT_MODIFIED; // modifica non eseguita: {"status_code":304}
	public static final int JN_SC_UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED; // non autorizzato: {"status_code":401}
	public static final int JN_SC_INTERNAL_SERVER_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // errore generale: {"status_code":500}
	
	// ----- JWT -----
	public static final String JN_jwtCreated = "jwtCreated";
	public static final String JN_jwtExpired = "jwtExpired";
	
	// ----- AUTISTA -----
	public static final String JN_username = "username";
	public static final String JN_idAutista = "idAutista";
	public static final String JN_fullNameAutista = "fullNameAutista";
	public static final String JN_denominazioneAziendaAutista = "denominazioneAziendaAutista";
	
	// ----- AUTOVEICOLO -----
	public static final String JN_idAutoveicolo = "idAutoveicolo";
	public static final String JN_marcaModelloTarga = "marcaModelloTarga";
	public static final String JN_classeAutoveicolo = "classeAutoveicolo";
	public static final String JN_anno = "anno";
	public static final String JN_marca = "marca";
	public static final String JN_modello = "modello";
	public static final String JN_targa = "targa"; 
	public static final String JN_listaAutoveicoli = "listaAutoveicoli"; 
	public static final String JN_Calendario_GiornoColore = "calendario_GiornoColore";
	
	// ----- MODELLO TARIFFARIO -----
	public static final String JN_idModelloTariffario = "idModelloTariffario";
	public static final String JN_nomeModelloTariffario = "nomeModelloTariffario";
	public static final String JN_idModelloTariffarioRimosso = "idModelloTariffarioRimosso";
	public static final String JN_listaModelliTariffari = "listaModelliTariffari";
	public static final String JN_tabellaTariffario = "tabellaTariffario";
	
	// ----- TARIFFARIO -----
	public static final String JN_fromKm = "fromKm";
	public static final String JN_toKm = "toKm";
	public static final String JN_eseguiCorse = "eseguiCorse";
	public static final String JN_prezzo = "prezzo";
	public static final String JN_raggio = "raggio";
	
	// ----- MODELLO GIORNATA -----
	public static final String JN_idModelloGiornata = "idModelloGiornata";
	public static final String JN_listaModelliGiornate = "listaModelliGiornate";
	public static final String JN_nomeModelloGiornata = "nomeModelloGiornata";
	public static final String JN_idModelloGiornataRimosso = "idModelloGiornataRimosso";
	
	// ----- GIORNATA -----
	public static final String JN_cancellaOrarioGiornata = "cancellaOrarioGiornata";
	public static final String JN_idGiornata = "idGiornata";
	public static final String JN_giorno = "giorno"; // formato: "01/01/2020"
	public static final String JN_tabellaGiornata = "tabellaGiornata";
	public static final String JN_orario = "orario";
	public static final String JN_orarioFormatEsteso = "orarioFormatEsteso";
	public static final String JN_attivo = "attivo";
	public static final String JN_MenuOrarioGiornata_Text_Orario = "menuOrarioGiornata_Text_Orario";
	public static final String JN_MenuOrarioGiornata_Text_Nometariffario = "menuOrarioGiornata_Text_Nometariffario";
	public static final String JN_MenuOrarioGiornata_Text_ModificaTariffario = "menuOrarioGiornata_Text_ModificaTariffario";
	public static final String JN_MenuOrarioGiornata_Text_CancellaTariffario = "menuOrarioGiornata_Text_CancellaTariffario";
	
	// ----- CALENDARIO -----
	public static final String JN_AutoClearProssimeOreGiornate = "autoClearProssimeOreGiornate";
	public static final String JN_AutoClearProssimeOreGiornate_ConvertGiorni = "autoClearProssimeOreGiornate_ConvertGiorni";
	public static final String JN_Calendario_GiorniMeseCalendario = "calendario_GiorniMeseCalendario";
	public static final String JN_meseAnno = "meseAnno"; // formato: "01/2020" (cioè mese/anno)
	public static final String JN_Calendario_NomeMese_Anno = "calendario_NomeMese_Anno";
	public static final String JN_Calendario_NumeroGiorno = "calendario_NumeroGiorno";
	public static final String JN_Calendario_Giorno_Sfondo = "calendario_Giorno_Sfondo";
	public static final String JN_Calendario_Giorno_Text = "calendario_Giorno_Text";
	public static final String JN_Calendario_Giorno_Text_Background = "calendario_Giorno_Text_Background";
	public static final String JN_Calendario_Giorno_Text_Colore = "calendario_Giorno_Text_Colore";
	public static final String JN_MenuCalendario_Text_Giornata = "menuCalendario_Text_Giornata";
	public static final String JN_MenuCalendario_Text_NomeGiornata = "menuCalendario_Text_NomeGiornata";
	public static final String JN_MenuCalendario_Text_ModificaGiornata = "menuCalendario_Text_ModificaGiornata";
	public static final String JN_MenuCalendario_Text_CancellaGiornata = "menuCalendario_Text_CancellaGiornata";
	
	// ----- AREA GEOGRAFICA -----
	public static final String JN_AreaGeog_Lat = "areaGeografica_Lat";
	public static final String JN_AreaGeog_Lng = "areaGeografica_Lng";
	public static final String JN_AreaGeog_Address = "areaGeografica_Address";
	
	// ----- MENU -----
	public static final String JN_Menu_Alert_AreaGeog = "menu_Alert_AreaGeog";
	public static final String JN_Menu_Alert_ModelTariffario = "menu_Alert_ModelTariffario";
	public static final String JN_Menu_Alert_ModelGiornata = "menu_Alert_ModelGiornata";
	public static final String JN_Menu_Address_Raggio_AreaGeog = "menu_Address_Raggio_AreaGeog";
	
	// ----------------------------------------------------------------------------
	
	public static final String SELEZIONA = "SELEZIONA";
	public static final String PERSONALIZZATO = "PERSONALIZZATO";
	public static final String MENU_ORARIO = "ORARIO: ";
	public static final String MENU_MODIFICA_TARIFFARIO = "MODIFICA TARIFFARIO";
	public static final String MENU_CANCELLA_TARIFFARIO = "CANCELLA TARIFFARIO";
	
	public static final String MENU_GIORNATA = "GIORNATA: ";
	public static final String MENU_MODIFICA_GIORNATA = "MODIFICA GIORNATA";
	public static final String MENU_CANCELLA_GIORNATA = "CANCELLA GIORNATA";
	
	public static final String COLORE_GREEN = "GREEN";
	public static final String COLORE_DEFAULT = "DEFAULT";
	
	public static final String COLORE_ESA_SFONDO_GIORNO_DISPONIBILE = "00FF00"; // Lime e 
	public static final String COLORE_ESA_SFONDO_GIORNO_DEFAULT = "FFFFFF"; 	// Bianco
	public static final String COLORE_ESA_TEXT = "000000";						// nero
	public static final String COLORE_ESA_TEXT_BACKGROUNG = "E0E0E0"; 			// grigio chiaro
	
	public static final String ALERT_AREA_GEOG_TEXT = "Crea Area Geografica";
	public static final String ALERT_MODEL_TARIFFARIO_TEXT = "Crea almeno un Modello Tariffario";
	public static final String ALERT_MODEL_GIORNATA_TEXT = "Crea almeno un Modello Giornata";
	
	public final static double Lat = 42.85985981506279; 
	public final static double Lng = 12.54638671875;
	public final static double RaggioArea_Default = 25;
	public final static double RaggioArea_Max = 50;
	public final static int autoClearProssimeOreGiornate = 48; // 96 ore = 4 giorni
	
	public static String ADDRESS_RAGGIO_AREAGEOG(String Address, Double raggio) {
		return Address +" Raggio Area: "+String.valueOf(raggio)+" km";
	}
	
	protected static List<Integer> Orari_List = new ArrayList<Integer>(Arrays.asList(
			new Integer(0),new Integer(1),new Integer(2),new Integer(3),new Integer(4),new Integer(5),new Integer(6),new Integer(7)
			,new Integer(8),new Integer(9),new Integer(10),new Integer(11),new Integer(12),new Integer(13),new Integer(14),new Integer(15)
			,new Integer(16),new Integer(17),new Integer(18),new Integer(19),new Integer(20),new Integer(21),new Integer(22),new Integer(23) ));
	
	
	/**
	 * Mi ritorna la Tabella Giornata coi valori a null. (tranne l'orario)
	 * Oppure se passo anche l'orario mi ritorna solo il record del relativo orario
	 */
	public static List<TabellaGiornata> Dammi_TabellaGiornata_Vuota() {
		List<TabellaGiornata> tableList = new ArrayList<TabellaGiornata>();
		for(Integer ora_ite: Orari_List) {
			TabellaGiornata tabellaGiornataAutista = Dammi_GiornataOrario_Vuota(ora_ite);
			tableList.add(tabellaGiornataAutista);
		}
		return tableList;
	}
	
	public static JSONObject GetValues_AutoClearProssimeOreGiornate(Autoveicolo autoveicolo) {
		JSONObject mainObj = new JSONObject();
		if( autoveicolo != null && autoveicolo.getAutoClearProssimeOreGiornate() != null  ) {
			mainObj.put(AgA_General.JN_AutoClearProssimeOreGiornate, autoveicolo.getAutoClearProssimeOreGiornate());
			mainObj.put(AgA_General.JN_AutoClearProssimeOreGiornate_ConvertGiorni, ConvertiOre_in_Giorno(autoveicolo.getAutoClearProssimeOreGiornate()));
		}else {
			mainObj.put(AgA_General.JN_AutoClearProssimeOreGiornate, autoClearProssimeOreGiornate);
			mainObj.put(AgA_General.JN_AutoClearProssimeOreGiornate_ConvertGiorni, ConvertiOre_in_Giorno(autoClearProssimeOreGiornate));
		}
		return mainObj;
	}
	
	private static String ConvertiOre_in_Giorno(int numeroOre) {
	    BigDecimal bd = BigDecimal.valueOf( new Double(numeroOre)/24 );
	    bd = bd.setScale(1, RoundingMode.HALF_UP);
	    return bd.toString().replace(".", ",") ;
	}
	
	public static JSONObject GetValues_AreaGeografica(Autoveicolo autoveicolo) {
		JSONObject mainObj = new JSONObject();
		mainObj.put(AgA_General.JN_AreaGeog_Lat, autoveicolo.getAgA_AreaGeografica_Lat() != null ? autoveicolo.getAgA_AreaGeografica_Lat() : Lat);
		mainObj.put(AgA_General.JN_AreaGeog_Lng, autoveicolo.getAgA_AreaGeografica_Lng() != null ? autoveicolo.getAgA_AreaGeografica_Lng() : Lng);
		mainObj.put(AgA_General.JN_raggio, autoveicolo.getAgA_AreaGeografica_Raggio() != null ? autoveicolo.getAgA_AreaGeografica_Raggio() : RaggioArea_Default);
		mainObj.put(AgA_General.JN_AreaGeog_Address, autoveicolo.getAgA_AreaGeografica_Address() != null ? autoveicolo.getAgA_AreaGeografica_Address() : JSONObject.NULL);
		return mainObj;
	}
	
	protected static TabellaGiornata Dammi_GiornataOrario_Vuota(Integer orario) {
		return new TabellaGiornata(orario, DammiOrarioFormatoEsteso(orario), false, null, SELEZIONA);
	}
	
	
	public static String DammiOrarioFormatoEsteso(Integer orario) {
		DecimalFormat decimalFormat = new DecimalFormat("00");
		return decimalFormat.format(orario)+":00";
	}
	
	
	/**
	 * passare string in questo formato: "01/01/2020"
	 */
	public static Date convertiDataString_Date_Giorno(String giorno) throws ParseException {
		Date date = DateUtil.FormatoData_1.parse(giorno);
		//System.out.println("giorno: "+date.getTime());
		return date;
	}
	
	
	public static JSONObject GetInfoAutoveicoloJson(Autoveicolo autoveicolo) {
		JSONObject infoAutoveicoloJson = (autoveicolo.getInfo() != null && !autoveicolo.getInfo().contentEquals("") 
				? new JSONObject(autoveicolo.getInfo()) : new JSONObject() );
		return infoAutoveicoloJson;
	}
	
	
}

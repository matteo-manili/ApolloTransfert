package com.apollon.webapp.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.util.DateUtil;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd.GiorniMeseCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_Calendario extends AgA_General {
	private static final Log log = LogFactory.getLog(AgA_Calendario.class);
	
	public static String DammiPrimeTreLettere(String string) {
		if( string.length() >= 4 ) {
			return string.substring(0, 4).toUpperCase();
		}else if( string.length() >= 3 ) {
			return string.substring(0, 3).toUpperCase();
		}else if( string.length() >= 2 ) {
			return string.substring(0, 2).toUpperCase();
		}else {
			return string.substring(0, 1).toUpperCase();
		}
	}
	
	public static Calendario_FrontEnd InserisciGiornate_in_MeseCalendario(Calendario_FrontEnd calendario_FrontEnd, TabellaCalendario tabellaCalendario, 
			List<TabellaModelloGiornata> tabellaModelloGiornataList) throws ParseException {
		for(int giorno_cal_ite = 0; giorno_cal_ite <= calendario_FrontEnd.getGiorniMeseCalendarioList().size() - 1; giorno_cal_ite++ ) {
			List<TabellaGiornata> tabellaGiornataList = tabellaCalendario.getGiornoCalendarioList().get(giorno_cal_ite).getTabellaGiornataList();
			TabellaModelloGiornata modelloGiornata = Dammi_ModelloGiornataApplicato(tabellaGiornataList, tabellaModelloGiornataList);
			if(modelloGiornata != null) {
				SetColore_CalendarioGiorno(calendario_FrontEnd.getGiorniMeseCalendarioList().get(giorno_cal_ite), 
						COLORE_ESA_SFONDO_GIORNO_DEFAULT, modelloGiornata.getNomeGiornata(), COLORE_ESA_TEXT, COLORE_ESA_TEXT_BACKGROUNG);
				for(TabellaGiornata ite_tabellaGiornata: tabellaGiornataList) {
					if( ite_tabellaGiornata.getAttivo() ) {
						SetColore_CalendarioGiorno(calendario_FrontEnd.getGiorniMeseCalendarioList().get(giorno_cal_ite), 
								COLORE_ESA_SFONDO_GIORNO_DISPONIBILE, modelloGiornata.getNomeGiornata(), COLORE_ESA_TEXT, COLORE_ESA_TEXT_BACKGROUNG);
						break;
					}
				}

			}else if( tabellaGiornataList != null && tabellaGiornataList.size() > 0 ) {
				for(TabellaGiornata ite: tabellaGiornataList ) {
					if( !ite.getTariffarioDesc().equals(SELEZIONA) ) {
						SetColore_CalendarioGiorno(calendario_FrontEnd.getGiorniMeseCalendarioList().get(giorno_cal_ite), 
								COLORE_ESA_SFONDO_GIORNO_DEFAULT, PERSONALIZZATO, COLORE_ESA_TEXT, COLORE_ESA_TEXT_BACKGROUNG);
						break;
					}
				}
				for(TabellaGiornata ite_tabellaGiornata: tabellaGiornataList) {
					if( ite_tabellaGiornata.getAttivo() ) {
						SetColore_CalendarioGiorno(calendario_FrontEnd.getGiorniMeseCalendarioList().get(giorno_cal_ite), 
								COLORE_ESA_SFONDO_GIORNO_DISPONIBILE, PERSONALIZZATO, COLORE_ESA_TEXT, COLORE_ESA_TEXT_BACKGROUNG);
						break;
					}
				}
			}
		}
		return calendario_FrontEnd;
	}
	
	
	private static GiorniMeseCalendario SetColore_CalendarioGiorno(GiorniMeseCalendario giorniMeseCalendario, String coloreSfondo, 
			String text, String textColore, String textBackground) {
		giorniMeseCalendario.setColoreSfondo( coloreSfondo ); giorniMeseCalendario.setText( text );
		giorniMeseCalendario.setTextColore( textColore ); giorniMeseCalendario.setTextBackground( textBackground );
		return giorniMeseCalendario;
	}
	
	
	public static JSONObject DammiJSON_Calendario(Calendario_FrontEnd calendario_FrontEnd, Long idAutoveicolo) {
		JSONObject mainObj = new JSONObject();
		mainObj.put(AgA_General.JN_meseAnno, calendario_FrontEnd.getMeseAnno());
		mainObj.put(AgA_General.JN_Calendario_NomeMese_Anno, calendario_FrontEnd.getNomeMese_Anno());
		JSONArray jArray = new JSONArray();
		
		for(GiorniMeseCalendario ite: calendario_FrontEnd.getGiorniMeseCalendarioList()) {
			JSONObject json = new JSONObject();
			//--------------------------
			json.put(AgA_General.JN_Calendario_Giorno_Sfondo, ite.getColoreSfondo());
			json.put(JN_Calendario_Giorno_Text, ite.getText()); 
			json.put(JN_Calendario_Giorno_Text_Colore, ite.getTextColore()); 
			json.put(JN_Calendario_Giorno_Text_Background, ite.getTextBackground());
			//-------------------------
			json.put(AgA_General.JN_giorno, DateUtil.FormatoData_1.format(ite.getGiorno()));
			json.put(AgA_General.JN_Calendario_NumeroGiorno, ite.getNumeroGiorno());
			jArray.put(json);
		}
		mainObj.put(AgA_General.JN_Calendario_GiorniMeseCalendario, jArray);
		return mainObj;
	}
	
	
	public static JSONObject Dammi_MenuCalendario_Giornata(String giorno, List<TabellaGiornata> tabellaGiornataList, List<TabellaModelloGiornata> tabellaModelloGiornataList, 
			List<AgA_AutoveicoloModelliGiornate> agA_AutoveicoloModelliGiornate) throws JSONException, ParseException { 
		JSONObject mainObj = new JSONObject();
		mainObj.put(JN_giorno, giorno);
		mainObj.put(JN_MenuCalendario_Text_Giornata, MENU_GIORNATA + FORMATO_GIORNO_MESE_ESTESO(giorno));
		mainObj.put(JN_MenuCalendario_Text_ModificaGiornata, MENU_MODIFICA_GIORNATA);
		TabellaModelloGiornata modelloGiornata = Dammi_ModelloGiornataApplicato(tabellaGiornataList, tabellaModelloGiornataList);
		if( modelloGiornata != null ) {
			mainObj.put(JN_MenuCalendario_Text_NomeGiornata, modelloGiornata.getNomeGiornata() );
		
		}else if( tabellaGiornataList != null && tabellaGiornataList.size() > 0 ) {
			for(TabellaGiornata ite: tabellaGiornataList ) {
				if( !ite.getTariffarioDesc().equals(SELEZIONA) ) {
					mainObj.put(JN_MenuCalendario_Text_NomeGiornata, PERSONALIZZATO );
					break;
				}
			}
		}
		mainObj.put(JN_MenuCalendario_Text_CancellaGiornata, MENU_CANCELLA_GIORNATA);
		JSONArray jArray = new JSONArray();
		if( agA_AutoveicoloModelliGiornate.size() > 0 ) {
			for(AgA_AutoveicoloModelliGiornate ite: agA_AutoveicoloModelliGiornate) {
				JSONObject json = new JSONObject();
				json.put(JN_idModelloGiornata, ite.getId());
				json.put(JN_nomeModelloGiornata, ite.getNomeGiornata());
				jArray.put(json);
			}
			mainObj.put(JN_listaModelliGiornate, jArray);
		}
		//log.debug("mainObj: "+mainObj.toString());
		return mainObj;
	}
	
	private static TabellaModelloGiornata Dammi_ModelloGiornataApplicato(List<TabellaGiornata> tabellaGiornataList, List<TabellaModelloGiornata> tabellaModelloGiornataList) { 
		// rimuovo gli orari che non hanno il tariffario
		List<TabellaGiornata> tabellaGiornataList_SoloTariffari = new ArrayList<TabellaGiornata>(tabellaGiornataList);
		//List<TabellaGiornata> tabellaGiornataList_SoloTariffari = tabellaGiornataList;
		Iterator<TabellaGiornata> iterator = tabellaGiornataList_SoloTariffari.iterator();
		while(iterator.hasNext()) {
			if( iterator.next().getIdTariffario() == null ) {
				iterator.remove();
			}
		}
		// confronto le giornate con le giornate modello
		for(TabellaModelloGiornata ite_111: tabellaModelloGiornataList) {
			boolean ModelloGiornataUguale = false;
			if( tabellaGiornataList_SoloTariffari.size() == ite_111.getGiornataOrarioTariffarioList().size() ) {
				for( int ite = 0; ite < tabellaGiornataList_SoloTariffari.size(); ite++ ) {
					if( tabellaGiornataList_SoloTariffari.get(ite).getOrario().intValue() == ite_111.getGiornataOrarioTariffarioList().get(ite).getOrario().intValue() 
							&& tabellaGiornataList_SoloTariffari.get(ite).getIdTariffario().longValue() == ite_111.getGiornataOrarioTariffarioList().get(ite).getIdModelloTariffario().longValue()
								&& tabellaGiornataList_SoloTariffari.get(ite).getAttivo().booleanValue() == ite_111.getGiornataOrarioTariffarioList().get(ite).isAttivo()
							) {
						ModelloGiornataUguale = true;
					}else {
						ModelloGiornataUguale = false;
						break;
					}
				}
				if( ModelloGiornataUguale == true ) {
					return ite_111;
				}
			}
		}
		return null;
	}
	
	
	public static Calendario_FrontEnd DammiGiornoCalendario(String giorno) throws ParseException{
		Date giornoDate = convertiDataString_Date_Giorno(giorno);
		Calendar cal = Calendar.getInstance();
		cal.setTime(giornoDate);
		Calendario_FrontEnd tabellaMeseCalendario = Dammi_TabellaMeseCalendario(cal);
	    List<GiorniMeseCalendario> giorniMeseCalendarioList = new ArrayList<GiorniMeseCalendario>();
	    GiorniMeseCalendario giorniMeseCalendario = Dammi_GiornoMeseCalendario_ColoreDefault(cal.getTime(), cal.get(Calendar.DAY_OF_MONTH));
	    giorniMeseCalendarioList.add(giorniMeseCalendario);
	    tabellaMeseCalendario.setGiorniMeseCalendarioList(giorniMeseCalendarioList);
		return tabellaMeseCalendario;
	}
	
	
	/**
	 * passare string in questo formato: "01/2020" (cioè gennaio 2020), altrimenti passare null ritorna il mese e anno corrente
	 * --------------------------------------------------------------------------------------------------------------------------------------------
	 * Ritorna una lista di 42 giorni iniziando dal lunedì precedente del giorno 1 del mese. Se il giorno 1 del mese è lunedì, inizia da questo.
	 * Sono 42 giorni perché 7x6=42. Ogni 7 giorni deve andare a capo per visualizzare il calendario del mese.
	 * --------------------------------------------------------------------------------------------------------------------------------------------
	 *  vedere https://stackoverflow.com/questions/10429331/get-all-days-of-a-current-week/10429716
	 */
	public static Calendario_FrontEnd DammiMeseCalendario(String meseAnno) throws ParseException{
		Date PrimoDelMese = null;
		if(meseAnno != null) {
			PrimoDelMese = convertiDataString_Date_Mese(meseAnno); // "01/2020"
		}else {
			Calendar now = Calendar.getInstance(); 
		    PrimoDelMese = convertiDataString_Date_Mese( Integer.toString(now.get(Calendar.MONTH)+1) +"/"+ Integer.toString(now.get(Calendar.YEAR)) );
		}
		//System.out.println("PrimoDelMese: "+PrimoDelMese);
		Calendar cal = Calendar.getInstance();
		cal.setTime(PrimoDelMese);
		Calendario_FrontEnd tabellaMeseCalendario = Dammi_TabellaMeseCalendario(cal);
	    int delta = -cal.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
	    cal.add(Calendar.DAY_OF_MONTH, delta );
	    List<GiorniMeseCalendario> giorniMeseCalendarioList = new ArrayList<GiorniMeseCalendario>();
	    for (int i = 0; i < 42; i++) {
	    	GiorniMeseCalendario giorniMeseCalendario = Dammi_GiornoMeseCalendario_ColoreDefault(cal.getTime(), cal.get(Calendar.DAY_OF_MONTH));
	    	giorniMeseCalendarioList.add(giorniMeseCalendario);
	    	cal.add(Calendar.DAY_OF_MONTH, 1);
	    }
	    tabellaMeseCalendario.setGiorniMeseCalendarioList(giorniMeseCalendarioList);
	    /*
	    for(GiorniMeseCalendario ite: tabellaMeseCalendario.getGiorniMeseCalendarioList()) {
	    	System.out.println("aaa: "+DateUtil.FormatoData_1.format(ite.getGiorno()) +" | "+ite.getNumeroGiorno()+" | "+ite.getColore());
	    }
	    */
		return tabellaMeseCalendario;
	}
    
	private static Calendario_FrontEnd Dammi_TabellaMeseCalendario(Calendar cal) throws ParseException {
		return new Calendario_FrontEnd(FORMATO_MESE_ESTESO_MAIUSCOLO(cal.getTime())+" "+Integer.toString(cal.get(Calendar.YEAR)), 
				FORMATO_MESE(cal.getTime())+"/"+Integer.toString(cal.get(Calendar.YEAR)), null);
	}
	
	private static GiorniMeseCalendario Dammi_GiornoMeseCalendario_ColoreDefault(Date giorno, Integer numeroGiorno) {
		return new GiorniMeseCalendario(giorno, numeroGiorno, COLORE_ESA_SFONDO_GIORNO_DEFAULT, null, null, null);
	}
	
	
	/**
	 * passare string in questo formato: "01/2020" (gennaio 2020)
	 */
	public static Date convertiDataString_Date_Mese(String giorno) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		Date date = sdf.parse(giorno);
		//System.out.println("giorno: "+date.getTime());
		return date;
	}
	
	
	/**
	 * mi ritorna il mese in questo formato: 01 (cioè gennaio) 
	 * @throws ParseException 
	 */
    public static String FORMATO_MESE(Date giorno) throws ParseException {
    	return new SimpleDateFormat("MM", Constants.Locale_IT).format( giorno ).toUpperCase();

    }
    
	
	/**
	 * mi ritorna il mese in questo formato: DICEMBRE 
	 * @throws ParseException 
	 */
    public static String FORMATO_MESE_ESTESO_MAIUSCOLO(Date giorno) throws ParseException {
    	return new SimpleDateFormat("MMMM", Constants.Locale_IT).format( giorno ).toUpperCase();

    }
    
	
	/**
	 * mi ritorna la data di questo formato: 8 dicembre 
	 * @throws ParseException 
	 */
    public static String FORMATO_GIORNO_MESE_ESTESO(String giorno) throws ParseException {
    	return new SimpleDateFormat("d MMMM", Constants.Locale_IT).format(convertiDataString_Date_Giorno(giorno));

    }
    
    
	/**
	 * TabellaCalendario
	 */
	public static class Calendario_FrontEnd {
		private String nomeMese_Anno;
		private String meseAnno;
		private List<GiorniMeseCalendario> giorniMeseCalendarioList;
		
		public Calendario_FrontEnd(String nomeMese_Anno, String meseAnno, List<GiorniMeseCalendario> giorniMeseCalendarioList) {
			super();
			this.nomeMese_Anno = nomeMese_Anno;
			this.meseAnno = meseAnno;
			this.giorniMeseCalendarioList = giorniMeseCalendarioList;
		}
		
		public String getNomeMese_Anno() {
			return nomeMese_Anno;
		}
		public void setNomeMese_Anno(String nomeMese_Anno) {
			this.nomeMese_Anno = nomeMese_Anno;
		}
		public String getMeseAnno() {
			return meseAnno;
		}
		public void setMeseAnno(String meseAnno) {
			this.meseAnno = meseAnno;
		}
		public List<GiorniMeseCalendario> getGiorniMeseCalendarioList() {
			return giorniMeseCalendarioList;
		}
		public void setGiorniMeseCalendarioList(List<GiorniMeseCalendario> giorniMeseCalendarioList) {
			this.giorniMeseCalendarioList = giorniMeseCalendarioList;
		}

		public static class GiorniMeseCalendario {
			private Date giorno;
			private Integer numeroGiorno;
			private String coloreSfondo;
			private String text;
			private String textColore;
			private String textBackground;
			
			public GiorniMeseCalendario(Date giorno, Integer numeroGiorno, String coloreSfondo, String text, String textColore, String textBackground) {
				super();
				this.giorno = giorno;
				this.numeroGiorno = numeroGiorno;
				this.coloreSfondo = coloreSfondo;
				this.text = text;
				this.textColore = textColore;
				this.textBackground = textBackground;
			}
			
			public Date getGiorno() {
				return giorno;
			}
			public void setGiorno(Date giorno) {
				this.giorno = giorno;
			}
			public Integer getNumeroGiorno() {
				return numeroGiorno;
			}
			public void setNumeroGiorno(Integer numeroGiorno) {
				this.numeroGiorno = numeroGiorno;
			}
			public String getColoreSfondo() {
				return coloreSfondo;
			}
			public void setColoreSfondo(String coloreSfondo) {
				this.coloreSfondo = coloreSfondo;
			}
			public String getText() {
				return text;
			}
			public void setText(String text) {
				this.text = text;
			}
			public String getTextColore() {
				return textColore;
			}
			public void setTextColore(String textColore) {
				this.textColore = textColore;
			}
			public String getTextBackground() {
				return textBackground;
			}
			public void setTextBackground(String textBackground) {
				this.textBackground = textBackground;
			}
		}
	}
}

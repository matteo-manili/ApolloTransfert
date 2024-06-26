package com.apollon.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.apollon.Constants;
import org.springframework.context.i18n.LocaleContextHolder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 *         to correct time pattern. Minutes should be mm not MM (MM is month).
 */
public final class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
    private static final String TIME_PATTERN = "HH:mm";
    
    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private DateUtil() {
    }
    
	/**
	 * Mi ritorna True se una corsa l'orario di prelevamento o l'orario Arrivo(calcolato in base alla durataConTraffico)
	 * si trovano all'interno della fascia oraria MaggiorazioneNotturna 
	 * @return boolean
	 */
    public static boolean MaggiorazioneNotturna(Date dataPrelevamento, Long durataConTraffico, String[] MaggNott_OrarioNutturno){
    	if(dataPrelevamento != null){
	    	Calendar calendarPartenza = Calendar.getInstance();
	    	calendarPartenza.setTime(dataPrelevamento);
	    	calendarPartenza.get(Calendar.HOUR_OF_DAY); 	// gets hour in 24h format
			final int InizioOraNotturno = Integer.parseInt(MaggNott_OrarioNutturno[0]);
			final int FineOraNotturno = Integer.parseInt(MaggNott_OrarioNutturno[1]);
			if(ControlloOrario_Se_FasciaOrariaNotturna(calendarPartenza.get(Calendar.HOUR_OF_DAY), InizioOraNotturno, FineOraNotturno)){
				return true;
			}else{
				Calendar calendarArrivo = Calendar.getInstance();
				calendarArrivo.setTime(dataPrelevamento);
				calendarArrivo.add(Calendar.SECOND,(int)(long)durataConTraffico); // aggiungo i secondi di durataConTraffico alla data di prelevamento
		    	if(ControlloOrario_Se_FasciaOrariaNotturna(calendarArrivo.get(Calendar.HOUR_OF_DAY), InizioOraNotturno, FineOraNotturno)){
					return true;
				}else{
					return false;
				}
			}
    	}else{
    		return false;
    	}
    }
    
    private static boolean ControlloOrario_Se_FasciaOrariaNotturna(int Ora, int InizioOraNotturno, int FineOraNotturno){
    	if( (Ora >= InizioOraNotturno && Ora > FineOraNotturno) || (Ora < InizioOraNotturno && Ora < FineOraNotturno) ){
    		return true;
    	}else{
    		return false;
    	}
    }
    
	public static Date AggiungiOre_a_DataAdesso(int numOre) { 
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.HOUR_OF_DAY, numOre);
	    return cal.getTime();
	}
	
	
	/**
	 * Passando una Data ritorna l'ora ad esempio "01/01/2020 15:30" Ritorna: 15
	 */
	public static int DammiOra_da_Data(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * Passando una Data ritorna la Data con le ore aggiunte
	 */
	public static Date AggiungiOre_a_Data(Date date, int numOre) { 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, numOre);
		return cal.getTime();
	}
	
	/**
	 * Passando una Data ritorna la Data con le ore sottratte
	 */
	public static Date TogliOre_a_Data(Date date, int numOre) { 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, -numOre);
		return cal.getTime();
	}
	

    
    
    /**
     * Mi ritorna la Data di Dopo Domani alle ore 10
     */
    public static Calendar DammiDopoDomaniDaOggi_ore_12(){
    	Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(new Date()); // sets calendar time/date
	    cal.add(Calendar.DATE, 2); // Adding one day
	    cal.set(Calendar.HOUR_OF_DAY, 10);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal;
    }
    
    /**
     * Mi ritorna la Data di Domani
     */
    public static Date TogliSettimaneDaAdesso(int NumeroSettimane){
    	Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(new Date()); // sets calendar time/date
	    cal.add(Calendar.WEEK_OF_YEAR, - NumeroSettimane); // Subtract week day
	    return cal.getTime();
    }
    
    /**
     * Rimuove i Minuti i Secondi e i Millisecondi da una data
     */
    public static Date TogliMinutiSecondiMillisecondi(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
    }
    
    /**
     * FORMATO: dd/MM/yyyy
     */
    final public static SimpleDateFormat FormatoData_1 = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * FORMATO: dd-MM-yyyy
     */
    final public static SimpleDateFormat FormatoData_2 = new SimpleDateFormat("dd-MM-yyyy");
    
    /**
	 * mi ritorna una data di questo formato: 20/12/2016 17:28 
	 */
	public static SimpleDateFormat FORMATO_DATA_ORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
	 * mi ritorna una ora di questo formato: 17:28 
	 */
	public static SimpleDateFormat FORMATO_ORA = new SimpleDateFormat("HH:mm");
	
    /**
	 * mi ritorna una ora di questo formato: 17 
	 */
	public static SimpleDateFormat FORMATO_ORA_SOLO_ORA = new SimpleDateFormat("HH");
	
    /**
	 * mi ritorna una data di questo formato: Dicembre 2019 
	 */
    public static DateFormat FORMATO_MESE_ESTESO_ANNO(Locale locale) {
    	String patternDate = "MMMM yyyy";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }
    
	/**
	 * mi ritorna una data di questo formato: 20 Dicembre 
	 */
    public static DateFormat FORMATO_GIORNO_MESE_ESTESO(Locale locale) {
    	String patternDate = "dd MMMM";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }
	
	/**
	 * mi ritorna una data di questo formato: 20 Dicembre, 17:28 
	 */
    public static DateFormat FORMATO_GIORNO_MESE_ORA_ESTESO(Locale locale) {
    	String patternDate = "dd MMMM, HH:mm";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }
	
    /**
	 * mi ritorna una data di questo formato: 20 Dicembre, 2016 
	 */
    public static DateFormat FORMATO_GIORNO_MESE_ANNO_ESTESO(Locale locale) {
    	String patternDate = "dd MMMM, yyyy";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }
    
	/**
	 * mi ritorna una data di questo formato: 20 Dicembre, 2016 17:28 
	 */
    public static DateFormat FORMATO_GIORNO_MESE_ANNO_ORA_ESTESO(Locale locale) {
    	String patternDate = "dd MMMM, yyyy HH:mm";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }
    
    
    /**
	 * mi ritorna una data di questo formato: domenica 20/12/2016 17:28 
	 */
    public static DateFormat FORMATO_GIORNO_SETTIMANA_DATA_ORA(Locale locale) {
    	String patternDate = "EEEE dd/MM/yyyy HH:mm";
    	if(locale != null){
    		return new SimpleDateFormat(patternDate, locale);
    	}else{
    		return new SimpleDateFormat(patternDate, Constants.Locale_IT);
    	}
    }

    /**
	 * mi ritorna una data di questo formato: domenica
	 */
    public static DateFormat FORMATO_GIORNO_SETTIMANA(Locale locale) {
    	return new SimpleDateFormat("EEEE", locale);
    }

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultDatePattern;
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "MM/dd/yyyy";
        }
        return defaultDatePattern;
    }

    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static String getDate(Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException when String doesn't match the expected format
     * @see java.text.SimpleDateFormat
     */
    public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);
        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException when String doesn't match the expected format
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));
        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate == null) {
            log.warn("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(final String strDate) throws ParseException {
        return convertStringToDate(getDatePattern(), strDate);
    }
}

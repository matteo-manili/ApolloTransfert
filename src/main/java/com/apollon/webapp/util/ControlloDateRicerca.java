package com.apollon.webapp.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.apollon.Constants;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.util.DateUtil;
import com.apollon.util.NumberUtil;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.webapp.util.geogoogle.GMaps_Api;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * Controllo Date Ricerca: Ricerca Customer, Orari prelevamenti autista, controlli che gli orari siano coerenti tra le varie fasi del processo...
 *
 */
public class ControlloDateRicerca extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(ControlloDateRicerca.class);
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	
	private static List<DistanzaMillsCorsaSuccessiva> list_Distanze = new ArrayList<DistanzaMillsCorsaSuccessiva>();
	
	public static Date FormatParseDateRicerca(String dataString, String oraString) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date data = new Date(Long.parseLong(dataString));
		cal.setTime(data);
		String[] parts = oraString.split(":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0].trim()));
		cal.set(Calendar.MINUTE, Integer.parseInt(parts[1].trim()));
		//DateFormat format = new SimpleDateFormat("HH:mm");
		return cal.getTime();
	}
	
	
	/**
	 * Ritorna l'orario massimo fino a cui è possibile applicare lo sconto
	 * 
	 */
	public static Date ControlloDataRitornoOrarioSconto(RicercaTransfert ricTransfert) {
		int numMaxOreAttesaRitorno = (int) (long) gestioneApplicazioneDao.getName("NUMERO_MAX_ORE_ATTESA_RITORNO_PER_SCONTO").getValueNumber();
		return ControlloDataRitornoOrarioSconto(ricTransfert, numMaxOreAttesaRitorno);

	}
	
	public static Date ControlloDataRitornoOrarioSconto(RicercaTransfert ricTransfert, int numMaxOreAttesaRitorno) {
		long datasommata = ricTransfert.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( ricTransfert.getDurataConTrafficoValue() );
		Date dataArrivoPrevisto = new Date( datasommata );
		Calendar cal_DataMaxAttesa = Calendar.getInstance();
		cal_DataMaxAttesa.setTime(dataArrivoPrevisto);
		cal_DataMaxAttesa.add(Calendar.HOUR_OF_DAY, numMaxOreAttesaRitorno);
		return cal_DataMaxAttesa.getTime();
	}
	
	
	/** 
	 * TODO CONTROLLARE FORSE NON SERVE PIù E CAUSA PROBLEMI AI PAGAMENTI CORSE PARTICOLARI
	 * 
	 * DATA DI RICERCA NON DEVE ESSERE PIU' VECCHIA DI 24 ORE DA ADESSO (NEL CASO IN CUI IL CLIENTE LASCIA LA PAGINA APERTA CON LA RICERCA)
	 * bolean true: esito positivo
	 * boolean false: esito negativo
	 * */
	public static boolean ControlloDataRicercercaSuperioreDi24OreDaAdesso(Date dataRicercaTransfert) throws Exception {
		Date date = DateUtil.AggiungiOre_a_Data(dataRicercaTransfert, 24);
		if( date.after(Calendar.getInstance().getTime()) || date.equals(Calendar.getInstance().getTime()) ){
			// chiamata valida
			return true;
		}else{
			return false;
		}
    }
	
	
	/**
	 * Questo valore indica il massimo di ore possibile per disdire una corsa dalla ora di adesso alla ora di prelevamento cliente, 
	 * da parte di un autista Medio e da un Cliente
	 * 
	 * bolean true: esito positivo
	 * boolean false: esito negativo
	 */
	public static boolean ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(Date dataPrelevamento) throws Exception{
		final Integer ORE = (int)(long)gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
		Calendar calendar_Prelevamento = Calendar.getInstance(); calendar_Prelevamento.setTime( dataPrelevamento );
		calendar_Prelevamento.add(Calendar.HOUR_OF_DAY, -ORE);
		if( calendar_Prelevamento.getTime().after(Calendar.getInstance().getTime()) || calendar_Prelevamento.getTime().equals(Calendar.getInstance().getTime()) ){
			//data prelevamento valida
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * LA DATA PRELEVAMENTO (del cliente) DEVE ESSERE DI ALMENO UN ORA SUCCESSIVA DALLA DATA DI ADESSO (PER DARE IL TEMPO ALL'UTISTA DI ARRIVARE)
	 * bolean true: esito positivo
	 * boolean false: esito negativo
	 */
	public static boolean ControlloDataPrelevamentoSuperioreTotOraDaAdesso(Date dataPrelevamento, String tipoCorsa) {
		Integer ORE = 0;
		if(tipoCorsa.equals( Constants.SERVIZIO_AGENDA_AUTISTA )){
			ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber();
		}else if(tipoCorsa.equals( Constants.SERVIZIO_MULTIPLO )){
			ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber();
		}else if(tipoCorsa.equals( Constants.SERVIZIO_PARTICOLARE )){
			ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber();
		}else if(tipoCorsa.equals( Constants.SERVIZIO_STANDARD )){
			ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber();
		}
		Calendar calendar_Adesso = Calendar.getInstance(); 
		calendar_Adesso.add(Calendar.HOUR_OF_DAY, ORE);
		Date Adesso = calendar_Adesso.getTime();
		if( dataPrelevamento.getTime() >= Adesso.getTime() ){
			//data prelevamento valida
			return true;
		}else{
			//data prelevamento NON valida
			return false;
		}
	}
	
	
	/**
	 * LA DATA PRELEVAMENTO DEVE ESSERE INFERIORE ALLA DATA DI ADESSO.
	 * SE RITORNA TRUE SIGNIFICA CHE è LA DATA PRELEVAMENTO è INFERIORE
	 * bolean true: esito positivo
	 * boolean false: esito positivo
	 */
	public static boolean ControlloDataPrelevamentoSuperioreInferioreDaAdesso(Date dataPrelevamento) throws Exception {
		Calendar calendar_Prelevamento = Calendar.getInstance(); 
		calendar_Prelevamento.setTime( dataPrelevamento );
		calendar_Prelevamento.set(Calendar.HOUR_OF_DAY, 0);
		calendar_Prelevamento.set(Calendar.MINUTE, 0);
		calendar_Prelevamento.set(Calendar.SECOND, 0);
		calendar_Prelevamento.set(Calendar.MILLISECOND, 0);
		Calendar calendar_oggi = Calendar.getInstance(); 
		calendar_oggi.set(Calendar.HOUR_OF_DAY, 0);
		calendar_oggi.set(Calendar.MINUTE, 0);
		calendar_oggi.set(Calendar.SECOND, 0);
		calendar_oggi.set(Calendar.MILLISECOND, 0);
		if( calendar_Prelevamento.getTime().before(calendar_oggi.getTime()) ){
			//data prelevamento non valida
			return true;
		}else{
			//data prelevamento valida
			return false;
		}
	}
	
	
	public static boolean ControlloDataPrelevamentoSuperioreInferioreDaAdessoTime(Date dataPrelevamento) throws Exception{
		Calendar calendar_Prelevamento = Calendar.getInstance(); calendar_Prelevamento.setTime( dataPrelevamento );
		if( calendar_Prelevamento.getTime().before(Calendar.getInstance().getTime()) ){
			//data prelevamento non valida
			return true;
		}else{
			//data prelevamento valida
			return false;
		}
	}
	
	
	/**
	 * mi restitusce il tempo di percorrenza in millesimi tra una distanza ed un altra con un più aggiunto un margine di tempo 
	 * gestito nel pannello amministratore.
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	private static long DammiDistanzaMillisCorsaSuccessiva(double latPartenza, double lngPartenza, double latArrivo, double lngArrivo, Date oraInizioCorsa) throws JSONException, NullPointerException, Exception{
		final long MargineMinuti = gestioneApplicazioneDao.getName("MARGINE_MINUTI_TRA_UNA_CORSA_E_ALTRA_AUTISTA").getValueNumber();
		GMaps_Api GMaps_Api = new GMaps_Api();
		long durataSecondiTempoPerArrivareACorsaSuccessiva = GMaps_Api.GoogleMaps_DistanceMatrixDurata(latPartenza, lngPartenza, 
				latArrivo, lngArrivo, oraInizioCorsa);
		long sommaTotaleMillis = TimeUnit.SECONDS.toMillis( durataSecondiTempoPerArrivareACorsaSuccessiva ) + TimeUnit.MINUTES.toMillis( MargineMinuti );
		log.debug( "latPartenza:"+latPartenza+" | lngPartenza:"+lngPartenza+" | latArrivo:"+latArrivo+" | lngArrivo:"+lngArrivo +" | oraInizioCorsa:"+ oraInizioCorsa );
		return sommaTotaleMillis;
	}
	
	
	private static class DistanzaMillsCorsaSuccessiva {
		public DistanzaMillsCorsaSuccessiva(double lat_A, double lng_A, double lat_B, double lng_B,
				Date oraInizioCorsa, long result) {
			this.lat_A = lat_A;
			this.lng_A = lng_A;
			this.lat_B = lat_B;
			this.lng_B = lng_B;
			this.oraInizioCorsa = oraInizioCorsa;
			this.result = result;
		}
		private double lat_A;
		private double lng_A; 
		private double lat_B;
		private double lng_B; 
		private Date oraInizioCorsa;
		private long result;
		public double getLat_A() {
			return lat_A;
		}
		public double getLng_A() {
			return lng_A;
		}
		public double getLat_B() {
			return lat_B;
		}
		public double getLng_B() {
			return lng_B;
		}
		public Date getOraInizioCorsa() {
			return oraInizioCorsa;
		}
		public long getResult() {
			return result;
		}
	}
	
	public Long CheckExistDistanza_Exist(List<DistanzaMillsCorsaSuccessiva> list_Distanze, double lat_A, double lng_A, 
			double lat_B, double lng_B, Date oraInizioCorsa) {
		for(DistanzaMillsCorsaSuccessiva ite: list_Distanze) {
			if( ite.getLat_A() == lat_A && ite.getLng_A() == lng_A 
					&& ite.getLat_B() == lat_B && ite.getLng_B() == lng_B && ite.getOraInizioCorsa().getTime() == oraInizioCorsa.getTime() ) {
				return ite.getResult();
			}
		}
		return null;
	}
	
	@Deprecated
	public static boolean ControlloAutistaCorseSovrapposte_OLD(long idRic, long AustistaID, RicercaTransfert CorsaTest) throws JSONException, NullPointerException, Exception{
		RicercaTransfert corsaComparata = ricercaTransfertDao.get(idRic);

		//---------------------------CONTROLLO 1 - 1
		if(CorsaTest.getDataOraPrelevamentoDate().getTime() < corsaComparata.getDataOraPrelevamentoDate().getTime() ){
			long datasommata = CorsaTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommata );
			long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
					corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
			dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
			if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraPrelevamentoDate().getTime()){
				return false;
			}
		}else{
			long datasommata = corsaComparata.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommata );
			long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
					CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
			dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
			if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraPrelevamentoDate().getTime()){
				return false;
			}
		}
		//---------------------------CONTROLLO 1 - 2
		if(corsaComparata.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraPrelevamentoDate().getTime() < corsaComparata.getDataOraRitornoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValue() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraRitornoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraPrelevamentoDate().getTime()){
					return false;
				}
			}
		}
		
		//---------------------------CONTROLLO 2 - 1
		if(CorsaTest.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraRitornoDate().getTime() < corsaComparata.getDataOraPrelevamentoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraPrelevamentoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValue() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraRitornoDate().getTime()){
					return false;
				}
			}
		}
		//---------------------------CONTROLLO 2 - 2
		if(CorsaTest.getDataOraRitornoDate() != null && corsaComparata.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraRitornoDate().getTime() < corsaComparata.getDataOraRitornoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraRitornoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() +  DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraRitornoDate().getTime()){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean ControlloAutistaCorseSovrapposte(long idRic, long AustistaID, RicercaTransfert CorsaTest) throws JSONException, NullPointerException, Exception{
		RicercaTransfert corsaComparata = ricercaTransfertDao.get(idRic); Long DistanzaMillis = null;
		
		//---------------------------CONTROLLO 1 - 1
		if(CorsaTest.getDataOraPrelevamentoDate().getTime() < corsaComparata.getDataOraPrelevamentoDate().getTime() ){
			long datasommata = CorsaTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommata );
			/*
			 long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
					corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
			 */
			if( CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
					corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto) != null ) {
				DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
			}else {
				DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
				DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto, DistanzaMillis);
				list_Distanze.add( dist );
			}
			dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
			if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraPrelevamentoDate().getTime()){
				return false;
			}
		}else{
			long datasommata = corsaComparata.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommata );
			/*
			long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
					CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
			*/
			if( CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
					CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto) != null ) {
				DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
			}else {
				DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
				DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto, DistanzaMillis);
				list_Distanze.add( dist );
			}
			dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
			if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraPrelevamentoDate().getTime()){
				return false;
			}
		}
		//---------------------------CONTROLLO 1 - 2
		if(corsaComparata.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraPrelevamentoDate().getTime() < corsaComparata.getDataOraRitornoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValue() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto) != null ) {
					
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraRitornoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto) != null ) {
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraPrelevamentoDate().getTime()){
					return false;
				}
			}
		}
		
		//---------------------------CONTROLLO 2 - 1
		if(CorsaTest.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraRitornoDate().getTime() < corsaComparata.getDataOraPrelevamentoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto) != null ) {
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraPrelevamentoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraPrelevamentoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValue() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto) != null ) {
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraRitornoDate().getTime()){
					return false;
				}
			}
		}
		//---------------------------CONTROLLO 2 - 2
		if(CorsaTest.getDataOraRitornoDate() != null && corsaComparata.getDataOraRitornoDate() != null){
			if(CorsaTest.getDataOraRitornoDate().getTime() < corsaComparata.getDataOraRitornoDate().getTime() ){
				long datasommata = CorsaTest.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( CorsaTest.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
						corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto) != null ) {
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(CorsaTest.getLat_Partenza(), CorsaTest.getLng_Partenza(), 
							corsaComparata.getLat_Arrivo(), corsaComparata.getLng_Arrivo(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > corsaComparata.getDataOraRitornoDate().getTime()){
					return false;
				}
			}else{
				long datasommata = corsaComparata.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( corsaComparata.getDurataConTrafficoValueRitorno() );
				Date dataArrivoPrevisto = new Date( datasommata );
				/*
				long DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				*/
				if( CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
						CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto) != null ) {
					DistanzaMillis = CheckExistDistanza_Exist(list_Distanze, corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
				}else {
					DistanzaMillis = DammiDistanzaMillisCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto);
					DistanzaMillsCorsaSuccessiva dist = new DistanzaMillsCorsaSuccessiva(corsaComparata.getLat_Partenza(), corsaComparata.getLng_Partenza(), 
							CorsaTest.getLat_Arrivo(), CorsaTest.getLng_Arrivo(), dataArrivoPrevisto, DistanzaMillis);
					list_Distanze.add( dist );
				}
				dataArrivoPrevisto = new Date( dataArrivoPrevisto.getTime() + DistanzaMillis );
				if(dataArrivoPrevisto.getTime() > CorsaTest.getDataOraRitornoDate().getTime()){
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * qui devo verificare se l'autista è occupato in altre corse che interferiscono con le date della corsa ricercata 
	 * e quindi l'autista deve escuderlo dagli autisti offerti
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException 
	 */
	public boolean ControlloAutistaCorseSovrapposte_MAIN(RicercaTransfert CorsaTest, long AustistaID) throws JSONException, NullPointerException, Exception {
		log.debug("ControlloAutistaCorseSovrapposte_MAIN");
		boolean AutistaCorsaDisponibile = true;
		int numErrorsCount = 0;
		List<RichiestaMediaAutista> listRichiestaAutistaMedio = ricercaTransfertDao.getCorseAutistaMedioConfermate_DateRange(AustistaID, CorsaTest, 12);
		List<RichiestaAutistaParticolare> listRichiestaAutistaParticolare = ricercaTransfertDao.getCorseAutistaParticolareConfermate_DateRange(AustistaID, CorsaTest, 12);
		List<RicercaTransfert> listCorseAutisti = new ArrayList<RicercaTransfert>();
		
		for(RichiestaMediaAutista listRichiestaAutistaMedio_ite: listRichiestaAutistaMedio){
			listCorseAutisti.add( listRichiestaAutistaMedio_ite.getRichiestaMedia().getRicercaTransfert() );
		}
		
		for(RichiestaAutistaParticolare listRichiestaAutistaParticolare_ite: listRichiestaAutistaParticolare){
			listCorseAutisti.add( listRichiestaAutistaParticolare_ite.getRicercaTransfert() );
		}
		
		List<Long> listCorseAutistiLong = new ArrayList<Long>();
		for(RicercaTransfert listCorseAutisti_ite : listCorseAutisti){
			listCorseAutistiLong.add( listCorseAutisti_ite.getId() );
		}
		
		listCorseAutistiLong = NumberUtil.removeDuplicatesLong( listCorseAutistiLong );
		
		for(Long listCorseAutistiLong_ite: listCorseAutistiLong){
			boolean esitoTest = true;
			long idRic = listCorseAutistiLong_ite;
			// aggiungere alla data prelevamento durata di tempo per arrivare a destinazione
			esitoTest = ControlloAutistaCorseSovrapposte(idRic, AustistaID, CorsaTest);
			if(esitoTest == false){
				numErrorsCount ++;
			}
		}
		if(numErrorsCount > 0){
			AutistaCorsaDisponibile = false;
		}
		
		return AutistaCorsaDisponibile;
	}
	
	
    

}

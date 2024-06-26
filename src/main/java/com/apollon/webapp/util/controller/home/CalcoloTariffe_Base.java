package com.apollon.webapp.util.controller.home;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import com.apollon.Constants;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.dao.ComuniDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.MessaggioEsitoRicerca;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultAgendaAutista;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class CalcoloTariffe_Base {
	private static final Log log = LogFactory.getLog(CalcoloTariffe_Base.class);
	protected static ApplicationContext contextDao = ApplicationUtils.contextDao;
	protected static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	protected static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	protected static ComuniDao comuniDao = (ComuniDao) contextDao.getBean("ComuniDao");
	protected static ClasseAutoveicoloDao classeAutoveicoloDao = (ClasseAutoveicoloDao) contextDao.getBean("ClasseAutoveicoloDao");
	protected static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	
	protected Date DATA_PRELEVAMENTO = null;
	protected Long DURATA_TRAFFICO_ANDATA = null ;
	protected Date DATA_RITORNO = null;
	protected Long DURATA_TRAFFICO_RITORNO = null;
	protected long totaleMetriSoloAndata ; 
	protected long totaleMetriSoloRitorno ;
	protected long totaleMetri_Andata_e_Ritorno ;
	protected int kilometri;
	protected int NumeroPasseggeri;
	protected List<MessaggioEsitoRicerca> MessaggiEsitoRicerca;
	protected boolean ScontoAttesaRitorno;
	
	// RICERCA AUTISTI TERRITORIO
	protected List<ResultRicercaAutista> resultRicercaAutistaList;
	//protected List<Long> AutistiDisponibiliList;
	protected int NumMinimoAutistiCorsaMedia = 0;
	protected String[] MaggNott_OrarioNutturno = null ;
	protected String[] Parametri = null;
	protected int MaggNott_Percentuale = 0;
	protected long PercentualeServizio = 0;
	protected int PercentualeVenditore = 0;
	

	protected ResultRicerca_Autista_Tariffe CalcolaAgendaAutista(RicercaTransfert ricTransfert, ResultRicerca_Autista_Tariffe resultRicerca_Autista_Tariffe, List<Long> AutistiDisponibiliList) {
		ResultAgendaAutista resultAgendaAutista =  new ResultAgendaAutista();
		if( gestioneApplicazioneDao.getName("CORSE_AGENDA_AUTISTA").getValueNumber() == 1l && AutistiDisponibiliList.size() >= 1 ) {
			List<AgendaAutista_Autista> agendaAutistaA_AndataList = autoveicoloDao.Result_AgendaAutista(PercentualeServizio, ricTransfert.getNumeroPasseggeri(), 
					ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDistanzaValue(), ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza(), 
					ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo());
			resultAgendaAutista.setAgendaAutista_AutistaAndata(agendaAutistaA_AndataList);
			if(ricTransfert.isRitorno()) {
				List<AgendaAutista_Autista> agendaAutistaA_RitornoList = autoveicoloDao.Result_AgendaAutista(PercentualeServizio, ricTransfert.getNumeroPasseggeri(), 
						ricTransfert.getDataOraRitornoDate(), ricTransfert.getDistanzaValueRitorno(), ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo(), 
						ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza());
				resultAgendaAutista.setAgendaAutista_AutistaRitorno(agendaAutistaA_RitornoList);
			}
		}
		resultRicerca_Autista_Tariffe.setResultAgendaAutista(resultAgendaAutista);
		return resultRicerca_Autista_Tariffe;
	}
	
	
	protected ResultRicerca_Autista_Tariffe CalcolaParticolare(RicercaTransfert ricTransfert, ResultRicerca_Autista_Tariffe resultRicerca_Autista_Tariffe, long idComPartenza, 
			long idProvPartenza , long idRegPartenza, long idComArrivo, long idProvArrivo, long idRegArrivo, List<Long> InfraProvincie, List<Long> AutistiDisponibiliList) 
					throws JSONException, NullPointerException, Exception {
		resultRicerca_Autista_Tariffe.setTipoServizio(Constants.SERVIZIO_PARTICOLARE);
		if( gestioneApplicazioneDao.getName("CORSE_PARTICOLARI").getValueNumber() == 1l && AutistiDisponibiliList.size() >= 1 ) {
			
		// SVILUPPO PER PARTICOLARE MULTIPLO 
		}else if( AutistiDisponibiliList.size() == 0 ) {
			resultRicercaAutistaList.clear();
			resultRicercaAutistaList.addAll( RicercaAutisti(idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, idProvArrivo, idRegArrivo, InfraProvincie, 1, false, 
					ricTransfert.isRitorno(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDataOraRitornoDate()) );
			AutistiDisponibiliList = getListaAutistiDisponibili(resultRicercaAutistaList, ricTransfert);
			if( AutistiDisponibiliList.size() > 0 ) {
				return Elaborazione_Servizio(ricTransfert,  resultRicerca_Autista_Tariffe, AutistiDisponibiliList);
				
			}else {
				MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.autisti.non.disponibili.provincia", null, Constants.SERVIZIO_PARTICOLARE));
				resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
			}
			
		}else if( !ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(DATA_PRELEVAMENTO, Constants.SERVIZIO_PARTICOLARE) ) {
			MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("errors.ora.partenza.corsa", null, Constants.SERVIZIO_PARTICOLARE));
			resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
			
		}else {
			MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.autisti.non.disponibili.provincia", null, Constants.SERVIZIO_PARTICOLARE));
			resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
		}
		return Elaborazione_Servizio(ricTransfert, resultRicerca_Autista_Tariffe, AutistiDisponibiliList);
	}
	
	

	protected ResultRicerca_Autista_Tariffe Elaborazione_Servizio(RicercaTransfert ricTransfert, ResultRicerca_Autista_Tariffe resultRicerca_Autista_Tariffe, List<Long> AutistiDisponibiliList) {
		if( resultRicerca_Autista_Tariffe != null && resultRicerca_Autista_Tariffe.getResultAgendaAutista() != null && 
				(resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaAndata() != null 
				|| resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaRitorno() != null) ) {
			resultRicerca_Autista_Tariffe.setRicercaRiuscita(true);
		}
		
		// -------------------------------------------
		List<AutoveicoloClasseAppo> AutoveicoloClasseAppoList = new ArrayList<AutoveicoloClasseAppo>();
		List<Long> autoList_id = new ArrayList<Long>();
		Map<Long, List<Long>> ClassiNumeroAutisti = new HashMap<Long, List<Long>>();
		Map<Long, ClasseAutoveicolo> ClassiAutoveicoloMapList = new HashMap<Long, ClasseAutoveicolo>();

		// POPOLO OGGETTO AUTOVEICOLOCLASSEAPPOLIST - RIMUOMO LE AUTO DUPLICATE TENENDO SOLO QUELLE CON LA TARIFFA BASE PIù ALTA (COMUNE o PROVINCIA)
		for(ResultRicercaAutista ite: resultRicercaAutistaList) {
			Autoveicolo auto_ite_1 =  ite.getAutoveicolo();
			if( AutistiDisponibiliList.contains(auto_ite_1.getAutista().getId()) && !autoList_id.contains(auto_ite_1.getId()) ) {
				AutoveicoloClasseAppo autoveicoloClasseAppo = new AutoveicoloClasseAppo();
				autoveicoloClasseAppo.setIdAutista( ite.getIdAutista() );
				autoveicoloClasseAppo.setFirstName( ite.getFirstName() );
				autoveicoloClasseAppo.setNomeMarcaAuto( ite.getNomeMarcaAuto() );
				autoveicoloClasseAppo.setAutoveicolo(auto_ite_1);
				ClasseAutoveicolo ClasseAutoveicoloReale = auto_ite_1.getClasseAutoveicoloReale();
				autoveicoloClasseAppo.setClasseAutoveicoloReale( ClasseAutoveicoloReale );
				BigDecimal MaxTariffaKmValue = new BigDecimal(0);
				for(ResultRicercaAutista ite_2: resultRicercaAutistaList) {
					//AutistaZone zone_ite_2 = (AutistaZone) ite_2[0];
					Autoveicolo auto_ite_2 =  ite_2.getAutoveicolo();
					if(auto_ite_1.getId() == auto_ite_2.getId()){
						BigDecimal TariffaBase = new BigDecimal(0);
						TariffaBase = ite_2.getTariffaBaseProvincia();
						BigDecimal MaxTariffaKmValueAppo = new BigDecimal(TariffeUtil.DammiTariffa_ValoreAuto(Parametri, ClasseAutoveicoloReale, TariffaBase, Constants.FASCE_KILOMETRICHE, (int)(long)totaleMetriSoloAndata/1000));
						if(MaxTariffaKmValueAppo.compareTo(MaxTariffaKmValue) > 0){
							MaxTariffaKmValue = MaxTariffaKmValueAppo;
						}
					}
				}
				autoveicoloClasseAppo.setTariffaPerKm(MaxTariffaKmValue);
				AutoveicoloClasseAppoList.add(autoveicoloClasseAppo);
				autoList_id.add( auto_ite_1.getId() );
			}
		}
		
		/*
		for(AutoveicoloClasseAppo ite: AutoveicoloClasseAppoList){
			log.debug("auto: "+ite.getAutoveicolo().getId() +" tariffaKm: "+ite.getTariffaPerKm());
		}
		*/
		
		// CREO LA LISTA MAP DELLE CLASSI AUTOVEICOLI
		for(ClasseAutoveicolo ClasseAutoveicolo_ite: classeAutoveicoloDao.getClasseAutoveicolo()) {
			ClassiAutoveicoloMapList.put(ClasseAutoveicolo_ite.getId(), ClasseAutoveicolo_ite);
		}
		
		// CREO L'OGGETTO CLASSE AUTOVEICOLO - NUMERO AUTISTI
		for(ClasseAutoveicolo ClasseAutoveicolo_ite: ClassiAutoveicoloMapList.values()) {
			int numAutisti = 0;
			List<Long> AutistaList_id = new ArrayList<Long>();
			for(AutoveicoloClasseAppo AutoveicoloClasseAppo_ite: AutoveicoloClasseAppoList){
				if(AutoveicoloClasseAppo_ite.getClasseAutoveicoloReale().getId() == ClasseAutoveicolo_ite.getId() 
						&& !AutistaList_id.contains(AutoveicoloClasseAppo_ite.getAutoveicolo().getAutista().getId())){
					AutistaList_id.add(AutoveicoloClasseAppo_ite.getAutoveicolo().getAutista().getId());
					numAutisti++;
				}
			}
			//log.debug(": "+numAutisti);
			ClassiNumeroAutisti.put(ClasseAutoveicolo_ite.getId(), AutistaList_id);
		}
		
		//System.out.println("----------------COMBINO LE CLASSI----------------------");
		List<ClassiAutoCombinate> classiAutoCombinateList = CombinaClassiAutoveicoli(ClassiNumeroAutisti, ClassiAutoveicoloMapList, NumMinimoAutistiCorsaMedia, 
				AutoveicoloClasseAppoList, resultRicerca_Autista_Tariffe.getTipoServizio());
		/*
		for (ClassiAutoCombinate ClassiAutoCombinate_ite : classiAutoCombinateList){
			log.debug(ClassiAutoCombinate_ite.getClasseAutoveicolo().getDescription()+ " | " 
					+ClassiAutoCombinate_ite.isShowClasseAutoveicolo()+" | " +ClassiAutoCombinate_ite.getClassiAutoveicoloCombinate());
		}
		*/
		List<ResultRicerca_Autista_Tariffe.ResultMedio> AAA_LIST = new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio>();
		for(ClassiAutoCombinate ClasseAutoveicolo_ite: classiAutoCombinateList) {
			if(ClasseAutoveicolo_ite.getClassiAutoveicoloCombinate().size() > 0) {
				ResultRicerca_Autista_Tariffe.ResultMedio AAA = new ResultRicerca_Autista_Tariffe.ResultMedio();
				AAA.setClasseAutoveicolo(ClasseAutoveicolo_ite.getClasseAutoveicolo());
				BigDecimal PrezzoAutistaPiuAlto = new BigDecimal(0);
				BigDecimal TariffaKmPiuAlta = new BigDecimal(0);
				BigDecimal PrezzoCommissioneServizioPiuAlta = new BigDecimal(0);
				BigDecimal PrezzoCommissioneServizioIvaPiuAlta = new BigDecimal(0);
				BigDecimal PrezzoCommissioneVenditorePiuAlta = new BigDecimal(0); //nuovo
				BigDecimal MaggiorazioneNotturnaAndata = new BigDecimal(0);
				BigDecimal MaggiorazioneNotturnaRitorno = new BigDecimal(0);
				List<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista> BBB_LIST = new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista>();
				List<Long> autistaId_List = new ArrayList<Long>();
				for(AutoveicoloClasseAppo AutoveicoloClasseAppo_ite_1: ClasseAutoveicolo_ite.getAutoveicoloClasseAppoList()){
					if( !autistaId_List.contains(AutoveicoloClasseAppo_ite_1.getAutoveicolo().getAutista().getId())
							&& ClasseAutoveicolo_ite.getClassiAutoveicoloCombinate().contains(AutoveicoloClasseAppo_ite_1.getClasseAutoveicoloReale().getId())){
						ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista BBB = new ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista();
						BBB.setAutista(AutoveicoloClasseAppo_ite_1.getAutoveicolo().getAutista());
						BBB.setPercentualeServizio((int)(long)PercentualeServizio);
						BBB.setClasseAutoveicolo( AutoveicoloClasseAppo_ite_1.getClasseAutoveicoloReale() );
						BBB.setTariffaPerKm( AutoveicoloClasseAppo_ite_1.getTariffaPerKm() );
						BigDecimal PrezzoAutista = CalcoloPrezzi.CalcolaPrezzoPerMetri((int)(long)totaleMetriSoloAndata / 1000 == 0 ? 1 : (int)(long)totaleMetriSoloAndata / 1000, AutoveicoloClasseAppo_ite_1.getTariffaPerKm(), false);
						if(DateUtil.MaggiorazioneNotturna(DATA_PRELEVAMENTO, DURATA_TRAFFICO_ANDATA, MaggNott_OrarioNutturno)){
							//MaggiorazioneNotturna = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutista, MaggNott_Percentuale); // prima volelo usare la percentuale, ma per le corse lunghe o corte vengono maggiorazioni troppo alte o basse, allora ho messo fisso
							MaggiorazioneNotturnaAndata = new BigDecimal(MaggNott_Percentuale);
							PrezzoAutista = PrezzoAutista.add(MaggiorazioneNotturnaAndata);
						}
						if(ricTransfert.isRitorno()){
							BigDecimal PrezzoAutistaRitorno = CalcoloPrezzi.CalcolaPrezzoPerMetri((int)(long)totaleMetriSoloRitorno / 1000 == 0 ? 1 : (int)(long)totaleMetriSoloRitorno / 1000, AutoveicoloClasseAppo_ite_1.getTariffaPerKm(), ScontoAttesaRitorno);
							if(DateUtil.MaggiorazioneNotturna(DATA_RITORNO, DURATA_TRAFFICO_RITORNO, MaggNott_OrarioNutturno)){
								//MaggiorazioneNotturnaRitorno = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutistaRitorno, MaggNott_Percentuale); // prima volelo usare la percentuale, ma per le corse lunghe o corte vengono maggiorazioni troppo alte o basse, allora ho messo fisso
								MaggiorazioneNotturnaRitorno = new BigDecimal(MaggNott_Percentuale);
								PrezzoAutistaRitorno = PrezzoAutistaRitorno.add(MaggiorazioneNotturnaRitorno);
							}
							PrezzoAutista = PrezzoAutista.add( PrezzoAutistaRitorno );
						}
						BigDecimal PrezzoCommissioneServizio = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutista, (int)(long)PercentualeServizio);
						BigDecimal PrezzoCommissioneServizioIva = CalcoloPrezzi.CalcolaPrezzoIva(PrezzoCommissioneServizio);
						BigDecimal PrezzoCommissioneVenditore = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutista, (int)(long)PercentualeVenditore); //nuovo
						BBB.setPrezzoTotaleAutista(PrezzoAutista);
						BBB.setPrezzoCommissioneServizio(PrezzoCommissioneServizio);
						BBB.setPrezzoCommissioneVenditore(PrezzoCommissioneVenditore); //nuovo
						BBB.setPrezzoCommissioneServizioIva(PrezzoCommissioneServizioIva);
						if(PrezzoAutista.compareTo(PrezzoAutistaPiuAlto) > 0){
							PrezzoAutistaPiuAlto = PrezzoAutista;
							TariffaKmPiuAlta = AutoveicoloClasseAppo_ite_1.getTariffaPerKm();
							PrezzoCommissioneServizioPiuAlta = PrezzoCommissioneServizio;
							PrezzoCommissioneServizioIvaPiuAlta = PrezzoCommissioneServizioIva;
							PrezzoCommissioneVenditorePiuAlta = PrezzoCommissioneVenditore; //nuovo
						}
						List<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo> CCC_LIST = new ArrayList<ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo>();
						for(AutoveicoloClasseAppo AutoveicoloClasseAppo_ite_2: ClasseAutoveicolo_ite.getAutoveicoloClasseAppoList()){
							if( AutoveicoloClasseAppo_ite_1.getAutoveicolo().getAutista().getId() == AutoveicoloClasseAppo_ite_2.getAutoveicolo().getAutista().getId()
									&& ClasseAutoveicolo_ite.getClassiAutoveicoloCombinate().contains(AutoveicoloClasseAppo_ite_2.getClasseAutoveicoloReale().getId())){
								ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo CCC = new ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo();
								CCC.setAutoveicolo( AutoveicoloClasseAppo_ite_2.getAutoveicolo() );
								CCC.setIdAutista( AutoveicoloClasseAppo_ite_2.getIdAutista() );
								CCC.setFirstName( AutoveicoloClasseAppo_ite_2.getFirstName() );
								CCC.setNomeMarcaAuto(AutoveicoloClasseAppo_ite_2.getNomeMarcaAuto());
								CCC_LIST.add(CCC);
							}
						}
						BBB.setRisultAutistaMedioAutoveicolo(CCC_LIST);
						BBB_LIST.add(BBB);
						autistaId_List.add(AutoveicoloClasseAppo_ite_1.getAutoveicolo().getAutista().getId());
					}
				}
				AAA.setShowClasseAutoveicolo((autoList_id.size() >= NumMinimoAutistiCorsaMedia) ? true : false);
				AAA.setPrezzoTotaleCliente(PrezzoAutistaPiuAlto.add(PrezzoCommissioneServizioPiuAlta).add(PrezzoCommissioneServizioIvaPiuAlta).add(PrezzoCommissioneVenditorePiuAlta));
				AAA.setMaggiorazioneNotturna( (MaggiorazioneNotturnaAndata.add(MaggiorazioneNotturnaRitorno).compareTo(BigDecimal.ZERO) != 0) ? MaggiorazioneNotturnaAndata.add(MaggiorazioneNotturnaRitorno) : null ); 
				AAA.setTariffaPerKm( TariffaKmPiuAlta );
				AAA.setPrezzoTotaleAutista(PrezzoAutistaPiuAlto);
				AAA.setPrezzoCommissioneServizio(PrezzoCommissioneServizioPiuAlta);
				AAA.setPrezzoCommissioneServizioIva(PrezzoCommissioneServizioIvaPiuAlta);
				AAA.setPrezzoCommissioneVenditore(PrezzoCommissioneVenditorePiuAlta); //nuovo
				AAA.setResultMedioAutista(BBB_LIST);
				AAA_LIST.add(AAA);
			}
		}
		for(ResultRicerca_Autista_Tariffe.ResultMedio AAA_ITE : AAA_LIST) {
			if(AAA_ITE.isShowClasseAutoveicolo()){
				resultRicerca_Autista_Tariffe.setRicercaRiuscita(true);
				break;
			}
		}
		resultRicerca_Autista_Tariffe.setResultMedio(AAA_LIST);
		return resultRicerca_Autista_Tariffe;
	}
	
	@SuppressWarnings("unchecked")
	private List<ClassiAutoCombinate> CombinaClassiAutoveicoli(Map<Long, List<Long>> ClassiNumeroAutisti, 
			Map<Long, ClasseAutoveicolo> ClassiAutoveicoloMapList, int NumMinimoAutistiCorsaMedia, List<AutoveicoloClasseAppo> AutoveicoloClasseAppoList, String TipoServizio) {
		
		// COMBINO LE CLASSI
		List<ClassiAutoCombinate> classiAutoCombinateList = new ArrayList<ClassiAutoCombinate>();
		
		// COMBINAZIONE TOTALE AUTO_ECONOMY, AUTO_PRIMA_CLASSE
		List<Long> TotAutoList_id = new ArrayList<Long>();
		TotAutoList_id.addAll(ListUtils.union(ClassiNumeroAutisti.get(Constants.AUTO_PRIMA_CLASSE),ClassiNumeroAutisti.get(Constants.AUTO_ECONOMY)));
		//log.debug("distinct TotAutoList_id: "+TotAutoList_id.stream().distinct().collect(Collectors.toList()).size());
		
		// COMBINAZIONE TOTALE AUTO_VAN_ECONOMY, AUTO_VAN_PRIMA_CLASSE
		List<Long> TotVanList_id = new ArrayList<Long>();
		TotVanList_id.addAll(ListUtils.union(ClassiNumeroAutisti.get(Constants.AUTO_VAN_PRIMA_CLASSE),ClassiNumeroAutisti.get(Constants.AUTO_VAN_ECONOMY)));
		//log.debug("distinct TotVanList_id: "+TotVanList_id.stream().distinct().collect(Collectors.toList()).size());
		
		// COMBINAZIONE TOTALE AUTO_ECONOMY, AUTO_PRIMA_CLASSE, AUTO_VAN_ECONOMY, AUTO_VAN_PRIMA_CLASSE (quando nessuna raggiunge la quota)
		List<Long> TotVanAutoList_id = new ArrayList<Long>();
		TotVanAutoList_id.addAll(TotAutoList_id); TotVanAutoList_id.addAll(TotVanList_id);
		//log.debug("distinct TotVanAutoList_id: "+TotVanAutoList_id.stream().distinct().collect(Collectors.toList()).size());
		
		// COMBINAZIONE TOTALE AUTO E VAN
		if(TipoServizio.equals(Constants.SERVIZIO_STANDARD)
				&&TotVanAutoList_id.stream().distinct().collect(Collectors.toList()).size() >= NumMinimoAutistiCorsaMedia
				&&TotAutoList_id.stream().distinct().collect(Collectors.toList()).size() < NumMinimoAutistiCorsaMedia
				&& TotVanList_id.stream().distinct().collect(Collectors.toList()).size() < NumMinimoAutistiCorsaMedia ){
			// LA CLASSE CON TARFFA PIù ALTA DI TUTTE
			ClassiAutoCombinate aa = new ClassiAutoCombinate();
			aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_PRIMA_CLASSE));
			aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_ECONOMY,Constants.AUTO_PRIMA_CLASSE,Constants.AUTO_VAN_ECONOMY,Constants.AUTO_VAN_PRIMA_CLASSE));
			aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
			aa.setShowClasseAutoveicolo(true);
			classiAutoCombinateList.add( aa );
		}else{
			// COMBINAZIONE AUTO ECONOMY e AUTO PRIMA CLASSE
			if( TipoServizio.equals(Constants.SERVIZIO_STANDARD)
					&& TotAutoList_id.stream().distinct().collect(Collectors.toList()).size() >= NumMinimoAutistiCorsaMedia
					&& ClassiNumeroAutisti.get(Constants.AUTO_PRIMA_CLASSE).size() < NumMinimoAutistiCorsaMedia
					&& ClassiNumeroAutisti.get(Constants.AUTO_ECONOMY).size() < NumMinimoAutistiCorsaMedia){
				ClassiAutoCombinate aa = new ClassiAutoCombinate();
				aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_PRIMA_CLASSE));
				aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_ECONOMY,Constants.AUTO_PRIMA_CLASSE));
				aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
				aa.setShowClasseAutoveicolo(true);
				classiAutoCombinateList.add( aa );
			}else{
				// AUTO ECONOMY
				if( ClassiNumeroAutisti.get(Constants.AUTO_ECONOMY).size() >= NumMinimoAutistiCorsaMedia){
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_ECONOMY));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_ECONOMY));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(true);
					classiAutoCombinateList.add( aa );
				}else if( ClassiNumeroAutisti.get(Constants.AUTO_ECONOMY).size() > 0 && ClassiNumeroAutisti.get(Constants.AUTO_ECONOMY).size() < NumMinimoAutistiCorsaMedia){	
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_ECONOMY));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_ECONOMY));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}else{
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_ECONOMY));
					aa.setClassiAutoveicoloCombinate(new ArrayList<Long>());
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}
				// AUTO PRIMA CLASSE
				if( ClassiNumeroAutisti.get(Constants.AUTO_PRIMA_CLASSE).size() >= NumMinimoAutistiCorsaMedia){
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_PRIMA_CLASSE));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_PRIMA_CLASSE));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(true);
					classiAutoCombinateList.add( aa );
				}else if( ClassiNumeroAutisti.get(Constants.AUTO_PRIMA_CLASSE).size() > 0 && ClassiNumeroAutisti.get(Constants.AUTO_PRIMA_CLASSE).size() < NumMinimoAutistiCorsaMedia){	
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_PRIMA_CLASSE));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_PRIMA_CLASSE));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}else{
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_PRIMA_CLASSE));
					aa.setClassiAutoveicoloCombinate(new ArrayList<Long>());
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}
			}
			// LUXORY
			if( ClassiNumeroAutisti.get(Constants.AUTO_LUXURY).size() >= NumMinimoAutistiCorsaMedia){
				ClassiAutoCombinate aa = new ClassiAutoCombinate();
				aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_LUXURY));
				aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_LUXURY));
				aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
				aa.setShowClasseAutoveicolo(true);
				classiAutoCombinateList.add( aa );
			}else if( ClassiNumeroAutisti.get(Constants.AUTO_LUXURY).size() > 0 && ClassiNumeroAutisti.get(Constants.AUTO_LUXURY).size() < NumMinimoAutistiCorsaMedia){	
				ClassiAutoCombinate aa = new ClassiAutoCombinate();
				aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_LUXURY));
				aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_LUXURY));
				aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
				aa.setShowClasseAutoveicolo(false);
				classiAutoCombinateList.add( aa );
			}else{
				ClassiAutoCombinate aa = new ClassiAutoCombinate();
				aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_LUXURY));
				aa.setClassiAutoveicoloCombinate(new ArrayList<Long>());
				aa.setShowClasseAutoveicolo(false);
				classiAutoCombinateList.add( aa );
			}
			// COMBINAZIONE VAN ECONOMY e VAN PRIMA CLASSE
			if( TipoServizio.equals(Constants.SERVIZIO_STANDARD)
					&& TotVanList_id.stream().distinct().collect(Collectors.toList()).size() >= NumMinimoAutistiCorsaMedia
					&& ClassiNumeroAutisti.get(Constants.AUTO_VAN_PRIMA_CLASSE).size() < NumMinimoAutistiCorsaMedia
					&& ClassiNumeroAutisti.get(Constants.AUTO_VAN_ECONOMY).size() < NumMinimoAutistiCorsaMedia ){
				ClassiAutoCombinate aa = new ClassiAutoCombinate();
				aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_PRIMA_CLASSE));
				aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_VAN_ECONOMY,Constants.AUTO_VAN_PRIMA_CLASSE));
				aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
				aa.setShowClasseAutoveicolo(true);
				classiAutoCombinateList.add( aa );
			}else{
				// VAN ECONOMY
				if( ClassiNumeroAutisti.get(Constants.AUTO_VAN_ECONOMY).size() >= NumMinimoAutistiCorsaMedia){
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_ECONOMY) );
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_VAN_ECONOMY));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(true);
					classiAutoCombinateList.add( aa );
				}else if( ClassiNumeroAutisti.get(Constants.AUTO_VAN_ECONOMY).size() > 0 && ClassiNumeroAutisti.get(Constants.AUTO_VAN_ECONOMY).size() < NumMinimoAutistiCorsaMedia){	
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_ECONOMY));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_VAN_ECONOMY));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}else{
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_ECONOMY));
					aa.setClassiAutoveicoloCombinate(new ArrayList<Long>());
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}
				// VAN PRIMA CLASSE
				if( ClassiNumeroAutisti.get(Constants.AUTO_VAN_PRIMA_CLASSE).size() >= NumMinimoAutistiCorsaMedia){
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_PRIMA_CLASSE) );
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_VAN_PRIMA_CLASSE));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(true);
					classiAutoCombinateList.add( aa );
				}else if( ClassiNumeroAutisti.get(Constants.AUTO_VAN_PRIMA_CLASSE).size() > 0 && ClassiNumeroAutisti.get(Constants.AUTO_VAN_PRIMA_CLASSE).size() < NumMinimoAutistiCorsaMedia){	
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_PRIMA_CLASSE));
					aa.setClassiAutoveicoloCombinate(Arrays.asList(Constants.AUTO_VAN_PRIMA_CLASSE));
					aa = CalcolaTariffaCompetitiva(aa, NumMinimoAutistiCorsaMedia, AutoveicoloClasseAppoList);
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}else{
					ClassiAutoCombinate aa = new ClassiAutoCombinate();
					aa.setClasseAutoveicolo( ClassiAutoveicoloMapList.get(Constants.AUTO_VAN_PRIMA_CLASSE));
					aa.setClassiAutoveicoloCombinate(new ArrayList<Long>());
					aa.setShowClasseAutoveicolo(false);
					classiAutoCombinateList.add( aa );
				}
			}
		}
		return classiAutoCombinateList;
	}
	
	
	protected List<ResultRicercaAutista> RicercaAutisti(long idComPartenza, long idProvPartenza, long idRegPartenza, long idComArrivo, long idProvArrivo, long idRegArrivo, 
			List<Long> InfraProvincie, Integer NumeroPasseggeri, boolean soloAzienda, boolean ritorno, Date dataOraPrelevamento, Date dataOraRitorno) {
		// questo serve a scartare le auto 5 posti nel caso in cui ci sono 4 passeggeri (perché altrimenti non entrano inclusi i bagagli)
		NumeroPasseggeri = (NumeroPasseggeri != null && NumeroPasseggeri == 4) ? NumeroPasseggeri = NumeroPasseggeri + 1 : NumeroPasseggeri;
		List<Object[]> resultObject = ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_AUTO_HQL(
				idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, idProvArrivo, idRegArrivo, InfraProvincie, NumeroPasseggeri, soloAzienda, ritorno, dataOraPrelevamento, dataOraRitorno);
		
		List<ResultRicercaAutista> resultRicercaAutistaList = new ArrayList<ResultRicercaAutista>();
		for(Object[] ite: resultObject) {
			ResultRicercaAutista resultRicercaAutista = new ResultRicercaAutista(((BigInteger)ite[0]).longValue(), 
					ite[1].toString(), ((BigInteger)ite[2]).longValue(), (Integer)ite[3], ite[4].toString(), ite[5].toString(), ite[6].toString(), ((BigInteger)ite[7]).longValue(), 
					(Integer)ite[8], (BigDecimal)ite[9], ite[10].toString(), ite[11].toString(), ite[12].toString());
			resultRicercaAutistaList.add(resultRicercaAutista);
		}
		
		// VISUALIZZAZONE AUTISTI
		for(ResultRicercaAutista ite: resultRicercaAutistaList){
			log.debug(
				ite.getIdAutoveicolo()
				+" | "+ite.getIdAutista()
				+" | "+ite.getNumCorseEseguite()
				+" | "+ite.getFirstName()+" "+ite.getLastName()
				+" | "+AutoveicoloUtil.DammiAutoClasseReale(ite.getIdClasseAutoveicolo(), ite.getAnnoImmatricolazione()).getDescription() 
				+" | "+ite.getNumeroPostiAutoveicolo()
				+" | "+ite.getPhoneNumber()
				+" | "+ite.getNomeMarcaAuto()+" "+ite.getNomeModelloAuto()
				+" | "+ite.getTariffaBaseProvincia()+ "(Prov: "+ite.getSiglaProvincia()+")" );
		}
		return resultRicercaAutistaList;
	}
	
	
	/**
	 * CREO LA LISTA DI AUTISTI DISPONIBILI
	 * @param resultRicercaAutistaList
	 * @param ricTransfert
	 * @throws Exception 
	 * @throws NullPointerException 
	 * @throws JSONException s
	 */
	protected List<Long> getListaAutistiDisponibili(List<ResultRicercaAutista> resultRicercaAutistaList, RicercaTransfert ricTransfert) throws JSONException, NullPointerException, Exception {
		List<Long> autistiListTot_id = new ArrayList<Long>();
		for(ResultRicercaAutista ite: resultRicercaAutistaList){
			Autoveicolo auto_ite = ite.getAutoveicolo();
			if(!autistiListTot_id.contains(auto_ite.getAutista().getId()) ){
				autistiListTot_id.add( auto_ite.getAutista().getId() );
			}
		}
		
		List<Long> AutistiDisponibiliList = new ArrayList<Long>();
		for(ResultRicercaAutista ite: resultRicercaAutistaList) {
			AutistiDisponibiliList.add( ite.getAutoveicolo().getAutista().getId() );
		}
		
		/*
		List<Long> autistiList_id = new ArrayList<Long>();
		ControlloDateRicerca ControlloDateRicerca = new ControlloDateRicerca();
		for(ResultRicercaAutista ite: resultRicercaAutistaList) {
			if(!autistiList_id.contains(ite.getAutoveicolo().getAutista().getId()) 
					&& ControlloDateRicerca.ControlloAutistaCorseSovrapposte_MAIN(ricTransfert, ite.getAutoveicolo().getAutista().getId())) {
				AutistiDisponibiliList.add( ite.getAutoveicolo().getAutista().getId() );
			}
			autistiList_id.add( ite.getAutoveicolo().getAutista().getId() );
		}
		*/
		
		//log.debug( "AutistiDisponibiliList.size: "+AutistiDisponibiliList.size() );
		return AutistiDisponibiliList;
	}
	
	
	private ClassiAutoCombinate CalcolaTariffaCompetitiva(ClassiAutoCombinate aa, int NumMinimoAutistiCorsaMedia, List<AutoveicoloClasseAppo> AutoveicoloClasseAppoList){
		// DEVO TENERE GLI AUTISTI CON LE TARIFFE DI PROVINCIA PIù BASSE SE TALI AUTISTI SONO IL DOPPIO DI NUM_MIN_AUTISTI_CORSA_MEDIA
		int NumMinimoAutistiCorsaMediaPerDue = NumMinimoAutistiCorsaMedia * 2;
		//int NumMinimoAutistiCorsaMediaPerDue = 5;
		List<BigDecimal> listTariffe = new ArrayList<BigDecimal>();
		for(AutoveicoloClasseAppo ite_1: AutoveicoloClasseAppoList){
			if(ite_1.getClasseAutoveicoloReale().getId() == aa.getClasseAutoveicolo().getId()){
				BigDecimal tariffa = ite_1.getTariffaPerKm();
				int conta = 0;
				List<Long> autistiList = new ArrayList<Long>();
				for(AutoveicoloClasseAppo ite_2: AutoveicoloClasseAppoList){
					if( !autistiList.contains(ite_2.getAutoveicolo().getAutista().getId()) 
							&& ite_2.getClasseAutoveicoloReale().getId() == aa.getClasseAutoveicolo().getId() 
							&& ite_2.getTariffaPerKm().compareTo(tariffa) == 0) {
						conta++;
						autistiList.add(ite_2.getAutoveicolo().getAutista().getId());
						if(conta == NumMinimoAutistiCorsaMediaPerDue){
							listTariffe.add(tariffa);
							break;
						}
					}
				}
			}
		}
		if(listTariffe.size() > 0){
			Collections.sort(listTariffe, Collections.reverseOrder());
			BigDecimal tariffaPiuBassa = listTariffe.get( listTariffe.size()-1 );
			List<AutoveicoloClasseAppo> AutoveicoloClasseAppoListPerClasseCombinata = new ArrayList<AutoveicoloClasseAppo>();
			for(AutoveicoloClasseAppo ite_1: AutoveicoloClasseAppoList){
				if(ite_1.getClasseAutoveicoloReale().getId() == aa.getClasseAutoveicolo().getId() && ite_1.getTariffaPerKm().compareTo(tariffaPiuBassa) <= 0){
					AutoveicoloClasseAppoListPerClasseCombinata.add(ite_1);
				}
			}
			aa.setAutoveicoloClasseAppoList(AutoveicoloClasseAppoListPerClasseCombinata);
			return aa;
		}else{
			return CalcolaTariffaPiuAlta(aa, AutoveicoloClasseAppoList);
		}
	}
	
	
	private ClassiAutoCombinate CalcolaTariffaPiuAlta(ClassiAutoCombinate aa, List<AutoveicoloClasseAppo> AutoveicoloClasseAppoList){
		BigDecimal tariffaPiuAlta = new BigDecimal(0);
		List<Long> autistaId_List = new ArrayList<Long>();
		List<AutoveicoloClasseAppo> AutoveicoloClasseAppoListPerClasseCombinata = new ArrayList<AutoveicoloClasseAppo>();
		for(AutoveicoloClasseAppo AutoveicoloClasseAppo_ite_1: AutoveicoloClasseAppoList){
			if( !autistaId_List.contains(AutoveicoloClasseAppo_ite_1.getAutoveicolo().getAutista().getId())
					&& aa.getClassiAutoveicoloCombinate().contains(AutoveicoloClasseAppo_ite_1.getClasseAutoveicoloReale().getId())){
				AutoveicoloClasseAppoListPerClasseCombinata.add(AutoveicoloClasseAppo_ite_1);
				if(AutoveicoloClasseAppo_ite_1.getTariffaPerKm().compareTo(tariffaPiuAlta) > 0){
					tariffaPiuAlta = AutoveicoloClasseAppo_ite_1.getTariffaPerKm();
				}
			}
		}
		aa.setAutoveicoloClasseAppoList(AutoveicoloClasseAppoListPerClasseCombinata);
		return aa;
	}

	
	private class AutoveicoloClasseAppo {
		private Autoveicolo autoveicolo;
		private ClasseAutoveicolo classeAutoveicoloReale;
		private BigDecimal tariffaPerKm;
		private long idAutista;
		private String firstName;
		private String nomeMarcaAuto;
		public long getIdAutista() {
			return idAutista;
		}
		public void setIdAutista(long idAutista) {
			this.idAutista = idAutista;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getNomeMarcaAuto() {
			return nomeMarcaAuto;
		}
		public void setNomeMarcaAuto(String nomeMarcaAuto) {
			this.nomeMarcaAuto = nomeMarcaAuto;
		}
		public Autoveicolo getAutoveicolo() {
			return autoveicolo;
		}
		public void setAutoveicolo(Autoveicolo autoveicolo) {
			this.autoveicolo = autoveicolo;
		}
		public ClasseAutoveicolo getClasseAutoveicoloReale() {
			return classeAutoveicoloReale;
		}
		public void setClasseAutoveicoloReale(ClasseAutoveicolo classeAutoveicoloReale) {
			this.classeAutoveicoloReale = classeAutoveicoloReale;
		}
		public BigDecimal getTariffaPerKm() {
			return tariffaPerKm;
		}
		public void setTariffaPerKm(BigDecimal tariffaPerKm) {
			this.tariffaPerKm = tariffaPerKm;
		}
	}
	
	private class ClassiAutoCombinate {
		private ClasseAutoveicolo classeAutoveicolo;
		private List<Long> classiAutoveicoloCombinate;
		private boolean showClasseAutoveicolo;
		private List<AutoveicoloClasseAppo> autoveicoloClasseAppoList;
		
		public ClasseAutoveicolo getClasseAutoveicolo() {
			return classeAutoveicolo;
		}
		public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
			this.classeAutoveicolo = classeAutoveicolo;
		}
		public List<Long> getClassiAutoveicoloCombinate() {
			return classiAutoveicoloCombinate;
		}
		public void setClassiAutoveicoloCombinate(List<Long> classiAutoveicoloCombinate) {
			this.classiAutoveicoloCombinate = classiAutoveicoloCombinate;
		}
		public boolean isShowClasseAutoveicolo() {
			return showClasseAutoveicolo;
		}
		public void setShowClasseAutoveicolo(boolean showClasseAutoveicolo) {
			this.showClasseAutoveicolo = showClasseAutoveicolo;
		}
		public List<AutoveicoloClasseAppo> getAutoveicoloClasseAppoList() {
			return autoveicoloClasseAppoList;
		}
		public void setAutoveicoloClasseAppoList(
				List<AutoveicoloClasseAppo> autoveicoloClasseAppoList) {
			this.autoveicoloClasseAppoList = autoveicoloClasseAppoList;
		}
	}
	
	public class ResultRicercaAutista {
		private Autoveicolo autoveicolo;
		private long idAutoveicolo ;
		private String annoImmatricolazione;
		private long idAutista;
		private int numCorseEseguite;
		private String firstName;
		private String lastName;
		private String phoneNumber;
		private Long idClasseAutoveicolo;
		private Integer numeroPostiAutoveicolo;
		private BigDecimal tariffaBaseProvincia;
		private String siglaProvincia;
		private String nomeMarcaAuto;
		private String nomeModelloAuto;
		
		public ResultRicercaAutista(long idAutoveicolo, String annoImmatricolazione, long idAutista,
				int numCorseEseguite, String firstName, String lastName, String phoneNumber, Long idClasseAutoveicolo,
				Integer numeroPostiAutoveicolo, BigDecimal tariffaBaseProvincia, String siglaProvincia,
				String nomeMarcaAuto, String nomeModelloAuto) {
			super();
			Autoveicolo newAutoveicolo = new Autoveicolo();
			newAutoveicolo.setId( idAutoveicolo );
			ClasseAutoveicolo newClasseAutoveicolo = new ClasseAutoveicolo();
			newClasseAutoveicolo.setId( idClasseAutoveicolo );
			ModelloAutoScout newModelloAutoScout = new ModelloAutoScout();
			newModelloAutoScout.setClasseAutoveicolo(newClasseAutoveicolo);
			ModelloAutoNumeroPosti newModelloAutoNumeroPosti = new ModelloAutoNumeroPosti();
			newModelloAutoNumeroPosti.setModelloAutoScout(newModelloAutoScout);
			newAutoveicolo.setModelloAutoNumeroPosti(newModelloAutoNumeroPosti);
			newAutoveicolo.setAnnoImmatricolazione(annoImmatricolazione);
			Autista newAutista = new Autista();
			newAutista.setId(idAutista);
			newAutoveicolo.setAutista( newAutista );
			this.autoveicolo = newAutoveicolo;
			this.idAutoveicolo = idAutoveicolo;
			this.annoImmatricolazione = annoImmatricolazione;
			this.idAutista = idAutista;
			this.numCorseEseguite = numCorseEseguite;
			this.firstName = firstName;
			this.lastName = lastName;
			this.phoneNumber = phoneNumber;
			this.idClasseAutoveicolo = idClasseAutoveicolo;
			this.numeroPostiAutoveicolo = numeroPostiAutoveicolo;
			this.tariffaBaseProvincia = tariffaBaseProvincia;
			this.siglaProvincia = siglaProvincia;
			this.nomeMarcaAuto = nomeMarcaAuto;
			this.nomeModelloAuto = nomeModelloAuto;
		}

		public Autoveicolo getAutoveicolo() {
			return autoveicolo;
		}
		public long getIdAutoveicolo() {
			return idAutoveicolo;
		}
		public String getAnnoImmatricolazione() {
			return annoImmatricolazione;
		}
		public long getIdAutista() {
			return idAutista;
		}
		public int getNumCorseEseguite() {
			return numCorseEseguite;
		}
		public String getFirstName() {
			return firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public Long getIdClasseAutoveicolo() {
			return idClasseAutoveicolo;
		}
		public Integer getNumeroPostiAutoveicolo() {
			return numeroPostiAutoveicolo;
		}
		public BigDecimal getTariffaBaseProvincia() {
			return tariffaBaseProvincia;
		}
		public String getSiglaProvincia() {
			return siglaProvincia;
		}
		public String getNomeMarcaAuto() {
			return nomeMarcaAuto;
		}
		public String getNomeModelloAuto() {
			return nomeModelloAuto;
		}
	}


	
}

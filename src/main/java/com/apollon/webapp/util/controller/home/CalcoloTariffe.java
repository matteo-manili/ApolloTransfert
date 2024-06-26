package com.apollon.webapp.util.controller.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.model.Comuni;
import com.apollon.model.GestioneApplicazione;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.NumberUtil;
import com.apollon.util.UtilString;
import com.apollon.util.customexception.FerrysTraghettiException;
import com.apollon.util.customexception.GoogleMatrixException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.MessaggioEsitoRicerca;
import com.apollon.webapp.util.controller.tariffe.TariffeVenditoreUtil;
import com.apollon.webapp.util.geogoogle.GMaps_Api;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class CalcoloTariffe extends CalcoloTariffe_Base {
	private static final Log log = LogFactory.getLog(CalcoloTariffe.class);

	public ResultRicerca_Autista_Tariffe CalcoloTariffe_Main(RicercaTransfert ricTransfert) throws Exception {
		long startTime = System.nanoTime();
		DATA_PRELEVAMENTO = ricTransfert.getDataOraPrelevamentoDate(); // new Date(Long.parseLong(dataOraStringPrelev)); 
		DURATA_TRAFFICO_ANDATA = ricTransfert.getDurataConTrafficoValue();
		DATA_RITORNO = ricTransfert.getDataOraRitornoDate(); DURATA_TRAFFICO_RITORNO = ricTransfert.getDurataConTrafficoValueRitorno();
		totaleMetriSoloAndata = ricTransfert.getDistanzaValue(); totaleMetriSoloRitorno = ricTransfert.getDistanzaValueRitorno();
		totaleMetri_Andata_e_Ritorno = totaleMetriSoloAndata + totaleMetriSoloRitorno; kilometri = (int)(long)(totaleMetri_Andata_e_Ritorno/1000);
		
		if(kilometri < 1){ kilometri = 1; }
		NumeroPasseggeri = ricTransfert.getNumeroPasseggeri(); 
		ResultRicerca_Autista_Tariffe resultRicerca_Autista_Tariffe = new ResultRicerca_Autista_Tariffe();
		resultRicerca_Autista_Tariffe.setRicercaRiuscita(false); MessaggiEsitoRicerca = new ArrayList<MessaggioEsitoRicerca>();

		// CONTROLLO METRI SUPERIORI E A 1 E NUMERI PASSEGGERI SUPERIORI A 0
		if(kilometri < 1 || NumeroPasseggeri < 1 /*|| Specificata_parteza_arrivo*/){
			MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("errors.cancel", null) );
			resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
			return resultRicerca_Autista_Tariffe;
		}
		
		ScontoAttesaRitorno = false;
		if( ricTransfert.isRitorno() && ricTransfert.getDataOraRitorno() != null && !ricTransfert.getDataOraRitorno().equals("") ){
			// CONTROLLO SCONTO APPLICABILE, SE IL RITORNO NON SUPERA UN CERTO ORARIO ALLORA LA CORSA AVRA' UNO SCONTO
			if( ControlloDateRicerca.ControlloDataRitornoOrarioSconto(ricTransfert).getTime() >= ricTransfert.getDataOraRitornoDate().getTime() ){
				ScontoAttesaRitorno = true;
				resultRicerca_Autista_Tariffe.setScontoRitorno(true);
			}else{
				resultRicerca_Autista_Tariffe.setScontoRitorno(false);
			}
		}
		
		//COMUNI
		Comuni comunePartenza = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Partenza(), ricTransfert.getSiglaProvicia_Partenza() );
		Comuni comuneArrivo = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Arrivo(), ricTransfert.getSiglaProvicia_Arrivo() );
		long idComPartenza = comunePartenza.getId(); long idProvPartenza = comunePartenza.getProvince().getId(); long idRegPartenza = comunePartenza.getRegioni().getId();
		long idComArrivo = comuneArrivo.getId(); long idProvArrivo = comuneArrivo.getProvince().getId(); long idRegArrivo = comuneArrivo.getRegioni().getId();
		List<Long> InfraProvincie = null;
		List<Long> AutistiDisponibiliList;
		// RICERCA SE CI SONO PROVINCE NEL TRAGITTO TRA PROVINCIA_DI_PARTENZA E PROVINCIA_DI_ARRIVO
		//log.debug( comunePartenza.getProvince().getSiglaProvincia()+" "+comuneArrivo.getProvince().getSiglaProvincia() );
		
		if ( comunePartenza.getProvince().getId() != comuneArrivo.getProvince().getId() 
				&& !comunePartenza.getProvince().getProvinceConfinanti().contains(comuneArrivo.getProvince()) ){
			try{
				//log.debug("INIZIO ricerca provincie tra Provincia di Partenza e Provincia Arrivo e anche secondo controllo traghetto...");
				Locale locale = new Locale.Builder().setLanguage("it").setRegion("IT").build();
				GMaps_Api GMaps_Api = new GMaps_Api();
				InfraProvincie = GMaps_Api.GoogleMaps_Directions(ricTransfert, locale.getLanguage());
			}catch(FerrysTraghettiException ft){
				MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.transfert.traghetto", null));
				resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
				return resultRicerca_Autista_Tariffe;
			}catch(GoogleMatrixException gme){
				MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("errors.googlemaps.matrix.temporary.unavailable", null));
				resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
				return resultRicerca_Autista_Tariffe;
			}
			//log.debug("... ricerca provincie tra Partenza e Arrivo e anche secondo controllo traghetto FINE");
		}
		log.debug("InfraProvincie: "+InfraProvincie);
		DammiTempoOperazione.DammiSecondi(startTime, "CalcoloTariffe_Main-1");
		// AGGIUNGO ANCHE LE PROVINCE ITALIANE VICINE ALLA PROVINCIA STRANIERA
		
		for(Province ite: comunePartenza.getProvince().getProvinceStraniere_provinceItaliane()) {
			InfraProvincie.add(ite.getId());
		}
		for(Province ite: comuneArrivo.getProvince().getProvinceStraniere_provinceItaliane()) {
			InfraProvincie.add(ite.getId());
		}
		
		List<Long> ProvinceTragitto_Id = new ArrayList<Long>(); 
		ProvinceTragitto_Id.add(idProvPartenza); 
		if( idProvPartenza != idProvArrivo ) { ProvinceTragitto_Id.add(idProvArrivo); }
		if(InfraProvincie != null) { ProvinceTragitto_Id.addAll(InfraProvincie); }
		resultRicerca_Autista_Tariffe.setProvinceTragitto_Id(ProvinceTragitto_Id);
		
		// RICERCA AUTISTI TERRITORIO
		resultRicercaAutistaList = RicercaAutisti(idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, idProvArrivo, idRegArrivo, InfraProvincie, NumeroPasseggeri, false,
				ricTransfert.isRitorno(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDataOraRitornoDate());
		AutistiDisponibiliList = getListaAutistiDisponibili(resultRicercaAutistaList, ricTransfert);
		NumMinimoAutistiCorsaMedia = ApplicationUtils.DammiNumMinimoAutistiCorsaMedia();
		GestioneApplicazione MaggNott_GestApp = gestioneApplicazioneDao.getName("MAGGIORAZIONE_NOTTURNA");
		MaggNott_OrarioNutturno = UtilString.RimuoviTuttiGliSpazi(MaggNott_GestApp.getValueString()).split("-");
		
		Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		MaggNott_Percentuale = (int)(long)MaggNott_GestApp.getValueNumber();

		PercentualeServizio = CalcoloPrezzi.CalcolaPercentualeMedia_TraDueProvince(comunePartenza.getProvince().getPercentualeServizio(), 
				comuneArrivo.getProvince().getPercentualeServizio());
		PercentualeVenditore = 0; //nuovo
		if(ricTransfert.getUserVenditore() != null) {
			PercentualeVenditore = TariffeVenditoreUtil.DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(ricTransfert.getUserVenditore().getId(), 
					comunePartenza.getProvince().getId(), comuneArrivo.getProvince().getId());
		}
		DammiTempoOperazione.DammiSecondi(startTime, "CalcoloTariffe_Main-2");
		//-------------------------------
		// TODO DA SVILUPPARE LA SITUAZIONE DI QUANDO CI SONO TRANSFER CON PIù DI 8 PASSEGGERI.......
		// TODO DEVO INVIARE I PREVENTIVI SOLO ALLE AZIENDE E HAI PROIVATI CHE HANNO PIù AUTOVEICOLI I QUALI COPRONO IL NUMERO DI PASSEGGERI RICHIESTO.
		if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || NumeroPasseggeri > 8 ) {
			return CalcolaParticolare(ricTransfert, resultRicerca_Autista_Tariffe, idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, 
					idProvArrivo, idRegArrivo, InfraProvincie, AutistiDisponibiliList);
			
		}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {
			resultRicerca_Autista_Tariffe.setTipoServizio(Constants.SERVIZIO_STANDARD);
			
			resultRicerca_Autista_Tariffe = CalcolaAgendaAutista(ricTransfert, resultRicerca_Autista_Tariffe, AutistiDisponibiliList);
			
			if( !ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(DATA_PRELEVAMENTO, Constants.SERVIZIO_STANDARD) 
					&& resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaAndata() == null
					&& resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaRitorno() == null ) {
				MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("errors.ora.partenza.corsa", null, Constants.SERVIZIO_STANDARD));
				resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
				
				if( gestioneApplicazioneDao.getName("CORSE_PARTICOLARI").getValueNumber() == 1l && AutistiDisponibiliList.size() >= 1 ) {
					MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.tuttavia.abbiamo.autisti.a.cui.puoi.richiedere.preventivo", null));
					resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
					return CalcolaParticolare(ricTransfert, resultRicerca_Autista_Tariffe, idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, 
							idProvArrivo, idRegArrivo, InfraProvincie, AutistiDisponibiliList);
				}
			}
			
			if( !ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(DATA_PRELEVAMENTO, Constants.SERVIZIO_STANDARD) ) {
				AutistiDisponibiliList.clear();
			}
			
			if( NumberUtil.removeDuplicatesLong(AutistiDisponibiliList).size() < NumMinimoAutistiCorsaMedia 
				&& resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaAndata() == null
				&& resultRicerca_Autista_Tariffe.getResultAgendaAutista().getAgendaAutista_AutistaRitorno() == null ) {
				if( gestioneApplicazioneDao.getName("CORSE_PARTICOLARI").getValueNumber() == 1l && AutistiDisponibiliList.size() >= 1 ) {
					MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.numero.autisti.non.sufficiente.richiedi.preventivi", 
							new Object[]{NumMinimoAutistiCorsaMedia, NumberUtil.removeDuplicatesLong(AutistiDisponibiliList).size()}));
					resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
					return CalcolaParticolare(ricTransfert,  resultRicerca_Autista_Tariffe, idComPartenza , idProvPartenza , idRegPartenza, idComArrivo, idProvArrivo, 
							idRegArrivo, InfraProvincie, AutistiDisponibiliList);
				}
				
				int iteNumeroPasseggeri = 1; 
				int result = getListaAutistiDisponibili(RicercaAutisti(idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, idProvArrivo, idRegArrivo, 
						InfraProvincie, iteNumeroPasseggeri, false, ricTransfert.isRitorno(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDataOraRitornoDate()), 
						ricTransfert).size();
				if( result < NumMinimoAutistiCorsaMedia ) {
					MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.autisti.non.disponibili.provincia", null, Constants.SERVIZIO_PARTICOLARE));
					resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
					return resultRicerca_Autista_Tariffe;
				
				}else {
					iteNumeroPasseggeri++; int numeroMaxPasseggeri = 0;
					while(true) {
						result = getListaAutistiDisponibili(RicercaAutisti(idComPartenza, idProvPartenza, idRegPartenza, idComArrivo, idProvArrivo, idRegArrivo, 
								InfraProvincie, iteNumeroPasseggeri, false, ricTransfert.isRitorno(), ricTransfert.getDataOraPrelevamentoDate(), 
								ricTransfert.getDataOraRitornoDate()), ricTransfert).size();
						if( result >= NumMinimoAutistiCorsaMedia ) {
							numeroMaxPasseggeri = iteNumeroPasseggeri;
							iteNumeroPasseggeri++;
						}else {
							break;
						}
					}
					// TODO MESSAGGIO: Non ci sono Auto per il numero Passeggeri richiesto, massimo numero passegeri {0}
					MessaggiEsitoRicerca.add( new MessaggioEsitoRicerca("messaggio.autisti.non.disponibili.provare.numero.passeggeri.diverso", 
							new Object[]{numeroMaxPasseggeri}, Constants.SERVIZIO_STANDARD));
					resultRicerca_Autista_Tariffe.setMessaggiEsitoRicerca( MessaggiEsitoRicerca );
					return resultRicerca_Autista_Tariffe;
				}
			}else {
				return Elaborazione_Servizio(ricTransfert,  resultRicerca_Autista_Tariffe, AutistiDisponibiliList);
			}
		}
		return null;
	}
		
	
}

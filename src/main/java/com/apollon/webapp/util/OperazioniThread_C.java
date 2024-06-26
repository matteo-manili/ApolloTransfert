package com.apollon.webapp.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import com.apollon.Constants;
import com.apollon.dao.RichiestaAutistaMedioAutoveicoloDao;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.util.UtilBukowski;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;

public class OperazioniThread_C extends ApplicationUtils implements Runnable {
	
	private static final Log log = LogFactory.getLog(OperazioniThread_C.class);
	
	private  RichiestaMediaDao richiestaMediaDao = (RichiestaMediaDao) contextDao.getBean("RichiestaMediaDao");
	private  RichiestaAutistaMedioDao richiestaAutistaMedioDao = (RichiestaAutistaMedioDao) contextDao.getBean("RichiestaAutistaMedioDao");
	private  RichiestaAutistaMedioAutoveicoloDao richiestaAutistaMedioAutoveicoloDao = (RichiestaAutistaMedioAutoveicoloDao) contextDao.getBean("RichiestaAutistaMedioAutoveicoloDao");
	private  RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	
	public  RicercaTransfert ricTransfertNew;
	public  ResultRicerca_Autista_Tariffe RIS;
	
	public OperazioniThread_C(RicercaTransfert ricTransfertNew, ResultRicerca_Autista_Tariffe RIS) {
		this.ricTransfertNew = ricTransfertNew;
		this.RIS = RIS;
	}
	
	
	@Override
	public void run() {
		if( ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
			
			final String[] ParametriCorseParticolari = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("CORSE_PARTICOLARI").getValueString()).split("-");
			int percentualeServizio = 0;
			if( ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
				percentualeServizio = Integer.parseInt(ParametriCorseParticolari[0]); 
			}else if( ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				percentualeServizio = Integer.parseInt(ParametriCorseParticolari[1]);
			}
			for(ResultRicerca_Autista_Tariffe.ResultMedio resultMedio_ite: RIS.getResultMedio()) {
				for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista RIS_AUTISTA_ITE: resultMedio_ite.getResultMedioAutista()) {
					for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo RIS_AUTO_ITE: RIS_AUTISTA_ITE.getRisultAutistaMedioAutoveicolo()) {
						RichiestaAutistaParticolare richPartic = new RichiestaAutistaParticolare();
						richPartic.setAutoveicolo(RIS_AUTO_ITE.getAutoveicolo());
						richPartic.setClasseAutoveicoloScelta(resultMedio_ite.getClasseAutoveicolo());
						richPartic.setDataChiamataPrenotata(null);
						richPartic.setInvioSms(false);
						richPartic.setInvioSmsCorsaConfermata(false);
						richPartic.setRimborsoCliente(null);
						richPartic.setPercentualeServizio( percentualeServizio );
						richPartic.setPrezzoCommissioneServizio(new BigDecimal("0"));
						richPartic.setPrezzoCommissioneServizioIva(new BigDecimal("0"));
						richPartic.setPrezzoTotaleAutista(new BigDecimal("0"));
						String InizioParteToken = "";
						if(ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
							InizioParteToken = Constants.SERVIZIO_PARTICOLARE;
						}else if(ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
							InizioParteToken = Constants.SERVIZIO_MULTIPLO;
						}
						while(true) {
							String token = UtilBukowski.getRandomToken_RichiestaAutista(Constants.LUNGHEZZA_URL_TOKEN_GENERALE, InizioParteToken);
							if( richiestaAutistaParticolareDao.getRichiestaAutista_by_token(token) == null ) {
								richPartic.setToken( token );
								break;
							}
						}
						richPartic.setRicercaTransfert( ricTransfertNew );
						richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare(richPartic);
					}
				}
			}
			
		}else if(ricTransfertNew.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
			if(RIS.getResultMedio() != null && RIS.getResultMedio().size() > 0) {
				for(ResultRicerca_Autista_Tariffe.ResultMedio resultMedio_ite: RIS.getResultMedio()) {
					RichiestaMedia richiestaMedia = new RichiestaMedia();
					richiestaMedia.setRicercaTransfert( ricTransfertNew );
					richiestaMedia.setRimborsoCliente(null);
					richiestaMedia.setClasseAutoveicolo(resultMedio_ite.getClasseAutoveicolo());
					richiestaMedia.setPrezzoTotaleCliente(resultMedio_ite.getPrezzoTotaleCliente());
					richiestaMedia.setMaggiorazioneNotturna(resultMedio_ite.getMaggiorazioneNotturna());
					richiestaMedia.setTariffaPerKm(resultMedio_ite.getTariffaPerKm());
					richiestaMedia.setPrezzoTotaleAutista(resultMedio_ite.getPrezzoTotaleAutista());
					richiestaMedia.setPrezzoCommissioneServizio(resultMedio_ite.getPrezzoCommissioneServizio());
					richiestaMedia.setPrezzoCommissioneServizioIva(resultMedio_ite.getPrezzoCommissioneServizioIva());
					richiestaMedia.setPrezzoCommissioneVenditore(resultMedio_ite.getPrezzoCommissioneVenditore());
					RichiestaMedia richiestaMediaNew = richiestaMediaDao.saveRichiestaMedia(richiestaMedia);
					RichiestaMediaAutista richiestaMediaAutistaNew;
					int OrdineAutista = 0;
					for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista RIS_AUTISTA_ITE: resultMedio_ite.getResultMedioAutista()) {
						RichiestaMediaAutista richiestaMediaAutista = new RichiestaMediaAutista();
						richiestaMediaAutista.setRichiestaMedia(richiestaMediaNew);
						richiestaMediaAutista.setAutista( RIS_AUTISTA_ITE.getAutista() );
						richiestaMediaAutista.setPrezzoTotaleAutista( RIS_AUTISTA_ITE.getPrezzoTotaleAutista() );
						richiestaMediaAutista.setTariffaPerKm(RIS_AUTISTA_ITE.getTariffaPerKm());
						richiestaMediaAutista.setPrezzoCommissioneServizio( RIS_AUTISTA_ITE.getPrezzoCommissioneServizio() );
						richiestaMediaAutista.setPrezzoCommissioneServizioIva( RIS_AUTISTA_ITE.getPrezzoCommissioneServizioIva() );
						richiestaMediaAutista.setPrezzoCommissioneVenditore( RIS_AUTISTA_ITE.getPrezzoCommissioneVenditore() );
						OrdineAutista++;
						richiestaMediaAutista.setOrdineAutista( OrdineAutista );
						while(true){ // faccio 	questo ciclo perché nel caso viene assegnato un token uguale ad un altro si solleva la DataIntegrityViolationException, allora lo rigenera un altro.
						    try{
						    	richiestaMediaAutista.setTokenAutista( UtilBukowski.getRandomToken_RichiestaAutista(Constants.LUNGHEZZA_URL_TOKEN_GENERALE, Constants.SERVIZIO_STANDARD));
						    	richiestaMediaAutistaNew = richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richiestaMediaAutista );
						    	break;
						    }catch(DataIntegrityViolationException duplic) {
						    	log.debug( duplic.getMessage() );
						    	log.debug("..token duplicato..");
						    }
						}
						for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo RIS_AUTO_ITE: RIS_AUTISTA_ITE.getRisultAutistaMedioAutoveicolo()){
							RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo = new RichiestaMediaAutistaAutoveicolo();
							richiestaMediaAutistaAutoveicolo.setAutoveicolo(RIS_AUTO_ITE.getAutoveicolo());
							richiestaMediaAutistaAutoveicolo.setRichiestaAutistaMedio(richiestaMediaAutistaNew);
							richiestaAutistaMedioAutoveicoloDao.saveRichiestaAutistaMedioAutoveicolo(richiestaMediaAutistaAutoveicolo);
						}
					}
				}
			}
		}
		
	}



	
	
	
}

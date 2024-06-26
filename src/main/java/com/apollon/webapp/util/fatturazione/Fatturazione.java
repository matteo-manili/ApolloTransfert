package com.apollon.webapp.util.fatturazione;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.Constants;
import com.apollon.dao.FattureDao;
import com.apollon.model.Autista;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Fatturazione extends ApplicationUtils{
	private static final Log log = LogFactory.getLog(Fatturazione.class);
	
	private static FattureDao fattureDao = (FattureDao) contextDao.getBean("FattureDao");

	public static void CreaFattura(RicercaTransfert ricTransfert) {
		try {
			Fatture fattura = new Fatture();
			fattura.setRicercaTransfert( ricTransfert );
			//if(ricTransfert.getApprovazioneAndata() == Constants.APPROVATA){
				fattura.setProgressivoFattura( fattureDao.dammiNumeroProgressivoFattura() );
			//}
			fattureDao.saveFatture(fattura);
		}catch (final DataIntegrityViolationException dataIntegrViolException) {
			log.debug(dataIntegrViolException.getMessage());
        }
	}

	
	public static BeanInfoFattura_Corsa Informazioni_FatturaCorsa(long courseId) throws Exception {
		BeanInfoFattura_Corsa fattCorsa = new BeanInfoFattura_Corsa(); RicercaTransfert ricTransfert = ricercaTransfertDao.get( courseId );
		
		if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) ) {
			AgendaAutistaScelta ageAutistScelta = ricTransfert.getAgendaAutistaScelta();
			fattCorsa.setRicTransfert(ricTransfert);
			Fatture fattura = fattureDao.getFatturaBy_IdRicercaTransfert(ricTransfert.getId());
			Fatture fatturaRimborso = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
			if( fatturaRimborso != null ) {
				fattCorsa.setNumeroProgressivo(fatturaRimborso.getProgressivoFattura().toString());
			}else{
				fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
			}
			fattCorsa.setAgendaAutistaScelta(ageAutistScelta);
			fattCorsa.setPrezzoServizio(ageAutistScelta.getPrezzoCommissioneServizioTotale().toString());
			fattCorsa.setPrezzoIva(ageAutistScelta.getPrezzoCommissioneServizioIvaTotale().toString());
			fattCorsa.setPrezzoTotaleServizio(ageAutistScelta.getPrezzoCommissioneServizioTotale().add(ageAutistScelta.getPrezzoCommissioneServizioIvaTotale()).toString());
			fattCorsa.setPrezzoCliente(ageAutistScelta.getPrezzoTotaleCliente().toString());
			fattCorsa.setPrezzoAutista(ageAutistScelta.getPrezzoTotaleAutisti().toString());
			fattCorsa.setMaggiorazioneNotturna( null );
			fattCorsa.setAutista(null);
			fattCorsa.setAutistaConfermato( true );
			if( ricTransfert.getAgendaAutista_RimborsoCliente() != null ){
				fattCorsa.setRimborsoCliente(ricTransfert.getAgendaAutista_RimborsoCliente().toString());
			}else{
				fattCorsa.setRimborsoCliente( BigDecimal.ZERO.setScale(2).toString() );
			}
			
		}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {
			RichiestaMediaAutista richMediaAutista = fattureDao.getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata( courseId );
			if( richMediaAutista != null ){
				fattCorsa.setRicTransfert(richMediaAutista.getRichiestaMedia().getRicercaTransfert());
				Fatture fattura = fattureDao.getFatturaBy_IdRicercaTransfert( richMediaAutista.getRichiestaMedia().getRicercaTransfert().getId() );
				Fatture fatturaRimborso = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( richMediaAutista.getRichiestaMedia().getRicercaTransfert().getId() );
				if( fatturaRimborso != null ){
					fattCorsa.setNumeroProgressivo(fatturaRimborso.getProgressivoFattura().toString());
				}else{
					fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
				}
				fattCorsa.setPrezzoServizio(richMediaAutista.getPrezzoCommissioneServizio().toString());
				fattCorsa.setPrezzoIva(richMediaAutista.getPrezzoCommissioneServizioIva().toString());
				fattCorsa.setPrezzoTotaleServizio(richMediaAutista.getPrezzoCommissioneServizio().add( richMediaAutista.getPrezzoCommissioneServizioIva() ).toString());
				fattCorsa.setPrezzoCliente(richMediaAutista.getRichiestaMedia().getPrezzoTotaleCliente().toString());
				fattCorsa.setPrezzoAutista(richMediaAutista.getRichiestaMedia().getPrezzoTotaleAutista().toString());
				fattCorsa.setMaggiorazioneNotturna( (richMediaAutista.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? 
						richMediaAutista.getRichiestaMedia().getMaggiorazioneNotturna().toString() : null );
				fattCorsa.setAutista(richMediaAutista.getAutista());
				fattCorsa.setAutistaConfermato( richMediaAutista.isCorsaConfermata() );
				if( richMediaAutista.getRichiestaMedia().getRimborsoCliente() != null ){
					fattCorsa.setRimborsoCliente(richMediaAutista.getRichiestaMedia().getRimborsoCliente().toString());
				}else{
					fattCorsa.setRimborsoCliente( BigDecimal.ZERO.setScale(2).toString() );
				}
			}
		}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
			RichiestaAutistaParticolare richAutPart = ricTransfert.getRichiestaAutistaParticolareAcquistato();
			fattCorsa.setRicTransfert(richAutPart.getRicercaTransfert());
			Fatture fattura = fattureDao.getFatturaBy_IdRicercaTransfert(richAutPart.getRicercaTransfert().getId());
			Fatture fatturaRimborso = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( richAutPart.getRicercaTransfert().getId() );
			if( fatturaRimborso != null ){
				fattCorsa.setNumeroProgressivo(fatturaRimborso.getProgressivoFattura().toString());
			}else{
				fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
			}
			fattCorsa.setPrezzoServizio(richAutPart.getPrezzoCommissioneServizio().toString());
			fattCorsa.setPrezzoIva(richAutPart.getPrezzoCommissioneServizioIva().toString());
			fattCorsa.setPrezzoTotaleServizio(richAutPart.getPrezzoCommissioneServizio().add( richAutPart.getPrezzoCommissioneServizioIva() ).toString());
			fattCorsa.setPrezzoCliente( richAutPart.getPrezzoTotaleCliente().toString() );
			fattCorsa.setPrezzoAutista(richAutPart.getPrezzoTotaleAutista().toString());
			fattCorsa.setMaggiorazioneNotturna( null );
			fattCorsa.setAutista(richAutPart.getAutoveicolo().getAutista());
			fattCorsa.setAutistaConfermato( true );
			if( richAutPart.getRimborsoCliente() != null ){
				fattCorsa.setRimborsoCliente(richAutPart.getRimborsoCliente().toString());
			}else{
				fattCorsa.setRimborsoCliente( BigDecimal.ZERO.setScale(2).toString() );
			}
		}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
			List<RichiestaAutistaParticolare> richiestaAutistaParticolareAcquistato_Multiplo = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
			fattCorsa.setRicTransfert(ricTransfert);
			Fatture fattura = fattureDao.getFatturaBy_IdRicercaTransfert(ricTransfert.getId());
			Fatture fatturaRimborso = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
			if( fatturaRimborso != null ) {
				fattCorsa.setNumeroProgressivo(fatturaRimborso.getProgressivoFattura().toString());
			}else{
				fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
			}
			BigDecimal prezzoServizio = BigDecimal.ZERO; BigDecimal prezzoIva = BigDecimal.ZERO; BigDecimal prezzoTotaleServizio = BigDecimal.ZERO;
			BigDecimal prezzoCliente = BigDecimal.ZERO; BigDecimal prezzoAutista = BigDecimal.ZERO; List<Autista> autistaMultiploList = new ArrayList<Autista>();
			for(RichiestaAutistaParticolare ite: richiestaAutistaParticolareAcquistato_Multiplo) {
				prezzoServizio = prezzoServizio.add( ite.getPrezzoCommissioneServizio() );
				prezzoIva = prezzoIva.add( ite.getPrezzoCommissioneServizioIva() );
				prezzoTotaleServizio = prezzoTotaleServizio.add( ite.getPrezzoCommissioneServizio().add(ite.getPrezzoCommissioneServizioIva()) );
				prezzoCliente = prezzoCliente.add( ite.getPrezzoTotaleCliente() );
				prezzoAutista = prezzoAutista.add( ite.getPrezzoTotaleAutista() );
				autistaMultiploList.add(ite.getAutoveicolo().getAutista());
			}
			fattCorsa.setPrezzoServizio(prezzoServizio.toString());
			fattCorsa.setPrezzoIva(prezzoIva.toString());
			fattCorsa.setPrezzoTotaleServizio(prezzoTotaleServizio.toString());
			fattCorsa.setPrezzoCliente(prezzoCliente.toString());
			fattCorsa.setPrezzoAutista(prezzoAutista.toString());
			fattCorsa.setMaggiorazioneNotturna(null);
			fattCorsa.setAutista(null);
			fattCorsa.setAutistaServizioMultiplo(autistaMultiploList);
			fattCorsa.setAutistaConfermato(true);
			if( ricTransfert.getRichiestaAutistaMultiploRimborsoCliente() != null ){
				fattCorsa.setRimborsoCliente(ricTransfert.getRichiestaAutistaMultiploRimborsoCliente().toString());
			}else{
				fattCorsa.setRimborsoCliente( BigDecimal.ZERO.setScale(2).toString() );
			}
		}
		return fattCorsa;
	}
	
	/**
	 * Minimo dei Dati per Fare una Fattura
	 */
	public static BeanInfoFattura_Corsa Informazioni_FatturaCorsa_VenditoreTest(RichiestaMedia richiestaMedia, RicercaTransfert ricercaTransfert){
		BeanInfoFattura_Corsa fattCorsa = new BeanInfoFattura_Corsa();
			fattCorsa.setRicTransfert(ricercaTransfert);
			fattCorsa.setNumeroProgressivo("0");
			fattCorsa.setPrezzoIva(richiestaMedia.getPrezzoCommissioneServizioIva().setScale(2, RoundingMode.HALF_EVEN).toString());
			fattCorsa.setPrezzoCliente(richiestaMedia.getPrezzoTotaleCliente().setScale(2, RoundingMode.HALF_EVEN).toString());
			fattCorsa.setAutistaConfermato( false );
			fattCorsa.setRimborsoCliente( BigDecimal.ZERO.setScale(2).toString() );
		return fattCorsa;
	}
	
	/**
	 * Il commercialista potrà scaricare anche la fattura originale prima del rimborso
	 */
	@Deprecated
	public static BeanInfoFattura_Corsa Informazioni_FatturaCorsa_Commercialista(long idFattura){
		BeanInfoFattura_Corsa fattCorsa = new BeanInfoFattura_Corsa();
		Fatture fattura = fattureDao.get(idFattura);
		if(fattura.getRicercaTransfert() != null || fattura.getRicercaTransfertRimborso() != null){
			long idRicercaTransfert = (fattura.getRicercaTransfert() != null) ? fattura.getRicercaTransfert().getId() : fattura.getRicercaTransfertRimborso().getId();
			
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(idRicercaTransfert); // TODO NUOVO PRIMA NON C'ERA
			
			if( ricTransfert.getTipoServizio() == null || ricTransfert.getTipoServizio().equals( Constants.SERVIZIO_STANDARD ) ) {  // TODO NUOVO PRIMA NON C'ERA
				RichiestaMediaAutista richMediaAutista = fattureDao.getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata( idRicercaTransfert );
				if( richMediaAutista != null ){
					fattCorsa.setRicTransfert(richMediaAutista.getRichiestaMedia().getRicercaTransfert());
					fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
					fattCorsa.setPrezzoServizio(richMediaAutista.getPrezzoCommissioneServizio().toString());
					fattCorsa.setPrezzoIva(richMediaAutista.getPrezzoCommissioneServizioIva().toString());
					fattCorsa.setPrezzoTotaleServizio(richMediaAutista.getPrezzoCommissioneServizio().add( richMediaAutista.getPrezzoCommissioneServizioIva() ).toString());
					fattCorsa.setPrezzoCliente(richMediaAutista.getRichiestaMedia().getPrezzoTotaleCliente().toString());
					fattCorsa.setPrezzoAutista(richMediaAutista.getRichiestaMedia().getPrezzoTotaleAutista().toString());
					fattCorsa.setMaggiorazioneNotturna( (richMediaAutista.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? 
							richMediaAutista.getRichiestaMedia().getMaggiorazioneNotturna().toString() : null );
					fattCorsa.setAutista(richMediaAutista.getAutista());
					fattCorsa.setAutistaConfermato( richMediaAutista.isCorsaConfermata() );
					if( fattura.getRicercaTransfertRimborso() != null ){
						fattCorsa.setRimborsoCliente(richMediaAutista.getRichiestaMedia().getRimborsoCliente().toString());
					}else{
						fattCorsa.setRimborsoCliente(BigDecimal.ZERO.setScale(2).toString());
					}
				}
				
			}else if( ricTransfert.getTipoServizio().equals( Constants.SERVIZIO_PARTICOLARE ) ) {  // TODO NUOVO PRIMA NON C'ERA
				RichiestaAutistaParticolare richAutPart = ricTransfert.getRichiestaAutistaParticolareAcquistato();
				fattCorsa.setRicTransfert(richAutPart.getRicercaTransfert());
				fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
				fattCorsa.setPrezzoServizio(richAutPart.getPrezzoCommissioneServizio().toString());
				fattCorsa.setPrezzoIva(richAutPart.getPrezzoCommissioneServizioIva().toString());
				fattCorsa.setPrezzoTotaleServizio(richAutPart.getPrezzoCommissioneServizio().add( richAutPart.getPrezzoCommissioneServizioIva() ).toString());
				fattCorsa.setPrezzoCliente(richAutPart.getPrezzoTotaleAutista().add(richAutPart.getPrezzoCommissioneServizio())
						.add(richAutPart.getPrezzoCommissioneServizioIva()).toString() );
				fattCorsa.setPrezzoAutista(richAutPart.getPrezzoTotaleAutista().toString());
				fattCorsa.setMaggiorazioneNotturna( null );
				fattCorsa.setAutista(richAutPart.getAutoveicolo().getAutista());
				fattCorsa.setAutistaConfermato( true );
				if( fattura.getRicercaTransfertRimborso() != null ){
					fattCorsa.setRimborsoCliente(richAutPart.getRimborsoCliente().toString());
				}else{
					fattCorsa.setRimborsoCliente(BigDecimal.ZERO.setScale(2).toString());
				}
				
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				List<RichiestaAutistaParticolare> richiestaAutistaParticolareAcquistato_Multiplo = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
				fattCorsa.setRicTransfert(ricTransfert);
				Fatture fatturaRimborso = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
				if( fatturaRimborso != null ) {
					fattCorsa.setNumeroProgressivo(fatturaRimborso.getProgressivoFattura().toString());
				}else{
					fattCorsa.setNumeroProgressivo(fattura.getProgressivoFattura().toString());
				}
				BigDecimal prezzoServizio = BigDecimal.ZERO; BigDecimal prezzoIva = BigDecimal.ZERO; BigDecimal prezzoTotaleServizio = BigDecimal.ZERO;
				BigDecimal prezzoCliente = BigDecimal.ZERO; BigDecimal prezzoAutista = BigDecimal.ZERO; List<Autista> autistaMultiploList = new ArrayList<Autista>();
				for(RichiestaAutistaParticolare ite: richiestaAutistaParticolareAcquistato_Multiplo) {
					prezzoServizio = prezzoServizio.add( ite.getPrezzoCommissioneServizio() );
					prezzoIva = prezzoIva.add( ite.getPrezzoCommissioneServizioIva() );
					prezzoTotaleServizio = prezzoTotaleServizio.add( ite.getPrezzoCommissioneServizio().add(ite.getPrezzoCommissioneServizioIva()) );
					prezzoCliente = prezzoCliente.add( ite.getPrezzoTotaleCliente() );
					prezzoAutista = prezzoAutista.add( ite.getPrezzoTotaleAutista() );
					autistaMultiploList.add(ite.getAutoveicolo().getAutista());
				}
				fattCorsa.setPrezzoServizio(prezzoServizio.toString());
				fattCorsa.setPrezzoIva(prezzoIva.toString());
				fattCorsa.setPrezzoTotaleServizio(prezzoTotaleServizio.toString());
				fattCorsa.setPrezzoCliente(prezzoCliente.toString());
				fattCorsa.setPrezzoAutista(prezzoAutista.toString());
				fattCorsa.setMaggiorazioneNotturna(null);
				fattCorsa.setAutista(null);
				fattCorsa.setAutistaServizioMultiplo(autistaMultiploList);
				fattCorsa.setAutistaConfermato(true);
				if( fattura.getRicercaTransfertRimborso() != null ){
					fattCorsa.setRimborsoCliente(ricTransfert.getRichiestaAutistaMultiploRimborsoCliente().toString());
				}else{
					fattCorsa.setRimborsoCliente(BigDecimal.ZERO.setScale(2).toString());
				}
			}
		}
		return fattCorsa;
	}
	
	
}

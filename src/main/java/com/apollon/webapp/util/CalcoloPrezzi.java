package com.apollon.webapp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.apollon.Constants;
import com.apollon.model.AutistaZone;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public final class CalcoloPrezzi extends ApplicationUtils {
	
	
	/**
	 * Mi restituisce il prezzo secondo la tariffa più bassa della provncia dell'autista. Il valore di MaxPercentualeRibasso fa abbassare di TOT percento il prezzo. 
	 * 
	 * @param richAutistPart
	 * @param MaxPercentualeRibasso
	 */
	public static BigDecimal DammiPrezzo_NO_GUERRA_PREZZO_AL_RIBASSO_NCC(RichiestaAutistaParticolare richAutistPart, int MaxPercentualeRibasso) {
		try {
			final String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
			ObjectMapper mapper = new ObjectMapper();
			List<Long> ProvinceTragitto_Id = Arrays.asList(mapper.readValue(richAutistPart.getRicercaTransfert().getProvinceTragitto_Id().toString(), Long[].class));
			
			List<BigDecimal> tariffaProvPiùBassa = new ArrayList<BigDecimal>();
			for(AutistaZone ite: richAutistPart.getAutoveicolo().getAutista().getAutistaZone()) {
				if( ite.getProvince() != null && ProvinceTragitto_Id.contains( ite.getProvince().getId()) ) {
					tariffaProvPiùBassa.add(ite.getProvince().getTariffaBase());
				}
			}
			Optional<BigDecimal> min = tariffaProvPiùBassa.stream().min(Comparator.naturalOrder());
			if (min.isPresent()) {
				System.out.println("MIN: "+min.get());
			}
			long totaleMetriSoloAndata = richAutistPart.getRicercaTransfert().getDistanzaValue(); 
			long totaleMetriSoloRitorno = richAutistPart.getRicercaTransfert().getDistanzaValueRitorno();
			BigDecimal TariffaKm = new BigDecimal(TariffeUtil.DammiTariffa_ValoreAuto(Parametri, richAutistPart.getAutoveicolo().getClasseAutoveicoloReale(), 
					min.get(), Constants.FASCE_KILOMETRICHE, (int)(long)totaleMetriSoloAndata/1000));
			if(richAutistPart.getRicercaTransfert().isRitorno()) {
				TariffaKm = TariffaKm.add( 
						new BigDecimal(TariffeUtil.DammiTariffa_ValoreAuto(Parametri, richAutistPart.getAutoveicolo().getClasseAutoveicoloReale(), 
								min.get(), Constants.FASCE_KILOMETRICHE, (int)(long)totaleMetriSoloRitorno/1000)) );
			}
			
			BigDecimal PrezzoAutista = CalcoloPrezzi.CalcolaPrezzoPerMetri((int)(long)totaleMetriSoloAndata / 1000, TariffaKm, false);
			if(richAutistPart.getRicercaTransfert().isRitorno()) {
				PrezzoAutista = PrezzoAutista.add( CalcoloPrezzi.CalcolaPrezzoPerMetri((int)(long)totaleMetriSoloRitorno / 1000, TariffaKm, false) );
			}
			System.out.println("PrezzoAutista: "+PrezzoAutista);
			PrezzoAutista = PrezzoAutista.divide( new BigDecimal(100) ).multiply( new BigDecimal(100 - MaxPercentualeRibasso) ) ; 
			System.out.println("PrezzoAutista: "+PrezzoAutista);
			return PrezzoAutista.setScale(2, RoundingMode.HALF_EVEN);
			
		}catch(Exception e) {
			return null;
		}
	}
	
	
	
	public static BigDecimal DammiTotalePrezzoCliente(Long distanzaMetri, String[] Parametri, ClasseAutoveicolo ClasseAutoveicoloReale, 
			BigDecimal TariffaBase, long percentualeServizioDefault, long percentualeVenditore){
		int kilometri = (int)(long)(distanzaMetri/1000);
		BigDecimal TariffaKm = new BigDecimal(TariffeUtil.DammiTariffa_ValoreAuto(Parametri, ClasseAutoveicoloReale, TariffaBase, Constants.FASCE_KILOMETRICHE, (int)(long)(distanzaMetri/1000)));
		BigDecimal PrezzoAutista = CalcoloPrezzi.CalcolaPrezzoPerMetri(kilometri, TariffaKm, false);
		BigDecimal PrezzoCommissioneServizio = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutista, (int)(long)percentualeServizioDefault);
		BigDecimal PrezzoCommissioneServizioIva = CalcoloPrezzi.CalcolaPrezzoIva(PrezzoCommissioneServizio);
		
		BigDecimal PrezzoCommissioneVenditore = CalcoloPrezzi.CalcolaPercentuale(PrezzoAutista, (int)(long)percentualeVenditore);
		
		return PrezzoAutista.add(PrezzoCommissioneServizio.add(PrezzoCommissioneServizioIva).add(PrezzoCommissioneVenditore)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	/**
	 * Mi ritorna la Tariffa Più alta
	 */
	public static BigDecimal DammiTariffaPiuAlta(BigDecimal TariffaBase1, BigDecimal TariffaBase2){
		if(TariffaBase1.compareTo(TariffaBase2) > 0){
			return TariffaBase1;
		}else{
			return TariffaBase2;
		}
	}
	
	
	/**
	 * Importate qui faccio il calcolo per kilometro
	 */
	public static BigDecimal CalcolaPrezzoPerMetri(int kilometri, BigDecimal TariffaKm, boolean ScontoAttesaRitorno){
		BigDecimal result = new BigDecimal(kilometri).multiply(TariffaKm);
		if(ScontoAttesaRitorno){
			BigDecimal sconto = CalcolaPercentuale(result, (int)(long)gestioneApplicazioneDao.getName("PERCENTUALE_SCONTO_RITORNO").getValueNumber());
			result = result.subtract(sconto);
		}
		return result.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	/**
	 * Esegui Sconto Ritorno
	 */
	public static BigDecimal EseguiScontoRitorno(BigDecimal result){
		BigDecimal sconto = CalcolaPercentuale(result, (int)(long)gestioneApplicazioneDao.getName("PERCENTUALE_SCONTO_RITORNO").getValueNumber());
		result = result.subtract(sconto);
		return result.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	public static long CalcolaPercentualeMedia_TraDueProvince(Integer PercentualeProvincia_1, Integer PercentualeProvincia_2){
		return (PercentualeProvincia_1 + PercentualeProvincia_2) / 2;
	}
	
	
	/**
	 * Mi ritorna la percentuale calcolata, ad esempio il 20% di 100 mi ritorna 20
	 */
	public static BigDecimal CalcolaPercentuale(BigDecimal Base, int percentualeInt){
		final BigDecimal PERCENTUALE_BD = new BigDecimal(percentualeInt);
		BigDecimal prezzoPercentualeBigDecimal = Base.divide( new BigDecimal(100) ).multiply(PERCENTUALE_BD); 
		//BigDecimal risultato =  PREZZO_BASE_ORIGINAL.add( prezzoPercentualeBigDecimal ).setScale(2, RoundingMode.HALF_EVEN);
		return prezzoPercentualeBigDecimal; //.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	public static BigDecimal CalcolaPrezzoRitardo(int numeroMezzoreRitardo ){
		BigDecimal PrezzoOrarioRitardoCliete = new BigDecimal( gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueString() );
		long PercentualeServiziorRitardoCliete = gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueNumber();
		BigDecimal prezzoRitardoClieteConCostoServizio = CalcoloPrezzi.CalcolaPercentuale(PrezzoOrarioRitardoCliete, (int) PercentualeServiziorRitardoCliete);
		BigDecimal prezzoMezzoraRitardoClienteConTassaServizio = PrezzoOrarioRitardoCliete.add(prezzoRitardoClieteConCostoServizio);
		prezzoMezzoraRitardoClienteConTassaServizio = prezzoMezzoraRitardoClienteConTassaServizio.divide( new BigDecimal(2) );
		BigDecimal risultato = prezzoMezzoraRitardoClienteConTassaServizio.multiply( new BigDecimal(numeroMezzoreRitardo) );
		return risultato.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	public static BigDecimal CalcolaPrezzoIva(BigDecimal commissioneServizio){
		BigDecimal prezzoIva = commissioneServizio.divide( new BigDecimal(100) ).multiply( DammiTassaIVA ); 
		return prezzoIva.setScale(2, RoundingMode.HALF_EVEN); //.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	public static Double CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(Double base, int percentualeInt){
		return base / 100 * percentualeInt;
	}
	
	
	public static BigDecimal CalcolaTariffas(Long metriLong, BigDecimal prezzo){
		BigDecimal km = new BigDecimal(metriLong).divide(new BigDecimal(1000)) ;
		prezzo = prezzo.divide(km, RoundingMode.HALF_UP);
		return prezzo.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	public static BigDecimal dammiTariffaPiuAltaBigDecimal(List<BigDecimal> tariffaListBD){
		return Collections.max(tariffaListBD);
	}
	

	public static BigDecimal dammiTariffaMediaBigDecimal(List<BigDecimal> tariffaListBD){
		BigDecimal sumBd = BigDecimal.ZERO;
		BigDecimal resutlBd = BigDecimal.ZERO;
		if(!tariffaListBD.isEmpty()) {
			for(BigDecimal mark : tariffaListBD) {
				//if(mark!=null)
					sumBd = sumBd.add( mark );
			}
			//result.d = sumBd.divid (tariffaListBD.size());
			resutlBd = sumBd.divide ( new BigDecimal(tariffaListBD.size()), 2 );
		}
		return resutlBd;
	}
	
	
	public static Long dammiTariffaMediaLong(List<Long> listPrezzi){
		Long sum = 0l;
		Long result = 0l;
		if(!listPrezzi.isEmpty()) {
			for(Long ite : listPrezzi) {
				sum = sum + ite;
			}
			result = sum / listPrezzi.size();
		}
		return result;
	}
	
	
	public static Double DammiTariffaMediaDouble(List<Double> listPrezzi){
		Double sum = 0d;
		Double result = 0d;
		if(!listPrezzi.isEmpty()) {
			for(Double ite : listPrezzi) {
				sum = sum + ite;
			}
			result = sum / listPrezzi.size();
		}
		return result;
	}
}

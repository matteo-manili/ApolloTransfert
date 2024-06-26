package com.apollon.webapp.util.controller.tariffe;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.apollon.Constants;
import com.apollon.dao.AeroportiDao;
import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.dao.DistanzeProvinceInfrastruttureDao;
import com.apollon.dao.MuseiDao;
import com.apollon.dao.PortiNavaliDao;
import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.DistanzeProvinceInfrastrutture;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.VenditorePercServProvincia;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.bean.TransferTariffeProvince.Tariffa_Province_e_Infrastrutture;
import com.apollon.webapp.util.bean.TransferTariffeProvince.Tariffa_Province_e_Infrastrutture.TariffaClasseAuto;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.google.maps.errors.ApiException;
import com.apollon.webapp.util.bean.TransferTariffeProvince;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TariffeTransferProvincieUtil extends MenuTariffeTransfer {

	private static final Log log = LogFactory.getLog(TariffeTransferProvincieUtil.class);
	private static ClasseAutoveicoloDao classeAutoveicoloDao = (ClasseAutoveicoloDao) contextDao.getBean("ClasseAutoveicoloDao");
	private static AeroportiDao aeroportiDao = (AeroportiDao) contextDao.getBean("AeroportiDao");
	private static PortiNavaliDao portiNavaliDao = (PortiNavaliDao) contextDao.getBean("PortiNavaliDao");
	private static MuseiDao museiDao = (MuseiDao) contextDao.getBean("MuseiDao");
	private static DistanzeProvinceInfrastruttureDao distanzeProvInfraDao = (DistanzeProvinceInfrastruttureDao) contextDao.getBean("DistanzeProvinceInfrastruttureDao");
	public static VenditorePercServProvinciaDao venditorePercServProvincia = (VenditorePercServProvinciaDao) contextDao.getBean("VenditorePercServProvinciaDao");
	
    /**
     * Mi ritornano gli elementi del Tariffario passandogli una lista di provincie o una sola Provincia
     */
    public static TransferTariffeProvince DammiTransferTariffeProvince(TerritorioAndataArrivo territorioAndataArrivo, Locale locale, String[] Parametri, Long idUserVenditore) 
    		throws NullPointerException, ApiException, InterruptedException, IOException {
    	log.debug("DammiTransferTariffeProvince");
    	TransferTariffeProvince transferTariffeProvince = new TransferTariffeProvince();
    	List<Tariffa_Province_e_Infrastrutture> tariffa_Province_e_InfrastruttureList = new ArrayList<Tariffa_Province_e_Infrastrutture>();
    	List<ClasseAutoveicolo> classeAutoveicoloList = classeAutoveicoloDao.getClasseAutoveicolo();
    	// Una Richiesta Distanza Fatta con Google Maps viene salvata nella Tabella Data_Distanze 
    	// e se è più vecchia di 6 settimane allora dovrà essere reseguita
    	List<VenditorePercServProvincia> venditorePercServProvinciaList =
    			(idUserVenditore != null) ? venditorePercServProvincia.getVenditorePercServProvincia_by_Venditore(idUserVenditore) : null;
    	HashMap<Long, Integer> hmapPercentualiProvinceVenditore = TariffeVenditoreUtil.DammiHashTablePercentualiProvinceVenditore(venditorePercServProvinciaList);
    	int PercentualeVenditoreProvDefault = DammiParametriPercentualiVenditori().get(Constants.VENDIDORE_PERC_DEFAULT);
    	// Province -- > Province 
    	for(Province ite: territorioAndataArrivo.getListProvince()) {
    		List<Province> listProvConfinanti = new ArrayList<Province>(ite.getProvinceConfinanti());
    		Collections.sort(listProvConfinanti, (o1, o2) -> o2.getNumeroAbitanti().compareTo(o1.getNumeroAbitanti()));
    		for(Province provConfinante : listProvConfinanti) {
	    		Tariffa_Province_e_Infrastrutture tariffa_Province_e_Infrastrutture = new Tariffa_Province_e_Infrastrutture();
	    		tariffa_Province_e_Infrastrutture.setTitoloCorsaPartenza(ite.getNomeProvincia());
	    		tariffa_Province_e_Infrastrutture.setTitoloCorsaArrivo(provConfinante.getNomeProvincia());
	    		tariffa_Province_e_Infrastrutture.setTipoTerritorio(PROVINCIA);
	    		Long distanzaMetri = 0l;
	    		if(provConfinante.getMetriDistanza() == null) {
	    			GMaps_Api GMaps_Api = new GMaps_Api();
	    			distanzaMetri = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(ite.getLat(), ite.getLng(), provConfinante.getLat(), provConfinante.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
	    			SalvaDistanzeProvinceInfrastrutture_Intelligente(ite, provConfinante.getId(), null, null, null, distanzaMetri);
	    		}else {
	    			distanzaMetri = provConfinante.getMetriDistanza();
	    		}
	    		tariffa_Province_e_Infrastrutture.setNumeroKilometro((int)(long)distanzaMetri / 1000);
	    		List<TariffaClasseAuto> tariffaClasseAutoProvList = new ArrayList<TariffaClasseAuto>();
	    		for(ClasseAutoveicolo classeAutoIte: classeAutoveicoloList) {
	    			TariffaClasseAuto tariffaClasseAuto = new TariffaClasseAuto(); 
	    			tariffaClasseAuto.setTitoloClasseAuto( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome(), locale) );
	    			int PercentualeVenditore = (idUserVenditore != null) ? 
	    					TariffeVenditoreUtil.DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(
	    					hmapPercentualiProvinceVenditore, PercentualeVenditoreProvDefault, ite.getId(), provConfinante.getId()) : 0;
	    			BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetri, Parametri, classeAutoIte, 
	    					CalcoloPrezzi.DammiTariffaPiuAlta(ite.getTariffaBase(), provConfinante.getTariffaBase()), ite.getPercentualeServizio(), PercentualeVenditore);
	    			tariffaClasseAuto.setPrezzoClinte(Totale);
	    			tariffaClasseAuto.setNumeroPasseggeri( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".num.pass", locale) );
	    			tariffaClasseAuto.setClasseAutoveicoloDesc( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".desc", locale) );
	    			tariffaClasseAutoProvList.add(tariffaClasseAuto);
	    		}
	    		tariffa_Province_e_Infrastrutture.setTariffaClasseAuto(tariffaClasseAutoProvList);
	    		tariffa_Province_e_InfrastruttureList.add(tariffa_Province_e_Infrastrutture);
    		}
			// AEROPORTI
			for(Aeroporti aeroporto: territorioAndataArrivo.getListAeroporti()) {
				if( ite.getId().longValue() == aeroporto.getDistanzaidProvinciaAndata().longValue() ) {
	    			Tariffa_Province_e_Infrastrutture tariffa_Province_e_Infrastrutture = new Tariffa_Province_e_Infrastrutture();
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaPartenza(ite.getNomeProvincia());
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaArrivo(aeroporto.getNomeAeroporto());
		    		tariffa_Province_e_Infrastrutture.setTipoTerritorio(AEROPORTO);
		    		Long distanzaMetriAero = 0l;
		    		if(aeroporto.getMetriDistanza() == null){
		    			GMaps_Api GMaps_Api = new GMaps_Api();
		    			distanzaMetriAero = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(ite.getLat(), ite.getLng(), aeroporto.getLat(), aeroporto.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
		    			SalvaDistanzeProvinceInfrastrutture_Intelligente(ite, null, aeroporto.getId(), null, null, distanzaMetriAero);
		    		}else {
		    			distanzaMetriAero = aeroporto.getMetriDistanza();
		    		}
		    		tariffa_Province_e_Infrastrutture.setNumeroKilometro((int)(long)distanzaMetriAero / 1000);
		    		List<TariffaClasseAuto> tariffaClasseAutoAeroList = new ArrayList<TariffaClasseAuto>();
		    		for(ClasseAutoveicolo classeAutoIte: classeAutoveicoloList) {
		    			TariffaClasseAuto tariffaClasseAuto = new TariffaClasseAuto();
		    			tariffaClasseAuto.setTitoloClasseAuto( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome(), locale) );
						int PercentualeVenditore = (idUserVenditore != null) ? 
		    					TariffeVenditoreUtil.DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(
		    					hmapPercentualiProvinceVenditore, PercentualeVenditoreProvDefault, ite.getId(), aeroporto.getComuni().getProvince().getId()) : 0;
		    			BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetriAero, Parametri, classeAutoIte, 
		    					CalcoloPrezzi.DammiTariffaPiuAlta(ite.getTariffaBase(), aeroporto.getTariffaBase()), ite.getPercentualeServizio(), PercentualeVenditore);
		    			tariffaClasseAuto.setPrezzoClinte(Totale);
		    			tariffaClasseAuto.setNumeroPasseggeri( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".num.pass", locale) );
		    			tariffaClasseAuto.setClasseAutoveicoloDesc( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".desc", locale) );
		    			tariffaClasseAutoAeroList.add(tariffaClasseAuto);
		    		}
		    		tariffa_Province_e_Infrastrutture.setTariffaClasseAuto(tariffaClasseAutoAeroList);
		    		tariffa_Province_e_InfrastruttureList.add(tariffa_Province_e_Infrastrutture);
				}
			}
			// PORTI
			for(PortiNavali portoNavale: territorioAndataArrivo.getListPortiNavali()) {
				if( ite.getId().longValue() == portoNavale.getDistanzaidProvinciaAndata().longValue() ) {
	    			Tariffa_Province_e_Infrastrutture tariffa_Province_e_Infrastrutture = new Tariffa_Province_e_Infrastrutture();
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaPartenza(ite.getNomeProvincia());
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaArrivo(portoNavale.getNomePorto());
		    		tariffa_Province_e_Infrastrutture.setTipoTerritorio(PORTO_NAV);
		    		Long distanzaMetriPorto = 0l;
		    		if(portoNavale.getMetriDistanza() == null) {
		    			GMaps_Api GMaps_Api = new GMaps_Api();
		    			distanzaMetriPorto = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(ite.getLat(), ite.getLng(), portoNavale.getLat(), portoNavale.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
		    			SalvaDistanzeProvinceInfrastrutture_Intelligente(ite, null, null, portoNavale.getId(), null, distanzaMetriPorto);
		    		}else{
		    			distanzaMetriPorto = portoNavale.getMetriDistanza();
		    		}
		    		tariffa_Province_e_Infrastrutture.setNumeroKilometro((int)(long)distanzaMetriPorto / 1000);
		    		List<TariffaClasseAuto> tariffaClasseAutoPortoNavaleList = new ArrayList<TariffaClasseAuto>();
		    		for(ClasseAutoveicolo classeAutoIte: classeAutoveicoloList){
		    			TariffaClasseAuto tariffaClasseAuto = new TariffaClasseAuto();
		    			tariffaClasseAuto.setTitoloClasseAuto( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome(), locale) );
						int PercentualeVenditore = (idUserVenditore != null) ? 
		    					TariffeVenditoreUtil.DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(
		    					hmapPercentualiProvinceVenditore, PercentualeVenditoreProvDefault, ite.getId(), portoNavale.getComuni().getProvince().getId()) : 0;
		    			BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetriPorto, Parametri, classeAutoIte, 
		    					CalcoloPrezzi.DammiTariffaPiuAlta(ite.getTariffaBase(), portoNavale.getTariffaBase()), 
		    					ite.getPercentualeServizio(), PercentualeVenditore);
		    			tariffaClasseAuto.setPrezzoClinte(Totale);
		    			tariffaClasseAuto.setNumeroPasseggeri( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".num.pass", locale) );
		    			tariffaClasseAuto.setClasseAutoveicoloDesc( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".desc", locale) );
		    			tariffaClasseAutoPortoNavaleList.add(tariffaClasseAuto);
		    		}
		    		tariffa_Province_e_Infrastrutture.setTariffaClasseAuto(tariffaClasseAutoPortoNavaleList);
		    		tariffa_Province_e_InfrastruttureList.add(tariffa_Province_e_Infrastrutture);
				}
			}
			// MUSEI
			for(Musei museo: territorioAndataArrivo.getListMusei()) {
				if( ite.getId().longValue() == museo.getDistanzaidProvinciaAndata().longValue() ) {
	    			Tariffa_Province_e_Infrastrutture tariffa_Province_e_Infrastrutture = new Tariffa_Province_e_Infrastrutture();
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaPartenza(ite.getNomeProvincia());
		    		tariffa_Province_e_Infrastrutture.setTitoloCorsaArrivo(museo.getNomeMuseo());
		    		tariffa_Province_e_Infrastrutture.setTipoTerritorio(MUSEO);
		    		Long distanzaMetriPorto = 0l;
		    		if(museo.getMetriDistanza() == null) {
		    			GMaps_Api GMaps_Api = new GMaps_Api();
		    			distanzaMetriPorto = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(ite.getLat(), ite.getLng(), museo.getLat(), museo.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
		    			SalvaDistanzeProvinceInfrastrutture_Intelligente(ite, null, null, null, museo.getId(), distanzaMetriPorto);
		    		}else{
		    			distanzaMetriPorto = museo.getMetriDistanza();
		    		}
		    		tariffa_Province_e_Infrastrutture.setNumeroKilometro((int)(long)distanzaMetriPorto / 1000);
		    		List<TariffaClasseAuto> tariffaClasseAutoMuseoList = new ArrayList<TariffaClasseAuto>();
		    		for(ClasseAutoveicolo classeAutoIte: classeAutoveicoloList) {
		    			TariffaClasseAuto tariffaClasseAuto = new TariffaClasseAuto();
		    			tariffaClasseAuto.setTitoloClasseAuto( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome(), locale) );
						int PercentualeVenditore = (idUserVenditore != null) ? 
		    					TariffeVenditoreUtil.DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(
		    					hmapPercentualiProvinceVenditore, PercentualeVenditoreProvDefault, ite.getId(), museo.getComuni().getProvince().getId()) : 0;
		    			BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetriPorto, Parametri, classeAutoIte, 
		    					CalcoloPrezzi.DammiTariffaPiuAlta(ite.getTariffaBase(), museo.getTariffaBase()), 
		    					ite.getPercentualeServizio(), PercentualeVenditore);
		    			tariffaClasseAuto.setPrezzoClinte(Totale);
		    			tariffaClasseAuto.setNumeroPasseggeri( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".num.pass", locale) );
		    			tariffaClasseAuto.setClasseAutoveicoloDesc( ApplicationMessagesUtil.DammiMessageSource(classeAutoIte.getNome()+".desc", locale) );
		    			tariffaClasseAutoMuseoList.add(tariffaClasseAuto);
		    		}
		    		tariffa_Province_e_Infrastrutture.setTariffaClasseAuto(tariffaClasseAutoMuseoList);
		    		tariffa_Province_e_InfrastruttureList.add(tariffa_Province_e_Infrastrutture);
				}
			}
    	}
    	transferTariffeProvince.setTariffa_Province_e_Infrastrutture(tariffa_Province_e_InfrastruttureList);
    	return transferTariffeProvince;
    }
	
    

	public static DistanzeProvinceInfrastrutture SalvaDistanzeProvinceInfrastrutture_Intelligente(Object AndataObj, 
			Long ProvArr, Long AeroArr, Long PortoArr, Long MuseoArr, long metriDistanza) {
		// Aggorno Esistente
		if(Province.class.isInstance(AndataObj)) {
			Province prov = (Province)AndataObj;
			DistanzeProvinceInfrastrutture distanzeProvInfra = distanzeProvInfraDao.getProvinciaAndata_InfraArrivo(prov.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, null);
			if(distanzeProvInfra != null){
				distanzeProvInfra.setMetriDistanza(metriDistanza);
				distanzeProvInfra.setDataRequestDistance(new Date());
				return distanzeProvInfraDao.saveDistanzeProvinceInfrastrutture(distanzeProvInfra);
			}
		}else if(Aeroporti.class.isInstance(AndataObj)) {
			Aeroporti aero = (Aeroporti)AndataObj;
			DistanzeProvinceInfrastrutture distanzeProvInfra = distanzeProvInfraDao.getAeroportoAndata_InfraArrivo(aero.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, null);
			if(distanzeProvInfra != null){
				distanzeProvInfra.setMetriDistanza(metriDistanza);
				distanzeProvInfra.setDataRequestDistance(new Date());
				return distanzeProvInfraDao.saveDistanzeProvinceInfrastrutture(distanzeProvInfra);
			}
		}else if(PortiNavali.class.isInstance(AndataObj)) {
			PortiNavali porto = (PortiNavali)AndataObj;
			DistanzeProvinceInfrastrutture distanzeProvInfra = distanzeProvInfraDao.getPortoAndata_InfraArrivo(porto.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, null);
			if(distanzeProvInfra != null){
				distanzeProvInfra.setMetriDistanza(metriDistanza);
				distanzeProvInfra.setDataRequestDistance(new Date());
				return distanzeProvInfraDao.saveDistanzeProvinceInfrastrutture(distanzeProvInfra);
			}
		}else if(Musei.class.isInstance(AndataObj)) {
			Musei museo = (Musei)AndataObj;
			DistanzeProvinceInfrastrutture distanzeProvInfra = distanzeProvInfraDao.getMuseoAndata_InfraArrivo(museo.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, null);
			if(distanzeProvInfra != null){
				distanzeProvInfra.setMetriDistanza(metriDistanza);
				distanzeProvInfra.setDataRequestDistance(new Date());
				return distanzeProvInfraDao.saveDistanzeProvinceInfrastrutture(distanzeProvInfra);
			}
		}
		// Salvo Nuovo
		DistanzeProvinceInfrastrutture distanzeProvInfraNew = new DistanzeProvinceInfrastrutture();
		if(Province.class.isInstance(AndataObj)) {
			distanzeProvInfraNew.setProvinciaAndata((Province)AndataObj);
		}else if(Aeroporti.class.isInstance(AndataObj)) {
			distanzeProvInfraNew.setAeroportoAndata((Aeroporti)AndataObj);
		}else if(PortiNavali.class.isInstance(AndataObj)) {
			distanzeProvInfraNew.setPortoAndata((PortiNavali)AndataObj);
		}else if(Musei.class.isInstance(AndataObj)) {
			distanzeProvInfraNew.setMuseoAndata((Musei)AndataObj);
		}
		if(ProvArr != null){
			distanzeProvInfraNew.setProvinciaArrivo( provinceDao.get(ProvArr) );
		}else if(AeroArr != null){
			distanzeProvInfraNew.setAeroportoArrivo( aeroportiDao.get(AeroArr) );
		}else if(PortoArr != null){
			distanzeProvInfraNew.setPortoArrivo( portiNavaliDao.get(PortoArr) );
		}else if(MuseoArr != null){
			distanzeProvInfraNew.setMuseoArrivo( museiDao.get(MuseoArr) );
		}
		distanzeProvInfraNew.setMetriDistanza(metriDistanza);
		distanzeProvInfraNew.setDataRequestDistance(new Date());
		return distanzeProvInfraDao.saveDistanzeProvinceInfrastrutture(distanzeProvInfraNew);
	}
    
}

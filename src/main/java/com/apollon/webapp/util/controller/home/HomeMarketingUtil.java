package com.apollon.webapp.util.controller.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.dao.AeroportiDao;
import com.apollon.dao.DistanzeProvinceInfrastruttureDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.DistanzeProvinceInfrastrutture;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.bean.TransferTariffe;
import com.apollon.webapp.util.bean.TransferTariffe.DestAeroportiConfinanti;
import com.apollon.webapp.util.bean.TransferTariffe.DestPorivinceConfinanti;
import com.apollon.webapp.util.controller.tariffe.TariffeTransferProvincieUtil;
import com.apollon.webapp.util.geogoogle.GMaps_Api;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class HomeMarketingUtil extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(HomeMarketingUtil.class);
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static AeroportiDao aeroportiDao = (AeroportiDao) contextDao.getBean("AeroportiDao");
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	private static DistanzeProvinceInfrastruttureDao distanzeProvInfraDao = (DistanzeProvinceInfrastruttureDao) contextDao.getBean("DistanzeProvinceInfrastruttureDao");
	
	/**
	 * Mi ritornano informazione sulla infrasttuttra, richimaare questo processo da AJAX perche è troppo pesante farlo Sincrono
	 * TODO POSSIBILE SOLUZIONE FUTURA: DEVO RIFARE QUESTO PROCESSO IN AJAX COSI LO FA ASINCRONO E CARICA PIU VELOCE LA PAGINA
	 */
	public static RicercaTransfert CaricaInfoMarketingInfrastruttura(String url, String typeDeviceUser) throws Exception{
		log.debug("CaricaInfoMarketingInfrastruttura");
		Object obj = ricercaTransfertDao.getAeroporto_Musei_Porti_Province_LIKE_Url( url );
		RicercaTransfert ricercaTransfert = new RicercaTransfert();
		ClasseAutoveicolo classeAutoveicolo = new ClasseAutoveicolo(); classeAutoveicolo.setId( Constants.AUTO_ECONOMY );
		if( obj instanceof Province ){
			Province prov = (Province) obj;
			String titolo = prov.getNomeProvincia();
			String Url = Constants.URL_TRANSFER + prov.getUrl();
			ricercaTransfert.setPartenzaRequest( prov.getNomeProvincia() +", "+prov.getSiglaProvincia() );
			List<Province> provinceVicineList = new ArrayList<Province>(prov.getProvinceConfinanti());
			List<Long> ListProvinceAeroportiViciniLong = new ArrayList<Long>();
			ListProvinceAeroportiViciniLong.add(prov.getId());
			for(Province ite: prov.getProvinceConfinanti()){
				ListProvinceAeroportiViciniLong.add(ite.getId());
			}
			List<Aeroporti> aeroportiViciniList = aeroportiDao.getAeroportiBy_ListProvince(ListProvinceAeroportiViciniLong, null); 
			ricercaTransfert = CaricaInfoInfrastruttura(ricercaTransfert, prov, 
					provinceVicineList, aeroportiViciniList, titolo, "earth-globe.png", Url, classeAutoveicolo, typeDeviceUser );
		}else if( obj instanceof Aeroporti ){
			Aeroporti aero = (Aeroporti) obj;
			String titolo = aero.getNomeAeroporto() +" ("+aero.getSiglaAeroporto()+")";
			String Url = Constants.URL_TRANSFER + aero.getUrl();
			ricercaTransfert.setPartenzaRequest( aero.getNomeAeroporto() );
			List<Province> provinceVicineList = new ArrayList<Province>();
			provinceVicineList.add(aero.getComuni().getProvince());
			provinceVicineList.addAll(aero.getComuni().getProvince().getProvinceConfinanti());
			List<Long> ListProvinceAeroportiViciniLong = new ArrayList<Long>();
			ListProvinceAeroportiViciniLong.add(aero.getComuni().getProvince().getId());
			for(Province ite: aero.getComuni().getProvince().getProvinceConfinanti()){
				ListProvinceAeroportiViciniLong.add(ite.getId());
			}
			List<Aeroporti> aeroportiViciniList = aeroportiDao.getAeroportiBy_ListProvince( ListProvinceAeroportiViciniLong, aero.getId() ); 
			ricercaTransfert = CaricaInfoInfrastruttura(ricercaTransfert, aero, 
					provinceVicineList, aeroportiViciniList, titolo, "airplane.png", Url, classeAutoveicolo, typeDeviceUser);
		}else if( obj instanceof Musei ){
			Musei museo = (Musei) obj;
			String titolo = museo.getNomeMuseo() + ((museo.getDescrizione() != null && !museo.getDescrizione().equals("")) ? " ("+museo.getDescrizione()+")" : "");
			String Url = Constants.URL_TRANSFER + museo.getUrl();
			ricercaTransfert.setPartenzaRequest( museo.getNomeMuseo() );
			List<Province> provinceVicineList = new ArrayList<Province>();
			provinceVicineList.add(museo.getComuni().getProvince());
			provinceVicineList.addAll(museo.getComuni().getProvince().getProvinceConfinanti());
			List<Long> ListProvinceAeroportiViciniLong = new ArrayList<Long>();
			ListProvinceAeroportiViciniLong.add(museo.getComuni().getProvince().getId());
			for(Province ite: museo.getComuni().getProvince().getProvinceConfinanti()){
				ListProvinceAeroportiViciniLong.add(ite.getId());
			}
			List<Aeroporti> aeroportiViciniList = aeroportiDao.getAeroportiBy_ListProvince( ListProvinceAeroportiViciniLong, null ); 
			ricercaTransfert = CaricaInfoInfrastruttura(ricercaTransfert, museo, 
					provinceVicineList, aeroportiViciniList, titolo, "bank.png", Url, classeAutoveicolo, typeDeviceUser );
		}else if( obj instanceof PortiNavali ){
			PortiNavali porto = (PortiNavali) obj;
			String Url = Constants.URL_TRANSFER + porto.getUrl();
			ricercaTransfert.setPartenzaRequest( porto.getNomePorto() );
			List<Province> provinceVicineList = new ArrayList<Province>();
			provinceVicineList.add(porto.getComuni().getProvince());
			provinceVicineList.addAll(porto.getComuni().getProvince().getProvinceConfinanti());
			List<Long> ListProvinceAeroportiViciniLong = new ArrayList<Long>();
			ListProvinceAeroportiViciniLong.add(porto.getComuni().getProvince().getId());
			for(Province ite: porto.getComuni().getProvince().getProvinceConfinanti()){
				ListProvinceAeroportiViciniLong.add(ite.getId());
			}
			List<Aeroporti> aeroportiViciniList = aeroportiDao.getAeroportiBy_ListProvince( ListProvinceAeroportiViciniLong, null ); 
			ricercaTransfert = CaricaInfoInfrastruttura(ricercaTransfert, porto, 
					provinceVicineList, aeroportiViciniList, porto.getNomePorto(), "cruise.png", Url, classeAutoveicolo, typeDeviceUser );
		}else{
			ricercaTransfert.setRicercaRiuscita(false);
			ricercaTransfert.setTipoServizio(Constants.SERVIZIO_STANDARD);
		}
		return ricercaTransfert;
	}
	
	
	private static RicercaTransfert CaricaInfoInfrastruttura(RicercaTransfert ricercaTransfert, Object InfraAndata,
    		List<Province> provinceVicineList, List<Aeroporti> aeroportiViciniList, String titolo, String icona, String Url, 
    		ClasseAutoveicolo ClasseAutoveicoloReale, String typeDeviceUser) throws Exception{
    	TransferTariffe prezzMarkTariffCliente = new TransferTariffe();
    	prezzMarkTariffCliente.setTitolo(titolo);
    	prezzMarkTariffCliente.setIcona(icona);
    	prezzMarkTariffCliente.setUrl(Url);
    	String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
	    // FACCIO VEDERE SOLO UNO/DUE/TRE PROVINCE/AEROPORTI in relazione al device usato dall'user PERCHé le operazione che fa sono troppo lente E LA PAGINA CARICA LENTA. 
	    int ContaGiriProv = 0; int ContaGiriAero = 0;
	    if(typeDeviceUser == null || typeDeviceUser.equals(Constants.DEVICE_USER_DESKTOP)){
	    	ContaGiriProv = 2; ContaGiriAero = 3;
	    }else{
	    	ContaGiriProv = 1; ContaGiriAero = 2;
	    }
	    BigDecimal TariffaBase = null; double latPart = 0; double lngPart = 0;
		if(Province.class.isInstance(InfraAndata)){
			Province prov = (Province)InfraAndata;
			TariffaBase = prov.getTariffaBase(); latPart = prov.getLat(); lngPart = prov.getLng();
		}else if(Aeroporti.class.isInstance(InfraAndata)){
			Aeroporti aero = (Aeroporti)InfraAndata;
			TariffaBase = aero.getComuni().getProvince().getTariffaBase(); latPart = aero.getLat(); lngPart = aero.getLng();
		}else if(PortiNavali.class.isInstance(InfraAndata)){
			PortiNavali porto = (PortiNavali)InfraAndata;
			TariffaBase = porto.getComuni().getProvince().getTariffaBase(); latPart = porto.getLat(); lngPart = porto.getLng();
		}else if(Musei.class.isInstance(InfraAndata)){
			Musei museo = (Musei)InfraAndata;
			TariffaBase = museo.getComuni().getProvince().getTariffaBase(); latPart = museo.getLat(); lngPart = museo.getLng();
		}
	    
	    List<DestPorivinceConfinanti> provConfList = new ArrayList<TransferTariffe.DestPorivinceConfinanti>();
		Iterator<Province> prov_ite = provinceVicineList.iterator();
		for(int i = 0; prov_ite.hasNext() && i < ContaGiriProv; i++) {
			Province ite = prov_ite.next();
    		DestPorivinceConfinanti provConf = new DestPorivinceConfinanti();
    		provConf.setProvincia(ite);
    		Long distanzaMetri = DammiDistanzaItelligente(InfraAndata, ite);
    		if(distanzaMetri == null){
    			GMaps_Api GMaps_Api = new GMaps_Api();
    			distanzaMetri = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(latPart, lngPart, ite.getLat(), ite.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
    			TariffeTransferProvincieUtil.SalvaDistanzeProvinceInfrastrutture_Intelligente(InfraAndata, ite.getId(), null, null, null, distanzaMetri);
    		}
    		BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetri, Parametri, ClasseAutoveicoloReale, 
    				CalcoloPrezzi.DammiTariffaPiuAlta(TariffaBase, ite.getTariffaBase()), ite.getPercentualeServizio(), 0);
    		provConf.setPrezzoCorsa(Totale);
			provConf.setClasseAutoveicolo(ClasseAutoveicoloReale);
			provConfList.add(provConf);
    	}
		List<DestAeroportiConfinanti> aeroConfList = new ArrayList<TransferTariffe.DestAeroportiConfinanti>();
    	Iterator<Aeroporti> aero_ite = aeroportiViciniList.iterator();
		for(int i = 0; aero_ite.hasNext() && i < ContaGiriAero; i++) {
			Aeroporti ite = aero_ite.next();
			DestAeroportiConfinanti aeroConf = new DestAeroportiConfinanti();
			aeroConf.setAeroporto(ite);
			Long distanzaMetri = DammiDistanzaItelligente(InfraAndata, ite);
    		if(distanzaMetri == null){
    			GMaps_Api GMaps_Api = new GMaps_Api();
    			distanzaMetri = GMaps_Api.GoogleMaps_DistanceMatrixDistanza(latPart, lngPart, ite.getLat(), ite.getLng(), DateUtil.DammiDopoDomaniDaOggi_ore_12().getTime());
    			TariffeTransferProvincieUtil.SalvaDistanzeProvinceInfrastrutture_Intelligente(InfraAndata, null, ite.getId(), null, null, distanzaMetri);
    		}
			BigDecimal Totale = CalcoloPrezzi.DammiTotalePrezzoCliente(distanzaMetri, Parametri, ClasseAutoveicoloReale, 
					CalcoloPrezzi.DammiTariffaPiuAlta(TariffaBase, ite.getComuni().getProvince().getTariffaBase()), 
					ite.getComuni().getProvince().getPercentualeServizio(), 0);
			aeroConf.setPrezzoCorsa(Totale);
			aeroConf.setClasseAutoveicolo(ClasseAutoveicoloReale);
			aeroConfList.add(aeroConf);
    	}
    	prezzMarkTariffCliente.setDestPorivinceConfinanti(provConfList);
    	prezzMarkTariffCliente.setDestAeroportiConfinanti(aeroConfList);
    	ricercaTransfert.setTransferTariffe(prezzMarkTariffCliente);
    	return ricercaTransfert;
    }

	
	private static Long DammiDistanzaItelligente(Object InfraAndata, Object InfraArrivo ){
		Long ProvArr = null; Long AeroArr = null; Long PortoArr = null; Long MuseoArr = null;
		if(Province.class.isInstance(InfraArrivo)){
			Province prov = (Province)InfraArrivo;
			ProvArr = prov.getId();
		}else if(Aeroporti.class.isInstance(InfraArrivo)){
			Aeroporti aero = (Aeroporti)InfraArrivo;
			AeroArr = aero.getId();
		}else if(PortiNavali.class.isInstance(InfraArrivo)){
			PortiNavali porto = (PortiNavali)InfraArrivo;
			PortoArr = porto.getId();
		}else if(Musei.class.isInstance(InfraArrivo)){
			Musei museo = (Musei)InfraArrivo;
			MuseoArr = museo.getId();
		}

		DistanzeProvinceInfrastrutture distanzeProvInfra = null;
		if(Province.class.isInstance(InfraAndata)){
			Province prov = (Province)InfraAndata;
			distanzeProvInfra = distanzeProvInfraDao.getProvinciaAndata_InfraArrivo(prov.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
		}else if(Aeroporti.class.isInstance(InfraAndata)){
			Aeroporti aero = (Aeroporti)InfraAndata;
			distanzeProvInfra = distanzeProvInfraDao.getAeroportoAndata_InfraArrivo(aero.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
		}else if(PortiNavali.class.isInstance(InfraAndata)){
			PortiNavali porto = (PortiNavali)InfraAndata;
			distanzeProvInfra = distanzeProvInfraDao.getPortoAndata_InfraArrivo(porto.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
		}else if(Musei.class.isInstance(InfraAndata)){
			Musei museo = (Musei)InfraAndata;
			distanzeProvInfra = distanzeProvInfraDao.getMuseoAndata_InfraArrivo(museo.getId(), ProvArr, AeroArr, PortoArr, MuseoArr, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
		}
		
		if( distanzeProvInfra != null ){
			return distanzeProvInfra.getMetriDistanza();
		}else{
			return null;
		}
		
	}
	
}

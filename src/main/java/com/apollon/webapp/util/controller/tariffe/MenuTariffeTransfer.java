package com.apollon.webapp.util.controller.tariffe;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.apollon.Constants;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RegioniDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.bean.TransferTariffeProvince;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement;
import com.google.maps.errors.ApiException;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class MenuTariffeTransfer extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(MenuTariffeTransfer.class);
	
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	public static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	
	private static final int Italia_MaxProvVisualizzate = 1;
	private static final int MacroReg_MaxProvVisualizzate = 1;
	private static final int Regioni_MaxProvVisualizzate = 2;
	
	protected static final String PROVINCIA = "PROVINCIA"; protected static final String AEROPORTO = "AEROPORTO"; protected static final String PORTO_NAV = "PORTO_NAV";
	protected static final String MUSEO = "MUSEO";
	
	
	public static TerritorioAndataArrivo TrasfrormaInOggetto(List<List<Object[]>> listOfLists) {
		TerritorioAndataArrivo territorioAndataArrivo = new TerritorioAndataArrivo();
		List<Province> province_List = new ArrayList<Province>();
		List<Long> porvLong = new ArrayList<Long>();
		Province prov = new Province(); Long provCorrente = null;
		for(Object[] ite_object: listOfLists.get(0)) {
			// provincia
			long 		var_0 = ((BigInteger)ite_object[0]).longValue();
			String 		var_1 = (String) ite_object[1];
			double 		var_2 = (Double)ite_object[2];
			double 		var_3 = (Double)ite_object[3];
			BigDecimal 	var_4 = (BigDecimal)ite_object[4];
			int 		var_5 = (Integer)ite_object[5];
			// province confinanti
			long 		var_6 = ((BigInteger)ite_object[6]).longValue();
			String 		var_7 = (String) ite_object[7];
			Double 		var_8 = ite_object != null ? (Double)ite_object[8] : null;
			Double 		var_9 = ite_object != null ? (Double)ite_object[9] : null;
			BigDecimal 	var_10 = (BigDecimal)ite_object[10];
			BigInteger	var_11 = (BigInteger)ite_object[11];
			// distanze
			Long 		var_12 = ite_object[12] != null ? var_12 = ((BigInteger)ite_object[12]).longValue() : null;
			//System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9+" | "+var_10+" | "+var_11+" | "+var_12); 
			if( provCorrente != null && provCorrente != new Long(var_0).longValue() ) {
				province_List.add(prov);
			}
			provCorrente = var_0;
			if( !porvLong.contains(new Long(var_0)) ) {
				prov = new Province();
				prov.setId( var_0 ); prov.setNomeProvincia(var_1); prov.setLat(var_2); prov.setLng(var_3); prov.setTariffaBase(var_4); prov.setPercentualeServizio(var_5);
			}
			Province prov_confinanti = new Province();
			prov_confinanti.setId( var_6 ); prov_confinanti.setNomeProvincia(var_7); prov_confinanti.setLat(var_8); prov_confinanti.setLng(var_9); 
			prov_confinanti.setTariffaBase(var_10); prov_confinanti.setMetriDistanza(var_12);
			prov_confinanti.setNumeroAbitanti(var_11.intValue());
			prov.addProvinciaConfinante( prov_confinanti );
			porvLong.add( var_0 );
		}
		province_List.add(prov);
		//============ AEROPORTI =======================
		List<Aeroporti> aeroporti_List = new ArrayList<Aeroporti>();
		for(Object[] ite_object: listOfLists.get(1)) {
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			//System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6); 
			Aeroporti aero = new Aeroporti();
			aero.setDistanzaidProvinciaAndata(var_0);
			aero.setId(var_1);
			aero.setNomeAeroporto(var_2);
			aero.setLat(var_3);
			aero.setLng(var_4);
			aero.setTariffaBase(var_5);
			aero.setMetriDistanza(var_6);
			aeroporti_List.add(aero);
		}
		//============ PORTI NAVALI =======================
		List<PortiNavali> porti_List = new ArrayList<PortiNavali>();
		for(Object[] ite_object: listOfLists.get(2)) {
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			//System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6);
			PortiNavali porto = new PortiNavali();
			porto.setDistanzaidProvinciaAndata(var_0);
			porto.setId(var_1);
			porto.setNomePorto(var_2);
			porto.setLat(var_3);
			porto.setLng(var_4);
			porto.setTariffaBase(var_5);
			porto.setMetriDistanza(var_6);
			porti_List.add(porto);
		}
		//============ MUSEI =======================
		List<Musei> musei_List = new ArrayList<Musei>();
		for(Object[] ite_object: listOfLists.get(3)) {
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			//System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6);
			Musei museo = new Musei();
			museo.setDistanzaidProvinciaAndata(var_0);
			museo.setId(var_1);
			museo.setNomeMuseo(var_2);
			museo.setLat(var_3);
			museo.setLng(var_4);
			museo.setTariffaBase(var_5);
			museo.setMetriDistanza(var_6);
			musei_List.add(museo);
		}
		
		territorioAndataArrivo.setListProvince(province_List);
		territorioAndataArrivo.setListAeroporti(aeroporti_List);
		territorioAndataArrivo.setListPortiNavali(porti_List);
		territorioAndataArrivo.setListMusei(musei_List);
		return territorioAndataArrivo;
	}

	
    public static MenuTerrTariffeTransfer CaricaMenuTerrTariffeTransfer(Locale locale, String url, String typeDeviceUser, Long idUserVenditore)  {
    	String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
    	MenuTerrTariffeTransfer menuTerrTariffeTransfer;
		try {
			menuTerrTariffeTransfer = DammiMenuItems_MacroRegioni_Regioni(locale, url, regioniDao.MenuTariffe_Province());
	    	if( url == null ) {
	    		menuTerrTariffeTransfer.setTransferTariffeProvince(
						TariffeTransferProvincieUtil.DammiTransferTariffeProvince( 							
								TrasfrormaInOggetto(regioniDao.Menu_Lista_ProvinceItalianeOrderByAbitanti_MaxResult(Italia_MaxProvVisualizzate, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE)), 							
								locale, Parametri, idUserVenditore));
	    		menuTerrTariffeTransfer.setUrlCanonical( Constants.PAGE_TARIFFE_TRANSFER );
	    		menuTerrTariffeTransfer.setTitle( ApplicationMessagesUtil.DammiMessageSource("tariffe.transfer.title", locale) );
	    	}else {
	        	for(MenuTerrTariffeTransferElement ite_macroRegioni: menuTerrTariffeTransfer.getMacroRegioni() ) {
	        		if( ite_macroRegioni.getUrl().contains(url)) {
	    				menuTerrTariffeTransfer.setTransferTariffeProvince(
		        				TariffeTransferProvincieUtil.DammiTransferTariffeProvince( 
		        						TrasfrormaInOggetto(regioniDao.Menu_Lista_MacroRegioneOrderByAbitanti_MaxResult(ite_macroRegioni.getId(), MacroReg_MaxProvVisualizzate, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE)), 	        						
		        						locale, Parametri, idUserVenditore));
	    				menuTerrTariffeTransfer.setUrlCanonical( ite_macroRegioni.getUrl() );
	        		}
	        	}
	        	for(MenuTerrTariffeTransferElement ite_regioni: menuTerrTariffeTransfer.getRegioni() ) {
	        		if( ite_regioni.getUrl().contains(url)) {
	    				menuTerrTariffeTransfer.setTransferTariffeProvince(
	    						TariffeTransferProvincieUtil.DammiTransferTariffeProvince(
	    								TrasfrormaInOggetto(regioniDao.Menu_Lista_RegioneOrderByAbitanti_MaxResult(ite_regioni.getId(), Regioni_MaxProvVisualizzate, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE)),
	    								locale, Parametri, idUserVenditore));
	    				menuTerrTariffeTransfer.setUrlCanonical( ite_regioni.getUrl() );
	        		}
	        	}
	        	for(MenuTerrTariffeTransferElement ite_province: menuTerrTariffeTransfer.getProvince() ) {
	        		if( ite_province.getUrl().contains(url)) {
	        			menuTerrTariffeTransfer.setTransferTariffeProvince(
	        					TariffeTransferProvincieUtil.DammiTransferTariffeProvince(
	        							TrasfrormaInOggetto(regioniDao.Menu_Lista_ProvinciaOrderByAbitanti_MaxResult(ite_province.getId(), Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE)), 
	        							locale, Parametri, idUserVenditore));
	        			menuTerrTariffeTransfer.setUrlCanonical( ite_province.getUrl() );
	        		}
	        	}
	    	}
	        String title = ""; String Metadescription = "Tariffe Noleggio con conducente "; String split = ", "; String end = "...";
	        boolean Provincia = false;
	        for (final MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement ite : menuTerrTariffeTransfer.getProvince()) {
	            if (ite.isSelezionato()) {
	                title = ite.getTerritorio() + " | ";
	                Metadescription = Metadescription +"da "+ ite.getTerritorio() +" a ";
	                Provincia = true;
	                break;
	            }
	        }
	        for (final MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement ite : menuTerrTariffeTransfer.getRegioni()) {
	            if (ite.isSelezionato()) {
	                title = title + ite.getTerritorio();
	                if( Provincia == false ) {
	                	Metadescription = Metadescription + ite.getTerritorio() +" servizi da "
	                    		+ menuTerrTariffeTransfer.getTransferTariffeProvince().getTariffa_Province_e_Infrastrutture().get(0).getTitoloCorsaPartenza() + split;
	                }
	                break;
	            }
	        }
	        for (final MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement ite : menuTerrTariffeTransfer.getMacroRegioni()) {
	            if (title.equals("") && ite.isSelezionato()) {
	                menuTerrTariffeTransfer.setMetaDescription(ite.getTerritorio());
	                title = ite.getTerritorio();
	                if( Provincia == false ) {
	                	Metadescription = Metadescription + ite.getTerritorio() +" servizi da " 
	                    		+ menuTerrTariffeTransfer.getTransferTariffeProvince().getTariffa_Province_e_Infrastrutture().get(0).getTitoloCorsaPartenza() + split;
	                }
	                break;
	            }
	        }
	        menuTerrTariffeTransfer.setTitle(title);
	        final int MaxLengthMetaDescription = 160;
	        //final int MaxLengthMetaDescription = 9999;
	        List<String> listTipoTerr = new ArrayList<String>(Arrays.asList(AEROPORTO, PROVINCIA, AEROPORTO, PORTO_NAV, MUSEO));
	        int SizeTariffa_Province_e_Infrastrutture = menuTerrTariffeTransfer.getTransferTariffeProvince().getTariffa_Province_e_Infrastrutture().size();
	        List<Long> ListElement_ProvInfra = new ArrayList<Long>(); int lunghezzaDescription = 0;
	        while (lunghezzaDescription <= MaxLengthMetaDescription && ListElement_ProvInfra.size() < SizeTariffa_Province_e_Infrastrutture) {
	        	for (String ite_111 : listTipoTerr) {
	                for (int conta_222 = 0; conta_222 < SizeTariffa_Province_e_Infrastrutture; ++conta_222) {
	                	String elementTitoloCorsaArrivo = menuTerrTariffeTransfer.getTransferTariffeProvince().getTariffa_Province_e_Infrastrutture().get(conta_222).getTitoloCorsaArrivo();
	                    String elementTipoTerritorio = menuTerrTariffeTransfer.getTransferTariffeProvince().getTariffa_Province_e_Infrastrutture().get(conta_222).getTipoTerritorio();
	                    lunghezzaDescription = Metadescription.length() + elementTitoloCorsaArrivo.length() + end.length();
	                    //System.out.println("Metadescription: "+Metadescription + elementTitoloCorsaArrivo + split);
	                    if (ite_111.equals(elementTipoTerritorio) && !ListElement_ProvInfra.contains(new Long(conta_222)) && lunghezzaDescription <= MaxLengthMetaDescription) {
	                        Metadescription = Metadescription + elementTitoloCorsaArrivo + split;
	                        ListElement_ProvInfra.add(new Long(conta_222));
	                        //System.out.println("Metadescription: "+Metadescription);
	                        //System.out.println("Metadescription.length: "+Metadescription.length() );
	                        break;
	                    }
	                }
	            }
	        }
	        Metadescription = Metadescription.substring(0, Metadescription.length() - split.length());
	        Metadescription = Metadescription + end;
	        menuTerrTariffeTransfer.setMetaDescription(Metadescription);
			return menuTerrTariffeTransfer;
		
		} catch (NullPointerException nullExc) {
			log.info( nullExc.getCause() );
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
    }
    
    
    private static ElementTerritorio SetElementTerritorio(String url, List<Object[]> MacroRegioni_Regioni_Province_ObjectList) {
    	if( url == null ) {
    		return null;
    	}
		for(Object[] ite_object: MacroRegioni_Regioni_Province_ObjectList) {
    		if( url.equals((String)ite_object[8]) ) {
    			return new ElementTerritorio((String)ite_object[8], null, null);
    		}
    		if( url.equals((String)ite_object[5]) ) {
    			return new ElementTerritorio((String)ite_object[8], (String)ite_object[5], null);
    		}
    		if( url.equals((String)ite_object[2]) ) {
    			return new ElementTerritorio((String)ite_object[8], (String)ite_object[5], (String)ite_object[2]);
    		}
    	}
    	return null;
    }

    
    public static MenuTerrTariffeTransfer DammiMenuItems_MacroRegioni_Regioni(Locale locale, String url, List<Object[]> MacroRegioni_Regioni_Province_ObjectList) throws Exception {
    	MenuTerrTariffeTransfer menuTerrTariffeTransfer = new MenuTerrTariffeTransfer();
    	ElementTerritorio elementTerritorio = SetElementTerritorio(url, MacroRegioni_Regioni_Province_ObjectList);
    	List<MenuTerrTariffeTransferElement> MenuTerrTariffeTransferElement_MacroRegioniList = new ArrayList<MenuTerrTariffeTransferElement>();
    	MenuTerrTariffeTransferElement MenuTerrTariffeTransferElement_Italia = new MenuTerrTariffeTransferElement();
    	MenuTerrTariffeTransferElement_Italia.setId( new Long(0) );
    	MenuTerrTariffeTransferElement_Italia.setTerritorio(ApplicationMessagesUtil.DammiMessageSource("italia", locale));
    	MenuTerrTariffeTransferElement_Italia.setUrl( Constants.PAGE_TARIFFE_TRANSFER );
    	MenuTerrTariffeTransferElement_Italia.setSelezionato( url == null ? true : false);
    	MenuTerrTariffeTransferElement_MacroRegioniList.add(MenuTerrTariffeTransferElement_Italia);
		List<Long> longList = new ArrayList<Long>();
		for(Object[] ite_object: MacroRegioni_Regioni_Province_ObjectList) {
			long idMarcoRegione = ((BigInteger)ite_object[6]).longValue();
			if( !longList.contains(idMarcoRegione) ) {
				MenuTerrTariffeTransferElement menuTerrTariffeTransferElement = new MenuTerrTariffeTransferElement();
				menuTerrTariffeTransferElement.setId( ((BigInteger)ite_object[6]).longValue() );
				menuTerrTariffeTransferElement.setTerritorio(ApplicationMessagesUtil.DammiMessageSource( (String)ite_object[7], locale));
				menuTerrTariffeTransferElement.setUrl(Constants.URL_TARIFFE_TRANSFER+(String)ite_object[8]); 
				menuTerrTariffeTransferElement.setSelezionato( url != null && elementTerritorio != null && ((String)ite_object[8]).equals(elementTerritorio.getUrlMacroRegione()) ? true : false);
				MenuTerrTariffeTransferElement_MacroRegioniList.add(menuTerrTariffeTransferElement);
				longList.add( new Long(idMarcoRegione) );
			}
		}
		List<MenuTerrTariffeTransferElement> MenuTerrTariffeTransferElement_RegioniList = new ArrayList<MenuTerrTariffeTransferElement>();
		longList = new ArrayList<Long>();
		for(Object[] ite_object: MacroRegioni_Regioni_Province_ObjectList) {
			if( !longList.contains(((BigInteger)ite_object[3]).longValue()) && (url == null || elementTerritorio.getUrlMacroRegione().equals((String)ite_object[8])) ) {
				MenuTerrTariffeTransferElement menuTerrTariffeTransferElement = new MenuTerrTariffeTransferElement();
				menuTerrTariffeTransferElement.setId( ((BigInteger)ite_object[3]).longValue() );
				menuTerrTariffeTransferElement.setTerritorio( (String) ite_object[4] );
				menuTerrTariffeTransferElement.setUrl( Constants.URL_TARIFFE_TRANSFER+(String)ite_object[5] );
				menuTerrTariffeTransferElement.setSelezionato( url != null && elementTerritorio != null && ((String)ite_object[5]).equals(elementTerritorio.getUrlRegione()) ? true : false);
				MenuTerrTariffeTransferElement_RegioniList.add( menuTerrTariffeTransferElement );
				longList.add( new Long(((BigInteger)ite_object[3]).longValue()) );
			}
		}
		List<MenuTerrTariffeTransferElement> MenuTerrTariffeTransferElement_ProvinceList = new ArrayList<MenuTerrTariffeTransferElement>();
		for(Object[] ite_object: MacroRegioni_Regioni_Province_ObjectList) {
			if( elementTerritorio == null || elementTerritorio.getUrlRegione() == null && elementTerritorio.getUrlMacroRegione().equals((String)ite_object[8]) 
					|| (elementTerritorio.getUrlRegione() != null && elementTerritorio.getUrlRegione().equals((String)ite_object[5])) ) {
				MenuTerrTariffeTransferElement menuTerrTariffeTransferElement = new MenuTerrTariffeTransferElement();
				menuTerrTariffeTransferElement.setId( ((BigInteger)ite_object[0]).longValue() );
				menuTerrTariffeTransferElement.setTerritorio( (String) ite_object[1] );
				menuTerrTariffeTransferElement.setUrl( Constants.URL_TARIFFE_TRANSFER+(String)ite_object[2] );
				menuTerrTariffeTransferElement.setSelezionato( url != null && elementTerritorio != null && ((String)ite_object[2]).equals(elementTerritorio.getUrlProvincia()) ? true : false);
				MenuTerrTariffeTransferElement_ProvinceList.add( menuTerrTariffeTransferElement );
			}
		}
		if( MenuTerrTariffeTransferElement_ProvinceList.size() == 1 ) {
			MenuTerrTariffeTransferElement_ProvinceList.get(0).setSelezionato(true);
		}
		Collections.sort(MenuTerrTariffeTransferElement_MacroRegioniList, (o1, o2) -> o1.getId().compareTo(o2.getId()));
		menuTerrTariffeTransfer.setMacroRegioni( MenuTerrTariffeTransferElement_MacroRegioniList );
		Collections.sort(MenuTerrTariffeTransferElement_RegioniList, (o1, o2) -> o1.getTerritorio().compareTo(o2.getTerritorio()));
		menuTerrTariffeTransfer.setRegioni( MenuTerrTariffeTransferElement_RegioniList );
		Collections.sort(MenuTerrTariffeTransferElement_ProvinceList, (o1, o2) -> o1.getTerritorio().compareTo(o2.getTerritorio()));
		menuTerrTariffeTransfer.setProvince(MenuTerrTariffeTransferElement_ProvinceList);
    	return menuTerrTariffeTransfer;
    }
    
    
    static public class TerritorioAndataArrivo {
    	List<Province> listProvince;
    	List<Aeroporti> listAeroporti;
    	List<PortiNavali> listPortiNavali;
    	List<Musei> listMusei;
		public List<Province> getListProvince() {
			return listProvince;
		}
		public void setListProvince(List<Province> listProvince) {
			this.listProvince = listProvince;
		}
		public List<Aeroporti> getListAeroporti() {
			return listAeroporti;
		}
		public void setListAeroporti(List<Aeroporti> listAeroporti) {
			this.listAeroporti = listAeroporti;
		}
		public List<PortiNavali> getListPortiNavali() {
			return listPortiNavali;
		}
		public void setListPortiNavali(List<PortiNavali> listPortiNavali) {
			this.listPortiNavali = listPortiNavali;
		}
		public List<Musei> getListMusei() {
			return listMusei;
		}
		public void setListMusei(List<Musei> listMusei) {
			this.listMusei = listMusei;
		}
    }
    
    
    static public class ElementTerritorio {
    	String urlMacroRegione;
    	String urlRegione;
    	String urlProvincia;
    	
		public ElementTerritorio(String urlMacroRegione, String urlRegione, String urlProvincia) {
			super();
			this.urlMacroRegione = urlMacroRegione;
			this.urlRegione = urlRegione;
			this.urlProvincia = urlProvincia;
		}
		
		public String getUrlMacroRegione() {
			return urlMacroRegione;
		}
		public void setUrlMacroRegione(String urlMacroRegione) {
			this.urlMacroRegione = urlMacroRegione;
		}
		public String getUrlRegione() {
			return urlRegione;
		}
		public void setUrlRegione(String urlRegione) {
			this.urlRegione = urlRegione;
		}
		public String getUrlProvincia() {
			return urlProvincia;
		}
		public void setUrlProvincia(String urlProvincia) {
			this.urlProvincia = urlProvincia;
		}
    }

    
    static public class MenuTerrTariffeTransfer {
    	private List<MenuTerrTariffeTransferElement> macroRegioni;
    	private List<MenuTerrTariffeTransferElement> regioni;
    	private List<MenuTerrTariffeTransferElement> province;
    	private TransferTariffeProvince transferTariffeProvince;
    	private String urlCanonical;
    	private String title;
    	private String metaDescription;
		public String getMetaDescription() {
			return metaDescription;
		}
		public void setMetaDescription(String metaDescription) {
			this.metaDescription = metaDescription;
		}
		public String getUrlCanonical() {
			return urlCanonical;
		}
		public void setUrlCanonical(String urlCanonical) {
			this.urlCanonical = urlCanonical;
		}
		public TransferTariffeProvince getTransferTariffeProvince() {
			return transferTariffeProvince;
		}
		public void setTransferTariffeProvince(TransferTariffeProvince transferTariffeProvince) {
			this.transferTariffeProvince = transferTariffeProvince;
		}
		public List<MenuTerrTariffeTransferElement> getMacroRegioni() {
			return macroRegioni;
		}
		public void setMacroRegioni(List<MenuTerrTariffeTransferElement> macroRegioni) {
			this.macroRegioni = macroRegioni;
		}
		public List<MenuTerrTariffeTransferElement> getRegioni() {
			return regioni;
		}
		public void setRegioni(List<MenuTerrTariffeTransferElement> regioni) {
			this.regioni = regioni;
		}
		public List<MenuTerrTariffeTransferElement> getProvince() {
			return province;
		}
		public void setProvince(List<MenuTerrTariffeTransferElement> province) {
			this.province = province;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		static public class MenuTerrTariffeTransferElement {
			private Long id;
    		private String territorio;
    		private String url;
    		private boolean selezionato;
			public Long getId() {
				return id;
			}
			public void setId(Long id) {
				this.id = id;
			}
			public String getTerritorio() {
    			return territorio;
    		}
			public void setTerritorio(String territorio) {
    			this.territorio = territorio;
    		}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public boolean isSelezionato() {
    			return selezionato;
    		}
    		public void setSelezionato(boolean selezionato) {
    			this.selezionato = selezionato;
    		}
    		
    	}
    }
}

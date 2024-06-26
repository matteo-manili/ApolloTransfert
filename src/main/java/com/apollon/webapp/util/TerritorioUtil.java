package com.apollon.webapp.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.apollon.Constants;
import com.apollon.dao.AutistaZoneDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RegioniDao;
import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaZone;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.model.VenditorePercServProvincia;
import com.apollon.util.NumberUtil;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe;
import com.apollon.webapp.util.bean.AutistaTerritorio.AutistiProvincia;
import com.apollon.webapp.util.bean.AutistaTerritorio.AutistiRegione;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe.Regioni_Entity;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe.Regioni_Entity.Province_Entity;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TerritorioUtil extends ApplicationUtils{
	
	private static AutistaZoneDao autistaZoneDao = (AutistaZoneDao) contextDao.getBean("AutistaZoneDao");
	private static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	private static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	private static VenditorePercServProvinciaDao venditorePercServProvinciaDao = (VenditorePercServProvinciaDao) contextDao.getBean("VenditorePercServProvinciaDao");
	
	public static List<TabellaMacroRegioniProvinceTariffe> DammiTabellaMacroRegioniProvinceTariffe(Long idUserVenditore, List<Regioni> regioniList, List<Province> provinceList) { 
		int percentualeDefaultVenditore = DammiParametriPercentualiVenditori().get( Constants.VENDIDORE_PERC_DEFAULT );
		Map<Long, Integer> map_ProvPercServiz_Venditore = new HashMap<Long, Integer>();
		if(idUserVenditore != null){
			List<VenditorePercServProvincia> venditorePercServizListProvinceList = venditorePercServProvinciaDao
					.getVenditorePercServProvincia_by_Venditore(idUserVenditore);
			for(VenditorePercServProvincia ite: venditorePercServizListProvinceList){
				map_ProvPercServiz_Venditore.put(ite.getProvince().getId(), ite.getPercentualeServizio());
			}
		}
		// Elimino le regioni STRANIERE
		for(int ite=0; ite < regioniList.size(); ite++ ){
			//if( regioniList.get(ite).getNomeRegione().equals(Constants.REGIONE_STRANIERA) ){
			if( !regioniList.get(ite).getNazione().getSiglaNazione().equals(Constants.ITALIA) ){
				provinceList.remove(ite);
			}
		}
		// Elimino le province STRANIERE
		for(int ite=0; ite < provinceList.size(); ite++ ){
			//if(provinceList.get(ite).getNomeProvincia().equals(Constants.PROVINCIA_STRANIERA) || provinceList.get(ite).getNumeroAbitanti() == null){
			if( !provinceList.get(ite).getRegioni().getNazione().getSiglaNazione().equals(Constants.ITALIA) ){
				provinceList.remove(ite);
			}
		}
		// Se faccio una comparazione con Null va in errore. Esempio copiato 
		// in https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
		// You can now write the last example in a shorter form by using a lambda expression for the Comparator:
		regioniList.sort(Comparator.comparing(Regioni::getNomeRegione));
		provinceList.sort(Comparator.comparing(Province::getNumeroAbitanti).reversed());

    	List<TabellaMacroRegioniProvinceTariffe> Oggettone_List = new ArrayList<TabellaMacroRegioniProvinceTariffe>();
    	for(MacroRegioni macroReg_ite: regioniDao.getMacroRegioniList()){
    		TabellaMacroRegioniProvinceTariffe TabellaMacro = new TabellaMacroRegioniProvinceTariffe();
			List<Regioni_Entity> regioniList_Entity = new ArrayList<Regioni_Entity>();
			for(Regioni entryReg : regioniList){
				if( entryReg.getMacroRegioni() != null && entryReg.getMacroRegioni().getId() == macroReg_ite.getId() ){
					TabellaMacroRegioniProvinceTariffe.Regioni_Entity Tab_Reg = new Regioni_Entity();
					Tab_Reg.setRegione(entryReg);
					regioniList_Entity.add(Tab_Reg);
					List<Province_Entity> provList = new ArrayList<Province_Entity>();
					for(Province entryProv : provinceList){
						if(entryProv.getRegioni().getId() == entryReg.getId() ){
							TabellaMacroRegioniProvinceTariffe.Regioni_Entity.Province_Entity Tab_Reg_Prov = new Province_Entity();
							Tab_Reg_Prov.setProvincia(entryProv);
							if(map_ProvPercServiz_Venditore.get(entryProv.getId()) != null){
								Tab_Reg_Prov.setPercentualeServizioVenditore(map_ProvPercServiz_Venditore.get(entryProv.getId()));
							}else{
								Tab_Reg_Prov.setPercentualeServizioVenditore(percentualeDefaultVenditore);
							}
							provList.add(Tab_Reg_Prov);
						}
					}
					Tab_Reg.setProvince_Entity(provList);
				}
			}
			
			TabellaMacro.setMacroRegione(macroReg_ite);
			TabellaMacro.setRegioni_Entity(regioniList_Entity);
			Oggettone_List.add(TabellaMacro);
    	}
		return Oggettone_List;
    }
	
	
	public static void ReportGenerale(){
		int autistaTot = 0;
		for(Autista autista_ite: autistaDao.getAutistiList()){
			if(autista_ite.isAttivo() && !autista_ite.isBannato()){
				autistaTot++;
			}
		}
		int autistaZoneTot = 0;
		for(Autista autista_ite: autistaDao.getAutistiList()){
			if(autista_ite.isAttivo() && !autista_ite.isBannato() && autistaZoneDao.ControllaServiziAttivi(autista_ite.getId())){
				autistaZoneTot++;
			}
		}
	}
	
	
	public static AutistaTerritorio ProvinciaAutistaList(){
    	List<AutistiProvincia> provinciaAutistaList = new ArrayList<AutistiProvincia>();
    	List<AutistaZone> autistaZone_list = autistaZoneDao.getAutistaZone();
    	List<Long> autistiList = new ArrayList<Long>();
    	int numAutisti = 0;
		for(Province listprovince_ite: provinceDao.getProvince()){
			AutistiProvincia autistiProvincia = new AutistiProvincia();
			autistiProvincia.setNomeProvincia(listprovince_ite.getNomeProvincia());
			List<Long> listAutista = new ArrayList<Long>();
			for(AutistaZone autistaZona_ite: autistaZone_list){
				if(autistaZona_ite.getProvince() != null){
					if( autistaZona_ite.getAutista().getAutistaDocumento().isApprovatoGenerale() && 
							autistaZona_ite.getAutista().isAttivo() && !autistaZona_ite.getAutista().isBannato() &&
									autistaZona_ite.getProvince().getId() == listprovince_ite.getId()){
						if(!listAutista.contains( autistaZona_ite.getAutista().getId())){
							listAutista.add( autistaZona_ite.getAutista().getId() );
							numAutisti++;
							autistiList.add( autistaZona_ite.getAutista().getId() );
						}
					}
				}
			}
			autistiProvincia.setAutisti(listAutista);
			provinciaAutistaList.add(autistiProvincia);
		}
		autistiList = NumberUtil.removeDuplicatesLong( autistiList );
		AutistaTerritorio autistaTerr = new AutistaTerritorio();
		autistaTerr.setAutistiProvincia(provinciaAutistaList);
		autistaTerr.setTotaleAutisti(numAutisti);
		return autistaTerr;
    }
	

	public static AutistaTerritorio RegioneAutistaList(){
    	List<AutistiRegione> regioneAutistaList = new ArrayList<AutistiRegione>();
    	List<AutistaZone> autistaZone_list = autistaZoneDao.getAutistaZone();
    	int numAutisti = 0;
		for(Regioni listRegioni_ite: regioniDao.getRegioniItaliane()){ 
			AutistiRegione autistiRegione = new AutistiRegione();
			autistiRegione.setNomeRegione(listRegioni_ite.getNomeRegione());
			List<Long> listAutista = new ArrayList<Long>();
			for(AutistaZone autistaZona_ite: autistaZone_list){
				if(autistaZona_ite.getProvince() != null){
					if(autistaZona_ite.getAutista().isAttivo() && !autistaZona_ite.getAutista().isBannato() &&
						autistaZona_ite.getProvince().getRegioni().getId() == listRegioni_ite.getId()){
						if(!listAutista.contains( autistaZona_ite.getAutista().getId())){
							listAutista.add( autistaZona_ite.getAutista().getId() );
							numAutisti++;
						}
					}
				}
			}
			autistiRegione.setAutisti(listAutista);
			regioneAutistaList.add(autistiRegione);
		}
		AutistaTerritorio autistaTerr = new AutistaTerritorio();
		autistaTerr.setAutistiRegione(regioneAutistaList);
		autistaTerr.setTotaleAutisti(numAutisti);
		return autistaTerr;
    }
	
	
	public static List<Province> ProvinceAutistiDisponibili_Provincia(){
		int numMinAutistiCorsaMedia = (int)(long) gestioneApplicazioneDao.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber();
		List<Province> listNomeProvince = new ArrayList<Province>();
		List<Object[]> provinceList = ricercaTransfertDao.AutistiZoneItalia_ServizioStandard_AUTO_HQL();
		for(Object ite[]: provinceList){
			Province prov_ite = (Province)ite[0];
			long count = (long)ite[1];
			if(count >= numMinAutistiCorsaMedia){
				listNomeProvince.add( prov_ite );
			}
		}
		return listNomeProvince;
	}
	
	
	public static List<String> ProvinceAutistiDisponibiliCorsa_NomeProvincia(String TipoServizio){
		int numMinAutistiCorsaMedia = 1;
		if(TipoServizio.equals(Constants.SERVIZIO_STANDARD)) {
			numMinAutistiCorsaMedia = (int)(long) gestioneApplicazioneDao.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber();
		}else {
			numMinAutistiCorsaMedia = (int)(long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_PART").getValueNumber();
		}
		List<String> listNomeProvince = new ArrayList<String>();
		List<Object[]> provinceList = ricercaTransfertDao.AutistiZoneItalia_ServizioStandard_AUTO_HQL();
		for(Object ite[]: provinceList){
			Province prov_ite = (Province)ite[0];
			long count = (long)ite[1];
			if(count >= numMinAutistiCorsaMedia){
				listNomeProvince.add( prov_ite.getNomeProvincia() );
			}
		}
		return listNomeProvince;
	}
	
	
	public static List<String> ProvinceAutistiDisponibili_SiglaProvincia(){
		int numMinAutistiCorsaMedia = (int)(long) gestioneApplicazioneDao.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber();
		List<String> listSigleProvince = new ArrayList<String>();
		List<Object[]> provinceList = ricercaTransfertDao.AutistiZoneItalia_ServizioStandard_AUTO_HQL();
		for(Object ite[]: provinceList){
			Province prov_ite = (Province)ite[0];
			long count = (long)ite[1];
			if(count >= numMinAutistiCorsaMedia){
				listSigleProvince.add( prov_ite.getSiglaProvincia() );
			}
		}
		return listSigleProvince;
	}
	
	
}

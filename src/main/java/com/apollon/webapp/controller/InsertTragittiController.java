package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.model.Aeroporti;
import com.apollon.model.Autista;
import com.apollon.model.AutistaMusei;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Comuni;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.User;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.ZoneGiornataCompleta;
import com.apollon.model.ZoneLungaPercorrenza;
import com.apollon.model.ZoneMatrimoni;
import com.apollon.service.AeroportiManager;
import com.apollon.service.AutistaAeroportiManager;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutistaMuseiManager;
import com.apollon.service.AutistaPortiNavaliManager;
import com.apollon.service.AutistaZoneManager;
import com.apollon.service.ComuniManager;
import com.apollon.service.MuseiManager;
import com.apollon.service.PortiNavaliManager;
import com.apollon.service.ProvinceManager;
import com.apollon.util.NumberUtil;
import com.apollon.webapp.util.MenuAutistaAttribute;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class InsertTragittiController extends BaseFormController {
    
	
	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private AutistaZoneManager autistaZoneManager;
    @Autowired
    public void setAutistaZoneManager(final AutistaZoneManager autistaZoneManager) {
        this.autistaZoneManager = autistaZoneManager;
    }
    
    private AutistaAeroportiManager autistaAeroportiManager;
    @Autowired
    public void setAutistaAeroportiManager(final AutistaAeroportiManager autistaAeroportiManager) {
        this.autistaAeroportiManager = autistaAeroportiManager;
    }
    
    private AutistaPortiNavaliManager autistaPortiNavaliManager;
    @Autowired
    public void setAutistaPortiNavaliManager(final AutistaPortiNavaliManager autistaPortiNavaliManager) {
        this.autistaPortiNavaliManager = autistaPortiNavaliManager;
    }
    
    private AutistaMuseiManager autistaMuseiManager;
    @Autowired
    public void setAutistaMuseiManager(final AutistaMuseiManager autistaMuseiManager) {
        this.autistaMuseiManager = autistaMuseiManager;
    }
    
    private AeroportiManager aeroportiManager;
	@Autowired
	public void setAeroportiManager(AeroportiManager aeroportiManager) {
		this.aeroportiManager = aeroportiManager;
	}
    
	private PortiNavaliManager portiNavaliManager;
	@Autowired
	public void setPortiNavaliManager(PortiNavaliManager portiNavaliManager) {
		this.portiNavaliManager = portiNavaliManager;
	}
	
	private MuseiManager museiManager;
	@Autowired
	public void setMuseiManager(MuseiManager museiManager) {
		this.museiManager = museiManager;
	}
    
    private ComuniManager comuniManager;
    @Autowired
    public void setComuniManager(final ComuniManager comuniManager) {
        this.comuniManager = comuniManager;
    }
    
    private ProvinceManager provinceManager;
    @Autowired
    public void setProvinceManager(final ProvinceManager provinceManager) {
        this.provinceManager = provinceManager;
    }
	
	@RequestMapping(value = "/insert-tragitti", method = RequestMethod.POST)
    public ModelAndView onSubmitTragitti( @ModelAttribute("autista") final Autista autistaMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response){
		log.info("sono in onSubmitTragitti POST");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("insert-tragitti");
		
		if (request.getParameter("cancel") != null) {
			
        }
		
		try {
			Autista autistaCorrente = autistaManager.get(autistaMod.getId());
			
			if(request.getParameter("salva") != null){
				//Iterator<AutistaZone> ite_provSelez = autistaManager.get(autistaCorrente.getId()).getAutistaZone().iterator();
				Iterator<AutistaZone> ite_provSelez = autistaManager.lazyAutistaZone(autistaCorrente.getId()).iterator();
		        while(ite_provSelez.hasNext()){
		        	AutistaZone aZona = ite_provSelez.next();
		        	
		        	String checkValue = request.getParameter("autistaZoneList.servizioAttivo["+ aZona.getId() +"]");
		        	if(checkValue != null){
		        		aZona.setServizioAttivo(true);
		        	}else{
		        		aZona.setServizioAttivo(false);
		        	}
		        	checkValue = request.getParameter("autistaZoneList.zoneGiornataCompleta["+ aZona.getId() +"]");
		        	if(checkValue != null){
		        		aZona.getZoneGiornataCompleta().setServizioAttivo(true);
		        	}else{
		        		aZona.getZoneGiornataCompleta().setServizioAttivo(false);
		        	}
		        	checkValue = request.getParameter("autistaZoneList.zoneLungaPercorrenza["+ aZona.getId() +"]");
		        	if(checkValue != null){
		        		aZona.getZoneLungaPercorrenza().setServizioAttivo(true);
		        	}else{
		        		aZona.getZoneLungaPercorrenza().setServizioAttivo(false);
		        	}
		        	checkValue = request.getParameter("autistaZoneList.zoneMatrimoni["+ aZona.getId() +"]");
		        	if(checkValue != null){
		        		aZona.getZoneMatrimoni().setServizioAttivo(true);
		        	}else{
		        		aZona.getZoneMatrimoni().setServizioAttivo(false);
		        	}
		        	autistaZoneManager.saveAutistaZone(aZona);
		        }
		        
		        //TODO - NON UTILIZZO PIU LE INFRASTRUTTURE
		        //SalvaInfrastruttureSerivizioAttivoCheckBox(request, autistaCorrente);
			} //fine salva
			
			
			if(request.getParameter("rimuovi") != null){
				String stringIdZoneMiste = request.getParameter("rimuovi");
				if(stringIdZoneMiste.split("#")[0].equals("REG")){
					long idRegione = Long.parseLong( stringIdZoneMiste.split("#")[1]);
					AutistaZone rimuoviAutistaZone = autistaZoneManager.getAutistaZoneBy_Autista_e_Regione(autistaCorrente.getId(), idRegione);	
					autistaZoneManager.removeAutistaZone(rimuoviAutistaZone.getId());
					//elimina infrastture
					List<Long> ListaComuniTutti_x_infrastutture = new ArrayList<Long>();
					ListaComuniTutti_x_infrastutture.addAll( comuniManager.getListComuniByRegione_soloID( idRegione ));
					EliminaInfrastruttureNonPresenti(ListaComuniTutti_x_infrastutture, autistaCorrente);
					
				}else if (stringIdZoneMiste.split("#")[0].equals("PRO")){
					long idProvincia = Long.parseLong( stringIdZoneMiste.split("#")[1]);
					AutistaZone rimuoviAutistaZone = autistaZoneManager.getAutistaZoneBy_Autista_e_Provincia(autistaCorrente.getId(), idProvincia);	
					autistaZoneManager.removeAutistaZone(rimuoviAutistaZone.getId());
					//elimina infrastture
					List<Long> ListaComuniTutti_x_infrastutture = new ArrayList<Long>();
					ListaComuniTutti_x_infrastutture.addAll( comuniManager.getListComuniByProvincia_soloID( idProvincia ));
					EliminaInfrastruttureNonPresenti(ListaComuniTutti_x_infrastutture, autistaCorrente);
			
				}else if (stringIdZoneMiste.split("#")[0].equals("COM")){
					long idComune = Long.parseLong( stringIdZoneMiste.split("#")[1]);
					AutistaZone rimuoviAutistaZone = autistaZoneManager.getAutistaZoneBy_Autista_e_Comune(autistaCorrente.getId(), idComune);	
					autistaZoneManager.removeAutistaZone(rimuoviAutistaZone.getId());
					//elimina infrastture
					long idProvinciaComune = comuniManager.get(idComune).getProvince().getId();
					List<Long> ListaComuniTutti_x_infrastutture = new ArrayList<Long>();
					ListaComuniTutti_x_infrastutture.addAll( comuniManager.getListComuniByComuneAppartenente_soloID_Suggerimenti(idProvinciaComune));
					ListaComuniTutti_x_infrastutture.add(idComune);
					EliminaInfrastruttureNonPresenti(ListaComuniTutti_x_infrastutture, autistaCorrente);
					
				}
			} //fine rimuovi
			
			if(request.getParameter("rimuovi-tutti") != null){
				Iterator<AutistaZone> zoneTutte_ite = autistaZoneManager.getAutistaZoneByAutista(autistaCorrente.getId()).iterator();
		        while(zoneTutte_ite.hasNext()){
		        	AutistaZone zoneTutte = zoneTutte_ite.next();
		        	autistaZoneManager.removeAutistaZone(zoneTutte.getId());
		        }
		        autistaAeroportiManager.removeAutistaAeroportiByIdAutista(autistaCorrente.getId());
		        autistaPortiNavaliManager.removeAutistaPortiNavaliByIdAutista(autistaCorrente.getId());
		        autistaMuseiManager.removeAutistaMuseiByIdAutista(autistaCorrente.getId());
			} // fine rimuovi-tutti
			
			
			if(request.getParameter("aggiorna-mobile") != null){
				String stringIdZoneMiste = request.getParameter("aggiorna-mobile");
				List<Long> listIdProvinceScelte = new ArrayList<Long>();
				listIdProvinceScelte = DammiProvincieZoneAutista( autistaManager.lazyAutistaZone(autistaCorrente.getId()) );
				if (stringIdZoneMiste.split("#")[0].equals("COM")){
		    		long idComune = Long.parseLong( stringIdZoneMiste.split("#")[1]);
		    		Comuni comune = comuniManager.get(idComune);
		    		if( listIdProvinceScelte.size() == 0 ){
		    			AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
		    		}
		    		if( listIdProvinceScelte.size() == 1 ){
						if( listIdProvinceScelte.contains( comune.getProvince().getId() ) ){
							AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
						}else{
							Province prov = provinceManager.get( listIdProvinceScelte.get(0) ); 
							List<Province> listProvConfinanti = new ArrayList<Province>(prov.getProvinceConfinanti());
							List<Long> listProvConfinantiLong = new ArrayList<Long>();
							for(Province listProvConfinanti_ite: listProvConfinanti){
								listProvConfinantiLong.add(listProvConfinanti_ite.getId());
							}
							if(listProvConfinantiLong.contains( comune.getProvince().getId() ) ){
								AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
							}else{
								saveMessage(request, "non puoi inserire un comune o una provincia diversa da quelle confinanti");
							}
						}
					}
					if( listIdProvinceScelte.size() == 2 ){
						if( listIdProvinceScelte.contains( comune.getProvince().getId() ) ){
							AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
						}else{
							saveMessage(request, "non puoi inserire più di due province");
						}
					}
		    	}
				
				if (stringIdZoneMiste.split("#")[0].equals("PRO")){
		    		long idProvincia = Long.parseLong( stringIdZoneMiste.split("#")[1]);
					Province provincia = provinceManager.get(idProvincia);
					if( listIdProvinceScelte.size() == 0 ){
		    			AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
		    		}
					if( listIdProvinceScelte.size() == 1 ){
						if( listIdProvinceScelte.contains( provincia.getId() ) ){
							AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
						}else{
							Province prov = provinceManager.get( listIdProvinceScelte.get(0) ); 
							List<Province> listProvConfinanti = new ArrayList<Province>(prov.getProvinceConfinanti());
							List<Long> listProvConfinantiLong = new ArrayList<Long>();
							for(Province listProvConfinanti_ite: listProvConfinanti){
								listProvConfinantiLong.add(listProvConfinanti_ite.getId());
							}
							if(listProvConfinantiLong.contains( provincia.getId() ) ){
								AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
							}else{
								saveMessage(request, "non puoi inserire un comune o una provincia diversa da quelle confinanti");
							}
						}
					}
					if( listIdProvinceScelte.size() == 2 ){
						if( listIdProvinceScelte.contains( provincia.getId() ) ){
							AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
						}else{
							saveMessage(request, "non puoi inserire più di due province");
						}
					}
				}
			}
			
			// NON LO USO PIU è L'EX CHOSEN
			if(request.getParameter("aggiorna") != null){
				if(autistaMod.getListZoneSelezionate_TAG() != null){
					Iterator<String> ite_zoneMisteSelez = autistaMod.getListZoneSelezionate_TAG().iterator();
			        while(ite_zoneMisteSelez.hasNext()){
			        	String stringIdZoneMiste = ite_zoneMisteSelez.next();
			        	AggiungiTerritorio(stringIdZoneMiste, autistaCorrente, request);
			        } // fine while (autistaMod.getListZoneSelezionate_TAG().iterator();)
				}
			}
			//TODO - NON UTILIZZO PIU LE INFRASTRUTTURE
			//AggiornaInfrastrutture( autistaCorrente );
            return caricaFormAutistaZone(mav, autistaCorrente, request);

    	}catch (final DataIntegrityViolationException DataIntegrViolException) {
    		DataIntegrViolException.printStackTrace();
    		saveError(request, getText("errors.chiaveDuplicata", locale));
            return mav;
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return mav;
        }
    }

	/**
	 * alleggerire questa query
	 */
	private List<Long> DammiProvincieZoneAutista(List<AutistaZone> list){
		List<Long> listIdProvinceScelte = new ArrayList<Long>();
		for(AutistaZone list_ite: list){
			if( list_ite.getProvince() != null ){
				listIdProvinceScelte.add( list_ite.getProvince().getId() );
			}
		}
		return NumberUtil.removeDuplicatesLong( listIdProvinceScelte );
	}
	
	private void AggiornaInfrastrutture(Autista autistaCorrente) throws Exception {
		List<Long> ListaComuniTutti_x_infrastutture = new ArrayList<Long>();
		List<AutistaZone> autistaZone = autistaZoneManager.getAutistaZoneByAutista( autistaCorrente.getId() );
		Iterator<AutistaZone> autistaZone_ite = autistaZone.iterator();
		while(autistaZone_ite.hasNext()){
			AutistaZone autistaZoneObj = autistaZone_ite.next();
			if(autistaZoneObj.getProvince() != null){
				long idProvincia = autistaZoneObj.getProvince().getId();
				long idRegione = autistaZoneObj.getProvince().getRegioni().getId();
				if(autistaZoneManager.getAutistaZoneBy_Autista_e_Regione(autistaCorrente.getId(), idRegione) == null ){
					ListaComuniTutti_x_infrastutture.addAll( comuniManager.getListComuniByProvincia_soloID( idProvincia ));
				}
				SalvaInfrastrutture(ListaComuniTutti_x_infrastutture, autistaCorrente, true);
			}
		}
	}
	
	private void SalvaInfrastrutture(List<Long> ListaComuniTutti_x_infrastutture, Autista autistaCorrente, boolean servizioAttivo) throws Exception {
		//AEROPORTI _ INVERSO
        List<Aeroporti> listAero = aeroportiManager.getAeroporti();
		Iterator<Aeroporti> listAero_ite = listAero.iterator();
		while(listAero_ite.hasNext()){
			Aeroporti aeroporto = listAero_ite.next();
			if(ListaComuniTutti_x_infrastutture.contains( aeroporto.getComuni().getId()) ){
				AutistaAeroporti autistaAeroporto = new AutistaAeroporti();
				autistaAeroporto.setServizioAttivo(servizioAttivo);
				autistaAeroporto.setAutista( autistaCorrente );
				autistaAeroporto.setAeroporti(aeroporto);
				try{
					autistaAeroportiManager.saveAutistaAeroporti(autistaAeroporto);
	    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
	    			DataIntegrViolException.printStackTrace();
	            }
			}
    	}
        List<PortiNavali> listPortiNav = portiNavaliManager.getPortiNavali();
		Iterator<PortiNavali> listPorti_ite = listPortiNav.iterator();
		while(listPorti_ite.hasNext()){
			PortiNavali portiNav = listPorti_ite.next();
			if(ListaComuniTutti_x_infrastutture.contains( portiNav.getComuni().getId()) ){
				AutistaPortiNavali autistaPortoNav = new AutistaPortiNavali();
				autistaPortoNav.setServizioAttivo(servizioAttivo);
				autistaPortoNav.setAutista( autistaCorrente );
				autistaPortoNav.setPortiNavali(portiNav);
				try{
					autistaPortiNavaliManager.saveAutistaPortiNavali(autistaPortoNav);
	    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
	    			DataIntegrViolException.printStackTrace();
	            }
			}
    	}
        List<Musei> listMusei = museiManager.getMusei();
		Iterator<Musei> listMuseo_ite = listMusei.iterator();
		while(listMuseo_ite.hasNext()){
			Musei museo = listMuseo_ite.next();
			if(ListaComuniTutti_x_infrastutture.contains( museo.getComuni().getId()) ){
				AutistaMusei autistaMuseo = new AutistaMusei();
				autistaMuseo.setServizioAttivo(servizioAttivo);
				autistaMuseo.setAutista( autistaCorrente );
				autistaMuseo.setMusei(museo);
				try{
					autistaMuseiManager.saveAutistaMusei(autistaMuseo);
	    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
	    			DataIntegrViolException.printStackTrace();
	            }
			}
    	}
		
	}
	
	private void EliminaInfrastruttureNonPresenti(List<Long> ListaComuniTutti_x_infrastutture, Autista autistaCorrente){
		//elimina gli aeroporti non selezionati nelle zone
        Iterator<AutistaAeroporti> autistaAeroporti_ite = autistaAeroportiManager.
        		getAutistaAeroportiByIdAutista( autistaCorrente.getId() ).iterator();
        while(autistaAeroporti_ite.hasNext()){
        	AutistaAeroporti autistaAeroporto = autistaAeroporti_ite.next();
        	if(ListaComuniTutti_x_infrastutture.contains(autistaAeroporto.getAeroporti().getComuni().getId())){
        		autistaAeroportiManager.removeAutistaAeroporti(autistaAeroporto.getId());
        	}
        }
        
        Iterator<AutistaPortiNavali> autistaPortiNavali_ite = autistaPortiNavaliManager.
        		getAutistaPortiNavaliByIdAutista( autistaCorrente.getId() ).iterator();
        while(autistaPortiNavali_ite.hasNext()){
        	AutistaPortiNavali autistaPortoNavale = autistaPortiNavali_ite.next();
        	if(ListaComuniTutti_x_infrastutture.contains(autistaPortoNavale.getPortiNavali().getComuni().getId())){
        		autistaPortiNavaliManager.removeAutistaPortiNavali(autistaPortoNavale.getId());
        	}
        }
        
        Iterator<AutistaMusei> autistaMusei_ite = autistaMuseiManager.
        		getAutistaMuseiByIdAutista( autistaCorrente.getId() ).iterator();
        while(autistaMusei_ite.hasNext()){
        	AutistaMusei autistaMuseo = autistaMusei_ite.next();
        	if(ListaComuniTutti_x_infrastutture.contains(autistaMuseo.getMusei().getComuni().getId())){
        		autistaMuseiManager.removeAutistaMusei(autistaMuseo.getId());
        	}
        }
	}
	
	
    private void SalvaInfrastruttureSerivizioAttivoCheckBox(HttpServletRequest request, Autista autistaCorrente) throws Exception{
		Iterator<AutistaAeroporti> ite_autistaAeroporti = autistaManager.lazyAutistaAeroporti(autistaCorrente.getId()).iterator();
        while(ite_autistaAeroporti.hasNext()){
        	AutistaAeroporti aAutistaAeroporti = ite_autistaAeroporti.next();
        	String checkValue = request.getParameter("checkBoxAutistaAeroporti["+ aAutistaAeroporti.getId() +"]");
        	if(checkValue != null){
        		aAutistaAeroporti.setServizioAttivo(true);
        	}else{
        		aAutistaAeroporti.setServizioAttivo(false);
        	}
        	autistaAeroportiManager.saveAutistaAeroporti(aAutistaAeroporti);
        }
        Iterator<AutistaPortiNavali> ite_autistaPortiNavali = autistaManager.lazyAutistaPortiNavali(autistaCorrente.getId()).iterator();
        while(ite_autistaPortiNavali.hasNext()){
        	AutistaPortiNavali autistaPortiNavali = ite_autistaPortiNavali.next();
        	String checkValue = request.getParameter("checkBoxAutistaPorti["+ autistaPortiNavali.getId() +"]");
        	if(checkValue != null){
        		autistaPortiNavali.setServizioAttivo(true);
        	}else{
        		autistaPortiNavali.setServizioAttivo(false);
        	}
        	autistaPortiNavaliManager.saveAutistaPortiNavali(autistaPortiNavali);
        }
        /*
        Iterator<AutistaMusei> ite_autistaMusei = autistaManager.get(autistaCorrente.getId()).getAutistaMusei().iterator();
        while(ite_autistaMusei.hasNext()){
        	AutistaMusei autistaMusei = ite_autistaMusei.next();
        	String checkValue = request.getParameter("checkBoxAutistaMusei["+ autistaMusei.getId() +"]");
        	if(checkValue != null){
        		autistaMusei.setServizioAttivo(true);
        	}else{
        		autistaMusei.setServizioAttivo(false);
        	}
        	autistaMuseiManager.saveAutistaMusei(autistaMusei);
        }
        */
	}
    
    
    private void AggiungiTerritorio(String stringIdZoneMiste, Autista autistaCorrente, HttpServletRequest request){
    	if(stringIdZoneMiste.split("#")[0].equals("PRO")){
    		long idProvincia = Long.parseLong( stringIdZoneMiste.split("#")[1]);
    		if( autistaZoneManager.getAutistaZoneBy_Autista_e_Provincia(autistaCorrente.getId(), idProvincia ) == null ){
				Province provincia = provinceManager.get(idProvincia);
				AutistaZone autistaZona = new AutistaZone();
	        	autistaZona.setAutista(autistaCorrente);
	        	autistaZona.setServizioAttivo(true);
	        	ZoneGiornataCompleta zonaGiornataCompl = new ZoneGiornataCompleta();
	        	zonaGiornataCompl.setAutistaZone(autistaZona);
	        	autistaZona.setZoneGiornataCompleta( zonaGiornataCompl );
	        	ZoneLungaPercorrenza zoneLungaPercorr = new ZoneLungaPercorrenza();
	        	zoneLungaPercorr.setAutistaZone(autistaZona);
	        	zoneLungaPercorr.setServizioAttivo(true);
	        	autistaZona.setZoneLungaPercorrenza(zoneLungaPercorr);
	        	ZoneMatrimoni zoneMatrimoni = new ZoneMatrimoni();
	        	zoneMatrimoni.setAutistaZone(autistaZona);
	        	autistaZona.setZoneMatrimoni(zoneMatrimoni);
	        	autistaZona.setProvince(provincia);
	        	autistaZoneManager.saveAutistaZone(autistaZona);
    		}
    		
    	}else if (stringIdZoneMiste.split("#")[0].equals("COM")){
    		long idComune = Long.parseLong( stringIdZoneMiste.split("#")[1]);
    		if( autistaZoneManager.getAutistaZoneBy_Autista_e_Comune(autistaCorrente.getId(), idComune ) == null ){
    			Comuni comune = comuniManager.get(idComune);
				AutistaZone autistaZona = new AutistaZone();
	        	autistaZona.setAutista(autistaCorrente);
	        	autistaZona.setServizioAttivo(true);
	        	ZoneGiornataCompleta zonaGiornataCompl = new ZoneGiornataCompleta();
	        	zonaGiornataCompl.setAutistaZone(autistaZona);
	        	autistaZona.setZoneGiornataCompleta( zonaGiornataCompl);
	        	ZoneLungaPercorrenza zoneLungaPercorr = new ZoneLungaPercorrenza();
	        	zoneLungaPercorr.setAutistaZone(autistaZona);
	        	zoneLungaPercorr.setServizioAttivo(true);
	        	autistaZona.setZoneLungaPercorrenza(zoneLungaPercorr);
	        	ZoneMatrimoni zoneMatrimoni = new ZoneMatrimoni();
	        	zoneMatrimoni.setAutistaZone(autistaZona);
	        	autistaZona.setZoneMatrimoni(zoneMatrimoni);
	        	autistaZoneManager.saveAutistaZone(autistaZona);
    		}
    	}
    }
    
    
    private ModelAndView caricaFormAutistaZone(ModelAndView mav, Autista autistaCorrente, HttpServletRequest request) throws Exception{
    	mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autistaCorrente, 2, request ) );
        Autista autista = autistaManager.get(autistaCorrente.getId());
        mav.addObject("autista", autista);
        List<AutistaZone> autistaZoneList = autistaManager.lazyAutistaZone(autistaCorrente.getId());
        mav.addObject("autistaZoneList", autistaZoneList);
        // TODO - NON UTILIZZO PIU LE INFRASTRUTTURE
        //List<AutistaAeroporti> zoneAeroList = autistaManager.lazyAutistaAeroporti(autistaCorrente.getId());
		//List<AutistaPortiNavali> zonePortiList = autistaManager.lazyAutistaPortiNavali(autistaCorrente.getId());
        mav.addObject("autistaAeroportiList", null /*zoneAeroList*/); 
        mav.addObject("autistaPortiNavaliList", null /*zonePortiList*/);
        List<Long> provZoneAutista = DammiProvincieZoneAutista(autistaZoneList);
        if(provZoneAutista.size() == 1){
	        Province prov = provinceManager.get( DammiProvincieZoneAutista(autistaZoneList).get(0) ); 
			List<Province> listProvConfinanti = new ArrayList<Province>(prov.getProvinceConfinanti());
			mav.addObject("listProvConfinanti", listProvConfinanti);
        }else if(provZoneAutista.size() == 2){
	        Province prov = provinceManager.get( DammiProvincieZoneAutista(autistaZoneList).get(1) ); 
			List<Province> listProvConfinanti = new ArrayList<Province>(prov.getProvinceConfinanti());
			mav.addObject("listProvConfinanti", listProvConfinanti);
        }else if(provZoneAutista.size() == 0){
        	mav.addObject("listProvConfinanti", null);
        }
    	return mav;
    }
    
    
    @RequestMapping(value = "/insert-tragitti", method = RequestMethod.GET)
    protected ModelAndView insertTragittiGET(final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(required = false, value = "idAutista") String idAutista) {
    	log.info("sono in insertTragitti GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("insert-tragitti");
    	try{
			Autista autista;
			if (idAutista != null && !idAutista.trim().equals("") && 
					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)) ){
				autista = autistaManager.get( Long.parseLong(idAutista) );
				User userCorrente = getUserManager().getUserByUsername(request.getRemoteUser());
				if( autista.getUser().getId() != userCorrente.getId() && (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autista.getCommerciale() != null && autista.getCommerciale().getId() != userCorrente.getId()) 
						|| (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autista.getCommerciale() == null)  ){
					saveError(request, getText("errors.violation.update.autista.commerciale", locale));
					return new ModelAndView("redirect:/admin/gestioneAutista?idAutista="+idAutista);
				}
			}else{
				User user = getUserManager().getUserByUsername(request.getRemoteUser());
    			autista = autistaManager.getAutistaByUser(user.getId());
			}
			return caricaFormAutistaZone(mav, autista, request);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("insert-tragitti");
    	}
    }
    
    
    
} //fine classe

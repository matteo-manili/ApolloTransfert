package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.service.ProvinceManager;
import com.apollon.service.RegioniManager;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminProvinceController extends BaseFormController {
    
	private ProvinceManager provinceManager;
	@Autowired
	public void setProvinceManager(ProvinceManager provinceManager) {
		this.provinceManager = provinceManager;
	}
	
	private RegioniManager regioniManager;
	@Autowired
	public void setRegioniManager(RegioniManager regioniManager) {
		this.regioniManager = regioniManager;
	}
	
	@RequestMapping(value = "/admin/admin-tableProvince", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("provincia") final Province provinceMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-province");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) { }
		try {
			if(request.getParameter("imposta-tariffa-regione") != null && request.getParameter("tariffa-regione") != null) {
				String idReg = request.getParameter("regione-tariffe") ;
				String TariffaBase = request.getParameter("tariffa-regione");
				if(idReg == null){
					saveMessage(request, "Seleziona una Regione");
					return caricaAdminTable(mav, null, "", null, false);
					
				}else if(idReg.equals("")){
					saveMessage(request, "Seleziona una Regione");
					return caricaAdminTable(mav, null, "", null, false); 
					
				}else if(idReg.equals("0")){ //Tutte le Regini
					for(Province prov_ite: provinceManager.getProvince()){
						prov_ite.setTariffaBase( new BigDecimal(TariffaBase) );
						provinceManager.saveProvince(prov_ite);
					}
				}else if(!idReg.equals("0")){
					Regioni regione = regioniManager.get(Long.parseLong(idReg));
					for(Province prov_ite: provinceManager.getProvinceByIdRegione(regione.getId())){
						prov_ite.setTariffaBase( new BigDecimal(TariffaBase) );
						provinceManager.saveProvince(prov_ite);
					}
				}else{
					return caricaAdminTable(mav, null, "", null, false); 
				}
			}
			
			if (request.getParameter("modifica") != null) {
				String idReg = request.getParameter("regioni.nomeRegione") ;
				if(idReg != null){
					Regioni regione = regioniManager.get(Long.parseLong(idReg));
					provinceMod.setRegioni(regione);
				}
				// prendi province confinanti - chosen
				if(provinceMod.getProvinceConfinanti_TAG() != null){
					List<String> listProv = new ArrayList<String>(provinceMod.getProvinceConfinanti_TAG());
					for(String listProv_ite: listProv){
						provinceMod.addProvinciaConfinante( provinceManager.get(Long.parseLong(listProv_ite)) );
					}
				}
				provinceMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(provinceMod.getNomeProvincia()) );
				Province provinciaModificato = provinceManager.saveProvince(provinceMod);
	        	saveMessage(request, "provincia modificata: "+provinciaModificato.getNomeProvincia() /*getText("user.saved", locale )*/ );
	        	return caricaAdminTable(mav, null, "", provinciaModificato.getId().toString(), true);
			
			}else if (request.getParameter("aggiungi") != null){
				String idReg = request.getParameter("regioni.nomeRegione");
				if(idReg != null){
					Regioni regione = regioniManager.get(Long.parseLong(idReg));
					provinceMod.setRegioni(regione);
				}
				provinceMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(provinceMod.getNomeProvincia()) );
				Province provinciaSalvato = provinceManager.saveProvince(provinceMod);
	        	saveMessage(request, "provincia aggiunta: "+provinciaSalvato.getNomeProvincia() /*getText("user.saved", locale )*/ );
	        	return caricaAdminTable(mav, null, "", provinciaSalvato.getId().toString(), true);
			
			}else if (request.getParameter("elimina") != null){
				provinceManager.removeProvince(provinceMod.getId());
	        	saveMessage(request, "provincia eliminato" /*getText("user.saved", locale )*/ );
	        	return caricaAdminTable(mav, null, "", null, false);
	        }

	        return caricaAdminTable(mav, null, "", "", false);

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		dataIntegrViolException.printStackTrace();
    		return caricaAdminTable(mav, null, "", null, false);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, null, "", null, false);
        }
    }
	
	
    private ModelAndView caricaAdminTable(ModelAndView mav, String search_type, String query, String idProvincia, boolean modifica) throws Exception{
    	List<Regioni> regioniItalianeList = regioniManager.getRegioniItaliane();
    	List<Province> provinceList = provinceManager.getProvince();
    	mav.addObject("TabellaMacroRegioni", TerritorioUtil.DammiTabellaMacroRegioniProvinceTariffe(null, regioniItalianeList, provinceList) );
    	if(query == null || "".equals(query.trim()) && search_type == null || "".equals(search_type.trim())) {
			mav.addObject("provinceList", provinceList);
        }else{
        	if(search_type.equals("regione")){
        		mav.addObject("provinceList", provinceManager.getNomeProvinciaBy_Like_NomeRegione(query) );
        		
        	}else if(search_type.equals("provincia")){
        		mav.addObject("provinceList", provinceManager.getNomeProvinciaBy_Like(query) );
        	}
        }
    	if(idProvincia == null || "".equals(idProvincia.trim())) {
    		mav.addObject("provincia", new Province());
    		mav.addObject("regioniList", regioniItalianeList );
    	}else{
			Province provincia = provinceManager.get( Long.parseLong(idProvincia) );
			RicercaTransfert_GoogleMaps_Info INFO_provinciaPORTO = null;
			GMaps_Api GMaps_Api = new GMaps_Api();
			INFO_provinciaPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(provincia.getNomeProvincia(), "it");
			if(INFO_provinciaPORTO==null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(provincia.getNomeProvincia(), "it");
				if(predictions1 != null){
					INFO_provinciaPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, "it");
				}
			}
			mav.addObject("regioniList", regioniItalianeList );
			mav.addObject("modifica", true );
			mav.addObject("infoGoogle", INFO_provinciaPORTO );
			mav.addObject("provincia", provincia );
    	}
    	
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableProvince", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "search_type") String search_type,
    		@RequestParam(required = false, value = "idProvincia") String idProvincia) {
    	ModelAndView mav = new ModelAndView("admin/admin-table-province");
    	try{
			mav = caricaAdminTable(mav, search_type, query, idProvincia, false);
			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
    
} //fine classe

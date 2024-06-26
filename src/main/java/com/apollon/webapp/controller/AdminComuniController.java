package com.apollon.webapp.controller;

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
import com.apollon.model.Comuni;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.service.ComuniManager;
import com.apollon.service.ProvinceManager;
import com.apollon.service.RegioniManager;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminComuniController extends BaseFormController {
    
	
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
	
	private ComuniManager comuniManager;
	@Autowired
	public void setComuniManager(ComuniManager comuniManager) {
		this.comuniManager = comuniManager;
	}

	@RequestMapping(value = "/admin/admin-tableComuni", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("comune") final Comuni comuniMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		
		ModelAndView mav = new ModelAndView("admin/admin-table-comuni");
		log.debug("admin/admin-tableComuni POST");
		final Locale locale = request.getLocale();
		
		/* non lo uso nel valitation.xml
		if (validator != null) { // validator is null during testing
    		validator.validate(comuniMod, errors);
            if (errors.hasErrors() ) { // don't validate when deleting
            	return mav;
            }
        }
        */
		if (request.getParameter("cancel") != null) {

        }
		try {
			if (request.getParameter("modifica") != null) {
				String idReg = request.getParameter("regioni.nomeRegione") ;
				if(idReg != null){
					Regioni regione = regioniManager.get(Long.parseLong(idReg));
					comuniMod.setRegioni(regione);
				}
				String idProv = request.getParameter("province.nomeProvincia") ;
				if(idProv != null){
					Province prov = provinceManager.get(Long.parseLong(idProv));
					comuniMod.setProvince(prov);
				}
				if(comuniMod.getRegioni().getId() != comuniMod.getProvince().getRegioni().getId()){
					saveError(request, "la provincia non corrisponde alla regione");
					mav = caricaAdminTable(mav, "", comuniMod.getId().toString(), true);
		            return mav;
				}
				
				Comuni comuneModificato = comuniManager.saveComuni(comuniMod);
	        	saveMessage(request, "comune modificato: "+comuneModificato.getNomeComune() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", comuneModificato.getId().toString(), true);
	            return mav;
	            
			}else if (request.getParameter("elimina") != null) {
				comuniManager.removeComuni(comuniMod.getId());
	        	saveMessage(request, "comune eliminato" /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", null, false);
	            return mav;

	        }else if (request.getParameter("aggiungi") != null) {
				String idReg = request.getParameter("regioni.nomeRegione") ;
				if(idReg != null){
					Regioni regione = regioniManager.get(Long.parseLong(idReg));
					comuniMod.setRegioni(regione);
				}
				String idProv = request.getParameter("province.nomeProvincia") ;
				if(idProv != null){
					Province prov = provinceManager.get(Long.parseLong(idProv));
					comuniMod.setProvince(prov);
				}
				if(comuniMod.getRegioni().getId() != comuniMod.getProvince().getRegioni().getId()){
					saveError(request, "la provincia non corrisponde alla regione");
					mav = caricaAdminTable(mav, "", comuniMod.getId().toString(), true);
		            return mav;
				}
				
				Comuni comuneSalvato = comuniManager.saveComuni(comuniMod);
	        	saveMessage(request, "comune aggiunto: "+comuneSalvato.getNomeComune() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", comuneSalvato.getId().toString(), true);
	            return mav;
	        }
			

	        mav = caricaAdminTable(mav, "", "", false);
            return mav;

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return caricaAdminTable(mav, "", null, false);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, "", null, false);
        }
    }
	

	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idComune, boolean modifica) throws Exception{

    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_15;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("comuniList", comuniManager.getComuni());
        }else{
        	mav.addObject("comuniList", comuniManager.getNomeComuneBy_Like(query) );
        }
    	if (idComune == null || "".equals(idComune.trim())) {
    		mav.addObject("comune", new Comuni());
    		mav.addObject("regioniList", regioniManager.getRegioni() );
    	}else{
    		
			Comuni comune = comuniManager.get( Long.parseLong(idComune) );
			RicercaTransfert_GoogleMaps_Info INFO_comunePORTO = null;
			GMaps_Api GMaps_Api = new GMaps_Api();
			INFO_comunePORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(comune.getNomeComune(), "it");
			if(INFO_comunePORTO==null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(comune.getNomeComune(), "it");
				if(predictions1 != null){
					INFO_comunePORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, "it");
				}
			}
			mav.addObject("regioniList", regioniManager.getRegioni() );
			mav.addObject("modifica", true );
			mav.addObject("infoGoogle", INFO_comunePORTO );
			mav.addObject("comune", comune );
    		
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableComuni",  method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam( required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idComune") String idComune) {
    	log.debug("admin/admin-tableComuni GET");
    	ModelAndView mav = new ModelAndView("admin/admin-table-comuni");
    	try{
			mav = caricaAdminTable(mav, query, idComune, false);
			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

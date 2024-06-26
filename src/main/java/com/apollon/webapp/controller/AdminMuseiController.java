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
import com.apollon.model.Musei;
import com.apollon.model.Comuni;
import com.apollon.service.ComuniManager;
import com.apollon.service.DistanzeProvinceInfrastruttureManager;
import com.apollon.service.MuseiManager;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminMuseiController extends BaseFormController {
    
	private ComuniManager comuniManager;
	@Autowired
	public void setComuniManager(ComuniManager comuniManager) {
		this.comuniManager = comuniManager;
	}
	
	private MuseiManager museiManager;
	@Autowired
	public void setMuseiManager(MuseiManager museiManager) {
		this.museiManager = museiManager;
	}
	
	private DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager;
	@Autowired
	public void setDistanzeProvinceInfrastruttureManager(DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager) {
		this.distanzeProvinceInfrastruttureManager = distanzeProvinceInfrastruttureManager;
	}

	@RequestMapping(value = "/admin/admin-tableMusei", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("museo") final Musei museiMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-musei");
		final Locale locale = request.getLocale();
		if (validator != null) { // validator is null during testing
    		validator.validate(museiMod, errors);
            if (errors.hasErrors() ) { // don't validate when deleting
            	return mav;
            }
        }
		if (request.getParameter("cancel") != null) {
			
        }
		try {
			if (request.getParameter("modifica") != null) {
				String idComune = request.getParameter("comuni.nomeComune") ;
				if(idComune != null){
					Comuni com = comuniManager.get(Long.parseLong(idComune));
					museiMod.setComuni(com);
				}
				museiMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(museiMod.getNomeMuseo()) );
				Musei museoModificato = museiManager.saveMusei(museiMod);
	        	saveMessage(request, "museo modificato: "+museoModificato.getNomeMuseo() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", museoModificato.getId().toString(), true);
	            return mav;
	            
			}else if (request.getParameter("elimina") != null) {
				distanzeProvinceInfrastruttureManager.removeMusei_AndataArrivo(museiMod.getId());
				museiManager.removeMusei(museiMod.getId());
	        	saveMessage(request, "museo eliminato" /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", null, false);
	            return mav;
	            
	        }else if (request.getParameter("aggiungi") != null) {
				String idComune = request.getParameter("comuni.nomeComune") ;
				if(idComune != null){
					Comuni com = comuniManager.get(Long.parseLong(idComune));
					museiMod.setComuni(com);
				}
				museiMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(museiMod.getNomeMuseo()) );
				Musei museoSalvato = museiManager.saveMusei(museiMod);
	        	saveMessage(request, "museo aggiunto: "+museoSalvato.getNomeMuseo() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", museoSalvato.getId().toString(), true);
	            return mav;
	        }
			
	        mav = caricaAdminTable(mav, "", "", false);
            return mav;

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		dataIntegrViolException.printStackTrace();
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return caricaAdminTable(mav, "", null, false);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, "", null, false);
        }
    }
	

	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idMuseo, boolean modifica) throws Exception{
    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_15;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("museiList", museiManager.getMusei());
        }else{
        	mav.addObject("museiList", museiManager.getMuseiBy_LIKE(query) );
        }
    	if (idMuseo == null || "".equals(idMuseo.trim())) {
    		mav.addObject("museo", new Musei());
    	}else{
    		
			Musei museo = museiManager.get( Long.parseLong(idMuseo) );
			RicercaTransfert_GoogleMaps_Info INFO_museoPORTO = null;
			GMaps_Api GMaps_Api = new GMaps_Api();
			INFO_museoPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(museo.getNomeMuseo(), "it");
			if(INFO_museoPORTO==null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(museo.getNomeMuseo(), "it");
				if(predictions1 != null){
					INFO_museoPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, "it");
				}
			}
			mav.addObject("modifica", true );
			mav.addObject("infoGoogle", INFO_museoPORTO );
			mav.addObject("museo", museo );
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableMusei", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idMuseo") String idMuseo) {
    	ModelAndView mav = new ModelAndView("admin/admin-table-musei");
    	try{
			mav = caricaAdminTable(mav, query, idMuseo, false);
			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

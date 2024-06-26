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
import com.apollon.model.Aeroporti;
import com.apollon.model.Comuni;
import com.apollon.service.AeroportiManager;
import com.apollon.service.ComuniManager;
import com.apollon.service.DistanzeProvinceInfrastruttureManager;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminAeroportiController extends BaseFormController {
    
	private AeroportiManager aeroportiManager;
	@Autowired
	public void setAeroportiManager(AeroportiManager aeroportiManager) {
		this.aeroportiManager = aeroportiManager;
	}
	
	private ComuniManager comuniManager;
	@Autowired
	public void setComuniManager(ComuniManager comuniManager) {
		this.comuniManager = comuniManager;
	}
	
	private DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager;
	@Autowired
	public void setDistanzeProvinceInfrastruttureManager(DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager) {
		this.distanzeProvinceInfrastruttureManager = distanzeProvinceInfrastruttureManager;
	}

	@RequestMapping(value = "/admin/admin-tableAeroporti", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("aeroporto") final Aeroporti aeroportiMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-aeroporti");
		log.debug("admin/admin-tableAeroporti POST");
		final Locale locale = request.getLocale();
		if (validator != null) { // validator is null during testing
    		validator.validate(aeroportiMod, errors);
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
					aeroportiMod.setComuni(com);
				}
				aeroportiMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(aeroportiMod.getNomeAeroporto()) );
				Aeroporti aeroModificato = aeroportiManager.saveAeroporti(aeroportiMod);
	        	saveMessage(request, "aeroporto modificato: "+aeroModificato.getNomeAeroporto() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", aeroModificato.getId().toString(), true);
	            return mav;
	            
			}else if (request.getParameter("elimina") != null) {
				distanzeProvinceInfrastruttureManager.removeAeroporti_AndataArrivo(aeroportiMod.getId());
				aeroportiManager.removeAeroporti(aeroportiMod.getId());
	        	saveMessage(request, "aeroporto eliminato" /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", null, false);
	            return mav;
	            
	        }else if (request.getParameter("aggiungi") != null){
				String idComune = request.getParameter("comuni.nomeComune") ;
				if(idComune != null){
					Comuni com = comuniManager.get(Long.parseLong(idComune));
					aeroportiMod.setComuni(com);
				}
				aeroportiMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(aeroportiMod.getNomeAeroporto()) );
				Aeroporti aeroSalvato = aeroportiManager.saveAeroporti(aeroportiMod);
	        	saveMessage(request, "aeroporto aggiunto: "+aeroSalvato.getNomeAeroporto() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", aeroSalvato.getId().toString(), true);
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
	

	
	private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idAeroporto, boolean modifica) throws Exception{

		final int PageSizeTable = Constants.PAGE_SIZE_TABLE_15;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("aeroportiList", aeroportiManager.getAeroporti());
        }else{
        	mav.addObject("aeroportiList", aeroportiManager.getAeroportiBy_LIKE(query) );
        }
    	if (idAeroporto == null || "".equals(idAeroporto.trim())) {
    		mav.addObject("aeroporto", new Aeroporti());
    	}else{
    		
			Aeroporti aero = aeroportiManager.get( Long.parseLong(idAeroporto) );
			RicercaTransfert_GoogleMaps_Info INFO_AEROPORTO = null;
			GMaps_Api GMaps_Api = new GMaps_Api();
			INFO_AEROPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(aero.getNomeAeroporto(), "it");
			if(INFO_AEROPORTO==null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(aero.getNomeAeroporto(), "it");
				if(predictions1 != null){
					INFO_AEROPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, "it");
				}
			}
			 
			mav.addObject("modifica", true );
			mav.addObject("infoGoogle", INFO_AEROPORTO );
			mav.addObject("aeroporto", aero );
    		
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableAeroporti", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idAeroporto") String idAeroporto) {
    	log.debug("admin/admin-tableAeroporti GET");
    	ModelAndView mav = new ModelAndView("admin/admin-table-aeroporti");
    	try{
			mav = caricaAdminTable(mav, query, idAeroporto, false);
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

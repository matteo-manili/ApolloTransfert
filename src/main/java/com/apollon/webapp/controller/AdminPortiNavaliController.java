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
import com.apollon.model.PortiNavali;
import com.apollon.model.Comuni;
import com.apollon.service.ComuniManager;
import com.apollon.service.DistanzeProvinceInfrastruttureManager;
import com.apollon.service.PortiNavaliManager;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminPortiNavaliController extends BaseFormController {
    
	private ComuniManager comuniManager;
	@Autowired
	public void setComuniManager(ComuniManager comuniManager) {
		this.comuniManager = comuniManager;
	}
	
	private PortiNavaliManager portiNavaliManager;
	@Autowired
	public void setPortiNavaliManager(PortiNavaliManager portiNavaliManager) {
		this.portiNavaliManager = portiNavaliManager;
	}
	
	private DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager;
	@Autowired
	public void setDistanzeProvinceInfrastruttureManager(DistanzeProvinceInfrastruttureManager distanzeProvinceInfrastruttureManager) {
		this.distanzeProvinceInfrastruttureManager = distanzeProvinceInfrastruttureManager;
	}
	
	@RequestMapping(value = "/admin/admin-tablePortiNavali", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("porto") final PortiNavali portoNavaleMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-portiNavali");
		final Locale locale = request.getLocale();
		if (validator != null) { // validator is null during testing
    		validator.validate(portoNavaleMod, errors);
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
					portoNavaleMod.setComuni(com);
				}
				portoNavaleMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(portoNavaleMod.getNomePorto()) );
				PortiNavali portoModificato = portiNavaliManager.savePortiNavali(portoNavaleMod);
	        	saveMessage(request, "porto modificato: "+portoModificato.getNomePorto() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", portoModificato.getId().toString(), true);
	            return mav;
	            
			}else if (request.getParameter("elimina") != null){
				distanzeProvinceInfrastruttureManager.removePortiNavali_AndataArrivo(portoNavaleMod.getId());
				
				portiNavaliManager.removePortiNavali(portoNavaleMod.getId());
	        	saveMessage(request, "porto eliminato" /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", null, false);
	            return mav;
	            
	        }else if (request.getParameter("aggiungi") != null){
				String idComune = request.getParameter("comuni.nomeComune") ;
				if(idComune != null){
					Comuni com = comuniManager.get(Long.parseLong(idComune));
					portoNavaleMod.setComuni(com);
				}
				portoNavaleMod.setUrl( CreaFriendlyUrl_Slugify.creaUrl(portoNavaleMod.getNomePorto()) );
				PortiNavali portoSalvato = portiNavaliManager.savePortiNavali(portoNavaleMod);
	        	saveMessage(request, "porto aggiunto: "+portoSalvato.getNomePorto() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", portoSalvato.getId().toString(), true);
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
	

	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idPortoNavale, boolean modifica) throws Exception{

    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_15;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("portiList", portiNavaliManager.getPortiNavali());
        }else{
        	mav.addObject("portiList", portiNavaliManager.getPortiNavaliBy_LIKE(query) );
        }
    	if (idPortoNavale == null || "".equals(idPortoNavale.trim())) {
    		mav.addObject("porto", new PortiNavali());
    	}else{
    		GMaps_Api GMaps_Api = new GMaps_Api();
			PortiNavali porto = portiNavaliManager.get( Long.parseLong(idPortoNavale) );
			RicercaTransfert_GoogleMaps_Info INFO_AEROPORTO = null;
			INFO_AEROPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(porto.getNomePorto(), "it");
			if(INFO_AEROPORTO==null){
				String predictions1 = GMaps_Api.GoogleMaps_PlaceAutocomplete(porto.getNomePorto(), "it");
				if(predictions1 != null){
					INFO_AEROPORTO = GMaps_Api.GoogleMaps_PlaceTextSearch(predictions1, "it");
				}
			}
			mav.addObject("modifica", true );
			mav.addObject("infoGoogle", INFO_AEROPORTO );
			mav.addObject("porto", porto );
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tablePortiNavali", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idPortoNavale") String idPortoNavale) {
    	ModelAndView mav = new ModelAndView("admin/admin-table-portiNavali");
    	try{
			mav = caricaAdminTable(mav, query, idPortoNavale, false);
			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

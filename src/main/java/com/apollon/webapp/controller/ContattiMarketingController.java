package com.apollon.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.User;
import com.apollon.service.AgenzieViaggioBitManager;
import com.apollon.service.RicercaTransfertManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ContattiMarketingController extends BaseFormController {
    
	private AgenzieViaggioBitManager agenzieViaggioBitManager;
	@Autowired
	public void setAgenzieViaggioBitManager(AgenzieViaggioBitManager agenzieViaggioBitManager) {
		this.agenzieViaggioBitManager = agenzieViaggioBitManager;
	}
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	@RequestMapping(value = "/contatti-marketing", method = RequestMethod.POST)
    protected ModelAndView onTable_POST(@ModelAttribute("agenzieViaggio") final AgenzieViaggioBit agenzieViaggioMod, final HttpServletRequest request, 
    		final HttpServletResponse response) throws Exception {
    	log.debug("contatti-marketing POST");
    	ModelAndView mav = new ModelAndView("contatti-marketing");
		if (request.getParameter("cancel") != null) { }
		try {
			if ( request.isUserInRole(Constants.ADMIN_ROLE) ) {
				if (request.getParameter("modifica") != null) {
					AgenzieViaggioBit agenzieViaggioModificato = agenzieViaggioBitManager.saveAgenzieViaggioBit(agenzieViaggioMod);
		        	saveMessage(request, "Contatto modificato: "+agenzieViaggioModificato.getEmail() );
		        	mav = caricaContattiMarketing(mav, null, agenzieViaggioModificato.getId().toString(), true, request);
		            return mav;
		            
		        }else if (request.getParameter("aggiungi") != null && !agenzieViaggioMod.getEmail().equals("") 
		        		&& !agenzieViaggioMod.getNome().equals("") && !agenzieViaggioMod.getCitta_e_indirizzo().equals("")  ){
		        	AgenzieViaggioBit agenzieViaggioModificato = agenzieViaggioBitManager.saveAgenzieViaggioBit(agenzieViaggioMod);
		        	saveMessage(request, "Contatto aggiunto: "+agenzieViaggioModificato.getEmail());
		        	mav = caricaContattiMarketing(mav, null, agenzieViaggioModificato.getId().toString(), false, request);
		            return mav;
		        
		        }else if (request.getParameter("elimina") != null){
		        	agenzieViaggioBitManager.removeAgenzieViaggioBit(agenzieViaggioMod.getId());
		        	saveMessage(request, "Contatto eliminato");
		        	mav = caricaContattiMarketing(mav, null, null, false, request);
		            return mav;
		        }
			}
	        mav = caricaContattiMarketing(mav, null, null, false, request);
            return mav;

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", request.getLocale()));
    		return caricaContattiMarketing(mav, null, null, false, request);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", request.getLocale()));
            return caricaContattiMarketing(mav, null, null, false, request);
        }
    }
	

	private ModelAndView caricaContattiMarketing(ModelAndView mav, String query, String idAgenzia, boolean modifica, HttpServletRequest request) throws Exception{
		final int PageSizeTable = Constants.PAGE_SIZE_TABLE_30;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	final String agenzieViaggioList = "agenzieViaggioList";
    	if (request.getParameter("ordinamento") != null && request.getParameter("ordinamento").equals("unsubscribe")) {
			mav.addObject(agenzieViaggioList, agenzieViaggioBitManager.getAgenzieViaggioBit_Unsubscribe()); 
    	}else if( query == null || "".equals(query.trim()) ) {
    		mav.addObject(agenzieViaggioList, agenzieViaggioBitManager.getAgenzieViaggioBit_DESC()); 
        }else{
    		mav.addObject(agenzieViaggioList, agenzieViaggioBitManager.getAgenzieViaggioBy_LIKE(query));
        }
    	final String agenzieViaggio = "agenzieViaggio";
    	if (idAgenzia == null || "".equals(idAgenzia.toString().trim())) {
    		mav.addObject(agenzieViaggio, new AgenzieViaggioBit());
    	}else{
    		mav.addObject(agenzieViaggio, agenzieViaggioBitManager.get(Long.parseLong(idAgenzia)));
    		mav.addObject("modifica", true );
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/contatti-marketing", method = RequestMethod.GET)
    protected ModelAndView onTable_GET(final HttpServletRequest request, 
    		@RequestParam(required = false, value = "q") String query, 
    		@RequestParam(required = false, value = "idAgenzia") String idAgenzia) {
    	log.debug("contatti-marketing GET");
    	ModelAndView mav = new ModelAndView("contatti-marketing");
    	try{
    		User user = getUserManager().getUserByUsername(request.getRemoteUser());
    		if( request.isUserInRole(Constants.ADMIN_ROLE) || (request.isUserInRole(Constants.VENDITORE_ROLE) 
    				&& ricercaTransfertManager.getRicercheTransfertVenditore(user.getId()) != null
    				&& ricercaTransfertManager.getRicercheTransfertVenditore(user.getId()).size() > 0) ){
    			mav = caricaContattiMarketing(mav, query, idAgenzia, false, request);
    		}else{
    			saveMessage(request, "Non puoi Visualizzare la lista Contatti prima di Vendere almeno una Corsa");
    		}
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

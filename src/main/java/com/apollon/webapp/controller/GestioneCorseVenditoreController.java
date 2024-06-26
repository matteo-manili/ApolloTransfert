package com.apollon.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.User;
import com.apollon.service.RicercaTransfertManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class GestioneCorseVenditoreController extends BaseFormController {
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	@RequestMapping(value = "/gestione-corse-venditore", method = RequestMethod.POST)
    public ModelAndView onTable_POST(final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("gestione-corse-venditore");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) { }
		try {
			

			return CaricaGestioneCorseVenditore(request, mav);
	        
    	}catch (final Exception exc) {
    		saveError(request, getText("errors.save", locale));
    		exc.printStackTrace();
    		return CaricaGestioneCorseVenditore(request, mav);
        }
    }
	
	
    private ModelAndView CaricaGestioneCorseVenditore(HttpServletRequest request, ModelAndView mav) throws Exception{
    	User user = getUserManager().getUserByUsername(request.getRemoteUser());
		if(request.isUserInRole(Constants.VENDITORE_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
			mav.addObject("ricercheTransfertVenditore", ricercaTransfertManager.getRicercheTransfertVenditore(user.getId()));
		}
    	
    	return mav;
    }
	
    @RequestMapping(value = "/gestione-corse-venditore", method = RequestMethod.GET)
    protected ModelAndView onTable_GET(final HttpServletRequest request) {
    	ModelAndView mav = new ModelAndView("gestione-corse-venditore");
    	try{
    		
			return CaricaGestioneCorseVenditore(request, mav);
			
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.save", request.getLocale()));
    		return mav;
    	}
    }
    

    
    
    
    
} //fine classe

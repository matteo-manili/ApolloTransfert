package com.apollon.webapp.controller;


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.model.User;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
@RequestMapping("/admin/admin-gestioneCorse*")
public class AdminGestioneCorseController extends BaseFormController {


	private ModelAndView caricaFormAutistaHome(ModelAndView mav, User user, Locale locale) throws Exception{
    	return mav;
    }
	
	
    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView showHome(final HttpServletRequest request, final HttpServletResponse response) {
    	log.info("sono in GestioneCorseController.showHome GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("admin/gestione-corse");
    	try{
    		User user = getUserManager().getUserByUsername(request.getRemoteUser());
    		return caricaFormAutistaHome(mav, user, locale);
		
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("admin/gestione-corse");
    	}
    }

    


}

package com.apollon.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.service.RicercaTransfertManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class NccAziendeController extends BaseFormController {

	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
    private ModelAndView CaricaNccAziende(ModelAndView mav, HttpServletRequest request) {
		return mav;
    }
    
    @RequestMapping(value="/{lang}/"+Constants.PAGE_NCC_AZIENDE, method = RequestMethod.GET)
    protected ModelAndView NccAziende_GET(final HttpServletRequest request, @PathVariable("lang") final String language) throws Exception {
    	log.info("sono in NccAziende GET");
    	ModelAndView mav = new ModelAndView( Constants.PAGE_NCC_AZIENDE );
    	try{
    		mav.addObject("recensioniApprovate", ricercaTransfertManager.Recencioni_Approvate(true) );  
    	}catch(Exception exc) {
    		exc.printStackTrace();
    	}
    	return CaricaNccAziende(mav, request);
    }
    

}

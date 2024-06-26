package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.Disponibilita;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.DisponibilitaManager;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloDisponibilita;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class InsertDisponibilitaController extends BaseFormController {
	
	
	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
    
    private DisponibilitaManager disponibilitaManager;
    @Autowired
    public void setDisponibilitaManager(final DisponibilitaManager disponibilitaManager) {
        this.disponibilitaManager = disponibilitaManager;
    }
    
    
	@RequestMapping(value = "/insert-disponibilita", method = RequestMethod.POST)
    public ModelAndView onSubmitDisponibilita( @ModelAttribute("autista") final Autista autistaMod, final HttpServletRequest request, final HttpServletResponse response){
		log.info("sono in onSubmitDisponibilita POST");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("insert-disponibilita");
		if (request.getParameter("cancel") != null) {

        }
		try {
			Autista autistaCorrente = autistaManager.get(autistaMod.getId());
			List<Autoveicolo> autovelicoloList = autoveicoloManager.getAutoveicoloByAutista(autistaCorrente.getId(), false) ;
			for(Autoveicolo autoveicoloITE : autovelicoloList){
				Autoveicolo auto = autoveicoloITE;
				String checkValue = request.getParameter("autoveicoloSospeso_["+ autoveicoloITE.getId() +"]");
				if(checkValue != null){
					auto.setAutoveicoloSospeso(true);
	        	}else{
	        		auto.setAutoveicoloSospeso(false);
	        	}
				autoveicoloManager.saveAutoveicolo(auto);
			}
	        mav = caricaFormAutistaDisponibilita(mav, autistaCorrente, request);
            return mav;

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
            return mav;
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return mav;
        }
    }

	
	
	
	
    private ModelAndView caricaFormAutistaDisponibilita(ModelAndView mav, Autista autistaCorrente, HttpServletRequest request) throws Exception{

    	mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autistaCorrente, 4, request ) );
    	
    	mav.addObject("autista", autistaCorrente);
        
    	List<AutoveicoloDisponibilita> autoveicoloDisponibilita = new ArrayList<AutoveicoloDisponibilita>();
        List<Autoveicolo> autoveicoliList = autoveicoloManager.getAutoveicoloByAutista(autistaCorrente.getId(), false);
		for(Autoveicolo autoveicoliList_ite: autoveicoliList){
			AutoveicoloDisponibilita autoDisponib = new AutoveicoloDisponibilita();
			
			autoDisponib.setAutoveicolo(autoveicoliList_ite);
			Disponibilita disponibilitaAuto = disponibilitaManager.getDisponibilitaByAutoveicolo(autoveicoliList_ite.getId());
			autoDisponib.setDateSelezionateLong( AutoveicoloUtil.getDateSelezionateLong(disponibilitaAuto) );
			autoveicoloDisponibilita.add(autoDisponib);
        }
        
        mav.addObject("autoveicoli", autoveicoloDisponibilita);
        
    	return mav;
    }
	
	
    @RequestMapping(value = "/insert-disponibilita", method = RequestMethod.GET)
    protected ModelAndView insertDisponibilitaGET(final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(required = false, value = "idAutista") String idAutista) {
    	log.info("sono in insertDisponibilita GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("insert-disponibilita");
    	try{
    		if(request.getRemoteUser() != null){
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
    			mav = caricaFormAutistaDisponibilita(mav, autista, request);
				return mav;
    		}else{
    			return new ModelAndView("redirect:/login");
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("insert-disponibilita");
    	}
    }
    
 
    
    

} //fine classe

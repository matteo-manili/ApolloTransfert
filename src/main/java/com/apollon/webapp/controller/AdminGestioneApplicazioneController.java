package com.apollon.webapp.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.model.GestioneApplicazione;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.Sitemap;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.email.EmailMarketing_AgenzieViaggi;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminGestioneApplicazioneController extends BaseFormController {
    
	private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
    
    private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
		
    
	@RequestMapping(value = "/admin/admin-tableGestioneApplicazione", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("gestioneApplicazione") final GestioneApplicazione gestioneAppMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-gestioneApplicazione");
		log.debug("admin/admin-tableGestioneApplicazione POST");
		final Locale locale = request.getLocale();
		if (validator != null) { // validator is null during testing
    		validator.validate(gestioneAppMod, errors);
            if (errors.hasErrors() ) { // don't validate when deleting
            	return mav;
            }
        }
		if (request.getParameter("cancel") != null) { }
		try {
			if (request.getParameter("set-parameter-ip-domain-mondoserver") != null) {
				Boolean esito = ApplicationUtils.SetParameterIpDomainMondoserver_Form_TableGestioneApplicazione();
				saveMessage(request, esito.toString() );
			}
			
			// INVIO EMAIL AGENZIE VIAGGIO FIERA MILANO
			if (request.getParameter("invia-email-agenzie-viaggio-fiera-milano") != null) {
				int TotaleNumeroEmailDaInviare = 30;
				List<String> indirizziEmailInviate = EmailMarketing_AgenzieViaggi.
						inviaEmailMarketing_AgenzieViaggi_FieraMilano(request.getLocale(), velocityEngine, TotaleNumeroEmailDaInviare );
				saveMessage(request, "InvioNumeroEmail: "+indirizziEmailInviate.size() );
				saveMessage(request, indirizziEmailInviate.toString() );
			}
			
			if (request.getParameter("aggiorna-sitemap") != null) {
				String esito = Sitemap.CreaSitemapUrl( request.getServletContext() );
				saveMessage(request, esito );
			}
			
			if (request.getParameter("switchTestProduzione") != null) {
				String esito = GestioneApplicazioneUtil.SwitchTestProduzione( request.getParameter("switchTestProduzione") );
				saveMessage(request, esito );
			}
			
			if (request.getParameter("modifica") != null) {
				GestioneApplicazione gestAppModificato = gestioneApplicazioneManager.saveGestioneApplicazione(gestioneAppMod);
	        	saveMessage(request, "gestioneApplicazione modificato: "+gestAppModificato.getName() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", gestAppModificato.getId().toString(), true);
	            return mav;
	            
			}else if (request.getParameter("elimina") != null){
				gestioneApplicazioneManager.removeGestioneApplicazione(gestioneAppMod.getId());
	        	saveMessage(request, "gestioneApplicazione eliminato" /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", null, false);
	            return mav;
	            
	        }else if (request.getParameter("aggiungi") != null){
				GestioneApplicazione gestAppSalvato = gestioneApplicazioneManager.saveGestioneApplicazione(gestioneAppMod);
	        	saveMessage(request, "gestioneApplicazione aggiunto: "+gestAppSalvato.getName() /*getText("user.saved", locale )*/ );
	        	mav = caricaAdminTable(mav, "", gestAppSalvato.getId().toString(), true);
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
	

	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idGestioneApplicazione, boolean modifica) throws Exception{
    	if( gestioneApplicazioneManager.getName("SWITCH_TEST_PRODUZIONE").getValueNumber() == 0 ){
    		mav.addObject("switchTestProduzione", false);
    	}else{
    		mav.addObject("switchTestProduzione", true);
    	}

    	if (query == null || "".equals(query.trim())) {
			mav.addObject("gestioneApplicazioneList", gestioneApplicazioneManager.getGestioneApplicazione());
        }else{
        	mav.addObject("gestioneApplicazioneList", gestioneApplicazioneManager.getGestioneApplicazioneBy_LIKE(query) );
        }
    	if (idGestioneApplicazione == null || "".equals(idGestioneApplicazione.trim())) {
    		mav.addObject("gestioneApplicazione", new GestioneApplicazione());
    	}else{
			GestioneApplicazione gestApp = gestioneApplicazioneManager.get( Long.parseLong(idGestioneApplicazione) );
			mav.addObject("modifica", true );
			mav.addObject("gestioneApplicazione", gestApp );
    	}
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableGestioneApplicazione", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idGestioneApplicazione") String idGestioneApplicazione) {
    	log.debug("admin/admin-tableGestioneApplicazione GET");
    	ModelAndView mav = new ModelAndView("admin/admin-table-gestioneApplicazione");
    	try{
    		
			System.out.println("query q: "+ query );
    		
			mav = caricaAdminTable(mav, query, idGestioneApplicazione, false);
			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

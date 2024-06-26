package com.apollon.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.VerifyRecaptcha;
import com.apollon.webapp.util.email.InviaEmail;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ContattiController extends BaseFormController {

    private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }

    @RequestMapping(value="/{lang}/contatti", method = RequestMethod.POST)
    public ModelAndView contatti_POST(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("lang") final String language) {
		ModelAndView mav = new ModelAndView("contatti");
		log.info("sono in contatti POST");
		try {
			// RECAPTCHA request param
			String gRecaptchaResponse = request.getParameter(Constants.RECAPTCHA_RESPONSE);
			log.debug("g-recaptcha-response: "+gRecaptchaResponse);
			if( !VerifyRecaptcha.Verify(gRecaptchaResponse) ){
				return CaricaContatti(mav, request, false);
			}
			// memorizzo i campi
			mav.addObject("NomeMittente", request.getParameter("cliente-firstName"));
			mav.addObject("EmailMittente", request.getParameter("cliente-email"));
			mav.addObject("textMessaggio", request.getParameter("text-message"));
			
			mav.addObject("checkPrivacyPolicy", request.getParameter("check-privacy-policy") != null ? true : false );
			
			System.out.println( request.getParameter("check-privacy-policy") );
			
			if(request.getParameter("invia-messaggio") != null){
				String textMessaggio = request.getParameter("text-message");
				if(textMessaggio == null || textMessaggio.equals("")){
					saveMessage(request, "Scrivi Messaggio");
					return CaricaContatti(mav, request, false);
				}
				if(request.getRemoteUser() != null) {
	    			User user = getUserManager().getUserByUsername(request.getRemoteUser());
	    			InviaEmail.InviaEmailContatti(user.getFullName(), user.getEmail(), textMessaggio, request, velocityEngine);
				}else {
	    			String NomeMittente = request.getParameter("cliente-firstName");
	    			String EmailMittente = request.getParameter("cliente-email");
	    			if(NomeMittente == null || NomeMittente.equals("") || EmailMittente == null || EmailMittente.equals("")){
						saveMessage(request, "Inserire Nome ed Email");
						return CaricaContatti(mav, request, false);
					}
	    			EmailValidator validator = EmailValidator.getInstance();
	    			if(!validator.isValid(EmailMittente)){
	    				saveMessage(request, getText("errors.format.email", new Object[] { EmailMittente }, request.getLocale()));
	    				return CaricaContatti(mav, request, false);
	    			}
	    			
	    			if(request.getParameter("check-privacy-policy") == null){
	    			    //checkbox not checked
	    				saveMessage(request, getText("errors.checked", new Object[] { getText("consenso.trattamento.dati", new Object[] { }, request.getLocale()) }, request.getLocale()));
	    				return CaricaContatti(mav, request, false);
	    			}else{
	    			    //checkbox checked
	    			}
	    			
	    			InviaEmail.InviaEmailContatti(NomeMittente, EmailMittente, textMessaggio, request, velocityEngine);
	    		}
				saveMessage(request, getText("messaggio.contatti.inviato", request.getLocale()));
				return CaricaContatti(mav, request, true);
			}
		} catch(Exception exc) {
			exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
		}
		return CaricaContatti(mav, request, false);
    }

	
    private ModelAndView CaricaContatti(ModelAndView mav, HttpServletRequest request, boolean emailInviata) {
    	try{
    		mav.addObject("emailInviata", emailInviata);
    		if(request.getRemoteUser() != null){
    			User user = getUserManager().getUserByUsername(request.getRemoteUser());
    			mav.addObject("userFullName", user.getFullName());
    			mav.addObject("userEmail", user.getEmail());
    			Autista autista = autistaManager.getAutistaByUser( user.getId() );
    			if( autista != null && autista.isAttivo() ){
    				mav.addObject("autista", autista);
    				mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autista, 0, request ) );
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
			saveMessage(request, e.getMessage() );
    	}
		return mav;
    }
	
    
    @RequestMapping(value={"/{lang}/contatti" }, method = RequestMethod.GET)
    protected ModelAndView contatti_GET(final HttpServletRequest request, @PathVariable("lang") final String language) throws Exception {
    	log.info("sono in contatti GET");
    	ModelAndView mav = new ModelAndView("contatti");
    	

    	System.out.println("sasasasa: "+ getText("language.code", new Locale(language))  );
    	
    	try{
    		mav.addObject("messaggioInviato", false);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
    	}
    	return CaricaContatti(mav, request, false);
    }
    

}

package com.apollon.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.service.AgenzieViaggioBitManager;
import com.apollon.webapp.util.VerifyRecaptcha;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class UnsubscribeEmailController extends BaseFormController {
	
	private AgenzieViaggioBitManager agenzieViaggioBitManager;
	@Autowired
	public void setAgenzieViaggioBitManager(AgenzieViaggioBitManager agenzieViaggioBitManager) {
		this.agenzieViaggioBitManager = agenzieViaggioBitManager;
	}
	
	private String ImpostaSottoscrizione(String Email, boolean sottoscrizione, HttpServletRequest request) throws Exception{
		AgenzieViaggioBit agenziaViaggio = agenzieViaggioBitManager.getEmailAgenzieViaggioBit(Email);
		if(agenziaViaggio != null){
			agenziaViaggio.setUnsubscribe( sottoscrizione );
			if(sottoscrizione){
				saveMessage(request, getText("messaggio.unsubscribe.email.cancellata", new Object[] { Email }, request.getLocale()));
			}else{
				saveMessage(request, getText("messaggio.unsubscribe.email.ok", new Object[] { Email }, request.getLocale()));
			}
			agenzieViaggioBitManager.saveAgenzieViaggioBit(agenziaViaggio);
			return agenziaViaggio.getEmail();
		}else{
			saveMessage(request, getText("messaggio.unsubscribe.email.non.presente", new Object[] { Email } ,request.getLocale()));
			return Email;
		}
	}

	@RequestMapping(value = "/unsubscribe*", method = RequestMethod.POST)
	protected ModelAndView UnsubscribeEmailPost(final HttpServletRequest request) {
		log.info("sono in UnsubscribeEmailPost");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("unsubscribe");
		try {
			String Email = request.getParameter("email");
			String gRecaptchaResponse = request.getParameter(Constants.RECAPTCHA_RESPONSE);
			log.debug("g-recaptcha-response: "+gRecaptchaResponse);
			if( !VerifyRecaptcha.Verify(gRecaptchaResponse) ){
				return CaricaFormUnsubscribeEmail(mav, Email, request);
			}
			
			if(request.getParameter("annulla-iscrizione") != null && Email != null && !Email.equals("")){
				System.out.println("annulla-iscrizione");
				Email = ImpostaSottoscrizione(Email, true, request);
				return CaricaFormUnsubscribeEmail(mav, Email, request);
			}
			
			if(request.getParameter("continua-iscrizione") != null && Email != null && !Email.equals("")){
				System.out.println("continua-iscrizione");
				Email = ImpostaSottoscrizione(Email, false, request);
				return CaricaFormUnsubscribeEmail(mav, Email, request);
			}
			
			return CaricaFormUnsubscribeEmail(mav, Email, request);
			
        }catch(Exception exc){
			exc.printStackTrace();
			saveError(request, getText("errors.general", locale));
			return mav;
		}
	}
	
	
	private ModelAndView CaricaFormUnsubscribeEmail(ModelAndView mav, String Email, HttpServletRequest request) throws Exception{
		mav.addObject("email", Email);
		if(Email == null || Email.equals("")){
			saveError(request, getText("errors.general", request.getLocale()));
		}
		return mav;
    }
	
    
    @RequestMapping(value = "/unsubscribe*", method = RequestMethod.GET)
    protected ModelAndView UnsubscribeEmailGet(final HttpServletRequest request, 
    		@RequestParam(value = "email", required = false) final String Email){
    	log.info("sono in UnsubscribeEmailGet");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("unsubscribe");
    	try{
    		return CaricaFormUnsubscribeEmail(mav, Email, request);
    		
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("home");
    	}
    }
    
    
    
}

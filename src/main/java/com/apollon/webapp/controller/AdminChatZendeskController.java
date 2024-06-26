package com.apollon.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminChatZendeskController extends BaseFormController {
    

	@RequestMapping(value = "/admin/admin-chatZendesk", method = RequestMethod.POST)
    public ModelAndView onTable_POST( final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/chat-zendesk");
		log.debug("admin/admin-chatZendesk POST");
		final Locale locale = request.getLocale();
		try {

			return CaricaChatZendesk(mav);

		}catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return CaricaChatZendesk(mav);
        }
    }
	
	private ModelAndView CaricaChatZendesk(ModelAndView mav) throws Exception{
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-chatZendesk", method = RequestMethod.GET)
    protected ModelAndView onTable_GET(final HttpServletRequest request, final HttpServletResponse response) {
    	log.debug("admin/admin-chatZendesk GET");
    	ModelAndView mav = new ModelAndView("admin/chat-zendesk");
    	try{
			return CaricaChatZendesk(mav);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe

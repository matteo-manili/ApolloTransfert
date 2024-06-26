package com.apollon.webapp.rest.api;

import com.apollon.service.AutistaManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.webapp.controller.BaseFormController;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * vedere (REQUEST): https://stackoverflow.com/questions/29313687/trying-to-use-spring-boot-rest-to-read-json-string-from-post 
 * vedere (RESPONSE): https://www.boraji.com/spring-mvc-4-restcontroller-example 
 * 
 *	origins, allowCredentials, allowedHeaders (di @CrossOrigin): forse sono inutili, già le request sono filtrate in com.apollon.webapp.filter.LocaleFilter
 *	@RestController
 *	@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
 *	@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "{Constants.JWT_HEADER_AUTHORIZATION}") 
 */
public class Api_Base extends BaseFormController {
	
    protected AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    protected AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
	

}

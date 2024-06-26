package com.apollon.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
//@ControllerAdvice
public class GlobalExceptionHandler {
	private final Log log = LogFactory.getLog(getClass());
	
    //StandardServletMultipartResolver
    //@ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e) {
    	log.debug("handleError1");
        //redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/uploadStatus";

    }

    //CommonsMultipartResolver
    //@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleError2(MaxUploadSizeExceededException e) {
    	log.debug("handleError2");
        //redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/";

    }

}

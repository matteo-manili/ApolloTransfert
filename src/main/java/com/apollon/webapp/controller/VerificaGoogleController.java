package com.apollon.webapp.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apollon.Constants;



/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Controller
public class VerificaGoogleController extends BaseFormController {
	
	
	@RequestMapping(value = "/googlee29bdebc24b34eb4.html", method = RequestMethod.GET)
	public void getFileGoogle(final HttpServletResponse response) {
		log.debug("/googlee29bdebc24b34eb4.html");
	    try {
	    	String fileUploadDirectory = getServletContext().getRealPath("/");
	    	File googleFile = new File(fileUploadDirectory+"/googlee29bdebc24b34eb4.html");
	        InputStream is = new FileInputStream(googleFile);
	        /*
		    int k = 0;
		    while( (k = is.read()) != -1 ){
		      response.getOutputStream().write(k);
		    }
		    */
		    response.getOutputStream().write( IOUtils.toByteArray(is) );
		    response.setContentType("application/html");
		    response.getOutputStream().close();
		    response.getOutputStream().flush();
	    }catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	}
	
	
	//@RequestMapping(value = "/AAAAAAAA", method = RequestMethod.GET)
    public @ResponseBody String returnGoogleVerify(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
	    return "google-site-verification: googlee29bdebc24b34eb4.html";
	}
	
	
	//@RequestMapping(value = "/AAAAAAAA", method = RequestMethod.GET)
	@SuppressWarnings("unused")
	public void getFile(final HttpServletResponse response) {
	    try {
	    	// get your file as InputStream
	    	File googleFile = new File("googlee29bdebc24b34eb4.html");
	    	// copy it to response's OutputStream
	    	response.setContentType("text/html");
            //IOUtils.copy(is, response.getOutputStream());
            //is.close(); //chiudo lo stram altrimenti i file rimangono appesi
	    	// org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	    	response.flushBuffer();
		    //FileObject aa = 
		    //response.getOutputStream().write(googleFile.get );
		    //response.getOutputStream().close();
		    //response.getOutputStream().flush();
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	}
	
	
	/*
	@RequestMapping(method = RequestMethod.GET)
	public String redirect(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("sono in VerificaGoogleController googlee29bdebc24b34eb4.htm");
		response.setContentType("text/html");
		//return "redirect:/static2/googlee29bdebc24b34eb4.html";
		return "static/googlee29bdebc24b34eb4.html";
	}
    */


}

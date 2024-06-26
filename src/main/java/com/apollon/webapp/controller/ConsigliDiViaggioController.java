package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.util.PropertiesFileUtil;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.bean.Triplet;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ConsigliDiViaggioController extends BaseFormController {

    private ModelAndView CaricaConsigliDiViaggio(ModelAndView mav, HttpServletRequest request) {
		return mav;
    }
    
    @RequestMapping(value = {"/"+Constants.PAGE_CONSIGLI_DI_VIAGGIO+"*", "/"+Constants.PAGE_CONSIGLI_DI_VIAGGIO+"/*"} , method = RequestMethod.GET)
    protected ModelAndView ConsigliDiViaggio_GET(final HttpServletRequest request) throws Exception {
    	log.info("sono in consigliDiViaggio GET");
    	ModelAndView mav = new ModelAndView( Constants.PAGE_CONSIGLI_DI_VIAGGIO );
    	try{
    		//System.out.println( ControllerUtil.getPageURL(request) );
    		String Url = ControllerUtil.getPageURL(request); // prende l'ultima stringa dopo l'ultimo slash
    		if( Url != null && ! Url.contentEquals( Constants.PAGE_CONSIGLI_DI_VIAGGIO ) ) {
    			mav = new ModelAndView("post/"+Url);
    		}else {
    			PropertiesFileUtil mpc = new PropertiesFileUtil();
		        Set<Object> keys = mpc.getAllKeys();
		        List<Triplet<String, String, String>> listPost = new ArrayList<>();
		        for(int conta = 0; conta <= keys.size(); conta++) {
		        	String keyUrl = null; String keyTitle = null; String keySubTitle = null;
		        	for(Object k: keys) {
		        		String key = (String)k;
			            if( key.startsWith( ("consigli.post.url."+conta+".").toString() ) ) {
			                //System.out.println(key);
			                keyUrl = key;
			                break;
			            }
			        }
		        	for(Object k: keys) {
		        		String key = (String)k;
			            if( key.startsWith( ("consigli.post.title."+conta+".").toString() ) ) {
			                //System.out.println(key);
			                keyTitle = key;
			                break;
			            }
			        }
		        	for(Object k: keys) {
		        		String key = (String)k;
			            if( key.startsWith( ("consigli.post.sub.title."+conta+".").toString() ) ) {
			            	//System.out.println(key);
			                keySubTitle = key;
			                break;
			            }
			        }
		        	if(keyUrl != null && keyTitle != null && keySubTitle != null) {
		        		Triplet<String, String, String> elementPost = new Triplet<String, String, String>(keyUrl, keyTitle, keySubTitle);
		        		listPost.add( elementPost );
		        	}
		        }
		        mav.addObject("listPost", listPost);
    		}
    		
    	}catch(Exception exc) {
    		exc.printStackTrace();
    	}
    	return CaricaConsigliDiViaggio(mav, request);
    }
    

}

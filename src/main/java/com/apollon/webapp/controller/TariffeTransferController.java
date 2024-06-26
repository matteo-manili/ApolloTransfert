package com.apollon.webapp.controller;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.google.maps.errors.ApiException;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class TariffeTransferController extends BaseFormController {
	
	private ModelAndView CaricaFormTariffeTransfer(ModelAndView mav, MenuTerrTariffeTransfer menuTerrTariffeTransfer, HttpServletRequest request) throws Exception {
		mav.addObject("menuTerrTariffeTransfer", menuTerrTariffeTransfer);
		mav.addObject("descrizioneCategorieAutoList", AutoveicoloUtil.DammiDescrizioneCategorieAutoList(request.getLocale()));
    	return mav;
    }
    
	 /**
     * Pagina Tariffe Transfer
	 * @throws Exception 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ApiException 
	 * @throws NullPointerException 
     */
	
	@RequestMapping(value="/{lang}/"+Constants.PAGE_TARIFFE_TRANSFER+"*", method = RequestMethod.GET)
    protected ModelAndView TariffeTransferGet(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("lang") final String language) 
    		throws NullPointerException, ApiException, InterruptedException, IOException, Exception {
    	log.info("sono in TransferTariffeGet");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView(Constants.PAGE_TARIFFE_TRANSFER);
    	try{
    		if(request.getRequestURL().toString().contains(Constants.URL_TARIFFE_TRANSFER)) {
    			String Url = ControllerUtil.getPageURL(request).split(Constants.URL_TARIFFE_TRANSFER)[1]; // prendo la parte a destra di Constants.URL_TRANSFER_TARIFFE
    			System.out.println("URL_TRANSFER_TARIFFE: "+Url);
    			return CaricaFormTariffeTransfer(mav, MenuTariffeTransfer
    					.CaricaMenuTerrTariffeTransfer(locale, Url, InfoUserConnectAddressMain.DammiDeviceType(request), null), request);
    		}else {
    			return CaricaFormTariffeTransfer(mav, MenuTariffeTransfer
    					.CaricaMenuTerrTariffeTransfer(locale, null, InfoUserConnectAddressMain.DammiDeviceType(request), null), request);
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView("redirect:/"+Constants.PAGE_TARIFFE_TRANSFER);

    	}
    }
    
    

    
    
    private ModelAndView CaricaFormTariffeAutisti(ModelAndView mav, HttpServletRequest request) throws Exception {
    	GMaps_Api GMaps_Api = new GMaps_Api();
		mav = GMaps_Api.AddAttribute_GoogleApiMap_JS(mav);
    	return mav;
    }
    
    
    /**
     * Pagina Tariffe Autisti
     */
    @RequestMapping(value = "/"+Constants.PAGE_TARIFFE_AUTISTI+"*", method = RequestMethod.GET)
    protected ModelAndView TariffeAutistiGet(final HttpServletRequest request) {
    	log.info("sono in TransferAutistiGet");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView(Constants.PAGE_TARIFFE_AUTISTI);
    	try{

			//String Url = ControllerUtil.getPageURL(request).split(Constants.URL_TARIFFE_TRANSFER)[1]; // prendo la parte a destra di Constants.URL_TRANSFER_TARIFFE
			//System.out.println("URL_TRANSFER_TARIFFE: "+Url);
			//Object obj = regioniManager.dammiMenuTerrTariffeTransfer_LIKE_Url( Url );
			return CaricaFormTariffeAutisti(mav, request);

    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("home");
    	}
    }
    
    
    
} //fine classe

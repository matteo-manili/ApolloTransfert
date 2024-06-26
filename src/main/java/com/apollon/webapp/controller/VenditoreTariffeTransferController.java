package com.apollon.webapp.controller;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.model.User;
import com.apollon.service.RegioniManager;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer;
import com.google.maps.errors.ApiException;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class VenditoreTariffeTransferController extends BaseFormController {
	
	private RegioniManager regioniManager;
	@Autowired
	public void setRegioniManager(RegioniManager regioniManager) {
		this.regioniManager = regioniManager;
	}
	

	private ModelAndView CaricaFormTariffeTransfer(ModelAndView mav, MenuTerrTariffeTransfer menuTerrTariffeTransfer, HttpServletRequest request) {
		mav.addObject("menuTerrTariffeTransfer", menuTerrTariffeTransfer);
		// Descrizione Categorie Auto
		mav.addObject("descrizioneCategorieAutoList", AutoveicoloUtil.DammiDescrizioneCategorieAutoList(request.getLocale()));
    	return mav;
    }
    
	 /**
     * Pagina Tariffe Transfer
     */
    @RequestMapping(value = "/"+Constants.PAGE_VENDITORE_TARIFFE_TRANSFER+"*", method = RequestMethod.GET)
    protected ModelAndView TransferTariffeGet(final HttpServletRequest request){
    	log.info("sono in TransferTariffeGet transferGet");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView(Constants.PAGE_VENDITORE_TARIFFE_TRANSFER);
    	try{
    		User user = getUserManager().getUserByUsername(request.getRemoteUser());
    		if(request.isUserInRole(Constants.VENDITORE_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
    			//System.out.println("request.getRequestURL().toString(): "+request.getRequestURL().toString());
	    		if(request.getRequestURL().toString().contains(Constants.URL_VENDITORE_TARIFFE_TRANSFER)){
	    			String Url = ControllerUtil.getPageURL(request).split(Constants.URL_VENDITORE_TARIFFE_TRANSFER)[1]; // prendo la parte a destra di Constants.URL_TRANSFER_TARIFFE
	    			System.out.println("URL_TRANSFER_TARIFFE: "+Url);
	    			Object obj = regioniManager.dammiMenuTerrTariffeTransfer_LIKE_Url( Url );
	    			return CaricaFormTariffeTransfer(mav, MenuTariffeTransfer.
	    				CaricaMenuTerrTariffeTransfer(locale, Url /*obj*/, InfoUserConnectAddressMain.DammiDeviceType(request), user.getId()), request);
	    		}else{
	    			return CaricaFormTariffeTransfer(mav, MenuTariffeTransfer.
	    					CaricaMenuTerrTariffeTransfer(locale, null, InfoUserConnectAddressMain.DammiDeviceType(request), user.getId()), request);
	    		}
    		}else{
    			return CaricaFormTariffeTransfer(mav, null, request);
    		}
    	}catch(NullPointerException exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("home");
    	}
    }
    
    
    
} //fine classe

package com.apollon.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.controller.homeUtente.HomeUtenteUtil;
import com.apollon.webapp.util.corse.PanelMainCorseCliente;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 */
@Controller
public class VisualizzaCorsaClienteController extends BaseFormController {
    
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
    private ModelAndView caricaFormVisualizzaCorsaCliente(ModelAndView mav, HttpServletRequest request) throws Exception{
    	mav = HomeUtenteUtil.Add_PanelInfoCorseCliente(mav, request);
    	return mav;
    }
	
    /**
     * Il token è formato da username@IdTransfert
     * esempio: www.apollotransfert.com/visualizza-corsa-cliente?token=customer_123@6565
     * esempio: www.ncctransferonline.it/visualizza-corsa-cliente?token=customer_123@6565
     * 
     * ricTransfert
     */
	@RequestMapping(value = "/"+Constants.URL_VISUALZZA_CORSA_CLIENTE, method = RequestMethod.GET)
    protected ModelAndView prenotaCorsaMedioGET(@RequestParam(value = "token", required = true) final String token, final HttpServletRequest request) {
    	try{
    		log.debug(Constants.URL_VISUALZZA_CORSA_CLIENTE+" GET");
    		ModelAndView mav = new ModelAndView( Constants.URL_VISUALZZA_CORSA_CLIENTE );
    		if(token != null && !token.equals("")){
    			String[] parts = token.split("@"); String UserName = parts[0]; String RicTransfertId = parts[1];
    			if( !UserName.equals("") && !RicTransfertId.equals("") ){
    				RicercaTransfert ricTransf = ricercaTransfertManager.get(Long.parseLong(RicTransfertId));
    				if(ricTransf.getUser().getUsername().equals(UserName)){
    					PanelMainCorseCliente panelCliente = new PanelMainCorseCliente(request, Long.parseLong(RicTransfertId));
    					mav.addObject("mainCorseCliente", panelCliente.getMainCorse());
    					mav.addObject("corseDaEseguireCliente", panelCliente.getCorseDaEseguireCliente());
    					mav.addObject("corseEseguiteCliente", panelCliente.getCorseEseguiteCliente());
    					return caricaFormVisualizzaCorsaCliente(mav, request);
    				}
    			}
    		}
    		return new ModelAndView("redirect:/home-user");
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView("redirect:/home-user");
    	}
		
    }
    
    
    
    
    
    
}

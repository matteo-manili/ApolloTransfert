package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.email.InviaEmail;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ListaPreventiviClienteController extends BaseFormController {
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}

	private ModelAndView CaricaListaPreventiviCliente(ModelAndView mav, RicercaTransfert ricercaTransfert, HttpServletRequest request) throws Exception {
		boolean almenoUnPreventivoInviato = false;
		for(RichiestaAutistaParticolare ite: ricercaTransfert.getRichiestaAutistaParticolare()) {
			if(ite.getPreventivo_inviato_cliente() != null && ite.getPreventivo_inviato_cliente()) {
				almenoUnPreventivoInviato = true;
				break;
			}
		}
		mav.addObject("almenoUnPreventivoInviato", almenoUnPreventivoInviato);
		mav.addObject("descrizioneCategorieAutoMap", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()));
        mav.addObject("ricercaTransfert", ricercaTransfert);
        mav.addObject("DammiUrl_InfoCorsa_Cliente", InviaEmail.DammiUrl_InfoCorsa_Cliente(ricercaTransfert, request.getLocale()));
        
        mav.addObject("AMBIENTE_PRODUZIONE", ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request));
     
    	return mav;
    }
    
	
	// esempio: https://localhost:8443/apollon/lista-preventivi-cliente?courseId=9924&tokenRicTransfert=9924EiFUhQpxKBWH2JB
    @RequestMapping(value = "/"+Constants.URL_LISTA_PREVENTIVI_CLIENTE, method = RequestMethod.GET)
    public ModelAndView ListaPreventiviCliente_GET( final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(value = "courseId", required = false) final String courseId,
    		@RequestParam(value = "tokenRicTransfert", required = false) final String tokenRicTransfert) throws Exception {
    	log.info("sono in ListaPreventiviCliente_GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("lista-preventivi-cliente");
    	try{
    		if( courseId != null ) {
    			RicercaTransfert ricTransfert = ricercaTransfertManager.get(Long.parseLong(courseId));
    			if( ricTransfert != null 
    					&& (ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ||  ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO))
    					&& ricTransfert.getRicTransfert_Token() != null && tokenRicTransfert != null && ricTransfert.getRicTransfert_Token().equals(tokenRicTransfert) 
    					&& ricTransfert.getRichiestaPreventivi_Inviata() != null && ricTransfert.getRichiestaPreventivi_Inviata() == true ) {
    				
    				List<RichiestaAutistaParticolare> listRichiestaAutistaParticolare = new ArrayList<RichiestaAutistaParticolare>();
    				listRichiestaAutistaParticolare.addAll(ricTransfert.getRichiestaAutistaParticolare());
    				/*
    				for(RichiestaAutistaParticolare ite: listRichiestaAutistaParticolare) {
    					System.out.println( ite.getId() +" | "+ ite.getAutoveicolo().getAutista().getUser().getFirstName()  );
    				}
    				*/
    				return CaricaListaPreventiviCliente(mav, ricTransfert, request);
    			}
    		} 
    		return CaricaListaPreventiviCliente(mav, new RicercaTransfert(), request);

    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return CaricaListaPreventiviCliente(mav, new RicercaTransfert(), request);
    	}
    }
    
    

    
    
} //fine classe

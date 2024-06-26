package com.apollon.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.service.FattureManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminVediFattureController extends BaseFormController {
    
    private RicercaTransfertManager ricercaTransfertManager;
    @Autowired
    public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
    
    private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
    
    private FattureManager fattureManager;
    @Autowired
    public void setFattureManager(FattureManager fattureManager) {
		this.fattureManager = fattureManager;
	}
    

	@RequestMapping(value = "/admin/admin-vediFatture", method = RequestMethod.POST)
    public ModelAndView onTable_POST(@ModelAttribute("ricercaTransfert") final RicercaTransfert RicercaTransfertMod, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/vedi-fatture");
		log.info("sono in onTable_POST");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) {
			return caricaAdminTable(mav, "", null, request);
        }
		try {
			
			return caricaAdminTable(mav, "", "", request);

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return caricaAdminTable(mav, "", null, request);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, "", null, request);
        }
    }
	
	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idCorsaRimborso, HttpServletRequest request) throws Exception {
    	if (query == null || "".equals(query.trim()) ) {
			mav.addObject("fattureList", fattureManager.getFatture());
        }else if( StringUtils.isNumeric( query ) ){
        	mav.addObject("fattureList", fattureManager.getFatture_By_ProgressivoFattua_IdCorsa(Long.parseLong(query)));
        }else{
        	mav.addObject("fattureList", fattureManager.getFatture());
        }
    	long stripeSWITCH = gestioneApplicazioneManager.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		if(stripeSWITCH == 0l){
			mav.addObject("stripeSWITCH", false);
		}else{
			mav.addObject("stripeSWITCH", true);
		}
		long payPalSWITCH = gestioneApplicazioneManager.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		if(payPalSWITCH == 0l){
			mav.addObject("payPalSWITCH", false);
		}else{
			mav.addObject("payPalSWITCH", true);
		}
    	if(idCorsaRimborso == null || "".equals(idCorsaRimborso.trim())) {
    		mav.addObject("ricercaTransfert", null);
    		
    	}else{
    		RicercaTransfert ricTransfert = ricercaTransfertManager.get(Long.parseLong(idCorsaRimborso));
    		RichiestaMediaAutista richiestaMediaAutista = ricercaTransfertManager.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
	    	if(richiestaMediaAutista != null){
	    		mav.addObject("autistaAssegnatoCorsa", richiestaMediaAutista.getAutista());
	    	}
	    	Fatture fattura = fattureManager.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
	    	if(fattura != null){
	    		mav.addObject("NumeroAvvisiInviatiRimborso", fattura.getNumeroAvvisiInviatiRimborso() );
	    	}
	    	InfoPaymentProvider infoPay = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
	    	mav.addObject("AmountRimborsoByProvider", infoPay.getRefund() );
	    	mav.addObject("AmountByProvider", infoPay.getAmount() );
	    	mav.addObject("NomeClienteByProvider", infoPay.getNomeCliente() );
	    	mav.addObject("AmountByProviderNetto", infoPay.getAmount().subtract(infoPay.getFee()));
	    	mav.addObject("AmountByProviderFee", infoPay.getFee() );
	    	mav = RimborsiUtil.MavAddObject_TypePaymentProvider(ricTransfert, mav);
	    	if( infoPay.getMessaggioErrore() != null ){
				saveMessage(request, infoPay.getMessaggioErrore() );
	    	}
	    	mav.addObject("ricercaTransfert", ricTransfert );
    	}
		return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-vediFatture", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idCorsa") String idCorsaRimborso,
    		final HttpServletRequest request) throws Exception {
    	log.info("sono in onTable_GET");
    	ModelAndView mav = new ModelAndView("admin/vedi-fatture");
    	try{
    		return caricaAdminTable(mav, query, idCorsaRimborso, request);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
    		return caricaAdminTable(mav, query, null, request);
    	}
		
    }
    

    
    
    
} //fine classe

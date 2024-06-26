package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.service.FattureManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminRimborsiController extends BaseFormController {
    
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
    
    private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@RequestMapping(value = "/admin/admin-gestioneRimborsi", method = RequestMethod.POST)
    public ModelAndView onTable_POST(@ModelAttribute("ricercaTransfert") final RicercaTransfert RicercaTransfertMod, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/gestione-rimborsi");
		log.info("sono in onTable_POST");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) {
			return caricaAdminTable(mav, "", null, request);
        }
		try {
			if(request.getParameter("invia-avviso-rimborso-eseguito") != null){
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( RicercaTransfertMod.getId() );
				RimborsiUtil.InviaEmailAvvisoRimborsoConFattura(ricTransfert, request, velocityEngine);
				saveMessage(request, getText("messaggio.invio.avviso.rimborso.email", new Object[] { ricTransfert.getUser().getEmail() }, locale));
				return caricaAdminTable(mav, "", RicercaTransfertMod.getId().toString(), request);
			}
			
			if(request.getParameter("esegui-rimborso") != null){
				if( request.getParameter("valore-rimborso-interi") != null && !request.getParameter("valore-rimborso-interi").equals("") 
					&& request.getParameter("valore-rimborso-decimali") != null && !request.getParameter("valore-rimborso-decimali").equals("") 
						&& StringUtils.isNumeric(request.getParameter("valore-rimborso-interi")) 
							&& StringUtils.isNumeric(request.getParameter("valore-rimborso-decimali"))
								&& request.getParameter("valore-rimborso-decimali").length() == 2){
					String ValoreRimborso = request.getParameter("valore-rimborso-interi") +"."+ request.getParameter("valore-rimborso-decimali");
					RicercaTransfert ricTransfert = ricercaTransfertManager.get( RicercaTransfertMod.getId() );
					String Esito = RimborsiUtil.EseguiRimborso(new BigDecimal(ValoreRimborso), ricTransfert, request);
					saveMessage(request, "Esito Transazione: "+Esito);
				}else{
					saveMessage(request, "inserire valore rimborso corretto esempio 123.22");
				}
				return caricaAdminTable(mav, "", RicercaTransfertMod.getId().toString(), request);
			}
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
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("ricercaTransfertList", ricercaTransfertManager.getRicercaTransfertVenduti(null));
        }else{
        	mav.addObject("ricercaTransfertList", ricercaTransfertManager.getRicercaTransfertVenduti(null));
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
    		
    		if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				AgendaAutistaScelta agendaAutistaScelta = ricTransfert.getAgendaAutistaScelta();
				if(agendaAutistaScelta != null) {
					mav.addObject("agendaAutistaScelta", agendaAutistaScelta); 
		    		mav.addObject("prezzoCorsaCliente", agendaAutistaScelta.getPrezzoTotaleCliente());
				}
			}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
    			RichiestaMediaAutista richiestaMediaScelta = ricTransfert.getRichiestaMediaAutistaCorsaConfermata();
    	    	if(richiestaMediaScelta != null) {
    	    		mav.addObject("autistaAssegnatoCorsa", richiestaMediaScelta.getAutista());
    	    	}
    		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
    			RichiestaAutistaParticolare richiestaAutistaParticolare = ricTransfert.getRichiestaAutistaParticolareAcquistato();
    	    	if(richiestaAutistaParticolare != null) {
    	    		mav.addObject("autistaAssegnatoCorsa", richiestaAutistaParticolare.getAutoveicolo().getAutista());
    	    		mav.addObject("prezzoCorsaCliente", ricTransfert.getRichiestaAutistaParticolareAcquistato().getPrezzoTotaleCliente());
    	    	}
    		}else if(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
    			List<RichiestaAutistaParticolare> richiestaAutistaParticolareList = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
    			if(richiestaAutistaParticolareList != null) {
    				List<Autista> autistaList = new ArrayList<Autista>();
    				BigDecimal prezzoTotaleClienteMultiplo = new BigDecimal(0);
    				for(RichiestaAutistaParticolare ite: richiestaAutistaParticolareList) {
    					autistaList.add( ite.getAutoveicolo().getAutista() );
    					prezzoTotaleClienteMultiplo = prezzoTotaleClienteMultiplo.add(ite.getPrezzoTotaleCliente());
    				}
    	    		mav.addObject("autistaAssegnatoCorsa", autistaList);
    	    		mav.addObject("prezzoCorsaCliente", prezzoTotaleClienteMultiplo);
    	    	}
    		}
	    	Fatture fattura = fattureManager.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
	    	if(fattura != null){
	    		mav.addObject("NumeroAvvisiInviatiRimborso", fattura.getNumeroAvvisiInviatiRimborso() );
	    	}
	    	InfoPaymentProvider infoPay = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
	    	mav.addObject("AmountRimborsoByProvider", infoPay.getRefund() );
	    	mav.addObject("AmountByProvider", infoPay.getAmount() );
	    	mav.addObject("NomeClienteByProvider", infoPay.getNomeCliente() );
	    	mav.addObject("AmountByProviderNetto", infoPay.getAmount() != null ? infoPay.getAmount().subtract(infoPay.getFee()) : null);
	    	mav.addObject("AmountByProviderFee", infoPay.getFee() );
	    	mav = RimborsiUtil.MavAddObject_TypePaymentProvider(ricTransfert, mav);
	    	if( infoPay.getMessaggioErrore() != null ){
				saveMessage(request, infoPay.getMessaggioErrore() );
	    	}
	    	mav.addObject("ricercaTransfert", ricTransfert );
    	}
		return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-gestioneRimborsi", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idCorsa") String idCorsaRimborso,
    		final HttpServletRequest request) throws Exception {
    	log.info("sono in onTable_GET");
    	ModelAndView mav = new ModelAndView("admin/gestione-rimborsi");
    	try{
    		return caricaAdminTable(mav, query, idCorsaRimborso, request);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
    		return caricaAdminTable(mav, query, null, request);
    	}
		
    }
    

    
    
    
} //fine classe

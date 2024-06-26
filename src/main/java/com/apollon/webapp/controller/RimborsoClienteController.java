package com.apollon.webapp.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.apollon.model.User;
import com.apollon.service.FattureManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RichiestaAutistaMedioManager;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;
import com.itextpdf.text.DocumentException;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class RimborsoClienteController extends BaseFormController {
    
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
	
	private RichiestaAutistaMedioManager richiestaAutistaMedioManager;
	@Autowired
	public void setRichiestaAutistaMedioManager(RichiestaAutistaMedioManager richiestaAutistaMedioManager) {
		this.richiestaAutistaMedioManager = richiestaAutistaMedioManager;
	}

	@RequestMapping(value = "/gestioneRimborsoCliente", method = RequestMethod.POST)
    public ModelAndView onTable_POST(@ModelAttribute("ricercaTransfert") final RicercaTransfert RicercaTransfertMod, 
    		final HttpServletRequest request, final HttpServletResponse response) {
		log.info("sono in onTable_POST");
		ModelAndView mav = new ModelAndView("gestione-rimborso-cliente");
		final Locale locale = request.getLocale();
		try {
			if( !CheckCorrectUserCorsa(RicercaTransfertMod.getId(), request) ){
				saveError(request, getText("403.message", locale));
				return new ModelAndView("redirect:/home-user");
			}
			if (request.getParameter("cancel") != null) {
				return new ModelAndView("redirect:/home-user");
	        }
			if(request.getParameter("esegui-rimborso-cliente") != null){
				RicercaTransfert ricTransfert = ricercaTransfertManager.get( RicercaTransfertMod.getId() );
				boolean esito = ControlloDateRicerca.ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(ricTransfert.getDataOraPrelevamentoDate());
				final Integer ORE = (int)(long)gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
				if( esito ){
					InfoPaymentProvider infoPay = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
					BigDecimal rimborso = infoPay.getAmount().subtract(infoPay.getRefund());
					if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {
						String Esito = RimborsiUtil.EseguiRimborso(rimborso, ricTransfert, request);
						if( Boolean.valueOf(Esito) ){
							// Annullo la Corsa e la Rendo non più disponibile agli Autisti
							ricTransfert.setApprovazioneAndata( Constants.NON_APPROVATA );
							ricTransfert.setApprovazioneRitorno( Constants.NON_APPROVATA );
							ricTransfert = ricercaTransfertManager.saveRicercaTransfert(ricTransfert);
								// Invio SMS e EMAIL Avviso eventuale Autista che l'ha presa in carico che la Corsa è Cancellata. 
								RichiestaMediaAutista richiestaMediaAutista = ricercaTransfertManager.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
								if(richiestaMediaAutista != null){
									// Cancello la Conferma dell'Autista che ha Confermato la Corsa
									richiestaMediaAutista.setCorsaConfermata(false);
									richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richiestaMediaAutista);
									RimborsiUtil.Invia_Sms_e_Email_CorsaCancellataMedio(richiestaMediaAutista, request, velocityEngine);
								}
								// Invio Email Avviso Rimborso Corsa con Fattura
								RimborsiUtil.InviaEmailAvvisoRimborsoConFattura(ricTransfert, request, velocityEngine);
								saveMessage(request, "Esito Rimborso: "+Esito);
								saveMessage(request, getText("messaggio.invio.avviso.rimborso.email", new Object[] { ricTransfert.getUser().getEmail() }, locale));
								saveMessage(request, getText("messaggio.cliente.corsa.cancellata", locale));
						}else{
							saveMessage(request, "Esito Transazione: "+Esito.toString());
						}
					}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
						// TODO FARE QUESTO
						saveMessage(request, "Esito Transazione: "+"Rimborso al momento Applicabile solo Manualmente - Contattre ApolloTransfert");
						
					}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
						// TODO FARE QUESTO
						saveMessage(request, "Esito Transazione: "+"Rimborso al momento Applicabile solo Manualmente - Contattre ApolloTransfert");
						
					}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) ) {
						// TODO FARE QUESTO
						saveMessage(request, "Esito Transazione: "+"Rimborso al momento Applicabile solo Manualmente - Contattre ApolloTransfert");
					}
				}else{
					saveMessage(request, getText("messaggio.cliente.disdetta.rimborso.corsa.non.possibile", new Object[] { ORE }, locale));
				}
				return caricaAdminTable(mav, ricTransfert.getId(), request);
			}
        } catch (DocumentException e) {
			e.printStackTrace();
			saveMessage(request, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			saveMessage(request, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, e.getMessage());
		} 
		
		return caricaAdminTable(mav, RicercaTransfertMod.getId(), request);
    }
	
	
    private ModelAndView caricaAdminTable(ModelAndView mav, Long idCorsa, HttpServletRequest request) {
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
		RicercaTransfert ricTransfert = ricercaTransfertManager.get(idCorsa);
		
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
    	try{
			InfoPaymentProvider infoPay = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
	    	mav.addObject("AmountRimborsoByProvider", infoPay.getRefund() );
	    	mav.addObject("AmountByProvider", infoPay.getAmount() );
	    	mav.addObject("NomeClienteByProvider", infoPay.getNomeCliente() );
	    	mav = RimborsiUtil.MavAddObject_TypePaymentProvider(ricTransfert, mav);
	    	if( infoPay.getMessaggioErrore() != null ){
				saveMessage(request, infoPay.getMessaggioErrore() );
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
			saveMessage(request, e.getMessage() );
    	}
    	mav.addObject("ricercaTransfert", ricTransfert );
		return mav;
    }
	
    private boolean CheckCorrectUserCorsa(long idCorsa, HttpServletRequest request){
    	RicercaTransfert ricTransfert = ricercaTransfertManager.get(idCorsa);
    	User user = getUserManager().getUserByUsername(request.getRemoteUser());
    	if((ricTransfert != null && ricTransfert.getUser().getId().longValue() == user.getId().longValue()) 
    			|| (ricTransfert.getUserVenditore() != null && ricTransfert.getUserVenditore().getId().longValue() == user.getId().longValue())
    			|| request.isUserInRole(Constants.ADMIN_ROLE) 
    			){
    		return true;
    	}else{
    		return false;
    	}
    }
	
    @RequestMapping(value = "/gestioneRimborsoCliente", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( @RequestParam(required = true, value = "idCorsa") String idCorsaRimborso, final HttpServletRequest request) 
    		throws Exception {
    	log.info("sono in onTable_GET");
    	ModelAndView mav = new ModelAndView("gestione-rimborso-cliente");
    	try{
    		final Locale locale = request.getLocale();
    		if(idCorsaRimborso != null){
	    		if(  CheckCorrectUserCorsa(Long.parseLong(idCorsaRimborso), request) ){
	    			return caricaAdminTable(mav, Long.parseLong(idCorsaRimborso), request);
	    			
	    		}else{
	    			saveError(request, getText("403.message", locale));
					return new ModelAndView("redirect:/home-user");
	    		}
    		}else{
    			return new ModelAndView("redirect:/home-user");
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
    		return caricaAdminTable(mav, Long.parseLong(idCorsaRimborso), request);
    	}
    }
    

    
    
    
} //fine classe

package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.Fatture;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.Ritardi;
import com.apollon.service.FattureManager;
import com.apollon.service.RitardiManager;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminGestioneRitardiController extends BaseFormController {
    
	private RitardiManager ritardiManager;
    @Autowired
    public void setRitardiManager(RitardiManager ritardiManager) {
		this.ritardiManager = ritardiManager;
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

	
	@RequestMapping(value = "/admin/admin-gestioneRitardi", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("gestioneRitardi") final Ritardi ritardoMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/gestione-ritardi");
		log.info("sono in onTable_POST");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) {
			return caricaAdminTable(mav, "", null);
        }
		try {
			Ritardi ritardo = ritardiManager.get( ritardoMod.getId() );
			if (request.getParameter("invia-sollecito") != null) {
				// INVIO EMAIL SOLLECITO
				InviaEmail.Invia_Email_SollecitoRitardoCliente(ritardo, request, velocityEngine);
				// INVIO SMS
				String linkHomeUser = request.getServerName() + request.getContextPath() + "/home-user"; 
				String testoSms = "Avviso Fattura per Ritardo Prelevamento Passeggero Corsa: "+
						ritardo.getRicercaTransfert().getPartenzaRequest() +" - "+ ritardo.getRicercaTransfert().getArrivoRequest() +" "+ linkHomeUser;
				InvioSms.Crea_SMS_Gateway(ritardo.getRicercaTransfert().getUser().getPhoneNumber(), testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO) ;
				// incremento numero solleciti inviati ritardo
				Fatture fattura = fattureManager.getFatturaBy_IdRitardo( ritardo.getId() );
				fattura.setNumeroSollecitiInviatiRitardo( fattura.getNumeroSollecitiInviatiRitardo() + 1 );
				fattureManager.saveFatture(fattura);
				saveMessage(request, "Inviato Sollecito Pagamento Ritardo Email: "+ritardo.getRicercaTransfert().getUser().getEmail());
				return caricaAdminTable(mav, "", ritardoMod.getId().toString());
			}
			if (request.getParameter("modifica") != null) {
				if(ritardoMod.getNumeroMezzoreRitardiAndata() != null){
					RitardiUtil.GestioneRitardo(ritardoMod.getRicercaTransfert().getId(), RitardiUtil.ANDATA, ritardoMod.getNumeroMezzoreRitardiAndata());
				}
				if(ritardoMod.getNumeroMezzoreRitardiRitorno() != null){
					RitardiUtil.GestioneRitardo(ritardoMod.getRicercaTransfert().getId(), RitardiUtil.RITORNO, ritardoMod.getNumeroMezzoreRitardiRitorno());
				}
	        	saveMessage(request, "Ritardo Aggiornato");
	        	return caricaAdminTable(mav, "", ritardoMod.getId().toString());
			}
			return caricaAdminTable(mav, "", "");

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return caricaAdminTable(mav, "", null);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, "", null);
        }
    }
	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idRitardi) throws Exception{
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("gestioneRitardiList", ritardiManager.getRitardi());
        }else{
        	//mav.addObject("gestioneRitardiList", ritardiManager.getRitardi(query) );
        	mav.addObject("gestioneRitardiList", ritardiManager.getRitardi());
        }
    	if (idRitardi == null || "".equals(idRitardi.trim()) || ritardiManager.get(Long.parseLong(idRitardi)) == null) {
    		mav.addObject("gestioneRitardi", null);
    	}else{
			Ritardi ritardo = ritardiManager.get( Long.parseLong(idRitardi) );
			
			if(ritardo.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				AgendaAutistaScelta agendaAutistaScelta = ritardo.getRicercaTransfert().getAgendaAutistaScelta();
				if(agendaAutistaScelta != null) {
					mav.addObject("agendaAutistaScelta", agendaAutistaScelta); 
		    		mav.addObject("prezzoCorsaCliente", agendaAutistaScelta.getPrezzoTotaleCliente());
				}
			}else if(ritardo.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
				RichiestaMediaAutista richiestaMediaAutista = ritardo.getRicercaTransfert().getRichiestaMediaAutistaCorsaConfermata();
		    	if(richiestaMediaAutista != null){
		    		mav.addObject("autistaAssegnatoCorsa", richiestaMediaAutista.getAutista() );
		    		mav.addObject("prezzoCorsaCliente", ritardo.getRicercaTransfert().getRichiestaMediaScelta().getPrezzoTotaleCliente());
		    	}
			}else if(ritardo.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
				RichiestaAutistaParticolare richiestaAutistaParticolare = ritardo.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato();
		    	if(richiestaAutistaParticolare != null){
		    		mav.addObject("autistaAssegnatoCorsa", richiestaAutistaParticolare.getAutoveicolo().getAutista());
		    		mav.addObject("prezzoCorsaCliente", ritardo.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato().getPrezzoTotaleCliente());
		    	}
			}else if(ritardo.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
				List<RichiestaAutistaParticolare> richiestaAutistaParticolareList = ritardo.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato_Multiplo();
				if(richiestaAutistaParticolareList != null){
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
			mav.addObject("modifica", true );
			mav.addObject("gestioneRitardi", ritardo );
    	}
    	return mav;
    }
	
    @RequestMapping(value = "/admin/admin-gestioneRitardi", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idRitardi") String idRitardi) {
    	log.debug("admin/admin-gestioneRitardi GET");
    	ModelAndView mav = new ModelAndView("admin/gestione-ritardi");
    	try{
			//Autista autistaCorrente = getAutistaManager().getAutistaByUser(user.getId());
			return caricaAdminTable(mav, query, idRitardi);

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

} //fine classe

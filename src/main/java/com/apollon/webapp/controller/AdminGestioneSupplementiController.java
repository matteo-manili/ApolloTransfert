package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
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
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.Supplementi;
import com.apollon.service.FattureManager;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.SupplementiManager;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminGestioneSupplementiController extends BaseFormController {
    
	private SupplementiManager supplementiManager;
    @Autowired
    public void setSupplementiManager(SupplementiManager supplementiManager) {
		this.supplementiManager = supplementiManager;
	}
		
    private RicercaTransfertManager ricercaTransfertManager;
    @Autowired
    public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
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
	
	@RequestMapping(value = "/admin/admin-gestioneSupplementi", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("gestioneSupplementi") final Supplementi supplementoMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/gestione-supplementi");
		log.info("sono in onTable_POST");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) {
			return caricaAdminTable(mav, "", null, false);
        }
		try {
			if (request.getParameter("invia-sollecito") != null) {
				Supplementi supplemento = supplementiManager.get( supplementoMod.getId() );
				// INVIO EMAIL SOLLECITO
				InviaEmail.Invia_Email_SollecitoSupplementoCliente(supplemento, request, velocityEngine);
				// INVIO SMS
				String linkHomeUser = request.getServerName() + request.getContextPath() + "/home-user"; 
				String testoSms = "Avviso Fattura per Supplemento Extra Corsa: "+
						supplemento.getRicercaTransfert().getPartenzaRequest() +" - "+ supplemento.getRicercaTransfert().getArrivoRequest() +" "+ linkHomeUser;
				InvioSms.Crea_SMS_Gateway(supplemento.getRicercaTransfert().getUser().getPhoneNumber(), testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
				// incremento numero solleciti inviati ritardo
				Fatture fattura = fattureManager.getFatturaBy_IdSupplemento( supplemento.getId() );
				fattura.setNumeroSollecitiInviatiRitardo( fattura.getNumeroSollecitiInviatiRitardo() + 1 );
				fattureManager.saveFatture(fattura);
				saveMessage(request, "Inviato Sollecito Pagamento Supplemento Extra Corsa Email: "+supplemento.getRicercaTransfert().getUser().getEmail());
				return caricaAdminTable(mav, "", supplemento.getId().toString(), false);
			}
			if (request.getParameter("modifica") != null) {
				Supplementi supplemento = supplementiManager.get( supplementoMod.getId() );
				if(supplementoMod.getDescrizione() != null) {
					supplemento.setDescrizione(supplementoMod.getDescrizione());
				}
				if(supplementoMod.getPrezzo() != null && !supplementoMod.isPagato()) {
					boolean pagamentoSupplemento = supplementoMod.getPrezzo().compareTo(BigDecimal.ZERO) > 0 ? false : true;
					supplemento.setPagato(pagamentoSupplemento);
					supplemento.setPrezzo( supplementoMod.getPrezzo() );
					supplementiManager.saveSupplementi(supplemento);
		        	saveMessage(request, "Supplemento Aggiornato");
		        	return caricaAdminTable(mav, "", supplementoMod.getId().toString(), false);
				}
			}
			if (request.getParameter("elimina") != null) {
				Supplementi supplemento = supplementiManager.get( supplementoMod.getId() );
				supplemento.setPagato(true);
				supplemento.setDescrizione( "supplemento rimosso | "+supplemento.getDescrizione() );
				supplemento.setPrezzo(new BigDecimal(0));
				supplementiManager.saveSupplementi(supplemento);
				return caricaAdminTable(mav, "", supplemento.getId().toString(), false);
				/* I Supplementi e i Ritardi non devono essere cancellati perché sono collegati alla fattura e la fattura non rimossa ma bensì stornata a 0.00
				RicercaTransfert ricTrans = ricercaTransfertManager.get( supplemento.getRicercaTransfert().getId() );
				JSONObject infoDatiPasseggero = new JSONObject( ricTrans.getInfoPasseggero() );
				infoDatiPasseggero.remove(Constants.Supplemento_Id);
				ricTrans.setInfoPasseggero( infoDatiPasseggero.toString() );
				ricercaTransfertManager.saveRicercaTransfert(ricTrans);
				fattureManager.removeFatturabySupplemento( supplemento.getId() );
				supplementiManager.removeSupplementi(supplemento.getId());
				*/
			}
			if (request.getParameter("aggiungi") != null) {
				RicercaTransfert ricTransf = ricercaTransfertManager.get( Long.parseLong(request.getParameter("ricercaTransfertId")));
				BigDecimal prezzoSupplemento = supplementoMod.getPrezzo() != null ? supplementoMod.getPrezzo() : BigDecimal.ZERO.setScale(2);
				boolean pagamentoSupplemento = prezzoSupplemento.compareTo(BigDecimal.ZERO) > 0 ? false : true;
				Supplementi nuovoSupplemento = new Supplementi(prezzoSupplemento, pagamentoSupplemento, supplementoMod.getDescrizione(), ricTransf);
				nuovoSupplemento = supplementiManager.saveSupplementi(nuovoSupplemento);
	        	saveMessage(request, "Nuovo Supplemento Creato");
	        	// collego il supplemento a RicercaTransfert
	        	JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransf);
	        	List<Supplementi> supplementiList = supplementiManager.getSupplementiBy_IdRicercaTransfert( ricTransf.getId() );
				JSONArray jsArray = new JSONArray();
				for(Supplementi ite: supplementiList) {
					jsArray.put( ite.getId() );
				}
				infoDatiPasseggero.put(Constants.Supplementi_Id, jsArray);
				ricTransf.setInfoPasseggero(infoDatiPasseggero.toString());
				ricercaTransfertManager.saveRicercaTransfert(ricTransf);
	        	// creo la fattura per il ritardo
				Fatture fattura = new Fatture();
				fattura.setProgressivoFattura( fattureManager.dammiNumeroProgressivoFattura() );
				fattura.setSupplementi(nuovoSupplemento);
				fattura.setNumeroSollecitiInviatiRitardo(0);
				fattureManager.saveFatture(fattura);
				saveMessage(request, "Nuova Fattura Creata Supplemento");
	        	return caricaAdminTable(mav, "", nuovoSupplemento.getId().toString(), false);
			}
			return caricaAdminTable(mav, "", "", false);

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		return caricaAdminTable(mav, "", null, false);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, "", null, false);
        }
    }
	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idSupplementi, boolean modifica) throws Exception {
    	if (query == null || "".equals(query.trim())) {
			mav.addObject("gestioneSupplementiList", supplementiManager.getSupplementi());
        }else{
        	//mav.addObject("gestioneSupplementiList", supplementiManager.getSupplementi(query) );
        	mav.addObject("gestioneSupplementiList", supplementiManager.getSupplementi());
        }
    	if (idSupplementi == null || "".equals(idSupplementi.trim()) || supplementiManager.get(Long.parseLong(idSupplementi)) == null) {
    		mav.addObject("gestioneSupplementi", new Supplementi());
    	}else{
			Supplementi supplemento = supplementiManager.get( Long.parseLong(idSupplementi) );
			if(supplemento.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				AgendaAutistaScelta agendaAutistaScelta = supplemento.getRicercaTransfert().getAgendaAutistaScelta();
				if(agendaAutistaScelta != null) {
					mav.addObject("agendaAutistaScelta", agendaAutistaScelta); 
		    		mav.addObject("prezzoCorsaCliente", agendaAutistaScelta.getPrezzoTotaleCliente());
				}
			}else if(supplemento.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) {
				RichiestaMediaAutista richiestaMediaAutista = supplemento.getRicercaTransfert().getRichiestaMediaAutistaCorsaConfermata();
		    	if(richiestaMediaAutista != null) {
		    		mav.addObject("autistaAssegnatoCorsa", richiestaMediaAutista.getAutista());
		    		mav.addObject("prezzoCorsaCliente", supplemento.getRicercaTransfert().getRichiestaMediaScelta().getPrezzoTotaleCliente());
		    	}
			}else if(supplemento.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE)) {
				RichiestaAutistaParticolare richiestaAutistaParticolare = supplemento.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato();
		    	if(richiestaAutistaParticolare != null) {
		    		mav.addObject("autistaAssegnatoCorsa", richiestaAutistaParticolare.getAutoveicolo().getAutista());
		    		mav.addObject("prezzoCorsaCliente", supplemento.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato().getPrezzoTotaleCliente());
		    	}
			}else if(supplemento.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
				List<RichiestaAutistaParticolare> richiestaAutistaParticolareList = supplemento.getRicercaTransfert().getRichiestaAutistaParticolareAcquistato_Multiplo();
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
			mav.addObject("modifica", true );
			mav.addObject("gestioneSupplementi", supplemento);
    	}
    	return mav;
    }
	
    @RequestMapping(value = "/admin/admin-gestioneSupplementi", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( final HttpServletRequest request, @RequestParam(required = false, value = "q") String query, 
    		@RequestParam(required = false, value = "idSupplementi") String idSupplementi) {
    	log.debug("admin/admin-gestioneSupplementi GET");
    	ModelAndView mav = new ModelAndView("admin/gestione-supplementi");
    	try {
			//Autista autistaCorrente = getAutistaManager().getAutistaByUser(user.getId());
    		if (request.getParameter("nuovo-supplemento") != null) {
    			mav.addObject("nuovoSupplemento", true);
    		}
    		
			return caricaAdminTable(mav, query, idSupplementi, false);
    	}catch(Exception exc) {
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

} //fine classe

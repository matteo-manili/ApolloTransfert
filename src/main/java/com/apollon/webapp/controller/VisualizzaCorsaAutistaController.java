package com.apollon.webapp.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autoveicolo;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.service.AgA_TariffariManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RichiestaAutistaMedioManager;
import com.apollon.service.RichiestaAutistaParticolareManager;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.homeUtente.HomeUtenteUtil;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.corse.PanelCorseTemplateUtil;
import com.apollon.webapp.util.corse.PanelMainCorseAutista;
import com.apollon.webapp.util.corse.ValiditaPreventivo;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 */
@Controller
public class VisualizzaCorsaAutistaController extends BaseFormController {
    
	private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(final GestioneApplicazioneManager gestioneApplicazioneManager) {
        this.gestioneApplicazioneManager = gestioneApplicazioneManager;
    }
	
	private RichiestaAutistaMedioManager richiestaAutistaMedioManager;
	@Autowired
	public void setRichiestaAutistaMedioManager(RichiestaAutistaMedioManager richiestaAutistaMedioManager) {
		this.richiestaAutistaMedioManager = richiestaAutistaMedioManager;
	}
	
	private RichiestaAutistaParticolareManager richiestaAutistaParticolareManager;
	@Autowired
	public void setRichiestaAutistaParticolareManager(RichiestaAutistaParticolareManager richiestaAutistaParticolareManager) {
		this.richiestaAutistaParticolareManager = richiestaAutistaParticolareManager;
	}
    
    private AgA_TariffariManager agA_TariffariManager;
    @Autowired
    public void setAgA_TariffariManager(AgA_TariffariManager agA_TariffariManager) {
        this.agA_TariffariManager = agA_TariffariManager;
    }
    
    
    /**
     * 	questi controlli servono a non far prenotare una corsa quando questa corsa è gia stata fatta da tempo
     */
    private ModelAndView caricaFormVerificaSmsAutista(ModelAndView mav, RichiestaMediaAutista richAutistMedio, RichiestaAutistaParticolare richAutistPart, 
    		RicercaTransfert ricTransfert, boolean disdettaCorsaPossibile, boolean corsaScaduta) throws Exception{
    	if( richAutistMedio != null ) {
    		long idAutista = richAutistMedio.getAutista().getUser().getId();
	    	List<Autoveicolo> autoveicoliUtilizzabili = 
	    			HomeUtenteUtil.AutoveicoliUtilizzabiliAutistaMedioList(richAutistMedio.getRichiestaMedia().getRicercaTransfert().getId(), idAutista);
			mav.addObject("autoveicoliUtilizzabili", autoveicoliUtilizzabili );
			mav.addObject("richAutistMedio", richAutistMedio);
    	}
    	if( richAutistPart != null ) {
    		mav.addObject("ValiditaPreventivo", ValiditaPreventivo.DammiJsonTabellaPeriodioValiditaPreventivo_JSONArray(richAutistPart.getRicercaTransfert()) );
    	}
    	if( ricTransfert != null ) {
    		mav.addObject("ricTransfert", ricTransfert);
    	}

    	final Integer ORE = (int) (long) gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
    	mav.addObject("numOreDisdettaPossibile", ORE );
    	final Integer ORE2 = (int) (long) gestioneApplicazioneManager.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_MEDIA").getValueNumber();
    	mav.addObject("numOrePreleavamentoScaduta", ORE2 );
    	mav.addObject("corsaScaduta", corsaScaduta);
    	mav.addObject("disdettaPossibile", disdettaCorsaPossibile);
		mav = RitardiUtil.AddAttribute_Ritardi(mav);
        return mav;
    }
	
    
    // TODO proseguire da qui per Agenda Autista - presentazione all'Autista corsa venduta 

    @RequestMapping(value = "/"+Constants.URL_CORSA_AGENDA_AUTISTA, method = RequestMethod.GET)
    protected ModelAndView agendaAutistaCorsaGET(@RequestParam(value = "idTariffario", required = true) final String idTariffario, final HttpServletRequest request) {
    	try{
    		log.debug("/"+Constants.URL_CORSA_AGENDA_AUTISTA);
    		ModelAndView mav = new ModelAndView("conferma-corsa-autista");
    		final Locale locale = request.getLocale();
    		AgA_Tariffari tariffario = agA_TariffariManager.get( Long.parseLong(idTariffario) );
    		RicercaTransfert ricTransfert = tariffario.getRicercaTransfertAcquistato();
    		
			PanelMainCorseAutista aa = new PanelMainCorseAutista(ricTransfert, tariffario.getAgA_Giornate().getAutoveicolo().getAutista().getId(), locale, request);
			mav.addObject("mainCorse", aa.getMainCorse());
			log.debug("mainCorse: "+aa.getMainCorse());
			
			// LA DATA PRELEVAMENTO (cliente) DEVE ESSERE DI ALMENO UN ORA SUCCESSIVA DALLA DATA DI ADESSO (PER DARE IL TEMPO ALL'UTISTA DI ARRIVARE)
			Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(ricTransfert.getDataOraPrelevamento(), ricTransfert.getOraPrelevamento());
			if(ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(dataPrelevamento, Constants.SERVIZIO_AGENDA_AUTISTA)) {
    			return caricaFormVerificaSmsAutista(mav, null, null, ricTransfert, false, false);
			}else{
				return caricaFormVerificaSmsAutista(mav, null, null, ricTransfert, false, true);
			}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView("redirect:/home-user");
    	}
    }
    
    
    @RequestMapping(value = "/"+Constants.URL_PREVENTIVO_CORSA, method = RequestMethod.GET)
    protected ModelAndView preventivoCorsaParticolareGET(@RequestParam(value = "token", required = true) final String token, final HttpServletRequest request) {
    	try{
    		log.debug("/"+Constants.URL_PREVENTIVO_CORSA);
    		ModelAndView mav = new ModelAndView("conferma-corsa-autista");
    		final Locale locale = request.getLocale();
			PanelMainCorseAutista aa = new PanelMainCorseAutista(token, locale, request);
			mav.addObject("mainCorse", aa.getMainCorse());
    		if(token != null && !token.equals("") && (PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_PARTICOLARE) 
    				|| PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_MULTIPLO)) ) {
				RichiestaAutistaParticolare richAutistPart = richiestaAutistaParticolareManager.getRichiestaAutista_by_token( token );
				// LA DATA PRELEVAMENTO (cliente) DEVE ESSERE DI ALMENO UN ORA SUCCESSIVA DALLA DATA DI ADESSO (PER DARE IL TEMPO ALL'UTISTA DI ARRIVARE)
				Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca(richAutistPart.getRicercaTransfert().getDataOraPrelevamento(), 
						richAutistPart.getRicercaTransfert().getOraPrelevamento());
				if(ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(dataPrelevamento, Constants.SERVIZIO_PARTICOLARE)) {
	                mav.addObject("richAutistPart", richAutistPart);
	    			return caricaFormVerificaSmsAutista(mav, null, richAutistPart, null, true, false);
				}else{
					mav.addObject("richAutistPart", richAutistPart  );
					return caricaFormVerificaSmsAutista(mav, null, richAutistPart, null, true, true);
				}
    		}
    		return new ModelAndView("redirect:/home-user");
    		
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView("redirect:/home-user");
    	}
    }
    
	@RequestMapping(value = "/"+Constants.URL_PRENOTA_CORSA, method = RequestMethod.GET)
    protected ModelAndView prenotaCorsaMedioGET(@RequestParam(value = "token", required = true) final String token, final HttpServletRequest request) {
    	try{
    		log.debug("/"+Constants.URL_PRENOTA_CORSA);
    		ModelAndView mav = new ModelAndView("conferma-corsa-autista");
    		final Locale locale = request.getLocale();
			PanelMainCorseAutista aa = new PanelMainCorseAutista(token, locale, request);
			mav.addObject("mainCorse", aa.getMainCorse());
    		if(token != null && !token.equals("")){
	    		if( PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_STANDARD) ) {
					//ModelAndView mav = new ModelAndView("conferma-corsa-medio");
					RichiestaMediaAutista richAutistMedio = richiestaAutistaMedioManager.getRichiestaAutista_by_token( token );
					if(richAutistMedio == null) {
						return new ModelAndView("redirect:/home-user");
					}
					// LA DATA PRELEVAMENTO (cliente) DEVE ESSERE DI ALMENO UN ORA SUCCESSIVA DALLA DATA DI ADESSO (PER DARE IL TEMPO ALL'UTISTA DI ARRIVARE)
					if( ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(
							richAutistMedio.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate(), Constants.SERVIZIO_STANDARD) ) {
						if(ControlloDateRicerca.ControlloDataPrelevamentoDaAdesso_DisdettaCorsaAutistaMedio(richAutistMedio.getRichiestaMedia().getRicercaTransfert()
								.getDataOraPrelevamentoDate())) {
			    			return caricaFormVerificaSmsAutista(mav, richAutistMedio, null, null, true, false);
						}else{
			    			return caricaFormVerificaSmsAutista(mav, richAutistMedio, null, null, false, false);
						}
					}else{
		    			return caricaFormVerificaSmsAutista(mav, richAutistMedio, null, null, false, true);
					}
	    		}
    		}
    		return new ModelAndView("redirect:/home-user");
    		
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView("redirect:/home-user");
    	}
    }
	

} //fine classe

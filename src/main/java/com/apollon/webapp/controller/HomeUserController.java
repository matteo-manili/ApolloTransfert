package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.TipoRuoli;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutistaZoneManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.RitardiManager;
import com.apollon.service.SupplementiManager;
import com.apollon.service.TipoRuoliManager;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.homeUtente.HomeUtenteUtil;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.corse.PanelMainCorseAutista;
import com.apollon.webapp.util.corse.PanelMainCorseCliente;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class HomeUserController extends BaseFormController {

	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(AutoveicoloManager autoveicoloManager) {
		this.autoveicoloManager = autoveicoloManager;
	}
    
    AutistaZoneManager autistaZoneManager;
    @Autowired
    public void setAutistaZoneManager(AutistaZoneManager autistaZoneManager) {
		this.autistaZoneManager = autistaZoneManager;
	}
    
    RitardiManager ritardiManager;
    @Autowired
    public void setRitardiManager(RitardiManager ritardiManager) {
		this.ritardiManager = ritardiManager;
	}
    
    SupplementiManager supplementiManager;
    @Autowired
    public void setSupplementiManager(SupplementiManager supplementiManager) {
		this.supplementiManager = supplementiManager;
	}

	private TipoRuoliManager tipoRuoliManager;
    @Autowired
    public void setTipoRuoliManager(final TipoRuoliManager tipoRuoliManager) {
        this.tipoRuoliManager = tipoRuoliManager;
    }
	
	private ModelAndView CaricaFormUtenteHome(ModelAndView mav, User user, Locale locale, HttpServletRequest request, boolean registrazioneEseguita) throws Exception {
		mav.addObject("registrazioneEseguita", registrazioneEseguita);
		mav.addObject("user", user);
		mav.addObject("recensioneTransferUser", user.getRecensioneTransfer());
		List<TipoRuoli> tipoRuoliList = new ArrayList<TipoRuoli>();
		tipoRuoliList.addAll( user.getTipoRuoli() );
		mav.addObject("tipoRuoliList", tipoRuoliList);
		Autista autista = autistaManager.getAutistaByUser( user.getId() );
		if( tipoRuoliList.contains(tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA)) && autista != null && autista.isAttivo() ) {
			// ALERT HOME PAGE INFORMAZIONI INSERIMENTO MANCANTI
			if(autista.isAttivo() == true && autista.isBannato() == false){
				Map<String, String> hrefMap = new LinkedHashMap<String, String>();
				int flag = 0;
				if(autoveicoloManager.autoveicoliPresentiNonCancellati(autista.getId()) == false){
					hrefMap.put("insert-autoveicolo", "Inserisci Autoveicolo");
					flag++;
				}
				if(autistaZoneManager.ControllaServiziAttivi(autista.getId()) == false){
					hrefMap.put("insert-tragitti", "Inserisci Zona Lavoro");
					flag++;
				}
				List<String> listaDocumenti = new ArrayList<String>(); // ORIGIALE: docUtil.documentiCompletatiEsclusoContratto == false
				DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
				if(HomeUtenteUtil.ControlloDocumentiInseriti(docUtil)){
					hrefMap.put("insert-documenti", "Inserisci Documenti");
					listaDocumenti = docUtil.DettaglioDocumentiAssenti();
					flag++;
				}
				if( flag > 0 ){
					mav.addObject("mapLinkInfoMancanti", hrefMap);
					mav.addObject("listaDocumenti", listaDocumenti);
				}else{
					mav.addObject("mapLinkInfoMancanti", null);
				}
			}
			mav.addObject("autista", autista);
			mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista(autista, 0, request) );
			PanelMainCorseAutista PanelAutista = new PanelMainCorseAutista(user, autista.getId(), locale, request);
			// TODO FARE VISUALIZZARE ANCHE LE CORSE AGENDA AUTISTA....................
			mav.addObject("mainCorse", PanelAutista.getMainCorse());
			mav.addObject("corseDaEseguire", PanelAutista.getCorseDaEseguire());
			mav.addObject("corseEseguite", PanelAutista.getCorseEseguite());
			mav.addObject("corseDisponibili", PanelAutista.getCorseDisponibili());
		}
		if( tipoRuoliList.contains(tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE)) ) {
			PanelMainCorseCliente PanelCliente = new PanelMainCorseCliente(user.getId(), request);
			mav.addObject("mainCorseCliente", PanelCliente.getMainCorse());
			mav.addObject("corseDaEseguireCliente", PanelCliente.getCorseDaEseguireCliente());
			mav.addObject("corseEseguiteCliente", PanelCliente.getCorseEseguiteCliente());
			mav = HomeUtenteUtil.Add_PanelInfoCorseCliente(mav, request);
			mav.addObject("ritardi", ritardiManager.getRitardiCliente(user.getId()) );
			mav = RitardiUtil.AddAttribute_Ritardi(mav);
			mav.addObject("supplementi", supplementiManager.getSupplementiCliente(user.getId()) );
		}
    	return mav;
    }
	

    @RequestMapping(value="/{lang}/home-user", method = RequestMethod.GET)
    protected ModelAndView showHome(final HttpServletRequest request, final HttpServletResponse response, 
    		@RequestParam(required = false, value = "registrazioneEseguita") final String registrazioneEseguita, @PathVariable("lang") final String language) {
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("home-user");
    	try{
    		Boolean registrEseguita = Boolean.valueOf( registrazioneEseguita );
    		User user = getUserManager().getUserByUsername(request.getRemoteUser());
    		return CaricaFormUtenteHome(mav, user, locale, request, registrEseguita);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("home-user");
    	}
    }

    


}

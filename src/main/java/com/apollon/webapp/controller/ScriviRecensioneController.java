package com.apollon.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.RecensioneTransferUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
@RequestMapping("/scrivi-recensione*")
public class ScriviRecensioneController extends BaseFormController {

	
    private RicercaTransfertManager ricercaTransfertManager;
    @Autowired
    public void setRicercaTransfertManager(final RicercaTransfertManager ricercaTransfertManager) {
        this.ricercaTransfertManager = ricercaTransfertManager;
    }
    
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView scriviRecensione_POST(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView( "scrivi-recensione" );
		log.info("sono in contatti POST");
		try {
			// memorizzo i campi
			String urlTokenRecensione = request.getParameter("url-token-recensione");
			User user = getUserManager().getUser_by_TokenRecensione( urlTokenRecensione );
			if(user != null) {
				if(request.getParameter("salva-recensione") != null){
					List<RicercaTransfert> listaTransfr = user.getRecensioneTransfer().getRicercaTransfertList_Approvati();
					for(RicercaTransfert ite_ric: listaTransfr) {
						if(ite_ric.getRecensioneApprovata() == null || (ite_ric.getRecensioneApprovata() != null && ite_ric.getRecensioneApprovata() == false)) {
							long idTransfer = Long.parseLong( request.getParameter("id-transfer") ) ;
							String StellaPunteggio = request.getParameter("stella-punteggio-"+ite_ric.getId());
							String TextRecensione = request.getParameter("text-recensione-"+ite_ric.getId());
							System.out.println("StellaPunteggio: "+StellaPunteggio);
							System.out.println("TextRecensione: "+TextRecensione);
							TextRecensione = UtilString.TagliaVarChar1000(TextRecensione);
							JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ite_ric);
							infoDatiPasseggero.put(Constants.PunteggioStelleRecensioneJSON, (StellaPunteggio.equals("0") ? 
									Constants.PunteggioStelleRecensioneValoreDefaultJSON : Integer.parseInt(StellaPunteggio)) );
							infoDatiPasseggero.put(Constants.RecensioneJSON, TextRecensione);
							infoDatiPasseggero.put(Constants.RecensioneApprovataJSON, false);
							
							ite_ric.setInfoPasseggero(infoDatiPasseggero.toString());
							ite_ric = ricercaTransfertManager.saveRicercaTransfert( ite_ric );
							System.out.println("sasassa: "+ite_ric.getInfoPasseggero());
						}
					}
					
					//mav.addObject("NomeMittente", request.getParameter("cliente-firstName"));
					saveMessage(request, getText("Recensione Inserita, Un nostro responsabile visionerà la Recensione. Grazie", request.getLocale()));
					return Carica_ScriviRecensione(mav, request, user);
				}
				
				return Carica_ScriviRecensione(mav, request, user);
				
			}else {
				return Carica_ScriviRecensione(mav, request, null);
			}
			
		} catch(Exception exc) {
			exc.printStackTrace();
    		saveMessage(request, exc.getMessage());
		}
		return Carica_ScriviRecensione(mav, request, null);
    }
	
    
	private ModelAndView Carica_ScriviRecensione(ModelAndView mav, HttpServletRequest request, User user) throws Exception{
		mav.addObject("user", user);
		mav.addObject("recensioneTransferUser", user.getRecensioneTransfer());
		return mav;
    }
	
	
    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView scriviRecensione_GET(final HttpServletRequest request, final HttpServletResponse response, 
    		@RequestParam(required = true, value = RecensioneTransferUtil.URL_PAGE_TOKEN_RECENSIONE) String tokenRecensione) {
    	ModelAndView mav = new ModelAndView( "scrivi-recensione" );
    	try{
    		User user = getUserManager().getUser_by_TokenRecensione(tokenRecensione);
    		System.out.println( "sasa: "+user.getWebsite() );
    		return Carica_ScriviRecensione(mav, request, user);
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return new ModelAndView( "scrivi-recensione" );
    	}
    }

    


}

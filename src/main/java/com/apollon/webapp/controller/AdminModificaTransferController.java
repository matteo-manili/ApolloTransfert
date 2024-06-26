package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.service.RichiestaAutistaMedioManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class AdminModificaTransferController extends BaseFormController {
    
    private RicercaTransfertManager ricercaTransfertManager;
    @Autowired
    public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}

    private RichiestaAutistaMedioManager richiestaAutistaMedioManager;
    @Autowired
    public void setRichiestaAutistaMedioManager(RichiestaAutistaMedioManager richiestaAutistaMedioManager) {
		this.richiestaAutistaMedioManager = richiestaAutistaMedioManager;
	}
	
    
    
    @RequestMapping(value = "/admin/admin-modificaTransfer", method = RequestMethod.POST)
    public ModelAndView onTable_POST(@ModelAttribute("ricercaTransfert") final RicercaTransfert ricercaTransfertMod,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
    	ModelAndView mav = new ModelAndView("admin/modifica_transfer");
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) {
			
        }
		try {
			if ( request.isUserInRole(Constants.ADMIN_ROLE) ){
				
				if (request.getParameter("modifica-prezzo-autista") != null) {
					String idRichiestaMediaAutista = request.getParameter("modifica-prezzo-autista");
					String newPrezzoAutista = request.getParameter("compenso_autista_"+idRichiestaMediaAutista);
					RichiestaMediaAutista richMediaAutista = richiestaAutistaMedioManager.get(Long.parseLong( idRichiestaMediaAutista));
					String oldPrezzoAutistaString = richMediaAutista.getPrezzoTotaleAutista().toString();
					if( NumberUtils.isNumber(newPrezzoAutista) && 
							richMediaAutista.getPrezzoTotaleAutista().compareTo(new BigDecimal(newPrezzoAutista)) != 0 ){
						richMediaAutista.setPrezzoTotaleAutista(new BigDecimal(newPrezzoAutista));
						richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richMediaAutista);
						String oldPrezzoAutistaMessage = " | Old Prezzo "+ richMediaAutista.getAutista().getUser().getFullName()+" "+oldPrezzoAutistaString;
	    	    		if( !ricercaTransfertMod.getNote().contains(oldPrezzoAutistaMessage) ){
	    	    			RicercaTransfert ricercaTransfert = ricercaTransfertManager.get( ricercaTransfertMod.getId() );
	    	    			ricercaTransfert.setNote( ricercaTransfertMod.getNote() + oldPrezzoAutistaMessage );
	    	    			ricercaTransfertManager.saveRicercaTransfert(ricercaTransfert);
	    	    		}
					}
		        }
				return CaricaModificaTransfer(mav, ricercaTransfertMod.getId().toString(), null);
			}

			
			return CaricaModificaTransfer(mav, null, null);

		}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("admin/modifica_transfer");
    	}
    }
    
    private ModelAndView CaricaModificaTransfer(ModelAndView mav, String idCorsa, String idRichiestaMediaAutista) throws Exception {
    	if(idCorsa != null){
    		RicercaTransfert ricercaTransfert = ricercaTransfertManager.get( Long.parseLong(idCorsa) );
    		mav.addObject("ricercaTransfert", ricercaTransfert);
    	}
		return mav;
    }
	
    @RequestMapping(value = "/admin/admin-modificaTransfer", method = RequestMethod.GET)
    protected ModelAndView ModificaTransfer(final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(required = true, value = "idCorsa") String idCorsa,
    		@RequestParam(required = false, value = "idRichiestaMediaAutista") String idRichiestaMediaAutista,
    		@RequestParam(required = false, value = "idAutistaPrezzo") String idAutistaPrezzo) {
    	log.info("sono in ModificaTransfer GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("admin/modifica_transfer");
    	try{
    		if ( request.isUserInRole(Constants.ADMIN_ROLE) ){
    			
    			if(idRichiestaMediaAutista != null && idCorsa != null ){ // 8409
    				System.out.println("idRichiestaMediaAutista: "+idRichiestaMediaAutista);
    	    		RicercaTransfert ricercaTransfert = ricercaTransfertManager.get( Long.parseLong(idCorsa) );
    	    		RichiestaMediaAutista richMediaAutista = richiestaAutistaMedioManager.get(Long.parseLong(idRichiestaMediaAutista));
    	    		richMediaAutista.setRichiestaMedia(ricercaTransfert.getRichiestaMediaScelta());
    	    		richMediaAutista.setOrdineAutista(0);
    	    		richiestaAutistaMedioManager.saveRichiestaAutistaMedio(richMediaAutista);
    	    	}
    			
        		return CaricaModificaTransfer(mav, idCorsa, idRichiestaMediaAutista);
    		}

	    	return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("admin/modifica_transfer");
    	}
    }
    

    
    
    
} //fine classe

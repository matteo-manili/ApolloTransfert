package com.apollon.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.ControllerUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminRicercheController extends BaseFormController {
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	

    private ModelAndView caricaAdminTable(ModelAndView mav, HttpServletRequest request, String query, boolean modifica) throws Exception{
    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_15;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
    		int page = ControllerUtil.DammiPageNumberDisplayTable_by_QueryString(request, PageSizeTable);
			mav.addObject("ricercheTransfertList", ricercaTransfertManager.getRicercaTransfertSoloRicercheEseguiteCliente(Constants.PAGE_SIZE_TABLE_15, page));
			mav.addObject("resultSize", ricercaTransfertManager.getCountRicercaTransfertSoloRicercheEseguiteCliente() );
        }else{
        	// TODO FARE ANCHE QUESTO
        	mav.addObject("ricercheTransfertList", ricercaTransfertManager.getRicercaTransfertBy_LIKE(query));
        	mav.addObject("resultSize", ricercaTransfertManager.getCountRicercaTransfertSoloRicercheEseguiteCliente() );
        }
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableRicercheTransfert", method = RequestMethod.GET)
    protected ModelAndView onTable_GET(final HttpServletRequest request, @RequestParam(required = false, value = "q") String query) {
    	ModelAndView mav = new ModelAndView("admin/admin-table-ricercheTransfert");
    	try{
			mav = caricaAdminTable(mav, request, query, false);
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
}

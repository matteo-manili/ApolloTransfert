package com.apollon.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.model.Visitatori;
import com.apollon.service.VisitatoriManager;
import com.apollon.webapp.util.ControllerUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminVisitatoriController extends BaseFormController {
    

	private VisitatoriManager visitatoriManager;
	@Autowired
	public void setVisitatoriManager(VisitatoriManager visitatoriManager) {
		this.visitatoriManager = visitatoriManager;
	}
	


    private ModelAndView caricaAdminTable(ModelAndView mav, HttpServletRequest request, String query, String idVisitatori, boolean modifica) throws Exception{
    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_20;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	if (query == null || "".equals(query.trim())) {
    		int page = ControllerUtil.DammiPageNumberDisplayTable_by_QueryString(request, PageSizeTable); 
    		mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
			mav.addObject("visitatoriList", visitatoriManager.getVisitatoriTable(PageSizeTable, page));
			mav.addObject("resultSize", visitatoriManager.getCountVisitatoriTable());
        }else{
        	mav.addObject("visitatoriList", visitatoriManager.getVisitatoriBy_LIKE(query) );
        	mav.addObject("resultSize", visitatoriManager.getCountVisitatoriTable());
        }
    	// non lo uso, non mi serve modificare un visitatore
    	if (idVisitatori == null || "".equals(idVisitatori.trim())) {
    		mav.addObject("visitatori", new Visitatori());
    	}else{
			Visitatori visitatori = visitatoriManager.get( Long.parseLong(idVisitatori) );
			mav.addObject("visitatori", visitatori );
    	}
        
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableVisitatori", method = RequestMethod.GET)
    protected ModelAndView onTable_GET(final HttpServletRequest request,
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "idVisitatori") String idVisitatori) {
    	ModelAndView mav = new ModelAndView("admin/admin-table-visitatori");
    	try{
			//Autista autistaCorrente = getAutistaManager().getAutistaByUser(user.getId());
			mav = caricaAdminTable(mav, request, query, idVisitatori, false);
			return mav;
			
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
}

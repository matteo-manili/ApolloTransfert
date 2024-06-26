package com.apollon.webapp.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.dao.AutistaZoneDao;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.dao.TipoRuoliDao;
import com.apollon.model.Autista;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.homeUtente.HomeUtenteUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class MenuAutistaAttribute extends ApplicationUtils{

	private static TipoRuoliDao tipoRuoliDao = (TipoRuoliDao) contextDao.getBean("TipoRuoliDao");
	private static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	private static AutistaZoneDao autistaZoneDao = (AutistaZoneDao) contextDao.getBean("AutistaZoneDao");
	
	
	/**
	 * autista corrente, e numero menu selezionato
	 */
	public static Map<String, Object> CaricaMenuAutista(Autista autista, int numeroMenuSelez, HttpServletRequest request) 
			throws Exception{
		Map<String, Object> attributeValue = new HashMap<String, Object>();
		attributeValue.put("menuAutista", autista);
		attributeValue.put("numeroMenu", numeroMenuSelez);
		if(request != null){
			if(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
				attributeValue.put("amministratoreAutisti", true);
			}
		}
		if(autista != null && autista.getUser().getTipoRuoli().contains( tipoRuoliDao.getTipoRuoliByName( Constants.AUTISTA ) )){
			/**
			 *  MENU AUTISTA AUTOVEICOLO
			 */
			boolean autoveicoliPresenti = autoveicoloDao.autoveicoliPresentiNonCancellati(autista.getId());
			if(autoveicoliPresenti){
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_AUTOVEICOLO, true);
        	}else{
        		attributeValue.put(Constants.ATTRIBUTE_AUTISTA_AUTOVEICOLO, false);
        	}
			/**
    		 *  MENU AUTISTA ZONE LAVORO
    		 */
			boolean sontrolloServiziAttivi = autistaZoneDao.ControllaServiziAttivi(autista.getId());
        	if(sontrolloServiziAttivi){
        		attributeValue.put(Constants.ATTRIBUTE_AUTISTA_ZONA_LAVORO, true);
        	}else{
        		attributeValue.put(Constants.ATTRIBUTE_AUTISTA_ZONA_LAVORO, false);
        	}
			/**
			 *  MENU AUTISTA TARIFFE
			 */
			//boolean resultTariffeImpostate = tariffeDao.TariffeImpostate(autista.getId()); // tariffe old
			boolean resultTariffeImpostate = autistaZoneDao.ControllaServiziAttivi(autista.getId());
			if(resultTariffeImpostate){
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_TARIFFE, true);
        	}else{
        		attributeValue.put(Constants.ATTRIBUTE_AUTISTA_TARIFFE, false);
        	}
			/**
			 *  MENU AUTISTA AUTO DISPONIBILITA
			 */
			boolean controllAutoSospesi = autoveicoloDao.ControlloAutoveicoliSospesiMenu(autista.getId());
			if(controllAutoSospesi){
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_AUTO_DISPONIBILITA, true);
        	}else{
        		attributeValue.put(Constants.ATTRIBUTE_AUTISTA_AUTO_DISPONIBILITA, false);
        	}
        	/**
    		 *  DOCUMENTI
    		 */
			DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
			if(HomeUtenteUtil.ControlloDocumentiInseriti(docUtil)){
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_DOCUMENTI, false);
			}else{
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_DOCUMENTI, true);
			}

			
			/*
			if(docUtil.documentiCompletatiInclusoContratto || autista.getAutistaDocumento().isApprovatoGenerale()){
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_DOCUMENTI, true);
			}else{
				attributeValue.put(Constants.ATTRIBUTE_AUTISTA_DOCUMENTI, false);
			}
			*/
		}
		return attributeValue;
	}
	
	
}

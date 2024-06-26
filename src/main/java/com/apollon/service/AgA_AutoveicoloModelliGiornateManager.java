package com.apollon.service;

import java.util.List;

import com.apollon.dao.AgA_AutoveicoloModelliGiornateDao;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_AutoveicoloModelliGiornateManager extends GenericManager<AgA_AutoveicoloModelliGiornate, Long> {
	
	void setAgA_AutoveicoloModelliGiornateDao(AgA_AutoveicoloModelliGiornateDao agA_AutoveicoloModelliGiornateDao);
	
	
	AgA_AutoveicoloModelliGiornate get(Long id);
	
	void removeAgA_AutoveicoloModelliGiornate(long idAgA_AutoveicoloModelliGiornate);
	
	AgA_AutoveicoloModelliGiornate saveAgA_AutoveicoloModelliGiornate(AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) throws Exception;
	
	List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate();
	
	List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(long idAutoveicolo);

	List<AgA_AutoveicoloModelliGiornate> AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(long idAutoveicolo);





}

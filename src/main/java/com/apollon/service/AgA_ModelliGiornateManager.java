package com.apollon.service;

import java.util.List;

import com.apollon.dao.AgA_ModelliGiornateDao;
import com.apollon.model.AgA_ModelliGiornate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_ModelliGiornateManager extends GenericManager<AgA_ModelliGiornate, Long> {
	
	void setAgA_ModelliGiornateDao(AgA_ModelliGiornateDao agA_ModelliGiornateDao);
	
	AgA_ModelliGiornate get(Long id);
	
	AgA_ModelliGiornate saveAgA_ModelliGiornate(AgA_ModelliGiornate agA_ModelliGiornate) throws Exception;
	
	void removeAgA_ModelliGiornate(long idAgA_ModelliGiornate);

	List<AgA_ModelliGiornate> getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata);

	AgA_ModelliGiornate getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(long idAutoveicoloModelGiornata, int orario);


}

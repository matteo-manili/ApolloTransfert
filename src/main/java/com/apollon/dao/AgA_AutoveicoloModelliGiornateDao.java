package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_AutoveicoloModelliGiornateDao extends GenericDao<AgA_AutoveicoloModelliGiornate, Long> {
	
	AgA_AutoveicoloModelliGiornate get(Long id);
	
	List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate();
	
	AgA_AutoveicoloModelliGiornate saveAgA_AutoveicoloModelliGiornate(AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(long idAutoveicolo);

	List<AgA_AutoveicoloModelliGiornate> AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(long idAutoveicolo);












	


	



}

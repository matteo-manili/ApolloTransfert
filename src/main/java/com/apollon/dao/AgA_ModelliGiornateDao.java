package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AgA_ModelliGiornate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_ModelliGiornateDao extends GenericDao<AgA_ModelliGiornate, Long> {
	
	AgA_ModelliGiornate get(Long id);
	
	AgA_ModelliGiornate saveAgA_ModelliGiornate(AgA_ModelliGiornate agA_ModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AgA_ModelliGiornate> getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata);

	AgA_ModelliGiornate getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(long idAutoveicoloModelGiornata, int orario);









	


	



}

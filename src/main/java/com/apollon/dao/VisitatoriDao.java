package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Visitatori;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface VisitatoriDao extends GenericDao<Visitatori, Long> {
	
	Visitatori get(Long id);
	
	List<Visitatori> getVisitatori();
	
	Visitatori saveVisitatori(Visitatori visitatori) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Visitatori> getVisitatoriBy_LIKE(String term);

	List<Visitatori> getVisitatoriTable(int maxResults, Integer firstResult);

	int getCountVisitatoriTable();










	


	



}

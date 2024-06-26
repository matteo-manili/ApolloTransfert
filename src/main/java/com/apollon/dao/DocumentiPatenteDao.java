package com.apollon.dao;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.DocumentiPatente;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiPatenteDao extends GenericDao<DocumentiPatente, Long> {
	
	DocumentiPatente get(Long id);
	
	DocumentiPatente saveDocumentiPatente(DocumentiPatente documentiPatente) throws DataIntegrityViolationException, HibernateJdbcException;



	



	










	


	



}

package com.apollon.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.DocumentiCap;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiCapDao extends GenericDao<DocumentiCap, Long> {
	
	DocumentiCap get(Long id);
	
	DocumentiCap saveDocumentiCap(DocumentiCap documentiCap) throws DataIntegrityViolationException, HibernateJdbcException;



	



	










	


	



}

package com.apollon.dao;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.DocumentiCartaIdentita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiCartaIdentitaDao extends GenericDao<DocumentiCartaIdentita, Long> {
	
	DocumentiCartaIdentita get(Long id);
	
	DocumentiCartaIdentita saveDocumentiCartaIdentita(DocumentiCartaIdentita documentiCartaIdentita) throws DataIntegrityViolationException, HibernateJdbcException;



	



	










	


	



}

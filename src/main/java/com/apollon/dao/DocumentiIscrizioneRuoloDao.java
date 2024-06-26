package com.apollon.dao;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.DocumentiIscrizioneRuolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiIscrizioneRuoloDao extends GenericDao<DocumentiIscrizioneRuolo, Long> {
	
	DocumentiIscrizioneRuolo get(Long id);
	
	DocumentiIscrizioneRuolo saveDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) throws DataIntegrityViolationException, HibernateJdbcException;



	



	










	


	



}

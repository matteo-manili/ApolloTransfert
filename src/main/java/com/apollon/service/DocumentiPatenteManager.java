package com.apollon.service;


import com.apollon.dao.DocumentiPatenteDao;
import com.apollon.model.DocumentiPatente;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiPatenteManager extends GenericManager<DocumentiPatente, Long> {
	
	void setDocumentiPatenteDao(DocumentiPatenteDao documentiPatenteDao);
	
	
	DocumentiPatente get(Long id);
	
	DocumentiPatente saveDocumentiPatente(DocumentiPatente documentiPatente) throws Exception;

	void removeDocumentiPatente(long idDocumentiPatente);


	


	
	












	
	

	

	


	

}

package com.apollon.service;


import com.apollon.dao.DocumentiCapDao;
import com.apollon.model.DocumentiCap;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiCapManager extends GenericManager<DocumentiCap, Long> {
	
	void setDocumentiCapDao(DocumentiCapDao documentiCapDao);
	
	
	DocumentiCap get(Long id);
	
	DocumentiCap saveDocumentiCap(DocumentiCap documentiCap) throws Exception;

	void removeDocumentiCap(long idDocumentiCap);
	


	
	












	
	

	

	


	

}

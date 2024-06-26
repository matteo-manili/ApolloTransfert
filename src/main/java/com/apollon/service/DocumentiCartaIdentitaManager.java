package com.apollon.service;


import com.apollon.dao.DocumentiCartaIdentitaDao;
import com.apollon.model.DocumentiCartaIdentita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiCartaIdentitaManager extends GenericManager<DocumentiCartaIdentita, Long> {
	
	void setDocumentiCartaIdentitaDao(DocumentiCartaIdentitaDao documentiCartaIdentitaDao);
	
	
	DocumentiCartaIdentita get(Long id);
	
	DocumentiCartaIdentita saveDocumentiCartaIdentita(DocumentiCartaIdentita documentiCartaIdentita) throws Exception;


	void removeDocumentiCartaIdentita(long idDocumentiCartaIdentita);


	


	
	












	
	

	

	


	

}

package com.apollon.service;


import com.apollon.dao.DocumentiIscrizioneRuoloDao;
import com.apollon.model.DocumentiIscrizioneRuolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DocumentiIscrizioneRuoloManager extends GenericManager<DocumentiIscrizioneRuolo, Long> {
	
	void setDocumentiIscrizioneRuoloDao(DocumentiIscrizioneRuoloDao documentiIscrizioneRuoloDao);
	
	
	DocumentiIscrizioneRuolo get(Long id);
	
	DocumentiIscrizioneRuolo saveDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) throws Exception;


	void removeDocumentiIscrizioneRuolo(long idDocumentiIscrizioneRuolo);


	


	
	












	
	

	

	


	

}

package com.apollon.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.DocumentiIscrizioneRuoloDao;
import com.apollon.model.DocumentiIscrizioneRuolo;
import com.apollon.service.DocumentiIscrizioneRuoloManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DocumentiIscrizioneRuoloManager")
public class DocumentiIscrizioneRuoloManagerImpl extends GenericManagerImpl<DocumentiIscrizioneRuolo, Long> implements DocumentiIscrizioneRuoloManager {

	private DocumentiIscrizioneRuoloDao documentiIscrizioneRuoloDao;
	
	@Override
    @Autowired
	public void setDocumentiIscrizioneRuoloDao(DocumentiIscrizioneRuoloDao documentiIscrizioneRuoloDao) {
		this.documentiIscrizioneRuoloDao = documentiIscrizioneRuoloDao;
	}

	
	@Override
	public DocumentiIscrizioneRuolo get(Long id) {
		return this.documentiIscrizioneRuoloDao.get(id);
	}
	

	@Override
	public DocumentiIscrizioneRuolo saveDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) throws DataIntegrityViolationException, HibernateJdbcException {
		return documentiIscrizioneRuoloDao.saveDocumentiIscrizioneRuolo(documentiIscrizioneRuolo);
	}


	@Override
	public void removeDocumentiIscrizioneRuolo(long idDocumentiIscrizioneRuolo) {
		documentiIscrizioneRuoloDao.remove(idDocumentiIscrizioneRuolo);
	}

	
	
	
	
}

package com.apollon.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.DocumentiCartaIdentitaDao;
import com.apollon.model.DocumentiCartaIdentita;
import com.apollon.service.DocumentiCartaIdentitaManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DocumentiCartaIdentitaManager")
public class DocumentiCartaIdentitaManagerImpl extends GenericManagerImpl<DocumentiCartaIdentita, Long> implements DocumentiCartaIdentitaManager {

	private DocumentiCartaIdentitaDao documentiCartaIdentitaDao;
	
	@Override
    @Autowired
	public void setDocumentiCartaIdentitaDao(DocumentiCartaIdentitaDao documentiCartaIdentitaDao) {
		this.documentiCartaIdentitaDao = documentiCartaIdentitaDao;
	}


	@Override
	public DocumentiCartaIdentita get(Long id) {
		return this.documentiCartaIdentitaDao.get(id);
	}
	
	@Override
	public DocumentiCartaIdentita saveDocumentiCartaIdentita(DocumentiCartaIdentita documentiCartaIdentita) throws DataIntegrityViolationException, HibernateJdbcException {
		return documentiCartaIdentitaDao.saveDocumentiCartaIdentita(documentiCartaIdentita);
	}


	@Override
	public void removeDocumentiCartaIdentita(long idDocumentiCartaIdentita) {
		documentiCartaIdentitaDao.remove(idDocumentiCartaIdentita);
	}


	
	
	
	
}

package com.apollon.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.DocumentiCapDao;
import com.apollon.model.DocumentiCap;
import com.apollon.service.DocumentiCapManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DocumentiCapManager")
public class DocumentiCapManagerImpl extends GenericManagerImpl<DocumentiCap, Long> implements DocumentiCapManager {

	private DocumentiCapDao documentiCapDao;
	
	@Override
    @Autowired
	public void setDocumentiCapDao(DocumentiCapDao documentiCapDao) {
		this.documentiCapDao = documentiCapDao;
	}

	
	@Override
	public DocumentiCap get(Long id) {
		return this.documentiCapDao.get(id);
	}
	

	@Override
	public DocumentiCap saveDocumentiCap(DocumentiCap documentiCap) throws DataIntegrityViolationException, HibernateJdbcException {
		return documentiCapDao.saveDocumentiCap(documentiCap);
	}

	
	@Override
	public void removeDocumentiCap(long idDocumentiCap) {
		documentiCapDao.remove(idDocumentiCap);
	}





	
	
	
	
}

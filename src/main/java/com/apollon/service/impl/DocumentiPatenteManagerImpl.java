package com.apollon.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.DocumentiPatenteDao;
import com.apollon.model.DocumentiPatente;
import com.apollon.service.DocumentiPatenteManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DocumentiPatenteManager")
public class DocumentiPatenteManagerImpl extends GenericManagerImpl<DocumentiPatente, Long> implements DocumentiPatenteManager {

	private DocumentiPatenteDao documentiPatenteDao;
	
	@Override
    @Autowired
	public void setDocumentiPatenteDao(DocumentiPatenteDao documentiPatenteDao) {
		this.documentiPatenteDao = documentiPatenteDao;
	}

	
	@Override
	public DocumentiPatente get(Long id) {
		return this.documentiPatenteDao.get(id);
	}
	
	
	@Override
	public DocumentiPatente saveDocumentiPatente(DocumentiPatente documentiPatente) throws DataIntegrityViolationException, HibernateJdbcException {
		return documentiPatenteDao.saveDocumentiPatente(documentiPatente);
	}



	@Override
	public void removeDocumentiPatente(long idDocumentiPatente) {
		documentiPatenteDao.remove(idDocumentiPatente);
	}



}

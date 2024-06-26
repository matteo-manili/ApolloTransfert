package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.VisitatoriDao;
import com.apollon.model.Visitatori;
import com.apollon.service.VisitatoriManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("VisitatoriManager")
public class VisitatoriManagerImpl extends GenericManagerImpl<Visitatori, Long> implements VisitatoriManager {

	private VisitatoriDao visitatoriDao;
	
	@Override
    @Autowired
	public void setVisitatoriDao(VisitatoriDao visitatoriDao) {
		this.visitatoriDao = visitatoriDao;
	}

	
	
	@Override
	public Visitatori get(Long id) {
		return this.visitatoriDao.get(id);
	}
	

	@Override
	public List<Visitatori> getVisitatori() {
		return visitatoriDao.getVisitatori();
	}
	
	
	@Override
	public List<Visitatori> getVisitatoriTable(int maxResults, Integer firstResult) {
		return visitatoriDao.getVisitatoriTable(maxResults, firstResult);
	}

	
	@Override
	public int getCountVisitatoriTable() {
		return visitatoriDao.getCountVisitatoriTable();
	}

	
	@Override
	public List<Visitatori> getVisitatoriBy_LIKE(String term){
		return visitatoriDao.getVisitatoriBy_LIKE(term);
	}


	@Override
	public Visitatori saveVisitatori(Visitatori visitatori) throws DataIntegrityViolationException, HibernateJdbcException {
		return visitatoriDao.saveVisitatori(visitatori);
	}


	@Override
    public void removeVisitatori(long id) {
		visitatoriDao.remove(id);
    }



	
	
	
	
}

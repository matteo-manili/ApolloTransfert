package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.SupplementiDao;
import com.apollon.model.Supplementi;
import com.apollon.service.SupplementiManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("SupplementiManager")
public class SupplementiManagerImpl extends GenericManagerImpl<Supplementi, Long> implements SupplementiManager {

	private SupplementiDao supplementiDao;
	
	@Override
    @Autowired
	public void setSupplementiDao(SupplementiDao supplementiDao) {
		this.supplementiDao = supplementiDao;
	}

	
	
	@Override
	public Supplementi get(Long id) {
		return this.supplementiDao.get(id);
	}
	

	@Override
	public List<Supplementi> getSupplementi() {
		return supplementiDao.getSupplementi();
	}
	
	
	@Override
	public List<Supplementi> getSupplementiBy_IdRicercaTransfert(long idCorsa) {
		return supplementiDao.getSupplementiBy_IdRicercaTransfert(idCorsa);
	}
	
	
	@Override
	public List<Supplementi> getSupplementiCliente(long idUser) {
		return supplementiDao.getSupplementiCliente(idUser);
	}
	

	@Override
	public Supplementi saveSupplementi(Supplementi supplementi) throws DataIntegrityViolationException, HibernateJdbcException {
		return supplementiDao.saveSupplementi(supplementi);
	}


	@Override
    public void removeSupplementi(long id) {
		supplementiDao.remove(id);
    }



	
	
	
	
}

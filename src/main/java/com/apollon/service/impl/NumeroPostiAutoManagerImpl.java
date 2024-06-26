package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.NumeroPostiAutoDao;
import com.apollon.model.NumeroPostiAuto;
import com.apollon.service.NumeroPostiAutoManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("NumeroPostiAutoManager")
public class NumeroPostiAutoManagerImpl extends GenericManagerImpl<NumeroPostiAuto, Long> implements NumeroPostiAutoManager {

	private NumeroPostiAutoDao numeroPostiAutoDao;
	
	@Override
    @Autowired
	public void setNumeroPostiAutoDao(NumeroPostiAutoDao numeroPostiAutoDao) {
		this.numeroPostiAutoDao = numeroPostiAutoDao;
	}

	
	
	@Override
	public NumeroPostiAuto get(Long id) {
		return this.numeroPostiAutoDao.get(id);
	}
	

	@Override
	public List<NumeroPostiAuto> getNumeroPostiAuto() {
		return numeroPostiAutoDao.getNumeroPostiAuto();
	}
	
	
	
	@Override
	public NumeroPostiAuto saveNumeroPostiAuto(NumeroPostiAuto numeroPostiAuto) throws Exception {
		return numeroPostiAutoDao.saveNumeroPostiAuto(numeroPostiAuto);
	}

	
	@Override
    public void removeNumeroPostiAuto(long id) {
		numeroPostiAutoDao.remove(id);
    }
	
	
	
}

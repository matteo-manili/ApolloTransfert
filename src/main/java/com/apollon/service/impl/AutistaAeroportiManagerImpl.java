package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaAeroportiDao;
import com.apollon.model.AutistaAeroporti;
import com.apollon.service.AutistaAeroportiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaAeroportiManager")
public class AutistaAeroportiManagerImpl extends GenericManagerImpl<AutistaAeroporti, Long> implements AutistaAeroportiManager {

	private AutistaAeroportiDao autistaAeroportiDao;
	
	@Override
    @Autowired
	public void setAutistaAeroportiDao(AutistaAeroportiDao autistaAeroportiDao) {
		this.autistaAeroportiDao = autistaAeroportiDao;
	}

	
	
	@Override
	public AutistaAeroporti get(Long id) {
		return this.autistaAeroportiDao.get(id);
	}
	
	
	@Override
	public List<AutistaAeroporti> getAutistaAeroporti() {
		return autistaAeroportiDao.getAutistaAeroporti();
	}
	
	@Override
	public List<AutistaAeroporti> getAutistaAeroportiByIdAutista(Long idAutista){
		return autistaAeroportiDao.getAutistaAeroportiByIdAutista(idAutista);
	}
	
	
	@Override
	public AutistaAeroporti saveAutistaAeroporti(AutistaAeroporti autistaAeroporti) throws Exception {
		return autistaAeroportiDao.saveAutistaAeroporti(autistaAeroporti);
	}

	
	@Override
    public void removeAutistaAeroportiByIdAutista(long idAutista) {
		autistaAeroportiDao.removeAutistaAeroportiByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeAutistaAeroporti(long id) {
		autistaAeroportiDao.remove(id);
    }
	
	
	
}

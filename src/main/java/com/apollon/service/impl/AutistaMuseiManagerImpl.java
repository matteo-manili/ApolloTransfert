package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaMuseiDao;
import com.apollon.model.AutistaMusei;
import com.apollon.service.AutistaMuseiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaMuseiManager")
public class AutistaMuseiManagerImpl extends GenericManagerImpl<AutistaMusei, Long> implements AutistaMuseiManager {

	private AutistaMuseiDao autistaMuseiDao;
	
	@Override
    @Autowired
	public void setAutistaMuseiDao(AutistaMuseiDao autistaMuseiDao) {
		this.autistaMuseiDao = autistaMuseiDao;
	}

	
	
	@Override
	public AutistaMusei get(Long id) {
		return this.autistaMuseiDao.get(id);
	}
	
	
	@Override
	public List<AutistaMusei> getAutistaMusei() {
		return autistaMuseiDao.getAutistaMusei();
	}
	
	@Override
	public List<AutistaMusei> getAutistaMuseiByIdAutista(Long idAutista){
		return autistaMuseiDao.getAutistaMuseiByIdAutista(idAutista);
	}
	
	
	@Override
	public AutistaMusei saveAutistaMusei(AutistaMusei autistaMusei) throws Exception {
		return autistaMuseiDao.saveAutistaMusei(autistaMusei);
	}

	
	@Override
    public void removeAutistaMuseiByIdAutista(long idAutista) {
		autistaMuseiDao.removeAutistaMuseiByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeAutistaMusei(long id) {
		autistaMuseiDao.remove(id);
    }
	
	
	
}

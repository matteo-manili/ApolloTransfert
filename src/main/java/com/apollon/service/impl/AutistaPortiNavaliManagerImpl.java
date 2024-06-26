package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaPortiNavaliDao;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.service.AutistaPortiNavaliManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaPortiNavaliManager")
public class AutistaPortiNavaliManagerImpl extends GenericManagerImpl<AutistaPortiNavali, Long> implements AutistaPortiNavaliManager {

	private AutistaPortiNavaliDao autistaPortiNavaliDao;
	
	@Override
    @Autowired
	public void setAutistaPortiNavaliDao(AutistaPortiNavaliDao autistaPortiNavaliDao) {
		this.autistaPortiNavaliDao = autistaPortiNavaliDao;
	}

	
	
	@Override
	public AutistaPortiNavali get(Long id) {
		return this.autistaPortiNavaliDao.get(id);
	}
	
	
	@Override
	public List<AutistaPortiNavali> getAutistaPortiNavali() {
		return autistaPortiNavaliDao.getAutistaPortiNavali();
	}
	
	@Override
	public List<AutistaPortiNavali> getAutistaPortiNavaliByIdAutista(Long idAutista){
		return autistaPortiNavaliDao.getAutistaPortiNavaliByIdAutista(idAutista);
	}
	
	
	@Override
	public AutistaPortiNavali saveAutistaPortiNavali(AutistaPortiNavali autistaPortiNavali) throws Exception {
		return autistaPortiNavaliDao.saveAutistaPortiNavali(autistaPortiNavali);
	}

	
	@Override
    public void removeAutistaPortiNavaliByIdAutista(long idAutista) {
		autistaPortiNavaliDao.removeAutistaPortiNavaliByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeAutistaPortiNavali(long id) {
		autistaPortiNavaliDao.remove(id);
    }
	
	
	
}

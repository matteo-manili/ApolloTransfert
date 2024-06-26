package com.apollon.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apollon.dao.PortiNavaliDao;
import com.apollon.model.PortiNavali;
import com.apollon.service.PortiNavaliManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("PortiNavaliManager")
public class PortiNavaliManagerImpl extends GenericManagerImpl<PortiNavali, Long> implements PortiNavaliManager {

	private PortiNavaliDao portiNavaliDao;
	
	@Override
    @Autowired
	public void setPortiNavaliDao(PortiNavaliDao portiNavaliDao) {
		this.portiNavaliDao = portiNavaliDao;
	}
	
	
	@Override
	public PortiNavali get(Long id) {
		return this.portiNavaliDao.get(id);
	}
	
	
	@Override
	public List<PortiNavali> getPortiNavali() {
		return portiNavaliDao.getPortiNavali();
	}
	
	
	@Override
	public List<PortiNavali> getPortiNavaliBy_LIKE(String term){
		return portiNavaliDao.getPortiNavaliBy_LIKE(term);
	}
	
	
	@Override
	public List<PortiNavali> getPortiNavaliByIdComune(Long idComune){
		return portiNavaliDao.getPortiNavaliByIdComune(idComune);
	}
	
	
	@Override
	public List<PortiNavali> getPortiNavaliBy_ListProvince(List<Long> listProvince, Long portiNavaleEsclusoId){
		return portiNavaliDao.getPortiNavaliBy_ListProvince(listProvince, portiNavaleEsclusoId);
	}
	
	
	@Override
	public PortiNavali getPortiNavaliBy_PlaceId(String PlaceId){
		return portiNavaliDao.getPortiNavaliBy_PlaceId(PlaceId);
	}
	
	
	@Override
	public PortiNavali savePortiNavali(PortiNavali portiNavali) throws Exception {
		return portiNavaliDao.savePortiNavali(portiNavali);
	}

	
	@Override
    public void removePortiNavali(long id) {
		portiNavaliDao.remove(id);
    }
	
	
	
}

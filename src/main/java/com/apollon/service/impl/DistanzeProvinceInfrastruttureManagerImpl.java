package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.DistanzeProvinceInfrastruttureDao;
import com.apollon.model.DistanzeProvinceInfrastrutture;
import com.apollon.service.DistanzeProvinceInfrastruttureManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DistanzeProvinceInfrastruttureManager")
public class DistanzeProvinceInfrastruttureManagerImpl extends GenericManagerImpl<DistanzeProvinceInfrastrutture, Long> implements DistanzeProvinceInfrastruttureManager {

	private DistanzeProvinceInfrastruttureDao distanzeProvinceInfrastruttureDao;
	
	@Override
    @Autowired
	public void setDistanzeProvinceInfrastruttureDao(DistanzeProvinceInfrastruttureDao distanzeProvinceInfrastruttureDao) {
		this.distanzeProvinceInfrastruttureDao = distanzeProvinceInfrastruttureDao;
	}
	
	
	@Override
	public DistanzeProvinceInfrastrutture get(Long id) {
		return this.distanzeProvinceInfrastruttureDao.get(id);
	}

	
	@Override
	public List<DistanzeProvinceInfrastrutture> getDistanzeProvinceInfrastrutture() {
		return distanzeProvinceInfrastruttureDao.getDistanzeProvinceInfrastrutture();
	}
	
	
	@Override
	public DistanzeProvinceInfrastrutture saveDistanzeProvinceInfrastrutture(DistanzeProvinceInfrastrutture distanzeProvinceInfrastrutture) throws Exception {
		return distanzeProvinceInfrastruttureDao.saveDistanzeProvinceInfrastrutture(distanzeProvinceInfrastrutture);
	}

	
	@Override
    public int removeAeroporti_AndataArrivo(Long idAeroporto) {
		return distanzeProvinceInfrastruttureDao.removeAeroporti_AndataArrivo(idAeroporto);
    }
	
	
	@Override
    public int removePortiNavali_AndataArrivo(Long idPortoNavale) {
		return distanzeProvinceInfrastruttureDao.removePortiNavali_AndataArrivo(idPortoNavale);
    }
	
	
	@Override
    public int removeMusei_AndataArrivo(Long idMuseo) {
		return distanzeProvinceInfrastruttureDao.removeMusei_AndataArrivo(idMuseo);
    }
	
	
	@Override
    public void removeDistanzeProvinceInfrastrutture(long id) {
		distanzeProvinceInfrastruttureDao.remove(id);
    }
	
	
	
}

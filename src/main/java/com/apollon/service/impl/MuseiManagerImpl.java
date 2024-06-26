package com.apollon.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.MuseiDao;
import com.apollon.model.Musei;
import com.apollon.service.MuseiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("MuseiManager")
public class MuseiManagerImpl extends GenericManagerImpl<Musei, Long> implements MuseiManager {

	private MuseiDao museiDao;
	
	@Override
    @Autowired
	public void setMuseiDao(MuseiDao museiDao) {
		this.museiDao = museiDao;
	}

	
	
	@Override
	public Musei get(Long id) {
		return this.museiDao.get(id);
	}
	
	
	@Override
	public List<Musei> getMusei() {
		return museiDao.getMusei();
	}
	
	@Override
	public Musei getMuseiBy_PlaceId(String PlaceId){
		return museiDao.getMuseiBy_PlaceId(PlaceId);
	}
	
	@Override
	public List<Musei> getMuseiBy_LIKE(String term){
		return museiDao.getMuseiBy_LIKE(term);
	}
	
	@Override
	public List<Musei> getMuseiByIdComune(Long idComune){
		return museiDao.getMuseiByIdComune(idComune);
	}
	
	
	@Override
	public Musei saveMusei(Musei musei) throws Exception {
		return museiDao.saveMusei(musei);
	}

	
	@Override
    public void removeMusei(long id) {
		museiDao.remove(id);
    }
	
	
	
}

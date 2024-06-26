package com.apollon.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apollon.dao.AeroportiDao;
import com.apollon.model.Aeroporti;
import com.apollon.service.AeroportiManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AeroportiManager")
public class AeroportiManagerImpl extends GenericManagerImpl<Aeroporti, Long> implements AeroportiManager {

	private AeroportiDao aeroportiDao;
	
	@Override
    @Autowired
	public void setAeroportiDao(AeroportiDao aeroportiDao) {
		this.aeroportiDao = aeroportiDao;
	}

	
	
	@Override
	public Aeroporti get(Long id) {
		return this.aeroportiDao.get(id);
	}
	
	
	@Override
	public List<Aeroporti> getAeroporti() {
		return aeroportiDao.getAeroporti();
	}
	
	
	@Override
	public List<Aeroporti> getAeroportiBy_LIKE(String term){
		return aeroportiDao.getAeroportiBy_LIKE(term);
	}

	
	@Override
	public Aeroporti getAeroporto_LIKE_Url(String term){
		return aeroportiDao.getAeroporto_LIKE_Url(term);
	}
	
	
	@Override
	public List<Aeroporti> getAeroporti_Pagination(int jtStartIndex, int jtPageSize) {
		return aeroportiDao.getAeroporti_Pagination(jtStartIndex, jtPageSize);
	}
	
	
	@Override
	public List<Aeroporti> getAeroportiByIdComune(Long idComune){
		return aeroportiDao.getAeroportiByIdComune(idComune);
	}
	
	@Override
	public List<Aeroporti> getAeroportiBy_ListProvince(List<Long> listProvince, Long aeroportoEsclusoId){
		return aeroportiDao.getAeroportiBy_ListProvince(listProvince, aeroportoEsclusoId);
	}
	
	@Override
	public Aeroporti getAeroportoBy_PlaceId(String PlaceId){
		return aeroportiDao.getAeroportoBy_PlaceId(PlaceId);
	}
	
	@Override
	public Aeroporti saveAeroporti(Aeroporti aeroporti) throws Exception {
		return aeroportiDao.saveAeroporti(aeroporti);
	}

	
	@Override
    public void removeAeroporti(long id) {
		aeroportiDao.remove(id);
    }
	
	
	
}

package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.VenditorePercServProvincia;
import com.apollon.service.VenditorePercServProvinciaManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("VenditorePercServProvinciaManager")
public class VenditorePercServProvinciaManagerImpl extends GenericManagerImpl<VenditorePercServProvincia, Long> implements VenditorePercServProvinciaManager {

	private VenditorePercServProvinciaDao VenditorePercServProvinciaDao;
	
	@Override
    @Autowired
	public void setVenditorePercServProvinciaDao(VenditorePercServProvinciaDao VenditorePercServProvinciaDao) {
		this.VenditorePercServProvinciaDao = VenditorePercServProvinciaDao;
	}
	
	
	@Override
	public VenditorePercServProvincia get(Long id) {
		return this.VenditorePercServProvinciaDao.get(id);
	}
	
	@Override
	public VenditorePercServProvincia getVenditorePercServProvincia_by_Venditore_Prov(long idUser, long idProvincia) {
		return this.VenditorePercServProvinciaDao.getVenditorePercServProvincia_by_Venditore_Prov(idUser, idProvincia);
	}

	
	@Override
	public List<VenditorePercServProvincia> getVenditorePercServProvincia() {
		return VenditorePercServProvinciaDao.getVenditorePercServProvincia();
	}
	
	
	@Override
	public VenditorePercServProvincia saveVenditorePercServProvincia(VenditorePercServProvincia VenditorePercServProvincia) throws Exception {
		return VenditorePercServProvinciaDao.saveVenditorePercServProvincia(VenditorePercServProvincia);
	}

	
	@Override
    public void removeVenditorePercServProvincia(long id) {
		VenditorePercServProvinciaDao.remove(id);
    }
	
	
	
}

package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.MarcaAutoScoutDao;
import com.apollon.model.MarcaAutoScout;
import com.apollon.service.MarcaAutoScoutManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("MarcaAutoScoutManager")
public class MarcaAutoScoutManagerImpl extends GenericManagerImpl<MarcaAutoScout, Long> implements MarcaAutoScoutManager {

	private MarcaAutoScoutDao marcaAutoScoutDao;
	
	@Override
    @Autowired
	public void setMarcaAutoScoutDao(MarcaAutoScoutDao marcaAutoScoutDao) {
		this.marcaAutoScoutDao = marcaAutoScoutDao;
	}

	
	
	@Override
	public MarcaAutoScout get(Long id) {
		return this.marcaAutoScoutDao.get(id);
	}
	
	
	@Override
	public MarcaAutoScout getMarcaAutoScout_by_IdAutoScout(Long idAutoScout) {
		return this.marcaAutoScoutDao.getMarcaAutoScout_by_IdAutoScout(idAutoScout);
	}
	
	
	@Override
	public MarcaAutoScout getNomeMarcaAutoScout(String nomeMarca) {
		return marcaAutoScoutDao.getNomeMarcaAutoScout(nomeMarca);
	}

	
	@Override
	public List<MarcaAutoScout> getMarcaAutoScout() {
		return marcaAutoScoutDao.getMarcaAutoScout();
	}
	
	@Override
	public List<MarcaAutoScout> getMarcaAutoScoutDescrizione(String term) {
		return marcaAutoScoutDao.getMarcaAutoScoutDescrizione(term);
	}
	
	
	@Override
	public MarcaAutoScout saveMarcaAutoScout(MarcaAutoScout marcaAutoScout) throws Exception {
		return marcaAutoScoutDao.saveMarcaAutoScout(marcaAutoScout);
	}

	
	@Override
    public void removeMarcaAutoScout(long id) {
		marcaAutoScoutDao.remove(id);
    }
	
	
	
}

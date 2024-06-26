package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.NazioniDao;
import com.apollon.model.Nazioni;
import com.apollon.service.NazioniManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("NazioniManager")
public class NazioniManagerImpl extends GenericManagerImpl<Nazioni, Long> implements NazioniManager {

	private NazioniDao regioniDao;
	
	@Override
    @Autowired
	public void setNazioniDao(NazioniDao regioniDao) {
		this.regioniDao = regioniDao;
	}

	
	
	@Override
	public Nazioni get(Long id) {
		return this.regioniDao.get(id);
	}
	
	@Override
	public List<Nazioni> getNazioni() {
		return regioniDao.getNazioni();
	}
	

	@Override
	public List<Nazioni> getNomeNazioneBy_Like(String term) {
		return regioniDao.getNomeNazioneBy_Like(term);
	}
	
	
	@Override
	public Nazioni saveNazioni(Nazioni regioni) throws DataIntegrityViolationException, HibernateJdbcException {
		return regioniDao.saveNazioni(regioni);
	}

	
	@Override
    public void removeNazioni(long id) {
		regioniDao.remove(id);
    }
	
	
	
}

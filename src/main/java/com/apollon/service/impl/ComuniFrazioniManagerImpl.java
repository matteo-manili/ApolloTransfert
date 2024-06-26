package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.ComuniFrazioniDao;
import com.apollon.model.ComuniFrazioni;
import com.apollon.service.ComuniFrazioniManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ComuniFrazioniManager")
public class ComuniFrazioniManagerImpl extends GenericManagerImpl<ComuniFrazioni, Long> implements ComuniFrazioniManager {

	private ComuniFrazioniDao comuniFrazioniDao;
	
	@Override
    @Autowired
	public void setComuniFrazioniDao(ComuniFrazioniDao comuniFrazioniDao) {
		this.comuniFrazioniDao = comuniFrazioniDao;
	}

	
	@Override
	public ComuniFrazioni get(Long id) {
		return this.comuniFrazioniDao.get(id);
	}
	
	@Override
	public List<ComuniFrazioni> getComuniFrazioni() {
		return comuniFrazioniDao.getComuniFrazioni();
	}

	@Override
	public List<ComuniFrazioni> getNomeFrazioneBy_Like(String term) {
		return comuniFrazioniDao.getNomeFrazioneBy_Like(term);
	}
	
	@Override
	public ComuniFrazioni getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione, Long idComune){
		return comuniFrazioniDao.getComuniFrazioniByNomeFrazione_Equal(nomeFrazione, idComune);
	}
	
	@Override
	public ComuniFrazioni saveComuniFrazioni(ComuniFrazioni comuniFrazioni) throws DataIntegrityViolationException, HibernateJdbcException {
		return comuniFrazioniDao.saveComuniFrazioni(comuniFrazioni);
	}

	
	@Override
    public void removeComuniFrazioni(long id) {
		comuniFrazioniDao.remove(id);
    }
	
	
	
}

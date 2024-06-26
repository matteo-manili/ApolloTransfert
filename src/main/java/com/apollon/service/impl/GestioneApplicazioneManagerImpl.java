package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.model.GestioneApplicazione;
import com.apollon.service.GestioneApplicazioneManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("GestioneApplicazioneManager")
public class GestioneApplicazioneManagerImpl extends GenericManagerImpl<GestioneApplicazione, Long> implements GestioneApplicazioneManager {

	private GestioneApplicazioneDao gestioneApplicazioneDao;
	
	@Override
    @Autowired
	public void setGestioneApplicazioneDao(GestioneApplicazioneDao gestioneApplicazioneDao) {
		this.gestioneApplicazioneDao = gestioneApplicazioneDao;
	}

	
	
	@Override
	public GestioneApplicazione get(Long id) {
		return this.gestioneApplicazioneDao.get(id);
	}
	
	
	@Override
	public List<GestioneApplicazione> getGestioneApplicazione() {
		return gestioneApplicazioneDao.getGestioneApplicazione();
	}
	
	@Override
	public List<GestioneApplicazione> getGestioneApplicazione_senzaCommenti() {
		return gestioneApplicazioneDao.getGestioneApplicazione_senzaCommenti();
	}
	
	@Override
	public List<GestioneApplicazione> getGestioneApplicazioneBy_LIKE(String term){
		return gestioneApplicazioneDao.getGestioneApplicazioneBy_LIKE(term);
	}
	
	
	@Override
	public GestioneApplicazione getName(String name){
		return gestioneApplicazioneDao.getName(name);
	}
	
	
	
	@Override
	public GestioneApplicazione saveGestioneApplicazione(GestioneApplicazione gestioneApplicazione) throws Exception {
		return gestioneApplicazioneDao.saveGestioneApplicazione(gestioneApplicazione);
	}

	
	
	
	@Override
    public void removeGestioneApplicazione(long id) {
		gestioneApplicazioneDao.remove(id);
    }
	
	
	
}

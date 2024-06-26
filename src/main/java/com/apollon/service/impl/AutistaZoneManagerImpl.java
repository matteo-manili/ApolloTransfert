package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaZoneDao;
import com.apollon.model.AutistaZone;
import com.apollon.service.AutistaZoneManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaZoneManager")
public class AutistaZoneManagerImpl extends GenericManagerImpl<AutistaZone, Long> implements AutistaZoneManager {

	private AutistaZoneDao autistaZoneDao;
	
	@Override
    @Autowired
	public void setAutistaZoneDao(AutistaZoneDao autistaZoneDao) {
		this.autistaZoneDao = autistaZoneDao;
	}

	
	
	@Override
	public AutistaZone get(Long id) {
		return this.autistaZoneDao.get(id);
	}
	
	@Override
	public boolean ControllaServiziAttivi(long idAutista) {
		return autistaZoneDao.ControllaServiziAttivi(idAutista);
	}
	
	@Override
	public List<AutistaZone> getAutistaZone() {
		return autistaZoneDao.getAutistaZone();
	}

	@Override
	public List<AutistaZone> getRegioneAutisti_table() {
		return autistaZoneDao.getRegioneAutisti_table();
	}
	
	@Override
	public List<AutistaZone> getAutistaZoneByAutista(long idAutista) {
		return autistaZoneDao.getAutistaZoneByAutista(idAutista);
	}
	
	@Override
	public AutistaZone getAutistaZoneBy_Autista_e_Regione(long idAutista, Long idRegione) {
		return autistaZoneDao.getAutistaZoneBy_Autista_e_Regione(idAutista, idRegione);
	}
	
	@Override
	public AutistaZone getAutistaZoneBy_Autista_e_Provincia(long idAutista, Long idProvincia) {
		return autistaZoneDao.getAutistaZoneBy_Autista_e_Provincia(idAutista, idProvincia);
	}
	
	@Override
	public AutistaZone getAutistaZoneBy_Autista_e_Comune(long idAutista, Long idComune) {
		return autistaZoneDao.getAutistaZoneBy_Autista_e_Comune(idAutista, idComune);
	}
	
	@Override
	public long getNumeroRegioniAutista(long idAutista) {
		return autistaZoneDao.getNumeroRegioniAutista(idAutista);
	}
	
	
	
	@Override
	public AutistaZone saveAutistaZone(AutistaZone autistaZone) throws DataIntegrityViolationException, HibernateJdbcException {
		return autistaZoneDao.saveAutistaZone(autistaZone);
	}

	@Override
    public void removeAutistaZone(long id) {
		autistaZoneDao.remove(id);
    }
	
	
	
}

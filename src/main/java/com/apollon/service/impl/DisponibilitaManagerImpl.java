package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.DisponibilitaDao;
import com.apollon.model.Disponibilita;
import com.apollon.service.DisponibilitaManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DisponibilitaManager")
public class DisponibilitaManagerImpl extends GenericManagerImpl<Disponibilita, Long> implements DisponibilitaManager {

	private DisponibilitaDao disponibilitaDao;
	
	@Override
    @Autowired
	public void setDisponibilitaDao(DisponibilitaDao disponibilitaDao) {
		this.disponibilitaDao = disponibilitaDao;
	}

	
	
	@Override
	public Disponibilita get(Long id) {
		return this.disponibilitaDao.get(id);
	}
	
	
	@Override
	public List<Disponibilita> getDisponibilita() {
		return disponibilitaDao.getDisponibilita();
	}
	
	

	
	
	@Override
	public Disponibilita getDisponibilitaByAutoveicolo(Long idAutoveicolo){
		return disponibilitaDao.getDisponibilitaByAutoveicolo(idAutoveicolo);
	}
	
	

	
	
	@Override
	public Disponibilita saveDisponibilita(Disponibilita disponibilita) throws Exception {
		return disponibilitaDao.saveDisponibilita(disponibilita);
	}

	
	
	@Override
    public void removeDisponibilitaByIdAutoveicolo(long idAutoveicolo) {
		disponibilitaDao.removeDisponibilitaByIdAutoveicolo(idAutoveicolo);
    }
	
	
	@Override
    public void removeDisponibilita(long id) {
		disponibilitaDao.remove(id);
    }
	
	
	
}

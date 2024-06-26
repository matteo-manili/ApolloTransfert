package com.apollon.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.DisponibilitaDateDao;
import com.apollon.model.DisponibilitaDate;
import com.apollon.service.DisponibilitaDateManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("DisponibilitaDateManager")
public class DisponibilitaDateManagerImpl extends GenericManagerImpl<DisponibilitaDate, Long> implements DisponibilitaDateManager {

	private DisponibilitaDateDao disponibilitaDateDao;
	
	@Override
    @Autowired
	public void setDisponibilitaDateDao(DisponibilitaDateDao disponibilitaDateDao) {
		this.disponibilitaDateDao = disponibilitaDateDao;
	}

	
	
	@Override
	public DisponibilitaDate get(Long id) {
		return this.disponibilitaDateDao.get(id);
	}
	
	
	@Override
	public List<DisponibilitaDate> getDisponibilitaDate() {
		return disponibilitaDateDao.getDisponibilitaDate();
	}
	
	
	@Override
	public DisponibilitaDate getDisponibilitaDateBy_Data(Date data){
		return disponibilitaDateDao.getDisponibilitaDateBy_Data(data);
	}
	

	
	
	@Override
	public List<DisponibilitaDate> getDisponibilitaDateByIdDisponibilita(Long IdDisponibilitaDate){
		return disponibilitaDateDao.getDisponibilitaDateByIdDisponibilita(IdDisponibilitaDate);
	}
	
	
	@Override
	public DisponibilitaDate saveDisponibilitaDate(DisponibilitaDate disponibilitaDate) throws Exception {
		return disponibilitaDateDao.saveDisponibilitaDate(disponibilitaDate);
	}


	@Override
    public void removeDisponibilitaDate(long IdDisponibilitaDate) {
		disponibilitaDateDao.remove(IdDisponibilitaDate);
    }

	
	@Override
	public void removeDisponibilitaDateByIdDisponibilita(long idDisponibilita) {
		disponibilitaDateDao.removeDisponibilitaDateByIdDisponibilita(idDisponibilita);
	}
	
	
	
}

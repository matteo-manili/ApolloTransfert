package com.apollon.service;

import java.util.Date;
import java.util.List;

import com.apollon.dao.DisponibilitaDateDao;
import com.apollon.model.DisponibilitaDate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DisponibilitaDateManager extends GenericManager<DisponibilitaDate, Long> {
	
	void setDisponibilitaDateDao(DisponibilitaDateDao disponibilitaDateDao);
	
	
	DisponibilitaDate get(Long id);
	
	List<DisponibilitaDate> getDisponibilitaDate();
	

	DisponibilitaDate getDisponibilitaDateBy_Data(Date data);
	
	List<DisponibilitaDate> getDisponibilitaDateByIdDisponibilita(Long IdDisponibilitaDate);
	
	DisponibilitaDate saveDisponibilitaDate(DisponibilitaDate disponibilitaDate) throws Exception;
	
	void removeDisponibilitaDate(long IdDisponibilitaDate);
	
	void removeDisponibilitaDateByIdDisponibilita(long idDisponibilita);

	

	


	

}

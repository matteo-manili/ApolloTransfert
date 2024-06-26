package com.apollon.dao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.DisponibilitaDate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DisponibilitaDateDao extends GenericDao<DisponibilitaDate, Long> {
	
	DisponibilitaDate get(Long id);
	
	List<DisponibilitaDate> getDisponibilitaDate();
	
	
	
	DisponibilitaDate getDisponibilitaDateBy_Data(Date data);

	List<DisponibilitaDate> getDisponibilitaDateByIdDisponibilita(Long idDisponibilita);
	

	DisponibilitaDate saveDisponibilitaDate(DisponibilitaDate disponibilitaDate) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeDisponibilitaDateByIdDisponibilita(Long idDisponibilita);


	

	



}

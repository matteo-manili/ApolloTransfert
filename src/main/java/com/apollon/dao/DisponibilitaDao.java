package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Disponibilita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DisponibilitaDao extends GenericDao<Disponibilita, Long> {
	
	Disponibilita get(Long id);
	
	List<Disponibilita> getDisponibilita();
	
	Disponibilita saveDisponibilita(Disponibilita disponibilita) throws DataIntegrityViolationException, HibernateJdbcException;

	void removeDisponibilitaByIdAutoveicolo(Long IdAutoveicolo);

	Disponibilita getDisponibilitaByAutoveicolo(Long idAutoveicolo);

	


	



}

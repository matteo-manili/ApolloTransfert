package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Musei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface MuseiDao extends GenericDao<Musei, Long> {
	
	Musei get(Long id);
	
	List<Musei> getMusei();
	
	Musei saveMusei(Musei musei) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Musei> getMuseiByIdComune(Long idComune);

	List<Musei> getMuseiBy_LIKE(String term);

	Musei getMuseiBy_PlaceId(String PlaceId);



}

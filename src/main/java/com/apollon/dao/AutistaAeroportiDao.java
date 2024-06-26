package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaAeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaAeroportiDao extends GenericDao<AutistaAeroporti, Long> {
	
	AutistaAeroporti get(Long id);
	
	List<AutistaAeroporti> getAutistaAeroporti();
	
	AutistaAeroporti saveAutistaAeroporti(AutistaAeroporti autistaAeroporti) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaAeroporti> getAutistaAeroportiByIdAutista(Long idComune);

	void removeAutistaAeroportiByIdAutista(Long idAutista);



}

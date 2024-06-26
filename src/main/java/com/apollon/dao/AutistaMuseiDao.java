package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaMusei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaMuseiDao extends GenericDao<AutistaMusei, Long> {
	
	AutistaMusei get(Long id);
	
	List<AutistaMusei> getAutistaMusei();
	
	AutistaMusei saveAutistaMusei(AutistaMusei musei) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaMusei> getAutistaMuseiByIdAutista(Long idComune);

	void removeAutistaMuseiByIdAutista(Long idAutista);



}

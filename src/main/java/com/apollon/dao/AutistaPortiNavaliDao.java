package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaPortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaPortiNavaliDao extends GenericDao<AutistaPortiNavali, Long> {
	
	AutistaPortiNavali get(Long id);
	
	List<AutistaPortiNavali> getAutistaPortiNavali();
	
	AutistaPortiNavali saveAutistaPortiNavali(AutistaPortiNavali autistaPortiNavali) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaPortiNavali> getAutistaPortiNavaliByIdAutista(Long idComune);

	void removeAutistaPortiNavaliByIdAutista(Long idAutista);



}

package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Ritardi;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RitardiDao extends GenericDao<Ritardi, Long> {
	
	Ritardi get(Long id);
	
	List<Ritardi> getRitardi();
	
	Ritardi getRitardoBy_IdRicercaTransfert(long idCorsa);
	
	Ritardi saveRitardi(Ritardi ritardi) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Ritardi> getRitardiCliente(long user);

	void removeRitardobyId(Long idRitardo);

	


	










	


	



}

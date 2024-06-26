package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Supplementi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface SupplementiDao extends GenericDao<Supplementi, Long> {
	
	Supplementi get(Long id);
	
	List<Supplementi> getSupplementi();
	
	List<Supplementi> getSupplementiBy_IdRicercaTransfert(long idCorsa);
	
	Supplementi saveSupplementi(Supplementi supplementi) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Supplementi> getSupplementiCliente(long user);

	void removeSupplementobyId(Long idSupplemento);

	


	










	


	



}

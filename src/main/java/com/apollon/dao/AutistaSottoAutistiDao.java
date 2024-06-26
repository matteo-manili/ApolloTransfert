package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaSottoAutisti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaSottoAutistiDao extends GenericDao<AutistaSottoAutisti, Long> {
	
	AutistaSottoAutisti get(Long id);
	
	List<AutistaSottoAutisti> getAutistaSottoAutisti();
	
	AutistaSottoAutisti saveAutistaSottoAutisti(AutistaSottoAutisti autistaSottoAutisti) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaSottoAutisti> getAutistaSottoAutisti_By_Autista(long idAutista);


	



	










	


	



}

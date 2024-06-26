package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaLicenzeNcc;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaLicenzeNccDao extends GenericDao<AutistaLicenzeNcc, Long> {
	
	AutistaLicenzeNcc get(Long id);
	
	List<AutistaLicenzeNcc> getAutistaLicenzeNcc();
	
	AutistaLicenzeNcc saveAutistaLicenzeNcc(AutistaLicenzeNcc autistaLicenzeNcc) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaLicenzeNcc> getAutistaLicenzeNcc_By_Autista(long idAutista);



	










	


	



}

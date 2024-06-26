package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.GestioneApplicazione;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface GestioneApplicazioneDao extends GenericDao<GestioneApplicazione, Long> {
	
	GestioneApplicazione get(Long id);
	
	List<GestioneApplicazione> getGestioneApplicazione();
	List<GestioneApplicazione> getGestioneApplicazione_senzaCommenti();
	
	GestioneApplicazione saveGestioneApplicazione(GestioneApplicazione gestioneApplicazione) throws DataIntegrityViolationException, HibernateJdbcException;

	GestioneApplicazione getName(String name);

	List<GestioneApplicazione> getGestioneApplicazioneBy_LIKE(String term);

	



	


	



}

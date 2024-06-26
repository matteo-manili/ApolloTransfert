package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AutistaZone;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaZoneDao extends GenericDao<AutistaZone, Long> {
	
	AutistaZone get(Long id);
	
	List<AutistaZone> getAutistaZone();
	
	AutistaZone saveAutistaZone(AutistaZone autistaZone) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AutistaZone> getAutistaZoneByAutista(long idAutista);

	AutistaZone getAutistaZoneBy_Autista_e_Regione(long idAutista,Long idRegione);
	
	AutistaZone getAutistaZoneBy_Autista_e_Provincia(long idAutista,Long idProvincia);

	AutistaZone getAutistaZoneBy_Autista_e_Comune(long idAutista, Long idComune);

	long getNumeroRegioniAutista(long idAutista);

	List<AutistaZone> getRegioneAutisti_table();

	boolean ControllaServiziAttivi(long idAutista);

	

	

	


}

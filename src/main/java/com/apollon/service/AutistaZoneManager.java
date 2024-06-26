package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.AutistaZoneDao;
import com.apollon.model.AutistaZone;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaZoneManager extends GenericManager<AutistaZone, Long> {
	
	void setAutistaZoneDao(AutistaZoneDao autistaZoneDao);
	
	
	AutistaZone get(Long id);
	
	List<AutistaZone> getAutistaZoneByAutista(long idAutista);
	
	List<AutistaZone> getAutistaZone();
	
	AutistaZone saveAutistaZone(AutistaZone autistaZone) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeAutistaZone(long userAutistaZone);

	AutistaZone getAutistaZoneBy_Autista_e_Regione(long idAutista,Long idRegione);
	
	AutistaZone getAutistaZoneBy_Autista_e_Provincia(long idAutista,Long idProvincia);

	AutistaZone getAutistaZoneBy_Autista_e_Comune(long idAutista, Long idComune);

	long getNumeroRegioniAutista(long idAutista);

	List<AutistaZone> getRegioneAutisti_table();

	boolean ControllaServiziAttivi(long idAutista);

	


	

}

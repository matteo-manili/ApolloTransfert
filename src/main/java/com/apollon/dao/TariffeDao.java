package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Tariffe;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface TariffeDao extends GenericDao<Tariffe, Long> {
	
	Tariffe get(Long id);
	
	List<Tariffe> getTariffe();
	
	List<Tariffe> getTariffe_by_OrderProvincia();

	List<Tariffe> getTariffeByIdAutista(Long idAutista);
	
	Tariffe getTariffeBy_Autoveicolo_e_Zona(Long idAutoveicolo,Long idZona);

	Tariffe getTariffeBy_Autoveicolo_e_Aeroporto(Long idAutoveicolo, Long idAero);

	Tariffe getTariffeBy_Autoveicolo_e_Porto(Long idAutoveicolo, Long idPorto);

	boolean TariffeImpostate(Long idAutista);
	
	
	Tariffe saveTariffe(Tariffe tariffe) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeTariffeByIdAutista(Long idAutista);

	



}

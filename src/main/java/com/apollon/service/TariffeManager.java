package com.apollon.service;

import java.util.List;

import com.apollon.dao.TariffeDao;
import com.apollon.model.Tariffe;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface TariffeManager extends GenericManager<Tariffe, Long> {
	
	void setTariffeDao(TariffeDao tariffeDao);
	
	
	Tariffe get(Long id);
	
	List<Tariffe> getTariffe();
	
	Tariffe saveTariffe(Tariffe tariffe) throws Exception;
	
	void removeTariffe(long userTariffe);

	Tariffe getTariffeBy_Autoveicolo_e_Zona(Long idAutoveicolo,Long idZona);
	
	Tariffe getTariffeBy_Autoveicolo_e_Aeroporto(Long idAutoveicolo, Long idAero);

	Tariffe getTariffeBy_Autoveicolo_e_Porto(Long idAutoveicolo, Long idPorto);
	
	List<Tariffe> getTariffeByIdAutista(Long idAutista);

	void removeTariffeByIdAutista(long idAutista);

	boolean TariffeImpostate(Long id);

	

	


	

}

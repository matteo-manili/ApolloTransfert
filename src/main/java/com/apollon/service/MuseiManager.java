package com.apollon.service;

import java.util.List;

import com.apollon.dao.MuseiDao;
import com.apollon.model.Musei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface MuseiManager extends GenericManager<Musei, Long> {
	
	void setMuseiDao(MuseiDao museiDao);
	
	
	Musei get(Long id);
	
	List<Musei> getMusei();
	
	Musei saveMusei(Musei musei) throws Exception;
	
	void removeMusei(long userMusei);

	List<Musei> getMuseiByIdComune(Long idComune);

	List<Musei> getMuseiBy_LIKE(String term);

	Musei getMuseiBy_PlaceId(String PlaceId);


	

}

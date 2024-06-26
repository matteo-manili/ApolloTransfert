package com.apollon.service;

import java.util.List;

import com.apollon.dao.VisitatoriDao;
import com.apollon.model.Visitatori;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface VisitatoriManager extends GenericManager<Visitatori, Long> {
	
	void setVisitatoriDao(VisitatoriDao visitatoriDao);
	
	
	Visitatori get(Long id);
	
	List<Visitatori> getVisitatori();
	
	List<Visitatori> getVisitatoriTable(int maxResults, Integer firstResult);
	
	int getCountVisitatoriTable();
	
	Visitatori saveVisitatori(Visitatori visitatori) throws Exception;

	void removeVisitatori(long idVisitatori);

	List<Visitatori> getVisitatoriBy_LIKE(String term);

	

	
}

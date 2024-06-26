package com.apollon.service;

import java.util.List;

import com.apollon.dao.AeroportiDao;
import com.apollon.model.Aeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AeroportiManager extends GenericManager<Aeroporti, Long> {
	
	void setAeroportiDao(AeroportiDao aeroportiDao);
	
	
	Aeroporti get(Long id);
	
	List<Aeroporti> getAeroporti();
	
	Aeroporti saveAeroporti(Aeroporti aeroporti) throws Exception;
	
	void removeAeroporti(long userAeroporti);

	List<Aeroporti> getAeroportiByIdComune(Long idComune);

	List<Aeroporti> getAeroportiBy_LIKE(String term);

	List<Aeroporti> getAeroporti_Pagination(int jtStartIndex, int jtPageSize);

	Aeroporti getAeroportoBy_PlaceId(String PlaceId);

	Aeroporti getAeroporto_LIKE_Url(String term);

	List<Aeroporti> getAeroportiBy_ListProvince(List<Long> listProvince, Long aeroportoEsclusoId);


	

}

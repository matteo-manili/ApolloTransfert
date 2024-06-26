package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Aeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AeroportiDao extends GenericDao<Aeroporti, Long> {
	
	Aeroporti get(Long id);
	
	List<Aeroporti> getAeroporti();
	
	Aeroporti saveAeroporti(Aeroporti aeroporti) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Aeroporti> getAeroportiByIdComune(Long idComune);

	List<Aeroporti> getAeroportiBy_LIKE(String term);

	List<Aeroporti> getAeroporti_Pagination(int jtStartIndex, int jtPageSize);

	Aeroporti getAeroportoBy_PlaceId(String PlaceId);

	Aeroporti getAeroporto_LIKE_Url(String term);

	List<Aeroporti> getAeroportiBy_ListProvince(List<Long> listProvince, Long aeroportoEsclusoId);






}

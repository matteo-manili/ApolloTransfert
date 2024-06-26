package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.CoordGeoProvince;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface CoordGeoProvinceDao extends GenericDao<CoordGeoProvince, Long> {
	
	CoordGeoProvince get(Long id);
	
	CoordGeoProvince saveCoordGeoProvince(CoordGeoProvince agA_ModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException;

	List<CoordGeoProvince> getCoordGeoProvince_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata);

	CoordGeoProvince getCoordGeoProvince_by_LatLng(double lat, double lng, int roundCoordinates);









	


	



}

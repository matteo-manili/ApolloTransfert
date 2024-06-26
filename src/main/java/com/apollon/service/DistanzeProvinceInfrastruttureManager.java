package com.apollon.service;

import java.util.List;

import com.apollon.dao.DistanzeProvinceInfrastruttureDao;
import com.apollon.model.DistanzeProvinceInfrastrutture;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DistanzeProvinceInfrastruttureManager extends GenericManager<DistanzeProvinceInfrastrutture, Long> {
	
	void setDistanzeProvinceInfrastruttureDao(DistanzeProvinceInfrastruttureDao distanzeProvinceInfrastruttureDao);
	
	DistanzeProvinceInfrastrutture get(Long id);
	
	List<DistanzeProvinceInfrastrutture> getDistanzeProvinceInfrastrutture();
	
	
	DistanzeProvinceInfrastrutture saveDistanzeProvinceInfrastrutture(DistanzeProvinceInfrastrutture distanzeProvinceInfrastrutture) throws Exception;
	
	void removeDistanzeProvinceInfrastrutture(long userDistanzeProvinceInfrastrutture);

	int removeAeroporti_AndataArrivo(Long idAeroporto);

	int removeMusei_AndataArrivo(Long idMuseo);

	int removePortiNavali_AndataArrivo(Long idPortoNavale);

}

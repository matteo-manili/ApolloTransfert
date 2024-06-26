package com.apollon.dao;

import java.util.List;
import com.apollon.model.DistanzeProvinceInfrastrutture;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DistanzeProvinceInfrastruttureDao extends GenericDao<DistanzeProvinceInfrastrutture, Long> {
	
	DistanzeProvinceInfrastrutture get(Long id);
	
	List<DistanzeProvinceInfrastrutture> getDistanzeProvinceInfrastrutture();
	
	DistanzeProvinceInfrastrutture getProvinciaAndata_InfraArrivo(long idProvinciaAndata, Long idProvinciaArrivo,
			Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance);

	DistanzeProvinceInfrastrutture getAeroportoAndata_InfraArrivo(long idAeroportoAndata, Long idProvinciaArrivo,
			Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance);

	DistanzeProvinceInfrastrutture getPortoAndata_InfraArrivo(long idPortoAndata, Long idProvinciaArrivo,
			Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance);

	DistanzeProvinceInfrastrutture getMuseoAndata_InfraArrivo(long idMuseoAndata, Long idProvinciaArrivo,
			Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance);

	
	DistanzeProvinceInfrastrutture saveDistanzeProvinceInfrastrutture(DistanzeProvinceInfrastrutture distanzeProvinceInfrastrutture);

	int removeAeroporti_AndataArrivo(Long idAeroporto);

	int removePortiNavali_AndataArrivo(Long idPortoNavale);

	int removeMusei_AndataArrivo(Long idMuseo);


	
}

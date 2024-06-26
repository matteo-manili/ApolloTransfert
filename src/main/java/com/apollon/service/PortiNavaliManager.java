package com.apollon.service;

import java.util.List;

import com.apollon.dao.PortiNavaliDao;
import com.apollon.model.PortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface PortiNavaliManager extends GenericManager<PortiNavali, Long> {
	
	void setPortiNavaliDao(PortiNavaliDao portiNavaliDao);
	
	
	PortiNavali get(Long id);
	
	List<PortiNavali> getPortiNavali();
	
	PortiNavali savePortiNavali(PortiNavali portiNavali) throws Exception;
	
	void removePortiNavali(long userPortiNavali);

	List<PortiNavali> getPortiNavaliByIdComune(Long idComune);
	
	List<PortiNavali> getPortiNavaliBy_ListProvince(List<Long> listProvince, Long portiNavaleEsclusoId);

	List<PortiNavali> getPortiNavaliBy_LIKE(String term);

	PortiNavali getPortiNavaliBy_PlaceId(String PlaceId);

	


	

}

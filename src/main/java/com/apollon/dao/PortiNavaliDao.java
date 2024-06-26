package com.apollon.dao;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.model.PortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface PortiNavaliDao extends GenericDao<PortiNavali, Long> {
	
	PortiNavali get(Long id);
	
	List<PortiNavali> getPortiNavali();
	
	PortiNavali savePortiNavali(PortiNavali portiNavali) throws DataIntegrityViolationException, HibernateJdbcException;

	List<PortiNavali> getPortiNavaliByIdComune(Long idComune);
	
	List<PortiNavali> getPortiNavaliBy_ListProvince(List<Long> listProvince, Long portiNavaleEsclusoId);

	List<PortiNavali> getPortiNavaliBy_LIKE(String term);

	PortiNavali getPortiNavaliBy_PlaceId(String PlaceId);

	



}

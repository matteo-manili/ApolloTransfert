package com.apollon.service;

import java.util.List;

import com.apollon.dao.AutistaPortiNavaliDao;
import com.apollon.model.AutistaPortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaPortiNavaliManager extends GenericManager<AutistaPortiNavali, Long> {
	
	void setAutistaPortiNavaliDao(AutistaPortiNavaliDao autistaPortiNavaliDao);
	
	
	AutistaPortiNavali get(Long id);
	
	List<AutistaPortiNavali> getAutistaPortiNavali();
	
	AutistaPortiNavali saveAutistaPortiNavali(AutistaPortiNavali autistaPortiNavali) throws Exception;
	
	void removeAutistaPortiNavali(long userAutistaPortiNavali);

	List<AutistaPortiNavali> getAutistaPortiNavaliByIdAutista(Long idAutista);

	void removeAutistaPortiNavaliByIdAutista(long idAutista);


	

}

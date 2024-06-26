package com.apollon.service;

import java.util.List;

import com.apollon.dao.AutistaMuseiDao;
import com.apollon.model.AutistaMusei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaMuseiManager extends GenericManager<AutistaMusei, Long> {
	
	void setAutistaMuseiDao(AutistaMuseiDao autistaMuseiDao);
	
	
	AutistaMusei get(Long id);
	
	List<AutistaMusei> getAutistaMusei();
	
	AutistaMusei saveAutistaMusei(AutistaMusei autistaMusei) throws Exception;
	
	void removeAutistaMusei(long userAutistaMusei);

	List<AutistaMusei> getAutistaMuseiByIdAutista(Long idAutista);

	void removeAutistaMuseiByIdAutista(long idAutista);


	

}

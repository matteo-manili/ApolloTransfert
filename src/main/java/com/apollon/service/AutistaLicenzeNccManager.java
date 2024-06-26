package com.apollon.service;

import java.util.List;

import com.apollon.dao.AutistaLicenzeNccDao;
import com.apollon.model.AutistaLicenzeNcc;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaLicenzeNccManager extends GenericManager<AutistaLicenzeNcc, Long> {
	
	void setAutistaLicenzeNccDao(AutistaLicenzeNccDao autistaLicenzeNccDao);
	
	
	AutistaLicenzeNcc get(Long id);
	
	List<AutistaLicenzeNcc> getAutistaLicenzeNcc();

	List<AutistaLicenzeNcc> getAutistaLicenzeNcc_By_Autista(long idAutista);
	
	void removeAutistaLicenzeNcc(long idAutistaLicenzeNcc);
	
	AutistaLicenzeNcc saveAutistaLicenzeNcc(AutistaLicenzeNcc autistaLicenzeNcc) throws Exception;


	
	












	
	

	

	


	

}

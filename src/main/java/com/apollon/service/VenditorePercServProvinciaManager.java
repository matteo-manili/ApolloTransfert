package com.apollon.service;

import java.util.List;

import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.VenditorePercServProvincia;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface VenditorePercServProvinciaManager extends GenericManager<VenditorePercServProvincia, Long> {
	
	void setVenditorePercServProvinciaDao(VenditorePercServProvinciaDao VenditorePercServProvinciaDao);
	
	VenditorePercServProvincia get(Long id);
	
	VenditorePercServProvincia getVenditorePercServProvincia_by_Venditore_Prov(long idUser, long idProvincia);
	
	List<VenditorePercServProvincia> getVenditorePercServProvincia();
	
	VenditorePercServProvincia saveVenditorePercServProvincia(VenditorePercServProvincia VenditorePercServProvincia) throws Exception;
	
	void removeVenditorePercServProvincia(long userVenditorePercServProvincia);

	



}

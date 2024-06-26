package com.apollon.dao;

import java.util.List;
import com.apollon.model.VenditorePercServProvincia;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface VenditorePercServProvinciaDao extends GenericDao<VenditorePercServProvincia, Long> {
	
	VenditorePercServProvincia get(Long id);
	
	VenditorePercServProvincia getVenditorePercServProvincia_by_Venditore_Prov(long idUser, long idProvincia);
	
	List<VenditorePercServProvincia> getVenditorePercServProvincia();
	
	List<VenditorePercServProvincia> getVenditorePercServProvincia_by_Venditore(long idUser);

	
	VenditorePercServProvincia saveVenditorePercServProvincia(VenditorePercServProvincia VenditorePercServProvincia);

}

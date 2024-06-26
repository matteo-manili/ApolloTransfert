package com.apollon.dao;

import java.util.List;

import com.apollon.model.Autista;
import com.apollon.model.Comunicazioni;
import com.apollon.model.User;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComunicazioniDao extends GenericDao<Comunicazioni, Long> {
	
	Comunicazioni saveComunicazioni(Comunicazioni emailAutistiMarketing);
	
	Comunicazioni get(Long id);
	
	List<Comunicazioni> getComunicazioni();
	
	Comunicazioni Counicazione_By_Comunicazione(String StringComunicazione);


}

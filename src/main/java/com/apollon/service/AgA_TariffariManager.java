package com.apollon.service;

import java.util.Date;
import java.util.List;

import com.apollon.dao.AgA_TariffariDao;
import com.apollon.model.AgA_Tariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_TariffariManager extends GenericManager<AgA_Tariffari, Long> {
	
	void setAgA_TariffariDao(AgA_TariffariDao agA_TariffariDao);
	
	AgA_Tariffari get(Long id);
	
	List<AgA_Tariffari> ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(Date dataGiornataOrario, long idAutoveicolo);
	
	AgA_Tariffari saveAgA_Tariffari(AgA_Tariffari agA_Tariffari) throws Exception;
	
	void removeAgA_Tariffari(long idAgA_Tariffari);
	
	void removeAgA_Tariffari_by_idGiornata(long idGiornata);

	List<AgA_Tariffari> getAgA_Tariffari_by_idGiornata(Long idGiornata);
	
	List<AgA_Tariffari> getAgA_Tariffari_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo);

	AgA_Tariffari getAgA_Tariffari_by_IdGiornata_e_KmCorsa(long idGiornata, int kmCorsa);

	List<AgA_Tariffari> getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(long idGiornata, Integer kmCorsaFrom, Integer kmCorsaTo);
	

}

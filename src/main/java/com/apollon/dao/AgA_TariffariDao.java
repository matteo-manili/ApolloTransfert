package com.apollon.dao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AgA_Tariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_TariffariDao extends GenericDao<AgA_Tariffari, Long> {
	
	AgA_Tariffari get(Long id);
	
	List<AgA_Tariffari> ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(Date dataGiornataOrario, long idAutoveicolo);
	
	AgA_Tariffari saveAgA_Tariffari(AgA_Tariffari agA_Tariffari) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeAgA_Tariffari_by_idGiornata(Long idGiornata);

	List<AgA_Tariffari> getAgA_Tariffari_by_idGiornata(Long idGiornata);
	
	List<AgA_Tariffari> getAgA_Tariffari_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo);

	AgA_Tariffari getAgA_Tariffari_by_IdGiornata_e_KmCorsa(long idGiornata, int kmCorsa);
	
	List<AgA_Tariffari> getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(long idGiornata, Integer kmCorsaFrom, Integer kmCorsaTo);

	

	









	


	



}

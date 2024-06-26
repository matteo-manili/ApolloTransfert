package com.apollon.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AgA_TariffariDao;
import com.apollon.model.AgA_Tariffari;
import com.apollon.service.AgA_TariffariManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_TariffariManager")
public class AgA_TariffariManagerImpl extends GenericManagerImpl<AgA_Tariffari, Long> implements AgA_TariffariManager {

	private AgA_TariffariDao agA_TariffariDao;
	
	@Override
    @Autowired
	public void setAgA_TariffariDao(AgA_TariffariDao agA_TariffariDao) {
		this.agA_TariffariDao = agA_TariffariDao;
	}

	@Override
	public AgA_Tariffari get(Long id) {
		return this.agA_TariffariDao.get(id);
	}
	
	@Override
	public AgA_Tariffari saveAgA_Tariffari(AgA_Tariffari agA_Tariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_TariffariDao.saveAgA_Tariffari(agA_Tariffari);
	}

	@Override
    public void removeAgA_Tariffari(long id) {
		agA_TariffariDao.remove(id);
    }
	
	@Override
    public void removeAgA_Tariffari_by_idGiornata(long idGiornata) {
		agA_TariffariDao.removeAgA_Tariffari_by_idGiornata(idGiornata);
    }
	
	@Override
	public List<AgA_Tariffari> getAgA_Tariffari_by_idGiornata(Long idGiornata) {
		return agA_TariffariDao.getAgA_Tariffari_by_idGiornata(idGiornata);
	}
	
	@Override
	public List<AgA_Tariffari> getAgA_Tariffari_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo) {
		return agA_TariffariDao.getAgA_Tariffari_by_dataGiornataOrario_idAutoveicolo(dataGiornataOrario, idAutoveicolo);
	}
	
	@Override
	public AgA_Tariffari getAgA_Tariffari_by_IdGiornata_e_KmCorsa(long idGiornata, int kmCorsa) {
		return agA_TariffariDao.getAgA_Tariffari_by_IdGiornata_e_KmCorsa(idGiornata, kmCorsa);
	}
	
	@Override
	public List<AgA_Tariffari> getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(long idGiornata, Integer kmCorsaFrom, Integer kmCorsaTo) {
		return agA_TariffariDao.getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(idGiornata, kmCorsaFrom, kmCorsaTo);
	}
	
	@Override
	public List<AgA_Tariffari> ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(Date dataGiornataOrario, long idAutoveicolo) {
		return agA_TariffariDao.ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(dataGiornataOrario, idAutoveicolo);
	}
	
}

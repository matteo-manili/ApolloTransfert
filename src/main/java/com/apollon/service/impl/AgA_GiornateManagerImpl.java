package com.apollon.service.impl;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;
import com.apollon.dao.AgA_GiornateDao;
import com.apollon.model.AgA_Giornate;
import com.apollon.service.AgA_GiornateManager;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_GiornateManager")
public class AgA_GiornateManagerImpl extends GenericManagerImpl<AgA_Giornate, Long> implements AgA_GiornateManager {

	private AgA_GiornateDao agA_GiornateDao;
	
	@Override
    @Autowired
	public void setAgA_GiornateDao(AgA_GiornateDao agA_GiornateDao) {
		this.agA_GiornateDao = agA_GiornateDao;
	}

	@Override
	public AgA_Giornate get(Long id) {
		return this.agA_GiornateDao.get(id);
	}
	
	@Override
	public AgA_Giornate saveAgA_Giornate(AgA_Giornate agA_Giornate) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_GiornateDao.saveAgA_Giornate(agA_Giornate);
	}

	@Override
    public void removeAgA_Giornate(long id) {
		agA_GiornateDao.remove(id);
    }
	
	@Override
	public List<AgA_Giornate> getAgA_Giornate_by_idAutoveicolo(Long idAutoveicolo) {
		return agA_GiornateDao.getAgA_Giornate_by_idAutoveicolo(idAutoveicolo);
	}

	@Override
	public AgA_Giornate getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo) {
		return agA_GiornateDao.getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(dataGiornataOrario, idAutoveicolo);
	}

	@Override
	public List<TabellaAutoveicoloModelloTariffario> ListaModelliTariffari(long idAutoveicolo) {
		return agA_GiornateDao.ListaModelliTariffari(idAutoveicolo);
	}
	
	@Override
	public List<Object> ListaTariffariGiornata_ListaModelliTariffari(Date giornata, long idAutoveicolo) {
		return agA_GiornateDao.ListaTariffariGiornata_ListaModelliTariffari(giornata, idAutoveicolo);
	}
	
	@Override
	public List<Object> ListaTariffariCalendario_ListaModelliTariffari(Calendario_FrontEnd calendario_FrontEnd, long idAutoveicolo) {
		return agA_GiornateDao.ListaTariffariCalendario_ListaModelliTariffari(calendario_FrontEnd, idAutoveicolo);
	}
	
	@Override
	public List<Object> ListaTariffariGiornataOrario(Date giornata, long idAutoveicolo) {
		return agA_GiornateDao.ListaTariffariGiornataOrario(giornata, idAutoveicolo);
	}
	
	@Override
	public void EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(Date giornata, long idModelloGiornata, long idAutoveicolo) {
		agA_GiornateDao.EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(giornata, idModelloGiornata, idAutoveicolo);
	}
	
	@Override
	public void EliminaGiornataListaTariffari(Date giornata, long idAutoveicolo) {
		agA_GiornateDao.EliminaGiornataListaTariffari(giornata, idAutoveicolo);
	}
	
	@Override
	public List<TabellaModelloGiornata> ListaModelliGiornata_OrariGiornataIdModelliTariffari(long idAutoveicolo) {
		return agA_GiornateDao.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
	}
	
	@Override
	public List<AgA_Giornate> ListaGiornata_DisponbileVendita_Mese(Calendario_FrontEnd tabellaMeseCalendario, long idAutoveicolo) {
		return agA_GiornateDao.ListaGiornata_DisponbileVendita_Mese(tabellaMeseCalendario, idAutoveicolo);
	}
	
	@Override
	public JSONObject Menu_Data(long idAutoveicolo) {
		return agA_GiornateDao.Menu_Data(idAutoveicolo);
	}

	@Override
	public boolean AutoveicoloDisponbileVendita(long idAutoveicolo) {
		return agA_GiornateDao.AutoveicoloDisponbileVendita(idAutoveicolo);
	}
}

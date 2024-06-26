package com.apollon.dao;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.model.AgA_Giornate;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_GiornateDao extends GenericDao<AgA_Giornate, Long> {
	
	AgA_Giornate get(Long id);
	
	AgA_Giornate saveAgA_Giornate(AgA_Giornate agA_Giornate) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AgA_Giornate> getAgA_Giornate_by_idAutoveicolo(Long idAutoveicolo);

	AgA_Giornate getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo);
	
	List<TabellaAutoveicoloModelloTariffario> ListaModelliTariffari(long idAutoveicolo);
	
	List<Object> ListaTariffariGiornata_ListaModelliTariffari(Date giornata, long idAutoveicolo);
	
	List<Object> ListaTariffariGiornataOrario(Date giornata, long idAutoveicolo);

	void EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(Date giornata, long idModelloGiornata, long idAutoveicolo);

	void EliminaGiornataListaTariffari(Date giornata, long idAutoveicolo);

	List<TabellaModelloGiornata> ListaModelliGiornata_OrariGiornataIdModelliTariffari(long idAutoveicolo);

	List<AgA_Giornate> ListaGiornata_DisponbileVendita_Mese(Calendario_FrontEnd tabellaMeseCalendario, long idAutoveicolo);

	List<Object> ListaTariffariCalendario_ListaModelliTariffari(Calendario_FrontEnd tabellaMeseCalendario, long idAutoveicolo);

	JSONObject Menu_Data(long idAutoveicolo);

	boolean AutoveicoloDisponbileVendita(long idAutoveicolo);

	List<TabellaGiornataTariffario> ListaTariffariMese(Calendario_FrontEnd calendario_FrontEnd, long idAutoveicolo);

	void PuliziaDatabase_GiornateTariffari();

	



	


	



}

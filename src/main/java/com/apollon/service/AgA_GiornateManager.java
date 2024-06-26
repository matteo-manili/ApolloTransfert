package com.apollon.service;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import com.apollon.dao.AgA_GiornateDao;
import com.apollon.model.AgA_Giornate;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_GiornateManager extends GenericManager<AgA_Giornate, Long> {
	
	void setAgA_GiornateDao(AgA_GiornateDao agA_GiornateDao);
	
	AgA_Giornate get(Long id);
	
	AgA_Giornate saveAgA_Giornate(AgA_Giornate agA_Giornate) throws Exception;
	
	void removeAgA_Giornate(long idAgA_Giornate);

	List<AgA_Giornate> getAgA_Giornate_by_idAutoveicolo(Long idAutoveicolo);

	AgA_Giornate getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo);
	
	List<TabellaAutoveicoloModelloTariffario> ListaModelliTariffari(long idAutoveicolo);
	
	List<Object> ListaTariffariGiornata_ListaModelliTariffari(Date giornata, long idAutoveicolo);
	
	List<Object> ListaTariffariCalendario_ListaModelliTariffari(Calendario_FrontEnd calendario_FrontEnd,long idAutoveicolo);
	
	List<Object> ListaTariffariGiornataOrario(Date giornata, long idAutoveicolo);
	
	void EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(Date giornata, long idModelloGiornata, long idAutoveicolo);
	
	void EliminaGiornataListaTariffari(Date giornata, long idAutoveicolo);
	
	List<TabellaModelloGiornata> ListaModelliGiornata_OrariGiornataIdModelliTariffari(long idAutoveicolo);

	List<AgA_Giornate> ListaGiornata_DisponbileVendita_Mese(Calendario_FrontEnd tabellaMeseCalendario, long idAutoveicolo);
	
	JSONObject Menu_Data(long idAutoveicolo);
	
	boolean AutoveicoloDisponbileVendita(long idAutoveicolo);

	
}


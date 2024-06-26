package com.apollon.service;

import java.util.Date;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaMusei;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.Tariffe;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RicercaTransfertManager extends GenericManager<RicercaTransfert, Long> {
	
	void setRicercaTransfertDao(RicercaTransfertDao ricercaTransfertDao);
	
	
	RicercaTransfert get(Long id);
	
	RicercaTransfert getInfoAutista_by_ClientePanel_AgendaAutista(Long idRicTransfert, int NumOreInfoAutista);
	
	RicercaTransfert getRicercaTransfertVenditore(long idUserVenditore, long idTransfert);
	
	List<RicercaTransfert> getRicercheTransfertVenditore(long idUserVenditore);
	
	RicercaTransfert getCollapsePanelCorsaAdmin(Long id);
	
	int updateCollapsePanelCorsaAdmin(long id, boolean collapse);
	
	int updateApprovazioneCorsaAndataAdmin(long id, int approv);

	int updateApprovazioneCorsaRitornoAdmin(long id, int approv);
	
	List<RicercaTransfert> getRicercaTransfert();
	
	RicercaTransfert saveRicercaTransfert(RicercaTransfert ricercaTransfert) throws Exception;
	
	void removeRicercaTransfert(long userRicercaTransfert);

	List<RicercaTransfert> getRicercaTransfertBy_LIKE(String term);

	List<AutistaAeroporti> Ricerca_Autisti_ServizioStandard_AEROPORTI(long idAeroporto);

	List<AutistaPortiNavali> Ricerca_Autisti_ServizioStandard_PORTINAVALI(long idPorto);
	
	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE(List<Long> listIdZone, List<Long> listTipiAutoveicoli);

	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(List<Long> listIdAeroporti, List<Long> listTipiAutoveicoli);

	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(List<Long> listIdporti, List<Long> listTipiAutoveicoli);

	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE_LP(List<Long> listIdZone, List<Long> listTipiAutoveicoli, int max_km_lp);

	boolean Autoveicolo_Disponibe(long autoveicolo, List<Date> datePrelevRitorn);

	List<AutistaMusei> Ricerca_Autisti_ServizioStandard_MUSEI(long idMuseo);

	List<Aeroporti> OrdinaAeroportiBy_Lat_Lng(double lat, double lng);

	List<PortiNavali> OrdinaPortiNavaliBy_Lat_Lng(double lat, double lng);

	List<Musei> OrdinaMuseiBy_Lat_Lng(double lat, double lng);
	
	public Object getInfrastrutturaBy_PlaceId(String PlaceId);

	
	List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolare(Long idUser, Long idRicercaTransfert);
	List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolareALL();

	List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedio(Long idUser, Long idRicercaTransfert);
	List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedioALL();
	
	List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolare(long idUser, String token);
	List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolareALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic);
	
	List<RichiestaMediaAutista> getCorseAutistaRichiestaAutistaMedio(long idUser, String token);
	List<GestioneCorseMedieAdmin> getCorseAutistaRichiestaAutistaMedioALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic) throws DataIntegrityViolationException, HibernateJdbcException;
	
	List<RichiestaMediaAutista> getCorsaMediaDisponibile_by_idAutista(long idUser, String token);

	RichiestaMediaAutista getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata(long idRic);

	List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente(int maxResults, Integer firstResult);
	
	int getCountRicercaTransfertSoloRicercheEseguiteCliente();

	RichiestaMediaAutista getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(long idTransfert);

	List<RicercaTransfert> getRicercaTransfertVenduti(Integer maxResult);

	Object getAeroporto_Musei_Porti_Province_LIKE_Url(String term);

	boolean CheckRecensioneApprovata_User(long idUser);

	List<Object[]> OrdinaPerRecencioniNonApprovate(Long idUser);

	List<Object[]> Recencioni_Approvate(boolean aziende);
	
	Object DammiTipoServizio(long idRicTransfert);


	


	


	


	

	


	


	


	


	


	


	

}

package com.apollon.dao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Aeroporti;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaMusei;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.Tariffe;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RicercaTransfertDao extends GenericDao<RicercaTransfert, Long> {
	
	//REPORT
	List<Object[]> report_1();
	List<Object[]> report_2();
	List<Object[]> report_2_bis_1();
	List<Object[]> report_3();
	List<Object[]> report_4();
	List<Object[]> report_5();
	List<RicercaTransfert> report_6();
	List<Object[]> report_7();
	//-----------------------------------------------
	
	
	
	RicercaTransfert get(Long id);
	RicercaTransfert getCollapsePanelCorsaAdmin(Long id);
	List<RicercaTransfert> getRicercaTransfert();
	RicercaTransfert saveRicercaTransfert(RicercaTransfert ricercaTransfert) throws DataIntegrityViolationException, HibernateJdbcException;
	List<RicercaTransfert> getRicercaTransfertBy_LIKE(String term);

	// QUERY FIND INFRASTRUTTURA
	Object getInfrastrutturaBy_PlaceId(String PlaceId);
	
	
	// INFRASTRUTTURE
	List<AutistaAeroporti> Ricerca_Autisti_ServizioStandard_AEROPORTI(long idAeroporto);
	List<AutistaPortiNavali> Ricerca_Autisti_ServizioStandard_PORTINAVALI(long idPorto);
	List<AutistaMusei> Ricerca_Autisti_ServizioStandard_MUSEI(long idMuseo);
	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(List<Long> listIdAeroporti, List<Long> listTipiAutoveicoli);
	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(List<Long> listIdporti, List<Long> listTipiAutoveicoli);
	
	// TARIFFE
	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE(List<Long> listIdZone, List<Long> listTipiAutoveicoli);
	List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE_LP(List<Long> listIdZone, List<Long> listTipiAutoveicoli, int max_km_lp);
	boolean Autoveicolo_Disponibe(long autoveicolo, List<Date> datePrelevRitorn);
	List<Aeroporti> OrdinaAeroportiBy_Lat_Lng(double lat, double lng);
	List<PortiNavali> OrdinaPortiNavaliBy_Lat_Lng(double lat, double lng);
	List<Musei> OrdinaMuseiBy_Lat_Lng(double lat, double lng);

	//-------- 
	List<RichiestaMediaAutista> getCorseAutistaMedioConfermate_DateRange(Long idAutista, RicercaTransfert ricTransfertTest, long oreRange);
	List<RichiestaAutistaParticolare> getCorseAutistaParticolareConfermate_DateRange(Long idAutista, RicercaTransfert ricTransfertTest, long oreRange);
	//-------- 
	
	List<RicercaTransfert> getCorseClienteAgendaAutista(Long idUser, Long idRicercaTransfert);
	List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedio(Long idUser, Long idRicercaTransfert);
	List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolare(Long idUser, Long idRicercaTransfert);
	RichiestaAutistaParticolare getCorsaAutRicTransfMultiploPrenotazione(String token);
	List<RicercaTransfert> getCorseClienteRichiestaAutistaMultiplo(Long idUser, Long idRicercaTransfert);
	List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaMultiplo(long idUser, String token);
	RichiestaAutistaParticolare getCorsaAutRicTransfPartPrenotazione(String token);
	List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolare(long idUser, String token);
	List<RichiestaMediaAutista> getCorseAutistaRichiestaAutistaMedio(long idUser, String token);
	
	List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolareALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic);
	List<GestioneCorseMedieAdmin> getCorseAutistaRichiestaAutistaMedioALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic) throws DataIntegrityViolationException, HibernateJdbcException;
	List<RicercaTransfert> getCorseAutistaRichiestaAutistaMultiplaALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic);
	List<RicercaTransfert> getCorseAutistaAgendaAutistaALL(boolean inAppr, boolean approv, boolean nonApprov,Date from, Date to, Long idRic);
	List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedioALL();
	List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolareALL();
	
	List<RichiestaMediaAutista> getCorsaMediaDisponibile_by_idAutista(long idUser, String token);
	RichiestaMediaAutista getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata(long idRic);
	boolean getCorsaMediaDisponibile_by_idRicerca(long idRicTransfert);
	List<RichiestaMediaAutista> getAutoveicoliUtilizzabiliAutistaMedio(long idRicTransfert, long idUser);
	int updateCollapsePanelCorsaAdmin(long id, boolean collapse);
	int updateApprovazioneCorsaRitornoAdmin(long id, int approv);
	int updateApprovazioneCorsaAndataAdmin(long id, int approv);

	/**
	 * Delete 
	 */
	RicercaTransfert deleteRicercaTransfert(long IdRicercaTransfert) throws Exception ;

	List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente(int maxResults, Integer firstResult);
	
	int getCountRicercaTransfertSoloRicercheEseguiteCliente();

	RichiestaMediaAutista getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(long idTransfert);

	List<RicercaTransfert> getRicercaTransfertVenduti(Integer maxResult);

	List<RicercaTransfert> getRicercaTransfertRicercatiInProvinciaAutista(List<String> listSiglaProvincieAutista);

	List<RicercaTransfert> UtimerRicercheTransfert();

	List<Object[]> Ricerca_Autisti_ServizioStandard_AUTO_HQL(long idComPart, long idProvPart, long idRegPart, long idComArr, long idProvArr, long idRegArr, 
			List<Long> provinceInfra, int numeroPasseggeri, boolean soloAzienda, boolean ritorno, Date dataOraPrelevamento, Date dataOraRitorno);

	List<Object[]> AutistiZoneItalia_ServizioStandard_AUTO_HQL();

	Object getAeroporto_Musei_Porti_Province_LIKE_Url(String term);
	
	// ricordati di cancellarlo
	List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST(int maxResults, Integer firstResult);
	List<RichiestaMedia> getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST_2(int maxResults, Integer firstResult, ClasseAutoveicolo classeAutoveicoloReale, String NomeProvincia);
	
	RicercaTransfert getInfoAutista_by_ClientePanel_AgendaAutista(Long idRicTransfert, int NumOreInfoAutista);
	
	List<RicercaTransfert> getRicercheTransfertVenditore(long idUserVenditore);
	RicercaTransfert getRicercaTransfertVenditore(long idUserVenditore, long idTransfert);

	List<RicercaTransfert> getTransferAcquistati_User_Approvati(long id);
	List<RicercaTransfert> getTransferAcquistati_User_Totali(long id);
	
	List<Object[]> getTransferAcquistati_Approvati_Distinct_User();
	List<Object[]> OrdinaPerRecencioniNonApprovate(Long idUser);
	
	boolean CheckRecensioneApprovata_User(long idUser);
	List<Object[]> Recencioni_Approvate(boolean aziende);
	
	boolean CheckRichiestaPreventivi_MAXinvii_IN24Ore(Date DataRicerca, String PhoneNumberCustomer, String RicTransfert_Email, Long RicTransfert_IdUser, 
			String IpAddess, int NumeroTentativiMassimo);
	
	Object DammiTipoServizio(long idRicTransfert);
	
	List<RicercaTransfert> getCorseAutistaAgendaAutista(long idUser);
	List<RicercaTransfert> getRicercaTransfert_idPaymentProvider_NotNull();
	
	
	
	
	
	

	
}

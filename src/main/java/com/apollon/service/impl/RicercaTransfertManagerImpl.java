package com.apollon.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

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
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RicercaTransfertManager")
public class RicercaTransfertManagerImpl extends GenericManagerImpl<RicercaTransfert, Long> implements RicercaTransfertManager {

	private RicercaTransfertDao ricercaTransfertDao;
	
	@Override
    @Autowired
	public void setRicercaTransfertDao(RicercaTransfertDao ricercaTransfertDao) {
		this.ricercaTransfertDao = ricercaTransfertDao;
	}

	@Override
	public RicercaTransfert getInfoAutista_by_ClientePanel_AgendaAutista(Long idRicTransfert, int NumOreInfoAutista){
		return this.ricercaTransfertDao.getInfoAutista_by_ClientePanel_AgendaAutista(idRicTransfert, NumOreInfoAutista);
	}
	
	@Override
	public RicercaTransfert getRicercaTransfertVenditore(long idUserVenditore, long idTransfert){
		return this.ricercaTransfertDao.getRicercaTransfertVenditore(idUserVenditore, idTransfert);
	}
	
	@Override
	public List<RicercaTransfert> getRicercheTransfertVenditore(long idUserVenditore){
		return this.ricercaTransfertDao.getRicercheTransfertVenditore(idUserVenditore);
	}
	
	
	@Override
	public List<RicercaTransfert> getRicercaTransfertVenduti(Integer maxResult){
		return this.ricercaTransfertDao.getRicercaTransfertVenduti(maxResult);
	}

	
	@Override
	public Object getInfrastrutturaBy_PlaceId(String PlaceId){
		return this.ricercaTransfertDao.getInfrastrutturaBy_PlaceId(PlaceId);
	}
	
	@Override
	public RicercaTransfert get(Long id) {
		return this.ricercaTransfertDao.get(id);
	}
	
	@Override
	public Object DammiTipoServizio(long idRicTransfert){
		return this.ricercaTransfertDao.DammiTipoServizio(idRicTransfert);
	}
	
	@Override
	public RicercaTransfert getCollapsePanelCorsaAdmin(Long id) {
		return this.ricercaTransfertDao.getCollapsePanelCorsaAdmin(id);
	}

	@Override
	public int updateCollapsePanelCorsaAdmin(long id, boolean collapse) {
		return this.ricercaTransfertDao.updateCollapsePanelCorsaAdmin(id, collapse);
	}
	
	@Override
	public int updateApprovazioneCorsaAndataAdmin(long id, int approv) {
		return this.ricercaTransfertDao.updateApprovazioneCorsaAndataAdmin(id, approv);
	}
	
	@Override
	public int updateApprovazioneCorsaRitornoAdmin(long id, int approv) {
		return this.ricercaTransfertDao.updateApprovazioneCorsaRitornoAdmin(id, approv);
	}
	
	
	//--------------------
	
	
	@Override
	public List<RichiestaMediaAutista> getCorsaMediaDisponibile_by_idAutista(long idUser, String token){
		return this.ricercaTransfertDao.getCorsaMediaDisponibile_by_idAutista(idUser, token);
	}
	
	@Override
	public List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolare(long idUser, String token){
		return this.ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolare(idUser, token);
	}
	
	@Override
	public List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolareALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic){
		return this.ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolareALL(inAppr, approv, nonApprov, from, to, idRic);
	}
	
	@Override
	public List<RichiestaMediaAutista> getCorseAutistaRichiestaAutistaMedio(long idUser, String token){
		return this.ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedio(idUser, token);
	}
	
	@Override
	public RichiestaMediaAutista getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(long idTransfert){
		return this.ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(idTransfert);
	}
	
	@Override
	public List<GestioneCorseMedieAdmin> getCorseAutistaRichiestaAutistaMedioALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic) throws DataIntegrityViolationException, HibernateJdbcException{
		return this.ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedioALL(inAppr, approv, nonApprov, from, to, idRic);
	}
	
	@Override
	public List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolare(Long idUser, Long idRicercaTransfert){
		return this.ricercaTransfertDao.getCorseClienteRichiestaAutistaParticolare(idUser, idRicercaTransfert);
	}
	
	@Override
	public List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolareALL(){
		return this.ricercaTransfertDao.getCorseClienteRichiestaAutistaParticolareALL();
	}
	
	@Override
	public List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedio(Long idUser, Long idRicercaTransfert){
		return this.ricercaTransfertDao.getCorseClienteRichiestaAutistaMedio(idUser, idRicercaTransfert);
	}
	
	@Override
	public List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedioALL(){
		return this.ricercaTransfertDao.getCorseClienteRichiestaAutistaMedioALL();
	}
	
	@Override
	public RichiestaMediaAutista getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata(long idRic){
		return this.ricercaTransfertDao.getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata(idRic);
	}
	
	//--------------------
	
	@Override
	public List<RicercaTransfert> getRicercaTransfert() {
		return ricercaTransfertDao.getRicercaTransfert();
	}
	
	@Override
	public List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente(int maxResults, Integer firstResult) {
		return ricercaTransfertDao.getRicercaTransfertSoloRicercheEseguiteCliente(maxResults, firstResult);
	}
	
	@Override
	public int getCountRicercaTransfertSoloRicercheEseguiteCliente(){
		return ricercaTransfertDao.getCountRicercaTransfertSoloRicercheEseguiteCliente();
	}
	
	@Override
	public List<RicercaTransfert> getRicercaTransfertBy_LIKE(String term){
		return ricercaTransfertDao.getRicercaTransfertBy_LIKE(term);
	}
	
	//--------------------INFRASTRUTTURE-----------
	
	@Override
	public Object getAeroporto_Musei_Porti_Province_LIKE_Url(String term){
		return ricercaTransfertDao.getAeroporto_Musei_Porti_Province_LIKE_Url(term);
	}
	
	@Override
	public List<AutistaAeroporti> Ricerca_Autisti_ServizioStandard_AEROPORTI(long idAeroporto){
		return ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_AEROPORTI(idAeroporto);
	}
	
	@Override
	public List<AutistaPortiNavali> Ricerca_Autisti_ServizioStandard_PORTINAVALI(long idPorto){
		return ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_PORTINAVALI(idPorto);
	}
	
	@Override
	public List<AutistaMusei> Ricerca_Autisti_ServizioStandard_MUSEI(long idMuseo){
		return ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_MUSEI(idMuseo);
	}
	
	@Override
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE(List<Long> listIdZone, List<Long> listTipiAutoveicoli){
		return ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_ZONE(listIdZone, listTipiAutoveicoli);
	}
	
	@Override
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE_LP(List<Long> listIdZone, List<Long> listTipiAutoveicoli, int max_km_lp){
		return ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_ZONE_LP(listIdZone, listTipiAutoveicoli, max_km_lp);
	}

	@Override
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(List<Long> listIdAeroporti, List<Long> listTipiAutoveicoli){
		return ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(listIdAeroporti, listTipiAutoveicoli);
	}
	
	@Override
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(List<Long> listIdporti, List<Long> listTipiAutoveicoli){
		return ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(listIdporti, listTipiAutoveicoli);
	}
	
	@Override
	public boolean Autoveicolo_Disponibe(long autoveicolo, List<Date> datePrelevRitorn){
		return ricercaTransfertDao.Autoveicolo_Disponibe(autoveicolo, datePrelevRitorn);
	}
	
	@Override
	public List<Aeroporti> OrdinaAeroportiBy_Lat_Lng(double lat, double lng){
		return ricercaTransfertDao.OrdinaAeroportiBy_Lat_Lng(lat, lng);
	}
	
	@Override
	public List<PortiNavali> OrdinaPortiNavaliBy_Lat_Lng(double lat, double lng){
		return ricercaTransfertDao.OrdinaPortiNavaliBy_Lat_Lng(lat, lng);
	};
	
	@Override
	public List<Musei> OrdinaMuseiBy_Lat_Lng(double lat, double lng){
		return ricercaTransfertDao.OrdinaMuseiBy_Lat_Lng(lat, lng);
	}
	
	@Override
	public boolean CheckRecensioneApprovata_User(long idUser){
		return ricercaTransfertDao.CheckRecensioneApprovata_User(idUser);
	}
	
	@Override
	public List<Object[]> OrdinaPerRecencioniNonApprovate(Long idUser){ 
		return ricercaTransfertDao.OrdinaPerRecencioniNonApprovate(idUser);
	}
	
	
	@Override
	public List<Object[]> Recencioni_Approvate(boolean aziende){
		return ricercaTransfertDao.Recencioni_Approvate(aziende);
	}
	
	
	@Override
	public RicercaTransfert saveRicercaTransfert(RicercaTransfert ricercaTransfert) throws Exception {
		return ricercaTransfertDao.saveRicercaTransfert(ricercaTransfert);
	}

	
	@Override
    public void removeRicercaTransfert(long id) {
		ricercaTransfertDao.remove(id);
    }



	
	
	
	
}

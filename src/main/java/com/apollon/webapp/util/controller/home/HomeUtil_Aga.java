package com.apollon.webapp.util.controller.home;

import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.apollon.dao.AgA_TariffariDao;
import com.apollon.dao.ComuniDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.Comuni;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultAgendaAutista;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class HomeUtil_Aga extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(HomeUtil_Aga.class);
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	private static ComuniDao comuniDao = (ComuniDao) contextDao.getBean("ComuniDao");
	private static AgA_TariffariDao agA_TariffariDao = (AgA_TariffariDao) contextDao.getBean("AgA_TariffariDao");
	
	
	public static RicercaTransfert AgendaAutista_InvertiAndataRitorno_CancellaRitorno(RicercaTransfert ricTransfert ) throws CloneNotSupportedException {
		RicercaTransfert OLD_ricTransfert = (RicercaTransfert)ricTransfert.clone();
		ricTransfert.setPartenzaRequest(ricTransfert.getArrivoRequest());
		ricTransfert.setArrivoRequest( OLD_ricTransfert.getPartenzaRequest() );
		ricTransfert.setComune_Partenza(ricTransfert.getComune_Arrivo());
		ricTransfert.setComune_Arrivo( OLD_ricTransfert.getComune_Partenza() );
		ricTransfert.setDataOraPrelevamento(ricTransfert.getDataOraRitorno());
		ricTransfert.setDataOraRitorno( OLD_ricTransfert.getDataOraPrelevamento() );
		ricTransfert.setDataOraPrelevamentoDate(ricTransfert.getDataOraRitornoDate());
		ricTransfert.setDataOraRitornoDate( OLD_ricTransfert.getDataOraPrelevamentoDate() );
		ricTransfert.setDistanzaText(ricTransfert.getDistanzaTextRitorno());
		ricTransfert.setDistanzaTextRitorno( OLD_ricTransfert.getDistanzaText() );
		ricTransfert.setDistanzaValue(ricTransfert.getDistanzaValueRitorno());
		ricTransfert.setDistanzaValueRitorno( OLD_ricTransfert.getDistanzaValue() );
		ricTransfert.setDurataConTrafficoText(ricTransfert.getDurataConTrafficoTextRitorno());
		ricTransfert.setDurataConTrafficoTextRitorno( OLD_ricTransfert.getDurataConTrafficoText() );
		ricTransfert.setDurataConTrafficoValue(ricTransfert.getDurataConTrafficoValueRitorno());
		ricTransfert.setDurataConTrafficoValueRitorno( OLD_ricTransfert.getDurataConTrafficoValue() );
		ricTransfert.setDurataValue(ricTransfert.getDurataValueRitorno());
		ricTransfert.setDurataValueRitorno( OLD_ricTransfert.getDurataValue() );
		ricTransfert.setDurataText(ricTransfert.getDurataTextRitorno());
		ricTransfert.setDurataTextRitorno( OLD_ricTransfert.getDurataText() );
		ricTransfert.setFormattedAddress_Partenza(ricTransfert.getFormattedAddress_Arrivo());
		ricTransfert.setFormattedAddress_Arrivo( OLD_ricTransfert.getFormattedAddress_Partenza() );
		ricTransfert.setLat_Partenza(ricTransfert.getLat_Arrivo());
		ricTransfert.setLat_Arrivo( OLD_ricTransfert.getLat_Partenza() );
		ricTransfert.setLng_Partenza(ricTransfert.getLng_Arrivo());
		ricTransfert.setLng_Arrivo( OLD_ricTransfert.getLng_Partenza() );
		ricTransfert.setName_Partenza(ricTransfert.getName_Arrivo());
		ricTransfert.setName_Arrivo( OLD_ricTransfert.getName_Partenza() );
		ricTransfert.setOraPrelevamento(ricTransfert.getOraRitorno());
		ricTransfert.setOraRitorno( OLD_ricTransfert.getOraPrelevamento() );
		ricTransfert.setPlace_id_Partenza(ricTransfert.getPlace_id_Arrivo());
		ricTransfert.setPlace_id_Arrivo( OLD_ricTransfert.getPlace_id_Partenza() );
		ricTransfert.setSiglaProvicia_Partenza(ricTransfert.getSiglaProvicia_Arrivo());
		ricTransfert.setSiglaProvicia_Arrivo( OLD_ricTransfert.getSiglaProvicia_Partenza() );
		ricTransfert.setRitorno(false);
		return ricTransfert;
	}
	
	
	public static ResultAgendaAutista Ricerca_AgendaAutista(long idRicTransfert) throws Exception {
		RicercaTransfert ricTransfert = ricercaTransfertDao.get(idRicTransfert);
		ResultAgendaAutista resultAgendaAutista = new ResultAgendaAutista();
		Comuni comunePartenza = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Partenza(), ricTransfert.getSiglaProvicia_Partenza() );
		Comuni comuneArrivo = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Arrivo(), ricTransfert.getSiglaProvicia_Arrivo() );
		final long PercentualeServizio = CalcoloPrezzi.CalcolaPercentualeMedia_TraDueProvince(comunePartenza.getProvince().getPercentualeServizio(), 
				comuneArrivo.getProvince().getPercentualeServizio());
		List<AgendaAutista_Autista> agendaAutistaA_AndataList = autoveicoloDao.Result_AgendaAutista(PercentualeServizio, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDistanzaValue(), 
				ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza(), ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo());
		resultAgendaAutista.setAgendaAutista_AutistaAndata(agendaAutistaA_AndataList);
		if(ricTransfert.isRitorno()) {
			List<AgendaAutista_Autista> agendaAutistaA_RitornoList = autoveicoloDao.Result_AgendaAutista(PercentualeServizio, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraRitornoDate(), ricTransfert.getDistanzaValueRitorno(), 
					ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo(), ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza());
			resultAgendaAutista.setAgendaAutista_AutistaRitorno(agendaAutistaA_RitornoList);
		}
		return resultAgendaAutista;
	}
	
	
	public static AgendaAutistaScelta ResultAgendaAutista_From_Tariffari_CalcolaPercentuale(long idRicTransfert, Long TariffarioAndata, Long TariffarioRitorno) {
		try {
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(idRicTransfert);
			AgendaAutistaScelta agendaAutistaScelta = new AgendaAutistaScelta();
			Comuni comunePartenza = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Partenza(), ricTransfert.getSiglaProvicia_Partenza() );
			Comuni comuneArrivo = comuniDao.getComuniByNomeComune_Equal( ricTransfert.getComune_Arrivo(), ricTransfert.getSiglaProvicia_Arrivo() );
			final long PercentualeServizio = CalcoloPrezzi.CalcolaPercentualeMedia_TraDueProvince(comunePartenza.getProvince().getPercentualeServizio(), 
					comuneArrivo.getProvince().getPercentualeServizio());
			agendaAutistaScelta.setAgendaAutista_AutistaAndata( AgendaAutista_Autista_FROM_Tariffario(TariffarioAndata, PercentualeServizio, true, ricTransfert) );
			agendaAutistaScelta.setAgendaAutista_AutistaRitorno( AgendaAutista_Autista_FROM_Tariffario(TariffarioRitorno, PercentualeServizio, false, ricTransfert) );
			agendaAutistaScelta.setPercentualeServizio(PercentualeServizio);
			return agendaAutistaScelta;
		}catch(Exception exc) {
			exc.printStackTrace();
			return 	null;
		}
	}
	
	
	public static AgendaAutistaScelta ResultAgendaAutista_From_Tariffari(long idRicTransfert, Long TariffarioAndata, Long TariffarioRitorno, long PercentualeServizio) {
		try {
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(idRicTransfert);
			AgendaAutistaScelta agendaAutistaScelta = new AgendaAutistaScelta();
			agendaAutistaScelta.setAgendaAutista_AutistaAndata( AgendaAutista_Autista_FROM_Tariffario(TariffarioAndata, PercentualeServizio, true, ricTransfert) );
			agendaAutistaScelta.setAgendaAutista_AutistaRitorno( AgendaAutista_Autista_FROM_Tariffario(TariffarioRitorno, PercentualeServizio, false, ricTransfert) );
			agendaAutistaScelta.setPercentualeServizio(PercentualeServizio);
			return agendaAutistaScelta;
		}catch(Exception exc) {
			exc.printStackTrace();
			return 	null;
		}
	}
	
	private static AgendaAutista_Autista AgendaAutista_Autista_FROM_Tariffario(Long idTariffario, long PercentualeServizio, boolean Andata, RicercaTransfert ricTransfert) throws Exception {
		if( idTariffario != null ) {
			AgA_Tariffari tariffario = agA_TariffariDao.get(idTariffario);
			Autoveicolo autoveicolo = tariffario.getAgA_Giornate().getAutoveicolo();
			Autista autista = autoveicolo.getAutista();
			User user = autista.getUser();
			double lat_Partenza = (Andata ? ricTransfert.getLat_Partenza() : ricTransfert.getLat_Arrivo());
			double lng_Partenza = (Andata ? ricTransfert.getLng_Partenza() : ricTransfert.getLng_Arrivo());
			double lat_Arrivo = (Andata ? ricTransfert.getLat_Arrivo() : ricTransfert.getLat_Partenza());
			double lng_Arrivo = (Andata ? ricTransfert.getLng_Arrivo() : ricTransfert.getLng_Partenza());
			AgendaAutista_Autista agendaAutista_Autista = new AgendaAutista_Autista(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), 
					autista.getId(), autista.getAutistaDocumento().getPartitaIvaDenominazione(), autista.isAzienda(), autista.getNumCorseEseguite(), autista.getNote(), 
					autoveicolo.getId(), autoveicolo.getAnnoImmatricolazione(), autoveicolo.getTarga(), autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getName(), 
					autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName(), 
					autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId(), 
					autoveicolo.getModelloAutoNumeroPosti().getNumeroPostiAuto().getNumero(), tariffario.getAgA_Giornate().getId(), tariffario.getAgA_Giornate().getDataGiornataOrario(), 
					tariffario.getId(), tariffario.getKmCorsa(), tariffario.getPrezzoCorsa(), tariffario.getKmRaggioArea(),
					AutoveicoloUtil.DammiAutoClasseReale(autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getClasseAutoveicolo().getId(), 
							autoveicolo.getAnnoImmatricolazione()), lat_Partenza, lng_Partenza, lat_Arrivo, lng_Arrivo, tariffario.getRicercaTransfertAcquistato());
			agendaAutista_Autista = AggiungiPrezzi_ad_AgendaAutista_Autista(agendaAutista_Autista, PercentualeServizio);
			return agendaAutista_Autista;
		}else {
			return null;
		}
	}
	
	
	public static AgendaAutista_Autista AggiungiPrezzi_ad_AgendaAutista_Autista(AgendaAutista_Autista agendaAutista_Autista, long PercentualeServizio) throws Exception {
		BigDecimal PrezzoCommissioneServizio = CalcoloPrezzi.CalcolaPercentuale(agendaAutista_Autista.getPrezzoCorsa(), (int)(long)PercentualeServizio);
		BigDecimal PrezzoCommissioneServizioIva = CalcoloPrezzi.CalcolaPrezzoIva(PrezzoCommissioneServizio);
		agendaAutista_Autista.setPercentualeServizio((int)(long)PercentualeServizio);
		agendaAutista_Autista.setPrezzoCommissioneServizio(PrezzoCommissioneServizio);
		agendaAutista_Autista.setPrezzoCommissioneServizioIva(PrezzoCommissioneServizioIva);
	    agendaAutista_Autista.setPrezzoCliente( agendaAutista_Autista.getPrezzoCorsa().add(PrezzoCommissioneServizio).add(PrezzoCommissioneServizioIva) );
	    return agendaAutista_Autista;
	}
	
	
	
	
}

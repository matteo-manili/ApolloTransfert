package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.util.NumberUtil;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.bean.TransferTariffe;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
import com.apollon.webapp.util.bean.RisultatoAutistiParticolare;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.MessaggioEsitoRicerca;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultAgendaAutista;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultMedio;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Aga;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name="ricerca_transfert")
public class RicercaTransfert extends BaseObject implements Serializable, Cloneable {
	private static final long serialVersionUID = 7678569597594354857L;
	private Long id;
	private String infoPasseggero;
	private List<MessaggioEsitoRicerca> messaggiEsitoRicerca;
	private boolean scontoRitorno;
	private String notePerAutista;
	private boolean collapsePanelCorseAdmin;
	private int approvazioneAndata;
	private int approvazioneRitorno;
	private String note;
	private boolean pagamentoEseguitoMedio;
	private boolean pagamentoParziale;
	// verifica phone Customer
	private String phoneNumberCustomer;
	private boolean invioSmsCustomer;
	private String tokenCustomer;
	private boolean verificatoCustomer;
	private boolean riepilogo;
	private boolean prenotazione;
	private boolean ricercaRiuscita; //non lo uso più lo ho integrato in getResultAutistaTariffePrezzo()
	private String tipoServizio;
	private String codiceAeroportuale;
	private String descrizioneMuseo;
	private int numeroPasseggeri;
	private Date dataRicerca;
	private boolean ritorno;
	private String dataOraPrelevamento;
	private String dataOraRitorno;
	private String oraPrelevamento;
	private String oraRitorno;
	private Date dataOraPrelevamentoDate;
	private Date dataOraRitornoDate;
	private String partenzaRequest;
	private String arrivoRequest;
	
	// partenze google place
	private String place_id_Partenza;
	private String formattedAddress_Partenza;
	private String name_Partenza;
	private double lat_Partenza;
	private double lng_Partenza;
	private String comune_Partenza;
	private String siglaProvicia_Partenza;
	private List<String> listTypes_Partenza;
	
	// arrivo google place
	private String place_id_Arrivo;
	private String formattedAddress_Arrivo;
	private String name_Arrivo;
	private double lat_Arrivo;
	private double lng_Arrivo;
	private String comune_Arrivo;
	private String siglaProvicia_Arrivo;
	private List<String> listTypes_Arrivo;
	
	// distanza e durata google place
	private String distanzaText;
	private long distanzaValue;
	private String durataText;
	private long durataValue;
	private String durataConTrafficoText;
	private long durataConTrafficoValue;
	
	// distanza e durata google place Ritorno
	private String distanzaTextRitorno;
	private long distanzaValueRitorno;
	private String durataTextRitorno;
	private long durataValueRitorno;
	private String durataConTrafficoTextRitorno;
	private long durataConTrafficoValueRitorno;
	
	//------------------------------------------------
	
	@Transient
	public BigDecimal getAgendaAutista_RimborsoCliente() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); 
			return json.getBigDecimal(Constants.AgendaAutista_RimborsoCliente).setScale(2, RoundingMode.HALF_EVEN);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getAgendaAutista_Percentuale_Servizio() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.AgendaAutista_Percentuale_Servizio);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getAgendaAutista_TariffarioAndata_Id() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.AgendaAutista_TariffarioId_Andata);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getAgendaAutista_TariffarioRitorno_Id() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.AgendaAutista_TariffarioId_Ritorno);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public BigDecimal getAgendaAutista_PrezzoTotaleCliente_Temp() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); 
			return json.getBigDecimal(Constants.AgendaAutista_PrezzoTotaleCliente_Temp).setScale(2, RoundingMode.HALF_EVEN);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public BigDecimal getRichiestaAutistaMultiploRimborsoCliente() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getBigDecimal(Constants.RichiestaAutistaMultiploRimborsoCliente);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public JSONArray getRichiestaAutistaMultiplo_Id() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getJSONArray(Constants.RichiestaAutistaMultiplo_Id);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Boolean getCancellaRicezionePreventiviCliente() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getBoolean(Constants.CancellaRicezionePreventiviCliente);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getRichiestaAutistaParticolare_Id() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.RichiestaAutistaParticolare_Id);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Boolean getRichiestaPreventivi_Inviata() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getBoolean(Constants.RichiestaPreventivi_Inviata);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getRicTransfert_IdUser() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.RicTransfert_IdUser);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null;}
	}
	
	@Transient
	public String getRicTransfert_Email() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_Email);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getRicTransfert_Nome() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_Nome);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getRicTransfert_Cognome() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_Cognome);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getRicTransfert_Token() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_Token);
			}catch(JSONException JsonExc){ return null; } 
		}else{ return null; }
	}
	
	@Transient
	public String getRicTransfert_IpAddress() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_IpAddress);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getRicTransfert_Address() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RicTransfert_Address);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}

	@Transient
	public String getNomeTelefonoPasseggero() {
		if(this.infoPasseggero != null){
			try{
				JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.NomePasseggeroJSON)+" "+json.getString(Constants.TelefonoPasseggeroJSON);
			}catch(JSONException JsonExc){ return null; } 
		}else{ return null; }
	}
	
	@Transient
	public String getNomePasseggero() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.NomePasseggeroJSON);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getTelefonoPasseggero() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.TelefonoPasseggeroJSON);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public BigDecimal getVecchioPrezzo() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getBigDecimal(Constants.VecchioPrezzoJSON);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getCodiceSconto() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.CodiceScontoJSON);
			}catch(JSONException JsonExc ){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Integer getPercentualeSconto() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getInt(Constants.PercentualeScontoJSON);
			}catch(JSONException JsonExc ){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getRecensione() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getString(Constants.RecensioneJSON);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Integer getPunteggioStelleRecensione() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getInt(Constants.PunteggioStelleRecensioneJSON);
			}catch(JSONException JsonExc){ return null; } 
		}else{ return null; }
	}
	
	@Transient
	public Boolean getRecensioneApprovata() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero);return json.getBoolean(Constants.RecensioneApprovataJSON);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null;}
	}
	
	@Transient
	public JSONArray getSupplementi_Id() {
		if(this.infoPasseggero != null){
			try{JSONObject json = new JSONObject(this.infoPasseggero); return json.getJSONArray(Constants.Supplementi_Id);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Long getRitardo() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getLong(Constants.Ritardo_Id);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public InfoPaymentProvider getProviderPagamentoInfo() {
		if(this.infoPasseggero != null){
			try{
				JSONObject json = new JSONObject(this.infoPasseggero);
				InfoPaymentProvider infoPaymentProvider = new InfoPaymentProvider(
						json.getString(Constants.PaymentProviderIdJSON),json.getString(Constants.PaymentProviderTipoJSON),
						json.getString(Constants.PaymentProviderNomeClienteJSON), json.getBigDecimal(Constants.PaymentProviderRefundJSON), 
						json.getBigDecimal(Constants.PaymentProviderAmountJSON), json.getBigDecimal(Constants.PaymentProviderFeeJSON) );
				return infoPaymentProvider;
			}catch(JSONException JsonExc) {
				JSONObject json = new JSONObject(this.infoPasseggero);
				return RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(this.id, json.getString(Constants.PaymentProviderIdJSON), json.getString(Constants.PaymentProviderTipoJSON));
			}
		}else {
			JSONObject json = new JSONObject(this.infoPasseggero);
			return RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(this.id, json.getString(Constants.PaymentProviderIdJSON), json.getString(Constants.PaymentProviderTipoJSON));
		}
	}
	
	@Transient
	public JSONArray getProvinceTragitto_Id() {
		if(this.infoPasseggero != null){
			try{ JSONObject json = new JSONObject(this.infoPasseggero); return json.getJSONArray(Constants.ProvinceTragitto_Id);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	private TransferTariffe transferTariffe;
	@Transient
	public TransferTariffe getTransferTariffe() {
		return transferTariffe;
	}
	@Transient
	public void setTransferTariffe(TransferTariffe transferTariffe) {
		this.transferTariffe = transferTariffe;
	}
	
	@Transient
	public List<String> getListTypes_Partenza() {
		return listTypes_Partenza;
	}
	@Transient
	public void setListTypes_Partenza(List<String> listTypes_Partenza) {
		this.listTypes_Partenza = listTypes_Partenza;
	}

	@Transient
	public List<String> getListTypes_Arrivo() {
		return listTypes_Arrivo;
	}
	@Transient
	public void setListTypes_Arrivo(List<String> listTypes_Arrivo) {
		this.listTypes_Arrivo = listTypes_Arrivo;
	}

	@Transient
	public boolean isRicercaRiuscita() {
		return ricercaRiuscita;
	}
	@Transient
	public void setRicercaRiuscita(boolean ricercaRiuscita) {
		this.ricercaRiuscita = ricercaRiuscita;
	}
	
	@Transient
	public List<MessaggioEsitoRicerca> getMessaggiEsitoRicerca() {
		return messaggiEsitoRicerca;
	}
	@Transient
	public void setMessaggiEsitoRicerca(List<MessaggioEsitoRicerca> messaggiEsitoRicerca) {
		this.messaggiEsitoRicerca = messaggiEsitoRicerca;
	}

	@Transient
	public boolean isPrenotazione() {
		return prenotazione;
	}
	@Transient
	public void setPrenotazione(boolean prenotazione) {
		this.prenotazione = prenotazione;
	}

	@Transient
	public boolean isRiepilogo() {
		return riepilogo;
	}
	@Transient
	public void setRiepilogo(boolean riepilogo) {
		this.riepilogo = riepilogo;
	}
	
	private User user;
	@ManyToOne
	@JoinColumn(name = "id", nullable = true)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	private Visitatori visitatore;
	@ManyToOne //(fetch = FetchType.LAZY) // TODO provare a mettere in lazy ma prima controllare tutto, si risparmia in memoria
	@JoinColumn(name = "id_visitatori", nullable = true)
	public Visitatori getVisitatore() {
		return visitatore;
	}
	public void setVisitatore(Visitatori visitatore) {
		this.visitatore = visitatore;
	}
	
	private User userVenditore;
	@ManyToOne
	@JoinColumn(name = "id_venditore", nullable = true)
	public User getUserVenditore() {
		return userVenditore;
	}
	public void setUserVenditore(User userVenditore) {
		this.userVenditore = userVenditore;
	}
	
	private Set<RichiestaMedia> richiestaMedia;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ricercaTransfert")
	@Fetch(FetchMode.SELECT)
	@OrderBy("classeAutoveicolo ASC")
	public Set<RichiestaMedia> getRichiestaMedia() {
		return richiestaMedia;
	}
	public void setRichiestaMedia(Set<RichiestaMedia> richiestaMedia) {
		this.richiestaMedia = richiestaMedia;
	}

	private Set<RichiestaAutistaParticolare> richiestaAutistaParticolare;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ricercaTransfert" )
	@Fetch(FetchMode.SELECT)
	public Set<RichiestaAutistaParticolare> getRichiestaAutistaParticolare() {
		return richiestaAutistaParticolare;
	}
	public void setRichiestaAutistaParticolare(Set<RichiestaAutistaParticolare> richiestaAutistaParticolare) {
		this.richiestaAutistaParticolare = richiestaAutistaParticolare;
	}
	
	
	/**
	 * CARICAMENTO DATI AGENDA_AUTISTA DATABASE --> BEAN
	 */
	@Transient
	private AgendaAutistaScelta agendaAutistaScelta;
	@Transient
	public AgendaAutistaScelta getAgendaAutistaScelta() {
		return HomeUtil_Aga.ResultAgendaAutista_From_Tariffari(this.id, this.getAgendaAutista_TariffarioAndata_Id(), this.getAgendaAutista_TariffarioRitorno_Id(), 
				this.getAgendaAutista_Percentuale_Servizio());
	}

	/**
	 * CARICAMENTO DATI MEDIO DATABASE --> BEAN
	 */
	@Transient
	private ResultAgendaAutista resultAgendaAutista;
	@Transient
	public ResultAgendaAutista getResultAgendaAutista() throws Exception {
		return HomeUtil_Aga.Ricerca_AgendaAutista(this.id);
	}
	

	@Transient
	private RichiestaAutistaParticolare richiestaAutistaParticolareAcquistato;
	@Transient
	public RichiestaAutistaParticolare getRichiestaAutistaParticolareAcquistato() {
		if(this.richiestaAutistaParticolare != null && this.richiestaAutistaParticolare.size() > 0 && this.getRichiestaAutistaParticolare_Id() != null) {
			for(RichiestaAutistaParticolare richiestaAutistaParticolare_ite: this.richiestaAutistaParticolare){
				if((long)richiestaAutistaParticolare_ite.getId() == (long)this.getRichiestaAutistaParticolare_Id()){
					return richiestaAutistaParticolare_ite;
				}
			}
		}
		return null;
	}

	@Transient
	private List<RichiestaAutistaParticolare> richiestaAutistaParticolareAcquistato_Multiplo;
	@Transient
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareAcquistato_Multiplo() {
		if(this.richiestaAutistaParticolare != null && this.richiestaAutistaParticolare.size() > 0 && this.getRichiestaAutistaMultiplo_Id() != null) {
			List<Long> List_IdRichiestaAutistaParticolare = new ArrayList<Long>();
			for (int i = 0; i < this.getRichiestaAutistaMultiplo_Id().length(); i++){ 
				List_IdRichiestaAutistaParticolare.add(this.getRichiestaAutistaMultiplo_Id().getLong(i));
			} 
			List<RichiestaAutistaParticolare> richiestaAutistaParticolareAcquistato_Multiplo_List = new ArrayList<RichiestaAutistaParticolare>();
			for(RichiestaAutistaParticolare richiestaAutistaParticolare_ite: this.richiestaAutistaParticolare){
				if( List_IdRichiestaAutistaParticolare.contains((long)richiestaAutistaParticolare_ite.getId()) ) {
					richiestaAutistaParticolareAcquistato_Multiplo_List.add(richiestaAutistaParticolare_ite);
				}
			}
			return richiestaAutistaParticolareAcquistato_Multiplo_List;
		}
		return null;
	}

	
	@Transient
	private RisultatoAutistiParticolare risultatoAutistiParticolare;
	
	@Transient
	public RisultatoAutistiParticolare getRisultatoAutistiParticolare() {
		if( resultAutistaTariffePrezzo != null ) {
			String[] ParametriCorseParticolari = ApplicationUtils.ParametriCorseParticolari;
			int percentualeServizio = 0;
			if( this.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {
				percentualeServizio = Integer.parseInt(ParametriCorseParticolari[0]); 
			}else if( this.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				percentualeServizio = Integer.parseInt(ParametriCorseParticolari[1]);
			}
			RisultatoAutistiParticolare resultAutistiParticolare = new RisultatoAutistiParticolare();
			List<RisultatoAutistiParticolare.ResultParticolare> resultParticolareList = new ArrayList<RisultatoAutistiParticolare.ResultParticolare>();
			for(ResultRicerca_Autista_Tariffe.ResultMedio resultMedio_ite: resultAutistaTariffePrezzo.getResultMedio()) {
				for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista RIS_AUTISTA_ITE: resultMedio_ite.getResultMedioAutista()) {
					for(ResultRicerca_Autista_Tariffe.ResultMedio.ResultMedioAutista.RisultAutistaMedioAutoveicolo RIS_AUTO_ITE: RIS_AUTISTA_ITE.getRisultAutistaMedioAutoveicolo()) {
						RisultatoAutistiParticolare.ResultParticolare resultParticolare = new RisultatoAutistiParticolare.ResultParticolare();
						resultParticolare.setClasseAutoveicoloScelta(resultMedio_ite.getClasseAutoveicolo());
						User user = new User(); user.setFirstName( RIS_AUTO_ITE.getFirstName() );
						Autista autista = new Autista(); autista.setId(RIS_AUTO_ITE.getIdAutista()); autista.setUser(user);
						MarcaAutoScout marcaAutoScout = new MarcaAutoScout(); marcaAutoScout.setName( RIS_AUTO_ITE.getNomeMarcaAuto() );
						ModelloAutoScout modelloAutoScout = new ModelloAutoScout(); modelloAutoScout.setMarcaAutoScout(marcaAutoScout);
						ModelloAutoNumeroPosti modelloAutoNumeroPosti = new ModelloAutoNumeroPosti(); modelloAutoNumeroPosti.setModelloAutoScout(modelloAutoScout);
						Autoveicolo auto = RIS_AUTO_ITE.getAutoveicolo(); auto.setAutista(autista); auto.setModelloAutoNumeroPosti(modelloAutoNumeroPosti);
						resultParticolare.setAutoveicolo( auto );
						resultParticolare.setRimborsoCliente(null);
						resultParticolare.setPercentualeServizio(percentualeServizio);
						resultParticolare.setPrezzoCommissioneServizio(new BigDecimal("0"));
						resultParticolare.setPrezzoCommissioneServizioIva(new BigDecimal("0"));
						resultParticolare.setPrezzoTotaleAutista(new BigDecimal("0"));
						resultParticolare.setPrezzoTotaleCliente(new BigDecimal("0"));
						resultParticolare.setInvioSmsCorsaConfermata(false);
						resultParticolare.setDataChiamataPrenotata(null);
						resultParticolare.setInvioSms(false);
						resultParticolare.setToken(null);
						resultParticolareList.add(resultParticolare);
					}
				}
			}
			List<Autista> autistiEffettiviList = new ArrayList<Autista>();
			List<Long> idAutistiList = new LinkedList<Long>();
			for(RisultatoAutistiParticolare.ResultParticolare ite: resultParticolareList) {
				if( !idAutistiList.contains( ite.getAutoveicolo().getAutista().getId() ) ) {
					autistiEffettiviList.add( ite.getAutoveicolo().getAutista() );
					idAutistiList.add( ite.getAutoveicolo().getAutista().getId() );
				}
			}
			resultAutistiParticolare.setResultParticolare(resultParticolareList);
			resultAutistiParticolare.setAutistiEffettivi( autistiEffettiviList );
			return resultAutistiParticolare;
		}else {
			return HomeUtil.getRisultatoAutistiParticolare(this.id);
		}
	}
	
	@Transient
	public void setRisultatoAutistiParticolare(RisultatoAutistiParticolare risultatoAutistiParticolare) {
		this.risultatoAutistiParticolare = risultatoAutistiParticolare;
	}

	
	@Transient
	private ResultRicerca_Autista_Tariffe resultAutistaTariffePrezzo;

	@Transient
	public ResultRicerca_Autista_Tariffe getResultAutistaTariffePrezzo() {
		if( resultAutistaTariffePrezzo != null ) {
			final int NumMinimoAutistiCorsaMedia = ApplicationUtils.DammiNumMinimoAutistiCorsaMedia();
			int totaleShowClasseAutoveicolo = 0;
			for(ResultMedio AAA : resultAutistaTariffePrezzo.getResultMedio()  ) {
				List<Long> autoList_id = new ArrayList<Long>();
				for(ResultMedioAutista BBB: AAA.getResultMedioAutista()  ) {
					autoList_id.add( BBB.getAutista().getId() );
				}
				if(autoList_id.size() >= NumMinimoAutistiCorsaMedia) {
					AAA.setShowClasseAutoveicolo( true );
					totaleShowClasseAutoveicolo ++;
				}else{
					AAA.setShowClasseAutoveicolo( false );
				}
			}
			resultAutistaTariffePrezzo.setTotaleShowClasseAutoveicolo(totaleShowClasseAutoveicolo);
			return resultAutistaTariffePrezzo;
		}else {
			return HomeUtil.getResultAutistaTariffePrezzo(this.id);
		}
	}
	
	@Transient
	public void setResultAutistaTariffePrezzo(ResultRicerca_Autista_Tariffe resultAutistaTariffePrezzo) {
		this.resultAutistaTariffePrezzo = resultAutistaTariffePrezzo;
	}
	

	@Transient
	private List<Long> autistiClasseAutoveicoloScelto;
	/**
	 * mi ritorna la lista di ID Autisti scelti di una Classe Auto
	 */
	@Transient
	public List<Long> getAutistiClasseAutoveicoloScelto() {
		if( this.richiestaMedia != null && this.richiestaMedia.size() > 0 ) {
			for(RichiestaMedia richiestaMedia_ite: this.richiestaMedia){
				if( richiestaMedia_ite.isClasseAutoveicoloScelta() ){
					List<Long> AutistiScelti = new ArrayList<Long>();
					for(RichiestaMediaAutista richiestaMediaAutista_ite: richiestaMedia_ite.getRichiestaMediaAutista()){
						if(!AutistiScelti.contains(richiestaMediaAutista_ite.getAutista().getId())){
							AutistiScelti.add( richiestaMediaAutista_ite.getAutista().getId() );
						}
					}
					return NumberUtil.removeDuplicatesLong( AutistiScelti );
				}
			}
		}
		return null;
	}
	@Transient
	public void setAutistiClasseAutoveicoloScelto(List<Long> autistiClasseAutoveicoloScelto) {
		this.autistiClasseAutoveicoloScelto = autistiClasseAutoveicoloScelto;
	}
	
	
	@Transient
	private RichiestaMedia richiestaMediaScelta;
	@Transient
	public RichiestaMedia getRichiestaMediaScelta() {
		if( this.richiestaMedia != null && this.richiestaMedia.size() > 0 ){
			for(RichiestaMedia richiestaMedia_ite: this.richiestaMedia){
				if( richiestaMedia_ite.isClasseAutoveicoloScelta() ){
					return richiestaMedia_ite;
				}
			}
		}
		return null;
	}
	@Transient
	public void setRichiestaMediaScelta(RichiestaMedia richiestaMediaScelta) {
		this.richiestaMediaScelta = richiestaMediaScelta;
	}
	
	
	@Transient
	private RichiestaMediaAutista richiestaMediaAutistaCorsaConfermata;
	@Transient
	public RichiestaMediaAutista getRichiestaMediaAutistaCorsaConfermata() {
		if( getRichiestaMediaScelta() != null ){
			for(RichiestaMediaAutista ite: getRichiestaMediaScelta().getRichiestaMediaAutista()){
				if( ite.isCorsaConfermata() ){
					return ite;
				}
			}
		}
		return null;
	}
	@Transient
	public void setRichiestaMediaAutistaCorsaConfermata(RichiestaMediaAutista richiestaMediaAutistaCorsaConfermata) {
		this.richiestaMediaAutistaCorsaConfermata = richiestaMediaAutistaCorsaConfermata;
	}
	
	
	@Transient
	private List<RichiestaMedia> richiestaMediaNonSceltaList;
	@Transient
	public List<RichiestaMedia> getRichiestaMediaNonSceltaList() {
		if( this.richiestaMedia != null && this.richiestaMedia.size() > 0 ){
			List<RichiestaMedia> richiestaMediaNonSceltaList = new ArrayList<RichiestaMedia>();
			for(RichiestaMedia richiestaMedia_ite: this.richiestaMedia){
				if( !richiestaMedia_ite.isClasseAutoveicoloScelta() ){
					richiestaMediaNonSceltaList.add( richiestaMedia_ite );
				}
			}
			return richiestaMediaNonSceltaList;
		}
		return null;
	}
	
	
	@Transient
	public long getPercentualeServizioRichiestaMediaScelta(){
		RichiestaMedia richMedScelta = getRichiestaMediaScelta();
		if(richMedScelta != null && richMedScelta.getPrezzoCommissioneServizio() != null && richMedScelta.getPrezzoCommissioneServizioIva() != null){
			BigDecimal commissione = richMedScelta.getPrezzoCommissioneServizio().add( richMedScelta.getPrezzoCommissioneServizioIva() );
			return commissione.divide( richMedScelta.getPrezzoTotaleAutista(), 2, RoundingMode.HALF_EVEN )
					.multiply(new BigDecimal(100)).longValue();
		}else{
			return 0l;
		}
	}
	
	
	@Transient
	public long getPercentualeServizioVenditoreRichiestaMediaScelta(){
		RichiestaMedia richMedScelta = getRichiestaMediaScelta();
		if(richMedScelta != null && richMedScelta.getPrezzoCommissioneVenditore() != null){
			BigDecimal commissione = richMedScelta.getPrezzoCommissioneVenditore();
			return commissione.divide( richMedScelta.getPrezzoTotaleAutista(), 2, RoundingMode.HALF_EVEN )
					.multiply(new BigDecimal(100)).longValue();
		}else{
			return 0l;
		}
	}
	
	
	public RicercaTransfert() { }


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_ricerca_transfert")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 2000)
	public String getInfoPasseggero() {
		return infoPasseggero;
	}
	public void setInfoPasseggero(String infoPasseggero) {
		this.infoPasseggero = infoPasseggero;
	}

	public boolean isScontoRitorno() {
		return scontoRitorno;
	}
	public void setScontoRitorno(boolean scontoRitorno) {
		this.scontoRitorno = scontoRitorno;
	}

	@Column(length = 1000)
	public String getNotePerAutista() {
		return notePerAutista;
	}
	public void setNotePerAutista(String notePerAutista) {
		this.notePerAutista = notePerAutista;
	}
	
	public boolean isCollapsePanelCorseAdmin() {
		return collapsePanelCorseAdmin;
	}
	public void setCollapsePanelCorseAdmin(boolean collapsePanelCorseAdmin) {
		this.collapsePanelCorseAdmin = collapsePanelCorseAdmin;
	}

	public int getApprovazioneAndata() {
		return approvazioneAndata;
	}
	public void setApprovazioneAndata(int approvazioneAndata) {
		this.approvazioneAndata = approvazioneAndata;
	}
	
	public int getApprovazioneRitorno() {
		return approvazioneRitorno;
	}
	public void setApprovazioneRitorno(int approvazioneRitorno) {
		this.approvazioneRitorno = approvazioneRitorno;
	}

	@Column(length = 1000)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public boolean isPagamentoEseguitoMedio() {
		return pagamentoEseguitoMedio;
	}
	public void setPagamentoEseguitoMedio(boolean pagamentoEseguitoMedio) {
		this.pagamentoEseguitoMedio = pagamentoEseguitoMedio;
	}

	public boolean isPagamentoParziale() {
		return pagamentoParziale;
	}
	public void setPagamentoParziale(boolean pagamentoParziale) {
		this.pagamentoParziale = pagamentoParziale;
	}

	@Column(length = 30)
	public String getPhoneNumberCustomer() {
		return phoneNumberCustomer;
	}
	public void setPhoneNumberCustomer(String phoneNumberCustomer) {
		this.phoneNumberCustomer = phoneNumberCustomer;
	}

	public boolean isInvioSmsCustomer() {
		return invioSmsCustomer;
	}
	public void setInvioSmsCustomer(boolean invioSmsCustomer) {
		this.invioSmsCustomer = invioSmsCustomer;
	}

	public String getTokenCustomer() {
		return tokenCustomer;
	}
	public void setTokenCustomer(String tokenCustomer) {
		this.tokenCustomer = tokenCustomer;
	}

	public boolean isVerificatoCustomer() {
		return verificatoCustomer;
	}
	public void setVerificatoCustomer(boolean verificatoCustomer) {
		this.verificatoCustomer = verificatoCustomer;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}
	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}
	
	public String getPartenzaRequest() {
		return partenzaRequest;
	}
	public void setPartenzaRequest(String partenzaRequest) {
		this.partenzaRequest = partenzaRequest;
	}

	public String getArrivoRequest() {
		return arrivoRequest;
	}
	public void setArrivoRequest(String arrivoRequest) {
		this.arrivoRequest = arrivoRequest;
	}
	
	public int getNumeroPasseggeri() {
		return numeroPasseggeri;
	}
	public void setNumeroPasseggeri(int numeroPasseggeri) {
		this.numeroPasseggeri = numeroPasseggeri;
	}

	public Date getDataRicerca() {
		return dataRicerca;
	}
	public void setDataRicerca(Date dataRicerca) {
		this.dataRicerca = dataRicerca;
	}

	public boolean isRitorno() {
		return ritorno;
	}
	public void setRitorno(boolean ritorno) {
		this.ritorno = ritorno;
	}

	public String getDataOraPrelevamento() {
		return dataOraPrelevamento;
	}
	public void setDataOraPrelevamento(String dataOraPrelevamento) {
		this.dataOraPrelevamento = dataOraPrelevamento;
	}

	public String getDataOraRitorno() {
		return dataOraRitorno;
	}
	public void setDataOraRitorno(String dataOraRitorno) {
		this.dataOraRitorno = dataOraRitorno;
	}
	
	public Date getDataOraPrelevamentoDate() {
		return dataOraPrelevamentoDate;
	}
	public void setDataOraPrelevamentoDate(Date dataOraPrelevamentoDate) {
		this.dataOraPrelevamentoDate = dataOraPrelevamentoDate;
	}

	public Date getDataOraRitornoDate() {
		return dataOraRitornoDate;
	}
	public void setDataOraRitornoDate(Date dataOraRitornoDate) {
		this.dataOraRitornoDate = dataOraRitornoDate;
	}

	@Column(length = 10)
	public String getOraPrelevamento() {
		return oraPrelevamento;
	}
	public void setOraPrelevamento(String oraPrelevamento) {
		this.oraPrelevamento = oraPrelevamento;
	}

	@Column(length = 10)
	public String getOraRitorno() {
		return oraRitorno;
	}
	public void setOraRitorno(String oraRitorno) {
		this.oraRitorno = oraRitorno;
	}

	public String getPlace_id_Partenza() {
		return place_id_Partenza;
	}
	public void setPlace_id_Partenza(String place_id_Partenza) {
		this.place_id_Partenza = place_id_Partenza;
	}

	public String getFormattedAddress_Partenza() {
		return formattedAddress_Partenza;
	}
	public void setFormattedAddress_Partenza(String formattedAddress_Partenza) {
		this.formattedAddress_Partenza = formattedAddress_Partenza;
	}

	public String getName_Partenza() {
		return name_Partenza;
	}
	public void setName_Partenza(String name_Partenza) {
		this.name_Partenza = name_Partenza;
	}

	public double getLat_Partenza() {
		return lat_Partenza;
	}
	public void setLat_Partenza(double lat_Partenza) {
		this.lat_Partenza = lat_Partenza;
	}

	public double getLng_Partenza() {
		return lng_Partenza;
	}
	public void setLng_Partenza(double lng_Partenza) {
		this.lng_Partenza = lng_Partenza;
	}

	public String getComune_Partenza() {
		return comune_Partenza;
	}
	public void setComune_Partenza(String comune_Partenza) {
		this.comune_Partenza = comune_Partenza;
	}

	public String getSiglaProvicia_Partenza() {
		return siglaProvicia_Partenza;
	}
	public void setSiglaProvicia_Partenza(String siglaProvicia_Partenza) {
		this.siglaProvicia_Partenza = siglaProvicia_Partenza;
	}

	public String getPlace_id_Arrivo() {
		return place_id_Arrivo;
	}
	public void setPlace_id_Arrivo(String place_id_Arrivo) {
		this.place_id_Arrivo = place_id_Arrivo;
	}

	public String getFormattedAddress_Arrivo() {
		return formattedAddress_Arrivo;
	}
	public void setFormattedAddress_Arrivo(String formattedAddress_Arrivo) {
		this.formattedAddress_Arrivo = formattedAddress_Arrivo;
	}

	public String getName_Arrivo() {
		return name_Arrivo;
	}
	public void setName_Arrivo(String name_Arrivo) {
		this.name_Arrivo = name_Arrivo;
	}

	public double getLat_Arrivo() {
		return lat_Arrivo;
	}
	public void setLat_Arrivo(double lat_Arrivo) {
		this.lat_Arrivo = lat_Arrivo;
	}

	public double getLng_Arrivo() {
		return lng_Arrivo;
	}
	public void setLng_Arrivo(double lng_Arrivo) {
		this.lng_Arrivo = lng_Arrivo;
	}

	public String getComune_Arrivo() {
		return comune_Arrivo;
	}
	public void setComune_Arrivo(String comune_Arrivo) {
		this.comune_Arrivo = comune_Arrivo;
	}

	public String getSiglaProvicia_Arrivo() {
		return siglaProvicia_Arrivo;
	}
	public void setSiglaProvicia_Arrivo(String siglaProvicia_Arrivo) {
		this.siglaProvicia_Arrivo = siglaProvicia_Arrivo;
	}

	public String getDistanzaText() {
		return distanzaText;
	}
	public void setDistanzaText(String distanzaText) {
		this.distanzaText = distanzaText;
	}

	public long getDistanzaValue() {
		return distanzaValue;
	}
	public void setDistanzaValue(long distanzaValue) {
		this.distanzaValue = distanzaValue;
	}

	public String getDurataText() {
		return durataText;
	}
	public void setDurataText(String durataText) {
		this.durataText = durataText;
	}

	public long getDurataValue() {
		return durataValue;
	}
	public void setDurataValue(long durataValue) {
		this.durataValue = durataValue;
	}

	public String getDurataConTrafficoText() {
		return durataConTrafficoText;
	}
	public void setDurataConTrafficoText(String durataConTrafficoText) {
		this.durataConTrafficoText = durataConTrafficoText;
	}

	public long getDurataConTrafficoValue() {
		return durataConTrafficoValue;
	}
	public void setDurataConTrafficoValue(long durataConTrafficoValue) {
		this.durataConTrafficoValue = durataConTrafficoValue;
	}

	public String getDistanzaTextRitorno() {
		return distanzaTextRitorno;
	}
	public void setDistanzaTextRitorno(String distanzaTextRitorno) {
		this.distanzaTextRitorno = distanzaTextRitorno;
	}

	public long getDistanzaValueRitorno() {
		return distanzaValueRitorno;
	}
	public void setDistanzaValueRitorno(long distanzaValueRitorno) {
		this.distanzaValueRitorno = distanzaValueRitorno;
	}

	public String getDurataTextRitorno() {
		return durataTextRitorno;
	}
	public void setDurataTextRitorno(String durataTextRitorno) {
		this.durataTextRitorno = durataTextRitorno;
	}

	public long getDurataValueRitorno() {
		return durataValueRitorno;
	}
	public void setDurataValueRitorno(long durataValueRitorno) {
		this.durataValueRitorno = durataValueRitorno;
	}

	public String getDurataConTrafficoTextRitorno() {
		return durataConTrafficoTextRitorno;
	}
	public void setDurataConTrafficoTextRitorno(String durataConTrafficoTextRitorno) {
		this.durataConTrafficoTextRitorno = durataConTrafficoTextRitorno;
	}

	public long getDurataConTrafficoValueRitorno() {
		return durataConTrafficoValueRitorno;
	}
	public void setDurataConTrafficoValueRitorno(long durataConTrafficoValueRitorno) {
		this.durataConTrafficoValueRitorno = durataConTrafficoValueRitorno;
	}

	@Transient
	public String getCodiceAeroportuale() {
		return codiceAeroportuale;
	}
	@Transient
	public void setCodiceAeroportuale(String codiceAeroportuale) {
		this.codiceAeroportuale = codiceAeroportuale;
	}
	
	@Transient
	public String getDescrizioneMuseo() {
		return descrizioneMuseo;
	}
	@Transient
	public void setDescrizioneMuseo(String descrizioneMuseo) {
		this.descrizioneMuseo = descrizioneMuseo;
	}

	
    public boolean equals(Object o) {
        if (this == o) {  return true; }
        if (!(o instanceof RicercaTransfert)) { return false; }
        final RicercaTransfert ricercaTransfert = (RicercaTransfert) o;
        return !(id != null ? !id.equals(ricercaTransfert.id) : ricercaTransfert.id != null);
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.id).toString();
    }
	
    public Object clone() throws CloneNotSupportedException {  
    	return super.clone();  
    } 

}

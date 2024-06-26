package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.Constants;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name="richiesta_autista_particolare")
public class RichiestaAutistaParticolare extends BaseObject implements Serializable {
	private static final long serialVersionUID = 7129910857642813834L;

	private Long id;
	private String infoCorsa;
	private String token;
	private Integer percentualeServizio;
	private BigDecimal prezzoCommissioneServizio;
	private BigDecimal prezzoCommissioneServizioIva;
	private BigDecimal prezzoTotaleAutista;
	private BigDecimal rimborsoCliente; // TODO ex maggiorazioneNotturna (aggiornare in produzione)
	private boolean invioSms;
	private Date dataChiamataPrenotata;
	private boolean invioSmsCorsaConfermata;
	
	
	@Deprecated
	private BigDecimal prezzo; 			// TODO NON LO USO
	@Deprecated
	private BigDecimal tariffaPerKm; 	// TODO NON LO USO
	@Deprecated
	private String tipoServizio; 		// TODO NON LO USO
	@Deprecated
	private boolean pagamentoEseguito;	// TODO NON LO USO
	@Deprecated
	private boolean chiamataPrenotata;	// TODO NON LO USO
	
	
	
	
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	public BigDecimal getTariffaPerKm() {
		return tariffaPerKm;
	}
	public void setTariffaPerKm(BigDecimal tariffaPerKm) {
		this.tariffaPerKm = tariffaPerKm;
	}
	public String getTipoServizio() {
		return tipoServizio;
	}
	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}
	public boolean isPagamentoEseguito() {
		return pagamentoEseguito;
	}
	public void setPagamentoEseguito(boolean pagamentoEseguito) {
		this.pagamentoEseguito = pagamentoEseguito;
	}
	public boolean isChiamataPrenotata() {
		return chiamataPrenotata;
	}
	public void setChiamataPrenotata(boolean chiamataPrenotata) {
		this.chiamataPrenotata = chiamataPrenotata;
	}

	@Transient
	private Integer totaleSmsRicevutiAutista;
	@Transient
	public Integer getTotaleSmsRicevutiAutista() {
		return totaleSmsRicevutiAutista;
	}
	@Transient
	public void setTotaleSmsRicevutiAutista(Integer totaleSmsRicevutiAutista) {
		this.totaleSmsRicevutiAutista = totaleSmsRicevutiAutista;
	}

	@Transient
	List<RichiestaAutistaParticolare> richiestaAutistaParticolareMultiploList;
	@Transient
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareMultiploList() {
		return richiestaAutistaParticolareMultiploList;
	}
	@Transient
	public void setRichiestaAutistaParticolareMultiploList(List<RichiestaAutistaParticolare> richiestaAutistaParticolareMultiploList) {
		this.richiestaAutistaParticolareMultiploList = richiestaAutistaParticolareMultiploList;
	}

	@Transient
	private BigDecimal prezzoTotaleCliente;
	@Transient
	public BigDecimal getPrezzoTotaleCliente() {
		if( this.prezzoTotaleAutista != null && this.prezzoCommissioneServizio != null && this.prezzoCommissioneServizioIva != null ) {
			return this.prezzoTotaleAutista.add(this.prezzoCommissioneServizio.add(this.prezzoCommissioneServizioIva));
		}else {
			return null;
		}
	}
	
	@Transient
	public Boolean getPreventivo_inviato_cliente() {
		if(this.infoCorsa != null){
			try{
				JSONObject json = new JSONObject(this.infoCorsa);
				return json.getBoolean(Constants.Preventivo_inviato_cliente);
			}catch(JSONException JsonExc){
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Transient
	public Long getPreventivo_validita_data() {
		if(this.infoCorsa != null){
			try{
				JSONObject json = new JSONObject(this.infoCorsa);
				return json.getLong(Constants.Preventivo_validita_data);
			}catch(JSONException JsonExc){
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Transient
	public Date getPreventivo_validita_data_Date() {
		return getPreventivo_validita_data() != null ? new Date(getPreventivo_validita_data()) : null;
	}
	
	private ClasseAutoveicolo classeAutoveicoloScelta;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_classe_autoveicolo", unique = false, nullable = true)
	public ClasseAutoveicolo getClasseAutoveicoloScelta() {
		return classeAutoveicoloScelta;
	}
	public void setClasseAutoveicoloScelta(ClasseAutoveicolo classeAutoveicoloScelta) {
		this.classeAutoveicoloScelta = classeAutoveicoloScelta;
	}
	
	private RicercaTransfert ricercaTransfert;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_ricerca_transfert", nullable = false)
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	
	private Autoveicolo autoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autoveicolo", nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	public RichiestaAutistaParticolare() {

	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_richiesta_autista_particolare")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 2000)
	public String getInfoCorsa() {
		return infoCorsa;
	}
	public void setInfoCorsa(String infoCorsa) {
		this.infoCorsa = infoCorsa;
	}
	
	public BigDecimal getPrezzoCommissioneServizioIva() {
		return prezzoCommissioneServizioIva;
	}
	public void setPrezzoCommissioneServizioIva(
			BigDecimal prezzoCommissioneServizioIva) {
		this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
	}

	@Column(nullable = false) 
	public Integer getPercentualeServizio() {
		return percentualeServizio;
	}
	public void setPercentualeServizio(Integer percentualeServizio) {
		this.percentualeServizio = percentualeServizio;
	}

	public boolean isInvioSmsCorsaConfermata() {
		return invioSmsCorsaConfermata;
	}
	public void setInvioSmsCorsaConfermata(boolean invioSmsCorsaConfermata) {
		this.invioSmsCorsaConfermata = invioSmsCorsaConfermata;
	}
	
	public boolean isInvioSms() {
		return invioSms;
	}
	public void setInvioSms(boolean invioSms) {
		this.invioSms = invioSms;
	}
	
	public Date getDataChiamataPrenotata() {
		return dataChiamataPrenotata;
	}
	public void setDataChiamataPrenotata(Date dataChiamataPrenotata) {
		this.dataChiamataPrenotata = dataChiamataPrenotata;
	}

	@Column(nullable = false, unique = true)
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public BigDecimal getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(BigDecimal rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	
	public BigDecimal getPrezzoTotaleAutista() {
		return prezzoTotaleAutista;
	}
	public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
		this.prezzoTotaleAutista = prezzoTotaleAutista;
	}

	public BigDecimal getPrezzoCommissioneServizio() {
		return prezzoCommissioneServizio;
	}
	public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
		this.prezzoCommissioneServizio = prezzoCommissioneServizio;
	}

	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RichiestaAutistaParticolare)) {
            return false;
        }
        final RichiestaAutistaParticolare ricercaTransfert = (RichiestaAutistaParticolare) o;
        return !(id != null ? !id.equals(ricercaTransfert.id) : ricercaTransfert.id != null);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.id)
                .toString();
    }
}

package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name="richiesta_media_autista"
	//, uniqueConstraints={@UniqueConstraint(columnNames={"id_richiesta_media","id_autista"})}
		)
public class RichiestaMediaAutista extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5402742801531706285L;

	private Long id;
	private int ordineAutista;
	private boolean chiamataPrenotata;
	private boolean corsaConfermata;
	private Date dataChiamataPrenotata;
	private Date dataCorsaConfermata;
	private boolean invioSms;
	private boolean invioSmsCorsaConfermata;
	private String tokenAutista;
	private BigDecimal prezzoCommissioneServizio;
	private BigDecimal prezzoCommissioneServizioIva;
	private BigDecimal prezzoCommissioneVenditore;
	private BigDecimal prezzoTotaleAutista;
	private BigDecimal tariffaPerKm;
	
	public BigDecimal getPrezzoCommissioneServizio() {
		return prezzoCommissioneServizio;
	}
	public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
		this.prezzoCommissioneServizio = prezzoCommissioneServizio;
	}
	
	public BigDecimal getPrezzoCommissioneServizioIva() {
		return prezzoCommissioneServizioIva;
	}
	public void setPrezzoCommissioneServizioIva(BigDecimal prezzoCommissioneServizioIva) {
		this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
	}
	
	public BigDecimal getPrezzoCommissioneVenditore() {
		return prezzoCommissioneVenditore;
	}
	public void setPrezzoCommissioneVenditore(BigDecimal prezzoCommissioneVenditore) {
		this.prezzoCommissioneVenditore = prezzoCommissioneVenditore;
	}
	
	public BigDecimal getPrezzoTotaleAutista() {
		return prezzoTotaleAutista;
	}
	public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
		this.prezzoTotaleAutista = prezzoTotaleAutista;
	}

	public BigDecimal getTariffaPerKm() {
		return tariffaPerKm;
	}
	public void setTariffaPerKm(BigDecimal tariffaPerKm) {
		this.tariffaPerKm = tariffaPerKm;
	}

	private Autista autista;
	@ManyToOne
	@JoinColumn(name = "id_autista", nullable = false)
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	
	
	private RichiestaMedia richiestaMedia;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_richiesta_media", nullable = false)
	public RichiestaMedia getRichiestaMedia() {
		return richiestaMedia;
	}
	public void setRichiestaMedia(RichiestaMedia richiestaMedia) {
		this.richiestaMedia = richiestaMedia;
	}

	
	private Set<RichiestaMediaAutistaAutoveicolo> richiestaMediaAutistaAutoveicolo;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "richiestaAutistaMedio" )
	@Fetch(FetchMode.SELECT)
	public Set<RichiestaMediaAutistaAutoveicolo> getRichiestaMediaAutistaAutoveicolo() {
		return richiestaMediaAutistaAutoveicolo;
	}
	public void setRichiestaMediaAutistaAutoveicolo(
			Set<RichiestaMediaAutistaAutoveicolo> richiestaMediaAutistaAutoveicolo) {
		this.richiestaMediaAutistaAutoveicolo = richiestaMediaAutistaAutoveicolo;
	}
	
	public RichiestaMediaAutista() {
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_richiesta_media_autista")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false) 
	public int getOrdineAutista() {
		return ordineAutista;
	}
	public void setOrdineAutista(int ordineAutista) {
		this.ordineAutista = ordineAutista;
	}
	
	public boolean isInvioSmsCorsaConfermata() {
		return invioSmsCorsaConfermata;
	}
	public void setInvioSmsCorsaConfermata(boolean invioSmsCorsaConfermata) {
		this.invioSmsCorsaConfermata = invioSmsCorsaConfermata;
	}

	public boolean isCorsaConfermata() {
		return corsaConfermata;
	}
	public void setCorsaConfermata(boolean corsaConfermata) {
		this.corsaConfermata = corsaConfermata;
	}

	public Date getDataCorsaConfermata() {
		return dataCorsaConfermata;
	}
	public void setDataCorsaConfermata(Date dataCorsaConfermata) {
		this.dataCorsaConfermata = dataCorsaConfermata;
	}

	@Column(nullable = false, unique = true)
	public String getTokenAutista() {
		return tokenAutista;
	}
	public void setTokenAutista(String tokenAutista) {
		this.tokenAutista = tokenAutista;
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

	public boolean isChiamataPrenotata() {
		return chiamataPrenotata;
	}
	public void setChiamataPrenotata(boolean chiamataPrenotata) {
		this.chiamataPrenotata = chiamataPrenotata;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RichiestaMediaAutista)) {
            return false;
        }
        final RichiestaMediaAutista ricercaTransfert = (RichiestaMediaAutista) o;
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

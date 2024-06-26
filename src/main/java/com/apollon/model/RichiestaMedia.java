package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name="richiesta_media", uniqueConstraints={@UniqueConstraint(columnNames={"id_classe_autoveicolo","id_ricerca_transfert"})})

public class RichiestaMedia extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5402742801531706285L;

	private Long id;
	private BigDecimal prezzoTotaleCliente;
	private BigDecimal maggiorazioneNotturna;
	private BigDecimal rimborsoCliente;
	private boolean classeAutoveicoloScelta;
	
	@Deprecated
	private BigDecimal tariffaPerKm;
	@Deprecated
	private BigDecimal prezzoTotaleAutista;
	@Deprecated
	private BigDecimal prezzoCommissioneServizio;
	@Deprecated
	private BigDecimal prezzoCommissioneServizioIva;
	@Deprecated
	private BigDecimal prezzoCommissioneVenditore;
	
	private ClasseAutoveicolo classeAutoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_classe_autoveicolo", unique = false, nullable = false)
	public ClasseAutoveicolo getClasseAutoveicolo() {
		return classeAutoveicolo;
	}
	public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
		this.classeAutoveicolo = classeAutoveicolo;
	}
	
	private RicercaTransfert ricercaTransfert;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert", unique = false, nullable = false)
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	
	private Set<RichiestaMediaAutista> richiestaMediaAutista;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "richiestaMedia")
	@Fetch(FetchMode.SELECT)
	public Set<RichiestaMediaAutista> getRichiestaMediaAutista() {
		return richiestaMediaAutista;
	}
	public void setRichiestaMediaAutista(
			Set<RichiestaMediaAutista> richiestaMediaAutista) {
		this.richiestaMediaAutista = richiestaMediaAutista;
	}
	
	public RichiestaMedia() {

	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_richiesta_media")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrezzoTotaleCliente() {
		return prezzoTotaleCliente;
	}
	public void setPrezzoTotaleCliente(BigDecimal prezzoTotaleCliente) {
		this.prezzoTotaleCliente = prezzoTotaleCliente;
	}
	
	public boolean isClasseAutoveicoloScelta() {
		return classeAutoveicoloScelta;
	}
	public void setClasseAutoveicoloScelta(boolean classeAutoveicoloScelta) {
		this.classeAutoveicoloScelta = classeAutoveicoloScelta;
	}
	
	public BigDecimal getMaggiorazioneNotturna() {
		return maggiorazioneNotturna;
	}
	public void setMaggiorazioneNotturna(BigDecimal maggiorazioneNotturna) {
		this.maggiorazioneNotturna = maggiorazioneNotturna;
	}
	
	public BigDecimal getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(BigDecimal rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	
	@Deprecated
	public BigDecimal getTariffaPerKm() {
		return tariffaPerKm;
	}
	@Deprecated
	public void setTariffaPerKm(BigDecimal tariffaPerKm) {
		this.tariffaPerKm = tariffaPerKm;
	}
	@Deprecated
	public BigDecimal getPrezzoCommissioneServizio() {
		return prezzoCommissioneServizio;
	}
	@Deprecated
	public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
		this.prezzoCommissioneServizio = prezzoCommissioneServizio;
	}
	@Deprecated
	public BigDecimal getPrezzoCommissioneServizioIva() {
		return prezzoCommissioneServizioIva;
	}
	@Deprecated
	public void setPrezzoCommissioneServizioIva(
			BigDecimal prezzoCommissioneServizioIva) {
		this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
	}
	public BigDecimal getPrezzoCommissioneVenditore() {
		return prezzoCommissioneVenditore;
	}
	public void setPrezzoCommissioneVenditore(BigDecimal prezzoCommissioneVenditore) {
		this.prezzoCommissioneVenditore = prezzoCommissioneVenditore;
	}
	@Deprecated
	public BigDecimal getPrezzoTotaleAutista() {
		return prezzoTotaleAutista;
	}
	@Deprecated
	public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
		this.prezzoTotaleAutista = prezzoTotaleAutista;
	}
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RichiestaMedia)) {
            return false;
        }

        final RichiestaMedia richiestaMedia = (RichiestaMedia) o;

        return !(id != null ? !id.equals(richiestaMedia.id) : richiestaMedia.id != null);

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

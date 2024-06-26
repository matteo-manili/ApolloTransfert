package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "ritardi")
public class Ritardi extends BaseObject implements Serializable {
	private static final long serialVersionUID = -6959015195897691671L;
	
	private Long id;
	// il numero totale delle mezzore di ritardo
	private Integer numeroMezzoreRitardiAndata;
	private Integer numeroMezzoreRitardiRitorno;
	// quando creo il ritardo inserico subito il prezzo
	private BigDecimal prezzoAndata;
	private BigDecimal prezzoRitorno;
	// default falso  quando il cliente paga lo metto a true
	private boolean pagatoAndata;
	private boolean pagatoRitorno;
	private String idPaymentProvider;
	
	
	private Fatture fatture;
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "ritardi")
	public Fatture getFatture() {
		return fatture;
	}
	public void setFatture(Fatture fatture) {
		this.fatture = fatture;
	}

	
	RicercaTransfert ricercaTransfert;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert", unique = true, nullable = false)
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	
	
	public Ritardi() { }
	
	
	public Ritardi(Integer numeroMezzoreRitardiAndata, Integer numeroMezzoreRitardiRitorno, BigDecimal prezzoAndata, BigDecimal prezzoRitorno, boolean pagatoAndata, boolean pagatoRitorno) {
		this.numeroMezzoreRitardiAndata = numeroMezzoreRitardiAndata;
		this.numeroMezzoreRitardiRitorno = numeroMezzoreRitardiRitorno;
		this.prezzoAndata = prezzoAndata;
		this.prezzoRitorno = prezzoRitorno;
		this.pagatoAndata = pagatoAndata;
		this.pagatoRitorno = pagatoRitorno;
	}

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_ritardi")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPagatoAndata() {
		return pagatoAndata;
	}
	public void setPagatoAndata(boolean pagatoAndata) {
		this.pagatoAndata = pagatoAndata;
	}

	public boolean isPagatoRitorno() {
		return pagatoRitorno;
	}
	public void setPagatoRitorno(boolean pagatoRitorno) {
		this.pagatoRitorno = pagatoRitorno;
	}

	public Integer getNumeroMezzoreRitardiAndata() {
		return numeroMezzoreRitardiAndata;
	}
	public void setNumeroMezzoreRitardiAndata(Integer numeroMezzoreRitardiAndata) {
		this.numeroMezzoreRitardiAndata = numeroMezzoreRitardiAndata;
	}

	public Integer getNumeroMezzoreRitardiRitorno() {
		return numeroMezzoreRitardiRitorno;
	}
	public void setNumeroMezzoreRitardiRitorno(Integer numeroMezzoreRitardiRitorno) {
		this.numeroMezzoreRitardiRitorno = numeroMezzoreRitardiRitorno;
	}

	public BigDecimal getPrezzoAndata() {
		return prezzoAndata;
	}
	public void setPrezzoAndata(BigDecimal prezzoAndata) {
		this.prezzoAndata = prezzoAndata;
	}

	public BigDecimal getPrezzoRitorno() {
		return prezzoRitorno;
	}
	public void setPrezzoRitorno(BigDecimal prezzoRitorno) {
		this.prezzoRitorno = prezzoRitorno;
	}
	
	@Column(length = 50)
	public String getIdPaymentProvider() {
		return idPaymentProvider;
	}
	public void setIdPaymentProvider(String idPaymentProvider) {
		this.idPaymentProvider = idPaymentProvider;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ritardi)) {
            return false;
        }
        final Ritardi ritardi = (Ritardi) o;
        return !(id != null ? !id.equals(ritardi.id) : ritardi.id != null);
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

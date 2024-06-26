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
@Table(name = "supplementi")
public class Supplementi extends BaseObject implements Serializable {
	private static final long serialVersionUID = 4815454269749310308L;

	private Long id;
	private BigDecimal prezzo;
	private boolean pagato;
	private String descrizione;
	private String idPaymentProvider;
	
	private Fatture fatture;
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "supplementi")
	public Fatture getFatture() {
		return fatture;
	}
	public void setFatture(Fatture fatture) {
		this.fatture = fatture;
	}

	
	RicercaTransfert ricercaTransfert;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert", unique = false, nullable = false)
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	
	
	public Supplementi() { }

	
	public Supplementi(BigDecimal prezzo, boolean pagato, String descrizione, RicercaTransfert ricercaTransfert) {
		this.prezzo = prezzo;
		this.pagato = pagato;
		this.descrizione = descrizione;
		this.ricercaTransfert = ricercaTransfert;
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_supplementi")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	
	public boolean isPagato() {
		return pagato;
	}
	public void setPagato(boolean pagato) {
		this.pagato = pagato;
	}
	
	@Column(length = 1000)
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
        if (!(o instanceof Supplementi)) {
            return false;
        }

        final Supplementi supplemento = (Supplementi) o;

        return !(id != null ? !id.equals(supplemento.id) : supplemento.id != null);

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

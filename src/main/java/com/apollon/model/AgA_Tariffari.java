package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name="aga_tariffari", uniqueConstraints={@UniqueConstraint(columnNames={"kmCorsa", "id_aga_giornate"}), @UniqueConstraint(columnNames={"id_aga_giornate", "id_aga_autoveicolo_modelli_tariffari"})})
//@Table(name="aga_tariffari", uniqueConstraints={@UniqueConstraint(columnNames={"kmCorsa","id_aga_giornate"})})
public class AgA_Tariffari extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1063437599754844312L;

	private Long id;
	private int kmCorsa;
	private boolean eseguiCorse;
	private BigDecimal prezzoCorsa;
	private double kmRaggioArea;
	
	private AgA_Giornate agA_Giornate;
	@ManyToOne
	@JoinColumn(name = "id_aga_giornate", unique = false, nullable = false)
	public AgA_Giornate getAgA_Giornate() {
		return agA_Giornate;
	}
	public void setAgA_Giornate(AgA_Giornate agA_Giornate) {
		this.agA_Giornate = agA_Giornate;
	}
	
	private AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari;
	@ManyToOne
	@JoinColumn(name = "id_aga_autoveicolo_modelli_tariffari", unique = false, nullable = true)
	public AgA_AutoveicoloModelliTariffari getAgA_AutoveicoloModelliTariffari() {
		return agA_AutoveicoloModelliTariffari;
	}
	public void setAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) {
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}
	

	private RicercaTransfert ricercaTransfertAcquistato;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert", unique = false, nullable = true)
	public RicercaTransfert getRicercaTransfertAcquistato() {
		return ricercaTransfertAcquistato;
	}
	public void setRicercaTransfertAcquistato(RicercaTransfert ricercaTransfertAcquistato) {
		this.ricercaTransfertAcquistato = ricercaTransfertAcquistato;
	}
	
	public AgA_Tariffari(int kmCorsa, boolean eseguiCorse, BigDecimal prezzoCorsa, double kmRaggioArea, AgA_Giornate agA_Giornate) {
		super();
		this.kmCorsa = kmCorsa;
		this.eseguiCorse = eseguiCorse;
		this.prezzoCorsa = prezzoCorsa;
		this.kmRaggioArea = kmRaggioArea;
		this.agA_Giornate = agA_Giornate;
	}
	
	public AgA_Tariffari(AgA_Giornate agA_Giornate, AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) {
		super();
		this.agA_Giornate = agA_Giornate;
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}
	
	public AgA_Tariffari() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_tariffari")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = true)
	public int getKmCorsa() {
		return kmCorsa;
	}
	public void setKmCorsa(int kmCorsa) {
		this.kmCorsa = kmCorsa;
	}
	
	public void setEseguiCorse(boolean eseguiCorse) {
		this.eseguiCorse = eseguiCorse;
	}
	public boolean isEseguiCorse() {
		return eseguiCorse;
	}
	
	@Column(nullable = true)
	public BigDecimal getPrezzoCorsa() {
		return prezzoCorsa;
	}
	public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
		this.prezzoCorsa = prezzoCorsa;
	}

	@Column(nullable = true)
	public double getKmRaggioArea() {
		return kmRaggioArea;
	}
	public void setKmRaggioArea(double kmRaggioArea) {
		this.kmRaggioArea = kmRaggioArea;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgA_Tariffari)) {
            return false;
        }
        final AgA_Tariffari richiestaMedia = (AgA_Tariffari) o;
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

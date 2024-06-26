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
@Table(name="aga_modelli_tariffari", uniqueConstraints={@UniqueConstraint(columnNames={"kmCorsa","id_aga_autoveicolo_modelli_tariffari"})})
public class AgA_ModelliTariffari extends BaseObject implements Serializable {
	private static final long serialVersionUID = -1070854850865914203L;

	private Long id;
	
	private int kmCorsa;
	private boolean eseguiCorse;
	private BigDecimal prezzoCorsa;
	private double kmRaggioArea;
	
	
	private AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari;
	@ManyToOne
	@JoinColumn(name = "id_aga_autoveicolo_modelli_tariffari", unique = false, nullable = false)
	public AgA_AutoveicoloModelliTariffari getAgA_AutoveicoloModelliTariffari() {
		return agA_AutoveicoloModelliTariffari;
	}
	public void setAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) {
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}

	public AgA_ModelliTariffari(int kmCorsa, boolean eseguiCorse, BigDecimal prezzoCorsa, double kmRaggioArea, AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) {
		super();
		this.kmCorsa = kmCorsa;
		this.eseguiCorse = eseguiCorse;
		this.prezzoCorsa = prezzoCorsa;
		this.kmRaggioArea = kmRaggioArea;
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}
	
	public AgA_ModelliTariffari() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_modelli_tariffari")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false)
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
	
	public BigDecimal getPrezzoCorsa() {
		return prezzoCorsa;
	}
	public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
		this.prezzoCorsa = prezzoCorsa;
	}

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
        if (!(o instanceof AgA_ModelliTariffari)) {
            return false;
        }
        final AgA_ModelliTariffari richiestaMedia = (AgA_ModelliTariffari) o;
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

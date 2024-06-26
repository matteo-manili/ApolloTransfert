package com.apollon.model;

import java.io.Serializable;

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
@Table(name="aga_modelli_giornate", uniqueConstraints={@UniqueConstraint(columnNames={"orario","id_aga_autoveicolo_modelli_giornate"})})
public class AgA_ModelliGiornate extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5086798818067785106L;

	private Long id;
	private int orario;
	private boolean attivo;
	
	
	private AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate;
	@ManyToOne
	@JoinColumn(name = "id_aga_autoveicolo_modelli_giornate", unique = false, nullable = false)
	public AgA_AutoveicoloModelliGiornate getAgA_AutoveicoloModelliGiornate() {
		return agA_AutoveicoloModelliGiornate;
	}
	public void setAgA_AutoveicoloModelliGiornate(AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) {
		this.agA_AutoveicoloModelliGiornate = agA_AutoveicoloModelliGiornate;
	}
	
	
	private AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari;
	@ManyToOne
	@JoinColumn(name = "id_aga_autoveicolo_modelli_tariffari", unique = false, nullable = false)
	public AgA_AutoveicoloModelliTariffari getAgA_AutoveicoloModelliTariffari() {
		return agA_AutoveicoloModelliTariffari;
	}
	public void setAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) {
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}

	
	public AgA_ModelliGiornate(int orario, boolean attivo, AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari, 
			AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) {
		super();
		this.orario = orario;
		this.attivo = attivo;
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
		this.agA_AutoveicoloModelliGiornate = agA_AutoveicoloModelliGiornate;
	}
	
	public AgA_ModelliGiornate() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_modelli_giornate")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public int getOrario() {
		return orario;
	}
	public void setOrario(int orario) {
		this.orario = orario;
	}
	
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgA_ModelliGiornate)) {
            return false;
        }

        final AgA_ModelliGiornate richiestaMedia = (AgA_ModelliGiornate) o;

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

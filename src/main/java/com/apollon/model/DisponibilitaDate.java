package com.apollon.model;

import java.io.Serializable;

import java.util.Date;

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
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */

//uniqueConstraints= @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo", "id_zona", "id_autista_aeroporti", "id_autista_porto_navale"}) 

@Entity
@Table(name = "disponibilita_date", 
	uniqueConstraints = @UniqueConstraint(columnNames={"id_disponibilita", "data"}) )

public class DisponibilitaDate extends BaseObject implements Serializable {
	private static final long serialVersionUID = -2098523082212790776L;
	
	
	private Long id;
	private Date data;

	private Disponibilita disponibilita;
	

	@ManyToOne
	@JoinColumn(name = "id_disponibilita", nullable = false)
	public Disponibilita getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(Disponibilita disponibilita) {
		this.disponibilita = disponibilita;
	}

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_disponibilita_date")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	

	public DisponibilitaDate() {
	}
	

	public DisponibilitaDate(Date data, Disponibilita disponibilita) {
		this.data = data;
		this.disponibilita = disponibilita;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisponibilitaDate)) {
            return false;
        }

        final DisponibilitaDate tipoRuoloServiziDog = (DisponibilitaDate) o;

        return !(id != null ? !id.equals(tipoRuoloServiziDog.id) : tipoRuoloServiziDog.id != null);

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

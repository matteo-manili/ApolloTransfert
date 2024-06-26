package com.apollon.model;

import java.io.Serializable;

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
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */

//uniqueConstraints= @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo", "id_zona", "id_autista_aeroporti", "id_autista_porto_navale"}) 

@Entity
@Table(name = "disponibilita" )

public class Disponibilita extends BaseObject implements Serializable {
	private static final long serialVersionUID = 7252064772719748942L;
	
	
	private Long id;
	private Set<DisponibilitaDate> disponibilitaDate;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "disponibilita", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<DisponibilitaDate> getDisponibilitaDate() {
		return disponibilitaDate;
	}
	public void setDisponibilitaDate(Set<DisponibilitaDate> disponibilitaDate) {
		this.disponibilitaDate = disponibilitaDate;
	}

	private Autoveicolo autoveicolo;
	@ManyToOne
	@JoinColumn(name = "id_autoveicolo", nullable = false, unique = true)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	public Disponibilita(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	
	public Disponibilita() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_disponibilita")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

	

	

	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disponibilita)) {
            return false;
        }

        final Disponibilita tipoRuoloServiziDog = (Disponibilita) o;

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

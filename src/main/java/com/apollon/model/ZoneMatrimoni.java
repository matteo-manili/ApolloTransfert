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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "zone_matrimoni")
public class ZoneMatrimoni extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5656985972522977531L;

	private Long id;
	private AutistaZone autistaZone;
	private boolean servizioAttivo;
	private String nota;
	
	
	
	@Column(length = 500)
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
	
	
	
	
	@ManyToOne
	@JoinColumn(name = "id_zona", nullable = false, unique = true)
    public AutistaZone getAutistaZone() {
		return autistaZone;
	}

	public void setAutistaZone(AutistaZone autistaZone) {
		this.autistaZone = autistaZone;
	}

	

	/**
     * Default constructor - creates a new instance with no values set.
     */
    public ZoneMatrimoni() {
	}
    
	public ZoneMatrimoni(String nomeRegione, Long id) {
		this.id = id;
	}

	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_zone_matrimoni")
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	@Column(columnDefinition="tinyint(1) default 0")
	public boolean isServizioAttivo() {
		return servizioAttivo;
	}

	public void setServizioAttivo(boolean servizioAttivo) {
		this.servizioAttivo = servizioAttivo;
	}

	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneMatrimoni)) {
            return false;
        }

        final ZoneMatrimoni tipoRuoloServiziDog = (ZoneMatrimoni) o;

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

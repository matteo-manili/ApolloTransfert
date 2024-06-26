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
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "autista_musei", uniqueConstraints= @UniqueConstraint(columnNames={"id_autista", "id_museo"})  )
public class AutistaMusei extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8961216253052235859L;
	
	private Long id;
	
	private Musei musei;
	private boolean servizioAttivo;
	private Autista autista;
	
	@ManyToOne
	@JoinColumn(name = "id_autista", nullable = false, unique = false)
	public Autista getAutista() {
		return autista;
	}

	public void setAutista(Autista autista) {
		this.autista = autista;
	}

	@ManyToOne
	@JoinColumn(name = "id_museo", nullable = false, unique = false)
    public Musei getMusei() {
		return musei;
	}

	public void setMusei(Musei musei) {
		this.musei = musei;
	}

	


	
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public AutistaMusei() {
	}
    
	public AutistaMusei(String nomeRegione, Long id) {
		this.id = id;
	}

	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autista_museo")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	
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
        if (!(o instanceof AutistaMusei)) {
            return false;
        }

        final AutistaMusei tipoRuoloServiziDog = (AutistaMusei) o;

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

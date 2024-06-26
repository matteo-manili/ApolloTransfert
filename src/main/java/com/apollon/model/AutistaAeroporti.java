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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "autista_aeroporti", uniqueConstraints= @UniqueConstraint(columnNames={"id_autista", "id_aeroporto"}))
public class AutistaAeroporti extends BaseObject implements Serializable {
	private static final long serialVersionUID = -8242817209976966405L;

	private Long id;
	
	private boolean servizioAttivo;
	private String nota;
	
	private Aeroporti aeroporti;
    @ManyToOne
	@JoinColumn(name = "id_aeroporto", nullable = false, unique = false)
	public Aeroporti getAeroporti() {
		return aeroporti;
	}
	public void setAeroporti(Aeroporti aeroporti) {
		this.aeroporti = aeroporti;
	}
	
	
	private Autista autista;
	@ManyToOne
	@JoinColumn(name = "id_autista", nullable = false, unique = false)
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	
	
	private Set<Tariffe> tariffe;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "autistaAeroporti", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<Tariffe> getTariffe() {
		return tariffe;
	}
	public void setTariffe(Set<Tariffe> tariffe) {
		this.tariffe = tariffe;
	}
	

	public AutistaAeroporti(String nomeRegione, Long id) {
		this.id = id;
	}

	public AutistaAeroporti() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autista_aeroporti")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 500)
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
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
        if (!(o instanceof AutistaAeroporti)) {
            return false;
        }

        final AutistaAeroporti tipoRuoloServiziDog = (AutistaAeroporti) o;

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

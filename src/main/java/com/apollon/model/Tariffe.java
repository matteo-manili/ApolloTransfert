package com.apollon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.search.annotations.IndexedEmbedded;


/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */

//uniqueConstraints= @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo", "id_zona", "id_autista_aeroporti", "id_autista_porto_navale"}) 

@Entity
@Table(name="tariffe", uniqueConstraints={
	   @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo","id_zona" }),
	   @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo","id_autista_aeroporti"}),
	   @UniqueConstraint(columnNames={"id_autista", "id_autoveicolo","id_autista_porto_navale"})
	}
)

public class Tariffe extends BaseObject implements Serializable {
	private static final long serialVersionUID = 2176499309042993952L;
	
	
	private Long id;
	private TariffeValori tariffeValori = new TariffeValori();
	

	@Embedded
    @IndexedEmbedded
	public TariffeValori getTariffeValori() {
		return tariffeValori;
	}

	public void setTariffeValori(TariffeValori tariffeValori) {
		this.tariffeValori = tariffeValori;
	}


	private Autista autista;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_autista", nullable = false)
	public Autista getAutista() {
		return autista;
	}
    
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	
	
	private Autoveicolo autoveicolo;
	@ManyToOne
	@JoinColumn(name = "id_autoveicolo", nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}

	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	
	private AutistaZone autistaZone;
	@ManyToOne
	@JoinColumn(name = "id_zona", nullable = true)
	public AutistaZone getAutistaZone() {
		return autistaZone;
	}
	public void setAutistaZone(AutistaZone autistaZone) {
		this.autistaZone = autistaZone;
	}

	
	private AutistaAeroporti autistaAeroporti;
	@ManyToOne
	@JoinColumn(name = "id_autista_aeroporti", nullable = true)
	public AutistaAeroporti getAutistaAeroporti() {
		return autistaAeroporti;
	}
	public void setAutistaAeroporti(AutistaAeroporti autistaAeroporti) {
		this.autistaAeroporti = autistaAeroporti;
	}

	
	private AutistaPortiNavali autistaPortiNavali;
	@ManyToOne
	@JoinColumn(name = "id_autista_porto_navale", nullable = true)
	public AutistaPortiNavali getAutistaPortiNavali() {
		return autistaPortiNavali;
	}
	public void setAutistaPortiNavali(AutistaPortiNavali autistaPortiNavali) {
		this.autistaPortiNavali = autistaPortiNavali;
	}

	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_tariffa")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Tariffe() {
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tariffe)) {
            return false;
        }

        final Tariffe tariffe = (Tariffe) o;

        return !(id != null ? !id.equals(tariffe.id) : tariffe.id != null);

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

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
import javax.persistence.OneToOne;
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
@Table(name = "zona", uniqueConstraints= @UniqueConstraint( columnNames={"id_autista", "id_provincia"}))
/*@Table(name="zona",  uniqueConstraints={ @UniqueConstraint(columnNames={"id_autista", "id_regione"}), @UniqueConstraint(columnNames={"id_autista", "id_provincia"}),
	@UniqueConstraint(columnNames={"id_autista", "id_comune"}) }) */

public class AutistaZone extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5412476588589121362L;

	private Long id;
	private boolean servizioAttivo;
	
	
	private Autista autista;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autista", nullable = false)
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	
	private Set<Tariffe> tariffe;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autistaZone", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<Tariffe> getTariffe() {
		return tariffe;
	}
	public void setTariffe(Set<Tariffe> tariffe) {
		this.tariffe = tariffe;
	}


	/*
	 * FORSE TOGLIERE IL EAGER
	 */
	private Province province;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_provincia", unique = false)
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	
	
	private ZoneLungaPercorrenza zoneLungaPercorrenza;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "autistaZone", cascade = CascadeType.ALL)
	public ZoneLungaPercorrenza getZoneLungaPercorrenza() {
		return zoneLungaPercorrenza;
	}

	public void setZoneLungaPercorrenza(ZoneLungaPercorrenza zoneLungaPercorrenza) {
		this.zoneLungaPercorrenza = zoneLungaPercorrenza;
	}
	
	
	private ZoneGiornataCompleta zoneGiornataCompleta;
	@OneToOne (fetch = FetchType.LAZY, mappedBy = "autistaZone", cascade = CascadeType.ALL  )
	public ZoneGiornataCompleta getZoneGiornataCompleta() {
		return zoneGiornataCompleta;
	}
	

	public void setZoneGiornataCompleta(ZoneGiornataCompleta zoneGiornataCompleta) {
		this.zoneGiornataCompleta = zoneGiornataCompleta;
	}
	
	
	private ZoneMatrimoni zoneMatrimoni;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "autistaZone", cascade = CascadeType.ALL)
	public ZoneMatrimoni getZoneMatrimoni() {
		return zoneMatrimoni;
	}

	public void setZoneMatrimoni(ZoneMatrimoni zoneMatrimoni) {
		this.zoneMatrimoni = zoneMatrimoni;
	}


	public AutistaZone() { }
	
	public AutistaZone(Long id) {
		this.id = id;
	}

	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_zona")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(columnDefinition="tinyint(1) default 1")
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
        if (!(o instanceof AutistaZone)) {
            return false;
        }

        final AutistaZone tipoRuoloServiziDog = (AutistaZone) o;

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

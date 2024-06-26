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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_musei")
public class Musei extends BaseObject implements Serializable {
	private static final long serialVersionUID = -6546369939267109120L;
	
	
	private Long id;
	private String url;
	private String nomeMuseo;
	private String descrizione;
	private Comuni comuni;
	private String indirizzo;
	private String placeId;
	private double lat;
	private double lng;
	private int numeroVisiteAnno;
	
	
	private Long distanzaidProvinciaAndata;
	@Transient
	public Long getDistanzaidProvinciaAndata() {
		return distanzaidProvinciaAndata;
	}
	@Transient
	public void setDistanzaidProvinciaAndata(Long distanzaidProvinciaAndata) {
		this.distanzaidProvinciaAndata = distanzaidProvinciaAndata;
	}
	private Long metriDistanza;
	@Transient
	public Long getMetriDistanza() {
		return metriDistanza;
	}
	@Transient
	public void setMetriDistanza(Long metriDistanza) {
		this.metriDistanza = metriDistanza;
	}
	private BigDecimal tariffaBase;
	@Transient
	public BigDecimal getTariffaBase() {
		return tariffaBase;
	}
	@Transient
	public void setTariffaBase(BigDecimal tariffaBase) {
		this.tariffaBase = tariffaBase;
	}
	
	@ManyToOne
	@JoinColumn(name = "id_comune", nullable = false, unique= false)
	public Comuni getComuni() {
		return comuni;
	}
	public void setComuni(Comuni comuni) {
		this.comuni = comuni;
	}

	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Musei() {
    }
    
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_museo")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getNumeroVisiteAnno() {
		return numeroVisiteAnno;
	}
	public void setNumeroVisiteAnno(int numeroVisiteAnno) {
		this.numeroVisiteAnno = numeroVisiteAnno;
	}
	
	@Column(nullable = false, unique = true)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(unique = true)
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getNomeMuseo() {
		return nomeMuseo;
	}
	public void setNomeMuseo(String nomeMuseo) {
		this.nomeMuseo = nomeMuseo;
	}

	@Column(length = 500)
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Musei)) {
            return false;
        }

        final Musei tipoRuoloServiziDog = (Musei) o;

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

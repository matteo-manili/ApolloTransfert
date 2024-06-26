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
@Table(name = "data_aeroporti")
public class Aeroporti extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5430871785519408050L;

	private Long id;
	private String url;
	private String siglaAeroporto;
	private String nomeAeroporto;
	private String indirizzo;
	private String placeId;
	private double lat;
	private double lng;
	private int numeroVoliAnno; // modificare in "trafficoPasseggeriAnno" i dati sono stati presi da: https://it.wikipedia.org/wiki/Aeroporti_d%27Italia_per_traffico_passeggeri#Statistiche_2017
	
	
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
	

	private Comuni comuni;
	@ManyToOne
	@JoinColumn(name = "id_comune", nullable = false, unique = false)
	public Comuni getComuni() {
		return comuni;
	}
	public void setComuni(Comuni comuni) {
		this.comuni = comuni;
	}

	
    public Aeroporti() { }

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aeroporto")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	public int getNumeroVoliAnno() {
		return numeroVoliAnno;
	}
	public void setNumeroVoliAnno(int numeroVoliAnno) {
		this.numeroVoliAnno = numeroVoliAnno;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
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
	
	public String getSiglaAeroporto() {
		return siglaAeroporto;
	}
	public void setSiglaAeroporto(String siglaAeroporto) {
		this.siglaAeroporto = siglaAeroporto;
	}
	
	@Column(nullable = false)
	public String getNomeAeroporto() {
		return nomeAeroporto;
	}
	public void setNomeAeroporto(String nomeAeroporto) {
		this.nomeAeroporto = nomeAeroporto;
	}


	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aeroporti)) {
            return false;
        }

        final Aeroporti tipoRuoloServiziDog = (Aeroporti) o;

        return !(nomeAeroporto != null ? !nomeAeroporto.equals(tipoRuoloServiziDog.nomeAeroporto) : tipoRuoloServiziDog.nomeAeroporto != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (nomeAeroporto != null ? nomeAeroporto.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.nomeAeroporto)
                .toString();
    }

}

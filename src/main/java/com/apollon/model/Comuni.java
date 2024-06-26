package com.apollon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_comuni", uniqueConstraints = @UniqueConstraint(columnNames={"nomeComune", "id_provincia"}))
public class Comuni extends BaseObject implements Serializable {
	private static final long serialVersionUID = 6793698520330446432L;

	private Long id;
	boolean isola;
	private String nomeComune;
	private String catasto;
	private boolean isolaProvincia;
	
	@Transient
	public boolean isIsolaProvincia() {
		return isolaProvincia;
	}
	@Transient
	public void setIsolaProvincia(boolean isolaProvincia) {
		this.isolaProvincia = isolaProvincia;
	}

	//-------------------- NAZIONI --------------------------
    Nazioni nazione;
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_nazione", unique = false, nullable = true)
    public Nazioni getNazione() {
		return nazione;
	}
	public void setNazione(Nazioni nazione) {
		this.nazione = nazione;
	}
	
	//-------------------- REGIONI --------------------------
  	private Regioni regioni;
  	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_regione", unique = false, nullable = false)
  	public Regioni getRegioni() {
		return regioni;
	}
	public void setRegioni(Regioni regioni) {
		this.regioni = regioni;
	}
	
	//-------------------- PROVINCE --------------------------
	private Province province;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_provincia", unique = false, nullable = false)
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	
	
	
	

	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Comuni() {
	}
    
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_comune")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIsola() {
		return isola;
	}
	public void setIsola(boolean isola) {
		this.isola = isola;
	}

	@Column(nullable = false)
	public String getNomeComune() {
		return nomeComune;
	}
	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}

	public String getCatasto() {
		return catasto;
	}
	public void setCatasto(String catasto) {
		this.catasto = catasto;
	}
	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comuni)) {
            return false;
        }
        final Comuni comune = (Comuni) o;
        return !(id != null ? !id.equals(comune.id) : comune.id != null);
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

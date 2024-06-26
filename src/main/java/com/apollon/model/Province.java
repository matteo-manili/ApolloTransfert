package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.apollon.webapp.util.bean.TransferTariffe;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_province")
public class Province extends BaseObject implements Serializable {
	private static final long serialVersionUID = -7705004538180416445L;
	
	private Long id;
	private Integer percentualeServizio;
	private String url;
	boolean isola;
	private Integer numeroAbitanti;
	private BigDecimal tariffaBase;
	private String nomeProvincia;
	private String siglaProvincia;
	private double lat;
	private double lng;
	
	private Long metriDistanza;
	@Transient
	public Long getMetriDistanza() {
		return metriDistanza;
	}
	@Transient
	public void setMetriDistanza(Long metriDistanza) {
		this.metriDistanza = metriDistanza;
	}

	
    /**
     * Esempio: la Provincia Straniera San Marino è all'interno della provincia di Rimini. 
     * Significa che per farmi dare gli autisti presenti su San Marino mi devono tornare quelli di Rimini.
     */
	//-------------------- PROVINCE STRANIERE --------------------------
    private Set<Province> provinceStraniere_provinceItaliane = new HashSet<Province>();
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "data_province_straniere_province_italiane",
 	    joinColumns = @JoinColumn (name = "provincia_straniera_id"),
 	    inverseJoinColumns = @JoinColumn(name = "provincia_italiana_id"))
    public Set<Province> getProvinceStraniere_provinceItaliane() {
		return provinceStraniere_provinceItaliane;
	}
	public void setProvinceStraniere_provinceItaliane(Set<Province> province) {
		this.provinceStraniere_provinceItaliane = province;
	}
	public void addProvinceStraniere_provinceItaliane(Province province){
		getProvinceStraniere_provinceItaliane().add(province);
	}
	
	//-------------------- PROVINCE CONFINANTI --------------------------
	private Set<Province> provinceConfinanti = new HashSet<Province>();
	@ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
	@OrderBy("numeroAbitanti DESC")
    @JoinTable( name = "data_province_confinanti", joinColumns = { @JoinColumn(name = "provincia_id") }, inverseJoinColumns = @JoinColumn(name = "provincia_conf_id"))
	public Set<Province> getProvinceConfinanti() {
		return provinceConfinanti;
	}
	public void setProvinceConfinanti(Set<Province> provinceConfinanti) {
		this.provinceConfinanti = provinceConfinanti;
	}
	public void addProvinciaConfinante(Province provinciaConfinante) {
		getProvinceConfinanti().add(provinciaConfinante);
    }
	
	//le jsp i form spring path voglio solo liste di String, non di map o altri oggetti più complessi....
	private List<String> provinceConfinanti_TAG = new ArrayList<String>();
	@Transient
	public List<String> getProvinceConfinanti_TAG() {
		return provinceConfinanti_TAG;
	}
	@Transient
	public void setProvinceConfinanti_TAG(List<String> provinceConfinanti_TAG) {
		this.provinceConfinanti_TAG = provinceConfinanti_TAG;
	}

	
	// @ManyToOne(fetch = FetchType.EAGER)
	//-------------------- REGIONI --------------------------
	private Regioni regioni;
  	@ManyToOne(fetch = FetchType.EAGER)
  	@OrderBy("nomeRegione")
  	@JoinColumn(name = "id_regione", unique = false, nullable = false)
	public Regioni getRegioni() {
		return regioni;
	}
	public void setRegioni(Regioni regioni) {
		this.regioni = regioni;
	}
  	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Province() {
	}
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_provincia")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false) 
	public Integer getPercentualeServizio() {
		return percentualeServizio;
	}
	public void setPercentualeServizio(Integer percentualeServizio) {
		this.percentualeServizio = percentualeServizio;
	}
	
	@Column(nullable = false, unique = true)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isIsola() {
		return isola;
	}
	public void setIsola(boolean isola) {
		this.isola = isola;
	}

	public Integer getNumeroAbitanti() {
		return numeroAbitanti;
	}
	public void setNumeroAbitanti(Integer numeroAbitanti) {
		this.numeroAbitanti = numeroAbitanti;
	}

	public BigDecimal getTariffaBase() {
		return tariffaBase;
	}
	public void setTariffaBase(BigDecimal tariffaBase) {
		this.tariffaBase = tariffaBase;
	}

	@Column(nullable = false)
	public String getNomeProvincia() {
		return nomeProvincia;
	}
	public void setNomeProvincia(String nomeProvincia) {
		this.nomeProvincia = nomeProvincia;
	}

	@Column(unique = true, nullable = false)
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
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

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }

        final Province provincia = (Province) o;

        return !(id != null ? !id.equals(provincia.id) : provincia.id != null);

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

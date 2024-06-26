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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_regioni")
public class Regioni extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8034684256470877141L;
	
	private Long id;
	private String url;
	boolean isola;
    private String nomeRegione;
    
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
    
    //-------------------- CAPOLUOGO DI PROVINCIA --------------------------
    Province capoluogoProvincia;
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_capoluogo_provincia", unique = true, nullable = true)
    public Province getCapoluogoProvincia() {
		return capoluogoProvincia;
	}
	public void setCapoluogoProvincia(Province capoluogoProvincia) {
		this.capoluogoProvincia = capoluogoProvincia;
	}
	
	//-------------------- MACRO REGIONI --------------------------
    MacroRegioni macroRegioni;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_macro_regioni", unique = false, nullable = true)
    public MacroRegioni getMacroRegioni() {
		return macroRegioni;
	}
	public void setMacroRegioni(MacroRegioni macroRegioni) {
		this.macroRegioni = macroRegioni;
	}
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Regioni() {
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_regione")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	@Column(nullable = false)
	public String getNomeRegione() {
		return nomeRegione;
	}
	public void setNomeRegione(String nomeRegione) {
		this.nomeRegione = nomeRegione;
	}
	


	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regioni)) {
            return false;
        }

        final Regioni regione = (Regioni) o;

        return !(id != null ? !id.equals(regione.id) : regione.id != null);

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

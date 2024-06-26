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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name="aga_autoveicolo_modelli_giornate")
public class AgA_AutoveicoloModelliGiornate extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1333134336173843480L;
	
	private Long id;
	private String nomeGiornata;

	private Autoveicolo autoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autoveicolo", unique = false, nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	
	private Set<AgA_ModelliGiornate> agA_ModelliGiornate; 
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "agA_AutoveicoloModelliGiornate", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<AgA_ModelliGiornate> getAgA_ModelliGiornate() {
		return agA_ModelliGiornate;
	}
	public void setAgA_ModelliGiornate(Set<AgA_ModelliGiornate> agA_ModelliGiornate) {
		this.agA_ModelliGiornate = agA_ModelliGiornate;
	}
	
	public AgA_AutoveicoloModelliGiornate() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_autoveicolo_modelli_giornate")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeGiornata() {
		return nomeGiornata;
	}
	public void setNomeGiornata(String nomeGiornata) {
		this.nomeGiornata = nomeGiornata;
	}

	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgA_AutoveicoloModelliGiornate)) {
            return false;
        }

        final AgA_AutoveicoloModelliGiornate richiestaMedia = (AgA_AutoveicoloModelliGiornate) o;

        return !(id != null ? !id.equals(richiestaMedia.id) : richiestaMedia.id != null);

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

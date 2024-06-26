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
@Table(name="aga_autoveicolo_modelli_tariffari")
public class AgA_AutoveicoloModelliTariffari extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1333134336173843480L;
	
	private Long id;
	private String nomeTariffario;
	
	private Autoveicolo autoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autoveicolo", unique = false, nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	
	private Set<AgA_ModelliTariffari> AgA_ModelliTariffari; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agA_AutoveicoloModelliTariffari", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<AgA_ModelliTariffari> getAgA_ModelliTariffari() {
		return AgA_ModelliTariffari;
	}
	public void setAgA_ModelliTariffari(Set<AgA_ModelliTariffari> agA_ModelliTariffari) {
		AgA_ModelliTariffari = agA_ModelliTariffari;
	}
	
	private Set<AgA_ModelliGiornate> agA_ModelliGiornate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agA_AutoveicoloModelliTariffari", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<AgA_ModelliGiornate> getAgA_ModelliGiornate() {
		return agA_ModelliGiornate;
	}
	public void setAgA_ModelliGiornate(Set<AgA_ModelliGiornate> agA_ModelliGiornate) {
		this.agA_ModelliGiornate = agA_ModelliGiornate;
	}
	
	private Set<AgA_Tariffari> agA_Tariffari; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agA_AutoveicoloModelliTariffari", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<AgA_Tariffari> getAgA_Tariffari() {
		return agA_Tariffari;
	}
	public void setAgA_Tariffari(Set<AgA_Tariffari> agA_Tariffari) {
		this.agA_Tariffari = agA_Tariffari;
	}
	
	
	

	
	
	public AgA_AutoveicoloModelliTariffari() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_autoveicolo_modelli_tariffari")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeTariffario() {
		return nomeTariffario;
	}
	public void setNomeTariffario(String nomeTariffario) {
		this.nomeTariffario = nomeTariffario;
	}

	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgA_AutoveicoloModelliTariffari)) {
            return false;
        }

        final AgA_AutoveicoloModelliTariffari richiestaMedia = (AgA_AutoveicoloModelliTariffari) o;

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

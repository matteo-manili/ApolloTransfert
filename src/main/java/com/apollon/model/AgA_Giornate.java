package com.apollon.model;

import java.io.Serializable;
import java.util.Date;
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
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name="aga_giornate", uniqueConstraints={@UniqueConstraint(columnNames={"dataGiornataOrario","id_autoveicolo"})})
public class AgA_Giornate extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5384528772532019236L;

	private Long id;
	
	private Date dataGiornataOrario; 
	private boolean attivo;
	
	private Set<AgA_Tariffari> AgA_Tariffari; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agA_Giornate" /*, cascade = CascadeType.REMOVE*/)
	@Fetch(FetchMode.SELECT)
	public Set<AgA_Tariffari> getAgA_Tariffari() {
		return AgA_Tariffari;
	}
	public void setAgA_Tariffari(Set<AgA_Tariffari> agA_Tariffari) {
		AgA_Tariffari = agA_Tariffari;
	}
	
	private Autoveicolo autoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autoveicolo", unique = false, nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	public AgA_Giornate(Date dataGiornataOrario, boolean attivo, Autoveicolo autoveicolo) {
		super();
		this.dataGiornataOrario = dataGiornataOrario;
		this.attivo = attivo;
		this.autoveicolo = autoveicolo;
	}
	
	public AgA_Giornate() { }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aga_giornate")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Date getDataGiornataOrario() {
		return dataGiornataOrario;
	}
	public void setDataGiornataOrario(Date dataGiornataOrario) {
		this.dataGiornataOrario = dataGiornataOrario;
	}
	
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgA_Giornate)) {
            return false;
        }

        final AgA_Giornate richiestaMedia = (AgA_Giornate) o;

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

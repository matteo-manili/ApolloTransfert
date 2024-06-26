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
@Table(name="data_comunicazioni")
public class Comunicazioni extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1333134336173843480L;
	
	private Long id;
	private String comunicazione;

	private Set<ComunicazioniUser> comunicazioniUser; 
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "comunicazioni", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<ComunicazioniUser> getComunicazioniUser() {
		return comunicazioniUser;
	}
	public void setComunicazioniUser(Set<ComunicazioniUser> comunicazioniUser) {
		this.comunicazioniUser = comunicazioniUser;
	}
	
	
	public Comunicazioni() { }
	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_data_comunicazioni")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = true, nullable = false)
	public String getComunicazione() {
		return comunicazione;
	}
	public void setComunicazione(String comunicazione) {
		this.comunicazione = comunicazione;
	}
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comunicazioni)) {
            return false;
        }

        final Comunicazioni richiestaMedia = (Comunicazioni) o;

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

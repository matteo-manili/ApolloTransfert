package com.apollon.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_nazioni")
public class Nazioni extends BaseObject implements Serializable {
	private static final long serialVersionUID = -828633901760052289L;
	
	private Long id;
	private String url;
	boolean isola;
    private String siglaNazione;
    
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Nazioni() {
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_nazione")
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

	@Column(length = 5, nullable = false)
	public String getSiglaNazione() {
		return siglaNazione;
	}
	public void setSiglaNazione(String siglaNazione) {
		this.siglaNazione = siglaNazione;
	}
	


	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nazioni)) {
            return false;
        }
        final Nazioni nazione = (Nazioni) o;
        return !(id != null ? !id.equals(nazione.id) : nazione.id != null);

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

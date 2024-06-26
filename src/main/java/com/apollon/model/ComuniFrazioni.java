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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_comuni_frazioni", uniqueConstraints= @UniqueConstraint(columnNames={"nomeFrazione", "id_comune"}))
public class ComuniFrazioni extends BaseObject implements Serializable {
	private static final long serialVersionUID = -3106853326782121501L;
	
	private Long id;
	boolean isola;
	private String nomeFrazione;
    
	
	//-------------------- COMUNE --------------------------
	private Comuni comune;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_comune", unique = false, nullable = false)
	public Comuni getComune() {
		return comune;
	}
	public void setComune(Comuni comune) {
		this.comune = comune;
	}
	

	/**
     * Default constructor - creates a new instance with no values set.
     */
    public ComuniFrazioni() {
	}
    
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_frazione")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getNomeFrazione() {
		return nomeFrazione;
	}
	
	public void setNomeFrazione(String nomeFrazione) {
		this.nomeFrazione = nomeFrazione;
	}

	public boolean isIsola() {
		return isola;
	}

	public void setIsola(boolean isola) {
		this.isola = isola;
	}
	
	
	
	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComuniFrazioni)) {
            return false;
        }
        final ComuniFrazioni comune = (ComuniFrazioni) o;
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

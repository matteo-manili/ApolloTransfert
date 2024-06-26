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
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name = "autista_licenze_ncc", uniqueConstraints = @UniqueConstraint(columnNames={"numeroLicenza", "id_comune"})  )
public class AutistaLicenzeNcc extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8849894705024993642L;

	private Long id;
	
	private boolean approvato;
	private String nomeFileDocumentoLicenza;
	private Integer numeroLicenza;
	
	
	private Comuni comune;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_comune", unique = false, nullable = false)
	public Comuni getComune() {
		return comune;
	}

	public void setComune(Comuni comune) {
		this.comune = comune;
	}
	
	
	Autista autista;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_autista", unique = false, nullable = false)
	public Autista getAutista() {
		return autista;
	}

	public void setAutista(Autista autista) {
		this.autista = autista;
	}


	public AutistaLicenzeNcc() {
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autista_licenze_ncc")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isApprovato() {
		return approvato;
	}

	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}

	@Column(unique = false, nullable = false)
	public Integer getNumeroLicenza() {
		return numeroLicenza;
	}

	public void setNumeroLicenza(Integer numeroLicenza) {
		this.numeroLicenza = numeroLicenza;
	}
	

	@Column(length = 150)
	public String getNomeFileDocumentoLicenza() {
		return nomeFileDocumentoLicenza;
	}

	public void setNomeFileDocumentoLicenza(String nomeFileDocumentoLicenza) {
		this.nomeFileDocumentoLicenza = nomeFileDocumentoLicenza;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutistaLicenzeNcc)) {
            return false;
        }

        final AutistaLicenzeNcc autistaLicenzeNcc = (AutistaLicenzeNcc) o;

        return !(id != null ? !id.equals(autistaLicenzeNcc.id) : autistaLicenzeNcc.id != null);

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

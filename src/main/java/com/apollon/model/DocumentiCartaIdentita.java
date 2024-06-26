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
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name = "documenti_carta_identita")
public class DocumentiCartaIdentita extends BaseObject implements Serializable {
	private static final long serialVersionUID = -1664691334741326907L;

	private Long id;

	private String numeroCartaIdentita;
    private String nomeFileCartaIdentitaFronte;
    private String nomeFileCartaIdentitaRetro;
    
    
	public DocumentiCartaIdentita() {
		
	}
	
	public DocumentiCartaIdentita(String numeroCartaIdentita) {
		this.numeroCartaIdentita = numeroCartaIdentita;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_documenti_carta_identita")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 50, unique = true, nullable = true)
	public String getNumeroCartaIdentita() {
		return numeroCartaIdentita;
	}

	public void setNumeroCartaIdentita(String numeroCartaIdentita) {
		this.numeroCartaIdentita = numeroCartaIdentita;
	}
	
	@Column(length = 150)
	public String getNomeFileCartaIdentitaFronte() {
		return nomeFileCartaIdentitaFronte;
	}

	public void setNomeFileCartaIdentitaFronte(String nomeFileCartaIdentitaFronte) {
		this.nomeFileCartaIdentitaFronte = nomeFileCartaIdentitaFronte;
	}

	@Column(length = 150)
	public String getNomeFileCartaIdentitaRetro() {
		return nomeFileCartaIdentitaRetro;
	}

	public void setNomeFileCartaIdentitaRetro(String nomeFileCartaIdentitaRetro) {
		this.nomeFileCartaIdentitaRetro = nomeFileCartaIdentitaRetro;
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentiCartaIdentita)) {
            return false;
        }

        final DocumentiCartaIdentita documentiCartaIdentita = (DocumentiCartaIdentita) o;

        return !(id != null ? !id.equals(documentiCartaIdentita.id) : documentiCartaIdentita.id != null);

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

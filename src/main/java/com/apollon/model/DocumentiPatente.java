package com.apollon.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "documenti_patente")
public class DocumentiPatente extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1466517168673874803L;

	private Long id;

	private Date dataScadenzaPatente;
	private String numeroPatente;
    private String nomeFilePatenteFronte;
    private String nomeFilePatenteRetro;
    
	public DocumentiPatente() {
	}
	
	public DocumentiPatente(String numeroPatente, Date dataScadenzaPatente, String nomeFilePatenteFronte, String nomeFilePatenteRetro) {
		this.numeroPatente = numeroPatente;
		this.dataScadenzaPatente = dataScadenzaPatente;
		this.nomeFilePatenteFronte = nomeFilePatenteFronte;
		this.nomeFilePatenteRetro = nomeFilePatenteRetro;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_documenti_patente")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataScadenzaPatente() {
		return dataScadenzaPatente;
	}
	public void setDataScadenzaPatente(Date dataScadenzaPatente) {
		this.dataScadenzaPatente = dataScadenzaPatente;
	}

	@Column(length = 50, unique = true, nullable = true)
	public String getNumeroPatente() {
		return numeroPatente;
	}
	public void setNumeroPatente(String numeroPatente) {
		this.numeroPatente = numeroPatente;
	}
	
	@Column(length = 150)
	public String getNomeFilePatenteFronte() {
		return nomeFilePatenteFronte;
	}
	public void setNomeFilePatenteFronte(String nomeFilePatenteFronte) {
		this.nomeFilePatenteFronte = nomeFilePatenteFronte;
	}

	@Column(length = 150)
	public String getNomeFilePatenteRetro() {
		return nomeFilePatenteRetro;
	}
	public void setNomeFilePatenteRetro(String nomeFilePatenteRetro) {
		this.nomeFilePatenteRetro = nomeFilePatenteRetro;
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentiPatente)) {
            return false;
        }
        final DocumentiPatente autistaLicenzeNcc = (DocumentiPatente) o;
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

package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "documenti_iscrizione_ruolo", uniqueConstraints = @UniqueConstraint(columnNames={"numeroRuoloConducenti","id_provincia_ruolo_conducenti"}))
public class DocumentiIscrizioneRuolo extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1527222623101122504L;

	private Long id;

	private Date dataIscrizioneRuoloConducenti;
    private String numeroRuoloConducenti;
    private String nomeFileDocumentoRuoloConducenti;
    
    
    public DocumentiIscrizioneRuolo() {
	}
    
    
    public DocumentiIscrizioneRuolo(Date dataIscrizioneRuoloConducenti, String numeroRuoloConducenti,
			String nomeFileDocumentoRuoloConducenti, Province provinciaRuoloConducenti) {
		this.dataIscrizioneRuoloConducenti = dataIscrizioneRuoloConducenti;
		this.numeroRuoloConducenti = numeroRuoloConducenti;
		this.nomeFileDocumentoRuoloConducenti = nomeFileDocumentoRuoloConducenti;
		this.provinciaRuoloConducenti = provinciaRuoloConducenti;
	}




	private Province provinciaRuoloConducenti;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_provincia_ruolo_conducenti", unique = false, nullable = false)
	public Province getProvinciaRuoloConducenti() {
		return provinciaRuoloConducenti;
	}

	public void setProvinciaRuoloConducenti(Province provinciaRuoloConducenti) {
		this.provinciaRuoloConducenti = provinciaRuoloConducenti;
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_documenti_iscrizione_ruolo")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 50)
	public String getNumeroRuoloConducenti() {
		return numeroRuoloConducenti;
	}
	
	public void setNumeroRuoloConducenti(String numeroRuoloConducenti) {
		this.numeroRuoloConducenti = numeroRuoloConducenti;
	}
	
	@Column(length = 150)
	public String getNomeFileDocumentoRuoloConducenti() {
		return nomeFileDocumentoRuoloConducenti;
	}

	public void setNomeFileDocumentoRuoloConducenti(
			String nomeFileDocumentoRuoloConducenti) {
		this.nomeFileDocumentoRuoloConducenti = nomeFileDocumentoRuoloConducenti;
	}

	public Date getDataIscrizioneRuoloConducenti() {
		return dataIscrizioneRuoloConducenti;
	}

	public void setDataIscrizioneRuoloConducenti(Date dataIscrizioneRuoloConducenti) {
		this.dataIscrizioneRuoloConducenti = dataIscrizioneRuoloConducenti;
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentiIscrizioneRuolo)) {
            return false;
        }

        final DocumentiIscrizioneRuolo autistaLicenzeNcc = (DocumentiIscrizioneRuolo) o;

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

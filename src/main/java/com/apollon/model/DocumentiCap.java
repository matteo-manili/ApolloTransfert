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
@Table(name = "documenti_cap")
public class DocumentiCap extends BaseObject implements Serializable {
	private static final long serialVersionUID = -1683255091655071201L;

	private Long id;
	
	private String numeroCAP;
	private Date dataScadenzaCAP;
    private String nomeFileCAP;
    
    
	public DocumentiCap() {
	}

	public DocumentiCap(String numeroCAP, Date dataScadenzaCAP, String nomeFileCAP) {
		this.numeroCAP = numeroCAP;
		this.dataScadenzaCAP = dataScadenzaCAP;
		this.nomeFileCAP = nomeFileCAP;
	}
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_documenti_cap")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataScadenzaCAP() {
		return dataScadenzaCAP;
	}

	public void setDataScadenzaCAP(Date dataScadenzaCAP) {
		this.dataScadenzaCAP = dataScadenzaCAP;
	}

	@Column(length = 50, unique = true, nullable = true)
	public String getNumeroCAP() {
		return numeroCAP;
	}

	public void setNumeroCAP(String numeroCAP) {
		this.numeroCAP = numeroCAP;
	}

	@Column(length = 150)
	public String getNomeFileCAP() {
		return nomeFileCAP;
	}

	public void setNomeFileCAP(String nomeFileCAP) {
		this.nomeFileCAP = nomeFileCAP;
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentiCap)) {
            return false;
        }

        final DocumentiCap autistaLicenzeNcc = (DocumentiCap) o;

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

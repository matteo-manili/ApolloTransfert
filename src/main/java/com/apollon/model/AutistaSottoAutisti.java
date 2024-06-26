package com.apollon.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name = "autista_sotto_autisti", uniqueConstraints = @UniqueConstraint(columnNames={"id_autista","id_documenti_iscrizione_ruolo","id_documenti_patente","id_documenti_cap"}))
public class AutistaSottoAutisti extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8741265771630127137L;
	
	private Long id;
	private boolean approvato;
	private String nome;
    private String cognome;
    
   
    /**
     * il Responsabile della Azienda o Il presidente della Coopertiva 
     */
	private Autista autista;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_autista", unique = false, nullable = false)
	public Autista getAutista() {
		return autista;
	}

	public void setAutista(Autista autista) {
		this.autista = autista;
	}
    
	
	private DocumentiIscrizioneRuolo documentiIscrizioneRuolo;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_iscrizione_ruolo", unique = true, nullable = false)
	public DocumentiIscrizioneRuolo getDocumentiIscrizioneRuolo() {
		return documentiIscrizioneRuolo;
	}

	public void setDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) {
		this.documentiIscrizioneRuolo = documentiIscrizioneRuolo;
	}


	private DocumentiPatente documentiPatente;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_patente", unique = true, nullable = false)
	public DocumentiPatente getDocumentiPatente() {
		return documentiPatente;
	}

	public void setDocumentiPatente(DocumentiPatente documentiPatente) {
		this.documentiPatente = documentiPatente;
	}
	
	
	private DocumentiCap documentiCap;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_cap", unique = true, nullable = false)
	public DocumentiCap getDocumentiCap() {
		return documentiCap;
	}

	public void setDocumentiCap(DocumentiCap documentiCap) {
		this.documentiCap = documentiCap;
	}

	public AutistaSottoAutisti() {}
	    
    public AutistaSottoAutisti(boolean approvato, String nome, String cognome,
			Autista autista, DocumentiIscrizioneRuolo documentiIscrizioneRuolo,
			DocumentiPatente documentiPatente, DocumentiCap documentiCap) {
		this.approvato = approvato;
		this.nome = nome;
		this.cognome = cognome;
		this.autista = autista;
		this.documentiIscrizioneRuolo = documentiIscrizioneRuolo;
		this.documentiPatente = documentiPatente;
		this.documentiCap = documentiCap;
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autista_sotto_autisti")
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

	@Column(length = 50)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(length = 50)
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutistaSottoAutisti)) {
            return false;
        }

        final AutistaSottoAutisti autistaLicenzeNcc = (AutistaSottoAutisti) o;

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

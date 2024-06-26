package com.apollon.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import org.hibernate.search.annotations.Indexed;

/**
 * This class is used to represent an address with address,
 * city, province and postal-code information.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Embeddable
@Indexed
public class AutistaDocumento extends BaseObject implements Serializable {
    private static final long serialVersionUID = 6606940214446259018L;
    
    private boolean approvatoGenerale;
    
    private String documentoAggiuntivo; // eventuale documento aggiuntivo per esempio visura aziendale Autista
	private String messaggioAutistaDocumenti;
    private boolean approvatoContratto;
    private String nomeFileContratto;
    private String nomeFileContratto_2; // eventuale seconda pagina perke il contratto si sta allungando
    
	private boolean approvatoDocumenti;
    private String numeroCartaIdentita;
    private String nomeFileCartaIdentita;
	private String partitaIva;
	private String partitaIvaDenominazione;
	private String iban;
    
	private DocumentiPatente documentiPatente;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_patente", unique = true, nullable = true)
	public DocumentiPatente getDocumentiPatente() {
		return documentiPatente;
	}
	public void setDocumentiPatente(DocumentiPatente documentiPatente) {
		this.documentiPatente = documentiPatente;
	}
	
	private DocumentiIscrizioneRuolo documentiIscrizioneRuolo;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_iscrizione_ruolo", unique = true, nullable = true)
	public DocumentiIscrizioneRuolo getDocumentiIscrizioneRuolo() {
		return documentiIscrizioneRuolo;
	}
	public void setDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) {
		this.documentiIscrizioneRuolo = documentiIscrizioneRuolo;
	}
	
	private DocumentiCap documentiCap;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_documenti_cap", unique = true, nullable = true)
	public DocumentiCap getDocumentiCap() {
		return documentiCap;
	}
	public void setDocumentiCap(DocumentiCap documentiCap) {
		this.documentiCap = documentiCap;
	}

	public boolean isApprovatoGenerale() {
		return approvatoGenerale;
	}
	public void setApprovatoGenerale(boolean approvatoGenerale) {
		this.approvatoGenerale = approvatoGenerale;
	}
	
	@Column(length = 1000)
	public String getMessaggioAutistaDocumenti() {
		return messaggioAutistaDocumenti;
	}
	public void setMessaggioAutistaDocumenti(String messaggioAutistaDocumenti) {
		this.messaggioAutistaDocumenti = messaggioAutistaDocumenti;
	}
	
	public boolean isApprovatoContratto() {
		return approvatoContratto;
	}
	public void setApprovatoContratto(boolean approvatoContratto) {
		this.approvatoContratto = approvatoContratto;
	}
	
	public String getDocumentoAggiuntivo() {
		return documentoAggiuntivo;
	}
	public void setDocumentoAggiuntivo(String documentoAggiuntivo) {
		this.documentoAggiuntivo = documentoAggiuntivo;
	}

	@Column(length = 150)
	public String getNomeFileContratto() {
		return nomeFileContratto;
	}
	public void setNomeFileContratto(String nomeFileContratto) {
		this.nomeFileContratto = nomeFileContratto;
	}
	
	@Column(length = 150)
	public String getNomeFileContratto_2() {
		return nomeFileContratto_2;
	}
	public void setNomeFileContratto_2(String nomeFileContratto_2) {
		this.nomeFileContratto_2 = nomeFileContratto_2;
	}

	public boolean isApprovatoDocumenti() {
		return approvatoDocumenti;
	}
	public void setApprovatoDocumenti(boolean approvatoDocumenti) {
		this.approvatoDocumenti = approvatoDocumenti;
	}

	@Column(length = 50, unique = true, nullable = true)
	public String getNumeroCartaIdentita() {
		return numeroCartaIdentita;
	}
	public void setNumeroCartaIdentita(String numeroCartaIdentita) {
		this.numeroCartaIdentita = numeroCartaIdentita;
	}

	@Column(length = 150)
	public String getNomeFileCartaIdentita() {
		return nomeFileCartaIdentita;
	}
	public void setNomeFileCartaIdentita(String nomeFileCartaIdentita) {
		this.nomeFileCartaIdentita = nomeFileCartaIdentita;
	}

	@Column(length = 50, unique = true, nullable = true)
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	@Column(length = 150)
	public String getPartitaIvaDenominazione() {
		return partitaIvaDenominazione;
	}
	public void setPartitaIvaDenominazione(String partitaIvaDenominazione) {
		this.partitaIvaDenominazione = partitaIvaDenominazione;
	}

	@Column(length = 50, unique = true, nullable = true)
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	


	/**
     * Overridden equals method for object comparison. Compares based on hashCode.
     *
     * @param o Object to compare
     * @return true/false based on hashCode
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutistaDocumento)) {
            return false;
        }

        final AutistaDocumento autistaDocumento = (AutistaDocumento) o;

        return this.hashCode() == autistaDocumento.hashCode();
    }

    /**
     * Overridden hashCode method - compares on address, city, province, country and postal code.
     *
     * @return hashCode
     */
    public int hashCode() {
        int result;
        result = (documentiPatente != null ? documentiPatente.hashCode() : 0);
        return result;
    }

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("numeroPatente", this.documentiPatente)
                .toString();
    }
}

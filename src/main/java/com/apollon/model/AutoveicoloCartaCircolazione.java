package com.apollon.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * This class is used to represent an address with address,
 * city, province and postal-code information.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Embeddable
@Indexed
public class AutoveicoloCartaCircolazione extends BaseObject implements Serializable {
    private static final long serialVersionUID = -5696350534700196174L;
    
	/**
     * TODO RICORDATI UN AUTOVEICOLO PER ESSERE DISPONIBILE AL TRANSFERT DEVE ESSERE ATTIVO, NON SOSPESO, E APPRAVA CARTA CIRCOLAZIONE !!!!!!
     */
	private boolean approvatoCartaCircolazione;
    private String nomeFileCartaCircolazione;

    
	public boolean isApprovatoCartaCircolazione() {
		return approvatoCartaCircolazione;
	}

	public void setApprovatoCartaCircolazione(boolean approvatoCartaCircolazione) {
		this.approvatoCartaCircolazione = approvatoCartaCircolazione;
	}

	@Column(length = 150)
    @Field
	public String getNomeFileCartaCircolazione() {
		return nomeFileCartaCircolazione;
	}

	public void setNomeFileCartaCircolazione(String nomeFileCartaCircolazione) {
		this.nomeFileCartaCircolazione = nomeFileCartaCircolazione;
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
        if (!(o instanceof AutoveicoloCartaCircolazione)) {
            return false;
        }

        final AutoveicoloCartaCircolazione autoveicoloCartaCircolazione = (AutoveicoloCartaCircolazione) o;

        return this.hashCode() == autoveicoloCartaCircolazione.hashCode();
    }

    /**
     * Overridden hashCode method - compares on address, city, province, country and postal code.
     *
     * @return hashCode
     */
    public int hashCode() {
        int result;
        result = (nomeFileCartaCircolazione != null ? nomeFileCartaCircolazione.hashCode() : 0);
        return result;
    }

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("nomeFileCartaCircolazione", this.nomeFileCartaCircolazione)
                .toString();
    }
}

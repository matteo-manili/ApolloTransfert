package com.apollon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name="richiesta_media_autista_autoveicolo")
public class RichiestaMediaAutistaAutoveicolo extends BaseObject implements Serializable {
	private static final long serialVersionUID = -6706913388968778814L;

	
	private Long id;
	
	
	private RichiestaMediaAutista richiestaMediaAutista;
	@ManyToOne
	@JoinColumn(name = "id_richiesta_media_autista", nullable = false)
	public RichiestaMediaAutista getRichiestaAutistaMedio() {
		return richiestaMediaAutista;
	}

	public void setRichiestaAutistaMedio(RichiestaMediaAutista richiestaMediaAutista) {
		this.richiestaMediaAutista = richiestaMediaAutista;
	}


	private Autoveicolo autoveicolo;
	@ManyToOne
	@JoinColumn(name = "id_autoveicolo", nullable = false)
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}

	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	
	public RichiestaMediaAutistaAutoveicolo() {
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_richiesta_media_autista_autoveicolo")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RichiestaMediaAutistaAutoveicolo)) {
            return false;
        }

        final RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo = (RichiestaMediaAutistaAutoveicolo) o;

        return !(id != null ? !id.equals(richiestaMediaAutistaAutoveicolo.id) : richiestaMediaAutistaAutoveicolo.id != null);

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

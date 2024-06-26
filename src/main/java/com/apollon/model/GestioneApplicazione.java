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
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_gestione_applicazione")
public class GestioneApplicazione extends BaseObject implements Serializable {
	private static final long serialVersionUID = -3650546151701132616L;
	
	private Long id;
	
    private String name;
    private String valueString;
    private Long valueNumber;
    private String commento;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
    @Column(unique = true)
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    @Column(name="value_number")
    public Long getValueNumber() {
		return valueNumber;
	}
	public void setValueNumber(Long valueNumber) {
		this.valueNumber = valueNumber;
	}

	@Column(name="value_string")
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public String getCommento() {
		return commento;
	}
	public void setCommento(String commento) {
		this.commento = commento;
	}

	/**
     * Default constructor - creates a new instance with no values set.
     */
    public GestioneApplicazione() {
    }
	public GestioneApplicazione(final String name) {
        this.name = name;
	}
	

	
	

	
	


	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GestioneApplicazione)) {
            return false;
        }

        final GestioneApplicazione tipoRuoloServiziDog = (GestioneApplicazione) o;

        return !(name != null ? !name.equals(tipoRuoloServiziDog.name) : tipoRuoloServiziDog.name != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

}

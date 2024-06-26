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
@Table(name = "data_classe_autoveicolo")
public class ClasseAutoveicolo extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5364901451282342213L;

	private Long id;
	private String nome;
	private String description;
	
	public ClasseAutoveicolo() {
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_classe_autoveicolo")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClasseAutoveicolo)) {
            return false;
        }
        final ClasseAutoveicolo tipoRuoloServiziDog = (ClasseAutoveicolo) o;
        return !(id != null ? !id.equals(tipoRuoloServiziDog.id) : tipoRuoloServiziDog.id != null);

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

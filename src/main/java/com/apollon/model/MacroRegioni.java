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
@Table(name = "data_macro_regioni")
public class MacroRegioni extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1704531305923756036L;

	private Long id;
	private String url;
    private String nome;
    private String description;


    /**
     * Default constructor - creates a new instance with no values set.
     */   
    public MacroRegioni() {
	}
    
	public MacroRegioni(String nome) {
		this.nome = nome;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_macro_regioni")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false, unique = true)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Column(nullable = false)
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
        if (!(o instanceof MacroRegioni)) {
            return false;
        }

        final MacroRegioni tipoRuoloServiziDog = (MacroRegioni) o;

        return !(nome != null ? !nome.equals(tipoRuoloServiziDog.nome) : tipoRuoloServiziDog.nome != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (nome != null ? nome.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.nome)
                .toString();
    }

}

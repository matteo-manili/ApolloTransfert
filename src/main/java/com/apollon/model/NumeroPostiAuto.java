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

/*
 * 

1
description default 1-3 posti
tipoAutoveicoli.autoveicolo.1

2
description default 1-5 posti
tipoAutoveicoli.autoveicolo.2

3
description default 1-8 posti
tipoAutoveicoli.autoveicolo.3
 */

@Entity
@Table(name = "data_numero_posti_auto") // ex data_tipo_auto - TipoAutoveicolo, ho rinonimato il nome della tabella
public class NumeroPostiAuto extends BaseObject implements Serializable {
	private static final long serialVersionUID = -1460614946677404405L;
	
	private Long id;
    private Integer numero;
    private String description;
    
    public NumeroPostiAuto() { }
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_numero_posti_auto")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(unique = true, nullable = false)
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
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
        if (!(o instanceof NumeroPostiAuto)) {
            return false;
        }
        final NumeroPostiAuto numeroPostiAuto = (NumeroPostiAuto) o;
        return !(numero != null ? !numero.equals(numeroPostiAuto.numero) : numeroPostiAuto.numero != null);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (numero != null ? numero.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.numero).toString();
    }

}

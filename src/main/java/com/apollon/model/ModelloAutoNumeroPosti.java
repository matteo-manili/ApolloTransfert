package com.apollon.model;

import java.io.Serializable;
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
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_modello_auto_numero_posti", uniqueConstraints=@UniqueConstraint(columnNames={"id_modello_autoscout", "id_numero_posti_auto"}))
public class ModelloAutoNumeroPosti extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5109357150400901404L;
	
	private Long id;
	
	private ModelloAutoScout modelloAutoScout;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_modello_autoscout", unique = false, nullable = false)
	public ModelloAutoScout getModelloAutoScout() {
		return modelloAutoScout;
	}

	public void setModelloAutoScout(ModelloAutoScout modelloAutoScout) {
		this.modelloAutoScout = modelloAutoScout;
	}
	
	
	private NumeroPostiAuto numeroPostiAuto;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_numero_posti_auto", unique = false, nullable = false)
	//@OrderBy("numero ASC")
	public NumeroPostiAuto getNumeroPostiAuto() {
		return numeroPostiAuto;
	}

	public void setNumeroPostiAuto(NumeroPostiAuto numeroPostiAuto) {
		this.numeroPostiAuto = numeroPostiAuto;
	}
	
	
	public ModelloAutoNumeroPosti() {
		
	}

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_modello_auto_numero_posti")
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
        if (!(o instanceof ModelloAutoNumeroPosti)) {
            return false;
        }

        final ModelloAutoNumeroPosti modelloAutoNumeroPosti = (ModelloAutoNumeroPosti) o;

        return !(numeroPostiAuto != null ? !numeroPostiAuto.equals(modelloAutoNumeroPosti.numeroPostiAuto) : modelloAutoNumeroPosti.numeroPostiAuto != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (numeroPostiAuto != null ? numeroPostiAuto.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.numeroPostiAuto)
                .toString();
    }

}

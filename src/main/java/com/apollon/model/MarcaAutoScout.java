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
@Table(name = "data_marca_autoscout")
public class MarcaAutoScout extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5171234560137224340L;
	
	private Long id;
	private Long idAutoScout;
    private String name;
    
    public MarcaAutoScout() { }
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_marca_autoscout")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true, nullable = false)
	public Long getIdAutoScout() {
		return idAutoScout;
	}
	public void setIdAutoScout(Long idAutoScout) {
		this.idAutoScout = idAutoScout;
	}
	
	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarcaAutoScout)) {
            return false;
        }

        final MarcaAutoScout marcaAutoScout = (MarcaAutoScout) o;

        return !(id != null ? !id.equals(marcaAutoScout.id) : marcaAutoScout.id != null);

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

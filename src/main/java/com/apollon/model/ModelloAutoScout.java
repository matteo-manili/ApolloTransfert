package com.apollon.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.OrderBy;
/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_modello_autoscout")
public class ModelloAutoScout extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1574500285588163552L;

	private Long id;
	private Long idAutoScout;
    private String name;
    
    private Set<ModelloAutoNumeroPosti> modelloAutoNumeroPosti;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "modelloAutoScout" )
	@Fetch(FetchMode.SELECT)
	@OrderBy("numeroPostiAuto ASC")
	public Set<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti() {
		return modelloAutoNumeroPosti;
	}
	public void setModelloAutoNumeroPosti(Set<ModelloAutoNumeroPosti> modelloAutoNumeroPosti) {
		this.modelloAutoNumeroPosti = modelloAutoNumeroPosti;
	}
    
	/**
	 * questa è la classe autoveicolo Originale, non quello Reale
	 */
    private ClasseAutoveicolo classeAutoveicolo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_classe_autoveicolo", unique = false, nullable = true)
    public ClasseAutoveicolo getClasseAutoveicolo() {
		return classeAutoveicolo;
	}
	public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
		this.classeAutoveicolo = classeAutoveicolo;
	}
	

	private MarcaAutoScout marcaAutoScout;
    @ManyToOne
	@JoinColumn(name = "id_marca_auto", unique = false, nullable = false)
	public MarcaAutoScout getMarcaAutoScout() {
		return marcaAutoScout;
	}

	public void setMarcaAutoScout(MarcaAutoScout marcaAutoScout) {
		this.marcaAutoScout = marcaAutoScout;
	}

	
	public ModelloAutoScout() {
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_modello_autoscout")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = true, nullable = false)
	public Long getIdAutoScout() {
		return idAutoScout;
	}

	public void setIdAutoScout(Long idAutoScout) {
		this.idAutoScout = idAutoScout;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelloAutoScout)) {
            return false;
        }

        final ModelloAutoScout modelloAutoScout = (ModelloAutoScout) o;

        return !(id != null ? !id.equals(modelloAutoScout.id) : modelloAutoScout.id != null);

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

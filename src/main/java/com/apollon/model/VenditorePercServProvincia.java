package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "venditore_perc_serv_prov", uniqueConstraints = @UniqueConstraint(columnNames={"id_user", "id_provincia"}))
public class VenditorePercServProvincia extends BaseObject implements Serializable {
	private static final long serialVersionUID = -9110798871554698197L;
	
	private Long id;
	private Integer percentualeServizio;
	private BigDecimal compensoFisso;
	
	private User user;
  	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", unique = false, nullable = false)
    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	private Province province;
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "id_provincia", unique = false, nullable = false)
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}

	public VenditorePercServProvincia(User user, Province province, Integer percentualeServizio) {
		super();
		this.user = user;
		this.province = province;
		this.percentualeServizio = percentualeServizio;
	}
	
    public VenditorePercServProvincia() {
    	super();
	}
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_venditore_perc_serv_prov")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Integer getPercentualeServizio() {
		return percentualeServizio;
	}
	public void setPercentualeServizio(Integer percentualeServizio) {
		this.percentualeServizio = percentualeServizio;
	}
	
	public BigDecimal getCompensoFisso() {
		return compensoFisso;
	}
	public void setCompensoFisso(BigDecimal compensoFisso) {
		this.compensoFisso = compensoFisso;
	}
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenditorePercServProvincia)) {
            return false;
        }
        final VenditorePercServProvincia vendPercServProv = (VenditorePercServProvincia) o;
        return !(id != null ? !id.equals(vendPercServProv.id) : vendPercServProv.id != null);
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

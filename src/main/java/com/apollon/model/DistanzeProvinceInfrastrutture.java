package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="data_distanze", uniqueConstraints={
		   @UniqueConstraint(columnNames={"id_provinciaAndata", "id_provinciaArrivo"}),
		   @UniqueConstraint(columnNames={"id_provinciaAndata", "id_aeroportoArrivo"}),
		   @UniqueConstraint(columnNames={"id_provinciaAndata", "id_portoArrivo"}),
		   @UniqueConstraint(columnNames={"id_provinciaAndata", "id_museoArrivo"}),
		   // ----------------
		   @UniqueConstraint(columnNames={"id_aeroportoAndata", "id_provinciaArrivo"}),
		   @UniqueConstraint(columnNames={"id_aeroportoAndata", "id_aeroportoArrivo"}),
		   @UniqueConstraint(columnNames={"id_aeroportoAndata", "id_portoArrivo"}),
		   @UniqueConstraint(columnNames={"id_aeroportoAndata", "id_museoArrivo"}),
		   // ----------------
		   @UniqueConstraint(columnNames={"id_portoAndata", "id_provinciaArrivo"}),
		   @UniqueConstraint(columnNames={"id_portoAndata", "id_aeroportoArrivo"}),
		   @UniqueConstraint(columnNames={"id_portoAndata", "id_portoArrivo"}),
		   @UniqueConstraint(columnNames={"id_portoAndata", "id_museoArrivo"}),
		   // ----------------
		   @UniqueConstraint(columnNames={"id_museoAndata", "id_provinciaArrivo"}),
		   @UniqueConstraint(columnNames={"id_museoAndata", "id_aeroportoArrivo"}),
		   @UniqueConstraint(columnNames={"id_museoAndata", "id_portoArrivo"}),
		   @UniqueConstraint(columnNames={"id_museoAndata", "id_museoArrivo"})
		})

public class DistanzeProvinceInfrastrutture extends BaseObject implements Serializable {
	private static final long serialVersionUID = 9093972769267304807L;

	private Long id;
	private Date dataRequestDistance;
	private long metriDistanza;
	
	
	private Province provinciaAndata;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_provinciaAndata", unique = false, nullable = true)
    public Province getProvinciaAndata() {
		return provinciaAndata;
	}
	public void setProvinciaAndata(Province provinciaAndata) {
		this.provinciaAndata = provinciaAndata;
	}

	private Province provinciaArrivo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_provinciaArrivo", unique = false, nullable = true)
	public Province getProvinciaArrivo() {
		return provinciaArrivo;
	}
	public void setProvinciaArrivo(Province provinciaArrivo) {
		this.provinciaArrivo = provinciaArrivo;
	}

	private Aeroporti aeroportoAndata;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_aeroportoAndata", unique = false, nullable = true)
	public Aeroporti getAeroportoAndata() {
		return aeroportoAndata;
	}
	public void setAeroportoAndata(Aeroporti aeroportoAndata) {
		this.aeroportoAndata = aeroportoAndata;
	}

	private Aeroporti aeroportoArrivo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_aeroportoArrivo", unique = false, nullable = true)
	public Aeroporti getAeroportoArrivo() {
		return aeroportoArrivo;
	}
	public void setAeroportoArrivo(Aeroporti aeroportoArrivo) {
		this.aeroportoArrivo = aeroportoArrivo;
	}
	
	private PortiNavali portoAndata;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_portoAndata", unique = false, nullable = true)
	public PortiNavali getPortoAndata() {
		return portoAndata;
	}
	public void setPortoAndata(PortiNavali portoAndata) {
		this.portoAndata = portoAndata;
	}
	
	private PortiNavali portoArrivo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_portoArrivo", unique = false, nullable = true)
	public PortiNavali getPortoArrivo() {
		return portoArrivo;
	}
	public void setPortoArrivo(PortiNavali portoArrivo) {
		this.portoArrivo = portoArrivo;
	}

	private Musei museoAndata;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_museoAndata", unique = false, nullable = true)
	public Musei getMuseoAndata() {
		return museoAndata;
	}
	public void setMuseoAndata(Musei museoAndata) {
		this.museoAndata = museoAndata;
	}

	private Musei museoArrivo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_museoArrivo", unique = false, nullable = true)
	public Musei getMuseoArrivo() {
		return museoArrivo;
	}
	public void setMuseoArrivo(Musei museoArrivo) {
		this.museoArrivo = museoArrivo;
	}


	public DistanzeProvinceInfrastrutture() { }
    
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_distanza")
	public Long getId(){
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(unique = false, nullable = false)
	public Date getDataRequestDistance() {
		return dataRequestDistance;
	}
	public void setDataRequestDistance(Date dataRequestDistance) {
		this.dataRequestDistance = dataRequestDistance;
	}
	
	@Column(unique = false, nullable = false)
	public long getMetriDistanza() {
		return metriDistanza;
	}
	public void setMetriDistanza(long metriDistanza) {
		this.metriDistanza = metriDistanza;
	}
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistanzeProvinceInfrastrutture)) {
            return false;
        }
        final DistanzeProvinceInfrastrutture distanze = (DistanzeProvinceInfrastrutture) o;
        return !(id != null ? !id.equals(distanze.id) : distanze.id != null);
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
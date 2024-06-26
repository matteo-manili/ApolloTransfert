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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.Constants;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_coordinate_geo_province", uniqueConstraints = @UniqueConstraint(columnNames={"lat","lng","id_provincia"}))
public class CoordGeoProvince extends BaseObject implements Serializable {
	private static final long serialVersionUID = 66276324746314174L;
	
	private Long id;
	private Date dataRequestCoordGeo;
	private double lat;
	private double lng;
	private String siglaProvincia;
	
	@Transient
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	@Transient
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
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
	

    public CoordGeoProvince(Date dataRequestCoordGeo, double lat, double lng, Province province) {
		super();
		this.dataRequestCoordGeo = dataRequestCoordGeo;
		this.lat = lat;
		this.lng = lng;
		this.province = province;
	}
    
	public CoordGeoProvince() { }

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_coordinate_geo_province")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = false, nullable = false)
	public Date getDataRequestCoordGeo() {
		return dataRequestCoordGeo;
	}
	public void setDataRequestCoordGeo(Date dataRequestCoordGeo) {
		this.dataRequestCoordGeo = dataRequestCoordGeo;
	}
	
	@Column(unique = false, nullable = false)
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	@Column(unique = false, nullable = false)
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoordGeoProvince)) {
            return false;
        }
        final CoordGeoProvince comune = (CoordGeoProvince) o;
        return !(id != null ? !id.equals(comune.id) : comune.id != null);
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
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.id).toString();
    }

}

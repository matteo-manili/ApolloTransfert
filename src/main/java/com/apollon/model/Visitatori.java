package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Entity
@Table(name="visitatori")
public class Visitatori extends BaseObject implements Serializable {

	private static final long serialVersionUID = 4572341192137543216L;

	private Long id;
	private String ipAddress;
	private String deviceType;
	private String nomeProviderIpLocationApi;
	private Date dataVisita;
	private String hostname;
	private String provider;
	private String regionCode;
	
	private String countryCode;
	private String countryName;
	private String regionName;
	private String city;
	private String postalCode;
	private double latitude;
	private double longitude;

	public Visitatori() {
	}
	
	
	@Transient
    public String getFullNameRegion() {
        return city + ' ' + regionName;
    }
	
	
	public Visitatori(String ipAddress, String deviceType, String nomeProviderIpLocationApi,
			Date dataVisita, String hostname, String provider,
			String regionCode, String countryCode, String countryName,
			String regionName, String city, String postalCode, double latitude,
			double longitude) {
		super();
		this.ipAddress = ipAddress;
		this.deviceType = deviceType;
		this.nomeProviderIpLocationApi = nomeProviderIpLocationApi;
		this.dataVisita = dataVisita;
		this.hostname = hostname;
		this.provider = provider;
		this.regionCode = regionCode;
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.regionName = regionName;
		this.city = city;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_visitatori")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNomeProviderIpLocationApi() {
		return nomeProviderIpLocationApi;
	}
	public void setNomeProviderIpLocationApi(String nomeProviderIpLocationApi) {
		this.nomeProviderIpLocationApi = nomeProviderIpLocationApi;
	}

	public Date getDataVisita() {
		return dataVisita;
	}
	public void setDataVisita(Date dataVisita) {
		this.dataVisita = dataVisita;
	}

	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visitatori)) {
            return false;
        }

        final Visitatori ricercaTransfert = (Visitatori) o;

        return !(id != null ? !id.equals(ricercaTransfert.id) : ricercaTransfert.id != null);

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

package com.apollon.webapp.util.bean;

import java.util.Date;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
	Output

	ServerLocation [countryCode=US, countryName=United States, region=CA, 
	regionName=California, city=Sunnyvale, postalCode=94089, 
	latitude=37.424896, longitude=-122.0074]
	
 */

public class InfoUserConnectAddress {
	private String ipAddress;
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

}

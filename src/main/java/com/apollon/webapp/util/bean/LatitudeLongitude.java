package com.apollon.webapp.util.bean;

import com.apollon.model.Province;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class LatitudeLongitude {
	
	private Province provincia;
	private double lat_Arrivo;
	private double lng_Arrivo;

	
	public Province getProvincia() {
		return provincia;
	}
	public void setProvincia(Province provincia) {
		this.provincia = provincia;
	}
	public double getLat_Arrivo() {
		return lat_Arrivo;
	}
	public void setLat_Arrivo(double lat_Arrivo) {
		this.lat_Arrivo = lat_Arrivo;
	}
	public double getLng_Arrivo() {
		return lng_Arrivo;
	}
	public void setLng_Arrivo(double lng_Arrivo) {
		this.lng_Arrivo = lng_Arrivo;
	}

	
}

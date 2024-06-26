package com.apollon.webapp.util.geogoogle;

import java.util.List;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RicercaTransfert_GoogleMaps_Info {
	private String place_id;
	private String formattedAddress;
	private String name;
	private double lat;
	private double lng;
	private String comune; // eventuale Frazione
	private String comune_2; // eventuale Comune della Frazione 
	private List<String> listTypes;
	private String geolocationHtml5Address; // ChiamateAjaxController localizzaPosizione
	private String webSite;
	
	private String siglaProvicia;
	private String nomeProvicia;
	private String siglaRegione;
	private String nomeRegione;
	private String siglaNazione;
	private String nomeNazione;
	
	
	
	public RicercaTransfert_GoogleMaps_Info() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RicercaTransfert_GoogleMaps_Info(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	
	public String getFormattedAddress() {
		return formattedAddress;
	}
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	
	public String getComune_2() {
		return comune_2;
	}
	public void setComune_2(String comune_2) {
		this.comune_2 = comune_2;
	}
	
	public List<String> getListTypes() {
		return listTypes;
	}
	public void setListTypes(List<String> listTypes) {
		this.listTypes = listTypes;
	}
	
	public String getGeolocationHtml5Address() {
		return geolocationHtml5Address;
	}
	public void setGeolocationHtml5Address(String geolocationHtml5Address) {
		this.geolocationHtml5Address = geolocationHtml5Address;
	}
	
	public String getSiglaProvicia() {
		return siglaProvicia;
	}
	public void setSiglaProvicia(String siglaProvicia) {
		this.siglaProvicia = siglaProvicia;
	}
	
	public String getNomeProvicia() {
		return nomeProvicia;
	}
	public void setNomeProvicia(String nomeProvicia) {
		this.nomeProvicia = nomeProvicia;
	}
	
	public String getSiglaRegione() {
		return siglaRegione;
	}
	public void setSiglaRegione(String siglaRegione) {
		this.siglaRegione = siglaRegione;
	}
	
	public String getNomeRegione() {
		return nomeRegione;
	}
	public void setNomeRegione(String nomeRegione) {
		this.nomeRegione = nomeRegione;
	}
	
	public String getSiglaNazione() {
		return siglaNazione;
	}
	public void setSiglaNazione(String siglaNazione) {
		this.siglaNazione = siglaNazione;
	}
	
	public String getNomeNazione() {
		return nomeNazione;
	}
	public void setNomeNazione(String nomeNazione) {
		this.nomeNazione = nomeNazione;
	}
	
	
}
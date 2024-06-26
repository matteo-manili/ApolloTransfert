package com.apollon.webapp.util.bean;

import com.apollon.model.User;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RecensioneTransfer {
	
	private long idRicercaTransfert;
	private Boolean recensioneApprovata;
	private String recensione;
	private Integer punteggioStelleRecensione;
	private User user;
	
	public long getIdRicercaTransfert() {
		return idRicercaTransfert;
	}
	public void setIdRicercaTransfert(long idRicercaTransfert) {
		this.idRicercaTransfert = idRicercaTransfert;
	}
	
	public Boolean getRecensioneApprovata() {
		return recensioneApprovata;
	}
	public void setRecensioneApprovata(Boolean recensioneApprovata) {
		this.recensioneApprovata = recensioneApprovata;
	}
	
	public String getRecensione() {
		return recensione;
	}
	public void setRecensione(String recensione) {
		this.recensione = recensione;
	}
	
	public Integer getPunteggioStelleRecensione() {
		return punteggioStelleRecensione;
	}
	public void setPunteggioStelleRecensione(Integer punteggioStelleRecensione) {
		this.punteggioStelleRecensione = punteggioStelleRecensione;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}

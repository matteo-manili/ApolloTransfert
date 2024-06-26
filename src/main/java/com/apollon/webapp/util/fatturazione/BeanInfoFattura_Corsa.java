package com.apollon.webapp.util.fatturazione;

import java.util.List;

import com.apollon.model.Autista;
import com.apollon.model.RicercaTransfert;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class BeanInfoFattura_Corsa { 
	
	private boolean autistaConfermato;
	private String rimborsoCliente;
	private String prezzoServizio; 
	private String prezzoIva; 
	private String prezzoTotaleServizio; 
	private String prezzoCliente;
	private String prezzoAutista;
	private String maggiorazioneNotturna;
	private Autista autista;
	private AgendaAutistaScelta agendaAutistaScelta;
	private List<Autista> autistaServizioMultiplo;
	private String numeroProgressivo;
	private RicercaTransfert ricTransfert;
	
	
	public boolean isAutistaConfermato() {
		return autistaConfermato;
	}
	public void setAutistaConfermato(boolean autistaConfermato) {
		this.autistaConfermato = autistaConfermato;
	}
	public String getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(String rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	public RicercaTransfert getRicTransfert() {
		return ricTransfert;
	}
	public void setRicTransfert(RicercaTransfert ricTransfert) {
		this.ricTransfert = ricTransfert;
	}
	public String getPrezzoServizio() {
		return prezzoServizio;
	}
	public void setPrezzoServizio(String prezzoServizio) {
		this.prezzoServizio = prezzoServizio;
	}
	public String getPrezzoIva() {
		return prezzoIva;
	}
	public void setPrezzoIva(String prezzoIva) {
		this.prezzoIva = prezzoIva;
	}
	public String getPrezzoTotaleServizio() {
		return prezzoTotaleServizio;
	}
	public void setPrezzoTotaleServizio(String prezzoTotaleServizio) {
		this.prezzoTotaleServizio = prezzoTotaleServizio;
	}
	public String getPrezzoCliente() {
		return prezzoCliente;
	}
	public void setPrezzoCliente(String prezzoCliente) {
		this.prezzoCliente = prezzoCliente;
	}
	public String getPrezzoAutista() {
		return prezzoAutista;
	}
	public void setPrezzoAutista(String prezzoAutista) {
		this.prezzoAutista = prezzoAutista;
	}
	public String getMaggiorazioneNotturna() {
		return maggiorazioneNotturna;
	}
	public void setMaggiorazioneNotturna(String maggiorazioneNotturna) {
		this.maggiorazioneNotturna = maggiorazioneNotturna;
	}
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	public AgendaAutistaScelta getAgendaAutistaScelta() {
		return agendaAutistaScelta;
	}
	public void setAgendaAutistaScelta(AgendaAutistaScelta agendaAutistaScelta) {
		this.agendaAutistaScelta = agendaAutistaScelta;
	}
	public List<Autista> getAutistaServizioMultiplo() {
		return autistaServizioMultiplo;
	}
	public void setAutistaServizioMultiplo(List<Autista> autistaServizioMultiplo) {
		this.autistaServizioMultiplo = autistaServizioMultiplo;
	}
	public String getNumeroProgressivo() {
		return numeroProgressivo;
	}
	public void setNumeroProgressivo(String numeroProgressivo) {
		this.numeroProgressivo = numeroProgressivo;
	}
	
	
	
}

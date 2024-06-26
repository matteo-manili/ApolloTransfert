package com.apollon.webapp.util.bean;

import java.util.List;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class AutistaTerritorio {
	
	private int totaleAutisti;
	private List<AutistiRegione> autistiRegione;
	private List<AutistiProvincia> autistiProvincia;
	
	public int getTotaleAutisti() {
		return totaleAutisti;
	}
	public void setTotaleAutisti(int totaleAutisti) {
		this.totaleAutisti = totaleAutisti;
	}
	public List<AutistiRegione> getAutistiRegione() {
		return autistiRegione;
	}
	public void setAutistiRegione(List<AutistiRegione> autistiRegione) {
		this.autistiRegione = autistiRegione;
	}
	public List<AutistiProvincia> getAutistiProvincia() {
		return autistiProvincia;
	}
	public void setAutistiProvincia(List<AutistiProvincia> autistiProvincia) {
		this.autistiProvincia = autistiProvincia;
	}
	
	
	static public class AutistiRegione {
		private String nomeRegione;
		private List<Long> autisti;
		
		public String getNomeRegione() {
			return nomeRegione;
		}
		public void setNomeRegione(String nomeRegione) {
			this.nomeRegione = nomeRegione;
		}
		public List<Long> getAutisti() {
			return autisti;
		}
		public void setAutisti(List<Long> autisti) {
			this.autisti = autisti;
		}
	}
	
	static public class AutistiProvincia {
		private String nomeProvincia;
		private List<Long> autisti;
		
		public String getNomeProvincia() {
			return nomeProvincia;
		}
		public void setNomeProvincia(String nomeProvincia) {
			this.nomeProvincia = nomeProvincia;
		}
		public List<Long> getAutisti() {
			return autisti;
		}
		public void setAutisti(List<Long> autisti) {
			this.autisti = autisti;
		}
	}
	
	
	
	
	
}

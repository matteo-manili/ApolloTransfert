package com.apollon.webapp.util.bean;

import java.math.BigDecimal;
import java.util.List;

import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.RicercaTransfert;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class GestioneCorseMedieAdmin {
	
	private RicercaTransfert ricTransfert;
	private long idAutistaAssegnato;
	private String prezzoCliente;
	private ClasseAutoveicolo classeAutoveicoloSceltaCliente;
	private BigDecimal rimborsoCliente;
	private BigDecimal maggiorazioneNotturna;
	private List<GestioneCorseMedieAdminAutisti> gestioneCorseMedieAdminAutisti;
	
	
	static public class GestioneCorseMedieAdminAutisti {
		private long idAutista;
		private BigDecimal tariffaPerKm;
		private BigDecimal maggiorazioneNotturna;
		private int ordineAutista;
		private boolean invioSms;
		private boolean assegnato;
		private int ordineChiamataPrenotata;
		private String tokenAutista;
		private List<Autoveicolo> autoveicoliRichiestiList;
		private String fullNameAutisa;
		private String prezzoAutista;
		private String noteAutista;
		private String prezzoCommServ;
		private int corseEffAutista;
		private String telAutista; 
		
		public GestioneCorseMedieAdminAutisti() {
			super();
		}

		public long getIdAutista() {
			return idAutista;
		}
		public void setIdAutista(long idAutista) {
			this.idAutista = idAutista;
		}
		public BigDecimal getTariffaPerKm() {
			return tariffaPerKm;
		}
		public void setTariffaPerKm(BigDecimal tariffaPerKm) {
			this.tariffaPerKm = tariffaPerKm;
		}
		public BigDecimal getMaggiorazioneNotturna() {
			return maggiorazioneNotturna;
		}
		public void setMaggiorazioneNotturna(BigDecimal maggiorazioneNotturna) {
			this.maggiorazioneNotturna = maggiorazioneNotturna;
		}
		public int getOrdineAutista() {
			return ordineAutista;
		}
		public void setOrdineAutista(int ordineAutista) {
			this.ordineAutista = ordineAutista;
		}
		public boolean isInvioSms() {
			return invioSms;
		}
		public void setInvioSms(boolean invioSms) {
			this.invioSms = invioSms;
		}
		public boolean isAssegnato() {
			return assegnato;
		}
		public void setAssegnato(boolean assegnato) {
			this.assegnato = assegnato;
		}
		public int getOrdineChiamataPrenotata() {
			return ordineChiamataPrenotata;
		}
		public void setOrdineChiamataPrenotata(int ordineChiamataPrenotata) {
			this.ordineChiamataPrenotata = ordineChiamataPrenotata;
		}
		public String getTokenAutista() {
			return tokenAutista;
		}
		public void setTokenAutista(String tokenAutista) {
			this.tokenAutista = tokenAutista;
		}
		public List<Autoveicolo> getAutoveicoliRichiestiList() {
			return autoveicoliRichiestiList;
		}
		public void setAutoveicoliRichiestiList(
				List<Autoveicolo> autoveicoliRichiestiList) {
			this.autoveicoliRichiestiList = autoveicoliRichiestiList;
		}
		public String getFullNameAutisa() {
			return fullNameAutisa;
		}
		public void setFullNameAutisa(String fullNameAutisa) {
			this.fullNameAutisa = fullNameAutisa;
		}
		public String getPrezzoAutista() {
			return prezzoAutista;
		}
		public void setPrezzoAutista(String prezzoAutista) {
			this.prezzoAutista = prezzoAutista;
		}
		public String getNoteAutista() {
			return noteAutista;
		}
		public void setNoteAutista(String noteAutista) {
			this.noteAutista = noteAutista;
		}
		public String getPrezzoCommServ() {
			return prezzoCommServ;
		}
		public void setPrezzoCommServ(String prezzoCommServ) {
			this.prezzoCommServ = prezzoCommServ;
		}
		public int getCorseEffAutista() {
			return corseEffAutista;
		}
		public void setCorseEffAutista(int corseEffAutista) {
			this.corseEffAutista = corseEffAutista;
		}
		public String getTelAutista() {
			return telAutista;
		}
		public void setTelAutista(String telAutista) {
			this.telAutista = telAutista;
		}

		
	}
	
	
	
	
	public RicercaTransfert getRicTransfert() {
		return ricTransfert;
	}
	public void setRicTransfert(RicercaTransfert ricTransfert) {
		this.ricTransfert = ricTransfert;
	}
	
	public long getIdAutistaAssegnato() {
		return idAutistaAssegnato;
	}
	public void setIdAutistaAssegnato(long idAutistaAssegnato) {
		this.idAutistaAssegnato = idAutistaAssegnato;
	}
	
	public String getPrezzoCliente() {
		return prezzoCliente;
	}
	public void setPrezzoCliente(String prezzoCliente) {
		this.prezzoCliente = prezzoCliente;
	}
	
	public ClasseAutoveicolo getClasseAutoveicoloSceltaCliente() {
		return classeAutoveicoloSceltaCliente;
	}
	public void setClasseAutoveicoloSceltaCliente(ClasseAutoveicolo classeAutoveicoloSceltaCliente) {
		this.classeAutoveicoloSceltaCliente = classeAutoveicoloSceltaCliente;
	}
	
	public BigDecimal getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(BigDecimal rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	
	public BigDecimal getMaggiorazioneNotturna() {
		return maggiorazioneNotturna;
	}
	public void setMaggiorazioneNotturna(BigDecimal maggiorazioneNotturna) {
		this.maggiorazioneNotturna = maggiorazioneNotturna;
	}
	
	public List<GestioneCorseMedieAdminAutisti> getGestioneCorseMedieAdminAutisti() {
		return gestioneCorseMedieAdminAutisti;
	}
	public void setGestioneCorseMedieAdminAutisti(List<GestioneCorseMedieAdminAutisti> gestioneCorseMedieAdminAutisti) {
		this.gestioneCorseMedieAdminAutisti = gestioneCorseMedieAdminAutisti;
	}
	
	



	
	
	
	
	

}

package com.apollon.webapp.util.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RisultatoAutistiParticolare implements Serializable {
	private static final long serialVersionUID = -9044848728966911812L;
	
	private List<Autista> autistiEffettivi;
	private List<ResultParticolare> resultParticolare; 

	
	public List<Autista> getAutistiEffettivi() {
		return autistiEffettivi;
	}
	public void setAutistiEffettivi(List<Autista> autistiEffettivi) {
		this.autistiEffettivi = autistiEffettivi;
	}
	
	public List<ResultParticolare> getResultParticolare() {
		return resultParticolare;
	}
	public void setResultParticolare(List<ResultParticolare> resultParticolare) {
		this.resultParticolare = resultParticolare;
	}

	// PRIMA CLASSE
	static public class ResultParticolare {
		private ClasseAutoveicolo classeAutoveicoloScelta;
		private Autoveicolo autoveicolo;
		private BigDecimal rimborsoCliente;
		private Integer percentualeServizio;
		private BigDecimal prezzoCommissioneServizio;
		private BigDecimal prezzoCommissioneServizioIva;
		private BigDecimal prezzoTotaleAutista;
		private BigDecimal prezzoTotaleCliente;
		private boolean invioSmsCorsaConfermata;
		private Date dataChiamataPrenotata;
		private boolean invioSms;
		private String token;
		private boolean chiamataPrenotata;
		
		public ClasseAutoveicolo getClasseAutoveicoloScelta() {
			return classeAutoveicoloScelta;
		}
		public void setClasseAutoveicoloScelta(ClasseAutoveicolo classeAutoveicoloScelta) {
			this.classeAutoveicoloScelta = classeAutoveicoloScelta;
		}
		public Autoveicolo getAutoveicolo() {
			return autoveicolo;
		}
		public void setAutoveicolo(Autoveicolo autoveicolo) {
			this.autoveicolo = autoveicolo;
		}
		public BigDecimal getRimborsoCliente() {
			return rimborsoCliente;
		}
		public void setRimborsoCliente(BigDecimal rimborsoCliente) {
			this.rimborsoCliente = rimborsoCliente;
		}
		public Integer getPercentualeServizio() {
			return percentualeServizio;
		}
		public void setPercentualeServizio(Integer percentualeServizio) {
			this.percentualeServizio = percentualeServizio;
		}
		public BigDecimal getPrezzoCommissioneServizio() {
			return prezzoCommissioneServizio;
		}
		public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
			this.prezzoCommissioneServizio = prezzoCommissioneServizio;
		}
		public BigDecimal getPrezzoCommissioneServizioIva() {
			return prezzoCommissioneServizioIva;
		}
		public void setPrezzoCommissioneServizioIva(BigDecimal prezzoCommissioneServizioIva) {
			this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
		}
		public BigDecimal getPrezzoTotaleAutista() {
			return prezzoTotaleAutista;
		}
		public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
			this.prezzoTotaleAutista = prezzoTotaleAutista;
		}
		public BigDecimal getPrezzoTotaleCliente() {
			return prezzoTotaleCliente;
		}
		public void setPrezzoTotaleCliente(BigDecimal prezzoTotaleCliente) {
			this.prezzoTotaleCliente = prezzoTotaleCliente;
		}
		public boolean isInvioSmsCorsaConfermata() {
			return invioSmsCorsaConfermata;
		}
		public void setInvioSmsCorsaConfermata(boolean invioSmsCorsaConfermata) {
			this.invioSmsCorsaConfermata = invioSmsCorsaConfermata;
		}
		public Date getDataChiamataPrenotata() {
			return dataChiamataPrenotata;
		}
		public void setDataChiamataPrenotata(Date dataChiamataPrenotata) {
			this.dataChiamataPrenotata = dataChiamataPrenotata;
		}
		public boolean isInvioSms() {
			return invioSms;
		}
		public void setInvioSms(boolean invioSms) {
			this.invioSms = invioSms;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public boolean isChiamataPrenotata() {
			return chiamataPrenotata;
		}
		public void setChiamataPrenotata(boolean chiamataPrenotata) {
			this.chiamataPrenotata = chiamataPrenotata;
		}
		
	} // FINE PRIMA CLASSE
	

	
	
	
	
	

}

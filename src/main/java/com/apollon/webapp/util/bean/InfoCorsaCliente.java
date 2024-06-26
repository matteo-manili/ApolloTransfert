package com.apollon.webapp.util.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.RicercaTransfert;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class InfoCorsaCliente implements Comparable<InfoCorsaCliente>, Serializable {
	private static final long serialVersionUID = -7943092885374453440L;

	private ClasseAutoveicolo classeAutoveicoloScelta;
	private BigDecimal prezzo;
	private BigDecimal rimborsoCliente;
	private BigDecimal maggiorazioneNotturna;
	private BigDecimal prezzoTotaleCliente;
	private boolean statusEseguitoAndata;
	private boolean statusEseguitoRitorno;
	private String tipoCorsa; // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
	private RicercaTransfert ricTransfert;
	private BigDecimal tariffaPerKm;
	private String distanzaText;
	private String distanzaTextRitorno;
	private Autoveicolo autoveicolo;
	private Autista autista;
	
	public ClasseAutoveicolo getClasseAutoveicoloScelta() {
		return classeAutoveicoloScelta;
	}
	public void setClasseAutoveicoloScelta(ClasseAutoveicolo classeAutoveicoloScelta) {
		this.classeAutoveicoloScelta = classeAutoveicoloScelta;
	}
	public BigDecimal getPrezzoTotaleCliente() {
		return prezzoTotaleCliente;
	}
	public void setPrezzoTotaleCliente(BigDecimal prezzoTotaleCliente) {
		this.prezzoTotaleCliente = prezzoTotaleCliente;
	}
	public RicercaTransfert getRicTransfert() {
		return ricTransfert;
	}
	public void setRicTransfert(RicercaTransfert ricTransfert) {
		this.ricTransfert = ricTransfert;
	}
	public boolean isStatusEseguitoAndata() {
		return statusEseguitoAndata;
	}
	public void setStatusEseguitoAndata(boolean statusEseguitoAndata) {
		this.statusEseguitoAndata = statusEseguitoAndata;
	}
	public boolean isStatusEseguitoRitorno() {
		return statusEseguitoRitorno;
	}
	public void setStatusEseguitoRitorno(boolean statusEseguitoRitorno) {
		this.statusEseguitoRitorno = statusEseguitoRitorno;
	}
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	public String getTipoCorsa() {
		return tipoCorsa;
	}
	public void setTipoCorsa(String tipoCorsa) {
		this.tipoCorsa = tipoCorsa;
	}
	public BigDecimal getTariffaPerKm() {
		return tariffaPerKm;
	}
	public void setTariffaPerKm(BigDecimal tariffaPerKm) {
		this.tariffaPerKm = tariffaPerKm;
	}
	public String getDistanzaText() {
		return distanzaText;
	}
	public void setDistanzaText(String distanzaText) {
		this.distanzaText = distanzaText;
	}
	public String getDistanzaTextRitorno() {
		return distanzaTextRitorno;
	}
	public void setDistanzaTextRitorno(String distanzaTextRitorno) {
		this.distanzaTextRitorno = distanzaTextRitorno;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	public BigDecimal getMaggiorazioneNotturna() {
		return maggiorazioneNotturna;
	}
	public void setMaggiorazioneNotturna(BigDecimal maggiorazioneNotturna) {
		this.maggiorazioneNotturna = maggiorazioneNotturna;
	}
	public BigDecimal getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(BigDecimal rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InfoCorsaCliente o) {
		return getRicTransfert().getDataOraPrelevamentoDate(). compareTo(o.getRicTransfert().getDataOraPrelevamentoDate());
	}

}

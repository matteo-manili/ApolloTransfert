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
public class InfoCorsaAutista implements Comparable<InfoCorsaAutista> {
	
	private ClasseAutoveicolo classeAutoveicoloScelta;
	private String linkPrenotaCorsaMedio;
	private String percentualeServizio;
	private BigDecimal prezzoTotaleCliente;
	private BigDecimal prezzoTotaleAutista;
	private boolean statusEseguitoAndata;
	private boolean statusEseguitoRitorno;
	private String tipoCorsa; // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
	private RicercaTransfert ricTransfert;
	private BigDecimal tariffaPerKm;
	private String distanzaText;
	private String distanzaTextRitorno;
	private List<Autoveicolo> autoveicolo;
	private String nomeCliente;
	private String telefonoCliente;
	
	

	public ClasseAutoveicolo getClasseAutoveicoloScelta() {
		return classeAutoveicoloScelta;
	}
	public void setClasseAutoveicoloScelta(ClasseAutoveicolo classeAutoveicoloScelta) {
		this.classeAutoveicoloScelta = classeAutoveicoloScelta;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getTelefonoCliente() {
		return telefonoCliente;
	}
	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
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

	public List<Autoveicolo> getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(List<Autoveicolo> autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	public String getPercentualeServizio() {
		return percentualeServizio;
	}
	public void setPercentualeServizio(String percentualeServizio) {
		this.percentualeServizio = percentualeServizio;
	}
	public BigDecimal getPrezzoTotaleCliente() {
		return prezzoTotaleCliente;
	}
	public void setPrezzoTotaleCliente(BigDecimal prezzoTotaleCliente) {
		this.prezzoTotaleCliente = prezzoTotaleCliente;
	}
	public BigDecimal getPrezzoTotaleAutista() {
		return prezzoTotaleAutista;
	}
	public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
		this.prezzoTotaleAutista = prezzoTotaleAutista;
	}
	
	public String getLinkPrenotaCorsaMedio() {
		return linkPrenotaCorsaMedio;
	}
	public void setLinkPrenotaCorsaMedio(String linkPrenotaCorsaMedio) {
		this.linkPrenotaCorsaMedio = linkPrenotaCorsaMedio;
	}
	
	
	@Override
	public int compareTo(InfoCorsaAutista o) {
		return getRicTransfert().getDataOraPrelevamentoDate().compareTo(o.getRicTransfert().getDataOraPrelevamentoDate());

	}
}

package com.apollon.webapp.util.bean;

import java.util.List;

import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Tariffe_Zone {

	private AutistaZone autistaZona;
	private List<Tariffe_AutoveicoloTariffa> tariffe_AutoveicoliTariffeList;
	private AutistaAeroporti autistaAeroporto; // non lo uso più
	private AutistaPortiNavali autistaPortiNavale; // non lo uso più
	
	
	public AutistaZone getAutistaZona() {
		return autistaZona;
	}
	public void setAutistaZona(AutistaZone autistaZona) {
		this.autistaZona = autistaZona;
	}
	
	public List<Tariffe_AutoveicoloTariffa> getTariffe_AutoveicoliTariffeList() {
		return tariffe_AutoveicoliTariffeList;
	}
	public void setTariffe_AutoveicoliTariffeList(List<Tariffe_AutoveicoloTariffa> tariffe_AutoveicoliTariffeList) {
		this.tariffe_AutoveicoliTariffeList = tariffe_AutoveicoliTariffeList;
	}
	
	public AutistaAeroporti getAutistaAeroporto() {
		return autistaAeroporto;
	}
	public void setAutistaAeroporto(AutistaAeroporti autistaAeroporto) {
		this.autistaAeroporto = autistaAeroporto;
	}

	public AutistaPortiNavali getAutistaPortiNavale() {
		return autistaPortiNavale;
	}
	public void setAutistaPortiNavale(AutistaPortiNavali autistaPortiNavale) {
		this.autistaPortiNavale = autistaPortiNavale;
	}

	
	
	
}

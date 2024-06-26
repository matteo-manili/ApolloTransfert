package com.apollon.webapp.util.bean;

import java.util.ArrayList;
import java.util.List;

import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Tariffe_Aeroporti_Porti {


	private AutistaAeroporti autistaAeroporto = new AutistaAeroporti();
	private AutistaPortiNavali autistaPortoNavale = new AutistaPortiNavali();
	private List<Tariffe_AutoveicoloTariffa> tariffe_AutoveicoliTariffeList = new ArrayList<Tariffe_AutoveicoloTariffa>();



	public AutistaAeroporti getAutistaAeroporto() {
		return autistaAeroporto;
	}
	public void setAutistaAeroporto(AutistaAeroporti autistaAeroporto) {
		this.autistaAeroporto = autistaAeroporto;
	}
	public AutistaPortiNavali getAutistaPortoNavale() {
		return autistaPortoNavale;
	}
	public void setAutistaPortoNavale(AutistaPortiNavali autistaPortoNavale) {
		this.autistaPortoNavale = autistaPortoNavale;
	}
	public List<Tariffe_AutoveicoloTariffa> getTariffe_AutoveicoliTariffeList() {
		return tariffe_AutoveicoliTariffeList;
	}
	public void setTariffe_AutoveicoliTariffeList(
			List<Tariffe_AutoveicoloTariffa> tariffe_AutoveicoliTariffeList) {
		this.tariffe_AutoveicoliTariffeList = tariffe_AutoveicoliTariffeList;
	}
	
	
	
	
}

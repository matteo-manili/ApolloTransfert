package com.apollon.webapp.util.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TransferTariffeProvince  {
	private List<Tariffa_Province_e_Infrastrutture> tariffa_Province_e_Infrastrutture;
	
	public List<Tariffa_Province_e_Infrastrutture> getTariffa_Province_e_Infrastrutture() {
		return tariffa_Province_e_Infrastrutture;
	}
	public void setTariffa_Province_e_Infrastrutture(List<Tariffa_Province_e_Infrastrutture> tariffa_Province_e_Infrastrutture) {
		this.tariffa_Province_e_Infrastrutture = tariffa_Province_e_Infrastrutture;
	}

	// PRIMA CLASSE
	static public class Tariffa_Province_e_Infrastrutture {
		private String titoloCorsaPartenza;
		private String titoloCorsaArrivo;
		private int numeroKilometro;
		private String tipoTerritorio;
		private List<TariffaClasseAuto> tariffaClasseAuto;
		
		public String getTitoloCorsaPartenza() {
			return titoloCorsaPartenza;
		}
		public void setTitoloCorsaPartenza(String titoloCorsaPartenza) {
			this.titoloCorsaPartenza = titoloCorsaPartenza;
		}
		public String getTitoloCorsaArrivo() {
			return titoloCorsaArrivo;
		}
		public void setTitoloCorsaArrivo(String titoloCorsaArrivo) {
			this.titoloCorsaArrivo = titoloCorsaArrivo;
		}
		public int getNumeroKilometro() {
			return numeroKilometro;
		}
		public void setNumeroKilometro(int numeroKilometro) {
			this.numeroKilometro = numeroKilometro;
		}
		public List<TariffaClasseAuto> getTariffaClasseAuto() {
			return tariffaClasseAuto;
		}
		public void setTariffaClasseAuto(List<TariffaClasseAuto> tariffaClasseAuto) {
			this.tariffaClasseAuto = tariffaClasseAuto;
		}
		public String getTipoTerritorio() {
			return tipoTerritorio;
		}
		public void setTipoTerritorio(String tipoTerritorio) {
			this.tipoTerritorio = tipoTerritorio;
		}


		// SECONDA CLASSE
		static public class TariffaClasseAuto {
			private String titoloClasseAuto;
			private BigDecimal prezzoClinte;
			private String numeroPasseggeri;
			private String classeAutoveicoloDesc;

			public String getTitoloClasseAuto() {
				return titoloClasseAuto;
			}
			public void setTitoloClasseAuto(String titoloClasseAuto) {
				this.titoloClasseAuto = titoloClasseAuto;
			}
			public BigDecimal getPrezzoClinte() {
				return prezzoClinte;
			}
			public void setPrezzoClinte(BigDecimal prezzoClinte) {
				this.prezzoClinte = prezzoClinte;
			}
			public String getNumeroPasseggeri() {
				return numeroPasseggeri;
			}
			public void setNumeroPasseggeri(String numeroPasseggeri) {
				this.numeroPasseggeri = numeroPasseggeri;
			}
			public String getClasseAutoveicoloDesc() {
				return classeAutoveicoloDesc;
			}
			public void setClasseAutoveicoloDesc(String classeAutoveicoloDesc) {
				this.classeAutoveicoloDesc = classeAutoveicoloDesc;
			}
		}
	}
	
	
}

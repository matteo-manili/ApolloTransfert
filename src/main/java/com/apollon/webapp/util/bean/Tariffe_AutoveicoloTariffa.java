package com.apollon.webapp.util.bean;

import java.math.BigDecimal;
import java.util.List;

import com.apollon.model.Autoveicolo;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Tariffe_AutoveicoloTariffa {

	static public class CompensoAutistaCorse {
		private BigDecimal compensoAutista;
		private int kilometri;
		private BigDecimal tariffaPerKm;
		
		public BigDecimal getCompensoAutista() {
			return compensoAutista;
		}
		public void setCompensoAutista(BigDecimal compensoAutista) {
			this.compensoAutista = compensoAutista;
		}
		public int getKilometri() {
			return kilometri;
		}
		public void setKilometri(int kilometri) {
			this.kilometri = kilometri;
		}
		public BigDecimal getTariffaPerKm() {
			return tariffaPerKm;
		}
		public void setTariffaPerKm(BigDecimal tariffaPerKm) {
			this.tariffaPerKm = tariffaPerKm;
		}
	}

	
	private Autoveicolo autoveicolo;
	private List<CompensoAutistaCorse> compensoAutistaCorse;
	
	@Deprecated
	private BigDecimal tariffaST; // non lo uso
	@Deprecated
	private BigDecimal tariffaLP; // non lo uso
	private int km_max_LP; // non lo uso
	private BigDecimal tariffaGC; // non lo uso
    private BigDecimal tariffaMA; // non lo uso
    private BigDecimal tariffaAERO; // non lo uso
    private BigDecimal tariffaPORTO; // non lo uso
    private int max_ore_GC; // non lo uso
    private int max_ore_MA; // non lo uso

    public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}

	public List<CompensoAutistaCorse> getCompensoAutistaCorse() {
		return compensoAutistaCorse;
	}
	public void setCompensoAutistaCorse(List<CompensoAutistaCorse> compensoAutistaCorse) {
		this.compensoAutistaCorse = compensoAutistaCorse;
	}
	
	@Deprecated
	public BigDecimal getTariffaST() {
		return tariffaST;
	}
	@Deprecated
	public void setTariffaST(BigDecimal tariffaST) {
		this.tariffaST = tariffaST;
	}
	@Deprecated
	public BigDecimal getTariffaLP() {
		return tariffaLP;
	}
	@Deprecated
	public void setTariffaLP(BigDecimal tariffaLP) {
		this.tariffaLP = tariffaLP;
	}
	
	public BigDecimal getTariffaGC() {
		return tariffaGC;
	}
	public void setTariffaGC(BigDecimal tariffaGC) {
		this.tariffaGC = tariffaGC;
	}
	
	public BigDecimal getTariffaMA() {
		return tariffaMA;
	}
	public void setTariffaMA(BigDecimal tariffaMA) {
		this.tariffaMA = tariffaMA;
	}
	
	public BigDecimal getTariffaAERO() {
		return tariffaAERO;
	}
	public void setTariffaAERO(BigDecimal tariffaAERO) {
		this.tariffaAERO = tariffaAERO;
	}
	
	public BigDecimal getTariffaPORTO() {
		return tariffaPORTO;
	}
	public void setTariffaPORTO(BigDecimal tariffaPORTO) {
		this.tariffaPORTO = tariffaPORTO;
	}
	public int getKm_max_LP() {
		return km_max_LP;
	}

	public void setKm_max_LP(int km_max_LP) {
		this.km_max_LP = km_max_LP;
	}

	public int getMax_ore_GC() {
		return max_ore_GC;
	}

	public void setMax_ore_GC(int max_ore_GC) {
		this.max_ore_GC = max_ore_GC;
	}

	public int getMax_ore_MA() {
		return max_ore_MA;
	}

	public void setMax_ore_MA(int max_ore_MA) {
		this.max_ore_MA = max_ore_MA;
	}

}

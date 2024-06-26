package com.apollon.model;

import javax.persistence.Embeddable;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.search.annotations.Indexed;

/**
 * This class is used to represent an address with address,
 * city, province and postal-code information.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Embeddable
@Indexed
public class TariffeValori extends BaseObject implements Serializable {
    private static final long serialVersionUID = -2505702078684618591L;
    
    
    private BigDecimal tariffaST;
    private BigDecimal tariffaLP;
    private BigDecimal tariffaGC;
    private BigDecimal tariffaMA;
    
    private BigDecimal tariffaAERO;
    private BigDecimal tariffaPORTO;
    
    private int km_max_LP;
    private int max_ore_GC;
    private int max_ore_MA;
    
    
    
    

    public BigDecimal getTariffaST() {
		return tariffaST;
	}

	public void setTariffaST(BigDecimal tariffaST) {
		this.tariffaST = tariffaST;
	}

	public BigDecimal getTariffaLP() {
		return tariffaLP;
	}

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



	/* (non-Javadoc)
	 * @see com.apollon.model.BaseObject#toString()
	 */
	@Override
	public String toString() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.apollon.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.apollon.model.BaseObject#hashCode()
	 */
	@Override
	public int hashCode() {
		return 0;
	}



    
}

package com.apollon.webapp.util.bean;

import java.math.BigDecimal;
import java.util.List;
import com.apollon.model.Aeroporti;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Province;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TransferTariffe {

	private String titolo;
	private String icona;
	private String url;
	private List<DestPorivinceConfinanti> destPorivinceConfinanti;
	private List<DestAeroportiConfinanti> destAeroportiConfinanti;
	
	// PRIMA CLASSE
	static public class DestPorivinceConfinanti {
		private Province provincia;
		private BigDecimal prezzoCorsa;
		private ClasseAutoveicolo ClasseAutoveicolo;
		
		public Province getProvincia() {
			return provincia;
		}
		public void setProvincia(Province provincia) {
			this.provincia = provincia;
		}
		public BigDecimal getPrezzoCorsa() {
			return prezzoCorsa;
		}
		public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
			this.prezzoCorsa = prezzoCorsa;
		}
		public ClasseAutoveicolo getClasseAutoveicolo() {
			return ClasseAutoveicolo;
		}
		public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
			ClasseAutoveicolo = classeAutoveicolo;
		}
	}
	
	// SECONDA CLASSE
	static public class DestAeroportiConfinanti {
		private Aeroporti aeroporto;
		private BigDecimal prezzoCorsa;
		private ClasseAutoveicolo ClasseAutoveicolo;
		
		public Aeroporti getAeroporto() {
			return aeroporto;
		}
		public void setAeroporto(Aeroporti aeroporto) {
			this.aeroporto = aeroporto;
		}
		public BigDecimal getPrezzoCorsa() {
			return prezzoCorsa;
		}
		public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
			this.prezzoCorsa = prezzoCorsa;
		}
		public ClasseAutoveicolo getClasseAutoveicolo() {
			return ClasseAutoveicolo;
		}
		public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
			ClasseAutoveicolo = classeAutoveicolo;
		}
	}
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getIcona() {
		return icona;
	}
	public void setIcona(String icona) {
		this.icona = icona;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<DestPorivinceConfinanti> getDestPorivinceConfinanti() {
		return destPorivinceConfinanti;
	}
	public void setDestPorivinceConfinanti(
			List<DestPorivinceConfinanti> destPorivinceConfinanti) {
		this.destPorivinceConfinanti = destPorivinceConfinanti;
	}
	public List<DestAeroportiConfinanti> getDestAeroportiConfinanti() {
		return destAeroportiConfinanti;
	}
	public void setDestAeroportiConfinanti(
			List<DestAeroportiConfinanti> destAeroportiConfinanti) {
		this.destAeroportiConfinanti = destAeroportiConfinanti;
	}



	
}

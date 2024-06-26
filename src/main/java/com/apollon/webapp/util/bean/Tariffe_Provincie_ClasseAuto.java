package com.apollon.webapp.util.bean;


import java.math.BigDecimal;
import java.util.List;

import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Province;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Tariffe_Provincie_ClasseAuto {

	
	public static class ClasseAutoveicoloTariffe {
		private ClasseAutoveicolo classeAutoveicolo;
		private BigDecimal tariffaST;
		private BigDecimal tariffaLP;
		
		public ClasseAutoveicoloTariffe(ClasseAutoveicolo classeAutoveicolo, BigDecimal tariffaST, BigDecimal tariffaLP) {
			super();
			this.classeAutoveicolo = classeAutoveicolo;
			this.tariffaST = tariffaST;
			this.tariffaLP = tariffaLP;
		}

		public ClasseAutoveicolo getClasseAutoveicolo() {
			return classeAutoveicolo;
		}

		public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
			this.classeAutoveicolo = classeAutoveicolo;
		}

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
		
		
		
		
	}
	
	
	
	private Province provincia;
	private List<ClasseAutoveicoloTariffe> classeAutoveicoloTariffe;
	
	/*
	public void addClasseAutoveicoloTariffe(ClasseAutoveicolo classeAutoveicolo, BigDecimal tariffaST, BigDecimal tariffaLP){
		ClasseAutoveicoloTariffe classeAutoveicoloTariffe = new ClasseAutoveicoloTariffe(classeAutoveicolo, tariffaST, tariffaLP);
		this.ClasseAutoveicoloTariffe.add(classeAutoveicoloTariffe);
	}
	*/
	
	public Province getProvincia() {
		return provincia;
	}
	public void setProvincia(Province provincia) {
		this.provincia = provincia;
	}
	public List<ClasseAutoveicoloTariffe> getClasseAutoveicoloTariffe() {
		return classeAutoveicoloTariffe;
	}
	public void setClasseAutoveicoloTariffe(
			List<ClasseAutoveicoloTariffe> classeAutoveicoloTariffe) {
		this.classeAutoveicoloTariffe = classeAutoveicoloTariffe;
	}

	
	
	
	
	
}

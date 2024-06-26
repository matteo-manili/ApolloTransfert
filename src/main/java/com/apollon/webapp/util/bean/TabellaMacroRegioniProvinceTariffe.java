package com.apollon.webapp.util.bean;

import java.util.List;

import com.apollon.model.MacroRegioni;
import com.apollon.model.Regioni;
import com.apollon.model.Province;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TabellaMacroRegioniProvinceTariffe {
	private MacroRegioni macroRegione;
	private List<Regioni_Entity> regioni_Entity;
	
	public MacroRegioni getMacroRegione() {
		return macroRegione;
	}
	public void setMacroRegione(MacroRegioni macroRegione) {
		this.macroRegione = macroRegione;
	}
	public List<Regioni_Entity> getRegioni_Entity() {
		return regioni_Entity;
	}
	public void setRegioni_Entity(List<Regioni_Entity> regioni_Entity) {
		this.regioni_Entity = regioni_Entity;
	}
	
	
	public static class Regioni_Entity{
		private Regioni regione;
		private List<Province_Entity> province_Entity;
		
		public Regioni getRegione() {
			return regione;
		}
		public void setRegione(Regioni regione) {
			this.regione = regione;
		}
		public List<Province_Entity> getProvince_Entity() {
			return province_Entity;
		}
		public void setProvince_Entity(List<Province_Entity> province_Entity) {
			this.province_Entity = province_Entity;
		}
		

		public static class Province_Entity{
			private Province provincia;
			private Integer percentualeServizioVenditore;
			
			public Province getProvincia() {
				return provincia;
			}
			public void setProvincia(Province provincia) {
				this.provincia = provincia;
			}
			public Integer getPercentualeServizioVenditore() {
				return percentualeServizioVenditore;
			}
			public void setPercentualeServizioVenditore(Integer percentualeServizioVenditore) {
				this.percentualeServizioVenditore = percentualeServizioVenditore;
			}
		}
	}
}
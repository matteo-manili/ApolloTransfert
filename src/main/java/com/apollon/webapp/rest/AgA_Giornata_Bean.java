package com.apollon.webapp.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class AgA_Giornata_Bean extends AgA_Giornata {
	
	/**
	 * TabellaModelloGiornata
	 */
	public static class TabellaModelloGiornata {
		private Long idModelloGiornata;
		private String nomeGiornata;
		private List<GiornataOrarioTariffario> giornataOrarioTariffarioList;
		
		public TabellaModelloGiornata(Long idModelloGiornata, String nomeGiornata, List<GiornataOrarioTariffario> giornataOrarioTariffarioList) {
			super();
			this.idModelloGiornata = idModelloGiornata;
			this.nomeGiornata = nomeGiornata;
			this.giornataOrarioTariffarioList = giornataOrarioTariffarioList;
		}
		
		public Long getIdModelloGiornata() {
			return idModelloGiornata;
		}
		public void setIdModelloGiornata(Long idModelloGiornata) {
			this.idModelloGiornata = idModelloGiornata;
		}
		public String getNomeGiornata() {
			return nomeGiornata;
		}
		public void setNomeGiornata(String nomeGiornata) {
			this.nomeGiornata = nomeGiornata;
		}
		public List<GiornataOrarioTariffario> getGiornataOrarioTariffarioList() {
			return giornataOrarioTariffarioList;
		}
		public void setGiornataOrarioTariffarioList(List<GiornataOrarioTariffario> giornataOrarioTariffarioList) {
			this.giornataOrarioTariffarioList = giornataOrarioTariffarioList;
		}

		public static class GiornataOrarioTariffario {
			private Integer orario;
			private boolean attivo;
			private Long idModelloTariffario;
			
			public GiornataOrarioTariffario(Integer orario, boolean attivo, Long idModelloTariffario) {
				super();
				this.orario = orario;
				this.attivo = attivo;
				this.idModelloTariffario = idModelloTariffario;
			}
			
			public Integer getOrario() {
				return orario;
			}
			public void setOrario(Integer orario) {
				this.orario = orario;
			}
			public boolean isAttivo() {
				return attivo;
			}
			public void setAttivo(boolean attivo) {
				this.attivo = attivo;
			}
			public Long getIdModelloTariffario() {
				return idModelloTariffario;
			}
			public void setIdModelloTariffario(Long idModelloTariffario) {
				this.idModelloTariffario = idModelloTariffario;
			}
		}
	}
	
	
	
	
	/**
	 * TabellaAutoveicoloModelloTariffario
	 */
	public static class TabellaAutoveicoloModelloTariffario {
		private Long idModelloTariffario;
		private String nomeTariffario;
		private List<TabellaModelloTariffario> tabellaModelloTariffarioList;
		
		public TabellaAutoveicoloModelloTariffario(Long idModelloTariffario, String nomeTariffario, List<TabellaModelloTariffario> tabellaModelloTariffarioList) {
			super();
			this.idModelloTariffario = idModelloTariffario;
			this.nomeTariffario = nomeTariffario;
			this.tabellaModelloTariffarioList = tabellaModelloTariffarioList;
		}
		
		public Long getIdModelloTariffario() {
			return idModelloTariffario;
		}
		public void setIdModelloTariffario(Long idModelloTariffario) {
			this.idModelloTariffario = idModelloTariffario;
		}
		public String getNomeTariffario() {
			return nomeTariffario;
		}
		public void setNomeTariffario(String nomeTariffario) {
			this.nomeTariffario = nomeTariffario;
		}
		public List<TabellaModelloTariffario> getTabellaModelloTariffarioList() {
			return tabellaModelloTariffarioList;
		}
		public void setTabellaModelloTariffarioList(List<TabellaModelloTariffario> tabellaModelloTariffarioList) {
			this.tabellaModelloTariffarioList = tabellaModelloTariffarioList;
		}
	}
	
	
	/**
	 * TabellaGiornataTariffario
	 */
	public static class TabellaGiornataTariffario {
		private Long idGiornata;
		private Date dataGiornataOrario; 
		private boolean attivo;
		private List<TabellaTariffario> tabellaTariffarioList;
		
		public TabellaGiornataTariffario(Long idGiornata, Date dataGiornataOrario, boolean attivo, List<TabellaTariffario> tabellaTariffarioList) {
			super();
			this.idGiornata = idGiornata;
			this.dataGiornataOrario = dataGiornataOrario;
			this.attivo = attivo;
			this.tabellaTariffarioList = tabellaTariffarioList;
		}
		
		public Long getIdGiornata() {
			return idGiornata;
		}
		public void setIdGiornata(Long idGiornata) {
			this.idGiornata = idGiornata;
		}
		public Date getDataGiornataOrario() {
			return dataGiornataOrario;
		}
		public void setDataGiornataOrario(Date dataGiornataOrario) {
			this.dataGiornataOrario = dataGiornataOrario;
		}
		public boolean isAttivo() {
			return attivo;
		}
		public void setAttivo(boolean attivo) {
			this.attivo = attivo;
		}
		public List<TabellaTariffario> getTabellaTariffarioList() {
			return tabellaTariffarioList;
		}
		public void setTabellaTariffarioList(List<TabellaTariffario> tabellaTariffarioList) {
			this.tabellaTariffarioList = tabellaTariffarioList;
		}
	}
	
	
	/**
	 * TabellaModelloTariffario
	 */
	public static class TabellaModelloTariffario {
		private Long idTariffario;
		private int kmCorsa;
		private boolean eseguiCorse;
		private BigDecimal prezzoCorsa;
		private double kmRaggioArea;
		
		public TabellaModelloTariffario(Long idTariffario, int kmCorsa, boolean eseguiCorse, BigDecimal prezzoCorsa, double kmRaggioArea) {
			super();
			this.idTariffario = idTariffario;
			this.kmCorsa = kmCorsa;
			this.eseguiCorse = eseguiCorse;
			this.prezzoCorsa = prezzoCorsa;
			this.kmRaggioArea = kmRaggioArea;
		}
		
		public Long getIdTariffario() {
			return idTariffario;
		}
		public void setIdTariffario(Long idTariffario) {
			this.idTariffario = idTariffario;
		}
		public int getKmCorsa() {
			return kmCorsa;
		}
		public void setKmCorsa(int kmCorsa) {
			this.kmCorsa = kmCorsa;
		}
		public boolean isEseguiCorse() {
			return eseguiCorse;
		}
		public void setEseguiCorse(boolean eseguiCorse) {
			this.eseguiCorse = eseguiCorse;
		}
		public BigDecimal getPrezzoCorsa() {
			return prezzoCorsa;
		}
		public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
			this.prezzoCorsa = prezzoCorsa;
		}
		public double getKmRaggioArea() {
			return kmRaggioArea;
		}
		public void setKmRaggioArea(double kmRaggioArea) {
			this.kmRaggioArea = kmRaggioArea;
		}
	}
	
	
	/**
	 * TabellaTariffario
	 */
	public static class TabellaTariffario {
		private Long idTariffario;
		private int kmCorsa;
		private boolean eseguiCorse;
		private BigDecimal prezzoCorsa;
		private double kmRaggioArea;

		private Long idModelloTariffario; // è quello nuovo
		
		public TabellaTariffario(Long idTariffario, int kmCorsa, boolean eseguiCorse, BigDecimal prezzoCorsa, double kmRaggioArea, Long idModelloTariffario) {
			super();
			this.idTariffario = idTariffario;
			this.kmCorsa = kmCorsa;
			this.eseguiCorse = eseguiCorse;
			this.prezzoCorsa = prezzoCorsa;
			this.kmRaggioArea = kmRaggioArea;
			this.idModelloTariffario = idModelloTariffario;
		}
		
		public Long getIdTariffario() {
			return idTariffario;
		}
		public void setIdTariffario(Long idTariffario) {
			this.idTariffario = idTariffario;
		}
		public int getKmCorsa() {
			return kmCorsa;
		}
		public void setKmCorsa(int kmCorsa) {
			this.kmCorsa = kmCorsa;
		}
		public boolean isEseguiCorse() {
			return eseguiCorse;
		}
		public void setEseguiCorse(boolean eseguiCorse) {
			this.eseguiCorse = eseguiCorse;
		}
		public BigDecimal getPrezzoCorsa() {
			return prezzoCorsa;
		}
		public void setPrezzoCorsa(BigDecimal prezzoCorsa) {
			this.prezzoCorsa = prezzoCorsa;
		}
		public double getKmRaggioArea() {
			return kmRaggioArea;
		}
		public void setKmRaggioArea(double kmRaggioArea) {
			this.kmRaggioArea = kmRaggioArea;
		}
		public Long getIdModelloTariffario() {
			return idModelloTariffario;
		}
		public void setIdModelloTariffario(Long idModelloTariffario) {
			this.idModelloTariffario = idModelloTariffario;
		}
		
		
		
	}
	
	
	
	/**
	 * TabellaGiornata
	 */
	public static class TabellaGiornata {
		private Integer orario;
		private String orarioFormatEsteso;
		private Boolean attivo;
		private Long idTariffario;
		private String tariffarioDesc;
		
		public TabellaGiornata(Integer orario, String orarioFormatEsteso, Boolean attivo, Long idTariffario, String tariffarioDesc) {
			super();
			this.orario = orario;
			this.orarioFormatEsteso = orarioFormatEsteso;
			this.attivo = attivo;
			this.idTariffario = idTariffario;
			this.tariffarioDesc = tariffarioDesc;
		}
		
		public Integer getOrario() {
			return orario;
		}
		public void setOrario(Integer orario) {
			this.orario = orario;
		}
		public String getOrarioFormatEsteso() {
			return orarioFormatEsteso;
		}
		public void setOrarioFormatEsteso(String orarioFormatEsteso) {
			this.orarioFormatEsteso = orarioFormatEsteso;
		}
		public Boolean getAttivo() {
			return attivo;
		}
		public void setAttivo(Boolean attivo) {
			this.attivo = attivo;
		}
		public Long getIdTariffario() {
			return idTariffario;
		}
		public void setIdTariffario(Long idTariffario) {
			this.idTariffario = idTariffario;
		}
		public String getTariffarioDesc() {
			return tariffarioDesc;
		}
		public void setTariffarioDesc(String tariffarioDesc) {
			this.tariffarioDesc = tariffarioDesc;
		}
	}
	
	
	/**
	 * TabellaCalendario
	 */
	public static class TabellaCalendario {
		private List<GiornoCalendario> giornoCalendarioList;
		
		public List<GiornoCalendario> getGiornoCalendarioList() {
			return giornoCalendarioList;
		}
		public void setGiornoCalendarioList(List<GiornoCalendario> giornoCalendarioList) {
			this.giornoCalendarioList = giornoCalendarioList;
		}

		public static class GiornoCalendario {
			private List<TabellaGiornata> tabellaGiornataList;

			public List<TabellaGiornata> getTabellaGiornataList() {
				return tabellaGiornataList;
			}
			public void setTabellaGiornataList(List<TabellaGiornata> tabellaGiornataList) {
				this.tabellaGiornataList = tabellaGiornataList;
			}
		}
	}
	
	
	
	
}

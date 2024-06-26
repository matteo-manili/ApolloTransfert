package com.apollon.webapp.util.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;

/**
 * @author Matteo - matteo.manili@gmail.com
 */
public class ResultRicerca_Autista_Tariffe implements Serializable {
	private static final long serialVersionUID = 8940215189147792337L;
	
	private boolean ricercaRiuscita;
	private int totaleShowClasseAutoveicolo;
	private List<MessaggioEsitoRicerca> messaggiEsitoRicerca;
	private boolean scontoRitorno;
	private String tipoServizio;
	private List<ResultMedio> resultMedio;
	private List<Long> provinceTragitto_Id;
	private ResultAgendaAutista resultAgendaAutista;
	
	
	static public class AgendaAutistaScelta {
		private AgendaAutista_Autista agendaAutista_AutistaAndata;
		private AgendaAutista_Autista agendaAutista_AutistaRitorno;
		private long percentualeServizio;
		
		public BigDecimal getPrezzoTotaleAutisti() {
			BigDecimal prezzoTotaleAutisti = new BigDecimal(0);
			if( this.agendaAutista_AutistaAndata != null ) {
				prezzoTotaleAutisti = prezzoTotaleAutisti.add(this.agendaAutista_AutistaAndata.getPrezzoCorsa());
			}
			if( this.agendaAutista_AutistaRitorno != null ) {
				prezzoTotaleAutisti = prezzoTotaleAutisti.add(this.agendaAutista_AutistaRitorno.getPrezzoCorsa());
			}
			return prezzoTotaleAutisti;
		}
		
		public BigDecimal getPrezzoTotaleCliente() {
			BigDecimal prezzoTotaleCliente = new BigDecimal(0);
			if( this.agendaAutista_AutistaAndata != null ) {
				prezzoTotaleCliente = prezzoTotaleCliente.add(this.agendaAutista_AutistaAndata.getPrezzoCliente());
			}
			if( this.agendaAutista_AutistaRitorno != null ) {
				prezzoTotaleCliente = prezzoTotaleCliente.add(this.agendaAutista_AutistaRitorno.getPrezzoCliente());
			}
			return prezzoTotaleCliente;
		}

		public BigDecimal getPrezzoCommissioneServizioTotale() {
			BigDecimal prezzoCommissioneServizioTotale = new BigDecimal(0);
			if( this.agendaAutista_AutistaAndata != null ) {
				prezzoCommissioneServizioTotale = prezzoCommissioneServizioTotale.add(this.agendaAutista_AutistaAndata.getPrezzoCommissioneServizio());
			}
			if( this.agendaAutista_AutistaRitorno != null ) {
				prezzoCommissioneServizioTotale = prezzoCommissioneServizioTotale.add(this.agendaAutista_AutistaRitorno.getPrezzoCommissioneServizio());
			}
			return prezzoCommissioneServizioTotale;
		}
		
		public BigDecimal getPrezzoCommissioneServizioIvaTotale() {
			BigDecimal prezzoCommissioneServizioIvaTotale = new BigDecimal(0);
			if( this.agendaAutista_AutistaAndata != null ) {
				prezzoCommissioneServizioIvaTotale = prezzoCommissioneServizioIvaTotale.add(this.agendaAutista_AutistaAndata.getPrezzoCommissioneServizioIva());
			}
			if( this.agendaAutista_AutistaRitorno != null ) {
				prezzoCommissioneServizioIvaTotale = prezzoCommissioneServizioIvaTotale.add(this.agendaAutista_AutistaRitorno.getPrezzoCommissioneServizioIva());
			}
			return prezzoCommissioneServizioIvaTotale;
		}
		public AgendaAutista_Autista getAgendaAutista_AutistaAndata() {
			return agendaAutista_AutistaAndata;
		}
		public void setAgendaAutista_AutistaAndata(AgendaAutista_Autista agendaAutista_AutistaAndata) {
			this.agendaAutista_AutistaAndata = agendaAutista_AutistaAndata;
		}
		public AgendaAutista_Autista getAgendaAutista_AutistaRitorno() {
			return agendaAutista_AutistaRitorno;
		}
		public void setAgendaAutista_AutistaRitorno(AgendaAutista_Autista agendaAutista_AutistaRitorno) {
			this.agendaAutista_AutistaRitorno = agendaAutista_AutistaRitorno;
		}
		public long getPercentualeServizio() {
			return percentualeServizio;
		}
		public void setPercentualeServizio(long percentualeServizio) {
			this.percentualeServizio = percentualeServizio;
		}
	}
	

	
	static public class ResultAgendaAutista {
		private List<AgendaAutista_Autista> AgendaAutista_AutistaAndata;
		private List<AgendaAutista_Autista> AgendaAutista_AutistaRitorno;
		
		public List<AgendaAutista_Autista> getAgendaAutista_AutistaAndata() {
			return AgendaAutista_AutistaAndata;
		}
		public void setAgendaAutista_AutistaAndata(List<AgendaAutista_Autista> agendaAutista_AutistaAndata) {
			AgendaAutista_AutistaAndata = agendaAutista_AutistaAndata;
		}
		public List<AgendaAutista_Autista> getAgendaAutista_AutistaRitorno() {
			return AgendaAutista_AutistaRitorno;
		}
		public void setAgendaAutista_AutistaRitorno(List<AgendaAutista_Autista> agendaAutista_AutistaRitorno) {
			AgendaAutista_AutistaRitorno = agendaAutista_AutistaRitorno;
		}
	}
	
	
	
	// PRIMA CLASSE
	static public class ResultMedio {
		private boolean showClasseAutoveicolo;
		private BigDecimal prezzoTotaleCliente;
		@Deprecated
		private BigDecimal tariffaPerKm;
		@Deprecated
		private BigDecimal prezzoTotaleAutista;
		@Deprecated
		private BigDecimal prezzoCommissioneServizio;
		@Deprecated
		private BigDecimal prezzoCommissioneServizioIva;
		@Deprecated
		private BigDecimal prezzoCommissioneVenditore;
		private BigDecimal rimborsoCliente;
		private boolean classeAutoveicoloScelta;
		private BigDecimal maggiorazioneNotturna;
		private ClasseAutoveicolo classeAutoveicolo;
		private List<ResultMedioAutista> resultMedioAutista;
		
		public boolean isShowClasseAutoveicolo() {
			return showClasseAutoveicolo;
		}
		public void setShowClasseAutoveicolo(boolean showClasseAutoveicolo) {
			this.showClasseAutoveicolo = showClasseAutoveicolo;
		}
		public BigDecimal getPrezzoTotaleCliente() {
			return prezzoTotaleCliente;
		}
		public void setPrezzoTotaleCliente(BigDecimal prezzoTotaleCliente) {
			this.prezzoTotaleCliente = prezzoTotaleCliente;
		}
		@Deprecated
		public BigDecimal getTariffaPerKm() {
			return tariffaPerKm;
		}
		@Deprecated
		public void setTariffaPerKm(BigDecimal tariffaPerKm) {
			this.tariffaPerKm = tariffaPerKm;
		}
		@Deprecated
		public BigDecimal getPrezzoTotaleAutista() {
			return prezzoTotaleAutista;
		}
		@Deprecated
		public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
			this.prezzoTotaleAutista = prezzoTotaleAutista;
		}
		@Deprecated
		public BigDecimal getPrezzoCommissioneServizio() {
			return prezzoCommissioneServizio;
		}
		@Deprecated
		public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
			this.prezzoCommissioneServizio = prezzoCommissioneServizio;
		}
		@Deprecated
		public BigDecimal getPrezzoCommissioneServizioIva() {
			return prezzoCommissioneServizioIva;
		}
		@Deprecated
		public void setPrezzoCommissioneServizioIva(
				BigDecimal prezzoCommissioneServizioIva) {
			this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
		}
		public BigDecimal getPrezzoCommissioneVenditore() {
			return prezzoCommissioneVenditore;
		}
		public void setPrezzoCommissioneVenditore(BigDecimal prezzoCommissioneVenditore) {
			this.prezzoCommissioneVenditore = prezzoCommissioneVenditore;
		}
		public BigDecimal getRimborsoCliente() {
			return rimborsoCliente;
		}
		public void setRimborsoCliente(BigDecimal rimborsoCliente) {
			this.rimborsoCliente = rimborsoCliente;
		}
		public boolean isClasseAutoveicoloScelta() {
			return classeAutoveicoloScelta;
		}
		public void setClasseAutoveicoloScelta(boolean classeAutoveicoloScelta) {
			this.classeAutoveicoloScelta = classeAutoveicoloScelta;
		}
		public BigDecimal getMaggiorazioneNotturna() {
			return maggiorazioneNotturna;
		}
		public void setMaggiorazioneNotturna(BigDecimal maggiorazioneNotturna) {
			this.maggiorazioneNotturna = maggiorazioneNotturna;
		}
		public ClasseAutoveicolo getClasseAutoveicolo() {
			return classeAutoveicolo;
		}
		public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
			this.classeAutoveicolo = classeAutoveicolo;
		}
		public List<ResultMedioAutista> getResultMedioAutista() {
			return resultMedioAutista;
		}
		public void setResultMedioAutista(
				List<ResultMedioAutista> resultMedioAutista) {
			this.resultMedioAutista = resultMedioAutista;
		}
		
		// SECONDA CLASSE
		static public class ResultMedioAutista {
			private Autista autista;
			private Integer percentualeServizio;
			private BigDecimal tariffaPerKm;
			private ClasseAutoveicolo classeAutoveicolo;
			private BigDecimal prezzoTotaleAutista;
			private BigDecimal prezzoCommissioneServizio;
			private BigDecimal prezzoCommissioneServizioIva;
			private BigDecimal prezzoCommissioneVenditore;
			private List<RisultAutistaMedioAutoveicolo> risultAutistaMedioAutoveicolo;
	
			public Autista getAutista() {
				return autista;
			}
			public void setAutista(Autista autista) {
				this.autista = autista;
			}
			public BigDecimal getPrezzoTotaleAutista() {
				return prezzoTotaleAutista;
			}
			public void setPrezzoTotaleAutista(BigDecimal prezzoTotaleAutista) {
				this.prezzoTotaleAutista = prezzoTotaleAutista;
			}
			public BigDecimal getTariffaPerKm() {
				return tariffaPerKm;
			}
			public void setTariffaPerKm(BigDecimal tariffaPerKm) {
				this.tariffaPerKm = tariffaPerKm;
			}
			public ClasseAutoveicolo getClasseAutoveicolo() {
				return classeAutoveicolo;
			}
			public void setClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
				this.classeAutoveicolo = classeAutoveicolo;
			}
			public BigDecimal getPrezzoCommissioneServizio() {
				return prezzoCommissioneServizio;
			}
			public void setPrezzoCommissioneServizio(BigDecimal prezzoCommissioneServizio) {
				this.prezzoCommissioneServizio = prezzoCommissioneServizio;
			}
			public BigDecimal getPrezzoCommissioneServizioIva() {
				return prezzoCommissioneServizioIva;
			}
			public void setPrezzoCommissioneServizioIva(
					BigDecimal prezzoCommissioneServizioIva) {
				this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
			}
			public BigDecimal getPrezzoCommissioneVenditore() {
				return prezzoCommissioneVenditore;
			}
			public void setPrezzoCommissioneVenditore(BigDecimal prezzoCommissioneVenditore) {
				this.prezzoCommissioneVenditore = prezzoCommissioneVenditore;
			}
			public Integer getPercentualeServizio() {
				return percentualeServizio;
			}
			public void setPercentualeServizio(Integer percentualeServizio) {
				this.percentualeServizio = percentualeServizio;
			}
			public List<RisultAutistaMedioAutoveicolo> getRisultAutistaMedioAutoveicolo() {
				return risultAutistaMedioAutoveicolo;
			}
			public void setRisultAutistaMedioAutoveicolo(
					List<RisultAutistaMedioAutoveicolo> risultAutistaMedioAutoveicolo) {
				this.risultAutistaMedioAutoveicolo = risultAutistaMedioAutoveicolo;
			}

			// TERZA CLASSE
			static public class RisultAutistaMedioAutoveicolo {
				private Autoveicolo autoveicolo;
				private long idAutista;
				private String firstName;
				private String nomeMarcaAuto;
				public long getIdAutista() {
					return idAutista;
				}
				public void setIdAutista(long idAutista) {
					this.idAutista = idAutista;
				}
				public String getFirstName() {
					return firstName;
				}
				public void setFirstName(String firstName) {
					this.firstName = firstName;
				}
				public String getNomeMarcaAuto() {
					return nomeMarcaAuto;
				}
				public void setNomeMarcaAuto(String nomeMarcaAuto) {
					this.nomeMarcaAuto = nomeMarcaAuto;
				}
				public Autoveicolo getAutoveicolo() {
					return autoveicolo;
				}
				public void setAutoveicolo(Autoveicolo autoveicolo) {
					this.autoveicolo = autoveicolo;
				}
	
			} // FINE TERZA CLASSE
		} // FINE SECONDA CLASSE
	} // FINE PRIMA CLASSE
	
	
	
	public ResultAgendaAutista getResultAgendaAutista() {
		return resultAgendaAutista;
	}
	public void setResultAgendaAutista(ResultAgendaAutista resultAgendaAutista) {
		this.resultAgendaAutista = resultAgendaAutista;
	}
	public List<Long> getProvinceTragitto_Id() {
		return provinceTragitto_Id;
	}
	public void setProvinceTragitto_Id(List<Long> provinceTragitto_Id) {
		this.provinceTragitto_Id = provinceTragitto_Id;
	}
	public List<ResultMedio> getResultMedio() {
		return resultMedio;
	}
	public void setResultMedio(List<ResultMedio> resultMedio) {
		this.resultMedio = resultMedio;
	}
	public int getTotaleShowClasseAutoveicolo() {
		return totaleShowClasseAutoveicolo;
	}
	public void setTotaleShowClasseAutoveicolo(int totaleShowClasseAutoveicolo) {
		this.totaleShowClasseAutoveicolo = totaleShowClasseAutoveicolo;
	}
	public String getTipoServizio() {
		return tipoServizio;
	}
	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}
	public boolean isScontoRitorno() {
		return scontoRitorno;
	}
	public void setScontoRitorno(boolean scontoRitorno) {
		this.scontoRitorno = scontoRitorno;
	}
	public boolean isRicercaRiuscita() {
		return ricercaRiuscita;
	}
	public void setRicercaRiuscita(boolean ricercaRiuscita) {
		this.ricercaRiuscita = ricercaRiuscita;
	}
	public List<MessaggioEsitoRicerca> getMessaggiEsitoRicerca() {
		return messaggiEsitoRicerca;
	}
	public void setMessaggiEsitoRicerca(List<MessaggioEsitoRicerca> messaggiEsitoRicerca) {
		this.messaggiEsitoRicerca = messaggiEsitoRicerca;
	}

	/**
	 * CLASSE UTILITY
	 * @author matteo
	 */
	static public class MessaggioEsitoRicerca {
		
		String tipoServizo;
		String propertiesMess;
		Object[] args;
		
		public MessaggioEsitoRicerca(String propertiesMess, Object[] args, String tipoServizo) {
			super();
			this.propertiesMess = propertiesMess;
			this.args = args;
			this.tipoServizo = tipoServizo;
		}
		
		public MessaggioEsitoRicerca(String propertiesMess, Object[] args) {
			super();
			this.propertiesMess = propertiesMess;
			this.args = args;
		}
		
		public String getPropertiesMess() {
			return propertiesMess;
		}
		public void setPropertiesMess(String propertiesMess) {
			this.propertiesMess = propertiesMess;
		}
		public Object[] getArgs() {
			return args;
		}
		public void setArgs(Object[] args) {
			this.args = args;
		}
		public String getTipoServizo() {
			return tipoServizo;
		}
		public void setTipoServizo(String tipoServizo) {
			this.tipoServizo = tipoServizo;
		}
		
		
		
	}
	
	
}


package com.apollon.webapp.util.bean;

import java.math.BigDecimal;
import java.util.Date;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.RicercaTransfert;

public class AgendaAutista_Autista {
	
	private Integer percentualeServizio;
	private BigDecimal prezzoCommissioneServizio;
	private BigDecimal prezzoCommissioneServizioIva;
	private BigDecimal prezzoCliente;
	private BigDecimal rimborsoCliente;
	
	//USER Autista
	private Long idUser;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	//AUTISTA
	private Long idAutista;
	private String partitaIvaDenominazione;
	private boolean azienda;
	private int numCorseEseguite;
	private String noteAutista;
	//AUTOVEICOLO
	private Long idAutoveicolo;
	private String annoImmatricolazione;
	private String targa;
	private String nomeModelloAuto;
	private String nomeMarcaAuto;
	private Long idClasseAutoveicolo;
	private int numeroPostiAutoveicolo;
	private ClasseAutoveicolo classeAutoveicoloReale;
	//GIORNATA
	private Long idGiornata;
	private Date dataGiornataOrario; 
	//TARIFFARIO
	private Long idTariffario;
	private int kmCorsa;
	private BigDecimal prezzoCorsa;
	private double kmRaggioArea;
	//RICERCA TRANSFERT
	private double lat_Partenza;
	private double lng_Partenza;
	private double lat_Arrivo;
	private double lng_Arrivo;
	RicercaTransfert ricercaTransfert;
	
	// UTILITY
	public boolean isRitornoCorsa() {
		if( this.lat_Partenza == ricercaTransfert.getLat_Partenza() && this.lng_Partenza == ricercaTransfert.getLng_Partenza() 
				&& this.lat_Arrivo == ricercaTransfert.getLat_Arrivo() && this.lng_Arrivo == ricercaTransfert.getLng_Arrivo() ) {
			return false;
		}else {
			return true;
		}
	}
	public String getFullName() {
		return this.firstName+" "+this.lastName;
	}
    public String getMarcaModello() {
		return this.nomeMarcaAuto+" "+ this.nomeModelloAuto;
    }
    public String getMarcaModelloTarga() {
		return getMarcaModello()+" "+this.targa;
    }
	
	
	public AgendaAutista_Autista(Long idUser, String email, String firstName, String lastName,
			String phoneNumber, Long idAutista, String partitaIvaDenominazione, boolean azienda,
			int numCorseEseguite, String noteAutista, Long idAutoveicolo, String annoImmatricolazione, String targa,
			String nomeModelloAuto, String nomeMarcaAuto, Long idClasseAutoveicolo, int numeroPostiAutoveicolo,
			Long idGiornata, Date dataGiornataOrario, Long idTariffario, int kmCorsa, BigDecimal prezzoCorsa,
			double kmRaggioArea, ClasseAutoveicolo classeAutoveicoloReale, double lat_Partenza, 
			double lng_Partenza, double lat_Arrivo, double lng_Arrivo ,RicercaTransfert ricercaTransfert) {
		super();
		this.idUser = idUser;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.idAutista = idAutista;
		this.partitaIvaDenominazione = partitaIvaDenominazione;
		this.azienda = azienda;
		this.numCorseEseguite = numCorseEseguite;
		this.noteAutista = noteAutista;
		this.idAutoveicolo = idAutoveicolo;
		this.annoImmatricolazione = annoImmatricolazione;
		this.targa = targa;
		this.nomeModelloAuto = nomeModelloAuto;
		this.nomeMarcaAuto = nomeMarcaAuto;
		this.idClasseAutoveicolo = idClasseAutoveicolo;
		this.numeroPostiAutoveicolo = numeroPostiAutoveicolo;
		this.idGiornata = idGiornata;
		this.dataGiornataOrario = dataGiornataOrario;
		this.idTariffario = idTariffario;
		this.kmCorsa = kmCorsa;
		this.prezzoCorsa = prezzoCorsa;
		this.kmRaggioArea = kmRaggioArea;
		this.classeAutoveicoloReale = classeAutoveicoloReale;
		this.lat_Partenza = lat_Partenza;
		this.lng_Partenza = lng_Partenza;
		this.lat_Arrivo = lat_Arrivo;
		this.lng_Arrivo =  lng_Arrivo;
		this.ricercaTransfert = ricercaTransfert;
	}
	
	public Integer getPercentualeServizio() {
		return percentualeServizio;
	}
	public void setPercentualeServizio(Integer percentualeServizio) {
		this.percentualeServizio = percentualeServizio;
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
	public void setPrezzoCommissioneServizioIva(BigDecimal prezzoCommissioneServizioIva) {
		this.prezzoCommissioneServizioIva = prezzoCommissioneServizioIva;
	}
	public BigDecimal getPrezzoCliente() {
		return prezzoCliente;
	}
	public void setPrezzoCliente(BigDecimal prezzoCliente) {
		this.prezzoCliente = prezzoCliente;
	}
	public BigDecimal getRimborsoCliente() {
		return rimborsoCliente;
	}
	public void setRimborsoCliente(BigDecimal rimborsoCliente) {
		this.rimborsoCliente = rimborsoCliente;
	}
	
	public Long getIdUser() {
		return idUser;
	}
	public String getEmail() {
		return email;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public Long getIdAutista() {
		return idAutista;
	}
	public String getPartitaIvaDenominazione() {
		return partitaIvaDenominazione;
	}
	public boolean isAzienda() {
		return azienda;
	}
	public int getNumCorseEseguite() {
		return numCorseEseguite;
	}
	public String getNoteAutista() {
		return noteAutista;
	}
	public Long getIdAutoveicolo() {
		return idAutoveicolo;
	}
	public String getAnnoImmatricolazione() {
		return annoImmatricolazione;
	}
	public String getTarga() {
		return targa;
	}
	public String getNomeModelloAuto() {
		return nomeModelloAuto;
	}
	public String getNomeMarcaAuto() {
		return nomeMarcaAuto;
	}
	public Long getIdClasseAutoveicolo() {
		return idClasseAutoveicolo;
	}
	public ClasseAutoveicolo getClasseAutoveicoloReale() {
		return classeAutoveicoloReale;
	}
	public int getNumeroPostiAutoveicolo() {
		return numeroPostiAutoveicolo;
	}
	public Long getIdGiornata() {
		return idGiornata;
	}
	public Date getDataGiornataOrario() {
		return dataGiornataOrario;
	}
	public Long getIdTariffario() {
		return idTariffario;
	}
	public int getKmCorsa() {
		return kmCorsa;
	}
	public BigDecimal getPrezzoCorsa() {
		return prezzoCorsa;
	}
	public double getKmRaggioArea() {
		return kmRaggioArea;
	}
	public double getLat_Partenza() {
		return lat_Partenza;
	}
	public void setLat_Partenza(double lat_Partenza) {
		this.lat_Partenza = lat_Partenza;
	}
	public double getLng_Partenza() {
		return lng_Partenza;
	}
	public void setLng_Partenza(double lng_Partenza) {
		this.lng_Partenza = lng_Partenza;
	}
	public double getLat_Arrivo() {
		return lat_Arrivo;
	}
	public void setLat_Arrivo(double lat_Arrivo) {
		this.lat_Arrivo = lat_Arrivo;
	}
	public double getLng_Arrivo() {
		return lng_Arrivo;
	}
	public void setLng_Arrivo(double lng_Arrivo) {
		this.lng_Arrivo = lng_Arrivo;
	}
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	

}

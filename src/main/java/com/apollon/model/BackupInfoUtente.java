package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.apollon.model.User;


/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "backup_info_utente")
public class BackupInfoUtente extends BaseObject implements Serializable {
	private static final long serialVersionUID = 4360167589359184230L;
	
	private Long id;
    private Date date;
	
	// table user backup info 
	private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
	
    // table autista backup info 
    @Deprecated
    private BigDecimal prezzoDesideratoRicTransfert;
	private String note;
	private String iban;
	private int numCorseEseguite;
	private boolean bannato;
	private boolean attivo;
	private String numeroPatente;
    private byte[] immaginePatente;
    private String numeroCartaIdentita;
    private byte[] immagineCartaIdentita;
    
    // table autoveicolo backup info 
	private String marca;
	private String nomeModello;
	private String targa;
	private String annoImmatricolazione;
	

  	private User user;
  	@ManyToOne //(cascade = CascadeType.ALL ) // CascadeType.ALL mi permette di salvare un autista e creare un User associato
	@JoinColumn(name = "id_user", unique = false, nullable = true)
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	private Autista autista;
	@ManyToOne
	@JoinColumn(name = "id_autista", unique = false, nullable = true)
	public Autista getAutista() {
		return autista;
	}
    
	public void setAutista(Autista autista) {
		this.autista = autista;
	}

    
	private Autoveicolo autoveicolo;
	@ManyToOne
	@JoinColumn(name = "id_autoveicolo", unique = false, nullable = true)
	
	public Autoveicolo getAutoveicolo() {
		return autoveicolo;
	}

	public void setAutoveicolo(Autoveicolo autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	
	
	public BackupInfoUtente() {
		super();
	}

	/**
	 * Salvo info User
	 */
	public BackupInfoUtente(User user, Date date, String username, String firstName,
			String lastName, String email, String phoneNumber) {
		super();
		this.date = date;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.user = user;
	}

	/**
	 * Salvo info Autista
	 */
	public BackupInfoUtente(Autista autista, Date date, /*BigDecimal prezzoDesideratoRicTransfert,*/
			String note, String iban, int numCorseEseguite, boolean bannato, 
			boolean attivo, String numeroCartaIdentita) {
		super();
		this.date = date;
		//this.prezzoDesideratoRicTransfert = prezzoDesideratoRicTransfert;
		this.note = note;
		this.iban = iban;
		this.numCorseEseguite = numCorseEseguite;
		this.bannato = bannato;
		this.attivo = attivo;
		this.numeroCartaIdentita = numeroCartaIdentita;
		this.autista = autista;
	}
	
	
	/**
	 * Salvo info Autoveicolo
	 */
	public BackupInfoUtente(Autoveicolo autoveicolo, Date date, String marca, String nomeModello,
			String targa, String annoImmatricolazione) {
		super();
		this.date = date;
		this.marca = marca;
		this.nomeModello = nomeModello;
		this.targa = targa;
		this.autoveicolo = autoveicolo;
		this.annoImmatricolazione = annoImmatricolazione;
	}

	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_backup_info_utente")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/*
	public BigDecimal getPrezzoDesideratoRicTransfert() {
		return prezzoDesideratoRicTransfert;
	}
	public void setPrezzoDesideratoRicTransfert(
			BigDecimal prezzoDesideratoRicTransfert) {
		this.prezzoDesideratoRicTransfert = prezzoDesideratoRicTransfert;
	}
	*/
	
	@Column(length = 500)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getNumCorseEseguite() {
		return numCorseEseguite;
	}
	public void setNumCorseEseguite(int numCorseEseguite) {
		this.numCorseEseguite = numCorseEseguite;
	}

	public boolean isBannato() {
		return bannato;
	}
	public void setBannato(boolean bannato) {
		this.bannato = bannato;
	}

	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
	public String getNumeroPatente() {
		return numeroPatente;
	}
	public void setNumeroPatente(String numeroPatente) {
		this.numeroPatente = numeroPatente;
	}

	@Lob
	@Column(columnDefinition="mediumblob")
	public byte[] getImmaginePatente() {
		return immaginePatente;
	}
	public void setImmaginePatente(byte[] immaginePatente) {
		this.immaginePatente = immaginePatente;
	}

	public String getNumeroCartaIdentita() {
		return numeroCartaIdentita;
	}
	public void setNumeroCartaIdentita(String numeroCartaIdentita) {
		this.numeroCartaIdentita = numeroCartaIdentita;
	}
	
	@Lob
	@Column(columnDefinition="mediumblob")
	public byte[] getImmagineCartaIdentita() {
		return immagineCartaIdentita;
	}
	public void setImmagineCartaIdentita(byte[] immagineCartaIdentita) {
		this.immagineCartaIdentita = immagineCartaIdentita;
	}

	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNomeModello() {
		return nomeModello;
	}
	public void setNomeModello(String nomeModello) {
		this.nomeModello = nomeModello;
	}

	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getAnnoImmatricolazione() {
		return annoImmatricolazione;
	}
	public void setAnnoImmatricolazione(String annoImmatricolazione) {
		this.annoImmatricolazione = annoImmatricolazione;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupInfoUtente)) {
            return false;
        }

        final BackupInfoUtente backupInfoUtente = (BackupInfoUtente) o;

        return !(id != null ? !id.equals(backupInfoUtente.id) : backupInfoUtente.id != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.id)
                .toString();
    }

}

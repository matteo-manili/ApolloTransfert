package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "lista_invio_email_sms")
public class ListaInvioEmailSms extends BaseObject implements Serializable {
	private static final long serialVersionUID = 31475362145606752L;
	
	private Long id;
	private Integer tipoMessaggio;
	private boolean attivo;
	private RicercaTransfert ricercaTransfert;
	private boolean inviato;
	private String testoMessaggio;
	private String numeroDestinatario;
	private Date dataInvio;
	private String token;
    
	public ListaInvioEmailSms() { }

	public ListaInvioEmailSms(RicercaTransfert ricercaTransfert, Integer tipoMessaggio, String testoMessaggio, String numeroDestinatario, String token, Date dataInvio) {
		super();
		this.tipoMessaggio = tipoMessaggio;
		this.ricercaTransfert = ricercaTransfert;
		this.testoMessaggio = testoMessaggio;
		this.numeroDestinatario = numeroDestinatario;
		this.dataInvio = dataInvio;
		this.token = token;
		this.attivo = true;
		this.inviato = false;
	}


  	
  	@ManyToOne(fetch = FetchType.LAZY)  //(cascade = CascadeType.ALL ) // CascadeType.ALL mi permette di salvare un autista e creare un User associato
	@JoinColumn(name = "id_ricerca_transfert", unique = false, nullable = true)
  	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_lista_invio_email_sms")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column(nullable = false, length = 50)
	public Integer getTipoMessaggio() {
		return tipoMessaggio;
	}
	public void setTipoMessaggio(Integer tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}
	
	
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
	
	public boolean isInviato() {
		return inviato;
	}
	public void setInviato(boolean inviato) {
		this.inviato = inviato;
	}
	
	public Date getDataInvio() {
		return dataInvio;
	}
	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}
	
	@Column(length = 1000)
	public String getTestoMessaggio() {
		return testoMessaggio;
	}
	public void setTestoMessaggio(String testoMessaggio) {
		this.testoMessaggio = testoMessaggio;
	}

	public String getNumeroDestinatario() {
		return numeroDestinatario;
	}
	public void setNumeroDestinatario(String numeroDestinatario) {
		this.numeroDestinatario = numeroDestinatario;
	}
	
	@Column(unique = true, nullable = false)
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}



	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListaInvioEmailSms)) {
            return false;
        }
        final ListaInvioEmailSms listaInvioSms = (ListaInvioEmailSms) o;
        return !(id != null ? !id.equals(listaInvioSms.id) : listaInvioSms.id != null);
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

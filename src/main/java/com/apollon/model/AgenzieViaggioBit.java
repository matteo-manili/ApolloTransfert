package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.Constants;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_agenzie_viaggio_bit")
public class AgenzieViaggioBit extends BaseObject implements Serializable {
	private static final long serialVersionUID = -1271099978966060291L;

	private Long id;
	
	private String email;
	private String nome;
	private boolean unsubscribe;
	
	private String citta_e_indirizzo;
	private Date dataInvioLastEmail;
	private int numeroEmailInviate;
	
	private String parametriSconto;
	private String sitoWebScraping;
	private String sitoWeb;
	
    public AgenzieViaggioBit() { }
    
    @Transient
	public Integer getPercentualeSconto() {
		if(this.parametriSconto != null){
			try{
				JSONObject json = new JSONObject(this.parametriSconto);
				return json.getInt(Constants.PercentualeScontoJSON);
			}catch(JSONException JsonExc ){
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Transient
	public String getCodiceSconto() {
		if(this.parametriSconto != null){
			try{
				JSONObject json = new JSONObject(this.parametriSconto);
				return json.getString(Constants.CodiceScontoJSON);
			}catch(JSONException JsonExc ){
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Transient
	public Boolean getCodiceScontoUsato() {
		if(this.parametriSconto != null){
			try{
				JSONObject json = new JSONObject(this.parametriSconto);
				return json.getBoolean(Constants.CodiceScontoUsatoJSON);
			}catch(JSONException JsonExc ){
				return null;
			}
		}else{
			return null;
		}
	}
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_agenzie_viaggio_bit")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column(name = "unsubscribe", columnDefinition="tinyint(1) default 0")
	public boolean isUnsubscribe() {
		return unsubscribe;
	}

	public void setUnsubscribe(boolean unsubscribe) {
		this.unsubscribe = unsubscribe;
	}

	public Date getDataInvioLastEmail() {
		return dataInvioLastEmail;
	}
	public void setDataInvioLastEmail(Date dataInvioLastEmail) {
		this.dataInvioLastEmail = dataInvioLastEmail;
	}

	public int getNumeroEmailInviate() {
		return numeroEmailInviate;
	}
	public void setNumeroEmailInviate(int numeroEmailInviate) {
		this.numeroEmailInviate = numeroEmailInviate;
	}

	@Column(unique = true, nullable = true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getParametriSconto() {
		return parametriSconto;
	}
	public void setParametriSconto(String parametriSconto) {
		this.parametriSconto = parametriSconto;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCitta_e_indirizzo() {
		return citta_e_indirizzo;
	}
	public void setCitta_e_indirizzo(String citta_e_indirizzo) {
		this.citta_e_indirizzo = citta_e_indirizzo;
	}
	
	public String getSitoWebScraping() {
		return sitoWebScraping;
	}
	public void setSitoWebScraping(String sitoWebScraping) {
		this.sitoWebScraping = sitoWebScraping;
	}
	
	public String getSitoWeb() {
		return sitoWeb;
	}
	public void setSitoWeb(String sitoWeb) {
		this.sitoWeb = sitoWeb;
	}
	
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgenzieViaggioBit)) {
            return false;
        }
        final AgenzieViaggioBit agenzieViaggioBit = (AgenzieViaggioBit) o;
        return !(id != null ? !id.equals(agenzieViaggioBit.id) : agenzieViaggioBit.id != null);
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

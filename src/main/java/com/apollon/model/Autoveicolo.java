package com.apollon.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.json.JSONException;
import org.json.JSONObject;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "autoveicolo")
public class Autoveicolo extends BaseObject implements Serializable {
	private static final long serialVersionUID = 7829297426883085357L;

	private Long id;
	private String annoImmatricolazione;
	private String targa;
	private boolean autoveicoloSospeso;
	private boolean autoveicoloCancellato;
	private String info;
	
	@Transient
	public Integer getAutoClearProssimeOreGiornate() {
		if(this.info != null){
			try{ JSONObject json = new JSONObject(this.info); return json.getInt(AgA_General.JN_AutoClearProssimeOreGiornate);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Double getAgA_AreaGeografica_Lat() {
		if(this.info != null){
			try{ JSONObject json = new JSONObject(this.info); return json.getDouble(AgA_General.JN_AreaGeog_Lat);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Double getAgA_AreaGeografica_Lng() {
		if(this.info != null){
			try{ JSONObject json = new JSONObject(this.info); return json.getDouble(AgA_General.JN_AreaGeog_Lng);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public Double getAgA_AreaGeografica_Raggio() {
		if(this.info != null){
			try{ JSONObject json = new JSONObject(this.info); return json.getDouble(AgA_General.JN_raggio);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null; }
	}
	
	@Transient
	public String getAgA_AreaGeografica_Address() {
		if(this.info != null){
			try{ JSONObject json = new JSONObject(this.info); return json.getString(AgA_General.JN_AreaGeog_Address);
			}catch(JSONException JsonExc){ return null; }
		}else{ return null;
		}
	}
	
	
	@Transient
    public String getMarcaModello() {
		return this.modelloAutoNumeroPosti.getModelloAutoScout().getMarcaAutoScout().getName()+" "+this.modelloAutoNumeroPosti.getModelloAutoScout().getName();
    }
	
	@Transient
    public String getMarcaModelloTarga() {
		return getMarcaModello()+" "+this.targa;
    }
	
	/**
	 * MI RITORNA LA REALE CLASSE AUTOVEICOLO
	 */
	@Transient
	public ClasseAutoveicolo getClasseAutoveicoloReale() {
		return AutoveicoloUtil.DammiAutoClasseReale(this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId(), this.annoImmatricolazione);
	}
	
	
    @Transient
    public Integer getNumeroPostiPasseggeri() {
    	if( this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId() == 1 
    			||  this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId() == 2
    			|| this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId() == 3) {
    		return modelloAutoNumeroPosti.getNumeroPostiAuto().getNumero() - 2;
    	}else if( this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId() == 4 
    			||  this.modelloAutoNumeroPosti.getModelloAutoScout().getClasseAutoveicolo().getId() == 5) {
    		return modelloAutoNumeroPosti.getNumeroPostiAuto().getNumero() - 1;
    	}else {
    		return null;
    	}
    }
	
	/*
	 * UTILIZZO QUESTI COME APPOGGIO QUANDO FACCIO L'INSERIMENTO DI UN AUTOVEICOLO
	 */
	private MarcaAutoScout marcaAutoScoutAppoggio;
	@Transient
	public MarcaAutoScout getMarcaAutoScoutAppoggio() {
		return marcaAutoScoutAppoggio;
	}
	@Transient
	public void setMarcaAutoScoutAppoggio(MarcaAutoScout marcaAutoScoutAppoggio) {
		this.marcaAutoScoutAppoggio = marcaAutoScoutAppoggio;
	}
	private ModelloAutoScout modelloAutoScoutAppoggio;
	@Transient
	public ModelloAutoScout getModelloAutoScoutAppoggio() {
		return modelloAutoScoutAppoggio;
	}
	@Transient
	public void setModelloAutoScoutAppoggio(ModelloAutoScout modelloAutoScoutAppoggio) {
		this.modelloAutoScoutAppoggio = modelloAutoScoutAppoggio;
	}
	
	//-------------------- AUTISTA --------------------------
    private Autista autista;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_autista", unique = false, nullable = false)
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	
	//-------------------- MODELLO AUTOSCOUT NUMERO POSTI --------------------------
	private ModelloAutoNumeroPosti modelloAutoNumeroPosti;
	@ManyToOne(fetch = FetchType.EAGER)
	// TODO METTO TEMPORANEMENTE A TRUE IL NULLEBOLE PER PER FARE L'INSERT DEI DATI, POI DEVO METTERLO A FALSE !!!!
	@JoinColumn(name = "id_modello_auto_numero_posti", unique = false, nullable = true)
	public ModelloAutoNumeroPosti getModelloAutoNumeroPosti() {
		return modelloAutoNumeroPosti;
	}
	public void setModelloAutoNumeroPosti(
			ModelloAutoNumeroPosti modelloAutoNumeroPosti) {
		this.modelloAutoNumeroPosti = modelloAutoNumeroPosti;
	}
	
	//---------- CARTA DI CIRCOLAZIONE ----------
    private AutoveicoloCartaCircolazione autoveicoloCartaCircolazione = new AutoveicoloCartaCircolazione();
	@Embedded
    @IndexedEmbedded
    public AutoveicoloCartaCircolazione getAutoveicoloCartaCircolazione() {
		return autoveicoloCartaCircolazione;
	}
	public void setAutoveicoloCartaCircolazione(
			AutoveicoloCartaCircolazione autoveicoloCartaCircolazione) {
		this.autoveicoloCartaCircolazione = autoveicoloCartaCircolazione;
	}
	
	//------------- AGENDA AUTISA - MODELLI TARIFFARI -------------
	private Set<AgA_AutoveicoloModelliTariffari> agA_AutoveicoloModelliTariffari; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autoveicolo")
	@Fetch(FetchMode.SELECT)
	public Set<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari() {
		return agA_AutoveicoloModelliTariffari;
	}
	public void setAgA_AutoveicoloModelliTariffari(
			Set<AgA_AutoveicoloModelliTariffari> agA_AutoveicoloModelliTariffari) {
		this.agA_AutoveicoloModelliTariffari = agA_AutoveicoloModelliTariffari;
	}
	
	//----------------- DISPONIBILITA ------------------------------
	@Deprecated
	private Set<Disponibilita> disponibilita; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autoveicolo")
	@Fetch(FetchMode.SELECT)
    public Set<Disponibilita> getDisponibilita() {
		return disponibilita;
	}
	public void setDisponibilita(Set<Disponibilita> disponibilita) {
		this.disponibilita = disponibilita;
	}

	//----------------- TARIFFE ------------------------------
	@Deprecated
	private Set<Tariffe> tariffe;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autoveicolo", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SELECT)
	public Set<Tariffe> getTariffe() {
		return tariffe;
	}
	public void setTariffe(Set<Tariffe> tariffe) {
		this.tariffe = tariffe;
	}
	
    public Autoveicolo() { }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autoveicolo")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 10)
	public String getAnnoImmatricolazione() {
		return annoImmatricolazione;
	}
	public void setAnnoImmatricolazione(String annoImmatricolazione) {
		this.annoImmatricolazione = annoImmatricolazione;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Column(length = 20, unique = true, nullable = false)
	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}
	
	@Column(name = "autoveicolo_sospeso", columnDefinition="tinyint(1) default 0")
	public boolean isAutoveicoloSospeso() {
		return autoveicoloSospeso;
	}
	public void setAutoveicoloSospeso(boolean autoveicoloSospeso) {
		this.autoveicoloSospeso = autoveicoloSospeso;
	}

	@Column(name = "autoveicolo_cancellato", columnDefinition="tinyint(1) default 0")
	public boolean isAutoveicoloCancellato() {
		return autoveicoloCancellato;
	}
	public void setAutoveicoloCancellato(boolean autoveicoloCancellato) {
		this.autoveicoloCancellato = autoveicoloCancellato;
	}

	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autoveicolo)) {
            return false;
        }

        final Autoveicolo auto = (Autoveicolo) o;

        return !(id != null ? !id.equals(auto.id) : auto.id != null);

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

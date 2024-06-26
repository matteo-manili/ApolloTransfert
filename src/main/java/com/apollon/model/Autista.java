package com.apollon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.apollon.model.User;

/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *	
 *	I VALORI DI DEFAULT DELLE ANNOTAZIONI JOIN SONO
 *	@OneToMany: LAZY
	@ManyToOne: EAGER
	@ManyToMany: LAZY
	@OneToOne: EAGER
 *
 *	CascadeType.PERSIST Le operazioni di salvataggio hanno effetto sulle istanze di classi
	associate quando viene chiamato il metodo persist()
	CascadeType.REFRESH Le operazioni di aggiornamento hanno effetto sulle istanze di
	classi associate quando viene chiamato il metodo refresh()
	CascadeType.REMOVE Le operazioni di eliminazione hanno effetto sulle istanze di classi
	associate quando viene chiamato il metodo delete()
	CascadeType.ALL Tutte le operazioni hanno effetto sulle istanze di classi associate
	
	
	E’ importante spiegare un aspetto del mapping di associazioni. Per poter utilizzare i metodi della classe
	Acquisto, totale() e calcolaSconto(), che coinvolgono buona parte delle tabelle della base di
	dati, è stato necessario mappare alcune collezioni con la proprietà fetch=FetchType.EAGER che
	imposta la strategia di eager fetching, cioè caricamento in memoria immeditato dei dati delle collezioni.
	Questa strategia peggiora molto le performance rispetto alla strategia di default lazy fetching, ma è utile
	per notare il livello di isolamento che JPA può fornire dalla base di dati.
	La gerarchia di classi composta da Articolo, Abbigliamento e Calzature è stata mappata con la
	strategia una tabella per sottoclasse
	
	,cascade = CascadeType.REFRESH 
 */
@Entity
@Table(name = "autista")
public class Autista extends BaseObject implements Serializable {
	private static final long serialVersionUID = -3629781333360890050L;

	private Long id;
	private boolean azienda;
	
	/*
	@Deprecated
	private BigDecimal prezzoDesideratoRicTransfert;
	@Deprecated
	private BigDecimal prezzoDesideratoRicTransfert_LP;
	*/
	
	private String note;
	private int numCorseEseguite;
	private boolean bannato;
	private boolean attivo;

	
	/**
	 * vedere DocumentiUtil
	 */
	private String documentiCompletatiFrazione;
	@Transient
	public String getDocumentiCompletatiFrazione() {
		return documentiCompletatiFrazione;
	}
	@Transient
	public void setDocumentiCompletatiFrazione(String documentiCompletatiFrazione) {
		this.documentiCompletatiFrazione = documentiCompletatiFrazione;
	}
	
	/**
	 * vedere DocumentiUtil
	 */
	private String documentiApprovatiFrazione;
	@Transient
    public String getDocumentiApprovatiFrazione() {
		return documentiApprovatiFrazione;
	}
	@Transient
	public void setDocumentiApprovatiFrazione(String documentiApprovatiFrazione) {
		this.documentiApprovatiFrazione = documentiApprovatiFrazione;
	}

	//---------- DOCUMENTO PERSONALE E PARTITA IVA ----------
    private AutistaDocumento autistaDocumento = new AutistaDocumento();
	@Embedded
    @IndexedEmbedded
    public AutistaDocumento getAutistaDocumento() {
		return autistaDocumento;
	}
	public void setAutistaDocumento(AutistaDocumento autistaDocumento) {
		this.autistaDocumento = autistaDocumento;
	}
	
    //-------------------- USER --------------------------
  	private User user;
  	@ManyToOne(fetch = FetchType.EAGER) //(cascade = CascadeType.ALL ) // CascadeType.ALL mi permette di salvare un autista e creare un User associato
	@JoinColumn(name = "id_user", unique = true, nullable = false)
    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	//-------------------- USER COMMERCIALE --------------------------
  	private User commerciale;
  	@ManyToOne(fetch = FetchType.LAZY)  //(cascade = CascadeType.ALL ) // CascadeType.ALL mi permette di salvare un autista e creare un User associato
	@JoinColumn(name = "id_commerciale", unique = false, nullable = true)
  	public User getCommerciale() {
		return commerciale;
	}
	public void setCommerciale(User commerciale) {
		this.commerciale = commerciale;
	}

	private Set<Autoveicolo> autoveicolo;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "autista") 
	@Fetch(FetchMode.SELECT)
	public Set<Autoveicolo> getAutoveicolo() {
		return autoveicolo;
	}
	public void setAutoveicolo(Set<Autoveicolo> autoveicolo) {
		this.autoveicolo = autoveicolo;
	}
	
	private Set<AutistaZone> autistaZone;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "autista" )
	@Fetch(FetchMode.SELECT)
	@OrderBy("id DESC")
	public Set<AutistaZone> getAutistaZone() {
		return autistaZone;
	}

	public void setAutistaZone(Set<AutistaZone> autistaZone) {
		this.autistaZone = autistaZone;
	}
	public void addAutistaZone(AutistaZone role) {
		getAutistaZone().add(role);
    }
	
	
	private Set<Tariffe> tariffe;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autista" )
	@Fetch(FetchMode.SELECT)
	@OrderBy("id DESC")
	public Set<Tariffe> getTariffe() {
		return tariffe;
	}
	public void setTariffe(Set<Tariffe> tariffe) {
		this.tariffe = tariffe;
	}
	
	//infrastruttire..........
	
	private Set<AutistaAeroporti> autistaAeroporti;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autista")
	@Fetch(FetchMode.SELECT)
	@OrderBy("id DESC")
	public Set<AutistaAeroporti> getAutistaAeroporti() {
		return autistaAeroporti;
	}
	public void setAutistaAeroporti(Set<AutistaAeroporti> autistaAeroporti) {
		this.autistaAeroporti = autistaAeroporti;
	}

	private Set<AutistaPortiNavali> autistaPorti; 
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autista")
	@Fetch(FetchMode.SELECT)
	@OrderBy("id DESC")
	public Set<AutistaPortiNavali> getAutistaPorti() {
		return autistaPorti;
	}
	public void setAutistaPorti(Set<AutistaPortiNavali> autistaPorti) {
		this.autistaPorti = autistaPorti;
	}

	private Set<AutistaMusei> autistaMusei;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autista")
	@Fetch(FetchMode.SELECT)
	@OrderBy("id DESC")
	public Set<AutistaMusei> getAutistaMusei() {
		return autistaMusei;
	}
	public void setAutistaMusei(Set<AutistaMusei> autistaMusei) {
		this.autistaMusei = autistaMusei;
	}

	
	/**
	 * NON LO USO PIU è L'EX CHOSEN, ma forse in qualche jsp è ancora chiamato
	 * 
	 * //le jsp i form spring path voglio solo liste di String, non di map o altri oggetti più complessi....
	 */
	private List<String> listZoneSelezionate_TAG = new ArrayList<String>();
	@Transient
	public List<String> getListZoneSelezionate_TAG() {
		return listZoneSelezionate_TAG;
	}
	@Transient
	public void setListZoneSelezionate_TAG(List<String> listZoneSelezionate_TAG) {
		this.listZoneSelezionate_TAG = listZoneSelezionate_TAG;
	}

	public Autista() {
		super();
	}

	/**
	 * creo un nuovo autista con le informazione basi valorizzate.
	 */
	public Autista(User user, boolean attivo) {
		super();
		this.user = user;
		this.attivo = attivo;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_autista")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(columnDefinition="tinyint(1) default 0")
	public boolean isAzienda() {
		return azienda;
	}
	public void setAzienda(boolean azienda) {
		this.azienda = azienda;
	}

	/*
	public BigDecimal getPrezzoDesideratoRicTransfert() {
		return prezzoDesideratoRicTransfert;
	}
	public void setPrezzoDesideratoRicTransfert(BigDecimal prezzoDesideratoRicTransfert) {
		this.prezzoDesideratoRicTransfert = prezzoDesideratoRicTransfert;
	}

	public BigDecimal getPrezzoDesideratoRicTransfert_LP() {
		return prezzoDesideratoRicTransfert_LP;
	}
	public void setPrezzoDesideratoRicTransfert_LP(
			BigDecimal prezzoDesideratoRicTransfert_LP) {
		this.prezzoDesideratoRicTransfert_LP = prezzoDesideratoRicTransfert_LP;
	}
	*/


	@Column(length = 1000)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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

	@Column(columnDefinition="tinyint(1) default 1")
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autista)) {
            return false;
        }

        final Autista tipoRuoloServiziDog = (Autista) o;

        return !(id != null ? !id.equals(tipoRuoloServiziDog.id) : tipoRuoloServiziDog.id != null);

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

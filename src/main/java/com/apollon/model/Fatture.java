package com.apollon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "fatture")
public class Fatture extends BaseObject implements Serializable {
	private static final long serialVersionUID = -6557913664791331834L;

	private Long id;
	/*
	 * Quando faccio un rimborso devo avvisare il cliente e mandargli la nuova fattura (nota di credito)
	 * questo campo serve per capire quante volte ho avvisato il Cliente
	 */
	private Integer numeroAvvisiInviatiRimborso;
	/*
	 * nella pagina gestione ritardi, al click del bottone invia sollecito viene inviata una email e un SMS al cliente per solecitare il pagamento del ritardo
	 * questo campo serve per capire quante volte ho inviato il sollecito al Cliente
	 */
	private Integer numeroSollecitiInviatiRitardo;
	private Long progressivoFattura;

	
	Supplementi supplementi;
	@ManyToOne
	@JoinColumn(name = "id_supplementi", unique = true, nullable = true)
	public Supplementi getSupplementi() {
		return supplementi;
	}
	public void setSupplementi(Supplementi supplementi) {
		this.supplementi = supplementi;
	}
	
	Ritardi ritardi;
	@ManyToOne
	@JoinColumn(name = "id_ritardi", unique = true, nullable = true)
	public Ritardi getRitardi() {
		return ritardi;
	}
	public void setRitardi(Ritardi ritardi) {
		this.ritardi = ritardi;
	}

	RicercaTransfert ricercaTransfert;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert", unique = true, nullable = true)
	public RicercaTransfert getRicercaTransfert() {
		return ricercaTransfert;
	}
	public void setRicercaTransfert(RicercaTransfert ricercaTransfert) {
		this.ricercaTransfert = ricercaTransfert;
	}

	/*
	 * La nota di credito è un documento fiscale che deve seguire gli stessi requisiti della fattura 
	 * art. 21 del DPR 633/72. Alla dicitura "fattura" va sostituita la dicitura "nota credito". 
	 * Viene adottata dal soggetto emittente per stornare in tutto o in parte una fattura precedentemente emessa.
	 * 
	 */
	RicercaTransfert ricercaTransfertRimborso;
	@ManyToOne
	@JoinColumn(name = "id_ricerca_transfert_rimborso", unique = true, nullable = true)
	public RicercaTransfert getRicercaTransfertRimborso() {
		return ricercaTransfertRimborso;
	}
	public void setRicercaTransfertRimborso(
			RicercaTransfert ricercaTransfertRimborso) {
		this.ricercaTransfertRimborso = ricercaTransfertRimborso;
	}

	public Fatture() {

	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_fatture")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroAvvisiInviatiRimborso() {
		return numeroAvvisiInviatiRimborso;
	}
	public void setNumeroAvvisiInviatiRimborso(Integer numeroAvvisiInviatiRimborso) {
		this.numeroAvvisiInviatiRimborso = numeroAvvisiInviatiRimborso;
	}
	
	public Integer getNumeroSollecitiInviatiRitardo() {
		return numeroSollecitiInviatiRitardo;
	}
	public void setNumeroSollecitiInviatiRitardo(
			Integer numeroSollecitiInviatiRitardo) {
		this.numeroSollecitiInviatiRitardo = numeroSollecitiInviatiRitardo;
	}
	
	@Column(nullable = false)
	public Long getProgressivoFattura() {
		return progressivoFattura;
	}
	public void setProgressivoFattura(Long progressivoFattura) {
		this.progressivoFattura = progressivoFattura;
	}
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fatture)) {
            return false;
        }

        final Fatture fatture = (Fatture) o;

        return !(id != null ? !id.equals(fatture.id) : fatture.id != null);

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

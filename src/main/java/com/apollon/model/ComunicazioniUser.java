package com.apollon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * @author Matteo Manili - matteo.manili@gmail.com
 *
 */
@Entity
@Table(name = "data_comunicazioni_user", uniqueConstraints={@UniqueConstraint(columnNames={"id","id_data_comunicazioni"})})
public class ComunicazioniUser extends BaseObject implements Serializable {
	private static final long serialVersionUID = 2738225758257796267L;
	
	
	private Long id;
	private int numEmailInviate;
	private Date dataInvioLastEmail;

	
	private Comunicazioni comunicazioni;
	@ManyToOne
	@JoinColumn(name = "id_data_comunicazioni", unique = false, nullable = false)
	public Comunicazioni getComunicazioni() {
		return comunicazioni;
	}
	public void setComunicazioni(Comunicazioni comunicazioni) {
		this.comunicazioni = comunicazioni;
	}
	
	private User user;
	@ManyToOne
	@JoinColumn(name = "id", unique = false, nullable = false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	public ComunicazioniUser() { }


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_data_comunicazioni_user")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getNumEmailInviate() {
		return numEmailInviate;
	}
	public void setNumEmailInviate(int numEmailInviate) {
		this.numEmailInviate = numEmailInviate;
	}

	public Date getDataInvioLastEmail() {
		return dataInvioLastEmail;
	}
	public void setDataInvioLastEmail(Date dataInvioLastEmail) {
		this.dataInvioLastEmail = dataInvioLastEmail;
	}
	
	
	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComunicazioniUser)) {
            return false;
        }

        final ComunicazioniUser backupInfoUtente = (ComunicazioniUser) o;

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

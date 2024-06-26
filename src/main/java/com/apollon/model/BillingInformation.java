package com.apollon.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import java.io.Serializable;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * This class is used to represent an address with address,
 * city, province and postal-code information.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Embeddable
@Indexed
public class BillingInformation extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3617859655330969141L;
    private String denominazioneCliente;
    private String codiceFiscalePartitaIva;
    private String address;
    private String city;
    private String postalCode;
    private String province;
    private String country;
    
    
    @Transient
    public String getIndirizzoFatturazione() {
    	String Indirizzo = "";
    	if(address != null && !address.equals("")){
    		Indirizzo = address +", ";
    	}
    	if(city != null && !city.equals("")){
    		Indirizzo = Indirizzo + city +", ";
    	}
    	if(postalCode != null && !postalCode.equals("")){
    		Indirizzo = Indirizzo + postalCode +", ";
    	}
    	if(province != null && !province.equals("")){
    		Indirizzo = Indirizzo + province +", ";
    	}
    	if(country != null && !country.equals("")){
    		Indirizzo = Indirizzo + country;
    	}
        return Indirizzo;
    }
    
    /*
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("country", this.country)
                .append("address", this.address)
                .append("province", this.province)
                .append("postalCode", this.postalCode)
                .append("city", this.city).toString();
    }
    */
    
    
    public String getDenominazioneCliente() {
		return denominazioneCliente;
	}
	public void setDenominazioneCliente(String denominazioneCliente) {
		this.denominazioneCliente = denominazioneCliente;
	}
	
	public String getCodiceFiscalePartitaIva() {
		return codiceFiscalePartitaIva;
	}
	public void setCodiceFiscalePartitaIva(String codiceFiscalePartitaIva) {
		this.codiceFiscalePartitaIva = codiceFiscalePartitaIva;
	}
	
	@Column(length = 150)
    @Field
    public String getAddress() {
        return address;
    }

    @Column(length = 50)
    @Field
    public String getCity() {
        return city;
    }

    @Column(length = 100)
    @Field
    public String getProvince() {
        return province;
    }

    @Column(length = 100)
    @Field
    public String getCountry() {
        return country;
    }

    @Column(name = "postal_code", length = 15)
    @Field(analyze= Analyze.NO)
    public String getPostalCode() {
        return postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Overridden equals method for object comparison. Compares based on hashCode.
     *
     * @param o Object to compare
     * @return true/false based on hashCode
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingInformation)) {
            return false;
        }

        final BillingInformation address1 = (BillingInformation) o;

        return this.hashCode() == address1.hashCode();
    }

    /**
     * Overridden hashCode method - compares on address, city, province, country and postal code.
     *
     * @return hashCode
     */
    public int hashCode() {
        int result;
        result = (address != null ? address.hashCode() : 0);
        result = 29 * result + (city != null ? city.hashCode() : 0);
        result = 29 * result + (province != null ? province.hashCode() : 0);
        result = 29 * result + (country != null ? country.hashCode() : 0);
        result = 29 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("country", this.country)
                .append("address", this.address)
                .append("province", this.province)
                .append("postalCode", this.postalCode)
                .append("city", this.city).toString();
    }
}

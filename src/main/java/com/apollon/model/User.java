package com.apollon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.apollon.model.TipoRuoli;
import com.apollon.webapp.util.RecensioneTransferUtil;

/**
 * This class represents the basic "user" object in AppFuse that allows for authentication
 * and user management.  It implements Spring Security's UserDetails interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *         Extended to implement Spring UserDetails interface
 *         by David Carter david@carter.net
 */
@Entity
@Table(name = "app_user")
@Indexed
@XmlRootElement
public class User extends BaseObject implements Serializable, UserDetails {
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private String ipAddressSignUp; 
    private Date signupDate;
    private String username;                    // required
    private String password;                    // required
    private String confirmPassword;
    private String passwordHint;
    private String firstName;                   // required
    private String lastName;                    // required
    private String email;                       // required; unique
    private String phoneNumber;
    private String website;
    private Integer version;
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private String note;
    private String codiceVenditore;
    @Transient
	boolean checkPrivacyPolicy;

    private BillingInformation billingInformation = new BillingInformation();
	@Embedded
    @IndexedEmbedded
    public BillingInformation getBillingInformation() {
		return billingInformation;
	}
	public void setBillingInformation(BillingInformation billingInformation) {
		this.billingInformation = billingInformation;
	}

	@Transient
	public boolean isCheckPrivacyPolicy() {
		return checkPrivacyPolicy;
	}
	
	@Transient
	public void setCheckPrivacyPolicy(boolean checkPrivacyPolicy) {
		this.checkPrivacyPolicy = checkPrivacyPolicy;
	}

	@Transient
	public RecensioneTransferUtil getRecensioneTransfer() {
		return new RecensioneTransferUtil(this.website, this.id);
	}
	
	
    /**
     * Returns the full name.
    
     * @return firstName + ' ' + lastName
     */
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getFullName() {
        return firstName+' '+lastName;
    }
    
    
    private Set<Role> roles = new HashSet<Role>();
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public void addRole(Role role) {
        getRoles().add(role);
    }
    

    private Set<TipoRuoli> tipoRuoli = new HashSet<TipoRuoli>();
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "user_tipo_ruoli",
 	    joinColumns = @JoinColumn (name = "user_id"),
 	    inverseJoinColumns = @JoinColumn(name = "tipo_ruoli_id"))
    public Set<TipoRuoli> getTipoRuoli() {
		return tipoRuoli;
	}
	public void setTipoRuoli(Set<TipoRuoli> tipoRuoli) {
		this.tipoRuoli = tipoRuoli;
	}
	public void addTipoRuoli(TipoRuoli tipoRuoli){
		getTipoRuoli().add(tipoRuoli);
	}
    
	/**
	 *  LO USO PER LO JWT
	 * @param username
	 * @param password
	 * @param authorities
	 * @param enabled
	 */
	public User(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled) {
        this.username = username;
        this.password = password;
        //this.authorities = authorities;
        this.enabled = enabled;
    }
	
    public User() { }
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    public Long getId() {
        return id;
    }
    
    @Column(length=10, unique=true, nullable=true)
    public String getCodiceVenditore() {
		return codiceVenditore;
	}
    
    @Column(nullable=false, length=50, unique=true)
    @Field
    public String getUsername() {
        return username;
    }
    
	public void setCodiceVenditore(String codiceVenditore) {
		this.codiceVenditore = codiceVenditore;
	}
	
    @Field
	public String getIpAddressSignUp() {
		return ipAddressSignUp;
	}
	public void setIpAddressSignUp(String ipAddressSignUp) {
		this.ipAddressSignUp = ipAddressSignUp;
	}
	
	public Date getSignupDate() {
		return signupDate;
	}
	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public User(final String username) {
        this.username = username;
    }

    @Column(nullable = false)
    @XmlTransient
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Column(name = "password_hint")
    @XmlTransient
    public String getPasswordHint() {
        return passwordHint;
    }

    @Column(name = "first_name", length = 50)
    @Field
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", length = 50)
    @Field
    public String getLastName() {
        return lastName;
    }

    @Column(nullable = false, unique = true)
    @Field
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number", nullable = false, length = 30, unique = true)
    //@Field(analyze= Analyze.NO)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Field
    public String getWebsite() {
        return website;
    }

    
    
    /**
     * Convert user roles to LabelValue objects for convenience.
     *
     * @return a list of LabelValue objects with role information
     */
    @Transient
    public List<LabelValue> getRoleList() {
        List<LabelValue> userRoles = new ArrayList<LabelValue>();
        if (this.roles != null) {
            for (Role role : roles) {
                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getName(), role.getName()));
            }
        }
        return userRoles;
    }
    
    
    @Transient
    public List<LabelValue> getTipoRuoliList() {
        List<LabelValue> userTipoRuoli = new ArrayList<LabelValue>();

        if (this.tipoRuoli != null) {
            for (TipoRuoli role : tipoRuoli) {
                // convert the user's roles to LabelValue Objects
            	userTipoRuoli.add(new LabelValue(role.getName(), role.getName()));
            }
        }
        return userTipoRuoli;
    }
    

    /**
     * @return GrantedAuthority[] an array of roles.
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @Transient
    @JsonIgnore // needed for UserApiITest in appfuse-ws archetype
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.addAll(roles);
        return authorities;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    @Column(name = "account_enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @Column(name = "account_expired", nullable = false)
    public boolean isAccountExpired() {
        return accountExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     * @return true if account is still active
     */
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Column(name = "account_locked", nullable = false)
    public boolean isAccountLocked() {
        return accountLocked;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     * @return false if account is locked
     */
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Column(name = "credentials_expired", nullable = false)
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     * @return true if credentials haven't expired
     */
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }
    
    @Column(length = 1000)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
        		.append("username", this.username).append("enabled", this.enabled).append("accountExpired", this.accountExpired).append("credentialsExpired", this.credentialsExpired)
        			.append("accountLocked", this.accountLocked);
        if (roles != null) {
            sb.append("Granted Authorities: ");

            int i = 0;
            for (Role role : roles) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(role.toString());
                i++;
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString();
    }
}

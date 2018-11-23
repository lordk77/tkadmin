package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TDEF_USER")
@XmlRootElement
public class User implements Serializable{

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String email;
	private Boolean active;
	private Date emailVerficationDate;
	private String emailVerificationCode;

	
	private String name;
	private String surname;
	private String phone;
	private String language;
	private String country;
	private String zip;
	private String platform;
	private String app_version;
	
	private String fb_identifier;
	private String bt_identifier;
	private String stripe_identifier;

    
    
	
	  private Date created;
	  private Date updated;
	  @PrePersist
	  protected void onCreate() {
	    created = new Date();
	  }

	  @PreUpdate
	  protected void onUpdate() {
	    updated = new Date();
	  }
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="TDEF_USER_ROLE",joinColumns=
	@JoinColumn(name="username",referencedColumnName="username"), inverseJoinColumns=	@JoinColumn(name="rolename",referencedColumnName="rolename"))
	private List<Role> roles=new ArrayList<Role>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ORGANIZATION_ID")
	private Organization organization;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="WALLET_ID")	
	private Wallet wallet;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getEmailVerficationDate() {
		return emailVerficationDate;
	}
	public void setEmailVerficationDate(Date emailVerficationDate) {
		this.emailVerficationDate = emailVerficationDate;
	}
	public String getEmailVerificationCode() {
		return emailVerificationCode;
	}
	public void setEmailVerificationCode(String emailVerificationCode) {
		this.emailVerificationCode = emailVerificationCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public String getFb_identifier() {
		return fb_identifier;
	}

	public void setFb_identifier(String fb_identifier) {
		this.fb_identifier = fb_identifier;
	}

	public String getBt_identifier() {
		return bt_identifier;
	}

	public void setBt_identifier(String bt_identifier) {
		this.bt_identifier = bt_identifier;
	}

	public String getStripe_identifier() {
		return stripe_identifier;
	}

	public void setStripe_identifier(String stripe_identifier) {
		this.stripe_identifier = stripe_identifier;
	}
	
	@Transient
	public  boolean isMerchant()
	{
		boolean merchant = false;
		
		if(this.getRoles()!=null)
		{
			for (Role r:this.getRoles())
				if("merchant".equalsIgnoreCase(r.getRolename()))
					merchant=true;
		}
		return merchant;
	}
			
	
}

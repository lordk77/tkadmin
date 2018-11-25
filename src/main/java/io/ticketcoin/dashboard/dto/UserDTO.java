package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.model.Wallet;

@XmlRootElement
public class UserDTO implements Serializable
{
	private static final long serialVersionUID = -9053032972156743082L;
	
	private Long id;
	private String username;
	private String email;
	private Boolean active;
	private Date emailVerficationDate;
	private String emailVerificationCode;
	private Date created;
	private Date updated;
	private OrganizationDTO organizationInfo;
	private String name;
	private String surname;
	private String phone;
	private String language;
	private String country;
	private String zip;
	private String platform;
	private String app_version;
	private Wallet wallet;
	
	
	private boolean merchant;
	
	private List<Role> roles;
	
	public UserDTO() {}
	public UserDTO(User user) {
		
		try {
			BeanUtils.copyProperties(this, user);
			if(user.getOrganization()!=null)
				organizationInfo = new OrganizationDTO(user.getOrganization());

			if(this.getWallet()!=null)
				this.getWallet().setUser(null);
			
			if(user.getRoles()!=null)
			{
				this.merchant = false;
				for (Role r:user.getRoles())
					if("merchant".equalsIgnoreCase(r.getRolename()))
						this.merchant=true;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public OrganizationDTO getOrganizationInfo() {
		return organizationInfo;
	}
	public void setOrganizationInfo(OrganizationDTO organizationInfo) {
		this.organizationInfo = organizationInfo;
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
	public boolean isMerchant() {
		return merchant;
	}
	public void setMerchant(boolean merchant) {
		this.merchant = merchant;
	}

	
	
	  
}

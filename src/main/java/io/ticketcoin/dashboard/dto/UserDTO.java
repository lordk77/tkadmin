package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.User;

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
	
	private List<Role> roles;
	
	public UserDTO() {}
	public UserDTO(User user) {
		
		try {
			BeanUtils.copyProperties(this, user);
			if(user.getOrganization()!=null)
				organizationInfo = new OrganizationDTO(user.getOrganization());
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
	
	
	  
}

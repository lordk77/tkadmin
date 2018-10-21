package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.Organization;
import io.ticketcoin.dashboard.persistence.model.User;

@XmlRootElement
public class OrganizationDTO implements Serializable{
	
	private static final long serialVersionUID = -3211225911792984783L;
	
	
	private Long id;
	private String name;
	private String info;
	private String email;
    private String phone;
	private String vatCode;
	private Date created;
	private Date updated;

	
	public OrganizationDTO() {}
	public OrganizationDTO(Organization organization) {
		
		try {
			BeanUtils.copyProperties(this, organization);
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVatCode() {
		return vatCode;
	}
	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
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
}

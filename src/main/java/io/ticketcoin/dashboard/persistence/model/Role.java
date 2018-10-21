package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TDEF_ROLE")
@XmlRootElement
public class Role  implements Serializable{

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String rolename;
	private String description;
	
	
	
	public Role() 
	{
		super();
	}
	
	public Role(String rolename)
	{
		this.rolename=rolename;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}

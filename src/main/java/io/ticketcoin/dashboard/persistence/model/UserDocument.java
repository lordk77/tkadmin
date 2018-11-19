package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TDEF_USER_DOCUMENTS")
@XmlRootElement
public class UserDocument implements Serializable{

	public enum DocTypes{
		ID_CARD,
		PASSPORT,
		CLUB_CARD,
		OTHER
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	
	@Column(name="CREATED_AT")
	private Date created;
	
	@Column(name="UPDATED_AT")
	private Date updated;

	@Column(name="DOCUMENT_NAME")
	private String name;
	 
	@Column(name="DOCUMENT_TYPE")
	private DocTypes docType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="FRONT_IMAGE_ID")	
	private FileAttachment frontImage;
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="BACK_IMAGE_ID")	
	private FileAttachment backImage;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public DocTypes getDocType() {
		return docType;
	}

	public void setDocType(DocTypes docType) {
		this.docType = docType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public FileAttachment getFrontImage() {
		return frontImage;
	}

	public void setFrontImage(FileAttachment frontImage) {
		this.frontImage = frontImage;
	}

	public FileAttachment getBackImage() {
		return backImage;
	}

	public void setBackImage(FileAttachment backImage) {
		this.backImage = backImage;
	}
	

}

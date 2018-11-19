package io.ticketcoin.dashboard.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.UserDocument;

@XmlRootElement
public class UserDocumentOutput implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3086757156442663030L;
	
	private Long id;
	private String name;
	private String documentType;
	private String front_photo;
	private String back_photo;
	
	
	public UserDocumentOutput() {} 
	public UserDocumentOutput(UserDocument document ) {
	
		this.setId(document.getId());
		this.setName(document.getName());
		this.setDocumentType(document.getDocType()!=null ? document.getDocType().toString() : null);
		if(document.getFrontImage()!=null)
			this.setFront_photo(document.getFrontImage().getAttachmentURL());
		if(document.getBackImage()!=null)
			this.setBack_photo(document.getBackImage().getAttachmentURL());
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFront_photo() {
		return front_photo;
	}
	public void setFront_photo(String front_photo) {
		this.front_photo = front_photo;
	}
	public String getBack_photo() {
		return back_photo;
	}
	public void setBack_photo(String back_photo) {
		this.back_photo = back_photo;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	

}

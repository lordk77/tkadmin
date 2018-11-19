package io.ticketcoin.dashboard.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDocumentInput implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3086757156442663030L;
	
	private Long id;
	private String name;
	private String documentType;
	private byte[] front_photo;
	private byte[] back_photo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getFront_photo() {
		return front_photo;
	}
	public void setFront_photo(byte[] front_photo) {
		this.front_photo = front_photo;
	}
	public byte[] getBack_photo() {
		return back_photo;
	}
	public void setBack_photo(byte[] back_photo) {
		this.back_photo = back_photo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	

}

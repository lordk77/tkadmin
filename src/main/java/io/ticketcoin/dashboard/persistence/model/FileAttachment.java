package io.ticketcoin.dashboard.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.ticketcoin.dashboard.utils.ResourceUtils;

@Entity
@Table(name="FILE_ATTACHMENT")
public class FileAttachment {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String contentType;
	private String fileName;
	private String attachmentUUID;
	
	@Transient
	private byte[] content;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAttachmentUUID() {
		return attachmentUUID;
	}
	public void setAttachmentUUID(String attachmentUUID) {
		this.attachmentUUID = attachmentUUID;
	}
	
	@Transient
	public byte[] getContent() {
		if(this.content==null)
			this.content = new ResourceUtils().getContent(this);
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	
}

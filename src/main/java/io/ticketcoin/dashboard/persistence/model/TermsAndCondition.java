package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TDEF_TERMS_AND_CONDITIONS")
@XmlRootElement
public class TermsAndCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 664141643875515819L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	@Column(name="CONTENT", length = 65535, columnDefinition = "text")
	private String content;
	
	@Column(name="VALID_FROM")
	private Date validFrom;
	
	@Column(name="VALID_TO")
	private Date validTo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
			
	
}

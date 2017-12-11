package io.ticketcoin.dashboard.persistence.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="LEGACY_TICKET_CODE")
@XmlRootElement
public class LegacyTicketCode {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private Date usedOn;

	private String promotionalCode;
	private boolean visible;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getUsedOn() {
		return usedOn;
	}
	public void setUsedOn(Date usedOn) {
		this.usedOn = usedOn;
	}
	public String getPromotionalCode() {
		return promotionalCode;
	}
	public void setPromotionalCode(String promotionalCode) {
		this.promotionalCode = promotionalCode;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}

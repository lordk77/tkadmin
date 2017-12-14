package io.ticketcoin.dashboard.persistence.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(
	name="TICKET", 
	indexes = { @Index(name = "TICKET_UUID_IDX", columnList = "TICKET_UUID") })
@XmlRootElement
public class Ticket 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "TICKET_UUID")
	private String ticketUUID;
	
    
	private Date issuedOn;
	private String issuedTo;
	private String ownedBy;
	
	private BigDecimal price;
	private String currency;
	
	
	private Date spentOn;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EVENT_ID")
	private Event event;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CATEGORY_ID")
	private TicketCategory category;
	
	
	private Date validFrom;
	private Date validTo;

	private boolean resellable;
	private BigDecimal resellPriceCap;
	
	
	  private Date created;
	  private Date updated;
	  @PrePersist
	  protected void onCreate() {
	    created = new Date();
	  }

	  @PrePersist
	  @PreUpdate
	  protected void onUpdate() {
	    updated = new Date();
	  }
	  
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTicketUUID() {
		return ticketUUID;
	}

	public void setTicketUUID(String ticketUUID) {
		this.ticketUUID = ticketUUID;
	}

	public Date getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(Date issuedOn) {
		this.issuedOn = issuedOn;
	}

	public String getIssuedTo() {
		return issuedTo;
	}

	public void setIssuedTo(String issuedTo) {
		this.issuedTo = issuedTo;
	}

	public String getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getSpentOn() {
		return spentOn;
	}

	public void setSpentOn(Date spentOn) {
		this.spentOn = spentOn;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public TicketCategory getCategory() {
		return category;
	}

	public void setCategory(TicketCategory category) {
		this.category = category;
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

	public boolean isResellable() {
		return resellable;
	}

	public void setResellable(boolean resellable) {
		this.resellable = resellable;
	}

	public BigDecimal getResellPriceCap() {
		return resellPriceCap;
	}

	public void setResellPriceCap(BigDecimal resellPriceCap) {
		this.resellPriceCap = resellPriceCap;
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

package io.ticketcoin.dashboard.persistence.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;



@Entity
@Table(name="TICKET_CATEGORY")
@XmlRootElement
public class TicketCategory {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String ticketCategoryUUID;
	private Integer categoryOrder;
	private String description;
	private BigDecimal streetPrice;
	private BigDecimal netPrice;
	private String currency;
	private String title;
	private Integer ticketSupply = 0;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="TICKET_CATEGORY_ID")
	private List<TicketCategoryDetail> categoryDetails;
	
	@Enumerated(EnumType.STRING)
	private SupplyType supplyType;
	
	public enum SupplyType {
	    LEGACY_CODE, NORMAL;
	}
	
	public TicketCategory(){}
	
	public TicketCategory(boolean generateUUID)
	{
		if(generateUUID)
			this.ticketCategoryUUID=UUID.randomUUID().toString();
	}
	
	@OneToMany(cascade=CascadeType.REMOVE,fetch=FetchType.LAZY)
	@JoinColumn(name="TICKET_CATEGORY_ID")
	private List<LegacyTicketCode> legacyTicketCodes;
	
	
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

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<LegacyTicketCode> getLegacyTicketCodes() {
		return legacyTicketCodes;
	}
	public void setLegacyTicketCodes(List<LegacyTicketCode> legacyTicketCodes) {
		this.legacyTicketCodes = legacyTicketCodes;
	}
	public Integer getCategoryOrder() {
		return categoryOrder;
	}
	public void setCategoryOrder(Integer categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}
	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}
	public BigDecimal getStreetPrice() {
		return streetPrice;
	}
	public void setStreetPrice(BigDecimal streetPrice) {
		this.streetPrice = streetPrice;
	}
	public BigDecimal getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}
	public String getTicketCategoryUUID() {
		return ticketCategoryUUID;
	}
	public void setTicketCategoryUUID(String ticketCategoryUUID) {
		this.ticketCategoryUUID = ticketCategoryUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<TicketCategoryDetail> getCategoryDetails() {
		return categoryDetails;
	}

	public void setCategoryDetails(List<TicketCategoryDetail> categoryDetails) {
		this.categoryDetails = categoryDetails;
	}

	public Integer getTicketSupply() {
		return ticketSupply;
	}

	public void setTicketSupply(Integer ticketSupply) {
		this.ticketSupply = ticketSupply;
	}
	
	
	
}

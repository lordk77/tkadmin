package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.TicketCategory;

@XmlRootElement
public class TicketCategoryDTO {

	private String ticketCategoryUUID;
	private String description;
	private BigDecimal price;
	private String currency;
	
	public TicketCategoryDTO(){}
	
	public TicketCategoryDTO(TicketCategory tc)
	{
		this.ticketCategoryUUID=tc.getTicketCategoryUUID();
		this.description=tc.getDescription();
		this.price=tc.getStreetPrice();
		this.currency=tc.getCurrency();
	}
	
	public String getTicketCategoryUUID() {
		return ticketCategoryUUID;
	}
	public void setTicketCategoryUUID(String ticketCategoryUUID) {
		this.ticketCategoryUUID = ticketCategoryUUID;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

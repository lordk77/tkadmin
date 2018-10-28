package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.persistence.service.EventService;

@XmlRootElement
public class TicketCategoryDTO {

	private String ticketCategoryUUID;
	private String description;
	private BigDecimal price;
	private String currency;
	private String title;
	private Integer maxQty;
	
	
	public TicketCategoryDTO(){}
	
	public TicketCategoryDTO(TicketCategory tc, Date date)
	{
		this.ticketCategoryUUID=tc.getTicketCategoryUUID();
		this.description=tc.getDescription();
		this.price=tc.getStreetPrice();
		this.currency=tc.getCurrency();
		this.title = tc.getTitle();
		
		TicketCategoryDetail tcd = new EventService().getTicketCategoryDetail(ticketCategoryUUID, date);
		maxQty =  tcd!=null ? tcd.getAvailableTicket() : 0;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	
}

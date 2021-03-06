package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.time.DateFormatUtils;

import io.ticketcoin.currency.CryptoConverter;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.persistence.service.PurchaseOrderService;

@XmlRootElement
public class TicketCategoryDTO {

	private String ticketCategoryUUID;
	private String description;
	private BigDecimal price;
	private BigDecimal priceETH;
	private String currency;
	private String title;
	private Integer maxQty;
	private String date; 	
	
	public TicketCategoryDTO(){}
	
	public TicketCategoryDTO(TicketCategory tc)
	{
		this.ticketCategoryUUID=tc.getTicketCategoryUUID();
		this.description=tc.getDescription();
		this.currency=tc.getCurrency();
		this.price=tc.getStreetPrice();
		this.priceETH = 
		CryptoConverter.convert(
				this.price.add(PurchaseOrderService.ETH_COMMISSION), currency !=null ? currency : tc.getEvent().getCurrency(), CryptoConverter.CryptoCurrency.ETH);
		this.title = tc.getTitle();
		
	}
	
	
	public TicketCategoryDTO(TicketCategory tc, Date date)
	{
		this(tc);
		
		TicketCategoryDetail tcd = new EventService().getTicketCategoryDetail(ticketCategoryUUID, date);
		if(tcd!=null)
		{
			maxQty = tcd.getAvailableTicket();
			this.date = tcd.getStartingDate()!=null ? DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(tcd.getStartingDate()) : null;
		}
		else
			maxQty = 0;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getPriceETH() {
		return priceETH;
	}

	public void setPriceETH(BigDecimal priceETH) {
		this.priceETH = priceETH;
	}

	
}

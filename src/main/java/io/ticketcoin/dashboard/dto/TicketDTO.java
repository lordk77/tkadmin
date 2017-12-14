package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketDTO {
	
	
		//IDENTIFIERS
		private String eventUUID;
		private String ticketCategoryUUID;
		private String ticketUUID;
		
		//ISSUER_DATA
		private String issuedBy;
		private String owner;
		private String ticketSign;
		

		//Detail
		private Date dateFrom;
		private Date dateTo;
		private boolean open;
		private boolean resellable;
		
		
		
		//TICKET_DETAIL EXTRA
		private BigDecimal price;
		private BigDecimal maxPrice;
		public String getEventUUID() {
			return eventUUID;
		}
		public void setEventUUID(String eventUUID) {
			this.eventUUID = eventUUID;
		}
		public String getTicketUUID() {
			return ticketUUID;
		}
		public void setTicketUUID(String ticketUUID) {
			this.ticketUUID = ticketUUID;
		}
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		public boolean isResellable() {
			return resellable;
		}
		public void setResellable(boolean resellable) {
			this.resellable = resellable;
		}
		public String getIssuedBy() {
			return issuedBy;
		}
		public void setIssuedBy(String issuedBy) {
			this.issuedBy = issuedBy;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getTicketSign() {
			return ticketSign;
		}
		public void setTicketSign(String ticketSign) {
			this.ticketSign = ticketSign;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public BigDecimal getMaxPrice() {
			return maxPrice;
		}
		public void setMaxPrice(BigDecimal maxPrice) {
			this.maxPrice = maxPrice;
		}
		public String getTicketCategoryUUID() {
			return ticketCategoryUUID;
		}
		public void setTicketCategoryUUID(String ticketCategoryUUID) {
			this.ticketCategoryUUID = ticketCategoryUUID;
		}
		public Date getDateFrom() {
			return dateFrom;
		}
		public void setDateFrom(Date dateFrom) {
			this.dateFrom = dateFrom;
		}
		public Date getDateTo() {
			return dateTo;
		}
		public void setDateTo(Date dateTo) {
			this.dateTo = dateTo;
		}

		
		
		
		
}

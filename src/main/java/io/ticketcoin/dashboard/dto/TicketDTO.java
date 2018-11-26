package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.persistence.model.User;

@XmlRootElement
public class TicketDTO implements Serializable{
	
	
		//IDENTIFIERS
		private Long id;	
		private String ticketUUID;
		
		//Detail
		private Date enrollTime;
		private Date validFrom;
		private Date validTo;
		private Date spentOn;
		private Date canceledOn;
		private Integer allowedTransfers;
		private Integer ticketState;
		private Integer transferRule;
		private Integer allowedEntrances;
		private Long tokenId;

		private TicketCategoryDTO ticketCategory;
		private EventDTO eventDetail;
		
		public TicketDTO() {}
		
		public TicketDTO(Ticket ticket) 
		{
			try {
				BeanUtils.copyProperties(this, ticket);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			this.setTicketCategory(new TicketCategoryDTO(ticket.getCategory()));
			this.setEventDetail(new EventDTO(ticket.getCategory().getEvent()));
			
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
		public Date getEnrollTime() {
			return enrollTime;
		}
		public void setEnrollTime(Date enrollTime) {
			this.enrollTime = enrollTime;
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
		public Date getSpentOn() {
			return spentOn;
		}
		public void setSpentOn(Date spentOn) {
			this.spentOn = spentOn;
		}
		public Date getCanceledOn() {
			return canceledOn;
		}
		public void setCanceledOn(Date canceledOn) {
			this.canceledOn = canceledOn;
		}
		public Integer getAllowedTransfers() {
			return allowedTransfers;
		}
		public void setAllowedTransfers(Integer allowedTransfers) {
			this.allowedTransfers = allowedTransfers;
		}
		public Integer getTicketState() {
			return ticketState;
		}
		public void setTicketState(Integer ticketState) {
			this.ticketState = ticketState;
		}
		public Integer getTransferRule() {
			return transferRule;
		}
		public void setTransferRule(Integer transferRule) {
			this.transferRule = transferRule;
		}
		public Integer getAllowedEntrances() {
			return allowedEntrances;
		}
		public void setAllowedEntrances(Integer allowedEntrances) {
			this.allowedEntrances = allowedEntrances;
		}
		public EventDTO getEventDetail() {
			return eventDetail;
		}
		public void setEventDetail(EventDTO eventDetail) {
			this.eventDetail = eventDetail;
		}

		public TicketCategoryDTO getTicketCategory() {
			return ticketCategory;
		}

		public void setTicketCategory(TicketCategoryDTO ticketCategory) {
			this.ticketCategory = ticketCategory;
		}

		public Long getTokenId() {
			return tokenId;
		}

		public void setTokenId(Long tokenId) {
			this.tokenId = tokenId;
		}
		
		
		
}

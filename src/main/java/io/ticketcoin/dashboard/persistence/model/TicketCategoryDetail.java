package io.ticketcoin.dashboard.persistence.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.time.DateUtils;



@Entity
@Table(name="TICKET_CATEGORY_DETAIL")
@XmlRootElement
public class TicketCategoryDetail {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="STARTING_DATE")
	private Date startingDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="ENDING_DATE")
	private Date endingDate;	
	
	@Column(name="AVAILABLE_TICKETS")
	private Integer availableTicket = 0;
	
	@Column(name="SOLD_TICKETS")
	private Integer soldTicket = 0;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TICKET_CATEGORY_ID")
	private TicketCategory ticketCategory;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAvailableTicket() {
		return availableTicket;
	}

	public void setAvailableTicket(Integer availableTicket) {
		this.availableTicket = availableTicket;
	}

	public Integer getSoldTicket() {
		return soldTicket;
	}

	public void setSoldTicket(Integer soldTicket) {
		this.soldTicket = soldTicket;
	}

	public TicketCategory getTicketCategory() {
		return ticketCategory;
	}

	public void setTicketCategory(TicketCategory ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		
		this.startingDate = startingDate!=null ? DateUtils.truncate(startingDate, Calendar.DAY_OF_MONTH) : startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate!=null ? DateUtils.truncate(endingDate, Calendar.DAY_OF_MONTH) : endingDate;
	}


	
	
	
	
	
}

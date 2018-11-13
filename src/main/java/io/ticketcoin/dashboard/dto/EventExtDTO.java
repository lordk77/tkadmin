package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;

@XmlRootElement
public class EventExtDTO extends EventDTO 
{

	private String country;
	private String city;
	private String address;
	private BigDecimal coordX;
	private BigDecimal coordY;
	private String date; 
	
	private List<TicketCategoryDTO> ticketCategories;

	private List<String> eventCategories;
	
	public EventExtDTO(){}
	
	public EventExtDTO(Event event, Date date)
	{
		super(event);
		
		if(date==null)
		{
			if(Event.TYPE_SINGLE_DATE.equals(event.getEventType()))
				date = event.getDateFrom();
			else if (Event.TYPE_PERIOD.equals(event.getEventType()))
			{
				date = new EventService().getFirstAvailableDate(event.getEventUUID());
				if(date == null)
					date =  event.getDateFrom()!=null && event.getDateFrom().after(new Date()) ? event.getDateFrom() : new Date();
			}
		}
		
		if(date!=null)
			this.setDate(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(date));
		
		
		if(event.getLocation()!=null && event.getLocation().getAddress()!=null)
		{
			this.country = event.getLocation().getAddress().getCountry();
			this.city = event.getLocation().getAddress().getCity();
			this.address = event.getLocation().getAddress().getAddress();
			try {//resolve coordinates
				this.coordX = StringUtils.isNoneBlank(event.getLocation().getAddress().getCoordX()) ? new BigDecimal(event.getLocation().getAddress().getCoordX()) : null;
				this.coordY = StringUtils.isNoneBlank(event.getLocation().getAddress().getCoordY()) ? new BigDecimal(event.getLocation().getAddress().getCoordY()) : null;
			}
			catch (Exception e) {
			}
		}

		
		this.ticketCategories=new ArrayList<TicketCategoryDTO>();
		if(event.getCategories()!=null && event.getCategories().size()>0)
		{
			for(int i =0; i < event.getCategories().size();i++)
				this.ticketCategories.add(new TicketCategoryDTO(event.getCategories().get(i), date));
		}
		
		
		
		this.eventCategories =new ArrayList<String>();
		if(event.getEventCategories()!=null && event.getEventCategories().size()>0)
		{
			for(int i =0; i < event.getEventCategories().size();i++)
				this.eventCategories.add(event.getEventCategories().get(i).toString());
		}
		
	}
	
	public List<TicketCategoryDTO> getTicketCategories() {
		return ticketCategories;
	}

	public void setTicketCategories(List<TicketCategoryDTO> ticketCategories) {
		this.ticketCategories = ticketCategories;
	}

	public List<String> getEventCategories() {
		return eventCategories;
	}

	public void setEventCategories(List<String> eventCategories) {
		this.eventCategories = eventCategories;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public void setCoordX(BigDecimal coordX) {
		this.coordX = coordX;
	}

	public void setCoordY(BigDecimal coordY) {
		this.coordY = coordY;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getCoordX() {
		return coordX;
	}

	public BigDecimal getCoordY() {
		return coordY;
	}
}

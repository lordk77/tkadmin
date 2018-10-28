package io.ticketcoin.dashboard.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventExtDTO extends EventDTO 
{

	private String country;
	private String city;
	private String address;
	private String coordX;
	private String coordY;
	
	private List<TicketCategoryDTO> ticketCategories;

	private List<String> eventCategories;
	
	public EventExtDTO(){}
	
	public EventExtDTO(Event event, Date date)
	{
		super(event);
		
		if(event.getLocation()!=null && event.getLocation().getAddress()!=null)
		{
			this.country = event.getLocation().getAddress().getCountry();
			this.city = event.getLocation().getAddress().getCity();
			this.address = event.getLocation().getAddress().getAddress();
			this.coordX = event.getLocation().getAddress().getCoordX();
			this.coordY = event.getLocation().getAddress().getCoordY();
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

	public String getCoordX() {
		return coordX;
	}

	public void setCoordX(String coordX) {
		this.coordX = coordX;
	}

	public String getCoordY() {
		return coordY;
	}

	public void setCoordY(String coordY) {
		this.coordY = coordY;
	}
}

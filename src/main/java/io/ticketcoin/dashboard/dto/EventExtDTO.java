package io.ticketcoin.dashboard.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.time.DateFormatUtils;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;

@XmlRootElement
public class EventExtDTO extends EventDTO 
{


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
		
		
//		if(event.getLocation()!=null && event.getLocation().getAddress()!=null)
//		{
//			this.country = event.getLocation().getAddress().getCountry();
//			this.city = event.getLocation().getAddress().getCity();
//			this.address = event.getLocation().getAddress().getAddress();
//			try {//resolve coordinates
//				this.coordX = StringUtils.isNoneBlank(event.getLocation().getAddress().getCoordX()) ? new BigDecimal(event.getLocation().getAddress().getCoordX()) : null;
//				this.coordY = StringUtils.isNoneBlank(event.getLocation().getAddress().getCoordY()) ? new BigDecimal(event.getLocation().getAddress().getCoordY()) : null;
//			}
//			catch (Exception e) {
//			}
//		}

		
		this.ticketCategories=new ArrayList<TicketCategoryDTO>();
		if(event.getCategories()!=null && event.getCategories().size()>0)
		{
			for(int i =0; i < event.getCategories().size();i++)
			{
				TicketCategoryDTO tcDTO = new TicketCategoryDTO(event.getCategories().get(i), date);
				//Inserts only ticket category available
				if((Event.TYPE_OPEN.equals(event.getEventType())) || ( tcDTO!=null && tcDTO.getDate()!=null))
					this.ticketCategories.add(tcDTO);
			}
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

	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

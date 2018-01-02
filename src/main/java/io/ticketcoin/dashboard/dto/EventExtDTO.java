package io.ticketcoin.dashboard.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventExtDTO extends EventDTO 
{

	private List<TicketCategoryDTO> ticketCategories;

	private List<String> eventCategories;
	
	public EventExtDTO(){}
	
	public EventExtDTO(Event event)
	{
		super(event);

		this.ticketCategories=new ArrayList<TicketCategoryDTO>();
		if(event.getCategories()!=null && event.getCategories().size()>0)
		{
			for(int i =0; i < event.getCategories().size();i++)
				this.ticketCategories.add(new TicketCategoryDTO(event.getCategories().get(i)));
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
}

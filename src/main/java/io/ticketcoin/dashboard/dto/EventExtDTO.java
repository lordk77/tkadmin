package io.ticketcoin.dashboard.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventExtDTO extends EventDTO 
{

	private List<TicketCategoryDTO> ticketCategories;

	
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
		
		
	}
	
	public List<TicketCategoryDTO> getTicketCategories() {
		return ticketCategories;
	}

	public void setTicketCategories(List<TicketCategoryDTO> ticketCategories) {
		this.ticketCategories = ticketCategories;
	}
}

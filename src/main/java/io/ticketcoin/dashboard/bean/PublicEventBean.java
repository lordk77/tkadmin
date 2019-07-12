package io.ticketcoin.dashboard.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;


@ManagedBean
@RequestScoped
public class PublicEventBean 
{
	
	private Event event;
	private String eventUUID;
	
	public Event getEvent() 
	{
		if(event==null)
		{
			try
			{
				EventFilter filter = new EventFilter();
				filter.setEventUUID(eventUUID);
				event=new EventService().searchEvents(filter).getResults().get(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public String getEventUUID() {
		return eventUUID;
	}
	public void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}
	

	
}

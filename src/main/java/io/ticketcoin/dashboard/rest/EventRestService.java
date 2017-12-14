package io.ticketcoin.dashboard.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;

@Path("/event")
public class EventRestService 
{

	@POST
	@Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	  public List<EventDTO> search(EventFilter filter) 
	  {
		List<Event> es = new EventService().searchEvents(filter);
		List<EventDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventDTO(e));
    	return events;
    	  
	  }
	  
	
}
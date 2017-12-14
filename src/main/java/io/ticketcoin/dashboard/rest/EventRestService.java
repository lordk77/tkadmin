package io.ticketcoin.dashboard.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.dto.EventExtDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;

@Path("/event")
public class EventRestService 
{

	private static final int MAX_RESULTS =50;
	
	@GET
	@Path("/list/{timeStamp}")
    @Produces(MediaType.APPLICATION_JSON)
	  public List<EventExtDTO> search(@PathParam("timeStamp") Long timeStamp) 
	  {
		EventFilter filter = new EventFilter();
		if(timeStamp!=null && timeStamp>0)
			filter.setUpdatedSince(new Date(timeStamp));
		filter.setMaxResult(MAX_RESULTS);
		List<Event> es = new EventService().searchEvents(filter);
		List<EventExtDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventExtDTO(e));
		return events;
	  }
	
	
	
	
	@POST
	@Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
	  public List<EventDTO> search(EventFilter filter) 
	  {
		filter.setMaxResult(MAX_RESULTS);
		List<Event> es = new EventService().searchEvents(filter);
		List<EventDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventDTO(e));
    	return events;
//		return Response.ok(new Gson().toJson(es)).type(MediaType.APPLICATION_JSON).build();
    	  
	  }
	  
	
}
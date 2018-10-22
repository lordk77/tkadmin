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

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.EventCategoryDTO;
import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.dto.EventExtDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.rest.response.JSONResponseWrapper;

@Path("/event")
public class EventRestService 
{

	private static final int MAX_RESULTS =50;
	
	@GET
	@Path("/list/{timeStamp}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response search(@PathParam("timeStamp") Long timeStamp) 
	  {
		EventFilter filter = new EventFilter();
		if(timeStamp!=null && timeStamp>0)
			filter.setUpdatedSince(new Date(timeStamp));
		filter.setMaxResult(MAX_RESULTS);
		List<Event> es = new EventService().searchEvents(filter);
		List<EventExtDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventExtDTO(e));

		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(events)))
				.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET")
					.type(MediaType.APPLICATION_JSON)
					.build();
	  }
	
	
	
	@GET
	@Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response categories() 
	  {
		
		List<EventCategoryDTO> categories =  new EventService().searchCategories();
		
		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(categories)))
				.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET")
					.type(MediaType.APPLICATION_JSON)
					.build();
	  }
	
	
	
	@GET
	@Path("/category/{categoryCode}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response category(@PathParam("categoryCode") String categoryCode)
	  {
		
		EventFilter filter = new EventFilter();
		filter.setMaxResult(MAX_RESULTS);
		filter.setCategory(categoryCode);
		List<Event> es = new EventService().searchEvents(filter);
		List<EventDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventDTO(e));

		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(events)))
				 .header("Access-Control-Allow-Origin", "*")
				 .header("Access-Control-Allow-Methods", "POST")
					.type(MediaType.APPLICATION_JSON)
				 .build();
		 
	  }
	
	
	
	
	@POST
	@Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
	  public Response search(EventFilter filter) 
	  {
		filter.setMaxResult(MAX_RESULTS);
		List<Event> es = new EventService().searchEvents(filter);
		List<EventDTO> events = new ArrayList<>();
		for (Event e:es)
			events.add(new EventDTO(e));

		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(events)))
				 .header("Access-Control-Allow-Origin", "*")
				 .header("Access-Control-Allow-Methods", "POST")
					.type(MediaType.APPLICATION_JSON)
				 .build();
		 
	  }
	  
	
	@GET
	@Path("/detail/{eventUUID}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response detail(@PathParam("eventUUID") String eventUUID) 
	  {
		EventFilter filter = new EventFilter();
		filter.setMaxResult(1);
		
		filter.setEventUUID(eventUUID);
		
		List<Event> es = new EventService().searchEvents(filter);
		EventExtDTO eventExtDTO = null;
		
		if (es!=null && !es.isEmpty())
			eventExtDTO = new EventExtDTO(es.get(0));

		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(eventExtDTO)))
				.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET")
					.type(MediaType.APPLICATION_JSON)
					.build();
	  }
	
	
	
	
}
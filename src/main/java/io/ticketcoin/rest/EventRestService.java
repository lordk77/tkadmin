package io.ticketcoin.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.EventCategoryDTO;
import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.dto.EventExtDTO;
import io.ticketcoin.dashboard.dto.EventSearchResultDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventSearchResult;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/event")
public class EventRestService 
{

	private static final int MAX_RESULTS =50;
	
	@GET
	@Path("/list/{timeStamp}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response search(@PathParam("timeStamp") Long timeStamp) 
	  {
		try 
		{
			EventFilter filter = new EventFilter();
			if(timeStamp!=null && timeStamp>0)
				filter.setUpdatedSince(new Date(timeStamp));
			filter.setMaxResult(MAX_RESULTS);
			List<Event> es = new EventService().searchEvents(filter).getResults();
			List<EventExtDTO> events = new ArrayList<>();
			for (Event e:es)
				events.add(new EventExtDTO(e, null));
				
			
	
			 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(events)))
					.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET")
						.type(MediaType.APPLICATION_JSON)
						.build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
			}
	  }
	
	
	
	@GET
	@Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response categories() 
	  {
		try {
		
		List<EventCategoryDTO> categories =  new EventService().searchCategories();
		
		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(categories)))
				.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET")
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
			}
	  }
	
	
	
	@GET
	@Path("/category/{categoryCode}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response category(@PathParam("categoryCode") String categoryCode)
	  {
		try
		{
			EventFilter filter = new EventFilter();
			filter.setMaxResult(MAX_RESULTS);
			filter.setCategory(categoryCode);
			EventSearchResult searchResult = new EventService().searchEvents(filter);
			List<EventDTO> events = new ArrayList<>();
			for (Event e:searchResult.getResults())
				events.add(new EventDTO(e));
			
			
			EventSearchResultDTO resDTO = new EventSearchResultDTO();
			resDTO.setResults(events);
			resDTO.setRowCount(searchResult.getRowCount());
			
			
	
			 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(resDTO)))
					 .header("Access-Control-Allow-Origin", "*")
					 .header("Access-Control-Allow-Methods", "POST")
						.type(MediaType.APPLICATION_JSON)
					 .build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
			}
		 
	  }
	
	
	
	
	@POST
	@Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
	  public Response search(EventFilter filter) 
	  {
		try 
		{
			filter.setMaxResult(MAX_RESULTS);
			EventSearchResult searchResult = new EventService().searchEvents(filter);
			List<EventDTO> events = new ArrayList<>();
			for (Event e:searchResult.getResults())
				events.add(new EventDTO(e));
	
			EventSearchResultDTO resDTO = new EventSearchResultDTO();
			resDTO.setResults(events);
			resDTO.setRowCount(searchResult.getRowCount());
			
			 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(resDTO)))
					 .header("Access-Control-Allow-Origin", "*")
					 .header("Access-Control-Allow-Methods", "POST")
						.type(MediaType.APPLICATION_JSON)
					 .build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
			}
			 
	  }
	  
	
	@GET
	@Path("/detail/{eventUUID}")
    @Produces(MediaType.APPLICATION_JSON)
	  public Response detail(@PathParam("eventUUID") String eventUUID, @QueryParam("date") String date) 
	  {
		try
		{
		EventFilter filter = new EventFilter();
		filter.setMaxResult(1);
		
		filter.setEventUUID(eventUUID);
		
		List<Event> es = new EventService().searchEvents(filter).getResults();
		EventExtDTO eventExtDTO = null;
		
		if (es!=null && !es.isEmpty())
			eventExtDTO = new EventExtDTO(es.get(0), date !=null ? DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(date):null);

		 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(eventExtDTO)))
				.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET")
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
			}
	  }
	
	
	
	
}
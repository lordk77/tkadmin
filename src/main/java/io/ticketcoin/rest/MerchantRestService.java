package io.ticketcoin.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.model.EphemeralKey;

import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.dto.EventExtDTO;
import io.ticketcoin.dashboard.dto.EventSearchResultDTO;
import io.ticketcoin.dashboard.dto.PurchaseOrderDTO;
import io.ticketcoin.dashboard.dto.TicketDTO;
import io.ticketcoin.dashboard.dto.TicketData;
import io.ticketcoin.dashboard.dto.UserDTO;
import io.ticketcoin.dashboard.dto.UserProfileDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.EventSearchResult;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.persistence.service.PurchaseOrderService;
import io.ticketcoin.dashboard.persistence.service.TicketService;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.rest.integration.stripe.StripeService;
import io.ticketcoin.rest.integration.stripe.dto.EphemeralKeysRequest;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/merchant")
public class MerchantRestService {
	
		@Context
	    private MessageContext mc;
	
	
		@GET
		@Path("/events")
		@Produces(MediaType.APPLICATION_JSON)
		public Response events() {
			
			try
			{
				String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
				User user = new UserService().getUser(userName);
				
				if(!user.isMerchant())
					return Response.status(401).build();
				else if(user.getOrganization()==null)
					throw new Exception("error.no.organization.specified");
				
				else
				{
					EventFilter filter = new EventFilter();
					filter.setOrganizationId(user.getOrganization().getId());
					
					EventSearchResultDTO searchResult = new EventService().searchEventsDTO(filter);
					
					 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(searchResult)))
								.header("Access-Control-Allow-Origin", "*")
									.header("Access-Control-Allow-Methods", "GET")
									.type(MediaType.APPLICATION_JSON)
									.build();
				}
				 
				 
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
		}
		
		

		@POST
		@Path("/check")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response check(TicketData ticketData) 
		  {
			try
			{
				String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();

				User user = new UserService().getUser(userName);
				
				if(!user.isMerchant())
					return Response.status(401).build();
				else if(user.getOrganization()==null)
					throw new Exception("error.no.organization.specified");
				
				else
				{
				
					TicketFilter filter = new TicketFilter();
					filter.setIncludeExpired(true);
					filter.setTicketUUID(ticketData.getTicketUUID());
					filter.setOrganizationId(user.getOrganization().getId());
					
					List<TicketDTO> ticketDTOs = new TicketService().searchTicketsDTO(filter);
					
					if(ticketDTOs.size()==0)
						throw new Exception("error.ticket.not.found");
					else if(ticketDTOs.size()>1)
						throw new Exception("error.ticket.uuid.ambiguous");
					else if(ticketDTOs.get(0).getTicketState()==Ticket.STATE_SPENT)
						throw new Exception("error.spent.ticket");
					else if(ticketDTOs.get(0).getTicketState()==Ticket.STATE_INVALID)
						throw new Exception("error.invalid.ticket");	
					
		
					
					 return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getSuccessWrapper(ticketDTOs)))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "GET")
								.type(MediaType.APPLICATION_JSON)
								.build();
				}
			  } catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}				 
		  }
		

		@POST
		@Path("/consume")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response consume(TicketData ticketData) 
		  {
			try
			{
				String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();

				User user = new UserService().getUser(userName);
				
				if(!user.isMerchant())
					return Response.status(401).build();
				else if(user.getOrganization()==null)
					throw new Exception("error.no.organization.specified");
				
				else
				{
				
					
					TicketDTO ticket =  new TicketService().consumeTicket(ticketData.getTicketUUID(), user.getOrganization().getId());
					
					 return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getSuccessWrapper(ticket)))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "GET")
								.type(MediaType.APPLICATION_JSON)
								.build();
				}
			  } catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}				 
		  }
		
		
		
}

package io.ticketcoin.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

import com.google.gson.GsonBuilder;

import io.ticketcoin.dashboard.dto.TicketDTO;
import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.service.TicketService;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/token")
public class TokenRestService {
	
		@Context
	    private MessageContext mc;
	

		@GET
		@Path("/{tokenId}")
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response tickets(@PathParam("tokenId") Long tokenId) 
		  {
			try
			{
				String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
				
				if(tokenId==null)
					throw new Exception("error.no.token.specified");
				
				TicketFilter filter = new TicketFilter();
				filter.setTokenId(tokenId);
				filter.setIncludeExpired(true);
				
				List<TicketDTO> ticketDTOs = new TicketService().searchTicketsDTO(filter);

				if(ticketDTOs==null || ticketDTOs.size()==0)
					throw new Exception("error.no.token.found");


				 return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getSuccessWrapper(ticketDTOs)))
						.header("Access-Control-Allow-Origin", "*")
							.header("Access-Control-Allow-Methods", "GET")
							.type(MediaType.APPLICATION_JSON)
							.build();
			  } catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}				 
		  }
	
		
				

}

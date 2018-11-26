package io.ticketcoin.rest;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.ticketcoin.dashboard.dto.CardData;
import io.ticketcoin.dashboard.dto.EventSearchResultDTO;
import io.ticketcoin.dashboard.dto.TicketDTO;
import io.ticketcoin.dashboard.dto.TicketData;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.persistence.service.TicketService;
import io.ticketcoin.dashboard.persistence.service.UserService;
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
					
		
					
					 return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getSuccessWrapper(ticketDTOs.get(0))))
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

		
		@POST
		@Path("/checkCard")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response checkCard(CardData cardData) 
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
				
					TicketDTO dto =  getCandidateTicket(cardData.getCardID(), user);
		
					
					 return Response.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(JSONResponseWrapper.getSuccessWrapper(dto)))
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
		@Path("/consumeCard")
		@Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response consume(CardData cardData) 
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
				
					TicketDTO dto =  getCandidateTicket(cardData.getCardID(), user);
					TicketDTO ticket =  new TicketService().consumeTicket(dto.getTicketUUID(), user.getOrganization().getId());
					
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
		
		
		private TicketDTO getCandidateTicket(String cardID, User user) throws Exception
		{
			TicketFilter filter = new TicketFilter();
			filter.setIncludeExpired(false);
			filter.setOrganizationId(user.getOrganization().getId());
			filter.setCardID(cardID);
			filter.setDate(new Date());
			
			List<TicketDTO> ticketDTOs = new TicketService().searchTicketsDTO(filter);
			
			if(ticketDTOs.size()==0)
				throw new Exception("error.ticket.not.found");
			else if(ticketDTOs.size()>1)
				throw new Exception("error.ticket.uuid.ambiguous");
			else if(ticketDTOs.get(0).getTicketState()==Ticket.STATE_SPENT)
				throw new Exception("error.spent.ticket");
			else if(ticketDTOs.get(0).getTicketState()==Ticket.STATE_INVALID)
				throw new Exception("error.invalid.ticket");	
			else
				return ticketDTOs.get(0);			
		}
		
		
		
		private boolean TicketData(TicketData td) throws SignatureException
		{
			BigInteger  signedMessageToKey = Sign.signedMessageToKey((td.getEventUUID() + td.getAddress() + td.getTicketUUID()+td.getUtc()).getBytes(), new Sign.SignatureData(
					hexStringToByteArray(td.getV())[0], 
					hexStringToByteArray(td.getR()), 
					hexStringToByteArray(td.getS())));
			
			System.out.println(signedMessageToKey.toString(16));
			return ("0x"+signedMessageToKey.toString(16)).equals(td.getAddress());

					
			
		}
		
		
		public static byte[] hexStringToByteArray(String s) {
		    int len = s.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
		                             + Character.digit(s.charAt(i+1), 16));
		    }
		    return data;
		}
		
		
		
		public static void main(String[] args)
		{
			Credentials.create(
					"", 
					"");
			/*
					val address = credentials.address
					val ticketUUID = item.ticketUUID
					val eventUUID = item.eventDetail.eventUUID!!
					val utc =  Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()
					val data = (eventUUID+address+ticketUUID+utc.toString()).toByteArray()
					val signature = Sign.signMessage(data, credentials.ecKeyPair)
					val r = Numeric.toHexString(signature.r)
					val s = Numeric.toHexString(signature.s)
					val v =  Numeric.toHexString(byteArrayOf(signature.v))*/
					
		}
		
}

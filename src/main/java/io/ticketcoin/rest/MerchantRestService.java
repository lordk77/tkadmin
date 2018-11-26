package io.ticketcoin.rest;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
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
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

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
				else if (!validateTicketData(ticketData))
					throw new Exception("error.bad.signature");				
				
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
				else if (!validateTicketData(ticketData))
					throw new Exception("error.bad.signature");
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
		
		
		
		private static boolean validateTicketData(TicketData td) throws SignatureException
		{

			String message = td.getEventUUID() + td.getAddress() + td.getTicketUUID()+td.getUtc();
			

			byte[] msgHash = Hash.sha3((message).getBytes());

           
	        SignatureData sd = new SignatureData(
	        		Numeric.hexStringToByteArray(td.getV())[0], 
					Numeric.hexStringToByteArray(td.getR()), 
					Numeric.hexStringToByteArray(td.getS()));

	        String addressRecovered = null;
	        boolean match = false;
	        
	        // Iterate for each possible key to recover
	        for (int i = 0; i < 4; i++) {
	            BigInteger publicKey = Sign.recoverFromSignature(
	                    (byte) i, 
	                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())), 
	                    msgHash);
	               
	            if (publicKey != null) {
	                addressRecovered = "0x" + Keys.getAddress(publicKey); 
	                
	                if (addressRecovered.equals(td.getAddress())) {
	                    match = true;
	                    break;
	                }
	            }
	        }
	        
			
			return match;
					
		}
	
		
		
		public static void main(String[] args)
		{
			TicketData _td = new TicketData();
			_td.setAddress("0x10b3371666621a6035f736fb42dd6822c1a0e09d");
			_td.setEventUUID("c99b59e9-097e-4904-a863-9a2427677218");
			_td.setR("0x37546c4140a1bc349fa80a0faf83e7aee5d07c25ee80da84abc01118a5b0c000");
			_td.setS("0x30475750d8c5179fa47d381f53e939f160ddc6f461bdc8c04fb81f41bbce2ce9");
			_td.setV("0x1C");
			_td.setTicketUUID("6");
			_td.setUtc(1543239273506l);
			
			
			try {
				System.out.println(validateTicketData(_td));
			} catch (SignatureException e) {
				e.printStackTrace();
			}
		}

			
		
}

package io.ticketcoin.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.PurchaseOrderDTO;
import io.ticketcoin.dashboard.dto.UserDTO;
import io.ticketcoin.dashboard.dto.UserProfileDTO;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.PurchaseOrderService;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.rest.integration.stripe.StripeService;
import io.ticketcoin.rest.integration.stripe.dto.EphemeralKeysRequest;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/user")
public class UserRestService {
	
		@Context
	    private MessageContext mc;
	
	
		@GET
		@Path("/me")
		@Produces(MediaType.APPLICATION_JSON)
		public Response me() {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			User user = new UserService().getUser(userName);
			UserDTO uDto = new UserDTO(user);
			
			 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(uDto)))
						.header("Access-Control-Allow-Origin", "*")
							.header("Access-Control-Allow-Methods", "GET")
							.type(MediaType.APPLICATION_JSON)
							.build();
		}
		
		
		
		@POST
		@Path("/me/checkout")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response checkout(PurchaseOrderDTO order) {
			
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			PurchaseOrderService pos = new PurchaseOrderService();
				try {

					String stripeEphemeralKeys = null;
					if(PurchaseOrder.PAYMENT_TYPE_STRIPE.equals(order.getPaymentType()))
						stripeEphemeralKeys= new StripeService().createEphemeralKeys(new EphemeralKeysRequest(order.getStripe_api_version(), null), userName).getId();
						
						
					PurchaseOrder createdOrder = pos.placeOrder(order, userName);
					
					order = new PurchaseOrderDTO(createdOrder);
					order.setStripeEphemeralKeys(stripeEphemeralKeys);

					
					return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new PurchaseOrderDTO(createdOrder))))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "POST")
								.type(MediaType.APPLICATION_JSON)
								.build();
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}
			

		}
		
		
		@GET
		@Path("/me/order/{orderUUID}")
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response detail(@PathParam("orderUUID") String orderUUID) 
		  {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			PurchaseOrder order = new PurchaseOrderService().getOrder(orderUUID);

			if(userName.equals(order.getUser().getUsername()))
			{
			 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new PurchaseOrderDTO(order))))
					.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET")
						.type(MediaType.APPLICATION_JSON)
						.build();
			}
			else
			{
				return Response.status(401).build();
			}
		  }
		
		
		@POST
		@Path("/me/update_profile")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateProfile(UserProfileDTO userData) 
		{
				try 
				{
					
					UserService userService = new UserService();
					String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
					
					User user = userService.getUser(userName);
					
					if(user==null)
					{
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("username.not.found"))).build();
					}
					else
					{
						userData.setUsername(userName);
						String oldPwd = user.getPassword();
						BeanUtils.copyProperties(user, userData);
						
						user.setLanguage(mc.getHttpHeaders().getRequestHeaders().getFirst("Accept-Language"));
						user.setPlatform(mc.getHttpHeaders().getRequestHeaders().getFirst("X-Platform"));
						user.setApp_version(mc.getHttpHeaders().getRequestHeaders().getFirst("X-App-Version"));
						
						user.setPassword(userData.getPassword()!=null ? UserService.hashPassword(userData.getPassword()) :oldPwd);
						
						userService.saveOrUpdate(user);
						
						
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new UserDTO(user), "user.updated")))
								.header("Access-Control-Allow-Origin", "*")
									.header("Access-Control-Allow-Methods", "POST")
									.type(MediaType.APPLICATION_JSON)
									.build();
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}
			

		}
		
		
		
		
		
		
		
		
		/*
		@GET
		@Path("/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public User getUser(@PathParam("id") Long id) {
			return new UserService().findById(id);
		}

		
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
		public User addUser(User user) {
			return new UserService().saveOrUpdate(user);
		}
		
	    
	    @PUT
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
		public User updateUser(User user) {
			return new UserService().saveOrUpdate(user);
		}
	    
	    
	    @DELETE
	    @Path("/{id}")
	    public Response deleteUser(@PathParam("id") Long id) {
	    	new UserService().delete(id);
	    	return Response.status(200).build();
		}
		
		*/
		
		public static void main(String[] args) {
			System.out.println(UserService.hashPassword("admin"));
		}
}

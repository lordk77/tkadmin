package io.ticketcoin.dashboard.rest;

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
import io.ticketcoin.dashboard.persistence.service.PurhchaseOrderService;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.dashboard.rest.response.JSONResponseWrapper;

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
		@Path("/me/buy")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response buy(PurchaseOrderDTO order) {
			
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			PurhchaseOrderService pos = new PurhchaseOrderService();
				try {
					PurchaseOrder createdOrder = pos.placeOrder(order, userName);
					
					return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new PurchaseOrderDTO(createdOrder))))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "POST")
								.type(MediaType.APPLICATION_JSON)
								.build();
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(200).build();
				}
			

		}
		
		
		@GET
		@Path("/me/order/{orderUUID}")
	    @Produces(MediaType.APPLICATION_JSON)
		  public Response detail(@PathParam("orderUUID") String orderUUID) 
		  {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			PurchaseOrder order = new PurhchaseOrderService().getOrder(orderUUID);

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
		public Response register(UserProfileDTO userData) 
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
						
						BeanUtils.copyProperties(user, userData);
						user.setPassword(UserService.hashPassword(userData.getPassword()));
						userService.saveOrUpdate(user);
						
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(null, "user.updated")))
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

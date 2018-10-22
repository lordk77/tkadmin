package io.ticketcoin.dashboard.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.UserProfileDTO;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.dashboard.persistence.service.WalletService;
import io.ticketcoin.dashboard.rest.response.JSONResponseWrapper;

@Path("/")
public class RegistrationRestService {
	
		@Context
	    private MessageContext mc;
	
	
		@POST
		@Path("/reset_password")
		@Produces(MediaType.APPLICATION_JSON)
		public Response resetPassword(String userName) 
		{
			User user = new UserService().getUser(userName);
			
			if (user!=null)
			{
				 return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(null,"email.sent")))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "POST")
								.type(MediaType.APPLICATION_JSON)
								.build();
			}
			else 
			{
				 return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("user.not.found")))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "POST")
								.type(MediaType.APPLICATION_JSON)
								.build();
			}
		}
		
		
		
		@POST
		@Path("/register")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response register(UserProfileDTO userData) 
		{
			
				try 
				{
					
					UserService userService = new UserService();
					userData.setUsername(userData.getUsername().trim());
					userData.setEmail(userData.getEmail().trim());
					
					if(!userService.verifyUsername(userData.getUsername()))
					{
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("username.already.exists"))).type(MediaType.APPLICATION_JSON).build();
					}
					else if(!userService.verifyEmail(userData.getEmail()))
					{
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("email.already.exists"))).type(MediaType.APPLICATION_JSON).build();
					}
					else
					{
						User user = new User();
						BeanUtils.copyProperties(user, userData);
						user.setPassword(UserService.hashPassword(userData.getPassword()));
						user.setWallet(new WalletService().createEthereumWallet());
						//set roles
						userService.save(user);
						
						
						return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(null, "user.created")))
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
		
		
}

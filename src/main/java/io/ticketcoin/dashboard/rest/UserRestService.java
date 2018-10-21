package io.ticketcoin.dashboard.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;
import org.apache.cxf.rs.security.oauth2.utils.OAuthContextUtils;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.UserDTO;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;

@Path("/user")
public class UserRestService {
	
		@Context
	    private MessageContext mc;
	
	
		@GET
		@Path("/me")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getMe() {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getClientId();
			User user = new UserService().getUser(userName);
			UserDTO uDto = new UserDTO(user);
			
			 return Response.ok(new Gson().toJson(uDto))
						.header("Access-Control-Allow-Origin", "*")
							.header("Access-Control-Allow-Methods", "GET")
							.type(MediaType.APPLICATION_JSON)
							.build();
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
}

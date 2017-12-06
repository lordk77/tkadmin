package io.ticketcoin.dashboard.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;

@Path("/user")
public class UserRestService {
	

		@GET
		@Path("/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public User getMsg(@PathParam("id") Long id) {
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
}

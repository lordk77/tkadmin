package io.ticketcoin.dashboard.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.persistence.service.FileAttachmentService;

@Path("/image")
public class ImageRestService 
{

	@GET
	  @Path("/{imageUUID}")
	  @Produces({"image/png", "image/jpg"})
	  public Response getImage(@PathParam("imageUUID") final String imageUUID) 
	  {
		  FileAttachment fa = new FileAttachmentService().findByUUID(imageUUID);
    	  if(fa!=null)
    		  return Response.ok(fa.getContent()).build();
    	  else
    		  return Response.status(Response.Status.NOT_FOUND).build();
    	  
	  }
	  
	
}
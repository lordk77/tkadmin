package io.ticketcoin.rest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.UserDocumentInput;
import io.ticketcoin.dashboard.dto.UserDocumentOutput;
import io.ticketcoin.dashboard.persistence.filter.UserDocumentFilter;
import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.model.UserDocument;
import io.ticketcoin.dashboard.persistence.service.FileAttachmentService;
import io.ticketcoin.dashboard.persistence.service.UserDocumentService;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/documents")
public class UserDocumentRestService {
	
		@Context
	    private MessageContext mc;
	
		@GET
		@Path("/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getDocument(@PathParam("id") Long id) {
			
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			try 
			{
				UserDocumentFilter filter = new UserDocumentFilter();
				filter.setUsername(userName);
				filter.setId(id);
				List<UserDocument> documents = new UserDocumentService().searchDocuments(filter);
				
				UserDocumentOutput out = null;
				if(documents.size()>0)
				{
					out = new UserDocumentOutput(documents.get(0));
					
				}
				else
					throw new Exception("error.file.not.found");
				
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(out)))
						.header("Access-Control-Allow-Origin", "*")
							.header("Access-Control-Allow-Methods", "POST")
							.type(MediaType.APPLICATION_JSON)
							.build();
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
			
				
		}

		
		
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
		public Response addDocument(UserDocumentInput input) {
	    	
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			try
			{
			
				UserDocument document = new UserDocument();
				document.setName(input.getName());
				document.setCreated(new Date());
				document.setUser(new UserService().getUser(userName));

				if(input.getFront_photo()!=null)
					document.setFrontImage(prepareFileAttachment(input.getFront_photo()));
				
				if(input.getBack_photo()!=null)
					document.setBackImage(prepareFileAttachment(input.getBack_photo()));
				
				try {
					document.setDocType(UserDocument.DocTypes.valueOf(input.getDocumentType()));	}
				catch (Exception e) {
					document.setDocType(UserDocument.DocTypes.OTHER);	
					}
			
			
				new UserDocumentService().save(document);
				
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new UserDocumentOutput(document))))
						.header("Access-Control-Allow-Origin", "*")
							.header("Access-Control-Allow-Methods", "POST")
							.type(MediaType.APPLICATION_JSON)
							.build();
				
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
		}
		
	    
	    @PUT
	    @Path("/{id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
		public Response updateDocument(@PathParam("id") Long id, UserDocumentInput input) {
	    	
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			try
			{

				UserDocumentFilter filter = new UserDocumentFilter();
				filter.setUsername(userName);
				filter.setId(id);
				List<UserDocument> documents = new UserDocumentService().searchDocuments(filter);
				
				if(documents.size()>0)
				{
					UserDocument document = documents.get(0);

					if(input.getName()!=null)
						document.setName(input.getName());
					
					document.setUpdated(new Date());
					document.setUser(new UserService().getUser(userName));

					if(input.getFront_photo()!=null)
					{
						
						if(input.getFront_photo().length>0) 
						{
							FileAttachment fa = new FileAttachment();
							
							if(document.getFrontImage()!=null)
								fa = document.getFrontImage();
							else
								fa= prepareFileAttachment(input.getFront_photo());
							
							fa.setContent(input.getFront_photo());
							
							document.setFrontImage(fa);
						}
						else if (document.getFrontImage()!=null)
						{
							new FileAttachmentService().delete(document.getFrontImage());
							document.setFrontImage(null);
						}
					}
					
					if(input.getBack_photo()!=null)
					{
						if(input.getBack_photo().length>0) 
						{
							FileAttachment fa = new FileAttachment();
							
							if(document.getBackImage()!=null)
								fa = document.getBackImage();
							else
								fa= prepareFileAttachment(input.getBack_photo());
							
							fa.setContent(input.getBack_photo());
							
							document.setBackImage(fa);
						}
						else if (document.getBackImage()!=null)
						{
							new FileAttachmentService().delete(document.getBackImage());
							document.setBackImage(null);
						}
					}
					
					
					try {
						if(input.getDocumentType()!=null)
							document.setDocType(UserDocument.DocTypes.valueOf(input.getDocumentType()));	}
					catch (Exception e) {
						document.setDocType(UserDocument.DocTypes.OTHER);	
						}
				
					new UserDocumentService().saveOrUpdate(document);

					return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(new UserDocumentOutput(document))))
							.header("Access-Control-Allow-Origin", "*")
								.header("Access-Control-Allow-Methods", "POST")
								.type(MediaType.APPLICATION_JSON)
								.build();
					
				}
				else
					throw new Exception("error.file.not.found");
				
				

				
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
		}
	    
	    
	    @DELETE
	    @Path("/{id}")
	    public Response deleteUser(@PathParam("id") Long id) {
		    	try {
			    	new UserDocumentService().delete(id);
			    	return Response.status(200).build();
		    } catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
		}
		
	    
	    

		 
		 
		  @GET
		  @Path("/{id}/front_image")
		  @Produces({"image/png", "image/jpg"})
		  public Response getFrontImage(@PathParam("id") Long id) 
		  {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			try 
			{
				UserDocumentFilter filter = new UserDocumentFilter();
				filter.setUsername(userName);
				filter.setId(id);
				List<UserDocument> documents = new UserDocumentService().searchDocuments(filter);
				
				if(documents.size()>0 && documents.get(0).getFrontImage()!=null)
					return Response.ok(documents.get(0).getFrontImage().getContent()).build();
				else
					return Response.status(Response.Status.NOT_FOUND).build();
		    	  
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
	    	  
		  }
		
		  @GET
		  @Path("/{id}/back_image")
		  @Produces({"image/png", "image/jpg"})
		  public Response getBackImage(@PathParam("id") Long id) 
		  {
			String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
			try 
			{
				UserDocumentFilter filter = new UserDocumentFilter();
				filter.setUsername(userName);
				filter.setId(id);
				List<UserDocument> documents = new UserDocumentService().searchDocuments(filter);
				
				if(documents.size()>0 && documents.get(0).getFrontImage()!=null)
					return Response.ok(documents.get(0).getFrontImage().getContent()).build();
				else
					return Response.status(Response.Status.NOT_FOUND).build();
		    	  
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
	    	  
		  }
		
		  
		  private FileAttachment prepareFileAttachment(byte[] content)
		  {
				 FileAttachment fa= new FileAttachment();
				 fa.setContentType("application/octet-stream");
				 fa.setAttachmentUUID(UUID.randomUUID().toString());
				 fa.setFileName(fa.getAttachmentUUID());
				 
				 //TODO:metodo temporaneo per impostare la attachment url
				 HttpServletRequest request = mc.getHttpServletRequest();
				 String attachmentURL = request.getScheme() + "://" +  request.getServerName()+":"+request.getServerPort() + "/" + (request.getContextPath()!=null ? request.getContextPath():"") +  
						 "/services/image/"+fa.getAttachmentUUID();
				 fa.setAttachmentURL(attachmentURL);
				 fa.setContent(content);
				 return fa;
		  }


}

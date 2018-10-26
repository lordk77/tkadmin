package io.ticketcoin.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrantHandler;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.dto.ResetPasswordDTO;
import io.ticketcoin.dashboard.dto.UserProfileDTO;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.oauth.provider.TkAminOAuthDataProvider;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Path("/")
public class RegistrationRestService {
	
		@Context
	    private MessageContext mc;
	
		
		@Autowired
		ResourceOwnerGrantHandler handler;
	
		@Autowired
		TkAminOAuthDataProvider dataProvider;		
	
		@POST
		@Path("/reset_password")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response resetPassword(ResetPasswordDTO resetUserDTO) 
		{
			try
			{
				User user = new UserService().getUser(resetUserDTO.getUsername());
				
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
			catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic",e.getMessage()))).build();
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

						user.setLanguage(mc.getHttpHeaders().getRequestHeaders().getFirst("Accept-Language"));
						user.setPlatform(mc.getHttpHeaders().getRequestHeaders().getFirst("X-Platform"));
						user.setApp_version(mc.getHttpHeaders().getRequestHeaders().getFirst("X-App-Version"));
						
						user.setPassword(UserService.hashPassword(userData.getPassword()));
						userService.createUser(user);
						
						
						
						MultivaluedMap<String, String> params = new MetadataMap<>();
						params.add(OAuthConstants.RESOURCE_OWNER_NAME, user.getUsername());
						params.add(OAuthConstants.RESOURCE_OWNER_PASSWORD, userData.getPassword());
						ServerAccessToken serverToken =  handler.createAccessToken(dataProvider.getClient("admin"), params);
						
				        if (serverToken == null) {
				        	return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.facebook.token.invalid_grant"))).build();
				        }
				        else
				        {
				            // Extract the information to be of use for the client
				            ClientAccessToken clientToken = OAuthUtils.toClientAccessToken(serverToken, true);
				            return Response.ok(clientToken)
				                       .header(HttpHeaders.CACHE_CONTROL, "no-store")
				                       .header("Pragma", "no-cache")
				                        .build();
				            
				        }
					
					}
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
				}
		}
		
	
		public ResourceOwnerGrantHandler getHandler() {
			return handler;
		}



		public void setHandler(ResourceOwnerGrantHandler handler) {
			this.handler = handler;
		}



		public TkAminOAuthDataProvider getDataProvider() {
			return dataProvider;
		}



		public void setDataProvider(TkAminOAuthDataProvider dataProvider) {
			this.dataProvider = dataProvider;
		}
	

}
	

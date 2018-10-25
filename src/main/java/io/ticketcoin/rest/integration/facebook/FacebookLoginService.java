package io.ticketcoin.rest.integration.facebook;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrantHandler;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.ticketcoin.dashboard.persistence.filter.UserFilter;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.oauth.provider.TkAminOAuthDataProvider;
import io.ticketcoin.rest.response.JSONResponseWrapper;

@Component
@Path("/fb")
public class FacebookLoginService {

	@Autowired
	ResourceOwnerGrantHandler handler;
	
	@Autowired
	TkAminOAuthDataProvider dataProvider;
	
	
	@GET
	@Path("/login/{access_token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("access_token") String access_token) 
    {
		UserProfile userProfile=getProfileInfo(access_token);
		UserService userService = new UserService();
		
		try 
		{
			if (userProfile==null || userProfile.getId()==null)
			{
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.facebook.user.not.found"))).build();
			}
			else
			{
				UserFilter filter = new UserFilter();
				filter.setFb_identifier(userProfile.getId());
				List<User> users = userService.searchUsers(filter);
	
				
				User user = null;
				
				String generatedPassword = UUID.randomUUID().toString();
				
				
				if (users.isEmpty())
				{
					user = new User();
					user.setUsername("fb_"+userProfile.getId());
					user.setFb_identifier(userProfile.getId());
					user.setEmail(userProfile.getEmail());
					user.setName(userProfile.getFirst_name());
					user.setSurname(userProfile.getLast_name());
					user.setPassword(UserService.hashPassword(generatedPassword));
					user = userService.createUser(user);
				}
				else
				{
					user = users.get(0);
					user.setPassword(UserService.hashPassword(generatedPassword));
					user = userService.saveOrUpdate(user);
				}
				
				MultivaluedMap<String, String> params = new MetadataMap<>();
				params.add(OAuthConstants.RESOURCE_OWNER_NAME, user.getUsername());
				params.add(OAuthConstants.RESOURCE_OWNER_PASSWORD, generatedPassword);
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
		}
		catch (Exception e) {e.printStackTrace();
			return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper("error.generic")), e.getMessage()).build();
		}
		
		
    }
	
	public UserProfile getProfileInfo(String accessToken) {
		UserProfile userProfile= null;

		FBGraph fbGraph = new FBGraph(accessToken);
		String graph = fbGraph.getFBGraph();
		Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
		
		
		if (StringUtils.isNotBlank(fbProfileData.get("id")))
		{
			userProfile=new UserProfile();
			userProfile.setId(fbProfileData.get("id"));
			userProfile.setFirst_name(fbProfileData.get("first_name"));
			userProfile.setLast_name(fbProfileData.get("last_name"));
			userProfile.setEmail(fbProfileData.get("email"));			
			userProfile.setGender(fbProfileData.get("gender"));	
		}
		
		return userProfile;
	}
	 
	 
	
	public static void main(String[] args) {
		UserProfile userProfile=new FacebookLoginService().getProfileInfo("EAAUos1YPopgBAIuf5hlr9CQPILC9qYrv95PUdEielZAVKgcPdbFM60Rp028Thz5LYKA7q4fNRLu9G8NKJNaTWHfmGsS6rduZCpKZAIieddwwCkKgChE5QYDYzPaCLBvT7ucHRDGoN0oeFrwl7w4slJ0zdvNg9fKSr7E0Da8cFQ6X3cR4VBlBZC78ZCKyZA1F0ZD");
		System.out.println(userProfile.getFirst_name());
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

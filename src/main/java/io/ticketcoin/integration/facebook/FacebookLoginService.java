package io.ticketcoin.integration.facebook;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

@Path("/fb")
public class FacebookLoginService {

	
	
	
	@POST
	@Path("/login/{access_token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("access_token") String access_token) 
    {
		UserProfile userProfile=getProfileInfo(access_token);
		
		return Response.ok(new Gson().toJson(userProfile)).build();
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
			userProfile.setEmail(fbProfileData.get("email"));			
			userProfile.setGender(fbProfileData.get("gender"));	
		}
		
		return userProfile;
	}
	 
	 
	
	public static void main(String[] args) {
		UserProfile userProfile=new FacebookLoginService().getProfileInfo("EAAUos1YPopgBAIuf5hlr9CQPILC9qYrv95PUdEielZAVKgcPdbFM60Rp028Thz5LYKA7q4fNRLu9G8NKJNaTWHfmGsS6rduZCpKZAIieddwwCkKgChE5QYDYzPaCLBvT7ucHRDGoN0oeFrwl7w4slJ0zdvNg9fKSr7E0Da8cFQ6X3cR4VBlBZC78ZCKyZA1F0ZD");
		System.out.println(userProfile.getFirst_name());
	}
}

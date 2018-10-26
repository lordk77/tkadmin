package io.ticketcoin.oauth.grants;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrantHandler;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

public class TkResourceOwnerGrantHandler extends ResourceOwnerGrantHandler {

	
	@Override
    protected List<String> getApprovedScopes(Client client, UserSubject subject, List<String> requestedScopes) {
		List<String> approvedScope = new ArrayList<String>();
		approvedScope.add(OAuthConstants.REFRESH_TOKEN_SCOPE);
        return approvedScope;
    }
    
	
	
}

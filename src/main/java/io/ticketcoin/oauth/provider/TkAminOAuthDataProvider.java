package io.ticketcoin.oauth.provider;
import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.DefaultEHCacheCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.DefaultEHCacheOAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
 
public class TkAminOAuthDataProvider extends DefaultEHCacheCodeDataProvider {
 
	public TkAminOAuthDataProvider()
	{
		super();
		Client c = new Client("test", "test", true);
		setClient(c);
	}

	@Override
	public Client getClient(String clientId) throws OAuthServiceException {
		System.out.println(clientId);
		return super.getClient(clientId);
	}

	@Override
	public List<Client> getClients(UserSubject resourceOwner) {
		// TODO Auto-generated method stub
		return super.getClients(resourceOwner);
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(Client c, UserSubject sub) {
		// TODO Auto-generated method stub
		return super.getAccessTokens(c, sub);
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client c, UserSubject sub) {
		// TODO Auto-generated method stub
		return super.getRefreshTokens(c, sub);
	}

	@Override
	public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return super.getAccessToken(accessToken);
	}
	
	
	
	
	
	
//    public ServerAccessToken createAccessToken(AccessTokenRegistration reg)
//        throws OAuthServiceException {
// 
//        ServerAccessToken token = new BearerAccessToken(reg.getClient(), 3600L);
//         
//        List<String> scope = reg.getApprovedScope().isEmpty() ? reg.getRequestedScope() 
//                                                        : reg.getApprovedScope();
//        token.setScopes(convertScopeToPermissions(reg.getClient(), scope));
//        token.setSubject(reg.getSubject());
//        token.setGrantType(reg.getGrantType());
//         
//        // persist or encrypt and return
// 
//        return token;
//   }
   
}
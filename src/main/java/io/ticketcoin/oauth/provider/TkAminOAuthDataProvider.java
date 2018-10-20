package io.ticketcoin.oauth.provider;
import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
 
public class TkAminOAuthDataProvider implements AuthorizationCodeDataProvider {
 
    public ServerAccessToken createAccessToken(AccessTokenRegistration reg)
        throws OAuthServiceException {
 
        ServerAccessToken token = new BearerAccessToken(reg.getClient(), 3600L);
         
        List<String> scope = reg.getApprovedScope().isEmpty() ? reg.getRequestedScope() 
                                                        : reg.getApprovedScope();
        token.setScopes(convertScopeToPermissions(reg.getClient(), scope));
        token.setSubject(reg.getSubject());
        token.setGrantType(reg.getGrantType());
         
        // persist or encrypt and return
 
        return token;
   }
   // other methods are not shown

	@Override
	public List<OAuthPermission> convertScopeToPermissions(Client arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken getAccessToken(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(Client arg0, UserSubject arg1) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client getClient(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken getPreauthorizedToken(Client arg0, List<String> arg1, UserSubject arg2, String arg3)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client arg0, UserSubject arg1) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken refreshAccessToken(Client arg0, String arg1, List<String> arg2)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAccessToken(ServerAccessToken arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revokeToken(Client arg0, String arg1, String arg2) throws OAuthServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration arg0)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerAuthorizationCodeGrant> getCodeGrants(Client arg0, UserSubject arg1)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAuthorizationCodeGrant removeCodeGrant(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
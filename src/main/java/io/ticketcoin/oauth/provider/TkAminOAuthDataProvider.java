package io.ticketcoin.oauth.provider;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.provider.DefaultEHCacheOAuthDataProvider;
 
public class TkAminOAuthDataProvider extends DefaultEHCacheOAuthDataProvider {
 
	public TkAminOAuthDataProvider()
	{
		super();
		Client c = new Client("test", "test", true);
		setClient(c);
	}
	
}
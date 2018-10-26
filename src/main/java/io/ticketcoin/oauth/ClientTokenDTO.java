package io.ticketcoin.oauth;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;

@XmlRootElement
public class ClientTokenDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String access_token;
	private String token_type;
	private Long expires_in;
	private String scope;
	private String refresh_token;
	
	
	public ClientTokenDTO () {}
	
	public ClientTokenDTO( ClientAccessToken clientToken) 
	{
		this.access_token = clientToken.getTokenKey();
		this.token_type = clientToken.getTokenType();
		this.expires_in = clientToken.getExpiresIn();
		this.scope = clientToken.getApprovedScope();
		this.refresh_token = clientToken.getRefreshToken();
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public Long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	
}

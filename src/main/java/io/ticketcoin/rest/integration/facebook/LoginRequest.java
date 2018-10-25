package io.ticketcoin.rest.integration.facebook;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginRequest  implements Serializable
{
	private static final long serialVersionUID = 7308957441239601169L;
	
	
	private String fb_access_token;

	public String getFb_access_token() {
		return fb_access_token;
	}

	public void setFb_access_token(String fb_access_token) {
		this.fb_access_token = fb_access_token;
	}
	
}

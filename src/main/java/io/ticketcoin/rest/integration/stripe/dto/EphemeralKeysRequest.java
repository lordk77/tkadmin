package io.ticketcoin.rest.integration.stripe.dto;

public class EphemeralKeysRequest {
	
	
	public EphemeralKeysRequest() {
		super();
	}
	public EphemeralKeysRequest(String api_version,String source) {
		 this.api_version =api_version;
		 this.source = source;
	}
	

	private String api_version;
	private String source;
	

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}

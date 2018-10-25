package io.ticketcoin.dashboard.persistence.filter;

public class UserFilter {
	
	private String username;
	private String email;
	private String fb_identifier;
	private String bt_identifier;
	private int maxResult;	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFb_identifier() {
		return fb_identifier;
	}
	public void setFb_identifier(String fb_identifier) {
		this.fb_identifier = fb_identifier;
	}
	public String getBt_identifier() {
		return bt_identifier;
	}
	public void setBt_identifier(String bt_identifier) {
		this.bt_identifier = bt_identifier;
	}
	public int getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
	
}

package io.ticketcoin.dashboard.persistence.filter;

import java.util.Date;

public class TicketFilter {
	
	private String username;
	private Long id;
	
	private String ticketUUID;
	private Long organizationId;
	private Long tokenId;
	private String cardID;
	private Date date;
	
	
	private boolean includeExpired;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isIncludeExpired() {
		return includeExpired;
	}
	public void setIncludeExpired(boolean includeExpired) {
		this.includeExpired = includeExpired;
	}
	public String getTicketUUID() {
		return ticketUUID;
	}
	public void setTicketUUID(String ticketUUID) {
		this.ticketUUID = ticketUUID;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getTokenId() {
		return tokenId;
	}
	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}

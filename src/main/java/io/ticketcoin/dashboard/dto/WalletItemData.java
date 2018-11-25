package io.ticketcoin.dashboard.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WalletItemData {
	
	private String type;
	private String description;
	private String address;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}

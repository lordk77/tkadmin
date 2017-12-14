package io.ticketcoin.dashboard.persistence.filter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class EventFilter {

	private String genericTxt;

	public String getGenericTxt() {
		return genericTxt;
	}

	public void setGenericTxt(String genericTxt) {
		this.genericTxt = genericTxt;
	}
	
	
}

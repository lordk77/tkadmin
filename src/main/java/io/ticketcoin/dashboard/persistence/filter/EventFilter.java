package io.ticketcoin.dashboard.persistence.filter;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class EventFilter {

	private String genericTxt;
	private Date updatedSince;
	private int maxResult;
	private String category;
	private String eventUUID;

	public String getGenericTxt() {
		return genericTxt;
	}

	public void setGenericTxt(String genericTxt) {
		this.genericTxt = genericTxt;
	}

	public Date getUpdatedSince() {
		return updatedSince;
	}

	public void setUpdatedSince(Date updatedSince) {
		this.updatedSince = updatedSince;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEventUUID() {
		return eventUUID;
	}

	public void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}
	
	
}

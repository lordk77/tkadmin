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

	private Integer skip;
	private Integer limit;
	
	private Long organizationId;
	
	private boolean includeUnpublishedEvents;
	
	private boolean onlyActive;
	
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

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public boolean isOnlyActive() {
		return onlyActive;
	}

	public void setOnlyActive(boolean onlyActive) {
		this.onlyActive = onlyActive;
	}

	public boolean isIncludeUnpublishedEvents() {
		return includeUnpublishedEvents;
	}

	public void setIncludeUnpublishedEvents(boolean includeUnpublishedEvents) {
		this.includeUnpublishedEvents = includeUnpublishedEvents;
	}
	
	
}

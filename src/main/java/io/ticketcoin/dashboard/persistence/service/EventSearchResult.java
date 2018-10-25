package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import io.ticketcoin.dashboard.persistence.model.Event;

public class EventSearchResult {
	
	private Integer rowCount;
	private List<Event> results;
	
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public List<Event> getResults() {
		return results;
	}
	public void setResults(List<Event> results) {
		this.results = results;
	}

	
	
}

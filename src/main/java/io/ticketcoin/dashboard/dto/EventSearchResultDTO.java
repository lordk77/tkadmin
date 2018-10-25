package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventSearchResultDTO implements Serializable {
	
	
	private Integer rowCount;
	private List<EventDTO> results;
	
	
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public List<EventDTO> getResults() {
		return results;
	}
	public void setResults(List<EventDTO> results) {
		this.results = results;
	}

}

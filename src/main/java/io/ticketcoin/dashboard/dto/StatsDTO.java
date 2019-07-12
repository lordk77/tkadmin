package io.ticketcoin.dashboard.dto;

public class StatsDTO {
	
	private Long newCustomers;
	private Long allCustomers;
	
	
	public Long getNewCustomers() {
		return newCustomers;
	}
	public void setNewCustomers(Long newCustomers) {
		this.newCustomers = newCustomers;
	}
	public Long getAllCustomers() {
		return allCustomers;
	}
	public void setAllCustomers(Long allCustomers) {
		this.allCustomers = allCustomers;
	}

}

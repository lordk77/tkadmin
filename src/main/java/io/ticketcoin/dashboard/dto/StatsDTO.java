package io.ticketcoin.dashboard.dto;

import java.math.BigDecimal;

public class StatsDTO {
	
	private Long newCustomers;
	private Long allCustomers;
	
	
	private Long newTickets;
	private Long allTickets;
	
	private BigDecimal newIncomes;
	private BigDecimal allIncomes;
	
	
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
	public Long getNewTickets() {
		return newTickets;
	}
	public void setNewTickets(Long newTickets) {
		this.newTickets = newTickets;
	}
	public Long getAllTickets() {
		return allTickets;
	}
	public void setAllTickets(Long allTickets) {
		this.allTickets = allTickets;
	}
	public BigDecimal getNewIncomes() {
		return newIncomes;
	}
	public void setNewIncomes(BigDecimal newIncomes) {
		this.newIncomes = newIncomes;
	}
	public BigDecimal getAllIncomes() {
		return allIncomes;
	}
	public void setAllIncomes(BigDecimal allIncomes) {
		this.allIncomes = allIncomes;
	}
	public BigDecimal getNewCustomersPercentage() 
	{
		if(newCustomers!=null && allCustomers!=null && allCustomers >0l)
			return new BigDecimal(newCustomers).divide(new BigDecimal(allCustomers), 0, BigDecimal.ROUND_HALF_DOWN);
		else
			return BigDecimal.ZERO;
	}
	public BigDecimal getNewTicketsPercentage() {
		if(newTickets!=null && allTickets!=null && allTickets >0l)
			return new BigDecimal(newTickets).divide(new BigDecimal(allTickets), 0, BigDecimal.ROUND_HALF_DOWN);
		else
			return BigDecimal.ZERO;
	}
	public BigDecimal getNewIncomesPercentage() {
		if(newIncomes!=null && allIncomes!=null && allIncomes.compareTo(BigDecimal.ZERO) >0)
			return newIncomes.divide(allIncomes, 0, BigDecimal.ROUND_HALF_DOWN);
		else
			return BigDecimal.ZERO;
	}
	

}

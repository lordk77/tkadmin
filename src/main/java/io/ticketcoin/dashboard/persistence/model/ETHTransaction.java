package io.ticketcoin.dashboard.persistence.model;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="ETH_TRANSACTION")
public class ETHTransaction {
	
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String transactionHash;
	private String transactionDetailURL;
	private String blockNumber;
	private BigInteger gasUsed;
	private BigInteger gasPriceInWEI;

	
//	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
//	@JoinColumn(name="TICKET_ID")
//	private List<Ticket> tickets = new ArrayList<>();
	
	public String getTransactionHash() {
		return transactionHash;
	}
	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
	public BigInteger getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(BigInteger gasUsed) {
		this.gasUsed = gasUsed;
	}
	public BigInteger getGasPriceInWEI() {
		return gasPriceInWEI;
	}
	public void setGasPriceInWEI(BigInteger gasPriceInWEI) {
		this.gasPriceInWEI = gasPriceInWEI;
	}
	public String getTransactionDetailURL() {
		return transactionDetailURL;
	}
	public void setTransactionDetailURL(String transactionDetailURL) {
		this.transactionDetailURL = transactionDetailURL;
	}
	
}

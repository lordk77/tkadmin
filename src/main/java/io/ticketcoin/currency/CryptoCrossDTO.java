package io.ticketcoin.currency;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CryptoCrossDTO {
	
	private BigDecimal BTC;
	private BigDecimal ETH;
	private BigDecimal LTC;
	public BigDecimal getBTC() {
		return BTC;
	}
	public void setBTC(BigDecimal bTC) {
		BTC = bTC;
	}
	public BigDecimal getETH() {
		return ETH;
	}
	public void setETH(BigDecimal eTH) {
		ETH = eTH;
	}
	public BigDecimal getLTC() {
		return LTC;
	}
	public void setLTC(BigDecimal lTC) {
		LTC = lTC;
	}
	
	
		
		
	

}

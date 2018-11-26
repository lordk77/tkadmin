package io.ticketcoin.dashboard.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CardData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7766758902519926349L;
	private String cardID;

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
}

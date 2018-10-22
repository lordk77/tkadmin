package io.ticketcoin.dashboard.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventCategoryDTO implements Serializable{

	private static final long serialVersionUID = -3568633452830321331L;
	
	
	private String description;
	private String emojiCode;
	private Long eventCount;
	
	public EventCategoryDTO() {}
			
	public EventCategoryDTO(String description, String emojiCode, Long eventCount) {
		super();
		
		 this.description = description;
		 this.emojiCode = emojiCode;
		 this.eventCount = eventCount;
		
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmojiCode() {
		return emojiCode;
	}
	public void setEmojiCode(String emojiCode) {
		this.emojiCode = emojiCode;
	}

	public Long getEventCount() {
		return eventCount;
	}

	public void setEventCount(Long eventCount) {
		this.eventCount = eventCount;
	}
	

}

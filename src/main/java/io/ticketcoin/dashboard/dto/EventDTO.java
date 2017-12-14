package io.ticketcoin.dashboard.dto;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventDTO {
	
	
		private Long id;
		private String eventUUID;
	    private String name;
		private String description;
		private String imageUUID;
		
		public EventDTO() {}
		
		public EventDTO(Event event) 
		{
			this.id = event.getId();
			this.eventUUID= event.getEventUUID();
			this.name= event.getName();
			this.description= event.getDescription();
			
		}
		
		
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getEventUUID() {
			return eventUUID;
		}
		public void setEventUUID(String eventUUID) {
			this.eventUUID = eventUUID;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getImageUUID() {
			return imageUUID;
		}
		public void setImageUUID(String imageUUID) {
			this.imageUUID = imageUUID;
		}
}

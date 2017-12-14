package io.ticketcoin.dashboard.dto;

import javax.xml.bind.annotation.XmlRootElement;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventDTO {
	
	
		private String eventUUID;
	    private String name;
		private String description;
		private String imageUUID;
		private String artist;
		private Long updatedOn;
		private Long organizationId;
		
		
		public EventDTO() {}
		
		public EventDTO(Event event) 
		{
			this.eventUUID= event.getEventUUID();
			this.name= event.getName();
			this.description= event.getDescription();
			this.organizationId=event.getOrganization()!=null?event.getOrganization().getId():null;
			
			if(event.getImages()!=null && event.getImages().size()>0)
				this.imageUUID = event.getImages().get(0).getAttachmentUUID();
			
			if(event.getArtists()!=null && event.getArtists().size()>0)
			{
				this.artist ="";
				for(int i =0; i < event.getArtists().size();i++)
					this.artist +=  (i>0 ? ",":"") + event.getArtists().get(i).getName();
			}
			
			if(event.getUpdated()!=null)
				this.updatedOn=event.getUpdated().getTime();
//			else if (event.getCreated()!=null)
//				this.updatedOn=event.getCreated().getTime();
			
			
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

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public Long getUpdatedOn() {
			return updatedOn;
		}

		public void setUpdatedOn(Long updatedOn) {
			this.updatedOn = updatedOn;
		}

		public Long getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(Long organizationId) {
			this.organizationId = organizationId;
		}

}

package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import io.ticketcoin.dashboard.persistence.model.Event;

@XmlRootElement
public class EventDTO implements Serializable {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 3812592240488418078L;
		private String eventUUID;
	    private String name;
	    private String shortDescription;
		private String description;
		private String imageUUID;
		private String imageURL;
		private String artist;
		private Long updatedOn;
		private Long organizationId;
		private String eventType;
		private String urlShare;
		
		
		private String email;
		private String phone;
		private String website;

		
		private String dateFrom;
		private String dateTo;
		
		
		
		public EventDTO() {}
		
		public EventDTO(Event event) 
		{
			try {
				BeanUtils.copyProperties(this, event);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			
//			//Imposta le date col formato esteso ISO
//			if(event.getDateFrom()!=null)
//				this.dateFrom = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(event.getDateFrom());
//			if(event.getDateTo()!=null)
//				this.dateTo = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(event.getDateTo());	
			
		
			
			this.organizationId=event.getOrganization()!=null?event.getOrganization().getId():null;
			
			if(event.getImages()!=null && event.getImages().size()>0)
			{
				this.imageUUID = event.getImages().get(0).getAttachmentUUID();
				this.imageURL = event.getImages().get(0).getAttachmentURL();
			}
			
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

		public String getShortDescription() {
			return shortDescription;
		}

		public void setShortDescription(String shortDescription) {
			this.shortDescription = shortDescription;
		}

		public String getEventType() {
			return eventType;
		}

		public void setEventType(String eventType) {
			this.eventType = eventType;
		}


		public String getUrlShare() {
			return urlShare;
		}

		public void setUrlShare(String urlShare) {
			this.urlShare = urlShare;
		}

		public String getImageURL() {
			return imageURL;
		}

		public void setImageURL(String imageURL) {
			this.imageURL = imageURL;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}


		public String getDateFrom() {
			return dateFrom;
		}

		public void setDateFrom(String dateFrom) {
			this.dateFrom = dateFrom;
		}

		public String getDateTo() {
			return dateTo;
		}

		public void setDateTo(String dateTo) {
			this.dateTo = dateTo;
		}

}

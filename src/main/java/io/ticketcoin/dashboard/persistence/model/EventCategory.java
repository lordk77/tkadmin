package io.ticketcoin.dashboard.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import emoji4j.EmojiUtils;


@Entity
@Table(name="TDEF_EVENT_CATEGORY")
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventCategory {
		
//		ART("&#x1F3A8;","Art"),
//		THEATRE("&#x1F3AD;","Theatre"),
//		MUSIC("&#x1F3B6;","Music"),
//		SPORT("&#x1F3C0;","Sport"),
//		MUSEUM("&#x1F3E6;","Museum");
		
		@Id 
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long id;
		private String description;
		private String emojiCode ;
		
		public EventCategory() {}
		
		public EventCategory(String emojiCode, String description)
		{
			this.description = description;
			this.emojiCode = emojiCode;
		}
		public String getEmoji()
		{
			if(this.emojiCode!=null)
				return EmojiUtils.getEmoji(this.emojiCode).getEmoji();
			else return null;
		}		
		public String getDescription()
		{
			return description;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmojiCode() {
			return emojiCode;
		}

		public void setEmojiCode(String emojiCode) {
			this.emojiCode = emojiCode;
		}

		public void setDescription(String description) {
			this.description = description;
		}

}

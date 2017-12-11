package io.ticketcoin.dashboard.persistence.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="TDEF_EVENT")
@XmlRootElement
public class Event {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "EVENT_UUID")
	private String eventUUID;
    
    
    private String name;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LOCATION_ID")
	private Location location;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="ARTIST_ID")
	private List<Artist> artists;
	
	@Column(length=4000)
	private String description;

	@ElementCollection(targetClass=String.class)
	private List<String> tags;

	@Temporal(TemporalType.DATE)
	private Date dateFrom;
	
	@Temporal(TemporalType.DATE)
	private Date dateTo;
	
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="EVENT_ID")
	private List<ClosingDate> closingDates;
	
	
	private String daysOfWeek;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ORGANIZATION_ID")
	private Organization organization;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="EVENT_ID")
	private List<TicketCategory> categories;
	
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="EVENT_ID")
	private List<FileAttachment> images;
	
	
	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public List<ClosingDate> getClosingDates() {
		return closingDates;
	}

	public void setClosingDates(List<ClosingDate> closingDates) {
		this.closingDates = closingDates;
	}

	public String getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(String daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public String getEventUUID() {
		return eventUUID;
	}

	public void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public String getTagsAsString()
	{
		if (this.getTags()!=null)
			return StringUtils.join(this.getTags(), ",") ;
		else 
			return null;
	}
	public void setTagsAsString(String tags)
	{
		if(this.tags!=null)
		{
			try {
				this.tags.clear();
			}
			catch(Exception e) {
				this.tags=null;
			}
		}
			
		
		if(StringUtils.isNotEmpty(tags))
			this.tags=Arrays.asList(StringUtils.split(tags,","));
			
	}

	
	public List<TicketCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<TicketCategory> categories) {
		this.categories = categories;
	}

	public List<FileAttachment> getImages() {
		return images;
	}

	public void setImages(List<FileAttachment> images) {
		this.images = images;
	}
}

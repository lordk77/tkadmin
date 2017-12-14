package io.ticketcoin.dashboard.bean;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.service.EventService;


@ManagedBean
@SessionScoped
public class EventBean 
{
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	
	private Event event;
	
	
	public EventBean()
	{
		initEvent();
	}
	
	public void initEvent()
	{
		this.event=new Event();
		this.event.setCategories(new ArrayList<TicketCategory>());
		this.getEvent().getCategories().add(new TicketCategory(true));
	}

	
	public String editEvent(Long eventId)
	{
		this.event=new EventService().findById(eventId);
		return "/admin/eventDetail.xhtml";
	}
	
	
	
	 public void handleImageUpload(FileUploadEvent event) 
	 {
		 if (this.event.getImages()==null)
			 this.event.setImages(new ArrayList<FileAttachment>());
		 else
			 this.event.getImages().clear();
			 
		 FileAttachment fa= new FileAttachment();
		 fa.setContentType(event.getFile().getContentType());
		 fa.setFileName(event.getFile().getFileName());
		 fa.setAttachmentUUID(UUID.randomUUID().toString());
		 fa.setContent(event.getFile().getContents());
		 this.event.getImages().add(fa);
    }

	 


		private StreamedContent content;

		public StreamedContent getContent()
		{
			FileAttachment fa = this.event.getImages()!=null && this.event.getImages().size()>0?this.event.getImages().get(0):null ;
			
			 if (fa !=null)
				 content= new DefaultStreamedContent(new ByteArrayInputStream(fa.getContent()),(fa.getContentType()));
			 else
				 content= null;
			 
			 return content;
		}
		public void setContent(StreamedContent content) {
			this.content = content;
		}
		
	 
	 public void calculateNetPrice()
	 {
		 for(TicketCategory tc:this.event.getCategories())
		 {
			 if(tc.getStreetPrice()==null || tc.getStreetPrice().compareTo(new BigDecimal("1"))<0)
				 tc.setNetPrice(new BigDecimal("0"));
			 else
				 tc.setNetPrice( tc.getStreetPrice().subtract(new BigDecimal("1")));
		 }
	 }
	
	public String save()
	{
		this.event.setOrganization(userBean.getLoggedUser().getOrganization());
		try
		{
			new EventService().saveOrUpdate(this.event);
			 FacesMessage message = new FacesMessage("Succesful", "Event saved.");
		     FacesContext.getCurrentInstance().addMessage(null, message);	
		}
		catch (Exception e) {
			e.printStackTrace();
			 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
		     FacesContext.getCurrentInstance().addMessage(null, message);				
		}
		return null;
	}
	
	public String addCategory()
	{
		if (this.event.getCategories()==null)
			this.event.setCategories(new ArrayList<TicketCategory>());
		
		this.event.getCategories().add(new TicketCategory(true));
		
		return null;
	}
	

	public String removeCategory(TicketCategory tc)
	{
		this.event.getCategories().remove(tc);
		return null;
	}
	
	
	
	public String eventCreation()
	{
		initEvent();
		return "/admin/eventDetail.xhtml";
	}

	public String eventDetail(Long eventId)
	{
//		this.event = new Eventser
		return "/admin/eventDetail.xhtml";
	}
	
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	

	
	
}

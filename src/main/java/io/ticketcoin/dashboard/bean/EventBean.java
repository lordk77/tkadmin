package io.ticketcoin.dashboard.bean;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
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
		this.event.setEventUUID(UUID.randomUUID().toString());
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
		 
		 //TODO:metodo temporaneo per impostare la attachment url
		 ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		 String attachmentURL = ctx.getRequestScheme() + "://" +  ctx.getRequestServerName()+":"+ctx.getRequestServerPort() + "/" + (ctx.getRequestContextPath()!=null ? ctx.getRequestContextPath():"") +  
				 "services/image/"+fa.getAttachmentUUID();
		 fa.setAttachmentURL(attachmentURL);
		 
		 fa.setContent(event.getFile().getContents());
		 this.event.getImages().add(fa);
    }

	 


		private StreamedContent content;

		public StreamedContent getContent()
		{
			FileAttachment fa = this.event.getImages()!=null && this.event.getImages().size()>0?this.event.getImages().get(0):null ;
			
			 if (fa !=null && fa.getContent()!=null)
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
		
		TicketCategory tc = new TicketCategory(true);

		this.event.getCategories().add(tc);
		
//		generateCategoryDetail(this.event, tc) ;
		
		return null;
	}
	
	
	private void generateCategoryDetail(Event evt, TicketCategory tc) 
	{
		Date startDate = DateUtils.truncate(evt.getDateFrom(), Calendar.DAY_OF_MONTH);
		Date endDate = DateUtils.truncate(evt.getDateTo(), Calendar.DAY_OF_MONTH);
		Date cDate= startDate;
		while (!cDate.after(endDate))
		{
			boolean present=false;
			if(tc.getCategoryDetails()!=null)
			{
				for(TicketCategoryDetail tcd : tc.getCategoryDetails())
				{
					if (tcd.getStartingDate().equals(cDate))
						present=true;
						break;
				}
			}
			else
				tc.setCategoryDetails(new ArrayList<TicketCategoryDetail>());
			
			if(!present)
			{
				if(evt.getDaysOfWeek() !=null && evt.getDaysOfWeek().indexOf(""+DateUtils.toCalendar(cDate).get(Calendar.DAY_OF_WEEK))>=0)
				{
					TicketCategoryDetail tcd = new TicketCategoryDetail();
					tcd.setAvailableTicket(tc.getTicketSupply());
					tcd.setSoldTicket(0);
					tcd.setStartingDate(cDate);
					tcd.setTicketCategory(tc);
					if(tc.getCategoryDetails()==null)
						tc.setCategoryDetails(new ArrayList<TicketCategoryDetail>());;
					tc.getCategoryDetails().add(tcd);
				}
			}
			
			cDate = DateUtils.addDays(cDate, 1);
		}
		
		
	}
	

	public String removeCategory(TicketCategory tc)
	{
		this.event.getCategories().remove(tc);
		return null;
	}
	
	
	public String removeCategoryDetail(TicketCategoryDetail tcd)
	{
		for (TicketCategory tc : event.getCategories())
		{
			if(tc.equals(tcd.getTicketCategory()))
				tc.getCategoryDetails().remove(tcd);
		}
		return null;
	}
	
	
	
	
	
	
	public String regenerateCategoryDetail(TicketCategory tc)
	{
		generateCategoryDetail(this.event, tc);
		return null;
	}
	
	
	
	public List<TicketCategoryDetail> getCategoryDetails()
	{
		List<TicketCategoryDetail> categoryDetails = new ArrayList<>();
		
		if(event.getCategories()!=null)
		{
			for (TicketCategory tc : event.getCategories())
			{
				if(tc.getCategoryDetails()!=null)
				{
					categoryDetails.addAll(tc.getCategoryDetails());
				}
			}
		}
		
		
		categoryDetails.sort(new Comparator<TicketCategoryDetail>() {

			@Override
			public int compare(TicketCategoryDetail o1, TicketCategoryDetail o2) {
				if(o1.getTicketCategory().getId()!=null && o2.getTicketCategory().getId()!=null && !o1.getTicketCategory().getId().equals(o2.getTicketCategory().getId()))
					return o1.getTicketCategory().getId().compareTo(o2.getTicketCategory().getId());
				else
				{
					return o1.getStartingDate().compareTo(o2.getStartingDate());
				}
//				if(!o1.getStartingDate().equals(o2.getStartingDate()))
//					return o1.getStartingDate().compareTo(o2.getStartingDate());
//				else
//				{
//					return o1.getTicketCategory().getId().compareTo(o2.getTicketCategory().getId());
//				}
//				
				
			}
		});
		
		return categoryDetails;
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

	public String getCode39()
	{
		return this.event!=null?"{\"eventUUID\":" + this.event.getEventUUID() + "}":"";
	}
	
	
}

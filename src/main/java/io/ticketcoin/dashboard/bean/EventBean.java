package io.ticketcoin.dashboard.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.persistence.service.GenericService;

@ManagedBean
@ViewScoped
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
		this.getEvent().getCategories().add(new TicketCategory());
	}

	
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
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
		
		this.event.getCategories().add(new TicketCategory());
		
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

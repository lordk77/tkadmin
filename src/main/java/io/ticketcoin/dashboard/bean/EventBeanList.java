package io.ticketcoin.dashboard.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.service.EventService;


@ManagedBean
@ViewScoped
public class EventBeanList
{
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	
	private List<Event> events;


	
	
	
	public List<Event> getEvents() {
		if(events==null)
		{
			events= new EventService().getEvents(userBean.getLoggedUser());
		}
		return events;
	}


	public void setEvents(List<Event> events) {
		this.events = events;
	}


	public UserBean getUserBean() {
		return userBean;
	}


	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	
	

	
	
}

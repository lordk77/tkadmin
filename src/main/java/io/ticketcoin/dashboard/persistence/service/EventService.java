package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.EventDAO;
import io.ticketcoin.dashboard.persistence.dao.GenericDAO;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class EventService extends GenericService<Event> {

	public EventService() {
		super(Event.class);
	}

	
	
	
	
	public List<Event> getEvents(User user)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Event> retval = new EventDAO().getEvents(user);
			session.getTransaction().commit();
			return retval;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	
	public Event findById(Long id)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();		
			Event event = new EventDAO().findById(id);
			if(event.getCategories()!=null)
				Hibernate.initialize(event.getCategories());
			
			if(event.getTags()!=null)
				Hibernate.initialize(event.getTags());

			if(event.getImages()!=null)
				Hibernate.initialize(event.getImages());

			if(event.getEventCategories()!=null)
				Hibernate.initialize(event.getEventCategories());
			
			session.getTransaction().commit();
			return event;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}		
	}
	
	
	
	
}

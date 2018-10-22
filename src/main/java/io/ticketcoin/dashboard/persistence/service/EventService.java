package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.dto.EventCategoryDTO;
import io.ticketcoin.dashboard.persistence.dao.EventDAO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
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
	
	

	
	
	
	public List<EventCategoryDTO> searchCategories()
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<EventCategoryDTO> retval = new EventDAO().searchCategories();

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
	
	
	
	
	public List<Event> searchEvents(EventFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Event> retval = new EventDAO().searchEvents(filter);

			for(Event event : retval)
			{
				if(event.getCategories()!=null)
					Hibernate.initialize(event.getCategories());
				
				if(event.getImages()!=null)
					Hibernate.initialize(event.getImages());
	
				if(event.getArtists()!=null)
					Hibernate.initialize(event.getArtists());

				
				if(event.getEventCategories()!=null)
					Hibernate.initialize(event.getEventCategories());
				
				
			}
			
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
			
			if(event.getArtists()!=null)
				Hibernate.initialize(event.getArtists());
			
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

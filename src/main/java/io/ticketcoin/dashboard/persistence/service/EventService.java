package io.ticketcoin.dashboard.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.dto.EventCategoryDTO;
import io.ticketcoin.dashboard.dto.EventDTO;
import io.ticketcoin.dashboard.dto.EventSearchResultDTO;
import io.ticketcoin.dashboard.persistence.dao.EventDAO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class EventService extends GenericService<Event> {

	public EventService() {
		super(Event.class);
	}


	

	
	public TicketCategoryDetail getTicketCategoryDetail(String ticketCategoryUUID, Date date)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			TicketCategoryDetail retval = new EventDAO().getTicketCategoryDetail(ticketCategoryUUID, date);
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
	
	
	public Date getFirstAvailableDate(String eventUUID) {
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Date retval = new EventDAO().getFirstAvailableDate(eventUUID);

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
	
	
	public EventSearchResult searchEvents(EventFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			EventSearchResult retval = new EventDAO().searchEvents(filter);

			for(Event event : retval.getResults())
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
	
	
	public EventSearchResultDTO searchEventsDTO(EventFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			EventSearchResult searchResult = new EventDAO().searchEvents(filter);

			List<EventDTO> events = new ArrayList<>();
			for (Event e:searchResult.getResults())
				events.add(new EventDTO(e));
			
			
			EventSearchResultDTO resDTO = new EventSearchResultDTO();
			resDTO.setResults(events);
			resDTO.setRowCount(searchResult.getRowCount());
			
			session.getTransaction().commit();
			return resDTO;
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
			{
				Hibernate.initialize(event.getCategories());
				
				for(TicketCategory tc :event.getCategories())
				{
					if(tc.getCategoryDetails()!=null)
						Hibernate.initialize(tc.getCategoryDetails());
				}
			}
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

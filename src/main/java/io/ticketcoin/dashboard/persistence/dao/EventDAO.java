package io.ticketcoin.dashboard.persistence.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.h2.command.ddl.CreateFunctionAlias;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;

import io.ticketcoin.dashboard.dto.EventCategoryDTO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.EventSearchResult;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class EventDAO extends GenericDAO<Event>{

	
	public EventDAO() {
		super(Event.class);
	}

	public List<Event> getEvents(User user) {
		return (List<Event>)HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Event.class)
				.add(Restrictions.eq("organization", user.getOrganization())).list();

	}

	public EventSearchResult searchEvents(EventFilter filter) {
		Criteria c = createCrieria(filter).getExecutableCriteria(HibernateUtils.getSessionFactory().getCurrentSession());
		
				
		if(filter.getMaxResult()>0)
			c.setMaxResults(filter.getMaxResult());
		
		if (filter.getSkip()!=null && filter.getSkip()>0)
			c.setFirstResult(filter.getSkip());
		
		if (filter.getLimit()!=null && filter.getLimit()>0)
				c.setMaxResults(filter.getLimit());		
			
		
		EventSearchResult result = new EventSearchResult();
		result.setResults(c.list());
		
		if(filter.getSkip()!=null || filter.getLimit()!=null)
		{//Esegue la count
			 c = createCrieria(filter).getExecutableCriteria(HibernateUtils.getSessionFactory().getCurrentSession());
			 result.setRowCount(((Number)c.setProjection(Projections.count("id")).uniqueResult()).intValue());
		}
		else
		{
			result.setRowCount(result.getResults().size());
		}
		 return result;
	}
	
	
	private DetachedCriteria createCrieria(EventFilter filter) 
	{
		
		DetachedCriteria c =  DetachedCriteria.forClass(Event.class)
			.createAlias("location", "location",JoinType.LEFT_OUTER_JOIN)
			.createAlias("location.address", "address",JoinType.LEFT_OUTER_JOIN)
			;
			
		
		if(!StringUtils.isEmpty(filter.getGenericTxt()))
		{
			List<Criterion> x = new ArrayList<Criterion>();
			for(String s : new StringUtils().split(filter.getGenericTxt()," "))
			{
				x.add(Restrictions.like("name", filter.getGenericTxt(), MatchMode.ANYWHERE).ignoreCase());
				x.add(Restrictions.like("description", filter.getGenericTxt(), MatchMode.ANYWHERE).ignoreCase());
			}
			
			
			c.add(Restrictions.or(x.toArray(new Criterion[] {})));
		}
		 
		
		if (!StringUtils.isEmpty(filter.getCategory()))
		{
			c.createAlias("eventCategories", "eventCategories");
			c.add(Restrictions.eq("eventCategories.elements", Event.EventCategory.valueOf(filter.getCategory())));
		}
			
		
		if(filter.getUpdatedSince()!=null)
			c.add(Restrictions.gt("updated", filter.getUpdatedSince()));
	
		
		if(filter.getEventUUID()!=null)
			c.add(Restrictions.eq("eventUUID", filter.getEventUUID()));
		
		if(filter.isOnlyActive())
			c.add(Restrictions.ge("dateTo", DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
		
		
		return c;
	}

	public List<EventCategoryDTO> searchCategories() {
		
		 List<EventCategoryDTO>  categories = new ArrayList<>();

		
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Event.class)
				.createAlias("eventCategories", "eventCategory")
				;
				
		//Considera solo eventi futuri
		c.add(Restrictions.ge("dateTo", new Date()));
		
		c.setProjection(
				Projections.projectionList()
				.add(Projections.property("eventCategory.elements"), "category")
				.add(Projections.count("id"), "eventCount")
				.add(Projections.groupProperty("eventCategory.elements"))
				);
		
		
//		c.setResultTransformer(new AliasToBeanResultTransformer(EventCategoryDTO.class));
		for (Object o : c.list())
		{
			Event.EventCategory cat = (Event.EventCategory)((Object[])o)[0];
			categories.add(new EventCategoryDTO(cat.toString(), cat.getDescription(), ((Event.EventCategory)((Object[])o)[0]).getEmoji(), ((Number)((Object[])o)[1]).longValue()));
		}
		
		 return categories;
	}

	public TicketCategoryDetail getTicketCategoryDetail(String ticketCategoryUUID, Date date) {
		
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(TicketCategoryDetail.class)
				.createAlias("ticketCategory", "ticketCategory");
				
		c.add(Restrictions.eq("ticketCategory.ticketCategoryUUID", ticketCategoryUUID));
		
		if(date!=null)
			c.add(Restrictions.eq("startingDate", DateUtils.truncate(date, Calendar.DAY_OF_MONTH)));
		
		List<TicketCategoryDetail> tcd = c.list();
		
		return tcd.isEmpty() ? null : tcd.get(0);
	}
	
	
	public Date getFirstAvailableDate(String eventUUID) {
		
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(TicketCategoryDetail.class)
				.createAlias("ticketCategory", "ticketCategory")
				.createAlias("ticketCategory.event", "event");
				
		c.add(Restrictions.eq("event.eventUUID", eventUUID));
		c.add(Restrictions.gt("availableTicket", 0));
		c.add(Restrictions.ge("startingDate", DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
		c.setProjection(Projections.min("startingDate"));
		
		
		return (Date)c.uniqueResult();
		
	}
	
	


}

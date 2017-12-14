package io.ticketcoin.dashboard.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.User;
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

	public List<Event> searchEvents(EventFilter filter) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Event.class);
				
		
		List<Criterion> x = new ArrayList<Criterion>();
		for(String s : new StringUtils().split(filter.getGenericTxt()," "))
		{
		
					x.add(Restrictions.like("name", filter.getGenericTxt(), MatchMode.ANYWHERE).ignoreCase());
					x.add(Restrictions.like("description", filter.getGenericTxt(), MatchMode.ANYWHERE).ignoreCase());
		}
		
		c.add(Restrictions.or(x.toArray(new Criterion[] {})));
		 
		 return c.list();
	}
	
	
	


}

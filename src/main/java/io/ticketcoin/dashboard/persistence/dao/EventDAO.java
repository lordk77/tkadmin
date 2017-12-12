package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

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


}

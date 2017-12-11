package io.ticketcoin.dashboard.persistence.dao;

import io.ticketcoin.dashboard.persistence.model.Event;

public class EventDAO extends GenericDAO<Event>{

	
	public EventDAO() {
		super(Event.class);
	}


}

package io.ticketcoin.dashboard.persistence.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class TicketDAO extends GenericDAO<Ticket>{

	public TicketDAO() {
		super(Ticket.class);
	}

	public List<Ticket> searchTickets(TicketFilter filter) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Ticket.class)
				.createAlias("ownedBy", "user");

		
		if(filter.getId()!=null)
			c.add(Restrictions.idEq(filter.getId()));

		if (!StringUtils.isEmpty(filter.getUsername()))
			c.add(Restrictions.eq("user.username", filter.getUsername()));
		
		
		if(!filter.isIncludeExpired())
		{
			c.add(Restrictions.or(
					Restrictions.ge("validTo", DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)),
					Restrictions.and(Restrictions.isNull("validTo"),Restrictions.ge("validFrom", DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)))
					)
				);
			c.add(Restrictions.eq("ticketState", Ticket.STATE_VALID));
		}
		
		c.addOrder(Order.asc("validFrom"));
		
		 return c.list();
	}




}

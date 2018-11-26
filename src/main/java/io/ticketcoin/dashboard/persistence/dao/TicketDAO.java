package io.ticketcoin.dashboard.persistence.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class TicketDAO extends GenericDAO<Ticket>{

	public TicketDAO() {
		super(Ticket.class);
	}

	public List<Ticket> searchTickets(TicketFilter filter) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Ticket.class, "tck")
				.createAlias("ownedBy", "user");

		
		if(filter.getId()!=null)
			c.add(Restrictions.idEq(filter.getId()));

		if (!StringUtils.isEmpty(filter.getUsername()))
			c.add(Restrictions.eq("user.username", filter.getUsername()));

		if (!StringUtils.isEmpty(filter.getTicketUUID()))
			c.add(Restrictions.eq("ticketUUID", filter.getTicketUUID()));
		
		if(filter.getOrganizationId()!=null)
		{
			c.createAlias("category", "category");
			c.createAlias("category.event", "event");
			c.add(Restrictions.eq("event.organization.id", filter.getOrganizationId()));
			
		}
		
		if(filter.getCardID()!=null)
		{
			DetachedCriteria subquery = DetachedCriteria.forClass(Card.class,"card");	
			subquery.add(Restrictions.eq("card.address", filter.getCardID()));
			subquery.add(Restrictions.eqProperty("card.user", "tck.ownedBy"));				
			c.add(Subqueries.exists(subquery.setProjection(Projections.property("card.id"))));				
		}
		if(filter.getDate()!=null)
		{
			c.add(Restrictions.or(Restrictions.isNull("validFrom"),Restrictions.le("validFrom", filter.getDate())));
			c.add(Restrictions.or(Restrictions.isNull("validTo"),Restrictions.ge("validTo", filter.getDate())));
		}
		
		
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

	public Ticket consumeTicket(String ticketUUID, Long organizationId) throws Exception {
		
		List<Ticket> tcks = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Ticket.class)
				.createAlias("category", "category")
				.createAlias("category.event", "event")
				.add(Restrictions.eq("ticketUUID", ticketUUID))
				.add(Restrictions.eq("event.organization.id", organizationId))
				.setLockMode(LockMode.PESSIMISTIC_WRITE)
				.list()
				;
		if(tcks.size()==0)
			throw new Exception("error.ticket.not.found");
		else if(tcks.size()>1)
			throw new Exception("error.ticket.uuid.ambiguous");
		else if(tcks.get(0).getTicketState()==Ticket.STATE_SPENT)
			throw new Exception("error.spent.ticket");
		else if(tcks.get(0).getTicketState()==Ticket.STATE_INVALID)
			throw new Exception("error.invalid.ticket");				
		else
		{
			Ticket ticket = tcks.get(0);
			ticket.setTicketState(Ticket.STATE_SPENT);
			ticket.setSpentOn(new Date());
			HibernateUtils.getSessionFactory().getCurrentSession().save(ticket);
			return ticket;
		}
	}

	public List<Card> getUserCards(String userName) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Card.class)
				.createAlias("user", "user");

		c.add(Restrictions.eq("user.username", userName));

		c.addOrder(Order.asc("id"));
		
		 return c.list();
	}

	public Card getCardByAddress(String address) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Card.class);
		c.add(Restrictions.eq("address", address));
		 return (Card)c.uniqueResult();
	}




}

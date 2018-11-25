package io.ticketcoin.dashboard.persistence.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import io.ticketcoin.dashboard.dto.TicketDTO;
import io.ticketcoin.dashboard.persistence.dao.TicketDAO;
import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class CardService extends GenericService<Card>{

	
	
	public CardService() {
		super(Card.class);
	}
	

	
	public List<Card> getUserCards(String userName)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Card> retval = new TicketDAO().getUserCards(userName);
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

	
	public Card getCardByAddress(String address)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Card retval = new TicketDAO().getCardByAddress(address);
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
	
	
	
}

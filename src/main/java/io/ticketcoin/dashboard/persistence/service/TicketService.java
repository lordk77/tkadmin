package io.ticketcoin.dashboard.persistence.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import io.ticketcoin.dashboard.dto.TicketDTO;
import io.ticketcoin.dashboard.persistence.dao.TicketDAO;
import io.ticketcoin.dashboard.persistence.filter.TicketFilter;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class TicketService extends GenericService<Ticket>{

	public TicketService() {
		super(Ticket.class);
	}
	

	
	public List<Ticket> searchTickets(TicketFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Ticket> retval = new TicketDAO().searchTickets(filter);
			
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

	
	public List<TicketDTO> searchTicketsDTO(TicketFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<TicketDTO> ticketDTOs = new ArrayList<>();

			List<Ticket> tickets = new TicketDAO().searchTickets(filter);

			
			for (Ticket t: tickets)
				ticketDTOs.add(new TicketDTO(t));
			
			
			session.getTransaction().commit();
			return ticketDTOs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	public TicketDTO consumeTicket(String ticketUUID, Long organizationId) throws Exception
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Ticket ticket = new TicketDAO().consumeTicket(ticketUUID, organizationId);
			
			TicketDTO ticketDTO = new TicketDTO(ticket);
			
			session.getTransaction().commit();
			return ticketDTO;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	
	
	
}

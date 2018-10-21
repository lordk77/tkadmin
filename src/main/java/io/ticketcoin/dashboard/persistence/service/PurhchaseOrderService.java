package io.ticketcoin.dashboard.persistence.service;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.dto.PurchaseOrderDTO;
import io.ticketcoin.dashboard.dto.PurchaseOrderDetailDTO;
import io.ticketcoin.dashboard.persistence.dao.EventDAO;
import io.ticketcoin.dashboard.persistence.dao.PurchaseOrderDAO;
import io.ticketcoin.dashboard.persistence.dao.UserDAO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrderDetail;
import io.ticketcoin.dashboard.persistence.model.TicketCategory;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.exception.EventNotFoundException;
import io.ticketcoin.dashboard.persistence.service.exception.TicketCategoryNotFoundException;
import io.ticketcoin.dashboard.persistence.service.exception.UserNotFoundException;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class PurhchaseOrderService extends GenericService<PurchaseOrder>{

	
	public static String STATUS_PENDING = "PENDING";
	public static String STATUS_REJECTED = "REJECTED";
	public static String STATUS_CANCELED = "CANCELED";
	public static String STATUS_COMLPETED = "COMLPETED";
	
	
	public PurhchaseOrderService() {
		super(PurchaseOrder.class);
	}
	
	public PurchaseOrder placeOrder(PurchaseOrderDTO purhchaseOrderDTO, String username) throws EventNotFoundException, UserNotFoundException, TicketCategoryNotFoundException,  Exception
	{
		Session session = null;
		
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			EventDAO eventDao = new EventDAO();
			EventFilter filter= new EventFilter();
			filter.setEventUUID(purhchaseOrderDTO.getEventUUID());
			List<Event> events = eventDao.searchEvents(filter);
			if(events == null || events.size()!=1)
				throw new EventNotFoundException();
			
			
			PurchaseOrder purhchaseOrder= new PurchaseOrder();
			purhchaseOrder.setEventUUID(purhchaseOrderDTO.getEventUUID());
			purhchaseOrder.setTotalAmount(new BigDecimal("0"));
			purhchaseOrder.setStatus(STATUS_PENDING);
			
			for (PurchaseOrderDetailDTO dto : purhchaseOrderDTO.getOrderDetail())
			{
				boolean found = false;
				for (TicketCategory category : events.get(0).getCategories())
				{
					if(category.getTicketCategoryUUID().equals(dto.getTicketCategoryUUID()))
					{
						PurchaseOrderDetail detail = new PurchaseOrderDetail();
						detail.setDate(dto.getDate());
						detail.setQuantity(dto.getQuantity());
						detail.setTicketCategoryUUID(category.getTicketCategoryUUID());
						detail.setAmount(category.getStreetPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
						detail.setDescription(category.getDescription());
						purhchaseOrder.setTotalAmount(purhchaseOrder.getTotalAmount().add(detail.getAmount()));
						found=true;
						break;
					}
				}
				if (!found)
					throw new TicketCategoryNotFoundException(dto.getTicketCategoryUUID());
			}

			
			User user = new UserDAO().getUser(username);
			
			if(user==null)
				throw new UserNotFoundException(purhchaseOrderDTO.getUsername());
			else
				purhchaseOrder.setUser(user);
			session.save(purhchaseOrder);
			session.getTransaction().commit();
			return purhchaseOrder;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}

	
	public PurchaseOrder getOrder(String orderUUID)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			PurchaseOrder retval = new PurchaseOrderDAO().getOrder(orderUUID);
			if(retval!=null && retval.getOrderDetail()!=null)
			{
				Hibernate.initialize(retval.getOrderDetail());
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
	
	
	public boolean verifyEmail(String email)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			boolean retval = new UserDAO().verifyEmail(email);
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

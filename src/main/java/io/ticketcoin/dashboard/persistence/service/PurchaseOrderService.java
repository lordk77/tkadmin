package io.ticketcoin.dashboard.persistence.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.currency.CryptoConverter;
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
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.exception.EventNotFoundException;
import io.ticketcoin.dashboard.persistence.service.exception.TicketCategoryNotFoundException;
import io.ticketcoin.dashboard.persistence.service.exception.UserNotFoundException;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class PurchaseOrderService extends GenericService<PurchaseOrder>{

	
	public static String STATUS_PENDING = "PENDING";
	public static String STATUS_REJECTED = "REJECTED";
	public static String STATUS_CANCELED = "CANCELED";
	public static String STATUS_COMLPETED = "COMLPETED";
	
	public static BigDecimal ETH_COMMISSION = new BigDecimal("0.2");
	
	public PurchaseOrderService() {
		super(PurchaseOrder.class);
	}
	
	public PurchaseOrder placeOrder(PurchaseOrderDTO purchaseOrderDTO, String username) throws EventNotFoundException, UserNotFoundException, TicketCategoryNotFoundException,  Exception
	{
		Session session = null;
		
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			
			EventDAO eventDao = new EventDAO();
			EventFilter filter= new EventFilter();
			filter.setEventUUID(purchaseOrderDTO.getEventUUID());
			List<Event> events = eventDao.searchEvents(filter).getResults();
			if(events == null || events.size()!=1)
				throw new EventNotFoundException();
			
			
			for(PurchaseOrderDetailDTO pod : purchaseOrderDTO.getOrderDetail())
			{
				TicketCategoryDetail tcd =
				(TicketCategoryDetail)HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(TicketCategoryDetail.class)
				.createAlias("ticketCategory", "ticketCategory")
				.add(Restrictions.eq("ticketCategory.ticketCategoryUUID", pod.getTicketCategoryUUID()))
				.add(Restrictions.eq("startingDate", purchaseOrderDTO.getReservationDate()))
				.uniqueResult();
				if(tcd==null)
					throw new Exception("error.ticketCategoryUUID.not.found");
				else if (tcd.getAvailableTicket()< pod.getQuantity())
					throw new Exception("error.insufficient.ticket");
					 
			}
			

			
			
			PurchaseOrder purchaseOrder= new PurchaseOrder();
			purchaseOrder.setEventUUID(purchaseOrderDTO.getEventUUID());
			purchaseOrder.setPaymentType(purchaseOrderDTO.getPaymentType());
			purchaseOrder.setTotalAmount(new BigDecimal("0"));
			purchaseOrder.setTotalAmountETH(new BigDecimal("0"));
			purchaseOrder.setStatus(STATUS_PENDING);
			purchaseOrder.setCreated(new Date());
			purchaseOrder.setOrderUUID(UUID.randomUUID().toString());
			purchaseOrder.setReservationDate(purchaseOrderDTO.getReservationDate());
			purchaseOrder.setCurrency(events.get(0).getCurrency());
			
			for (PurchaseOrderDetailDTO dto : purchaseOrderDTO.getOrderDetail())
			{
				boolean found = false;
				for (TicketCategory category : events.get(0).getCategories())
				{
					if(category.getTicketCategoryUUID().equals(dto.getTicketCategoryUUID()))
					{
						PurchaseOrderDetail detail = new PurchaseOrderDetail();
						detail.setDescription(category.getDescription());
						detail.setQuantity(dto.getQuantity());
						detail.setTicketCategoryUUID(category.getTicketCategoryUUID());
						
						detail.setAmount(category.getStreetPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
						detail.setAmountETH(
							CryptoConverter.convert(
									//Add commission (for the test phase is fixed to 20 euro cents
									detail.getAmount().add(ETH_COMMISSION), purchaseOrder.getCurrency(), CryptoConverter.CryptoCurrency.ETH));
						
						purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(detail.getAmount()));
						purchaseOrder.setTotalAmountETH(purchaseOrder.getTotalAmountETH().add(detail.getAmountETH()));
						purchaseOrder.getOrderDetail().add(detail);
						found=true;
						break;
					}
				}
				if (!found)
					throw new TicketCategoryNotFoundException(dto.getTicketCategoryUUID());
			}

			
			User user = new UserDAO().getUser(username);
			
			if(user==null)
				throw new UserNotFoundException(purchaseOrderDTO.getUsername());
			else
				purchaseOrder.setUser(user);
			
			
			
			session.save(purchaseOrder);
			session.getTransaction().commit();
			return purchaseOrder;
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

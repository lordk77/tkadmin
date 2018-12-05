package io.ticketcoin.dashboard.persistence.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.stripe.model.Charge;

import io.ticketcoin.dashboard.persistence.dao.ChargeDAO;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.utils.HibernateUtils;
import io.ticketcoin.web3j.wrapper.TicketCoinCoreUtil;

public class ChargeService {
	
	
	
	public Charge carge(PurchaseOrder order, Map<String, Object> chargeParams) throws Exception {
		
		Session session = null;

		if (PurchaseOrderService.STATUS_COMLPETED.equals(order.getStatus()))
			throw new Exception("error.order.already.processed");
		
		try
		{
			
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			
			
			List<Ticket> tickets =  new ChargeDAO().processOrder(order);

			Charge charge = Charge.create(chargeParams);
	
			if(charge.getPaid())
			{
				session.getTransaction().commit();
				updateOrderState( order, PurchaseOrderService.STATUS_COMLPETED);
			}
			else
			{
				session.getTransaction().rollback();
				updateOrderState( order, PurchaseOrderService.STATUS_REJECTED);
			}
			
			
			//Scrive i ticket in blochchain
			for(Ticket t:tickets)
				new TicketCoinCoreUtil().enrollTicket(t.getId());
			
			
			return charge;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			updateOrderState( order, PurchaseOrderService.STATUS_CANCELED);
			throw e;
		}
	}
	
	private void updateOrderState(PurchaseOrder order, String state) {
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			//update order
			order = (PurchaseOrder)session.createCriteria(PurchaseOrder.class).add(Restrictions.idEq(order.getId())).uniqueResult();
			order.setStatus(state);
			
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}

}

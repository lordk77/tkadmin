package io.ticketcoin.dashboard.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrderDetail;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.persistence.model.TicketCategoryDetail;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class ChargeDAO {

	

	public List<Ticket> processOrder(PurchaseOrder order) throws Exception {
		
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		List<String> ticketCategoryUUIDs = new ArrayList<>();
		
		List<Ticket> enrolledTickets  = new ArrayList<>();
		
		for(PurchaseOrderDetail pod : order.getOrderDetail())
			ticketCategoryUUIDs.add(pod.getTicketCategoryUUID());
		
		List<TicketCategoryDetail> tcds = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(TicketCategoryDetail.class)
				.createAlias("ticketCategory", "ticketCategory")
				.setLockMode(LockMode.PESSIMISTIC_WRITE)
				.add(Restrictions.in("ticketCategory.ticketCategoryUUID", ticketCategoryUUIDs))
				.add(Restrictions.eq("startingDate", order.getReservationDate()))
				.list()
				;
				
				for(PurchaseOrderDetail pod : order.getOrderDetail())
				{
					boolean found = false;
					
					for(TicketCategoryDetail tcd : tcds)
					{
						if(tcd.getTicketCategory().getTicketCategoryUUID().equals(pod.getTicketCategoryUUID()))
						{
							if (tcd.getAvailableTicket()< pod.getQuantity())
								throw new Exception("error.insufficient.ticket");
							else
							{
								tcd.setAvailableTicket(tcd.getAvailableTicket() - pod.getQuantity());
								session.save(tcd);
								
								//Creates the tickets
								for (int i = 0; i < (Boolean.TRUE.equals(pod.getGroupTicket()) ? 1 : pod.getQuantity());i++)
								{
									Ticket tikcet = new Ticket(pod,tcd);
									session.save(tikcet);
									enrolledTickets.add(tikcet);
								}
								
							}
							
							found = true;
							break;
						}
						

					}
					if(!found)
						throw new Exception("error.ticketCategoryUUID.not.found");
				}
				
				return enrolledTickets;
				
				
				

	}


}

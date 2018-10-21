package io.ticketcoin.dashboard.persistence.dao;

import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class PurchaseOrderDAO extends GenericDAO<PurchaseOrder>{

	public PurchaseOrderDAO() {
		super(PurchaseOrder.class);
	}



	public PurchaseOrder getOrder(String orderUUID) {
		return (PurchaseOrder)HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(PurchaseOrder.class)
				.createAlias("user", "user")
				.add(Restrictions.eq("orderUUID", orderUUID)).uniqueResult();
	}


}

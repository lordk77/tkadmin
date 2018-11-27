package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import io.ticketcoin.dashboard.persistence.model.TermsAndCondition;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class TermsAndConditionDAO extends GenericDAO<TermsAndCondition>{

	public TermsAndConditionDAO() {
		super(TermsAndCondition.class);
	}

	public Long getLatestTermsAndConditionId() {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(TermsAndCondition.class);

		c.addOrder(Order.desc("id"));
		c.setProjection(Projections.property("id"));

		List<Number> ids = c.list();
		
		if(ids.size()>0)
			return ids.get(0).longValue();
		else
			return null;
	}

}

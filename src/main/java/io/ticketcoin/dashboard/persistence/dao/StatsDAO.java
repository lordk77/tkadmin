package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.facebook.api.schema.User;

import io.ticketcoin.dashboard.dto.StatsDTO;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class StatsDAO {

	public StatsDTO getGenericStats(Long organizationId) {

		
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(User.class);

		c.addOrder(Order.desc("id"));
		c.setProjection(Projections.property("id"));

		List<Number> ids = c.list();
		/*
		if(ids.size()>0)
			return ids.get(0).longValue();
		else
			return null;
		*/
		
		
		return null;
	}




}

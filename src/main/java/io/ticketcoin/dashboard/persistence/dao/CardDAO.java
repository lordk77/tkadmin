package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class CardDAO extends GenericDAO<Card>{

	public CardDAO() {
		super(Card.class);
	}

	public List<Card> getUserCards(String userName) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Card.class)
				.createAlias("user", "user");

		c.add(Restrictions.eq("user.username", userName));

		c.addOrder(Order.asc("id"));
		
		 return c.list();
	}




}

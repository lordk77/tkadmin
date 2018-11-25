package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Wallet;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class WalletDAO extends GenericDAO<Wallet>{

	public WalletDAO() {
		super(Wallet.class);
	}

	public List<Wallet> getUserWallets(String userName) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Wallet.class)
				.createAlias("user", "user");

		c.add(Restrictions.eq("user.username", userName));

		c.addOrder(Order.asc("id"));
		
		 return c.list();
	}
	
	
	public Card getWalletByAddress(String address) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(Wallet.class);
		c.add(Restrictions.eq("address", address));
		 return (Card)c.uniqueResult();
	}




}

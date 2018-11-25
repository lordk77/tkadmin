package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Wallet;
import io.ticketcoin.dashboard.persistence.model.WalletItem;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class WalletItemDAO extends GenericDAO<Wallet>{

	public WalletItemDAO() {
		super(Wallet.class);
	}

	public List<WalletItem> getUserWalletItems(String userName) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(WalletItem.class)
				.createAlias("user", "user");

		c.add(Restrictions.eq("user.username", userName));

		c.addOrder(Order.asc("id"));
		
		 return c.list();
	}
	
	
	public WalletItem getWalletItemByAddress(String address) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(WalletItem.class);
		c.add(Restrictions.eq("address", address));
		 return (WalletItem)c.uniqueResult();
	}


	public WalletItem getWalletItemById(Long id) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(WalletItem.class);
		c.add(Restrictions.idEq(id));
		 return (WalletItem)c.uniqueResult();
	}
	


}

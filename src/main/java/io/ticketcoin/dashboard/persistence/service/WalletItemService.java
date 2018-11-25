package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.WalletItemDAO;
import io.ticketcoin.dashboard.persistence.model.WalletItem;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class WalletItemService extends GenericService<WalletItem>{

	public WalletItemService() {
		super(WalletItem.class);
	}
	

	
	public WalletItem getWalletItemByAddress(String address)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			WalletItem retval = new WalletItemDAO().getWalletItemByAddress(address);
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

	public WalletItem getWalletItemById(Long id)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			WalletItem retval = new WalletItemDAO().getWalletItemById(id);
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
	

	public List<WalletItem> getUserWalletItems(String userName)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<WalletItem> retval = new WalletItemDAO().getUserWalletItems(userName);
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

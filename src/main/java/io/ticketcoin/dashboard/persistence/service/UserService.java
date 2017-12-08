package io.ticketcoin.dashboard.persistence.service;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.UserDAO;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class UserService extends GenericService<User>{

	public UserService() {
		super(User.class);
	}
	
	public boolean verifyUsername(String username)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			boolean retval = new UserDAO().verifyUsername(username);
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

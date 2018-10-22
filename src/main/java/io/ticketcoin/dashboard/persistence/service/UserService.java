package io.ticketcoin.dashboard.persistence.service;

import org.hibernate.Hibernate;
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
	
	public static String hashPassword(String password)
	{
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
	}

	
	public User getUser(String username)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			User retval = new UserDAO().getUser(username);
			if(retval!=null && retval.getOrganization()!=null)
			{
				Hibernate.initialize(retval.getOrganization());
			}
			if(retval!=null && retval.getRoles()!=null)
				Hibernate.initialize(retval.getRoles());
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
	
	
	public boolean verifyEmail(String email)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			boolean retval = new UserDAO().verifyEmail(email);
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

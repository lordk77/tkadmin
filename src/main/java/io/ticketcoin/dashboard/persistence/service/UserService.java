package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.EventDAO;
import io.ticketcoin.dashboard.persistence.dao.UserDAO;
import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.filter.UserFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.model.Wallet;
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
	
	

	
	public User createUser(User user) throws Exception
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			//crea lo wallet
			Wallet mainWallet = new WalletService().createEthereumWallet();
			mainWallet.setUser(user);
			mainWallet.setDescription("Ticketcoin main wallet");
			user.setWallet(mainWallet);
			
			//assegna il profilo
			
			
			//Salva l'utente
			new UserDAO().save(user);
			
			
			
			session.getTransaction().commit();
			return user;
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
	
	
	public List<User> searchUsers(UserFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<User> retval = new UserDAO().searchUsers(filter);
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

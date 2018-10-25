package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.GenericDAO;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class GenericService<T> {

    protected Class<T> clazz;

    public GenericService(Class<T> clazz) {
        this.clazz = clazz;
    }

	public T save(T entity)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			T retval = new GenericDAO<T>(clazz).save(entity);
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
	
    
	public T saveOrUpdate(T entity)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			T retval = new GenericDAO<T>(clazz).saveOrUpdate(entity);
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

	
	public void delete(T entity)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			new GenericDAO<T>(clazz).delete(entity);
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}			
	}
	

	public void delete(Long id)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			GenericDAO<T> gd = new GenericDAO<T>(clazz);
			gd.delete(gd.findById(id));
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}			
	}
	
	
	public List<T> findAll()
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();		
			List<T> retval = new GenericDAO<T>(clazz).findAll();
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

	public T findById(Long id)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();		
			T retval = new GenericDAO<T>(clazz).findById(id);
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

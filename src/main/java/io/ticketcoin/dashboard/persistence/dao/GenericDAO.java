package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import io.ticketcoin.dashboard.utils.HibernateUtils;

public class GenericDAO<T> {
	
	
    protected Class<T> clazz;

    public GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
    }


	public T save(T entity)
	{
		HibernateUtils.getSessionFactory().getCurrentSession().save(entity);
		return entity;
	}
    
	public T saveOrUpdate(T entity)
	{
		HibernateUtils.getSessionFactory().getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	
	public void delete(T entity)
	{
		HibernateUtils.getSessionFactory().getCurrentSession().delete(entity);
	}
	
	
	public List<T> findAll()
	{
		return HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(clazz).list();
	}



	public T findById(Long id) {
		return HibernateUtils.getSessionFactory().getCurrentSession().get(clazz, id);
	}
	
	
}

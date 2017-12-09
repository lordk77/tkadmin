package io.ticketcoin.dashboard.persistence.service;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.RoleDAO;
import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class RoleService extends GenericService<Role> {

	public RoleService() {
		super(Role.class);
	}

	
	
	public Role findByName(String rolename)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Role retval = new RoleDAO().findByName(rolename);
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

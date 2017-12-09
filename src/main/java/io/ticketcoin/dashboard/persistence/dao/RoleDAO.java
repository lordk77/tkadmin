package io.ticketcoin.dashboard.persistence.dao;

import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class RoleDAO extends GenericDAO<Role>{

	
	public RoleDAO() {
		super(Role.class);
	}

	public Role findByName(String rolename) {
		return (Role)HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(Role.class).add(Restrictions.eq("rolename", rolename)).uniqueResult();

	}

}

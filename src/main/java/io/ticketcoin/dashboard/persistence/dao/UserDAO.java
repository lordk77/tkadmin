package io.ticketcoin.dashboard.persistence.dao;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class UserDAO extends GenericDAO<User>{

	public UserDAO() {
		super(User.class);
	}

	public boolean verifyUsername(String username) {
		return HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(User.class).add(Restrictions.eq("username", username).ignoreCase()).setProjection(Projections.property("id")).list().size()==0;
	}

	public boolean verifyEmail(String email) {
		return HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(User.class).add(Restrictions.eq("email", email).ignoreCase()).setProjection(Projections.property("id")).list().size()==0;
	}


}

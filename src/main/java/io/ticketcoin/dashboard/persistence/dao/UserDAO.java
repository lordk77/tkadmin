package io.ticketcoin.dashboard.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.sql.JoinType;

import io.ticketcoin.dashboard.persistence.filter.EventFilter;
import io.ticketcoin.dashboard.persistence.filter.UserFilter;
import io.ticketcoin.dashboard.persistence.model.Event;
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

	public User getUser(String username) {
		return (User)HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
	}

	public List<User> searchUsers(UserFilter filter) {
		
			Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
					.createCriteria(User.class);
			

			if (!StringUtils.isEmpty(filter.getUsername()))
				c.add(Restrictions.eq("username", filter.getUsername()));
			
			
			if (!StringUtils.isEmpty(filter.getEmail()))
				c.add(Restrictions.eq("email", filter.getEmail()));
			
			
			if (!StringUtils.isEmpty(filter.getFb_identifier()))
				c.add(Restrictions.eq("fb_identifier", filter.getFb_identifier()));
			
			
			if (!StringUtils.isEmpty(filter.getBt_identifier()))
				c.add(Restrictions.eq("bt_identifier", filter.getBt_identifier()));
			
			
			if(filter.getMaxResult()>0)
				c.setMaxResults(filter.getMaxResult());
			
			
			 return c.list();

	}

	public Integer updateAcceptedTermsAndCondition(String username, Long acceptedTermsAndCondition) {
		Query query = HibernateUtils.getSessionFactory().getCurrentSession().createQuery("update User set acceptedTermsAndCondition = :acceptedTermsAndCondition where username = :username ");
		query.setParameter("username", username);
		query.setParameter("acceptedTermsAndCondition", acceptedTermsAndCondition);
		return query.executeUpdate();
	}


}

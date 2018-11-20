package io.ticketcoin.dashboard.persistence.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.filter.UserDocumentFilter;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.model.UserDocument;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class UserDocumentDAO extends GenericDAO<UserDocument>{

	public UserDocumentDAO() {
		super(UserDocument.class);
	}

	public List<UserDocument> searchDocuments(UserDocumentFilter filter) {
		Criteria c = HibernateUtils.getSessionFactory().getCurrentSession()
				.createCriteria(UserDocument.class)
				.createAlias("user", "user");
		
		if(filter.getId()!=null)
			c.add(Restrictions.idEq(filter.getId()));

		if (!StringUtils.isEmpty(filter.getUsername()))
			c.add(Restrictions.eq("user.username", filter.getUsername()));
		
		
		 return c.list();
	}




}

package io.ticketcoin.dashboard.persistence.service;

import java.util.List;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.UserDocumentDAO;
import io.ticketcoin.dashboard.persistence.filter.UserDocumentFilter;
import io.ticketcoin.dashboard.persistence.model.UserDocument;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class UserDocumentService extends GenericService<UserDocument>{

	public UserDocumentService() {
		super(UserDocument.class);
	}
	

	
	public List<UserDocument> searchDocuments(UserDocumentFilter filter)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<UserDocument> retval = new UserDocumentDAO().searchDocuments(filter);
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

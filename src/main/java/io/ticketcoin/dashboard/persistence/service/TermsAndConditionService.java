package io.ticketcoin.dashboard.persistence.service;

import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.TermsAndConditionDAO;
import io.ticketcoin.dashboard.persistence.model.TermsAndCondition;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class TermsAndConditionService extends GenericService<TermsAndCondition>{

	public TermsAndConditionService() {
		super(TermsAndCondition.class);
	}
	
	public Long getLatestTermsAndConditionId()
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Long retval = new TermsAndConditionDAO().getLatestTermsAndConditionId();
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

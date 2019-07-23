package io.ticketcoin.dashboard.persistence.service;

import java.util.Date;
import java.util.Map;

import org.hibernate.Session;

import com.facebook.api.schema.User;

import io.ticketcoin.dashboard.dto.StatsDTO;
import io.ticketcoin.dashboard.persistence.dao.StatsDAO;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class StatsService {

	
	
	public StatsDTO getGenericStats(Long organizationId)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			StatsDTO retval = new StatsDAO().getGenericStats(organizationId);
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
	
	
	
	public Map<Date, Long> getTicketTimeSeries(Long organizationId)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Map<Date, Long> retval = new StatsDAO().getTicketTimeSeries(organizationId);
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

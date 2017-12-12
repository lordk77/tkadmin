package io.ticketcoin.dashboard.persistence.service;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import io.ticketcoin.dashboard.persistence.dao.FileAttachmentDAO;
import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class FileAttachmentService extends GenericService<FileAttachment>{

	public FileAttachmentService() {
		super(FileAttachment.class);
	}
	

	
	public FileAttachment findByUUID(String attachmentUUID)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			FileAttachment retval = new FileAttachmentDAO().findByUUID(attachmentUUID);
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

package io.ticketcoin.dashboard.persistence.dao;

import org.hibernate.criterion.Restrictions;

import io.ticketcoin.dashboard.persistence.model.FileAttachment;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class FileAttachmentDAO extends GenericDAO<FileAttachment>{

	public FileAttachmentDAO() {
		super(FileAttachment.class);
	}

	public FileAttachment findByUUID(String attachmentUUID) {
		return (FileAttachment)HibernateUtils.getSessionFactory().getCurrentSession().createCriteria(FileAttachment.class).add(Restrictions.eq("attachmentUUID", attachmentUUID)).uniqueResult();

	}



}

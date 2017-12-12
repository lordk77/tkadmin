package io.ticketcoin.dashboard.bean;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.WeakHashMap;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import io.ticketcoin.dashboard.persistence.model.FileAttachment;

@ManagedBean
public class FileAttachmentBean {

	private Map<String, FileAttachment> resourcesMap = new WeakHashMap<String, FileAttachment>();
	
	
	private StreamedContent content;
	
//	public StreamedContent getContent()
//	{
//		FileAttachment fa = resourcesMap.get(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("contentUUID"));
//		
//		 if (fa !=null)
//			 content= new DefaultStreamedContent(new ByteArrayInputStream(fa.getContent()),(fa.getContentType()));
//		 else
//			 content= null;
//		 
//		 return content;
//	}
//	
	
	public void addResource(FileAttachment fa)
	{
		resourcesMap.put(fa.getAttachmentUUID(),fa);
		
	}




	public void setContent(StreamedContent content) {
		this.content = content;
	}


	
	
	
}

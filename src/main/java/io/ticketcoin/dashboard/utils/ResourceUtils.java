package io.ticketcoin.dashboard.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import io.ticketcoin.dashboard.persistence.model.FileAttachment;

public class ResourceUtils {

	public static String imageFolder;
	
	
	public FileAttachment save(FileAttachment fa) throws IOException
	{
		File f = null;
		String suffix = fa.getFileName().substring(fa.getFileName().lastIndexOf("."));
		if (fa.getId()==null)
		{
			f = File.createTempFile(fa.getAttachmentUUID(), suffix, new File(getImgeFolder()));
			f.deleteOnExit();
		}
		else
		{
			f = new File(getImgeFolder() + fa.getAttachmentUUID()+suffix);
		}
		
		FileOutputStream fos = new FileOutputStream(f);
		ByteArrayInputStream bais = new ByteArrayInputStream(fa.getContent());
		IOUtils.copyLarge(bais, fos);
		bais.close();
		fos.close();
		
		fa.setFileName(f.getName());
		return fa;
	}
	
	
	private String getImgeFolder()
	{
		if (imageFolder==null)
			imageFolder=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/uploaded-images/");
		
		return imageFolder;
	}


	public byte[] getContent(FileAttachment fa) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(getImgeFolder() +'/'+ fa.getFileName()));
			IOUtils.copyLarge(fis, baos);
			fis.close();
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}

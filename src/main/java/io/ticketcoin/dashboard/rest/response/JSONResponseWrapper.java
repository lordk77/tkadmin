package io.ticketcoin.dashboard.rest.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JSONResponseWrapper {
	

	
	private boolean success=true;
	private String message;
	private Object data;
	
	private JSONResponseWrapper() {}
	

	
	
	public static JSONResponseWrapper getSuccessWrapper(Object data, String message) {
		JSONResponseWrapper wrapper = new JSONResponseWrapper();
		wrapper.setData(data);
		wrapper.success = true;
		wrapper.setMessage(message);
		return wrapper;
	}
	
	
	public static JSONResponseWrapper getSuccessWrapper(Object data) {
		return getSuccessWrapper(data, null);

	}
	
	public static JSONResponseWrapper getFaultWrapper(String message) {
		JSONResponseWrapper wrapper = new JSONResponseWrapper();
		wrapper.success=false;
		wrapper.setMessage(message);
		return wrapper;
		
	}
	

	

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}




	public boolean isSuccess() {
		return success;
	}




	public void setSuccess(boolean success) {
		this.success = success;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}

}

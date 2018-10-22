package io.ticketcoin.dashboard.rest.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JSONResponseWrapper {
	
	public static String SATUS_SUCCESS = "success";
	public static String SATUS_FAILURE = "failure";

	
	private String status;
	private String Message;
	private Object data;
	
	private JSONResponseWrapper() {}
	

	
	
	public static JSONResponseWrapper getSuccessWrapper(Object data, String message) {
		JSONResponseWrapper wrapper = new JSONResponseWrapper();
		wrapper.setData(data);
		wrapper.setStatus(SATUS_SUCCESS);
		wrapper.setMessage(message);
		return wrapper;
	}
	
	
	public static JSONResponseWrapper getSuccessWrapper(Object data) {
		return getSuccessWrapper(data, null);

	}
	
	public static JSONResponseWrapper getFaultWrapper(String message) {
		JSONResponseWrapper wrapper = new JSONResponseWrapper();
		wrapper.setStatus(SATUS_FAILURE);
		return wrapper;
		
	}
	


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

}

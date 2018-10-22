package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrderDetail;

@XmlRootElement
public class PurchaseOrderDetailDTO  implements Serializable{

	
	private static final long serialVersionUID = -1767279242094573896L;
	private String ticketCategoryUUID;
	private Integer quantity;
	
	public PurchaseOrderDetailDTO() {}
	public PurchaseOrderDetailDTO(PurchaseOrderDetail detail) {
		try {
			BeanUtils.copyProperties(this, detail);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getTicketCategoryUUID() {
		return ticketCategoryUUID;
	}
	public void setTicketCategoryUUID(String ticketCategoryUUID) {
		this.ticketCategoryUUID = ticketCategoryUUID;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


}

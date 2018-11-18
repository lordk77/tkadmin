package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.PurchaseOrderDetail;
import io.ticketcoin.rest.integration.stripe.StripeService;

@XmlRootElement
public class PurchaseOrderDTO implements Serializable{
	private static final long serialVersionUID = -6180250644038091899L;
	private String eventUUID;
	private Date reservationDate;
	private List<PurchaseOrderDetailDTO> orderDetail;
	private String orderUUID;
	private BigDecimal totalAmount;
	private BigDecimal totalAmountETH;
	
	private String description;
	private String status;
	private String username;
	private String stripePublicKey;
	private String currency;
	
	
    
	
	public PurchaseOrderDTO() {}
	
	public PurchaseOrderDTO(PurchaseOrder order) {
		try {
			 BeanUtils.copyProperties(this, order);
			 stripePublicKey = StripeService.getPublicKey();
			 orderDetail = new ArrayList<PurchaseOrderDetailDTO>();
			 
			if(order.getUser()!=null)
				this.setUsername(order.getUser().getUsername());
			if(order.getOrderDetail()!=null) {
				for(PurchaseOrderDetail detail : order.getOrderDetail())
					orderDetail.add(new PurchaseOrderDetailDTO(detail));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getEventUUID() {
		return eventUUID;
	}
	public void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}
	public List<PurchaseOrderDetailDTO> getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(List<PurchaseOrderDetailDTO> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public String getOrderUUID() {
		return orderUUID;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getStripePublicKey() {
		return stripePublicKey;
	}

	public void setStripePublicKey(String stripePublicKey) {
		this.stripePublicKey = stripePublicKey;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getTotalAmountETH() {
		return totalAmountETH;
	}

	public void setTotalAmountETH(BigDecimal totalAmountETH) {
		this.totalAmountETH = totalAmountETH;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}

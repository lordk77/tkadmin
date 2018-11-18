package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="PURCHASE_ORDER")
@XmlRootElement
public class PurchaseOrder implements Serializable{

	private static final long serialVersionUID = 6728445760213803385L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String orderUUID;
	
	private String eventUUID;
	private String status;
	
	
	private String currency;
	private BigDecimal totalAmount;
	private BigDecimal totalAmountETH;
	
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Temporal(TemporalType.DATE)
	private Date reservationDate;
	

	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="PURCHASE_ORDER_ID")
	private List<PurchaseOrderDetail> orderDetail = new ArrayList<>();
	
	@Temporal(TemporalType.DATE)
	private Date created;

	  @PrePersist
	  protected void onCreate() {
	    created = new Date();
	    orderUUID = UUID.randomUUID().toString();
	  }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventUUID() {
		return eventUUID;
	}

	public void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}

	public List<PurchaseOrderDetail> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<PurchaseOrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getOrderUUID() {
		return orderUUID;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
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
	

}

package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="PURCHASE_ORDER_DETAIL")
@XmlRootElement
public class PurchaseOrderDetail implements Serializable{

	private static final long serialVersionUID = 6728445760213803385L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String ticketCategoryUUID;
	private Integer quantity;
	private BigDecimal amount;
	private String description;
	
	@Column(name="IS_GROUP_TICKET")
	private Boolean groupTicket = Boolean.TRUE;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PURCHASE_ORDER_ID")
	private PurchaseOrder order;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Boolean getGroupTicket() {
		return groupTicket;
	}
	public void setGroupTicket(Boolean groupTicket) {
		this.groupTicket = groupTicket;
	}
	public PurchaseOrder getOrder() {
		return order;
	}
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

}

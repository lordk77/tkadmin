package io.ticketcoin.rest.integration.stripe.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name="STRIPE_CHARGE_REQUEST")
@XmlRootElement
public class StripeChargeRequest {
	
	@XmlRootElement
	    public enum Currency {
	        EUR, USD;
	    }
	
		@Id 
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long id;
	
	
		private Date requestDate;
		
	    private String description;
	    private Currency currency;
	    private String stripeEmail;
	    private String stripeToken;
	    private String orderUUID;
	    private String userId;
	    private String stripeTokenType;
	    
	    
	    
	    
		@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
		@JoinColumn(name="CHARGE_REQUEST_ID")
	    private StripeChargeResponse response;
		
	    
	    
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Currency getCurrency() {
			return currency;
		}
		public void setCurrency(Currency currency) {
			this.currency = currency;
		}
		public String getStripeEmail() {
			return stripeEmail;
		}
		public void setStripeEmail(String stripeEmail) {
			this.stripeEmail = stripeEmail;
		}
		public String getStripeToken() {
			return stripeToken;
		}
		public void setStripeToken(String stripeToken) {
			this.stripeToken = stripeToken;
		}
		public String getOrderUUID() {
			return orderUUID;
		}
		public void setOrderUUID(String orderUUID) {
			this.orderUUID = orderUUID;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public StripeChargeResponse getResponse() {
			return response;
		}
		public void setResponse(StripeChargeResponse response) {
			this.response = response;
		}
		public String getStripeTokenType() {
			return stripeTokenType;
		}
		public void setStripeTokenType(String stripeTokenType) {
			this.stripeTokenType = stripeTokenType;
		}
		public Date getRequestDate() {
			return requestDate;
		}
		public void setRequestDate(Date requestDate) {
			this.requestDate = requestDate;
		}
	
	

}

package io.ticketcoin.dashboard.persistence.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(
	name="TICKET", 
	indexes = { @Index(name = "TICKET_UUID_IDX", columnList = "TICKET_UUID") })
@XmlRootElement
public class Ticket 
{
	
    public static int STATE_VALID = 0;
    public static int STATE_SPENT = 1;
    public static int STATE_INVALID = 2;

    public static int TRANSFER_RULE_ANY = 0;
    public static int TRANSFER_RULE_WHITE_LIST_ONLY = 1;
    
    public Ticket() 
    {
    	super();
    }

    public Ticket(PurchaseOrderDetail pod, TicketCategoryDetail categoryDetail) 
    {
    	this.ticketUUID = UUID.randomUUID().toString();
    	this.ownedBy = pod.getOrder().getUser();
    	this.currency = pod.getOrder().getCurrency() ;
    	this.validFrom = categoryDetail.getStartingDate();
    	this.validTo= categoryDetail.getEndingDate();
    	this.allowedTransfers = -1;
    	this.enrollTime = new Date();
    	this.ticketState = STATE_VALID;
    	this.transferRule = TRANSFER_RULE_ANY;
    	this.allowedEntrances= Boolean.TRUE.equals(pod.getGroupTicket()) ? pod.getQuantity() : 1;
    	this.category = categoryDetail.getTicketCategory();
    	this.order = pod.getOrder();
    }
    
    
    
    
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "TICKET_UUID")
	private String ticketUUID;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OWNED_BY")
	private User ownedBy;

    @Column(name = "CURRENCY")
	private String currency;
    
    @Column(name = "SPENT_ON")
	private Date spentOn;

    @Column(name = "CANCELED_ON")
	private Date canceledOn;
    
    @Column(name = "VALID_FROM")
	private Date validFrom;
    
    @Column(name = "VALID_TO")
	private Date validTo;
    
    @Column(name = "ALLOWED_TRANSFERS")
	private Integer allowedTransfers;
    
    @Column(name = "ENROLLED_ON")
	private Date enrollTime;
    
    @Column(name = "TICKET_STATE")
	private Integer ticketState;
    
    @Column(name = "TRANSFER_RULE")
	private Integer transferRule;

    @Column(name = "ALLOWED_ENTRANCES")
	private Integer allowedEntrances;
    
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CATEGORY_ID")
	private TicketCategory category;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ORDER_ID")
	private PurchaseOrder order;
	
	@Column(name = "TOKEN_ID")
	private Long tokenId;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTicketUUID() {
		return ticketUUID;
	}


	public void setTicketUUID(String ticketUUID) {
		this.ticketUUID = ticketUUID;
	}



	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public Date getSpentOn() {
		return spentOn;
	}


	public void setSpentOn(Date spentOn) {
		this.spentOn = spentOn;
	}


	public Date getCanceledOn() {
		return canceledOn;
	}


	public void setCanceledOn(Date canceledOn) {
		this.canceledOn = canceledOn;
	}


	public Date getValidFrom() {
		return validFrom;
	}


	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}


	public Date getValidTo() {
		return validTo;
	}


	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}


	public Integer getAllowedTransfers() {
		return allowedTransfers;
	}


	public void setAllowedTransfers(Integer allowedTransfers) {
		this.allowedTransfers = allowedTransfers;
	}


	public Date getEnrollTime() {
		return enrollTime;
	}


	public void setEnrollTime(Date enrollTime) {
		this.enrollTime = enrollTime;
	}


	public Integer getTicketState() {
		return ticketState;
	}


	public void setTicketState(Integer ticketState) {
		this.ticketState = ticketState;
	}


	public Integer getTransferRule() {
		return transferRule;
	}


	public void setTransferRule(Integer transferRule) {
		this.transferRule = transferRule;
	}


	public TicketCategory getCategory() {
		return category;
	}


	public void setCategory(TicketCategory category) {
		this.category = category;
	}


	public PurchaseOrder getOrder() {
		return order;
	}


	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}


	public User getOwnedBy() {
		return ownedBy;
	}


	public void setOwnedBy(User ownedBy) {
		this.ownedBy = ownedBy;
	}


	public Integer getAllowedEntrances() {
		return allowedEntrances;
	}


	public void setAllowedEntrances(Integer allowedEntrances) {
		this.allowedEntrances = allowedEntrances;
	}

	
	
	  
	  

	
}

package io.ticketcoin.dashboard.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.model.Wallet;
import io.ticketcoin.dashboard.persistence.model.WalletItem;

@XmlRootElement
public class WalletItemDTO implements Serializable{

	private static final long serialVersionUID = -1287124660465443912L;
	private Long id;
	private Long version;
	private String address;
	private String description;
	private String type;
	private String icon;
	
	
	public WalletItemDTO() {}
	public WalletItemDTO(WalletItem walletItem) {
		
		try {
			BeanUtils.copyProperties(this, walletItem);
			
			if(walletItem instanceof Wallet)
				this.setIcon(((Wallet)walletItem).getType()!=null ? ((Wallet)walletItem).getType().icon : null);
			else if(walletItem instanceof Card)
				this.setIcon(((Card)walletItem).getType()!=null ? ((Card)walletItem).getType().icon : null);
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	
	
}

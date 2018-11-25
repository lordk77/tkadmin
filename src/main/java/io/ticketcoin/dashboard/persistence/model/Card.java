package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="CARD")
@XmlRootElement
public class Card extends WalletItem implements Serializable{
	
	
	
	public enum CardType
	{
		CARD("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjwhRE9DVFlQRSBzdmcgIFBVQkxJQyAnLS8vVzNDLy9EVEQgU1ZHIDEuMS8vRU4nICAnaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkJz48c3ZnIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgMCAwIDUwIDUwIiBoZWlnaHQ9IjUwcHgiIGlkPSJMYXllcl8xIiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCA1MCA1MCIgd2lkdGg9IjUwcHgiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPjxyZWN0IGZpbGw9Im5vbmUiIGhlaWdodD0iNTAiIHdpZHRoPSI1MCIvPjxyZWN0IGZpbGw9Im5vbmUiIGhlaWdodD0iNTAiIHdpZHRoPSI1MCIvPjxwYXRoIGQ9IiAgTTQyLDEwYzAsMC0yOS4zOTcsMC0zNCwwYy0zLjA3NiwwLTUsMy01LDV2MjAuMzg0QzMsMzcuOTM0LDUuMDY2LDQwLDcuNjE2LDQwaDM0Ljc2OEM0NC45MzMsNDAsNDcsMzcuOTM0LDQ3LDM1LjM4NFYxNC44NDYgIEM0NywxMi4yOTksNDQuNTQ5LDEwLDQyLDEweiIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjMDAwMDAwIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1taXRlcmxpbWl0PSIxMCIgc3Ryb2tlLXdpZHRoPSIyIi8+PHJlY3QgaGVpZ2h0PSI1IiB3aWR0aD0iNDQiIHg9IjMiIHk9IjE2Ii8+PC9zdmc+");

		public String icon;
		
	    private CardType(String icon) {
			this.icon=icon;
		}
	}
	

	
	public Card()
	{
		this.setType(CardType.CARD);
	}
	
	private static final long serialVersionUID = -1287124660465443912L;
	
	@Enumerated(EnumType.STRING)
	private CardType type;


	public CardType getType() {
		return type;
	}


	public void setType(CardType type) {
		this.type = type;
	}
	
	
	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return this.type!=null ? this.type.icon:null;
	}
	
	
	
	
}

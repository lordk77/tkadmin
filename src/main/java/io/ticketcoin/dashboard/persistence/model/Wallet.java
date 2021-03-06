package io.ticketcoin.dashboard.persistence.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="WALLET")
@XmlRootElement
public class Wallet extends WalletItem implements Serializable{
	
	public enum WalletType
	{
		ETH_WALLET("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjwhRE9DVFlQRSBzdmcgIFBVQkxJQyAnLS8vVzNDLy9EVEQgU1ZHIDEuMS8vRU4nICAnaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkJz48c3ZnIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgMCAwIDY0IDY0IiBoZWlnaHQ9IjY0cHgiIGlkPSJMYXllcl8xIiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCA2NCA2NCIgd2lkdGg9IjY0cHgiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPjxnIGlkPSJFdGhlcmV1bV9FVEgiPjxnPjxnPjxnPjxnPjxnPjxnPjxnPjxwYXRoIGQ9Ik0zMiw1OUMxNy4xMTIsNTksNSw0Ni44ODgsNSwzMlMxNy4xMTIsNSwzMiw1czI3LDEyLjExMiwyNywyN1M0Ni44ODgsNTksMzIsNTl6IE0zMiw2ICAgICAgICAgIEMxNy42NjMsNiw2LDE3LjY2Myw2LDMyczExLjY2MywyNiwyNiwyNnMyNi0xMS42NjMsMjYtMjZTNDYuMzM3LDYsMzIsNnoiIGZpbGw9IiMzNzQ3NEYiLz48L2c+PC9nPjwvZz48L2c+PGc+PGc+PGc+PGc+PHBhdGggZD0iTTMyLDU1QzE5LjMxNyw1NSw5LDQ0LjY4Myw5LDMyUzE5LjMxNyw5LDMyLDlzMjMsMTAuMzE3LDIzLDIzUzQ0LjY4Myw1NSwzMiw1NXogTTMyLDEwICAgICAgICAgIGMtMTIuMTMxLDAtMjIsOS44NjktMjIsMjJzOS44NjksMjIsMjIsMjJzMjItOS44NjksMjItMjJTNDQuMTMxLDEwLDMyLDEweiIgZmlsbD0iIzM3NDc0RiIvPjwvZz48L2c+PC9nPjwvZz48L2c+PC9nPjwvZz48Zz48Zz48Zz48Zz48Zz48cGF0aCBkPSJNMzEuOTk3LDE1LjUybDkuOTI5LDE2LjQ3NWwtOS45MjksNS44NjlsLTkuOTMtNS44NjlMMzEuOTk3LDE1LjUyTDMxLjk5NywxNS41MnogTTMxLjk5NywxMy41ODIgICAgICAgIEwyMC42OSwzMi4zNDJsMTEuMzA3LDYuNjgzbDExLjMwNi02LjY4M0wzMS45OTcsMTMuNTgyTDMxLjk5NywxMy41ODJ6IiBmaWxsPSIjMzc0NzRGIi8+PC9nPjwvZz48L2c+PC9nPjxnPjxnPjxnPjxnPjxwYXRoIGQ9Ik0zMS45OTcsMjguNjY3bDkuMDg2LDMuODI1bC05LjA4Niw1LjM3MWwtOS4wODctNS4zNzFMMzEuOTk3LDI4LjY2N0wzMS45OTcsMjguNjY3eiBNMzEuOTk3LDI3LjU4MiAgICAgICAgbC0xMS4zMDcsNC43NmwxMS4zMDcsNi42ODNsMTEuMzA2LTYuNjgzTDMxLjk5NywyNy41ODJMMzEuOTk3LDI3LjU4MnoiIGZpbGw9IiMzNzQ3NEYiLz48L2c+PC9nPjwvZz48L2c+PGc+PGc+PGc+PGc+PHBhdGggZD0iTTM5Ljc3OCwzNy43MzJsLTcuNzgsMTAuOTU3bC03Ljc3Ni0xMC45NTdsNy4yNjcsNC4yOTRsMC41MDksMC4zbDAuNTA4LTAuM0wzOS43NzgsMzcuNzMyICAgICAgICBMMzkuNzc4LDM3LjczMnogTTQzLjMxMSwzNC40ODVsLTExLjMxMyw2LjY4bC0xMS4zMDYtNi42OEwzMiw1MC40MTZ2MC4wMDJMNDMuMzExLDM0LjQ4NUw0My4zMTEsMzQuNDg1eiIgZmlsbD0iIzM3NDc0RiIvPjwvZz48L2c+PC9nPjwvZz48L2c+PGc+PGc+PGc+PHJlY3QgZmlsbD0iIzM3NDc0RiIgaGVpZ2h0PSIyMi43OTIiIHdpZHRoPSIxIiB4PSIzMS41IiB5PSIxNS40NjEiLz48L2c+PC9nPjwvZz48Zz48Zz48Zz48cmVjdCBmaWxsPSIjMzc0NzRGIiBoZWlnaHQ9IjcuODc0IiB3aWR0aD0iMSIgeD0iMzEuNSIgeT0iNDEuMzM3Ii8+PC9nPjwvZz48L2c+PC9nPjwvc3ZnPg=="),
		BTC_WALLET("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjwhRE9DVFlQRSBzdmcgIFBVQkxJQyAnLS8vVzNDLy9EVEQgU1ZHIDEuMS8vRU4nICAnaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkJz48c3ZnIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgMCAwIDY0IDY0IiBoZWlnaHQ9IjY0cHgiIGlkPSJMYXllcl8xIiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCA2NCA2NCIgd2lkdGg9IjY0cHgiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPjxnIGlkPSJCaXRjb2luX0JUQyI+PGc+PGc+PGc+PGc+PGc+PGc+PGc+PHBhdGggZD0iTTMyLDU5QzE3LjExMiw1OSw1LDQ2Ljg4OCw1LDMyUzE3LjExMiw1LDMyLDVzMjcsMTIuMTEyLDI3LDI3UzQ2Ljg4OCw1OSwzMiw1OXogTTMyLDYgICAgICAgICAgQzE3LjY2Myw2LDYsMTcuNjYzLDYsMzJzMTEuNjYzLDI2LDI2LDI2czI2LTExLjY2MywyNi0yNlM0Ni4zMzcsNiwzMiw2eiIgZmlsbD0iIzM3NDc0RiIvPjwvZz48L2c+PC9nPjwvZz48Zz48Zz48Zz48Zz48cGF0aCBkPSJNMzIsNTVDMTkuMzE3LDU1LDksNDQuNjgzLDksMzJTMTkuMzE3LDksMzIsOXMyMywxMC4zMTcsMjMsMjNTNDQuNjgzLDU1LDMyLDU1eiBNMzIsMTAgICAgICAgICAgYy0xMi4xMzEsMC0yMiw5Ljg2OS0yMiwyMnM5Ljg2OSwyMiwyMiwyMnMyMi05Ljg2OSwyMi0yMlM0NC4xMzEsMTAsMzIsMTB6IiBmaWxsPSIjMzc0NzRGIi8+PC9nPjwvZz48L2c+PC9nPjwvZz48L2c+PC9nPjxnPjxnPjxnPjxnPjxwYXRoIGQ9Ik0zMS45MTMsNDkuMjI1bC0zLjkyNy0wLjk3OWwxLjIwOS00LjgzNmMtMC40NTYtMC4xMTUtMC45MTgtMC4yMzQtMS4zODgtMC4zNTlMMjYuNTk4LDQ3LjkgICAgICAgbC0zLjkyMi0wLjk3OWwxLjIyMy00LjkxNGMtMS40OTItMC4zNzctNS40Ny0xLjM4LTUuNDctMS4zOGwtMC41NzEtMC4xNDRsMS44NzMtNC4zMTlsMC40MTYsMC4xMSAgICAgICBjMC42NDUsMC4xNzIsMS4zNDksMC4zNTQsMS43NTIsMC40NTNsMC4wMDUtMC4wMTFsMC4zODEsMC4wOTNjMC4wODMsMC4wMiwwLjE2LDAuMDMsMC4yMjksMC4wM2MwLjA5MSwwLDAuMzY3LDAsMC41NDItMC40MzcgICAgICAgbDMuMjk1LTEzLjIzNGMwLjAyNC0wLjU1LTAuMjkzLTAuOTMzLTEuMDEzLTEuMTExbC0wLjMzMS0wLjEwMmMtMC4zODgtMC4xLTEuMTMtMC4yNzYtMS44MDYtMC40MzFsLTAuNDk3LTAuMTEzbDEuMDMtNC4xMzkgICAgICAgbDAuNDg1LDAuMTIxYzAsMCwzLjk5NywxLDUuNDc0LDEuMzYxbDEuMjEzLTQuODU5bDMuOTIzLDAuOThsLTEuMTg0LDQuNzQ4YzAuNDY2LDAuMTA2LDAuOTMzLDAuMjE1LDEuMzk0LDAuMzI2bDEuMTc5LTQuNzI5ICAgICAgIGwzLjkyOCwwLjk3OUwzOC45MjYsMjEuMWMyLjkzMSwxLjA2Niw2LjQ1NCwyLjk3OCw1LjgyNSw3LjE5MWMtMC4zMjMsMi4xOC0xLjM1NCwzLjY1NS0zLjA2OSw0LjQgICAgICAgYzIuNDQ3LDEuNjY5LDMuMDk2LDQuMDM0LDEuOTg2LDcuMmMtMS4xMzIsMy4yNC0zLjM2Nyw0Ljc0OS03LjAzMyw0Ljc0OWMtMS4wMDksMC0yLjE1NS0wLjExNC0zLjQ5Mi0wLjM0OEwzMS45MTMsNDkuMjI1eiAgICAgICAgTTI5LjE5OSw0Ny41MTdsMS45ODYsMC40OTVsMS4yMTYtNC44NzRsMC40NjQsMC4wODhjMS40NzcsMC4yNzksMi43MSwwLjQxNSwzLjc3MSwwLjQxNWMzLjIyNSwwLDUuMTAzLTEuMjU4LDYuMDg5LTQuMDc5ICAgICAgIGMxLjA4MS0zLjA4NCwwLjMxNy01LjEwNi0yLjQ3NC02LjU2bC0xLjIwMi0wLjYyNWwxLjMyLTAuMzA2YzEuOTU5LTAuNDUyLDMuMDY5LTEuNzM3LDMuMzkzLTMuOTI4ICAgICAgIGMwLjQyNS0yLjg0NS0xLjI5OS00Ljc3Mi01LjU5My02LjI1MWwtMC40MzQtMC4xNDlsMS4yMDEtNC44MTRsLTEuOTg2LTAuNDk0bC0xLjE3OSw0LjcyOGwtMC40ODUtMC4xMjEgICAgICAgYy0wLjc3My0wLjE5Mi0xLjU2OC0wLjM3NS0yLjM1Ny0wLjU1NWwtMC40OTktMC4xMTJsMS4xOS00Ljc2OWwtMS45ODMtMC40OTZsLTEuMjA5LDQuODQ1bC0wLjQ3OC0wLjEwOSAgICAgICBjLTAuNTI0LTAuMTItNC4xNTYtMS4wMjYtNS40ODQtMS4zNThsLTAuNTQyLDIuMTc4YzAuOTA5LDAuMjA5LDEuMzk3LDAuMzI2LDEuNjY2LDAuNDI1YzEuMTc4LDAuMjk2LDEuODE1LDEuMTA4LDEuNzQ5LDIuMjMgICAgICAgbC0zLjMzMSwxMy4zOTJjLTAuMzUxLDAuODktMS4wNTQsMS4yNy0xLjkwNSwxLjA4MmMtMC4xMTctMC4wMTUtMC40NTQtMC4wODgtMS43OTYtMC40NDFsLTEuMDYyLDIuNDUgICAgICAgYzEuMzgyLDAuMzQ4LDQuODM1LDEuMjE4LDUuMzg0LDEuMzU4bDAuNDgyLDAuMTIzbC0xLjIyMiw0LjkwOWwxLjk4MSwwLjQ5NWwxLjIxNS00Ljg3bDAuNDkzLDAuMTM1ICAgICAgIGMwLjgwMywwLjIxOSwxLjU4MywwLjQxOCwyLjM0NiwwLjYwOWwwLjQ4NSwwLjEyMkwyOS4xOTksNDcuNTE3eiBNMzMuODY2LDQwLjM1OGMtMS44MDIsMC0zLjgxOC0wLjUzMy01LjAyMi0wLjg1MyAgICAgICBsLTEuMDQ1LTAuMjY3bDEuODY1LTcuNDc3bDAuNDg1LDAuMTIxYzAuMTk4LDAuMDUsMC40NCwwLjEwNCwwLjcxNSwwLjE2NmMyLjA2MywwLjQ2Miw1LjUxNSwxLjIzNiw2Ljc3MiwzLjI3MyAgICAgICBjMC40NTksMC43NDQsMC41NzgsMS41OCwwLjM1MywyLjQ4NUMzNy42OTgsMzguOTcyLDM2Ljc0OCw0MC4zNTgsMzMuODY2LDQwLjM1OHogTTI5LjAwOSwzOC41MTZsMC4wOSwwLjAyMyAgICAgICBjMS4xNTcsMC4zMDcsMy4wOTQsMC44MTksNC43NjcsMC44MTljMS44MiwwLDIuODUyLTAuNTg2LDMuMTUyLTEuNzkyYzAuMTYtMC42NDMsMC4wODQtMS4yMDUtMC4yMzMtMS43MTggICAgICAgYy0xLjAzNi0xLjY3OS00LjM1Ni0yLjQyNC02LjE0LTIuODIzYy0wLjA4Ny0wLjAyLTAuMTcxLTAuMDM5LTAuMjUxLTAuMDU3TDI5LjAwOSwzOC41MTZ6IE0zNS4xOSwzMC42OTEgICAgICAgYy0xLjQ3NiwwLTMuMDMtMC40MTQtNC4wNTgtMC42ODhsLTAuOTY5LTAuMjQ2bDEuNzE0LTYuODc0bDAuNDg1LDAuMTIxYzAuMTY3LDAuMDQyLDAuMzcsMC4wODcsMC42MDIsMC4xMzggICAgICAgYzEuNjksMC4zNzUsNC41MiwxLjAwMiw1LjYxMywyLjc2OGMwLjQzNCwwLjcsMC41NDMsMS41LDAuMzI1LDIuMzc2QzM4LjYyNywyOS4zODMsMzcuNzYxLDMwLjY5MSwzNS4xOSwzMC42OTF6ICAgICAgICBNMzEuMzc0LDI5LjAzM2wwLjAxNSwwLjAwNWMwLjk3OCwwLjI2LDIuNDU2LDAuNjUzLDMuODAxLDAuNjUzYzIuMDg4LDAsMi41NjQtMC45NCwyLjc0MS0xLjY0NyAgICAgICBjMC4xNTItMC42MTMsMC4wODUtMS4xMzktMC4yMDUtMS42MDdjLTAuODcxLTEuNDA2LTMuNTQzLTEuOTk5LTQuOTc5LTIuMzE3Yy0wLjA0Ny0wLjAxMS0wLjA5NC0wLjAyMS0wLjE0LTAuMDMxICAgICAgIEwzMS4zNzQsMjkuMDMzeiIgZmlsbD0iIzM3NDc0RiIvPjwvZz48L2c+PC9nPjwvZz48L2c+PC9zdmc+");
		
		public String icon;
		
	    private WalletType(String icon) {
			this.icon=icon;
		}
	}
	
	
	private static final long serialVersionUID = -1287124660465443912L;
	
	private String private_key;
	
	@Enumerated(EnumType.STRING)
	private WalletType type;
	
	
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public WalletType getType() {
		return type;
	}
	public void setType(WalletType type) {
		this.type = type;
	}
	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return this.type!=null ? this.type.icon:null;
	}
	
	
}

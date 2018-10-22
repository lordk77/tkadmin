package io.ticketcoin.oauth.handler;

import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;

public class TkAdminLoginHandler implements ResourceOwnerLoginHandler{

	@Override
	public UserSubject createSubject(String name, String password) {
		// TODO Auto-generated method stub
		return new UserSubject(name);
	}

}

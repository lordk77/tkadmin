package io.ticketcoin.oauth.handler;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;

import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;

public class TkAdminLoginHandler implements ResourceOwnerLoginHandler{

	@Override
	public UserSubject createSubject(Client client, String name, String password){
		User user = new UserService().getUser(name);
		 
		if (user!=null && user.getPassword().equals(UserService.hashPassword(password)))
			return new UserSubject(name);
		else 
			return null;
	}

}

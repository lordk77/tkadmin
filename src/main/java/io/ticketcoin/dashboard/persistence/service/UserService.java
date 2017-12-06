package io.ticketcoin.dashboard.persistence.service;

import io.ticketcoin.dashboard.persistence.model.User;

public class UserService extends GenericService<User>{

	public UserService() {
		super(User.class);
	}

}

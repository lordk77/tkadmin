package io.ticketcoin.dashboard.persistence.dao;

import io.ticketcoin.dashboard.persistence.model.User;

public class UserDAO extends GenericDAO<User>{

	public UserDAO() {
		super(User.class);
	}


}

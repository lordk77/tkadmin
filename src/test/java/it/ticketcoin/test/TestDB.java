package it.ticketcoin.test;

import io.ticketcoin.dashboard.persistence.service.UserService;

public class TestDB {

	public static void main(String[] args){
		
		
		new UserService().findAll();
	}
}

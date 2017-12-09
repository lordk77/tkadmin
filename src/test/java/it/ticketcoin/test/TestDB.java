package it.ticketcoin.test;

import java.util.ArrayList;

import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.RoleService;
import io.ticketcoin.dashboard.persistence.service.UserService;

public class TestDB {

	public static void main(String[] args){
		
		UserService us = new UserService();
		//verfies the starter data
		User user = us.getUser("admin");
		if(user==null)
		{
			RoleService rd= new RoleService();
			Role adminRole = rd.findByName("admin");
			if(adminRole==null)
			{
				adminRole=rd.saveOrUpdate(new Role("admin"));
				rd.saveOrUpdate(new Role("user"));
			}
			
			user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.setRoles(new ArrayList<Role>());
			user.getRoles().add(adminRole);
			us.saveOrUpdate(user);
		}
	}
}

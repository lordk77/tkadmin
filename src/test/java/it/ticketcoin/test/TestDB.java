package it.ticketcoin.test;

import java.util.ArrayList;

import io.ticketcoin.dashboard.persistence.model.Organization;
import io.ticketcoin.dashboard.persistence.model.Role;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.GenericService;
import io.ticketcoin.dashboard.persistence.service.RoleService;
import io.ticketcoin.dashboard.persistence.service.UserService;

public class TestDB {

	public static void main(String[] args){
		
		UserService us = new UserService();
//		EventCategoryService ecs = new EventCategoryService();
		
		//verfies the starter data
		User user = us.getUser("admin");
		if(user==null)
		{
			RoleService rd= new RoleService();
			GenericService<Organization> organizationService = new GenericService<Organization>(Organization.class);
			Organization organization = new Organization("TicketCoin");
			organizationService.saveOrUpdate(organization);
			
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
			user.setOrganization(organization);
			us.saveOrUpdate(user);
			
		}
//		if(ecs.findAll().size()==0)
//		{
//			{
//				ecs.saveOrUpdate(new EventCategory("&#x1F3A8;","Art"));
//				ecs.saveOrUpdate(new EventCategory("&#x1F3AD;","Theatre"));
//				ecs.saveOrUpdate(new EventCategory("&#x1F3B6;","Music"));
//				ecs.saveOrUpdate(new EventCategory("&#x1F3C0;","Sport"));
//				ecs.saveOrUpdate(new EventCategory("&#x1F3E6;","Museum"));
//			}
//			
//		}
	}
}

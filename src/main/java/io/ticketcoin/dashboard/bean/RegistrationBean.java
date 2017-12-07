package io.ticketcoin.dashboard.bean;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import io.ticketcoin.dashboard.persistence.model.Address;
import io.ticketcoin.dashboard.persistence.model.Organiztion;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.GenericService;
import io.ticketcoin.dashboard.persistence.service.UserService;
 
@ManagedBean
public class RegistrationBean implements Serializable {
 
    private User user = new User();
    private Organiztion organization= new Organiztion();
    
    public RegistrationBean()
    {
    	 	user = new User();
    	    organization= new Organiztion();
    	    organization.setAddress(new Address());
    }
     
    public Organiztion getCompany() {
		return organization;
	}

	public void setCompany(Organiztion company) {
		this.organization = company;
	}

	private boolean skip;
     
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
     
    public void save() {        
    	
    		try 
    		{
    			new GenericService<Organiztion>(Organiztion.class).saveOrUpdate(organization);
    			new UserService().saveOrUpdate(user);

    			FacesMessage msg = new FacesMessage("Successful", "Welcome :" + organization.getName());
    	        FacesContext.getCurrentInstance().addMessage(null, msg);
    		}
		catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Error", e.getMessage());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
    		
    }
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     

	public Organiztion getOrganization() {
		return organization;
	}

	public void setOrganization(Organiztion organization) {
		this.organization = organization;
	}
}
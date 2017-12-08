package io.ticketcoin.dashboard.bean;
import java.io.Serializable;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import io.ticketcoin.dashboard.persistence.model.Address;
import io.ticketcoin.dashboard.persistence.model.Organization;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.GenericService;
import io.ticketcoin.dashboard.persistence.service.UserService;
 
@ManagedBean
public class RegistrationBean implements Serializable {
 
    private User user = new User();
    private Organization organization= new Organization();
    private Boolean usernameVerified=null;
    
    
    public RegistrationBean()
    {
    		resetFields();

    }
     
    public Organization getCompany() {
		return organization;
	}

	public void setCompany(Organization company) {
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
    			//Sets the same email of the company
    			this.user.setEmail(this.organization.getEmail());
    			
    			if(!new UserService().verifyUsername(this.getUser().getUsername().trim()))
    			{
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already used", "Username already used"));
    			}
    			else if(!new UserService().verifyEmail(this.getUser().getUsername().trim()))
    			{
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already used", "Email already used"));
    			}	
    			else
    			{
	    			new GenericService<Organization>(Organization.class).saveOrUpdate(organization);
	    			user.setEmailVerificationCode(UUID.randomUUID().toString());
	    			new UserService().saveOrUpdate(user);
	
	    			resetFields();
	    			
	    			FacesMessage msg = new FacesMessage("Successful", "Please chech your email for verificaton");
	    	        FacesContext.getCurrentInstance().addMessage(null, msg);
    			}
    		}
		catch (Exception e) 
    		{
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Error", e.getMessage());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
    		
    }
    public void verifyUsername()
    {
    		if(this.user.getUsername()==null || this.getUser().getUsername().trim().equals(""))
    			usernameVerified=null;
    		else 
    			usernameVerified = this.getUser().getUsername().trim().length()>3 &&  new UserService().verifyUsername(this.getUser().getUsername().trim());
    }
    
    public void resetFields()
    {
	 	user = new User();
	    organization= new Organization();
	    organization.setAddress(new Address());
    }
 
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Boolean getUsernameVerified() {
		return usernameVerified;
	}

	public void setUsernameVerified(Boolean usernameVerified) {
		this.usernameVerified = usernameVerified;
	}
}
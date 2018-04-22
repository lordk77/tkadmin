package io.ticketcoin.dashboard.bean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.UserService;
 
@ManagedBean
@SessionScoped
public class UserBean {
     
    private String username;
    private String password;
    private User loggedUser;
    
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
   
    public String login() {

        boolean loggedIn=false;
        if(username != null)
        {
	        try 
	        {
	        		//integration with container security 
//	        		((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).login(username, password);
	        		
	        		//reads user data from db
	        		if ((this.loggedUser= new UserService().getUser(username) )!=null) 
	        			loggedIn=true;
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        if(loggedIn)
        {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username));
            return "/admin/index.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials"));
            return null;
        }
        	 
         
    }
    
    public String logout()
    {
    		this.loggedUser=null;
		((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().invalidate();
		return "/index.xhtml";
    }
    

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}   
}
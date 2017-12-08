package io.ticketcoin.dashboard.bean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
 
import org.primefaces.context.RequestContext;

import io.ticketcoin.dashboard.persistence.model.User;
 
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
        RequestContext context = RequestContext.getCurrentInstance();

        if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username));
            return "/admin/login.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials"));
            return null;
        }
         
    }

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}   
}
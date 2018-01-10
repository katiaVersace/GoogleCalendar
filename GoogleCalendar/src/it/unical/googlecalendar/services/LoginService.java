package it.unical.googlecalendar.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.User;

@Service
public class LoginService {

	@Autowired
	private UserDAOImpl udao;
	
	@PostConstruct
	public void initialize() {
		
		udao.save(new User("ciccio@c.it","ciccio", "ciccio"));
		udao.save(new User("pippo@p.it","pippo", "pippo"));
		udao.save(new User("mario@m.it","mario", "mario"));
	
		
	}
	
	public boolean loginAttempt(String email, String password) {
		if(!udao.existsUser(email,password)) {
			return false;
		}
		return true;
		
	}
	
	
	
	public boolean registrationAttempt(String email, String username, String password) {
		if (!email.equals("") && !username.equals("")&& !password.equals(""))
	              	if(udao.save(new User(email,username,password))) return true;
	
		return false;
			
		
		
	}
}

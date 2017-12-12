package it.unical.myblog.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

	private Map<String, String> credentials = new HashMap<>();
	
	@PostConstruct
	public void initialize() {
		credentials.put("ciccio", "ciccio");
		credentials.put("pippo", "pippo");
		credentials.put("mario", "mario");
	}
	
	public boolean loginAttempt(String username, String password) {
		if(!credentials.containsKey(username)) {
			return false;
		}
		return credentials.get(username).equals(password);
		
	}
	
	
	
	public boolean registrationAttempt(String username, String password) {
		
		return credentials.put(username, password)==null;
			
		
		
	}
}

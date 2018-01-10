package it.unical.googlecalendar.dao;

import java.util.List;


import it.unical.googlecalendar.model.User;


public interface UserDAO {
	
	boolean save(User User);

	List<User> getAllUsers();
	public boolean existsUser(String email, String password);
	
}

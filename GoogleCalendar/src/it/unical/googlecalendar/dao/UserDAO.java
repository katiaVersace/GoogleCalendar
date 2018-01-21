package it.unical.googlecalendar.dao;

import java.util.Collection;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;


public interface UserDAO {
	
	boolean save(User User);

	List<User> getAllUsers();
	public boolean existsUser(String email, String password);
	public String getUsernameByEmail(String email);
	
	boolean update(User user);

	boolean updateUserById(User u, String username, String password);

	User getUserById(int u_id);

	User getUserByEmail(String email);

	List<String> searchEmail(String email);

	
	
	
}

package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;


public interface UserDAO {
	
	boolean save(User User);

	List<User> getAllUsers();
	public String existsUser(String email, String password);
	public String getUsernameByEmail(String email);
	
	boolean update(User user);

	String updateUserById(int u_id, String username, String oldPassword,String password);

	User getUserById(int u_id);

	User getUserByEmail(String email);
 
	List<String> searchEmail(String email,String personalEmail);
	
	boolean existsUserFB(String email);

	int insertNewUser(String email, String username, String password);

	Calendar getFbCalendar(int user_id);	
} 
package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.User;


public interface UserDAO {
	
	void save(User User);

	List<User> getAllUsers();
	
}

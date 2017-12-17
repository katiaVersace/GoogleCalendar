package it.unical.googlecalendar.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.User;


public class UserDAOTest {
	
	@Test
	public void saveTest() {
		UserDAO dao = UserDAOImpl.getInstance();
		User pippo = new User("pippo", "1234");
		dao.save(new User("ciccio", "5678"));
		dao.save(pippo);
		List<User> allUsers = dao.getAllUsers();
		
		Assert.assertTrue(allUsers.contains(pippo));
	}

}

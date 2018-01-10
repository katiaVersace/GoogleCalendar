package it.unical.googlecalendar.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.unical.googlecalendar.configuration.AppConfiguration;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class UserDAOTest {
	
	@Autowired
	private UserDAOImpl dao;
	
	@Test
	public void saveTest() {
		
		User pippo = new User("pippo@p.it","pippo", "1234");
		dao.save(new User("ciccio@c.it","ciccio", "5678"));
		dao.save(pippo);
		List<User> allUsers = dao.getAllUsers();
		
		Assert.assertTrue(allUsers.contains(pippo));
	}

}

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
		
		User pippo = new User("pippo77@p.it","pippo", "1234");
		dao.save(pippo);
		//Non puo salvare due utenti con la stessa email!!
		//Assert.assertTrue((!dao.save(new User("pippo77@p.it","ciccio", "5678"))));
			
		
		
		
		List<User> allUsers = dao.getAllUsers();
//		for(User u:allUsers){
//			System.out.println("Utente "+u.getUsername()+" "+u.getEmail());
//		}
//		
		Assert.assertTrue(allUsers.contains(pippo));
	}

}

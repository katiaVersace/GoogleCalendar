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
public class SearchTest {
	
	@Autowired
	private UserDAOImpl dao;
	
	@Test
	public void saveTest() {
		
		User pippo = new User("pippo89@p.it","pippo1", "1234");
		dao.save(pippo);
		User kat = new User("tka77@p.it","katy", "1234");
		dao.save(kat);
		User kate = new User("tkate77@p.it","katy2", "1234");
		dao.save(kate);
		
		
//		List<String> resultSearch = dao.searchEmail("ka");
//		System.out.println("Risultato: ");
//		for(String u:resultSearch){
//			System.out.println("risultato "+u);
//		}
		
		
	}

}

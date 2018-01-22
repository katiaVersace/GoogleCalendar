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
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class insertNewCalendarTest {
	
	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private CalendarDAOImpl cdao;
	
	@Test
	public void saveTest() {
		
		User pippo = new User("pippo56@p.it","pippo", "1234");
		udao.save(pippo);
		int c1_id=cdao.insertNewCalendar(pippo.getId(), "c1", "descr 1");
		int c2_id=cdao.insertNewCalendar(pippo.getId(), "c2", "descr 2");
		int c3_id=cdao.insertNewCalendar(pippo.getId(), "c3", "descr 3");
		
		//System.out.println("c1: "+c1_id+" c2: "+c2_id+" c3: "+c3_id);
	}

}

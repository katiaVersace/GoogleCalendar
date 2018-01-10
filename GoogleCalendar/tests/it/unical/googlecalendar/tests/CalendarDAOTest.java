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
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class CalendarDAOTest {
	
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private UserDAOImpl udao;
	
	@Test
	public void saveTest() {
		
			
		User katia=new User("k@j.it","Katia","1234");		
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
		Calendar katiaCalendar2 = new Calendar(katia,"katia's Calendar 2", "second list of katia's events");
		
		
		udao.save(katia);
		cdao.save(katiaCalendar);
		cdao.save(katiaCalendar2);
		
		List<Calendar> allCalendars = cdao.getAllCalendars();
		System.out.println("size of calendars"+allCalendars.size());
		for(Calendar c:allCalendars){
			System.out.println(c.getTitle());
			
		}
		Assert.assertTrue(allCalendars.contains(katiaCalendar));
		Assert.assertTrue(cdao.getCalendarsByUser(katia).size()==2);
		
		//stampa tutti i calendari di Katia
		for(Calendar c:cdao.getCalendarsByUser(katia)){
			System.out.println(c.getTitle());
		}
	}

}

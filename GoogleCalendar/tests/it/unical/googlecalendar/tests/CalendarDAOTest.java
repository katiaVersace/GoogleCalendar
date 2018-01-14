package it.unical.googlecalendar.tests;

import java.awt.Color;
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
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class CalendarDAOTest {
	
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private Users_CalendarsDAOImpl ucdao;
	
	@Test
	public void saveTest() {
		
		
		User ciccino=new User("c@j.it","ciccino","1234");	
		Calendar ciccinoCalendar = new Calendar(ciccino,"ciccino's Calendar", "list of ciccino's events");
		Calendar ciccinoCalendar2 = new Calendar(ciccino,"ciccino's Calendar2", "list2 of ciccino's events");
		
		Users_Calendars uciccino=new Users_Calendars(ciccino,ciccinoCalendar,"ADMIN",  Color.black, ciccinoCalendar.getTitle());
		Users_Calendars uciccino2=new Users_Calendars(ciccino,ciccinoCalendar2,"ADMIN",  Color.black, ciccinoCalendar2.getTitle());
		ucdao.save(uciccino);
		ucdao.save(uciccino2);
		
		List<Calendar> allCalendars = cdao.getAllCalendars();
		//System.out.println("size of calendars"+allCalendars.size());
		for(Calendar c:allCalendars){
			System.out.println(c.getTitle());
			
		}
		//Assert.assertTrue(allCalendars.contains(katiaCalendar));
		System.out.println("size "+ucdao.getAllAssociation().size());
		//Assert.assertTrue(cdao.getCalendarsByUser(katia).size()==2);
		
		//stampa tutti i calendari di Katia
//		for(Calendar c:cdao.getCalendarsByUser(katia)){
//			System.out.println(c.getTitle());
//		}
	}

}

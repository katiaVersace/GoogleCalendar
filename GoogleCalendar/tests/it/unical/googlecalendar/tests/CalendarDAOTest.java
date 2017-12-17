package it.unical.googlecalendar.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.unical.googlecalendar.dao.CalendarDAO;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;


public class CalendarDAOTest {
	
	@Test
	public void saveTest() {
		
			
		CalendarDAO cdao = CalendarDAOImpl.getInstance();
		UserDAO udao = UserDAOImpl.getInstance();
		User katia=new User("Katia","1234");		
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

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
		
			
		//User katia=new User("k@j.it","Katia","1234");		
		//Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
		//Calendar katiaCalendar2 = new Calendar(katia,"katia's Calendar 2", "second list of katia's events");
//		Users_Calendars uc=new Users_Calendars(katia, katiaCalendar,"ADMIN",  Color.black, katiaCalendar.getTitle());
//		Users_Calendars uc2=new Users_Calendars(katia, katiaCalendar2,"ADMIN",  Color.black, katiaCalendar2.getTitle());
//		
//		ucdao.save(uc);
//		ucdao.save(uc2);
//		
		User ciccio=new User("c@j.it","ciccio","1234");	
		Calendar ciccioCalendar = new Calendar(ciccio,"ciccio's Calendar", "list of ciccio's events");
		Calendar ciccioCalendar2 = new Calendar(ciccio,"ciccio's Calendar2", "list2 of ciccio's events");
		
		Users_Calendars uciccio=new Users_Calendars(ciccio,ciccioCalendar,"ADMIN",  Color.black, ciccioCalendar.getTitle());
		Users_Calendars uciccio2=new Users_Calendars(ciccio,ciccioCalendar2,"ADMIN",  Color.black, ciccioCalendar2.getTitle());
		ucdao.save(uciccio);
		ucdao.save(uciccio2);
		
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

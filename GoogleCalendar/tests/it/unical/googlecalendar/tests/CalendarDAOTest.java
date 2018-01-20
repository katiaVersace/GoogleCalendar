package it.unical.googlecalendar.tests;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class CalendarDAOTest {
	
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private OccurrenceDAOImpl odao;
	@Autowired
	private Users_CalendarsDAOImpl ucdao;
	
	
	@Test
	public void saveTest() {
		
		
		User ciccino=new User("c@j.it","ciccino","1234");	
		udao.save(ciccino);
		Calendar ciccinoCalendar = new Calendar(ciccino,"ciccino's Calendar", "list of ciccino's events");
		Calendar ciccinoCalendar2 = new Calendar(ciccino,"ciccino's Calendar2", "list2 of ciccino's events");
		cdao.save(ciccinoCalendar);
		cdao.save(ciccinoCalendar2);
		
		
		//ora creo un evento e lo associo al mio calendario
				SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
				String dateInString = "21-01-2018 10:20:56";
				String dateInString2 = "24-01-2018 16:20:00";
				//int minutes=5;
		Occurrence ev3=null;
		Occurrence ev1=null;
		try {
			
			ev3=new Occurrence(ciccinoCalendar,ciccino,"memo di ciccio","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),Color.black.toString(),Color.BLUE.toString());
			odao.save(ev3);
		//	System.out.println("id ev3: s"+ev3.getId());
			
			
			ev1=new Occurrence(ciccinoCalendar,ciccino,"memo 1 di ciccio","Ricordati di comprare il 2latte",sdf.parse(dateInString),sdf.parse(dateInString2),Color.black.toString(),Color.BLUE.toString());
			odao.save(ev1);
			//System.out.println("id ev1: "+ev1.getId());
			

			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
//	cdao.save(ciccinoCalendar);
		
		
		
		//System.out.println("size of calendars"+allCalendars.size());
		List<Calendar> allCalendars = cdao.getAllCalendars();
		Assert.assertTrue(allCalendars.contains(ciccinoCalendar));
//		
//		System.out.println("Occurrences di ciccinoCalendar:");
//		for(Occurrence o:ciccinoCalendar.getOccurrences())
//			System.out.println(o.getTitle());
//		
		Assert.assertTrue(ciccinoCalendar.getOccurrences().contains(ev1));
		Assert.assertTrue(cdao.getCalendarsByEmail(ciccino.getEmail()).size()==2);
		
		Assert.assertTrue(odao.getOccurrencesByCalendar(ciccinoCalendar).size()==2);
		
		Assert.assertTrue(cdao.deleteById(ciccinoCalendar,ciccino));
			
		
		
		
	}

}

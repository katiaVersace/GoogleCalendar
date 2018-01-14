package it.unical.googlecalendar.tests;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.unical.googlecalendar.configuration.AppConfiguration;
import it.unical.googlecalendar.dao.CalendarDAO;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class OccurrenceDAOTest {
	
	@Autowired
	private Users_CalendarsDAOImpl ucdao;
	@Autowired
	private OccurrenceDAOImpl odao;
	@Autowired
	private CalendarDAOImpl cdao;
	
	@Test
	public void saveTest() {
		
	
		User katia=new User("katia.versace@hotmail.it","Katia","1234");	
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
		User ciccio=new User("ciccio.h@hotmail.it","Ciccio","1234");
		Calendar ciccioCalendar = new Calendar(ciccio,"ciccio's Calendar", "list of ciccio's events");
		
		Users_Calendars uc=new Users_Calendars(katia, katiaCalendar,"ADMIN",  Color.black, katiaCalendar.getTitle());
		Users_Calendars uc2=new Users_Calendars(ciccio, ciccioCalendar,"ADMIN",  Color.black, ciccioCalendar.getTitle());

		
		//ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		Occurrence memo3=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),minutes * 60000);
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),minutes * 60000);
			memo3=new Memo(ciccioCalendar,ciccio,"memo di ciccio",sdf.parse(dateInString2),minutes * 60000);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
	
		ucdao.save(uc);
		ucdao.save(uc2);
		odao.save(memo1);
		odao.save(memo2);
		odao.save(memo3);
    	
		 Users_Calendars uc3=ciccioCalendar.sendInvitationToCalendar(ciccio,katia, "ADMIN");
			ucdao.save(uc3);
				
		Assert.assertTrue(odao.getOccurrencesByCalendar(katiaCalendar).contains(memo2));
		
			Assert.assertTrue(odao.getOccurrencesByEmail(katia.getEmail()).size()==3);
			
		
	}

}

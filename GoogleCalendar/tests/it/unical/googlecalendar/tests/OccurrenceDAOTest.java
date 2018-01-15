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
import it.unical.googlecalendar.dao.InvitationDAOImpl;
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
	private UserDAOImpl udao;
	@Autowired
	private OccurrenceDAOImpl odao;
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private InvitationDAOImpl idao;
	
	@Test
	public void saveTest() {
		
	
		User katia=new User("katia.versace@hotmail.it","Katia","1234");	
		User ciccio=new User("ciccio.h@hotmail.it","Ciccio","1234");
    	udao.save(katia);
    	udao.save(ciccio);
    	Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
		Calendar ciccioCalendar = new Calendar(ciccio,"ciccio's Calendar", "list of ciccio's events");
		cdao.save(ciccioCalendar);
    	cdao.save(katiaCalendar);
		
		
		
		//ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
	//	int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		Occurrence memo3=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),"Ricordati di comprare il latte");
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),"Ricordati di comprare il pane");
			memo3=new Memo(ciccioCalendar,ciccio,"memo di ciccio",sdf.parse(dateInString2),"Ricordati di comprare qualcosa");
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
	
		odao.save(memo1);
		odao.save(memo2);
		odao.save(memo3);
    	
		
		idao.sendInvitation(ciccio.getId(),katia.getEmail(), ciccioCalendar.getId(),"ADMIN");
		
			cdao.save(ciccioCalendar);
			udao.save(katia);
				
		Assert.assertTrue(odao.getOccurrencesByCalendar(ciccioCalendar).contains(memo3));
		
		Assert.assertTrue(odao.getOccurrencesByEmail(katia.getEmail()).size()==3);
			
		
	}

}

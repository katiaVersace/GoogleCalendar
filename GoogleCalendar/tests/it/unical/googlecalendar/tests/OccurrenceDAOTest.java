package it.unical.googlecalendar.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class OccurrenceDAOTest {
	
	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private OccurrenceDAOImpl odao;
	
	@Test
	public void saveTest() {
		
	
		User katia=new User("Katia","1234");	
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
				
		
		
		//ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),minutes * 60000);
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),minutes * 60000);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
		
		
		udao.save(katia);
		cdao.save(katiaCalendar);
		odao.save(memo1);
		odao.save(memo2);
    	
		
		Assert.assertTrue(odao.getOccurrencesByCalendar(katiaCalendar).contains(memo2));
		
		//stampa tutti i promemoria del calendario di Katia
		for(Occurrence o:odao.getOccurrencesByCalendar(katiaCalendar)){
			System.out.println(o.getTitle());
		}
			
		
	}

}

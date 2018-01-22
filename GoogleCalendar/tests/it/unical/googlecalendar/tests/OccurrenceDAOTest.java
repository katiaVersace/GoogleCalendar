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
import it.unical.googlecalendar.model.Invitation;
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
		
	
		User katia=new User("katia2.versace@hotmail.it","Katia","1234");	
		User ciccio=new User("ciccio3.h@hotmail.it","Ciccio","1234");
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
			//System.out.println("Ciccio calendar id "+ciccioCalendar.getId());
			memo1=new Occurrence(katiaCalendar,katia,"Comprare il latte","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),Color.black.toString(),Color.BLUE.toString());
			memo2=new Occurrence(katiaCalendar,katia,"Comprare il pane","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),Color.black.toString(),Color.BLUE.toString());
			memo3=new Occurrence(ciccioCalendar,ciccio,"memo di ciccio","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),Color.black.toString(),Color.BLUE.toString());
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
	
		odao.save(memo1);
		odao.save(memo2);
		odao.save(memo3);
    	
		idao.sendInvitation(ciccio.getId(),katia.getEmail(), ciccioCalendar,"ADMIN");
		Invitation i=idao.getInvitationByCalendarAndReceiver(katia.getId(), ciccioCalendar.getId());
		//System.out.println("Ciccio calendar id dopo send invitation "+ciccioCalendar.getId());
		idao.acceptInvitation(katia,ciccioCalendar);
			cdao.update(ciccioCalendar);
			udao.update(katia);
			
		//	System.out.println("Sto cercando gli eventi del calendario con id: "+ciccioCalendar.getId());

			List<Occurrence> occList=odao.getOccurrencesByCalendar(ciccioCalendar);
//			System.out.println("Eventi del calendario di ciccio: "+occList.size());
//			for(Occurrence o:occList){
//				System.out.println(o.getTitle());
//			}
//			List<Occurrence> occ2List=odao.getAllOccurrences();
//			System.out.println("Tutti gli Eventi : "+occ2List.size());
//			for(Occurrence o:occ2List){
//				System.out.println(o.getTitle()+" Id: "+o.getCalendar().getId()+" Start: "+o.getStartTime()+" End: "+o.getEndTime()+" Color1: "+o.getPrimaryColor()+" Color 2"+o.getSecondaryColor());
//			}
			
	List<Calendar> c=cdao.getCalendarsByEmail(katia.getEmail());
	Assert.assertTrue(c.contains(ciccioCalendar));
				
		Assert.assertTrue(occList.contains(memo3));
		
		Assert.assertTrue(odao.getOccurrencesByEmail(katia.getEmail()).size()==3);
			
		
	}

}

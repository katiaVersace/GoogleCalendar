package it.unical.googlecalendar.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import org.junit.Test;

import it.unical.googlecalendar.dao.CalendarDAO;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Event;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public class GuestsTest {
	
	@Test
	public void saveTest() {
		
		//creo gli utenti e un calendario e all'utente katia associo il calendario 
		UserDAO udao = UserDAOImpl.getInstance();
		User katia=new User("Katia","1234");
		User ciccio=new User("Ciccio","9876");
		User pippo=new User("Pippo","7654");
		CalendarDAO cdao = CalendarDAOImpl.getInstance();				
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
				
		
		
		//ora creo un evento e lo associo al mio calendar
		OccurrenceDAO odao=OccurrenceDAOImpl.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		String dateInString3 = "17-01-2018 00:00:00";
		int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		Occurrence compleannoCiccio=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),minutes * 60000);
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),minutes * 60000);
		compleannoCiccio=new Event(katiaCalendar,ciccio,"Il compleanno di ciccio",sdf.parse(dateInString3),"Si terrà una festa presso casa mia il giorno 17 gennaio 2018, siete tutti invitati");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//sto invitando ciccio e pippo (forse la possibilità di invitare qualcuno andrebbe usata solo per gli eventi e non per i promemoria)
		//il metodo addGuest gestisce l'associazione tra Occurrence e User(invito all'evento)
		memo1.addGuest(ciccio);
		memo1.addGuest(pippo);
		
		//sto invitando pippo e katia al compleanno di ciccio
		compleannoCiccio.addGuest(katia);
		compleannoCiccio.addGuest(pippo);
		
		//save users
		udao.save(katia);
		udao.save(ciccio);
		udao.save(pippo);
		
		//save calendars
		cdao.save(katiaCalendar);
		
		//save occurrences
		odao.save(memo1);
		odao.save(memo2);
		odao.save(compleannoCiccio);
    	
		//pippo è invitato a due occurrences
		Assert.assertTrue(odao.getOccurrencesByGuest(pippo).size()==2);
		
		
		//stampa tutti gli invitati al memo1
		System.out.println("stampa tutti gli invitati al memo1");
		for(User u:odao.getGuestsByOccurrence(memo1)){
			System.out.println(u.getUsername());
		}
		
		System.out.println("stampa tutte le Occurrence a cui è invitato pippo");
		//stampa tutte le Occurrence a cui è invitato pippo
				for(Occurrence o:odao.getOccurrencesByGuest(pippo)){
					System.out.println(o.getTitle());
				}
		
	}

}

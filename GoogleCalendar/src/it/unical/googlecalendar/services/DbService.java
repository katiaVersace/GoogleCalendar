package it.unical.googlecalendar.services;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.InvitationDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Comment;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Post;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Service
public class DbService {


	@Autowired
	private OccurrenceDAOImpl odao;
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private InvitationDAOImpl idao;
	
@PostConstruct
	public void initialize() {
		User katia=new User("k@h.it","Katia","1234");	
udao.save(katia);
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
		Calendar katiaCalendar2 = new Calendar(katia,"Calendar n2", "second list of katia's events");
cdao.save(katiaCalendar2);
cdao.save(katiaCalendar);
		
		//ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		//int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),"Ricordati di comprare il latte");
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),"Ricordati di comprare il latte");
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		odao.save(memo1);
		odao.save(memo2);
		
		
		
	}
	

	public Collection<Occurrence> stampaTuttiGliEventi() {
		
		return odao.getAllOccurrences();
	}
	
	//forse modificare in stampaEventiPerUtente e calendari(lista)
	public Collection<Occurrence> stampaEventiPerUtente(String email){
		return odao.getOccurrencesByEmail(email);
	}

	public Collection<Calendar> getCalendarsForEmail(String email) {
		
		return cdao.getCalendarsByEmail(email);
	}


	


	public boolean deleteCalendarById(int calendarId,int user_id) {
		return cdao.deleteById(calendarId,user_id);
	}


	public int insertNewCalendar(int user_id, String title, String description) {
		
		return cdao.insertNewCalendar(user_id, title, description);
		
	}


	public boolean updateCalendarById(int calendar_id, String title, String description, int user_id) {
		return cdao.updateCalendarById(calendar_id, title, description, user_id);
	}


	public int insertNewEvent(int calendar_id, int creator_id, String title, Date data, String description) {
	return odao.insertNewEvent(calendar_id,creator_id, title,  data, description);
	}


	public int insertNewMemo(int calendar_id, int creator_id, String title, Date data, String description) {
		return odao.insertNewMemo(calendar_id,creator_id, title,  data, description);
	}


	public boolean deleteOccurrenceById(int occurrenceId, int user_id) {
		return odao.deleteById(occurrenceId,user_id);
	}


	public boolean updateEventById(int event_id, String title, Date data, String description, int user_id) {
		return odao.updateEventById(event_id, title,data, description, user_id);
	}
	public boolean updateMemoById(int memo_id, String title, Date data, String description, int user_id) {
		return odao.updateMemoById(memo_id, title,data, description,user_id);
	}


	public boolean updateUserById(int user_id, String username, String password) {
		return udao.updateUserById(user_id, username,password);}
	
	public boolean disconnectMeByCalendar(int user_id, int calendarId) {
		return cdao.disconnectUserFromCalendarById(calendarId, user_id);
}


	public boolean sendInvitation(int user_id, String receiver_email, int calendar,String privilege) {
		return idao.sendInvitation(user_id, receiver_email ,calendar,privilege);
	}
	
	
	
	
}



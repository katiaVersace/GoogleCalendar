package it.unical.googlecalendar.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.InvitationDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Event;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;

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
		User katia=new User("k@h.it","Katia2","1234");	
		udao.save(katia);
		Calendar katiaCalendar = new Calendar(katia,"katias's Calendar", "list of katia's events");
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
		Calendar c=cdao.getCalendarById(calendarId);
		User u=udao.getUserById(user_id);
		return cdao.deleteById(c,u);
	}


	public int insertNewCalendar(int user_id, String title, String description) {
		User u=udao.getUserById(user_id);
		return cdao.insertNewCalendar(u, title, description);
		
	}


	public boolean updateCalendarById(int calendar_id, String title, String description, int user_id) {
		Calendar c=cdao.getCalendarById(calendar_id);
				return cdao.updateCalendarById(c, title, description, user_id);
	}


	public int insertNewEvent(int calendar_id, int creator_id, String title, Date data, String description) {
		Calendar c=cdao.getCalendarById(calendar_id);
		User u=udao.getUserById(creator_id);
		
	return odao.insertNewEvent(c,u, title,  data, description);
	}


	public int insertNewMemo(int calendar_id, int creator_id, String title, Date data, String description) {
		Calendar c=cdao.getCalendarById(calendar_id);
		User u=udao.getUserById(creator_id);
				return odao.insertNewMemo(c,u, title,  data, description);
	}


	public boolean deleteOccurrenceById(int occurrenceId, int user_id,int calendar_id) {
		Occurrence c=odao.getOccurrenceById(occurrenceId);
		User u=udao.getUserById(user_id);
		Calendar ca=cdao.getCalendarById(calendar_id);
		return odao.deleteById(c,u,ca);
	}

	public boolean updateEventById(int event_id, String title, Date data, String description, int user_id) {
		Event e=(Event) odao.getOccurrenceById(event_id);		
		return odao.updateEventById(e, title,data, description, user_id);
	}

	public boolean updateMemoById(int memo_id, String title, Date data, String description, int user_id) {
		Memo m=(Memo)odao.getOccurrenceById(memo_id);
		return odao.updateMemoById(m, title,data, description,user_id);
	}

	public boolean updateUserById(int user_id, String username, String password) {
		User u=udao.getUserById(user_id);
		return udao.updateUserById(u, username,password);}
	
	public boolean disconnectMeByCalendar(int user_id, int calendarId) {
		User u=udao.getUserById(user_id);
		Calendar ca=cdao.getCalendarById(calendarId);
		return cdao.disconnectUserFromCalendarById(ca, u);
	}

	public boolean sendInvitation(int user_id, String receiver_email, int calendar_id,String privilege) {
		Calendar ca=cdao.getCalendarById(calendar_id);
		return idao.sendInvitation(user_id, receiver_email ,ca,privilege);
	}

	public List<Calendar> getAllMyCalendars(String email) {
		return cdao.getCalendarsByEmail(email);
	}
}
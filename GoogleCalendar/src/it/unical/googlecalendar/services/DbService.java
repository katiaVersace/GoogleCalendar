package it.unical.googlecalendar.services;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sound.midi.SysexMessage;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.InvitationDAOImpl;
import it.unical.googlecalendar.dao.MemoDAO;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
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
	@Autowired
	private MemoDAO mdao;
	
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
		String leftBoundary = "01-02-2018 12:00:00";
		String rightBoundary = "05-02-2018 12:00:00";
		//int minutes=5;
		Occurrence ev1=null;
		Occurrence ev2=null;
		Occurrence ev3=null;
		Occurrence ev4=null;
		try {
			ev1=new Occurrence(katiaCalendar,katia,"Comprare il latte","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),"#555555","#aaaaaa");
			ev2=new Occurrence(katiaCalendar,katia,"Comprare il pane","Ricordati di comprare il latte",sdf.parse(dateInString),sdf.parse(dateInString2),"#555555","#aaaaaa");
			ev3=new Occurrence(katiaCalendar2,katia,"Ricordati che devi morire","Sii retto, ma non in faccia",sdf.parse(dateInString),sdf.parse(dateInString2),"#555555","#aaaaaa");
			ev4=new Occurrence(katiaCalendar2,katia,"Una mano � solo un piede che non ha mai smesso di sognare","Saggezza",sdf.parse(leftBoundary),sdf.parse(rightBoundary),"#555555","#aaaaaa");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		odao.save(ev1);
		odao.save(ev2);
		odao.save(ev3);
		odao.save(ev4);
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


	public int insertNewEvent(int calendar_id, int creator_id, String title, String description, Date startTime, Date endTime, String c1,String c2) {
		Calendar c=cdao.getCalendarById(calendar_id);
		User u=udao.getUserById(creator_id);
		
	return odao.insertNewEvent(c,u, title, description,startTime,endTime, c1,  c2);
	}


	public int insertNewMemo(int creator_id, String title, Date data, String description, String c1) {
				User u=udao.getUserById(creator_id);
				return mdao.insertNewMemo(u,title,data,description,c1);
	}

	

	public boolean deleteOccurrenceById(int occurrenceId, int user_id,int calendar_id) {
		Occurrence c=odao.getOccurrenceById(occurrenceId);
		User u=udao.getUserById(user_id);
		Calendar ca=cdao.getCalendarById(calendar_id);
		return odao.deleteById(c,u,ca);
	}

	public boolean updateEventById(int event_id, String title, String description, Date startTime, Date endTime, String c1,String c2,int user_id) {
		Occurrence e=(Occurrence) odao.getOccurrenceById(event_id);		
		return odao.updateEventById(e, title,  description,startTime,endTime, c1,  c2, user_id);
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

	public boolean updateMemoById(int memo_id, int user_id, String title, Date data, String description,
			String c1) {
		Memo m=mdao.getMemoById(memo_id);
	return mdao. updateMemoById(m,user_id, title,data, description,c1);

	}

	public boolean deleteMemoById(int memo_id, int user_id) {
		Memo c=mdao.getMemoById(memo_id);
		User u=udao.getUserById(user_id);
	
		return mdao.deleteMemoById(c,u);
	}

	public List<Calendar> getAllMyCalendars(String email) {
		return cdao.getCalendarsByEmail(email);
	}
	
	public List<Occurrence> getMyEventsInPeriod(String email, int calendar_id, String start, String end) {
		return odao.getOccurrenceByEmailInPeriod(email, calendar_id, start, end);
	}
}
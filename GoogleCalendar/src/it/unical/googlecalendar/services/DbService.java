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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unical.googlecalendar.dao.AlarmDAO;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.ExceptionDAO;
import it.unical.googlecalendar.dao.InvitationDAOImpl;
import it.unical.googlecalendar.dao.MemoDAO;
import it.unical.googlecalendar.dao.NotificationDAO;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.RepetitionDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Alarm;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Notification;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Repetition;
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
	@Autowired
	private AlarmDAO adao;
	@Autowired
	private NotificationDAO ndao;
	@Autowired
	private RepetitionDAO rdao;
    @Autowired
    private ExceptionDAO edao;

	@PostConstruct
	public void initialize() {
		// User katia=udao.getUserByEmail("k@h.it");
		User katia = new User("k@h.it", "Katia2", "1234");
		udao.save(katia);
		
		Calendar katiaCalendar = new Calendar(katia, "katia's Calendar", "list of katia's events",false);
		Calendar katiaCalendar2 = new Calendar(katia, "Calendar n2", "second list of katia's events",false);

		cdao.save(katiaCalendar2);
		cdao.save(katiaCalendar);
		Notification n = new Notification(katia, "la mia prima notifica");
		ndao.save(n);
		// ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		String leftBoundary = "03-02-2018 12:00:00";
		String rightBoundary = "10-02-2018 12:00:00";
		// int minutes=5;
		Occurrence ev1 = null;
		Occurrence ev2 = null;
		Occurrence ev3 = null;
		Occurrence ev4 = null;
		try {
			ev1 = new Occurrence(katiaCalendar, katia, "Comprare il latte", "Ricordati di comprare il latte",
					sdf.parse(dateInString), sdf.parse(dateInString2), "#555555", "#aaaaaa");
			ev2 = new Occurrence(katiaCalendar, katia, "Comprare il pane", "Ricordati di comprare il latte",
					sdf.parse(dateInString), sdf.parse(dateInString2), "#555555", "#aaaaaa");
			ev3 = new Occurrence(katiaCalendar2, katia, "Ricordati che devi morire", "Sii retto, ma non in faccia",
					sdf.parse(dateInString), sdf.parse(dateInString2), "#555555", "#aaaaaa");
			ev4 = new Occurrence(katiaCalendar2, katia, "Una mano è solo un piede che non ha mai smesso di sognare",
					"Saggezza", sdf.parse(leftBoundary), sdf.parse(leftBoundary), "#555555", "#aaaaaa");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		odao.save(ev1);
		odao.save(ev2);
		odao.save(ev3);
		odao.save(ev4);
		
		int rep_id;
		
		try {
		    rep_id = rdao.insertNewRepetition(
		            ev4.getId(), "DAY", katia.getId(), sdf.parse("01-02-2018 06:00:00"), sdf.parse("10-02-2018 06:00:00"));
		    Repetition rep_obj = rdao.getRepetitionById(rep_id);
		    edao.insertNewException(
		            rep_obj.getId(), sdf.parse("05-02-2018 12:00:00"), sdf.parse("05-02-2018 12:00:00"), katia.getId());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public Collection<Occurrence> stampaTuttiGliEventi() {

		return odao.getAllOccurrences();
	}

	// forse modificare in stampaEventiPerUtente e calendari(lista)
	public Collection<Occurrence> stampaEventiPerUtente(String email) {
		return odao.getOccurrencesByEmail(email);
	}

	public Collection<Calendar> getCalendarsForEmail(String email) {

		return cdao.getCalendarsByEmail(email);
	}

	public boolean deleteCalendarById(int calendarId, int user_id) {
		return cdao.deleteById(calendarId, user_id);
	}

	public int insertNewCalendar(int user_id, String title, String description) {
		
		return cdao.insertNewCalendar(user_id, title, description);

	}

	public boolean updateCalendarById(int calendar_id, String title, String description, int user_id) {
		return cdao.updateCalendarById(calendar_id, title, description, user_id);
	}

	public int insertNewEvent(int calendar_id, int creator_id, String title, String description, Date startTime,
			Date endTime, String c1, String c2) {
		
		return odao.insertNewEvent(calendar_id, creator_id, title, description, startTime, endTime, c1, c2);
	}

	public int insertNewMemo(int creator_id, String title, Date data, String description, String c1) {
		
		return mdao.insertNewMemo(creator_id, title, data, description, c1);
	}

	public boolean deleteOccurrenceById(int occurrenceId, int user_id) {
	
		return odao.deleteById(occurrenceId, user_id);
	}

	public boolean updateEventById(int event_id, String title, String description, Date startTime, Date endTime,
			String c1, String c2, int user_id) {
		return odao.updateEventById(event_id, title, description, startTime, endTime, c1, c2, user_id);
	}

	public String updateUserById(int user_id, String username, String oldPassword, String newPassword) {
		return udao.updateUserById(user_id, username, oldPassword, newPassword);
	}

	public boolean disconnectMeByCalendar(int user_id, int calendarId) {
		return cdao.disconnectUserFromCalendarById(calendarId, user_id);
	}

	public boolean sendInvitation(int user_id, String receiver_email, int calendar_id, String privilege) {
		return idao.sendInvitation(user_id, receiver_email, calendar_id, privilege);
	}

	public boolean updateMemoById(int memo_id, int user_id, String title, Date data, String description, String c1) {
		return mdao.updateMemoById(memo_id, user_id, title, data, description, c1);

	}

	public boolean deleteMemoById(int memo_id, int user_id) {
		return mdao.deleteMemoById(memo_id, user_id);
	}

	public List<Calendar> getAllMyCalendars(String email) {
		return cdao.getCalendarsByEmail(email);
	}

	public List<Occurrence> getMyEventsInPeriod(String email, int calendar_id, String start, String end) {
		return odao.getOccurrenceByEmailInPeriod(email, calendar_id, start, end);
	}

	public boolean updateAlarm(int alarm_id, int minutes) {
		return adao.updateAlarmById(alarm_id, minutes);
	}

	public int addAlarm(int user_id, int occurrence_id, int minutes) {
		return adao.insertNewAlarm(user_id, occurrence_id, minutes);
	}

	public boolean deleteAlarm(int alarm_id, int user_id) {
	return adao.deleteAlarmById(alarm_id, user_id);
	}

	public List<Alarm> getMyAlarms(int user_id) {
		return adao.getAlarmsByUserId(user_id);

	}

	public Alarm getTheAlarmForAnOccurrence(int user_id, int occurrence_id) {
		return adao.getAlarmsByOccurrenceIdAndUserId(user_id, occurrence_id);
	}

	public List<Notification> getUnsentNotifications(int user_id) {
		return ndao.getUnsentNotificationByUserId(user_id);
	}

	public List<Invitation> getUnsentInvitations(int user_id) {
		return idao.getUnsentInvitationByUserId(user_id);
	}

	public List<Invitation> getMyInvitation(int user_id) {
		return idao.getInvitationsByReceiverId(user_id);
	}

	public List<Memo> getMyMemos(int user_id) {
		return mdao.getMemoByUserId(user_id);
	}

	public boolean resetSentState(int user_id) {
		return ndao.resetSentStateByUserId(user_id) && idao.resetSentStateByUserId(user_id);

	}

	public boolean deleteNotifications(int user) {
		return ndao.deleteNotifications(user);
	}

	public List<String> searchEmail(String emailToSearch, String personalEmail) {
		return udao.searchEmail(emailToSearch,personalEmail);
	}
	
	public String answerInvitation(int inv_id, int u_id, String answer) {
		Invitation i = idao.getInvitationById(inv_id);
		
		if (answer.equals("accept") && idao.acceptInvitation(u_id, i.getCalendar().getId()))
			return "accepted";
		else if (answer.equals("decline") && idao.declineInvitation(u_id, i.getCalendar().getId()))
			return "declined";
		return "error";
	}

	public int insertNewRepetition(int occ_id, String rType, int u_id, Date sT, Date eT) {
		return rdao.insertNewRepetition(occ_id, rType, u_id, sT, eT);
	}

	public int insertNewException(int r, Date sT, Date eT, int user_id) {
		return edao.insertNewException(r, sT, eT, user_id);
	}

	public boolean updateRepetition(int r, String rType, Date st, Date et, int user_id) {
		return rdao.updateRepetitionById(r, rType,st,et, user_id);
	}

	public boolean deleteRepetition(int r_id,int u_id) {
	return rdao.deleteRepetitionById(r_id, u_id);
	}

	public boolean updateException(int e_id, Date st, Date et, int user_id) {
		return edao.updateExceptionById(e_id ,st, et, user_id);
	}

	public boolean deleteException(int ex_id, int user_id) {
		return edao.deleteExceptionById(ex_id, user_id);
	}
}
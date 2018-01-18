package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Event;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface OccurrenceDAO {
	
	public void save(Occurrence Occurrence);

	public List<Occurrence> getAllOccurrences();
	void update(Occurrence Occurrence);
	Occurrence getOccurrenceById(int o_id);
	List<Occurrence> getOccurrencesByEmail(String email);

	
	//events
	List<Occurrence> filterEventsOfUserByCalendars(List<Calendar> calendars, User user);
	List<Occurrence> getEventsByCalendar(Calendar calendar);
	int insertNewEvent(User creator, Calendar c, String title, String description, Date startTime, Date endTime,
			String c1, String c2);
	boolean deleteEventById(Event oc, User u, Calendar ca);
	boolean updateEventById(Event v,  String title, String description, Date startTime, Date endTime,
			String c1, String c2, int user_id);

	Event getEventByUserId(int user_id);

	
	//memo
	Memo getMemoByUserId(int user_id);

	boolean deleteMemoById(Memo m, User u);

	int insertNewMemo(User creator, String title, Date date, String description, String c1);

	boolean updateMemoById(Memo m, int creator, String title, Date date, String description, String c1);


	

	
	
}

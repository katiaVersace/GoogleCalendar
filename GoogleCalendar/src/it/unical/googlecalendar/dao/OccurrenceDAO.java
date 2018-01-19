package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface OccurrenceDAO {
	
	public void save(Occurrence Occurrence);

	public List<Occurrence> getAllOccurrences();
	
	public List<Occurrence> getOccurrencesByEmail(String email);
	
	public List<Occurrence> getOccurrenceByEmailInPeriod(String email, int calendar_id, String startPeriod, String endPeriod);
	
	public List<Occurrence> getOccurrencesByCalendar(Calendar calendar);

	public List<User> getGuestsByOccurrence(Occurrence occurrence);

	public List<Occurrence> getOccurrencesByGuest(User guest);
	public List<Occurrence> filterOccurrencesOfUserByCalendars(List<Calendar> calendars, User user); 
	

	
	
	void update(Occurrence Occurrence);

	

	
	
	//boolean updateEventById(Event v, String title, Date data, String description, int user_id);

//	boolean updateMemoById(Memo m, String title, Date data, String description, int user_id);

	Occurrence getOccurrenceById(int o_id);

	
	
	
	int insertNewEvent(Calendar c, User u, String title, String description, Date startTime, Date endTime, String c1,
			String c2);

	boolean updateEventById(Occurrence v, String title, String description, Date startTime, Date endTime, String c1,
			String c2, int user_id);

	boolean deleteById(Occurrence oc, User u);
	
}

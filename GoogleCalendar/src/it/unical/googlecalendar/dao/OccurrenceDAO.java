package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface OccurrenceDAO {
	
	public void save(Occurrence Occurrence);

	public List<Occurrence> getAllOccurrences();
	
	public List<Occurrence> getOccurrencesByEmail(String email);
	
	public List<Occurrence> getOccurrencesByCalendar(Calendar calendar);

	public List<User> getGuestsByOccurrence(Occurrence occurrence);

	public List<Occurrence> getOccurrencesByGuest(User guest);
	public List<Occurrence> filterOccurrencesOfUserByCalendars(List<Calendar> calendars, User user); 
	public int insertNewEvent(int calendar_id, int creator_id, String title, Date data, String description);
	public int insertNewMemo(int calendar_id, int creator_id, String title, Date data, String description);
	
	boolean deleteById(int occurrenceId, int user_id);

	boolean updateEventById(int event_id, String title, Date data, String description, int user_id);

	boolean updateMemoById(int memo_id, String title, Date data, String description, int user_id);
	
}

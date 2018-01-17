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
	
	public List<Occurrence> getOccurrencesByEmail(String email);
	
	public List<Occurrence> getOccurrencesByCalendar(Calendar calendar);

	public List<User> getGuestsByOccurrence(Occurrence occurrence);

	public List<Occurrence> getOccurrencesByGuest(User guest);
	public List<Occurrence> filterOccurrencesOfUserByCalendars(List<Calendar> calendars, User user); 
	

	
	
	void update(Occurrence Occurrence);

	int insertNewEvent(Calendar c, User u, String title, Date data, String description);


	boolean deleteById(Occurrence o, User u, Calendar c);

	int insertNewMemo(Calendar c, User u, String title, Date data, String description);

	boolean updateEventById(Event v, String title, Date data, String description, int user_id);

	boolean updateMemoById(Memo m, String title, Date data, String description, int user_id);

	Occurrence getOccurrenceById(int o_id);
	
}

package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface OccurrenceDAO {
	
	void save(Occurrence Occurrence);

	List<Occurrence> getAllOccurrences();
	
	List<Occurrence> getOccurrencesByCalendar(Calendar calendar);

	List<User> getGuestsByOccurrence(Occurrence occurrence);

	List<Occurrence> getOccurrencesByGuest(User guest);
	
}

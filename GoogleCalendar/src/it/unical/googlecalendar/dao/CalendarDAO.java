package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface CalendarDAO {
	
	void save(Calendar Calendar);

	List<Calendar> getAllCalendars();
	List<Calendar> getCalendarsByEmail(String email);	
	public boolean deleteById(int calendarId, int user_id);
	public int insertNewCalendar(int user_id, String title, String description);
	
	boolean disconnectUserFromCalendarById(int calendarId, int user_id);

	boolean updateCalendarById(int calendar_id, String title, String description, int user_id);
}

package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface CalendarDAO {
	
	void save(Calendar Calendar);

	List<Calendar> getAllCalendars();
	List<Calendar> getCalendarsByEmail(String email);	
	
	
	
	void update(Calendar Calendar);

	String getPrivilegeForCalendarAndUser(int user_id, int calendar_id);

	boolean deleteById(Calendar c, User u);

	boolean disconnectUserFromCalendarById(Calendar c, User u);

	int insertNewCalendar(User u, String title, String description);

	boolean updateCalendarById(Calendar c, String title, String description, int user_id);

	Calendar getCalendarById(int c_id);
}

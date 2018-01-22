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
	
	Calendar getCalendarById(int c_id);

	boolean deleteById(int c_id, int u_id);

	int insertNewCalendar(int u_id, String title, String description);

	boolean updateCalendarById(int c_id, String title, String description, int user_id);

	boolean disconnectUserFromCalendarById(int c_id, int u_id);
}

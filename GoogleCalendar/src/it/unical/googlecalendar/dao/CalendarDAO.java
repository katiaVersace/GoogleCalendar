package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;


public interface CalendarDAO {
	
	void save(Calendar Calendar);

	List<Calendar> getAllCalendars();
	List<Calendar> getCalendarsByEmail(String email);	
	
	
	
	void update(Calendar Calendar);

	
	Calendar getCalendarById(int c_id);

	boolean deleteById(int c_id, int u_id);

	int insertNewCalendar(int u_id, String title, String description);

	boolean updateCalendarById(int c_id, String title, String description, int user_id);

	boolean disconnectUserFromCalendarById(int c_id, int u_id);

	List<Users_Calendars> getPrivilegesForCalendars(int user_id);

	String getPrivilegeForCalendarAndUser(int user_id, int calendar_id);
 
    HashMap<Integer, Date> getVersionStateForUserCalendars(int user_id);
}

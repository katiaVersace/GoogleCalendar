package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;


public interface CalendarDAO {
	
	void save(Calendar Calendar);

	List<Calendar> getAllCalendars();
	List<Calendar> getCalendarsByUser(User u);	
}

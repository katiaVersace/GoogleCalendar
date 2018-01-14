package it.unical.googlecalendar.dao;

import java.util.Collection;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Users_Calendars;



public interface Users_CalendarsDAO {
	
	void save(Users_Calendars uc);

	List<Users_Calendars> getAllAssociation();
	Collection<Calendar> getCalendarsForUser(String email);
	
	
}

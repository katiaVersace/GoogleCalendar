package it.unical.googlecalendar.dao;

import java.util.Collection;
import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Users_Calendars;



public interface Users_CalendarsDAO {
	
	void save(Users_Calendars uc);

	public List<Users_Calendars> getAllAssociation();
	public List<Users_Calendars> getAssociationByUserIdAndCalendarId(int user_id,int calendar_id);

	List<Users_Calendars> getAssociationByCalendarId(int calendar_id);
	
}

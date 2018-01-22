package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.User;

public interface InvitationDAO {
	
	void save(Invitation invitation);

	List<Invitation> getAllInvitations();

	
	
	
	
	void update(Invitation invitation);
	
	boolean acceptInvitation(User u, Calendar c);

	boolean declineInvitation(User u, Calendar c);

	
	
	boolean sendInvitation(int sender_id, String receiver_email, Calendar calendar, String privilege);

	List<Invitation> getUnsentInvitationByUserId(int user_id);

	boolean resetSentStateByUserId(int user_id);

	Invitation getInvitationById(int u_id);

	
	
}

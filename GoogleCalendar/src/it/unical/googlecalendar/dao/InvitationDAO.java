package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.User;

public interface InvitationDAO {
	
	void save(Invitation invitation);

	List<Invitation> getAllInvitations();

	
	
	
	
	void update(Invitation invitation);

	List<Invitation> getUnsentInvitationByUserId(int user_id);

	boolean resetSentStateByUserId(int user_id);

	Invitation getInvitationById(int u_id);

	boolean sendInvitation(int sender_id, String receiver_email, int c_id, String privilege);

	boolean acceptInvitation(int u_id, int c_id);

	boolean declineInvitation(int u_id, int c_id);

	
	
}

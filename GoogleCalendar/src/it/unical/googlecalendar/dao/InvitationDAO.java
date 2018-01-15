package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.User;

public interface InvitationDAO {
	
	void save(Invitation invitation);

	List<Invitation> getAllInvitations();

	
	boolean acceptInvitation(int receiver_id, int invitation_id);

	boolean declineInvitation(int receiver_id, int invitation_id);

	
	boolean sendInvitation(int sender_id, String receiver_email, int calendar_id, String privilege);
	
}

package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Invitation;

public interface InvitationDAO {
	
	void save(Invitation invitation);

	List<Invitation> getAllInvitations();
	
}

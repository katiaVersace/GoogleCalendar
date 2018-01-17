package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class InvitationDAOImpl implements InvitationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public InvitationDAOImpl() {

	}

	@Override
	public void save(Invitation invitation) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(invitation);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	@Override
	public void update(Invitation invitation) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(invitation);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	public List<Invitation> getAllInvitations() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Invitation> result = session.createNativeQuery("SELECT * FROM invitation", Invitation.class).list();

		session.close();
		return result;

	}

	public List<Invitation> getInvitationsByCalendarAndReceiver(int receiver_id, int calendar_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query1 = session.createQuery(
				"SELECT i FROM Invitation i WHERE i.receiver.id= :user_id and i.calendar.id= :calendar_id");
		query1.setParameter("user_id", receiver_id).setParameter("calendar_id", calendar_id);

		List<Invitation> result = query1.getResultList();
		session.close();
		return result;

	}

	public String getPriviledgeForInvitationsByCalendarAndReceiver(int receiver_id, int calendar_id) {
		Session session = sessionFactory.openSession();
		String result = null;
		// sql query
		Query query1 = session.createQuery(
				"SELECT i.privilege FROM Invitation i WHERE i.receiver.id= :user_id and i.calendar.id= :calendar_id");
		query1.setParameter("user_id", receiver_id).setParameter("calendar_id", calendar_id);

		List<String> resultList = query1.getResultList();
		if (resultList.size() > 0) {
			if (resultList.contains("ADMIN"))
				result = "ADMIN";
			else if (resultList.contains("RW")) {
				result = "RW";
			} else
				result = "R";
		}

		session.close();
		return result;

	}

	@Override
	public boolean acceptInvitation(User u,Calendar c) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		String myPrivilege = getPriviledgeForInvitationsByCalendarAndReceiver(u.getId(), c.getId());
		
		if (myPrivilege != null)// signofica che non ho ricevuto inviti da
								// accettare per questo calendario quindi esco
		{
			List<Invitation> invitationToDelete = getInvitationsByCalendarAndReceiver(u.getId(), c.getId());

			Transaction tx = null;
			try {
				tx = session.beginTransaction();

				// creo un'associazione tra l'utente e il calendario ed elimino
				// gli inviti dal db
				Users_Calendars association = new Users_Calendars(u, c, myPrivilege, Color.CYAN, c.getTitle());
				for (Invitation inv : invitationToDelete) {
					session.delete(inv);
				}

				session.update(c);
				session.update(u);

				tx.commit();

				result = true;

			} catch (Exception e) {
				result = false;
				tx.rollback();
			}
		}
		

		
		session.close();
		return result;
	}

	@Override
	public boolean declineInvitation(User u, Calendar c) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		List<Invitation> invitationToDelete = getInvitationsByCalendarAndReceiver(u.getId(),c.getId());
		if (invitationToDelete.size() > 0) {
			Transaction tx = null;
			try {
				

				tx = session.beginTransaction();

				for (Invitation inv : invitationToDelete) {
					session.delete(inv);
				}
				// session.saveOrUpdate(association);
				session.update(c);
				session.update(u);
				tx.commit();

				result = true;

			} catch (Exception e) {
				result = false;
				tx.rollback();
			}
		}

		session.close();
		return result;
	}

	@Override
	public boolean sendInvitation(int sender_id, String receiver_email, Calendar calendar, String privilege) {
		Session session = sessionFactory.openSession();
		boolean result = false;

		Query query1 = session.createQuery("SELECT u FROM User u WHERE u.email= :email");
		query1.setParameter("email", receiver_email);

		List<User> receiverId = query1.getResultList();
		if (receiverId.size() != 0) 
		{
			int receiver_id = receiverId.get(0).getId();
			//int receiver_id = receiver.getId();

			Query query = session.createQuery(
					"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
			query.setParameter("calendar_id", calendar.getId()).setParameter("user_id", sender_id);

			List<Users_Calendars> resultsId = query.getResultList();
			if (resultsId.size() != 0) {

				Users_Calendars uc = resultsId.get(0);

				if (uc.getPrivileges().equals("ADMIN")) {

					Transaction tx = null;

					try {

						Invitation i = new Invitation(sender_id,receiverId.get(0) , calendar, privilege);
						tx = session.beginTransaction();
						session.save(i);
						tx.commit();

						result = true;

					}

					catch (Exception e) {
						tx.rollback();
						result = false;
					}
				}
			}
		}
		session.close();

		return result;
	}

	@Override
	public boolean changePrivilegeOfInvitation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendNotificationOfResponse() {
		// TODO Auto-generated method stub
		return false;
	}

}

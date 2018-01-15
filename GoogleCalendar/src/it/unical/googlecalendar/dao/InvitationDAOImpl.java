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

	public void save(Invitation invitation) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(invitation);
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

	@Override
	public boolean acceptInvitation(int receiver_id, int invitation_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		Invitation i = (Invitation) session.get(Invitation.class, invitation_id);
		if (receiver_id == i.getReceiver().getId()) {

			System.out.println("Sono il ricevente");
			Transaction tx = null;
			try {
				User u = (User) session.get(User.class, receiver_id);
				Calendar c = (Calendar) session.get(Calendar.class, i.getCalendar().getId());
				Users_Calendars association = new Users_Calendars(u, c, i.getPrivilege(), Color.CYAN, c.getTitle());
				
				tx = session.beginTransaction();
				session.delete(i);
				// session.saveOrUpdate(association);
				session.saveOrUpdate(c);
				session.saveOrUpdate(u);
				tx.commit();

				System.out.println("commit fatto");
				u.receivedInvitations.remove(i);
				result = true;

			} catch (Exception e) {
				System.out.println("Sono entrato nel catch");
				result = false;
				tx.rollback();
			}
		}
		session.close();
		return result;
	}

	@Override
	public boolean declineInvitation(int receiver_id, int invitation_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		Invitation i = (Invitation) session.get(Invitation.class, invitation_id);
		if (receiver_id == i.getReceiver().getId()) {

			Transaction tx = null;

			try {
				User u = (User) session.get(User.class, receiver_id);
				
				tx = session.beginTransaction();
				session.delete(i);
				session.save(u);
				tx.commit();
				u.receivedInvitations.remove(i);
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
	public boolean sendInvitation(int sender_id, String receiver_email, int calendar_id, String privilege) {
		
		Session session = sessionFactory.openSession();
		boolean result=false;
		
		Query query1 = session.createQuery(
				"SELECT u.id FROM User u WHERE u.email= :email");
		query1.setParameter("email", receiver_email);

		List<Integer> receiverId = query1.getResultList();
		if (receiverId.size() != 0) {
			int receiver_id=receiverId.get(0);
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", calendar_id).setParameter("user_id", sender_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")) {
		

		Transaction tx = null;

		try {
			
			User receiver = (User) session.get(User.class, receiver_id);
			Calendar calendar = (Calendar) session.get(Calendar.class, calendar_id);
			
			Invitation i=new Invitation(sender_id,receiver,calendar,privilege);
			tx = session.beginTransaction();
			session.saveOrUpdate(i);
			tx.commit();
			receiver.receivedInvitations.add(i);
			result=true;

		} catch (Exception e) {
			tx.rollback();
			result=false;
		}
			}}}
		session.close();

		
		return result;
	}
}

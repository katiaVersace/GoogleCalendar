package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.List;

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


	@Override
	public boolean acceptInvitation(int receiver_id, int invitation_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		Invitation i = (Invitation) session.get(Invitation.class, invitation_id);
		if (receiver_id == i.getId()) {
			Transaction tx = null;
			try {
				User u = (User) session.get(User.class, receiver_id);
				Calendar c = (Calendar) session.get(Calendar.class, i.getCalendar().getId());
				Users_Calendars association = new Users_Calendars(u, c, i.getPrivilege(), Color.CYAN, c.getTitle());
u.receivedInvitations.remove(i);
				tx = session.beginTransaction();
				session.delete(i);
				//session.saveOrUpdate(association);
				session.saveOrUpdate(c);
				session.saveOrUpdate(u);
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
		public boolean declineInvitation(int receiver_id, int invitation_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		Invitation i = (Invitation) session.get(Invitation.class, invitation_id);
		if (receiver_id == i.getId()) {

			Transaction tx = null;

			try {

				tx = session.beginTransaction();
				session.delete(i);
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
}

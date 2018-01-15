package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Event;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class OccurrenceDAOImpl implements OccurrenceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public OccurrenceDAOImpl() {

	}

	public void save(Occurrence Occurrence) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(Occurrence);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	public List<Occurrence> getAllOccurrences() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Occurrence> result = session.createNativeQuery("SELECT * FROM occurrence", Occurrence.class).list();

		session.close();
		return result;

	}

	public List<Occurrence> getOccurrencesByCalendar(Calendar calendar) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session.createQuery("SELECT o FROM Occurrence o JOIN o.calendar c WHERE c.id = :calendar_id");
		query.setParameter("calendar_id", calendar.getId());
		List<Occurrence> result = query.getResultList();
		session.close();
		return result;
	}

	@Override
	public List<User> getGuestsByOccurrence(Occurrence occurrence) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session.createQuery("SELECT u FROM User u JOIN u.occurrences o WHERE o.id= :occurrence_id ");
		query.setParameter("occurrence_id", occurrence.getId());
		List<User> result = query.getResultList();
		session.close();
		return result;
	}

	@Override
	public List<Occurrence> getOccurrencesByGuest(User guest) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session.createQuery("SELECT o FROM Occurrence o JOIN o.guests u WHERE u.id= :guest_id ");
		query.setParameter("guest_id", guest.getId());
		List<Occurrence> result = query.getResultList();
		session.close();
		return result;
	}

	@Override
	public List<Occurrence> getOccurrencesByEmail(String email) {
		Session session = sessionFactory.openSession();

		// sql query

		Query query = session.createQuery(
				"SELECT o FROM Occurrence o, Users_Calendars uc JOIN uc.user u WHERE o.calendar=uc.calendar and u.email = :email");
		query.setParameter("email", email);

		// sql query
		List<Occurrence> result = query.getResultList();
		session.close();
		return result;
	}

	// quando l'utente seleziona solo alcuni calendari da vedere useremo questa
	// funzione per filtrare gli eventi
	@Override
	public List<Occurrence> filterOccurrencesOfUserByCalendars(List<Calendar> calendars, User user) {
		Session session = sessionFactory.openSession();

		Query query = session.createQuery(
				"SELECT o FROM Occurrence o, Users_Calendars uc JOIN uc.user u WHERE o.calendar=uc.calendar  and u.id = :user_id and o.calendar in (:calendars)");
		query.setParameter("calendars", calendars).setParameter("user_id", user.getId());

		// sql query
		List<Occurrence> result = query.getResultList();
		session.close();
		return result;

	}

	@Override
	public int insertNewEvent(int calendar_id, int creator_id, String title, Date data, String description) {
		Session session = sessionFactory.openSession();
		User u = (User) session.get(User.class, creator_id);
		Calendar c = (Calendar) session.get(Calendar.class, calendar_id);
		Event ev = new Event(c, u, title, data, description);
		int result = ev.getId();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(ev);
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}

	@Override
	public int insertNewMemo(int calendar_id, int creator_id, String title, Date data, String description) {
		Session session = sessionFactory.openSession();
		User u = (User) session.get(User.class, creator_id);
		Calendar c = (Calendar) session.get(Calendar.class, calendar_id);
		Memo m = new Memo(c, u, title, data, description);
		int result = m.getId();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(m);
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}

	@Override
	public boolean deleteById(int occurrenceId, int user_id) {
		Session session = sessionFactory.openSession();
		Occurrence c = null;
		Transaction tx = null;
		boolean result = false;

		Occurrence o = (Occurrence) session.get(Occurrence.class, occurrenceId);
		// per controllare i privilegi devo prendermi l'associazione tra
		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", o.getCalendar().getId()).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			// gestire se sei l'ultimo admin chi diventa admin?
			if (uc.getPrivileges().equals("ADMIN")) {
				try {
					c = (Occurrence) session.get(Occurrence.class, occurrenceId);
					tx = session.beginTransaction();

					session.delete(c);
					session.flush();

					tx.commit();
					Calendar calendar = session.get(Calendar.class, c.getCalendar().getId());
					calendar.getOccurrences().remove(c);
					result = true;

				} catch (Exception e) {
					e.printStackTrace();
					result = false;
					tx.rollback();
				}
			}
		}

		session.close();
		return result;
	}

	@Override
	public boolean updateEventById(int event_id, String title, Date data, String description, int user_id) {
		Session session = sessionFactory.openSession();
		Event v = (Event) session.get(Event.class, event_id);

		boolean result = false;
		
		Occurrence o = (Occurrence) session.get(Occurrence.class, event_id);
		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", o.getCalendar().getId()).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
		
		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			v.setTitle(title);
			v.setDate(data);
			v.setDescription(description);
			session.update(v);
			tx.commit();
			result = true;

		} catch (Exception e) {
			result = false;
			tx.rollback();
		}
			}}
		session.close();
		return result;
	}

	@Override
	public boolean updateMemoById(int memo_id, String title, Date data, String description, int user_id) {
		Session session = sessionFactory.openSession();
		Memo m = (Memo) session.get(Memo.class, memo_id);

		boolean result = false;

		Occurrence o = (Occurrence) session.get(Occurrence.class, memo_id);
		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", o.getCalendar().getId()).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
		

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			m.setTitle(title);
			m.setDate(data);
			m.setDescription(description);
			session.update(m);
			tx.commit();
			result = true;

		} catch (Exception e) {
			result = false;
			tx.rollback();
		}}}

		session.close();
		return result;
	}

}

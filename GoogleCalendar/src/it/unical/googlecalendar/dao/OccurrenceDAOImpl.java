package it.unical.googlecalendar.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;

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
			session.save(Occurrence);
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

		//sql query
      
		Query query = session.createQuery(
				"SELECT o FROM Occurrence o, Users_Calendars uc JOIN uc.user u WHERE o.calendar=uc.calendar and u.email = :email");
				query.setParameter("email", email);

		// sql query
		List<Occurrence> result = query.getResultList();
		session.close();
		return result;
	}

	
	//quando l'utente seleziona solo alcuni calendari da vedere useremo questa funzione per filtrare gli eventi
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

}

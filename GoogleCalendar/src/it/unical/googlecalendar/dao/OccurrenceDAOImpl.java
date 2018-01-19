package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.sound.midi.SysexMessage;

import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class OccurrenceDAOImpl implements OccurrenceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public OccurrenceDAOImpl() {

	}

	@Override
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
	
	@Override
	public void update(Occurrence Occurrence) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(Occurrence);
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
	
	@Override
	public List<Occurrence> getOccurrenceByEmailInPeriod(String email,
			int calendar_id, String start, String end) {
		Session session = sessionFactory.openSession();
		
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startPeriod = format.parse(start);
			Date endPeriod = format.parse(end);
			
			Query query = session.createQuery(
					"SELECT o "
			      + "FROM Occurrence o, Users_Calendars uc "
				  + "JOIN uc.user u "
			      + "WHERE o.calendar = uc.calendar "
				  + "  and o.calendar.id = :calendar_id "
				  + "  and u.email = :email "
				  + "  and ((startTime >= :startPeriod and startTime <= :endPeriod) or "
				  + "       (endTime >= :startPeriod and endTime <= :endPeriod)) "
			);
			
			query.setParameter("email", email);
			query.setParameter("calendar_id", calendar_id);
			query.setParameter("startPeriod", format.format(startPeriod));
			query.setParameter("endPeriod", format.format(endPeriod));
			
			return query.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return null;
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
	public int insertNewEvent(Calendar c, User u, String title, String description,Date startTime,Date endTime,Color c1, Color c2) {
		Session session = sessionFactory.openSession();
		Occurrence ev = new Occurrence(c, u, title, description,startTime,endTime, c1, c2);
		int result =-1;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(ev);
			result=ev.getId();
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}

//	@Override
//	public int insertNewMemo(Calendar c, User u, String title, Date data, String description) {
//		Session session = sessionFactory.openSession();
//		Memo m = new Memo(c, u, title, data, description);
//		int result =-1;
//		Transaction tx = null;
//
//		try {
//			tx = session.beginTransaction();
//			session.save(m);
//			result=m.getId();
//			tx.commit();
//
//		} catch (Exception e) {
//			result = -1;
//			tx.rollback();
//		}
//
//		session.close();
//		return result;
//	}

	@Override
	public boolean deleteById(Occurrence oc,User u, Calendar ca) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		boolean result = false;

		// per controllare i privilegi devo prendermi l'associazione tra
		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", oc.getCalendar().getId()).setParameter("user_id", u.getId());

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			// gestire se sei l'ultimo admin chi diventa admin?
			if (uc.getPrivileges().equals("ADMIN")) {
				try {
					tx = session.beginTransaction();

					session.delete(oc);
					session.flush();

					tx.commit();
					ca.getOccurrences().remove(oc);
					result = true;
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
					tx.rollback();
					e.printStackTrace();
				}
			}
		}
		session.close();
		return result;
	}

	@Override
	public boolean updateEventById(Occurrence v, String title, String description, Date startTime, Date endTime,
			Color c1, Color c2, int user_id) {
		Session session = sessionFactory.openSession();

		boolean result = false;

		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", v.getCalendar().getId()).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN") || uc.getPrivileges().equals("RW")) {

				Transaction tx = null;

				try {
					tx = session.beginTransaction();
					v.setTitle(title);
					v.setStartTime(startTime);
					v.setEndTime(endTime);
					v.setPrimaryColor(c1);
					v.setSecondaryColor(c2);
					v.setDescription(description);
					
					// DEBUG
					session.clear();
					Cache cache = sessionFactory.getCache();
					if (cache != null) {
						cache.evictAllRegions();
					}
					// END DEBUG
					
					session.saveOrUpdate(v);
					tx.commit();
					result = true;

				} catch (Exception e) {
					result = false;
					tx.rollback();
					e.printStackTrace();
				}
			}
		}
		session.close();
		return result;
	}

//	@Override
//	public boolean updateMemoById(Memo m, String title, Date data, String description, int user_id) {
//		Session session = sessionFactory.openSession();
//		
//		boolean result = false;
//
//		// l'utente e il calendario dell'occurrence
//		Query query = session.createQuery(
//				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
//		query.setParameter("calendar_id", m.getCalendar().getId()).setParameter("user_id", user_id);
//
//		List<Users_Calendars> resultsId = query.getResultList();
//		if (resultsId.size() != 0) {
//			Users_Calendars uc = resultsId.get(0);
//
//			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
//		
//
//		Transaction tx = null;
//
//		try {
//
//			tx = session.beginTransaction();
//			m.setTitle(title);
//			m.setDate(data);
//			m.setDescription(description);
//			session.update(m);
//			tx.commit();
//			result = true;
//
//		} catch (Exception e) {
//			result = false;
//			tx.rollback();
//		}}}
//
//		session.close();
//		return result;
//	}
	
	@Override
	public Occurrence getOccurrenceById(int o_id){
		Session session = sessionFactory.openSession();

		// sql query
		Occurrence result = session.get(Occurrence.class,o_id);

		session.close();
		return result;
	}
}

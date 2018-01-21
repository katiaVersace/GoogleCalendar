package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Repetition;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class RepetitionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public RepetitionDAO() {

	}

	public boolean save(Repetition Repetition) {

		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(Repetition);
			tx.commit();
			result = true;

		} catch (Exception e) {
			// e.printStackTrace();
			tx.rollback();
			result = false;
		}

		session.close();
		return result;

	}

	public List<Repetition> getAllRepetitions() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Repetition> result = session.createNativeQuery("SELECT * FROM Repetition", Repetition.class).list();

		session.close();
		return result;

	}

	public List<Repetition> getRepetitionsByOccurrenceId(int occurrence_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Repetition> result = session.createQuery("SELECT r FROM Repetition r where r.occurrence.id= :occurrence_id")
				.setParameter("occurrence_id", occurrence_id).getResultList();

		
			return result;
		
	}
	
	
//FIXME: mi sa che qui il lazy rompe
	public boolean updateRepetitionById(Repetition r,int nR, String qR,int user_id
			) {
		Session session = sessionFactory.openSession();
		boolean result = false;
		Occurrence o=session.get(Occurrence.class, r.getOccurrence().getId());

		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id",o.getCalendar().getId() ).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
		Transaction tx = null;

			try {
				tx = session.beginTransaction();
                r.setNumRepetition(nR);
                r.setRepetitionType(qR);
                session.update(r);
				tx.commit();
				result = true;

			} catch (Exception e) {
				// e.printStackTrace();
				tx.rollback();
				result = false;
			}
			}
		}
		session.close();
		return result;

	}

	public boolean deleteRepetitionById(Repetition r, int user_id) {
		Session session = sessionFactory.openSession();
		boolean result = false;
		Occurrence o=session.get(Occurrence.class, r.getOccurrence().getId());

		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id",o.getCalendar().getId() ).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
				Transaction tx = null;
				
			try {
				tx = session.beginTransaction();

				session.delete(r);
				session.flush();

				tx.commit();
				//o.getRepetitions().remove(r);
				o.setRepetition(null);
				result = true;

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
				tx.rollback();
			}
		
			}}
		session.close();
		return result;
	}

	
	public int insertNewRepetition(Occurrence o,int nR, String type, int user_id, Date st, Date et) {
		Session session = sessionFactory.openSession();
		int result =-1;
		
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id",o.getCalendar().getId() ).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
				Repetition m = new Repetition(o,nR, type, st, et);
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(m);
			result=m.getId();
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}
			}}

		session.close();
		return result;
	}

	public Repetition getRepetitionById(int Repetition_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Repetition result = session.get(Repetition.class,Repetition_id);

		session.close();
		return result;
	
	}

}

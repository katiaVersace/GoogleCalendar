package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Repetition;
import it.unical.googlecalendar.model.Calendar;
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
	public boolean updateRepetitionById(int r_id, String qR,Date st, Date et,int user_id) {
		Session session = sessionFactory.openSession();
		Repetition r=session.get(Repetition.class, r_id);
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
                r.setRepetitionType(qR);
                r.setStartTime(st);
                r.setEndTime(et);
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

	public boolean deleteRepetitionById(int r_id, int user_id) {
		Session session = sessionFactory.openSession();
		boolean result = false;
		Repetition r=session.get(Repetition.class, r_id);
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

	public int insertNewRepetition(int o2, String type, int user_id, Date st, Date et) {
		Session session = sessionFactory.openSession();
		int result =-1;
		Occurrence o=session.get(Occurrence.class, o2);
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id",o.getCalendar().getId() ).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);
			Transaction tx = null;

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
				try {
				Repetition m = new Repetition(o, type, st, et);
		

			tx = session.beginTransaction();
			Date oldVersion=o.getVersioneEvento();
			Occurrence cDB=session.get(Occurrence.class, o2);
			Date newVersion= cDB.getVersioneEvento();
			// DEBUG
			session.clear();
			Cache cache = sessionFactory.getCache();
			if (cache != null) {
				cache.evictAllRegions();
			}
			// END DEBUG

			//CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
			
			Calendar c=session.get(Calendar.class, cDB.getCalendar().getId());
			if(oldVersion.equals(newVersion)){
			Date now=new Date();
			o.setVersioneEvento(now);
			c.setVersioneStato(now);
			session.save(m);
			session.update(c);
			session.update(o);
			tx.commit();
			result=m.getId();
			}
			else{
				throw new Exception("L'evento con ripetizione è stato modificato da un utente, le tue modifiche andranno perse");
				
			}
			
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
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
package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.EventException;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Repetition;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class ExceptionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public ExceptionDAO() {

	}

	public boolean save(EventException Exception) {

		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(Exception);
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

	public List<EventException> getAllExceptions() {
		Session session = sessionFactory.openSession();

		// sql query
		List<EventException> result = session.createNativeQuery("SELECT * FROM EventException", EventException.class).list();

		session.close();
		return result;

	}

	public List<EventException> getExceptionsByRepetitionId(int repetition_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<EventException> result = session.createQuery("SELECT e FROM EventException e where e.repetition.id= :repetition_id")
				.setParameter("repetition_id", repetition_id).getResultList();

		
			return result;
		
	}
	
	

	public boolean updateExceptionById(EventException ex, Date startTime,Date endTime, int user_id
			) {
		
		Session session = sessionFactory.openSession();
		boolean result = false;
		
		if(isWriter(user_id, ex.getId())){
		Transaction tx = null;

			try {
				tx = session.beginTransaction();
                ex.setStartTime(startTime);
                ex.setEndTime(endTime);
                session.update(ex);
				tx.commit();
				result = true;

			} catch (Exception e) {
				// e.printStackTrace();
				tx.rollback();
				result = false;
			}
		
		}
		session.close();
		return result;

	}

	public boolean deleteExceptionById(EventException m,Repetition r,int user_id) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		boolean result = false;
		if(isWriter(user_id, m.getId())){
				
			try {
				tx = session.beginTransaction();

				session.delete(m);
				session.flush();

				tx.commit();
				r.getExceptions().remove(m);
				result = true;

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
				tx.rollback();
			}
		}

		session.close();
		return result;
	}

	public int insertNewException(Repetition r, Date s, Date en,int user_id) {
		Session session = sessionFactory.openSession();
		Repetition u=session.get(Repetition.class, r.getId());
		int result =-1;
		Transaction tx = null;
	
		try {
			
			EventException m = new EventException(u,s, en);
			tx = session.beginTransaction();
			   // DEBUG
			session.clear();
			Cache cache = sessionFactory.getCache();
			if (cache != null) {
				cache.evictAllRegions();
			}
			// END DEBUG
			session.save(m);
			tx.commit();
			result=m.getId();
			
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
			tx.rollback();
		}
		
		session.close();
		
		return result;
	}

	public Exception getExceptionById(int Exception_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Exception result = session.get(Exception.class,Exception_id);

		session.close();
		return result;
	
	}
	
	public boolean isWriter(int user_id, int exception_id){
		Session session = sessionFactory.openSession();
		boolean result = false;
		
		

		Query query = session.createQuery(
				"SELECT uc.privileges "
				+ "FROM Users_Calendars uc, Calendar c, Occurrence o, Repetition r, EventException ev"
				+ "WHERE uc.calendar.id= c.id and uc.user.id= :user_id and c.id=o.calendar.id and o.id=r.occurrence.id and ev.repetition.id=r.id and ev.id=:exception_id  ");
		query.setParameter("exception_id",exception_id).setParameter("user_id", user_id);

		List<String> resultsId = query.getResultList();
		if (!resultsId.isEmpty()) {
			String uc = resultsId.get(0);

			if (uc.equals("ADMIN")||uc.equals("RW")) {
				result=true;
			}
			
		}
		session.close();
		return result;
			
	}

}

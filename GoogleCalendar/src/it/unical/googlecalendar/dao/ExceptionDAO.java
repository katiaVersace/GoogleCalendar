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

import it.unical.googlecalendar.model.Calendar;
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
	
	

	public boolean updateExceptionById(int e_id, Date startTime,Date endTime, int user_id
			) {
		
		Session session = sessionFactory.openSession();
		EventException ex=session.get(EventException.class, e_id);
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

	public boolean deleteExceptionById(int ex_id,int user_id) {
		Session session = sessionFactory.openSession();
		EventException m=session.get(EventException.class,ex_id);
		Repetition r=session.get(Repetition.class,m.getRepetition().getId());
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


	public int insertNewException(int r_id, Date s, Date en,int user_id) {
	    Session session = sessionFactory.openSession();
        Repetition u=session.get(Repetition.class,r_id);
        Occurrence o=session.get(Occurrence.class, u.getOccurrence().getId());
        int result =-1;
        Transaction tx = null;
        
        Query query = session.createQuery(
                "SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
        query.setParameter("calendar_id",u.getOccurrence().getCalendar().getId() ).setParameter("user_id", user_id);

        List<Users_Calendars> resultsId = query.getResultList();
        if (resultsId.size() != 0) {
            Users_Calendars uc = resultsId.get(0);
                if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
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
            Date oldVersion=o.getVersioneEvento();
            
            //CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
            Occurrence cDB=session.get(Occurrence.class, u.getOccurrence().getId());
            Date newVersione=cDB.getVersioneEvento();
        
                   // DEBUG
                session.clear();
             cache = sessionFactory.getCache();
                if (cache != null) {
                    cache.evictAllRegions();
                }
                // END DEBUG
            Calendar c=session.get(Calendar.class, cDB.getCalendar().getId());
        if(oldVersion.equals(newVersione)){
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
                throw new Exception("L'evento con ripetizione ed eccezione è stato modificato da un utente, le tue modifiche andranno perse");
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
            tx.rollback();
        }}}
        
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

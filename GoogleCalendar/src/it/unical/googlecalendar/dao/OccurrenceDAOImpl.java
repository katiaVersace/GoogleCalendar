package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.sound.midi.SysexMessage;

import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.OccurrenceDAO;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.EventException;
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
	public void update(Occurrence o) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;
		

		try {
			tx = session.beginTransaction();
						
			//CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
			Date oldVersion=o.getVersioneEvento();
			Occurrence cDB=getOccurrenceById(o.getId());
			Date newVersion=cDB.getVersioneEvento();
			
			// DEBUG
			session.clear();
			Cache cache = sessionFactory.getCache();
			if (cache != null) {
				cache.evictAllRegions();
			}// END DEBUG
			
			if(oldVersion.equals(newVersion)){
			Date now=new Date();
			o.setVersioneEvento(now);
			session.update(o);
			tx.commit();
						}
			else{
				
				throw new Exception("L'evento è stato modificato da un utente, le tue modifiche andranno perse");
				
			}}
		catch (Exception e) {
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
			
			List<Occurrence> res1=query.getResultList();
			
			
			Query queryR = session.createQuery("SELECT o FROM Occurrence o  JOIN o.repetition orep, Users_Calendars uc JOIN uc.user u"
					+ " WHERE orep.endTime>= :start and o.calendar = uc.calendar and o.calendar.id = :calendar_id and u.email = :email").setParameter("start",startPeriod);
			queryR.setParameter("email", email);
			queryR.setParameter("calendar_id", calendar_id);List<Occurrence> res2=queryR.getResultList();
			res1.addAll(res2);
			Set<Occurrence>s1=new HashSet<>();
			s1.addAll(res1);
			res1.clear();
			res1.addAll(s1);
			
			for (Occurrence occurrence : res1) {
			    if (occurrence.getRepetition() != null) {
			        Hibernate.initialize(occurrence.getRepetition());
			        
			        if (!occurrence.getRepetition().getExceptions().isEmpty()) {
			            Hibernate.initialize(occurrence.getRepetition().getExceptions());
			        }
			    }
			}
			
			return res1;
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
public int insertNewEvent(int ca, int u_id, String title, String description,Date startTime,Date endTime,String c1, String c2) {
		
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Calendar c=session.get(Calendar.class, ca);
		Occurrence ev = new Occurrence(c, u, title, description,startTime,endTime, c1, c2);
		int result =-1;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			//CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
			Date now=new Date();
			c.setVersioneStato(now);
			session.save(ev);
			session.update(c);
			tx.commit();
			result=ev.getId();
			
			
		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}



	@Override
	public boolean deleteById(int oc_id,int u_id) {
		Session session = sessionFactory.openSession();
		Occurrence oc =session.get(Occurrence.class,oc_id);
		User u = session.get(User.class,u_id);
		Transaction tx = null;
		boolean result = false;
		Calendar ca=session.load(Calendar.class, oc.getCalendar().getId());
		Hibernate.initialize(ca.getOccurrences());
		// per controllare i privilegi devo prendermi l'associazione tra
		// l'utente e il calendario dell'occurrence
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc, Calendar c, Occurrence o"
				+ " WHERE uc.calendar.id= c.id and uc.user.id= :user_id and c.id=o.calendar.id and o.id= :occurrence_id ");
		query.setParameter("occurrence_id", oc.getId()).setParameter("user_id", u.getId());

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			// gestire se sei l'ultimo admin chi diventa admin?
			if (uc.getPrivileges().equals("ADMIN")) {
				try {
					tx = session.beginTransaction();
					Date oldVersion=oc.getVersioneEvento();
					Occurrence cDB=session.get(Occurrence.class, oc_id);
					Date newVersion=cDB.getVersioneEvento();
					    // DEBUG
						session.clear();
						Cache cache = sessionFactory.getCache();
						if (cache != null) {
							cache.evictAllRegions();
						}
						// END DEBUG
						
						
						//CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
						
						Calendar c=session.get(Calendar.class, ca.getId());
						if(oldVersion.equals(newVersion)){
						Date now=new Date();
						oc.setVersioneEvento(now);
						c.setVersioneStato(now);
						session.delete(oc);
						session.update(c);
						session.flush();
						tx.commit();
						result = true;

						
						}
						else{
							
							throw new Exception("L'evento con ripetizione ed eccezione è stato modificato da un utente, le tue modifiche andranno perse");
							
						}
						ca.getOccurrences().remove(oc);
						
						
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
	public boolean updateEventById(int ev_id, String title,  String description, Date startTime,Date endTime,String c1, String c2,int user_id) {
		Session session = sessionFactory.openSession();
		Occurrence oldO=session.get(Occurrence.class,ev_id);
		Occurrence v = oldO;
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
					
					Date oldVersion=v.getVersioneEvento();
					Occurrence cDB=getOccurrenceById(ev_id);
					Date newVersion=cDB.getVersioneEvento();
					// DEBUG
					session.clear();
					Cache cache = sessionFactory.getCache();
					if (cache != null) {
						cache.evictAllRegions();
					}
					// END DEBUG
					
					//CONCORRENZA: controllo della versione(ricarico l'oggetto dal db e controllo se la versione è cambiata)
					
					Calendar c=session.get(Calendar.class, v.getCalendar().getId());
					if(oldVersion.equals(newVersion)){
					Date now=new Date();
					v.setVersioneEvento(now);
					c.setVersioneStato(now);
					session.saveOrUpdate(v);
					session.update(c);
					tx.commit();
					result = true;
					}
					else{
						v.setTitle(oldO.getTitle());
						v.setStartTime(oldO.getStartTime());
						v.setEndTime(oldO.getEndTime());
						v.setPrimaryColor(oldO.getPrimaryColor());
						v.setSecondaryColor(oldO.getSecondaryColor());
						v.setDescription(oldO.getDescription());
						throw new Exception("L'evento è stato modificato da un utente, le tue modifiche andranno perse");
						
					}
					
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
	
	@Override
	public Occurrence getOccurrenceById(int o_id){
		Session session = sessionFactory.openSession();

		// sql query
		Occurrence result = session.get(Occurrence.class,o_id);
		Hibernate.initialize(result.getRepetition().getExceptions());

		session.close();
		return result;
	}
}

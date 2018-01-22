package it.unical.googlecalendar.dao;

import java.util.List;

import javax.persistence.Query;
import javax.sound.midi.SysexMessage;

import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import it.unical.googlecalendar.dao.CalendarDAO;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class CalendarDAOImpl implements CalendarDAO {

	@Autowired
	private SessionFactory sessionFactory;
@Autowired
private Users_CalendarsDAOImpl ucdao;


	public CalendarDAOImpl() {
	}

	@Override
	public void save(Calendar Calendar) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(Calendar);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}
	
	@Override
	public void update(Calendar Calendar) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(Calendar);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	public List<Calendar> getAllCalendars() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Calendar> result = session.createNativeQuery("SELECT * FROM calendar", Calendar.class).list();

		session.close();
		return result;

	}

	public List<Calendar> getCalendarsByEmail(String email) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session
//				.createQuery("SELECT uc.calendar FROM Users_Calendars uc JOIN uc.user u WHERE u.email = :email");
				.createQuery("SELECT c FROM Calendar c JOIN FETCH c.users_calendars uc WHERE uc.user.email = :email");
		query.setParameter("email", email);
		List<Calendar> result = query.getResultList();
		session.close();
		return result;
	}
	
	@Override
	public String getPrivilegeForCalendarAndUser(int user_id, int calendar_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session
				.createQuery("SELECT uc.privileges FROM Users_Calendars uc WHERE uc.calendar.id = :calendar and uc.user.id = :user");
		query.setParameter("user", user_id).setParameter("calendar", calendar_id);
		String result = (String) query.getSingleResult();
		session.close();
		return result;
	}

	@Override
	public boolean deleteById(int c_id, int u_id) {
		Session session = sessionFactory.openSession();
		Calendar c =session.get(Calendar.class,c_id);
		User u = session.get(User.class,u_id);
		Transaction tx = null;
		boolean result = false;
		Users_Calendars resultAssociation;
		List<Users_Calendars> resultsId=ucdao.getAssociationByUserIdAndCalendarId(u.getId(), c.getId());
		if(resultsId.size()!=0){
		resultAssociation = resultsId.get(0);
		
		//gestire se sei l'ultimo admin chi diventa admin?
		    if(resultAssociation.getPrivileges().equals("ADMIN")){
		    		
		try {
			
			tx = session.beginTransaction();
			
			session.delete(c);
			session.flush();
			
			tx.commit();
			
			//dovrebbe farlo la cascata..delete c->delete uc->refresh u
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
	public boolean disconnectUserFromCalendarById(int c_id, int u_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Calendar c = session.get(Calendar.class,c_id);
		Users_Calendars resultAssociation;
		List<Users_Calendars> resultsId=ucdao.getAssociationByUserIdAndCalendarId(u.getId(), c.getId());
		if(resultsId.size()!=0){
		resultAssociation = resultsId.get(0);
		boolean toDelete=false;
		Transaction tx = null;		
		//Se sei l'ultimo degli admin
		    if(resultAssociation.getPrivileges().equals("ADMIN")&&numOfUsersWithPriviledgeForCalendar(c, "ADMIN")==1){
		    	Users_Calendars uc;
		    	
		    		if(numOfUsersWithPriviledgeForCalendar(c, "RW")>0){
		    			uc=usersWithPriviledgeForCalendar(c, "RW").get(0);
		    			uc.setPrivileges("ADMIN");
		    			tx = session.beginTransaction();
		    			session.update(uc);
		    			tx.commit();
		    		}
		    		else if(numOfUsersWithPriviledgeForCalendar(c, "R")>0){
		    			uc=usersWithPriviledgeForCalendar(c, "R").get(0);
		    			uc.setPrivileges("ADMIN");
		    			tx = session.beginTransaction();
		    			session.update(uc);
		    			tx.commit();
		    		} 
		    			else toDelete=true;
		    	}
		
		int resultId=0;
		Users_Calendars uc = (Users_Calendars) session.get(Users_Calendars.class, resultId);
		try {
			
			tx = session.beginTransaction();
			
			//se rimuovo il calendario a cascata rimuovo anche l'associazione
			if(toDelete){
			deleteById(c.getId(), u.getId());
						}
			//altrimenti rimuovo solo l'associazione
			else session.delete(uc);

			session.flush();
			tx.commit();
			
			//dovrebbe farlo la cascata..delete c->delete uc->refresh u
			//session.save(u);
			//u.removeAssociationWithCalendar(c);
			//c.getUsers_calendars().remove(uc);
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			tx.rollback();
		}
		}
		//}
		
		session.close();
		
		return result;
	}

	
	
	
	@Override
	public int insertNewCalendar(int u_id, String title, String description) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class, u_id);
		Calendar c=new Calendar(u,title,description,false);
		int result = -1;
		
				Transaction tx = null;

				try {
					tx = session.beginTransaction();
					session.save(c);
					result=c.getId();
					session.update(u);
					tx.commit();
					result = c.getId();
				} catch (Exception e) {
					result=-1;
					tx.rollback();

				}

				session.close();

				return result;
	}

	@Override
	public boolean updateCalendarById(int c_id,String title, String description, int user_id) {
		Session session = sessionFactory.openSession();
		Calendar c = session.get(Calendar.class,c_id);
		
		boolean result=false;
		
		Query query = session.createQuery(
				"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		query.setParameter("calendar_id", c.getId()).setParameter("user_id", user_id);

		List<Users_Calendars> resultsId = query.getResultList();
		if (resultsId.size() != 0) {
			Users_Calendars uc = resultsId.get(0);

			if (uc.getPrivileges().equals("ADMIN")||uc.getPrivileges().equals("RW")) {
		

				Transaction tx = null;

				try {
					
					tx = session.beginTransaction();
					c.setTitle(title);
					c.setDescription(description);
					
					// DEBUG
					session.clear();
					Cache cache = sessionFactory.getCache();
					if (cache != null) {
						cache.evictAllRegions();
					}
					// END DEBUG
					
					session.update(c);
					tx.commit();
					result=true;
					

				} catch (Exception e) {
					result=false;
					tx.rollback();
					e.printStackTrace();
				}}}

				session.close();
return result;
	}

	@Override
	public Calendar getCalendarById(int c_id){
		Session session = sessionFactory.openSession();

		// sql query
		Calendar result = session.get(Calendar.class,c_id);

		session.close();
		return result;
		
	}
	
	
	public int numOfUsersWithPriviledgeForCalendar(Calendar c, String privilege){
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session
				.createQuery("FROM Users_Calendars uc WHERE uc.calendar.id = :calendar_id and uc.privileges= :privilege");
		query.setParameter("calendar_id", c.getId()).setParameter("privilege",privilege);
		List<String> result = query.getResultList();
		session.close();
		return result.size();
	
	}
	
	
	public List<Users_Calendars> usersWithPriviledgeForCalendar(Calendar c, String privilege){
		Session session = sessionFactory.openSession();

		// sql query
		Query query = session
				.createQuery("SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id = :calendar_id and uc.privileges= :privilege");
		query.setParameter("calendar_id", c.getId()).setParameter("privilege",privilege);
		List<Users_Calendars> result = query.getResultList();
		session.close();
		return result;
	
	}

	public int insertNewFBCalendar(int u_id, String title, String description) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class, u_id);
		Calendar c=new Calendar(u,title,description,true);
		int result = -1;
		
				Transaction tx = null;

				try {
					tx = session.beginTransaction();
					session.save(c);
					result=c.getId();
					session.update(u);
					tx.commit();
					result = c.getId();
				} catch (Exception e) {
					result=-1;
					tx.rollback();

				}

				session.close();

				return result;
	}
	
}
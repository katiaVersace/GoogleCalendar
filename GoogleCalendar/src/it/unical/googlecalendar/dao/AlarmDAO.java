package it.unical.googlecalendar.dao;

import java.util.Date;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Alarm;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.User;

@Repository
public class AlarmDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public AlarmDAO() {

	}

	public boolean save(Alarm Alarm) {

		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(Alarm);
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

	public List<Alarm> getAllAlarms() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Alarm> result = session.createNativeQuery("SELECT * FROM Alarm", Alarm.class).list();

		session.close();
		return result;

	}

	public List<Alarm> getAlarmsByUserId(int user_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Alarm> result = session.createQuery("SELECT m FROM Alarm m where m.user.id= :user_id")
				.setParameter("user_id", user_id).getResultList();

		
			return result;
		
	}
	
	public Alarm getAlarmsByOccurrenceIdAndUserId(int user_id,int occurrence_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Alarm result = (Alarm) session.createQuery("SELECT a FROM Alarm a where a.user.id= :user_id and a.occurrence.id= :occurrence_id")
				.setParameter("user_id", user_id).setParameter("alarm_id", occurrence_id).uniqueResult();

		
			return result;
		

	}

	public boolean updateAlarmById(int a_id,int alarm
			) {
		Session session = sessionFactory.openSession();
		Alarm m = session.get(Alarm.class, a_id);
		boolean result = false;
		Transaction tx = null;

			try {
				tx = session.beginTransaction();
                m.setAlarm(alarm);
                m.setMinutes(alarm);
                session.update(m);
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

	public boolean deleteAlarmById(int a_id,int u_id) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Alarm m =  session.get(Alarm.class,a_id);
		
		Transaction tx = null;
		boolean result = false;
		
				
			try {
				tx = session.beginTransaction();

				session.delete(m);
				session.flush();

				tx.commit();
				u.getAlarms().remove(m);
				result = true;

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
				tx.rollback();
			}
		

		session.close();
		return result;
	}

	public int insertNewAlarm(int u_id,int o_id,int a) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class, u_id);
		Occurrence o = session.get(Occurrence.class, o_id);
		Alarm m = new Alarm(u,o, a);
		int result =-1;
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

		session.close();
		return result;
	}

	public Alarm getAlarmById(int Alarm_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Alarm result = session.get(Alarm.class,Alarm_id);

		session.close();
		return result;
	
	}
	public List<Alarm> getAlarmsToNotifyById(int user_id) {
		Session session = sessionFactory.openSession();
		Date now=new Date();
		//Date now=new Date(118, 2, 1, 10, 0, 0);
		Date now1=new Date(now.getTime()+59*1000L);
		
		

		// sql query
				List<Alarm> result = session.createQuery("SELECT m FROM Alarm m where m.user.id= :user_id and m.alarm>=:now and m.alarm<=:now1 ")
						.setParameter("user_id", user_id).setParameter("now1", now1).setParameter("now", now).getResultList();
				
//				List<Date> resultDates = session.createQuery("SELECT datediff(SECOND,m.alarm,:now) P FROM Alarm m where m.user.id= :user_id ")
//						.setParameter("user_id", user_id).setParameter("now", now).getResultList();
//				for(Date d:resultDates){
//					System.out.println(d);
//				}

		session.close();
		return result;
	
	}

}

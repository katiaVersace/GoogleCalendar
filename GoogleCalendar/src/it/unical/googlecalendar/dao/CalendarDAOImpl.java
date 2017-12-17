package it.unical.googlecalendar.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import it.unical.googlecalendar.dao.CalendarDAO;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;

public class CalendarDAOImpl implements CalendarDAO {

	

	private static CalendarDAOImpl instance;
	
	public static CalendarDAOImpl getInstance() {
		if(instance==null) {
			instance = new CalendarDAOImpl();
		}
		return instance;
	}

	private SessionFactory sessionFactory;
	
	public CalendarDAOImpl() {

		sessionFactory = DBManager.getSessionFactory();
	}

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

	public List<Calendar> getAllCalendars() {
		Session session = sessionFactory.openSession();

		//sql query
		List<Calendar> result = session.createNativeQuery("SELECT * FROM calendar", Calendar.class).list();

		session.close();
		return result;

	}
	
	

	public List<Calendar> getCalendarsByUser(User user) {
		Session session = sessionFactory.openSession();

		//sql query
		Query query = session.createQuery("SELECT c FROM Calendar c JOIN c.users u WHERE u.id = :user_id");
		query.setParameter("user_id",user.getId());
				List<Calendar> result = query.getResultList();
		session.close();
		return result;
		}

}
package it.unical.googlecalendar.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class Users_CalendarsDAOImpl implements Users_CalendarsDAO{
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void save(Users_Calendars uc) {
		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(uc);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}
	
	@Override
	public void update(Users_Calendars uc) {
		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(uc);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	@Override
	public List<Users_Calendars> getAllAssociation() {
		Session session = sessionFactory.openSession();

		
		Query query = session.createQuery("SELECT uc FROM Users_Calendars uc  ");
		
		List<Users_Calendars> result = query.getResultList();
		
		session.close();
		return result;

	}
	
	@Override
	public List<Users_Calendars> getAssociationByUserIdAndCalendarId(int user_id,int calendar_id) {
		Session session = sessionFactory.openSession();

		
		Query query = session.createQuery("SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
		
		query.setParameter("user_id", user_id).setParameter("calendar_id", calendar_id);
		List<Users_Calendars> result = query.getResultList();
		
		session.close();
		return result;

	}
	@Override
	public List<Users_Calendars> getAssociationByCalendarId(int calendar_id) {
		Session session = sessionFactory.openSession();

		
		Query query = session.createQuery("SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id");
		
		query.setParameter("calendar_id", calendar_id);
		List<Users_Calendars> result = query.getResultList();
		
		session.close();
		return result;

	}
	


}

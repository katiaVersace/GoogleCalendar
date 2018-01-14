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
	public List<Users_Calendars> getAllAssociation() {
		Session session = sessionFactory.openSession();

		
		Query query = session.createQuery("SELECT uc FROM Users_Calendars uc  ");
		
		List<Users_Calendars> result = query.getResultList();
		
		session.close();
		return result;

	}
	
	@Override
	public Collection<Calendar> getCalendarsForUser(String email) {
	Session session = sessionFactory.openSession();

	//sql query
	Query query = session.createQuery("SELECT uc.calendar FROM Users_Calendars uc JOIN uc.user u WHERE u.email = :email");
	query.setParameter("email",email);
			List<Calendar> result = query.getResultList();
	session.close();


	return result;
	}


}

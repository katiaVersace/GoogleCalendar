package it.unical.googlecalendar.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import it.unical.googlecalendar.dao.UserDAO;

import it.unical.googlecalendar.model.User;

public class UserDAOImpl implements UserDAO {

	

	private static UserDAOImpl instance;
	
	public static UserDAOImpl getInstance() {
		if(instance==null) {
			instance = new UserDAOImpl();
		}
		return instance;
	}

	private SessionFactory sessionFactory;
	
	public UserDAOImpl() {

		sessionFactory = DBManager.getSessionFactory();
	}

	public void save(User user) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	public List<User> getAllUsers() {
		Session session = sessionFactory.openSession();

		//sql query
		List<User> result = session.createNativeQuery("SELECT * FROM user", User.class).list();

		session.close();
		return result;

	}

}

package it.unical.googlecalendar.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.UserDAO;

import it.unical.googlecalendar.model.User;


@Repository
public class UserDAOImpl implements UserDAO {


@Autowired	
	private SessionFactory sessionFactory;
	
	public UserDAOImpl() {

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

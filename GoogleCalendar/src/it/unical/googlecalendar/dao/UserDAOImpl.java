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

	public boolean save(User user) {

		Session session = sessionFactory.openSession();
		boolean result=false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			result=true;

		} catch (Exception e) {
			tx.rollback();
			result=false;
		}

		session.close();
		return result;

	}

	public List<User> getAllUsers() {
		Session session = sessionFactory.openSession();

		//sql query
		List<User> result = session.createNativeQuery("SELECT * FROM user", User.class).list();

		session.close();
		return result;

	}
	
	public boolean existsUser(String email, String password) {
		Session session = sessionFactory.openSession();

		//sql query
		List<User> result = session.createQuery("SELECT * FROM user u WHERE u.email = :user_email and u.password = :user_password").setParameter("user_email", email).setParameter("user_password", password).getResultList();

		session.close();
		if (result.size()==0)
		return false;
		else return true;
	
	}

}

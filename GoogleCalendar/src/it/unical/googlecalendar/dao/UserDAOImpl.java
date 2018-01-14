package it.unical.googlecalendar.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.model.Calendar;
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
			session.saveOrUpdate(user);
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
		List<User> result;
		Query q= session.createQuery("SELECT u FROM User u WHERE u.email = :user_email and u.password = :user_password");
		q.setParameter("user_email", email).setParameter("user_password", password);
		result=q.getResultList();

		session.close();
		if (result.size()==0)
		return false;
		else return true;
	
	}

	public String getUsernameByEmail(String email){
		Session session = sessionFactory.openSession();

		//sql query
		List<String>result = session.createQuery("SELECT u.username FROM User u where u.email= :user_email").setParameter("user_email",email).getResultList();

		session.close();
		return result.get(0);
		
	}
	
	
	
	
	
}

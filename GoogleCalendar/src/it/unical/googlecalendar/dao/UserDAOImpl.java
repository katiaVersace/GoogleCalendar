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

	@Override
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
			//e.printStackTrace();
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
		if( result.size()>0)
		return result.get(0);
		else return null;
		
	}

	public int getIdByEmail(String email) {
		Session session = sessionFactory.openSession();

		//sql query
		List<Integer>result = session.createQuery("SELECT u.id FROM User u where u.email= :user_email").setParameter("user_email",email).getResultList();

		session.close();
		if(result.size()>0)
		return result.get(0);
		return -1;
	}

	@Override
	public boolean updateUserById(User u, String username, String password) {
		Session session = sessionFactory.openSession();
		
		
		boolean result=false;
				Transaction tx = null;

				try {
					
					tx = session.beginTransaction();
					u.setUsername(username);
					u.setPassword(password);
					session.update(u);
					tx.commit();
					result=true;
					

				} catch (Exception e) {
					result=false;
					tx.rollback();
				}

				session.close();
return result;
	}

	@Override
	public boolean update(User user) {
		Session session = sessionFactory.openSession();
		boolean result=false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			result=true;

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result=false;
		}

		session.close();
		return result;
		
	}
	
	@Override
	public User getUserById(int u_id){
		Session session = sessionFactory.openSession();

		// sql query
		User result = session.get(User.class,u_id);

		session.close();
		return result;
		
	}
	@Override
	public User getUserByEmail(String email){
		Session session = sessionFactory.openSession();
		//sql query
				User result;
				result=(User) session.createQuery("SELECT u FROM User u WHERE u.email = :user_email").setParameter("user_email", email).uniqueResult();
				 


		session.close();
		return result;
		
	}
	
	@Override
	public List<String> searchEmail(String email){
		Session session = sessionFactory.openSession();
		//sql query
				
			List<String> result= session.createQuery("SELECT u.email FROM User u WHERE u.email like :user_email").setParameter("user_email", "%"+email+"%").getResultList();
				 
		session.close();
		return result;
	}
}
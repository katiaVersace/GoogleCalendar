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
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(user);
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

	@Override
	public List<User> getAllUsers() {
		Session session = sessionFactory.openSession();

		// sql query
		List<User> result = session.createNativeQuery("SELECT * FROM user", User.class).list();

		session.close();
		return result;

	}

	@Override
	public String existsUser(String email, String password) {
		Session session = sessionFactory.openSession();

		// se la password non � null stai facendo un normale login
		// sql query
		User result;
		Query q = session.createQuery("SELECT u FROM User u WHERE u.email = :user_email");
		q.setParameter("user_email", email);
		try {
			result = (User) q.getSingleResult();
		} catch (Exception e) {

			session.close();
			return "FALSE"; // utente non registrato
			// TODO: handle exception
		}

		if (result != null) {
			if (result.getPassword() == null)// TODO: ti sei sempre loggato con
												// fb quindi comunicare all
												// utente di entrare cn fb e
												// cambiare la password
			{
				session.close();
				return "SETPASSWORD";
			}

			else if (result.getPassword().equals(password)) {
				session.close();
				return "TRUE";
			}
		}
		session.close();

		return "FALSE"; // utente non registrato

	}

	@Override
	public boolean existsUserFB(String email) {
		Session session = sessionFactory.openSession();

		// sql query
		List<User> result;
		Query q = session.createQuery("SELECT u FROM User u WHERE u.email = :user_email");
		q.setParameter("user_email", email);
		result = q.getResultList();

		session.close();
		if (result.size() == 0)
			return false;
		else
			return true;

	}

	public String getUsernameByEmail(String email) {
		Session session = sessionFactory.openSession();

		// sql query
		List<String> result = session.createQuery("SELECT u.username FROM User u where u.email= :user_email")
				.setParameter("user_email", email).getResultList();

		session.close();
		if (result.size() > 0)
			return result.get(0);
		else
			return null;

	}

	public int getIdByEmail(String email) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Integer> result = session.createQuery("SELECT u.id FROM User u where u.email= :user_email")
				.setParameter("user_email", email).getResultList();

		session.close();
		if (result.size() > 0)
			return result.get(0);
		return -1;
	}

	@Override
	public String updateUserById(int u_id, String username, String oldPassword, String newPassword) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class, u_id);

		String oldPssDb = u.getPassword();

		String result = "";
		Transaction tx = null;

		try {
			if (oldPassword.equals(oldPssDb) && !newPassword.isEmpty() && !username.isEmpty()) {
				tx = session.beginTransaction();
				u.setUsername(username);
				u.setPassword(newPassword);
				session.update(u);
				tx.commit();
				result = "User update successfully";
			} else if (oldPassword.isEmpty() && !username.isEmpty()) {
				tx = session.beginTransaction();
				u.setUsername(username);
				session.update(u);
				tx.commit();
				result = "Username changed successfully";
			} else if (username.isEmpty() && oldPassword.equals(oldPssDb)) {
				tx = session.beginTransaction();
				u.setPassword(newPassword);
				session.update(u);
				tx.commit();
				result = "Password changed successfully";
			} else {
				result = "Old Password not mach";
			}

		} catch (Exception e) {
			result = "Error";
			tx.rollback();
		}
		session.close();
		return result;
	}

	@Override
	public boolean update(User user) {
		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;
		}

		session.close();
		return result;

	}

	@Override
	public User getUserById(int u_id) {
		Session session = sessionFactory.openSession();

		// sql query
		User result = session.get(User.class, u_id);

		session.close();
		return result;

	}

	@Override
	public User getUserByEmail(String email) {
		Session session = sessionFactory.openSession();
		// sql query
		User result;
		result = (User) session.createQuery("SELECT u FROM User u WHERE u.email = :user_email")
				.setParameter("user_email", email).uniqueResult();

		session.close();
		return result;
	}

	@Override
	public List<String> searchEmail(String email, String personalEmail) {
		Session session = sessionFactory.openSession();
		// sql query
		List<String> result = session
				.createQuery("SELECT u.email FROM User u WHERE u.email like :user_email AND u.email <> :personal_email")
				.setParameter("user_email", "%" + email + "%").setParameter("personal_email", personalEmail)
				.getResultList();

		session.close();
		return result;
	}

	@Override
	public int insertNewUser(String email, String username, String password) {
		Session session = sessionFactory.openSession();
		User u = new User(email, username, password);
		int result = -1;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(u);
			tx.commit();
			result = u.getId();
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
			tx.rollback();

		}

		session.close();

		return result;

	}

	@Override
	public Calendar getFbCalendar(int user_id) {
		Session session = sessionFactory.openSession();
		// sql query

		User u = session.get(User.class, user_id);
		return u.getMyFacebookCalendar();

	}
}
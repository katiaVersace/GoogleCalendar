package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Notification;
import it.unical.googlecalendar.model.User;

@Repository
public class NotificationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public NotificationDAO() {

	}

	public boolean save(Notification notification) {

		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(notification);
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

	public List<Notification> getAllNotification() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Notification> result = session.createNativeQuery("SELECT * FROM Notification",Notification.class).list();

		session.close();
		return result;

	}

	//Questa qui � la funzione che il server SSE invoca per vedere quali sono le notifiche da inviare
	public List<Notification> getUnsentNotificationByUserId(int user_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Notification> result = session.createQuery("SELECT n FROM Notification n where n.user.id= :user_id and n.sent is false")
				.setParameter("user_id", user_id).getResultList();
		
		Transaction tx = null;
		
//TODO se nn funziona pulire cache
		try {
			tx = session.beginTransaction();
			
			for(Notification noti:result){
				noti.setSent(true);
				
				session.update(noti);
			}
			
			tx.commit();
			
		} catch (Exception e) {
			// e.printStackTrace();
			tx.rollback();
			
		}


		session.close();
		return result;

	}
	
	//Questa qui viene chiamata dall'utente al login 
	public boolean resetSentStateByUserId(int user_id) {
		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;
		List<Notification> n = session.createQuery("SELECT n FROM Notification n where n.user.id= :user_id ")
				.setParameter("user_id", user_id).getResultList();
//TODO se nn funziona pulire cache
		try {
			tx = session.beginTransaction();
			
			for(Notification noti:n){
				noti.setSent(false);
				
				session.update(noti);
			}
			
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

	
	public boolean deleteNotifications(int u_id) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Transaction tx = null;
		boolean result = false;
	
		try {
			tx = session.beginTransaction();

			Query query = session
					.createQuery("DELETE FROM Notification n WHERE n.user.id= :user_id");
			query.setParameter("user_id",u.getId());
			query.executeUpdate();

			tx.commit();
			Hibernate.initialize(u.getNotifications());
			u.getNotifications().clear();
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			tx.rollback();
		}
		

		session.close();
		return result;
	}

	public int sendNotification(User u, String description) {
		Session session = sessionFactory.openSession();
		Notification n = new Notification(u,description);
		int result =-1;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(n);
			result=n.getId();
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}

	public Notification getNotificationById(int notification_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Notification result = session.get(Notification.class,notification_id);

		session.close();
		return result;
	
	}

}

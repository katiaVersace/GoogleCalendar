package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.Date;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.User;

@Repository
public class MemoDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public MemoDAO() {

	}

	public boolean save(Memo Memo) {

		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(Memo);
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

	public List<Memo> getAllMemos() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Memo> result = session.createNativeQuery("SELECT * FROM Memo", Memo.class).list();

		session.close();
		return result;

	}

	public List<Memo> getMemoByUserId(int user_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Memo> result = session.createQuery("SELECT m FROM Memo m where m.user.id= :user_id")
				.setParameter("user_id", user_id).getResultList();

		session.close();
		return result;
	}

	public boolean updateMemoById(int m_id, int creator, String title, Date date, String description, String color1
			) {
		Session session = sessionFactory.openSession();
		Memo m = session.get(Memo.class,m_id);
		boolean result = false;
		Transaction tx = null;

		if (m.getUser().getId()==creator) {
			try {
				tx = session.beginTransaction();
				m.setTitle(title);
				m.setCreationDate(date);
				m.setDescription(description);
				m.setPrimaryColor(color1);
				session.update(m);
				tx.commit();
				result = true;

			} catch (Exception e) {
				// e.printStackTrace();
				tx.rollback();
				result = false;
			}
		}

		session.close();
		return result;

	}

	public boolean deleteMemoById(int m_id, int u_id) {
		Session session = sessionFactory.openSession();
		Memo m = session.get(Memo.class, m_id);
		User u = session.get(User.class,u_id);

		
		Transaction tx = null;
		boolean result = false;

		if (m.getUser().equals(u)) {
			try {
				tx = session.beginTransaction();

				session.delete(m);
				session.flush();

				tx.commit();
				u.getMemos().remove(m); // FIXME: lazy initialization
				result = true;

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
				tx.rollback();
			}
		}

		session.close();
		return result;
	}

	public int insertNewMemo(int creator_id, String title, Date data, String description, String c1) {
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,creator_id);
		Memo m = new Memo(u, title, data, description,c1);
		int result =-1;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(m);
			result=m.getId();
			tx.commit();

		} catch (Exception e) {
			result = -1;
			tx.rollback();
		}

		session.close();
		return result;
	}

	public Memo getMemoById(int memo_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Memo result = session.get(Memo.class,memo_id);

		session.close();
		return result;
	
	}

}

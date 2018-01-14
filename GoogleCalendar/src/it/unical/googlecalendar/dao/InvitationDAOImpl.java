package it.unical.googlecalendar.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.unical.googlecalendar.model.Invitation;



@Repository
public class InvitationDAOImpl implements InvitationDAO {


@Autowired	
	private SessionFactory sessionFactory;
	
	public InvitationDAOImpl() {

	}

	public void save(Invitation invitation) {

		Session session = sessionFactory.openSession();
		
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(invitation);
			tx.commit();
		
		} catch (Exception e) {
			tx.rollback();
		}

		session.close();
		
	}

	public List<Invitation> getAllInvitations() {
		Session session = sessionFactory.openSession();

		//sql query
		List<Invitation> result = session.createNativeQuery("SELECT * FROM invitation", Invitation.class).list();

		session.close();
		return result;

	}
		
}

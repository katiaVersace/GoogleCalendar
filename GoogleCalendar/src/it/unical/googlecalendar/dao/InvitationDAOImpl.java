package it.unical.googlecalendar.dao;

import java.awt.Color;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonElement;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.Notification;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@Repository
public class InvitationDAOImpl implements InvitationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public InvitationDAOImpl() {

	}

	@Override
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

	@Override
	public void update(Invitation invitation) {

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(invitation);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		}

		session.close();

	}

	public List<Invitation> getAllInvitations() {
		Session session = sessionFactory.openSession();

		// sql query
		List<Invitation> result = session.createNativeQuery("SELECT * FROM invitation", Invitation.class).list();

		session.close();
		return result;

	}
	
	public List<Integer> getSendersOfInvitationById(int invitation_id) {
		Session session = sessionFactory.openSession();

		// sql query

		Invitation result =  session.get(Invitation.class, invitation_id);
		Hibernate.initialize(result.getSendersId());
		session.close();
		return result.getSendersId();

	}

	
	
	
	
	public Invitation getInvitationByCalendarAndReceiver(int receiver_id, int calendar_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query1 = session.createQuery(
				"SELECT i FROM Invitation i WHERE i.receiver.id= :user_id and i.calendar.id= :calendar_id");
		query1.setParameter("user_id", receiver_id).setParameter("calendar_id", calendar_id);

		Invitation result = (Invitation) query1.getSingleResult();
		session.close();
		return result;

	}
	
	public List<Invitation> getInvitationsByReceiverId(int receiver_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Query query1 = session.createQuery(
				"SELECT i FROM Invitation i WHERE i.receiver.id= :user_id");
		query1.setParameter("user_id", receiver_id);
		List<Invitation> result = query1.getResultList();
	for(Invitation i:result)
		Hibernate.initialize(i.getSendersId());

		session.close();
		return result;

	}

	public String getPriviledgeForInvitationByCalendarAndReceiver(int receiver_id, int calendar_id) {
		Session session = sessionFactory.openSession();
		String result = null;
		// sql query
		Query query1 = session.createQuery(
				"SELECT i.privilege FROM Invitation i WHERE i.receiver.id= :user_id and i.calendar.id= :calendar_id");
		query1.setParameter("user_id", receiver_id).setParameter("calendar_id", calendar_id);

		result = (String) query1.getSingleResult();
		

		session.close();
		return result;

	}

	@Override
	public boolean acceptInvitation(int u_id,int c_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Calendar c = session.get(Calendar.class, c_id);
		
		String myPrivilege = getPriviledgeForInvitationByCalendarAndReceiver(u.getId(), c.getId());
		if (myPrivilege != null)// signofica che non ho ricevuto inviti da
								// accettare per questo calendario quindi esco
		{	Invitation invitationToDelete = getInvitationByCalendarAndReceiver(u.getId(), c.getId());
			Transaction tx = null;
			try {
				
				tx = session.beginTransaction();

				// creo un'associazione tra l'utente e il calendario ed elimino
				
				Users_Calendars association = new Users_Calendars(u, c, myPrivilege);
				List<Integer>sen=getSendersOfInvitationById(invitationToDelete.getId());
				
				for (Integer senderInv : sen) {
					User sender=session.get(User.class,senderInv);
					
					Notification acceptNotification=new Notification(sender,u.getUsername()+" accepted your invitation to calendar: "+c.getTitle());

					session.update(sender);
					
				}
				session.delete(invitationToDelete);
				
				session.update(c);
				session.update(u);
				

				tx.commit();
				result = true;

			} catch (Exception e) {
				result = false;
				tx.rollback();
			}
		}
		

		
		session.close();
		return result;
	}

	@Override
	public boolean declineInvitation(int u_id,int c_id) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		User u = session.get(User.class,u_id);
		Calendar c = session.get(Calendar.class,c_id);
		Invitation invitationToDelete = getInvitationByCalendarAndReceiver(u.getId(),c.getId());
		if (invitationToDelete!=null) {
			Transaction tx = null;
			try {
				

				tx = session.beginTransaction();
		
					
					session.delete(invitationToDelete);
				
				// session.saveOrUpdate(association);
				session.update(c);
				session.update(u);
				tx.commit();

				result = true;

			} catch (Exception e) {
				result = false;
				tx.rollback();
			}
		}

		session.close();
		return result;
	}

	@Override
	public boolean sendInvitation(int sender_id, String receiver_email, int c_id, String privilege) {
		Session session = sessionFactory.openSession();
		Calendar calendar =session.get(Calendar.class,c_id);
		boolean result = false;

		Query query1 = session.createQuery("SELECT u FROM User u WHERE u.email= :email");
		query1.setParameter("email", receiver_email);

		List<User> receiverId = query1.getResultList();
		if (receiverId.size() != 0) 
		{
			int receiver_id = receiverId.get(0).getId();
			//int receiver_id = receiver.getId();

			Query query = session.createQuery(
					"SELECT uc FROM Users_Calendars uc WHERE uc.calendar.id= :calendar_id and uc.user.id= :user_id");
			query.setParameter("calendar_id", calendar.getId()).setParameter("user_id", sender_id);

			List<Users_Calendars> resultsId = query.getResultList();
			if (resultsId.size() != 0) {
				Users_Calendars uc = resultsId.get(0);

				if (uc.getPrivileges().equals("ADMIN")) {

					Transaction tx = null;

					try {
						Query queryExistsInvitation = session.createQuery(
								"SELECT i FROM Invitation i WHERE i.calendar.id= :calendar_id and i.receiver.id= :user_id");
						queryExistsInvitation.setParameter("calendar_id", calendar.getId()).setParameter("user_id", receiver_id);
						tx = session.beginTransaction();
						//System.out.println("1");
						
						Invitation i;
						//System.out.println("Dovrei entrare nel try"+receiver_email);
						try {
							
							i= (Invitation) queryExistsInvitation.getSingleResult();
							i.getSendersId().add(sender_id);
							i.setPrivilege(privilege);
							session.update(i);
						}
						catch (Exception e) {
						i = new Invitation(sender_id,receiverId.get(0) , calendar, privilege);
						session.save(i);
						
						}
						tx.commit();

						result = true;
}

					catch (Exception e) {
						tx.rollback();
						result = false;
					}
				}
			}
		}
		session.close();

		return result;
	}

	

	@Override
	public List<Invitation> getUnsentInvitationByUserId(int user_id) {
		Session session = sessionFactory.openSession();

		// sql query
		List<Invitation> result = session.createQuery("SELECT i FROM Invitation i where i.receiver.id= :user_id and i.sent is false")
				.setParameter("user_id", user_id).getResultList();
		
		Transaction tx = null;
		
//TODO se nn funziona pulire cache
		try {
			tx = session.beginTransaction();
			
			for(Invitation inv:result){
				inv.setSent(true);
				
				session.update(inv);
			}
			
			tx.commit();
			
		} catch (Exception e) {
			// e.printStackTrace();
			tx.rollback();
			
		}


		session.close();
		return result;

	}

	@Override
	public boolean resetSentStateByUserId(int user_id) {
		Session session = sessionFactory.openSession();
		boolean result = false;

		Transaction tx = null;
		List<Invitation> n = session.createQuery("SELECT i FROM Invitation i where i.receiver.id= :user_id ")
				.setParameter("user_id", user_id).getResultList();
//TODO se nn funziona pulire cache
		try {
			tx = session.beginTransaction();
			
			for(Invitation inv:n){
				inv.setSent(false);
				
				session.update(inv);
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

	@Override
	public Invitation getInvitationById(int u_id) {
		Session session = sessionFactory.openSession();

		// sql query
		Invitation result = session.get(Invitation.class, u_id);

		session.close();
		return result;

	}
	

}

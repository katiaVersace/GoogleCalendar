package it.unical.googlecalendar.tests;

import java.awt.Color;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.unical.googlecalendar.configuration.AppConfiguration;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.InvitationDAOImpl;
import it.unical.googlecalendar.dao.NotificationDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
import it.unical.googlecalendar.model.Notification;
import it.unical.googlecalendar.model.User;
import it.unical.googlecalendar.model.Users_Calendars;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class InvitationDAOTest {

	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private InvitationDAOImpl idao;

	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private Users_CalendarsDAOImpl ucdao;
	@Autowired
	private NotificationDAO ndao;

	@Test
	public void saveTest() {

		User mario = new User("mario@p.it", "mario", "1234");
		User peppe = new User("peppe@c.it", "peppe", "5678");
		User fabio = new User("fabio@c.it", "fabio", "5678");
		udao.save(peppe);
		udao.save(mario);
		udao.save(fabio);

		Calendar c = new Calendar(peppe, "peppe calendar", "jshdjs");
		cdao.save(c);
		//peppe invita mario a c
		idao.sendInvitation(peppe.getId(), mario.getEmail(), c.getId(), "RW");
		Invitation i = idao.getInvitationByCalendarAndReceiver(mario.getId(), c.getId());
		//peppe invita fabio a c
		idao.sendInvitation(peppe.getId(), fabio.getEmail(), c.getId(), "ADMIN");
		Invitation in = idao.getInvitationByCalendarAndReceiver(fabio.getId(), c.getId());
		
		//System.out.println("inviti di mario prima che accetti: "+mario.receivedInvitations.size());
//		 for(Invitation in:mario.receivedInvitations){
//				 System.out.println("Invitok: "+in.getSenderId()+in.getReceiver().getUsername()+" + "+in.getPrivilege());
//				 }

		// Invitation i=new Invitation(peppe.getId(), mario, c, "ADMIN");
		// idao.save(i);
		// Assert.assertTrue(idao.getAllInvitations().size()==1);
		Assert.assertTrue(idao.getAllInvitations().contains(i));
		//idao.acceptInvitation(mario.getId(), i.getId());
		//fabio accetta a c
		idao.acceptInvitation(fabio.getId(), c.getId());	
		
		//fabio invita mario a c
		idao.sendInvitation(fabio.getId(), mario.getEmail(), c.getId(), "ADMIN");
		Assert.assertTrue(idao.getInvitationsByReceiverId(mario.getId()).size()==1);
		//mario accetta a c
		idao.acceptInvitation(mario.getId(), c.getId());
//		List<Users_Calendars> ass=ucdao.getAssociationByUserIdAndCalendarId(mario.getId(), c.getId());
//		System.out.println("Associazioni di mario con il calendario c "+ass.size());
		List<Notification> notif=ndao.getUnsentNotificationByUserId(peppe.getId());
//		System.out.println("Notifiche di peppe: "+notif.size());
//		for(Notification no:notif){
//			System.out.println(no.getDescription());
//		}
		
		//System.out.println("Peppe sta x leggere le notifiche e cancellarle");
		ndao.deleteNotifications(peppe.getId());
		
		 notif=ndao.getUnsentNotificationByUserId(peppe.getId());
//			System.out.println("Notifiche di peppe: "+notif.size());
//			for(Notification no:notif){
//				System.out.println(no.getDescription());
//			}
			//ucdao.getAssociationByUserIdAndCalendarId(mario.getId(), c.getId()).isEmpty();
//		 System.out.println("Associazioni");
//		 List <Users_Calendars>ass= ucdao.getAllAssociation();
//		 for(Users_Calendars u:ass){
//			 System.out.println(u.getUser().getUsername()+" "+u.getCalendarName());
//		 }
//		
		String privilegiDiMario=cdao.getPrivilegeForCalendarAndUser(mario.getId(),c.getId());
//System.out.println(privilegiDiMario);
		
		
//	System.out.println("Privilegi di mario "+privilegiDiMario);
	Assert.assertTrue(privilegiDiMario.equals("ADMIN"));
	//		System.out.println("inviti di mario dopo che accetti: "+mario.receivedInvitations.size());
//		 for(Invitation in:mario.receivedInvitations){
//				 System.out.println("Invitok: "+in.getSenderId()+in.getReceiver().getUsername()+" + "+in.getPrivilege());
//				 }
		// System.out.println("All invitations after accepting: ");
		// for(Invitation in:idao.getAllInvitations()){
		// System.out.println(in.getSenderId()+" +
		// "+in.getReceiver().getUsername()+" + "+in.getPrivilege());
		// }

		// mario non può invitare nessuno perchè non è admin
		//Assert.assertTrue(!idao.sendInvitation(mario.getId(), peppe.getEmail(), c.getId(), "R"));
		// now that mario accepted invitation there is no invitation
	//	Assert.assertTrue(!idao.getAllInvitations().contains(i));
	Assert.assertTrue(cdao.numOfUsersWithPriviledgeForCalendar(c,"ADMIN")==3);
		List<User> allUsers = udao.getAllUsers();

		Assert.assertTrue(allUsers.contains(mario));
	}

}

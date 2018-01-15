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
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.dao.Users_CalendarsDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Invitation;
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
	
	@Test
	public void saveTest() {
		
		User mario = new User("mario@p.it","mario", "1234");
		User peppe=new User("peppe@c.it","peppe", "5678");
		udao.save(peppe);
		udao.save(mario);
	
		
		Calendar c=new Calendar(peppe,"peppe calendar","jshdjs");
		cdao.save(c);
		Invitation i=new Invitation(peppe.getId(), mario, c, "ADMIN");
		idao.save(i);
		List<User> allUsers = udao.getAllUsers();
		Assert.assertTrue(idao.getAllInvitations().size()==1);
		
		Assert.assertTrue(allUsers.contains(mario));
	}

}

package it.unical.googlecalendar.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.User;

@Service
public class LoginService {

	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private OccurrenceDAOImpl odao;

	// form visible
	private String visible = "block";
	private String active = "active";
	// form hidden
	private String hidden = "none";
	private String inactive = "inactive";

	//@PostConstruct
	public void initialize() {

		udao.save(new User("ciccio@c.it", "ciccio", "ciccio"));
		udao.save(new User("pippo@p.it", "pippo", "pippo"));
		udao.save(new User("k@h.it", "Katia", "1234"));
		udao.save(new User("kikk@h.it", "Katia", "1234"));

	}

	public String loginAttempt(String email, String password) {
		if (udao.existsUser(email, password).equals("TRUE"))
			return "TRUE";

		else if (udao.existsUser(email, password).equals("SETPASSWORD"))
			return "SETPASSWORD";
		else if (udao.existsUser(email, password).equals("FALSE"))
			return "FALSE";
		return "FALSE";

	}

	public int registrationAttempt(String email, String username, String password, String confirmPassword) {

		// empty fields

		if (email.equals("") || username.equals("") || password.equals(""))

			return -1;

		// password missmatch

		if (!password.equals(confirmPassword))

			return -1;

		// user Exist
         int user_id=udao.insertNewUser(email, username, password);
		
		return user_id;

	}

	public String getUsername(String email) {
		return udao.getUsernameByEmail(email);
	}

	public void showLoginForm(Model model) {
		model.addAttribute("login_block", visible);
		model.addAttribute("login_title", active);

		model.addAttribute("register_title", inactive);
		model.addAttribute("register_block", hidden);
	}

	public void showRegisterForm(Model model) {
		model.addAttribute("login_block", hidden);
		model.addAttribute("login_title", inactive);

		model.addAttribute("register_block", visible);
		model.addAttribute("register_title", active);
	}

	public int getId(String email) {
		return udao.getIdByEmail(email);
	}

	public boolean existsUser(String emailFB) {
		return udao.existsUserFB(emailFB);
	}

	public boolean creaUtenteFB(String emailFB, String username) {
		if (udao.insertNewUser(emailFB, username, null)!=-1)
			{
			return true;}
		{
			
			return false;}

	}

	public int createFbCalendar(int user_id, String title, String description) {
		return cdao.insertNewFBCalendar(user_id, title, description);
	}

	public void insertNewFBEvent(int calendar_id,int user_id,String title, String description,Date startTime,Date endTime,String c1, String c2) {
		odao.insertNewEvent(calendar_id, user_id, title, description, startTime, endTime, c1, c2);
		
	}

	public Calendar getFBCalendar(int user_id) {
	return	udao.getFbCalendar(user_id);
	}

	public boolean deleteEventsById(int o, int c) {
	return odao.deleteById(o, c);
		
	}

	
}

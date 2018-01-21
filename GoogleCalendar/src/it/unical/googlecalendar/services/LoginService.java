package it.unical.googlecalendar.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.User;

@Service
public class LoginService {

	@Autowired
	private UserDAOImpl udao;

	// form visible
	private String visible = "block";
	private String active = "active";
	// form hidden
	private String hidden = "none";
	private String inactive = "inactive";

	@PostConstruct
	public void initialize() {

		udao.save(new User("ciccio@c.it", "ciccio", "ciccio"));
		udao.save(new User("pippo@p.it", "pippo", "pippo"));
		udao.save(new User("k@h.it", "Katia", "1234"));

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

	public boolean registrationAttempt(String email, String username, String password, String confirmPassword) {

		// empty fields

		if (email.equals("") || username.equals("") || password.equals(""))

			return false;

		// password missmatch

		if (!password.equals(confirmPassword))

			return false;

		// user Exist

		if (!udao.save(new User(email, username, password)))

			return false;

		return true;

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
		if (udao.save(new User(emailFB, username, null)))
			return true;
		return false;

	}
}

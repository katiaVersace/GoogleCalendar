package it.unical.googlecalendar.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.unical.googlecalendar.services.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping("/")
	public String loginPage(HttpSession session,Model model) {
		if (session.getAttribute("email") == null) {
			loginService.showLoginForm(model);
			return "login";
		}
		return "redirect:/index";
	}

	@RequestMapping(value = "/loginAttempt", method = RequestMethod.POST)
	public String loginAttempt(@RequestParam String email, @RequestParam String password, Model model,
			HttpSession session) {

		if (loginService.loginAttempt(email, password)) {
			session.setAttribute("email", email);
			session.setAttribute("username", loginService.getUsername(email));
			return "redirect:/index";
		}
		
		loginService.showLoginForm(model);
		return "login";
	}
	
	@RequestMapping(value = "/registrationAttempt", method = RequestMethod.POST)
	public String registrationAttempt(@RequestParam String email, @RequestParam String username,
			@RequestParam String password, @RequestParam String confirm_password, Model model, HttpSession session) {

	if (loginService.registrationAttempt(email, username, password, confirm_password)) {

		session.setAttribute("email", email);

			session.setAttribute("username", username);

			return "redirect:/index";

		}
		
		loginService.showRegisterForm(model);
		
		return "login";

	}


@RequestMapping("/logout")
	public String logout(HttpSession session,Model model) {
		loginService.showLoginForm(model);
		session.invalidate();		
		return "login";
	}

}

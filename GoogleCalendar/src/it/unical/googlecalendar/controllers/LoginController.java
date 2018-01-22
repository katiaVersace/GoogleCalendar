package it.unical.googlecalendar.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.unical.googlecalendar.services.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping("/")
	public String loginPage(HttpSession session, Model model) {
		if (session.getAttribute("email") == null) {
			loginService.showLoginForm(model);
			return "login";
		}
		return "redirect:/index";
	}

	@RequestMapping(value = "/loginAttempt", method = RequestMethod.POST)
	public String loginAttempt(@RequestParam String email, @RequestParam String password, Model model,
			HttpSession session) {

		if (loginService.loginAttempt(email, password).equals("TRUE")) {
			session.setAttribute("email", email);
			session.setAttribute("username", loginService.getUsername(email));
			session.setAttribute("user_id", loginService.getId(email));

			return "redirect:/index";
		} else if (loginService.loginAttempt(email, password).equals("SETPASSWORD")) {
			System.out.println("Vai a loggarti cn fb e cambia la password");
			model.addAttribute("SETPASSWORD", true);
			loginService.showLoginForm(model);
			return "login";
		}
		model.addAttribute("SETPASSWORD", false);
		loginService.showLoginForm(model);
		return "login";
	}

	@RequestMapping(value = "/registrationAttempt", method = RequestMethod.POST)
	public String registrationAttempt(@RequestParam String email, @RequestParam String username,
			@RequestParam String password, @RequestParam String confirm_password, Model model, HttpSession session) {
		int user_id = loginService.registrationAttempt(email, username, password, confirm_password);
		if (user_id != -1) {

			session.setAttribute("email", email);

			session.setAttribute("username", username);
			session.setAttribute("user_id", user_id);

			return "redirect:/index";

		}

		loginService.showRegisterForm(model);

		return "login";

	}

	@RequestMapping("/logout")
	public String logout(HttpSession session, Model model) {
		loginService.showLoginForm(model);
		session.invalidate();
		return "login";
	}

	@RequestMapping("getFBData")
	@ResponseBody
	public String fbDataRequest(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String emailFB = request.getParameter("email");
		if (!loginService.existsUser(emailFB)) {

			if (loginService.creaUtenteFB(emailFB, request.getParameter("name"))) {
				session.setAttribute("username", request.getParameter("name"));
				session.setAttribute("email", emailFB);
				session.setAttribute("user_id", loginService.getId(emailFB));
				return "index";
			} else
				return "login";
		}
		session.setAttribute("username", loginService.getUsername(emailFB));
		session.setAttribute("user_id", loginService.getId(emailFB));
		session.setAttribute("email", emailFB);

		return "index";

	}
	
	}

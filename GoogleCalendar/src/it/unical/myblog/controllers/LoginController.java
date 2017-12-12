package it.unical.myblog.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.unical.myblog.services.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	@RequestMapping("/")
	public String loginPage(HttpSession r) {
		if(r.getAttribute("user")==null)
		return "login";
		return "redirect:/index";
	}
	
	@RequestMapping(value="/loginAttempt",method=RequestMethod.POST)
	public String loginAttempt(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
		
		if(loginService.loginAttempt(username, password)) {
			session.setAttribute("user", username);
			return "redirect:/index";
		}
		
		return "login";
	}
	
	@RequestMapping(value="/registrationAttempt",method=RequestMethod.POST)
	public String registrationAttempt(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {

		if(loginService.registrationAttempt(username, password)) {
			session.setAttribute("user", username);
			
			return "redirect:/index";
		}
		
		return "login";
		}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "login";
	}

}

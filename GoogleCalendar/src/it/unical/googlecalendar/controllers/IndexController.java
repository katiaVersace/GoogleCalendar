package it.unical.googlecalendar.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unical.googlecalendar.services.DbService;

@Controller
public class IndexController {
	@Autowired
	private DbService dbService;
	
	@RequestMapping("/index")
	public String homePage(Model model, HttpSession session) {
		String email=(String) session.getAttribute("email");
	if(email==null)	return "redirect:/";
		model.addAttribute("events", dbService.stampaEventiPerUtente(email));
		model.addAttribute("calendars", dbService.getCalendarsForUser(email));
		
		return "index";
	}
	
	

}

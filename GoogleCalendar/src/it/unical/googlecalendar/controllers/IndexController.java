package it.unical.googlecalendar.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.*;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.services.DbService;

@Controller
public class IndexController {
	@Autowired
	private DbService dbService;
	
	@RequestMapping("/index")
	public String homePage(Model model, HttpSession session) {
		String email=(String) session.getAttribute("email");
		
		if(email==null)	return "redirect:/";
		
		Collection<Calendar> collectionCalendar = dbService.getCalendarsForUser(email);
		
		model.addAttribute("events", dbService.stampaEventiPerUtente(email));
		model.addAttribute("calendars", collectionCalendar);
		model.addAttribute("test", (new Gson()).toJson(collectionCalendar));

		return "index";
	}
	
	

}

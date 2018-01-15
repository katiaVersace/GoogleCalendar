package it.unical.googlecalendar.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		model.addAttribute("calendars", dbService.getCalendarsForEmail(email));

		return "index";
	}
	
	@RequestMapping(value = "/delete/{calendarId}",method = RequestMethod.POST)
	@ResponseBody
	public String deleteCalendarId(@PathVariable("calendarId") String calendarId){
		return dbService.deleteCalendarById(Integer.parseInt(calendarId)) ? "YES" : "NO";
	}
}
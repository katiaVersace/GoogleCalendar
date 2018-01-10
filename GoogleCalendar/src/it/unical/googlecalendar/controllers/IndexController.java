package it.unical.googlecalendar.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unical.googlecalendar.services.BlogService;

@Controller
public class IndexController {
	@Autowired
	private BlogService dbService;
	
	@RequestMapping("/index")
	public String homePage(Model model, HttpSession session) {
	if(session.getAttribute("user")==null)	return "redirect:/";
		model.addAttribute("events", dbService.stampaTuttiGliEventi());
		
		return "index";
	}
	
	

}

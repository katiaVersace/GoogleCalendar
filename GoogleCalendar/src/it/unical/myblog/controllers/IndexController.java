package it.unical.myblog.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unical.myblog.services.BlogService;

@Controller
public class IndexController {
	@Autowired
	private BlogService blogService;
	
	@RequestMapping("/index")
	public String homePage(Model model, HttpSession session) {
	if(session.getAttribute("user")==null)	return "redirect:/";
		model.addAttribute("posts", blogService.getPosts());
		return "index";
	}
	
	@RequestMapping("/comment")
	public String addComment(@RequestParam int post, @RequestParam String comment, HttpSession session, Model model) {
		blogService.addComment(post, comment, session.getAttribute("user").toString());
		model.addAttribute("posts", blogService.getPosts());
		return "redirect:/";
	}

}

package it.unical.googlecalendar.controllers;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.unical.googlecalendar.model.User;
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
	  
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine
	  @RequestMapping(value = "/insertNewCalendar/{user_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewCalendar(@PathVariable("user_id") String user_id, @RequestParam String title,@RequestParam String description){

		  return dbService.insertNewCalendar(Integer.parseInt(user_id), title, description) ;
	  }

	 
	  @RequestMapping(value = "/update/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateCalendar(@PathVariable("calendar_id")String calendar_id, @RequestParam String title,@RequestParam String description){
		  return dbService.updateCalendarById(Integer.parseInt(calendar_id),title,description) ? "YES" : "NO";
		  
	  }
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine (manca la ripetizione negli eventi)
	  @RequestMapping(value = "/insertNewEvent/{calendar_id}/{creator_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewEvent(@PathVariable("calendar_id")String calendar_id,@PathVariable("creator_id")String creator_id,@RequestParam String title,@RequestParam Date data,@RequestParam String description){

		  return dbService.insertNewEvent(Integer.parseInt(calendar_id),Integer.parseInt(creator_id), title,data, description) ;
	  }
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine
	  @RequestMapping(value = "/insertNewMemo/{calendar_id}/{creator_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewMemo(@PathVariable("calendar_id")String calendar_id,@PathVariable("creator_id")String creator_id,@RequestParam String title,@RequestParam Date data,@RequestParam String description){

		  return dbService.insertNewMemo(Integer.parseInt(calendar_id),Integer.parseInt(creator_id), title,data, description) ;
	  }

	  
	  @RequestMapping(value = "/deleteOccurrence/{occurrenceId}",method = RequestMethod.POST)
	  @ResponseBody
	  public String deleteOccurrenceId(@PathVariable("occurrenceId") String occurrenceId){

		  return dbService.deleteOccurrenceById(Integer.parseInt(occurrenceId)) ? "YES" : "NO";
	  }
	  
	  @RequestMapping(value = "/updateEvent/{occurrence_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateEvent(@PathVariable("occurrence_id")String occurrence_id, @RequestParam String title,@RequestParam Date data,@RequestParam String description){
		  return dbService.updateEventById(Integer.parseInt(occurrence_id),title,data,description) ? "YES" : "NO";
		  
	  }
	  @RequestMapping(value = "/updateMemo/{occurrence_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateMemo(@PathVariable("occurrence_id")String occurrence_id, @RequestParam String title,@RequestParam Date data,@RequestParam String description){
		  return dbService.updateMemoById(Integer.parseInt(occurrence_id),title,data,description) ? "YES" : "NO";
		  
	  }
	  
	  @RequestMapping(value = "/updateUser/{user_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateUser(@PathVariable("user_id")String user_id, @RequestParam String username,@RequestParam String password){
		  return dbService.updateUserById(Integer.parseInt(user_id),username,password) ? "YES" : "NO";
		  
	  }
	  
	  


}
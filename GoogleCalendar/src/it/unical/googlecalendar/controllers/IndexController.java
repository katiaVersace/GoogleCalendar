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
	  public String deleteCalendarId(@PathVariable("calendarId") String calendarId,HttpSession session){

		  return dbService.deleteCalendarById(Integer.parseInt(calendarId),(Integer)session.getAttribute("user_id")) ? "YES" : "NO";
	  }
	 
	  
	  @RequestMapping(value = "/disconnect/{calendarId}",method = RequestMethod.POST)
	  @ResponseBody
	  public String disconnectFromCalendarId(@PathVariable("calendarId") String calendarId,HttpSession session){

		  return dbService.disconnectMeByCalendar((Integer)session.getAttribute("user_id"),Integer.parseInt(calendarId)) ? "YES" : "NO";
	  }
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine
	  //FIXME: nel path che verrà chiamato lato client togliere user_id perchè adesso viene preso dalla sessione
	  @RequestMapping(value = "/insertNewCalendar",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewCalendar(@PathVariable("user_id") String user_id, @RequestParam String title,@RequestParam String description, HttpSession session){

		  return dbService.insertNewCalendar((Integer)session.getAttribute("user_id"), title, description) ;
	  }

	//FIXME: nel path che verrà chiamato lato client togliere user_id perchè adesso viene preso dalla sessione
	@RequestMapping(value = "/update/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateCalendar(HttpSession session,@PathVariable("calendar_id")String calendar_id, @RequestParam String title,@RequestParam String description){
		  return dbService.updateCalendarById(Integer.parseInt(calendar_id),title,description, (Integer) session.getAttribute("user_id")) ? "YES" : "NO";
		  
	  }
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine (manca la ripetizione negli eventi)
	//FIXME: nel path che verrà chiamato lato client togliere user_id perchè adesso viene preso dalla sessione
	  	  @RequestMapping(value = "/insertNewEvent/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewEvent( HttpSession session,@PathVariable("calendar_id")String calendar_id,@RequestParam String title,@RequestParam Date data,@RequestParam String description){

		  return dbService.insertNewEvent(Integer.parseInt(calendar_id),(Integer) session.getAttribute("user_id"), title,data, description) ;
	  }
	  
	  //se ritorna -1 significa che l'inserimento non è andato a buon fine
	  	//FIXME: nel path che verrà chiamato lato client togliere user_id perchè adesso viene preso dalla sessione
	  		  	  @RequestMapping(value = "/insertNewMemo/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public int insertNewMemo( HttpSession session, @PathVariable("calendar_id")String calendar_id,@RequestParam String title,@RequestParam Date data,@RequestParam String description){

		  return dbService.insertNewMemo(Integer.parseInt(calendar_id),(Integer) session.getAttribute("user_id"), title,data, description) ;
	  }

	  
	  @RequestMapping(value = "/deleteOccurrence/{occurrenceId}/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String deleteOccurrenceId(@PathVariable("occurrenceId") String occurrenceId,@PathVariable("calendar_id") String calendar_id, HttpSession session){

		  return dbService.deleteOccurrenceById(Integer.parseInt(occurrenceId),(Integer)session.getAttribute("user_id"),Integer.parseInt(occurrenceId)) ? "YES" : "NO";
	  }
	  
	  @RequestMapping(value = "/updateEvent/{occurrence_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateEvent(HttpSession session, @PathVariable("occurrence_id")String occurrence_id, @RequestParam String title,@RequestParam Date data,@RequestParam String description){
		  return dbService.updateEventById(Integer.parseInt(occurrence_id),title,data,description,(Integer) session.getAttribute("user_id")) ? "YES" : "NO";
		  
	  }
	  @RequestMapping(value = "/updateMemo/{occurrence_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateMemo(HttpSession session,@PathVariable("occurrence_id")String occurrence_id, @RequestParam String title,@RequestParam Date data,@RequestParam String description){
		  return dbService.updateMemoById(Integer.parseInt(occurrence_id),title,data,description,(Integer) session.getAttribute("user_id")) ? "YES" : "NO";
		  
	  }
	  
	//FIXME: nel path che verrà chiamato lato client togliere user_id perchè adesso viene preso dalla sessione
	 @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
	  @ResponseBody
	  public String updateUser(HttpSession session, @RequestParam String username,@RequestParam String password){
		  return dbService.updateUserById((Integer) session.getAttribute("user_id"),username,password) ? "YES" : "NO";
		  
	  }
	 
	 @RequestMapping(value = "/sendInvitation/{calendar_id}",method = RequestMethod.POST)
	  @ResponseBody
	  public String sendInvitation(HttpSession session, @RequestParam String receiver_email,@PathVariable("calendar_id")String calendar_id,@RequestParam String privilege){
		  return dbService.sendInvitation((Integer) session.getAttribute("user_id"),receiver_email,Integer.parseInt(calendar_id),privilege) ? "YES" : "NO";
		  
	  }
	 
	 @RequestMapping(value = "/myCalendar",method = RequestMethod.POST)
	  @ResponseBody
	  public String getAllMyCalendars(HttpSession session){
			
	 return dbService.getAllMyCalendars((String)session.getAttribute("email"));
		  
	  }
	  
	  


}
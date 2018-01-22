package it.unical.googlecalendar.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unical.googlecalendar.model.Notification;
import it.unical.googlecalendar.services.DbService;

@Controller
public class IndexController {
    @Autowired
    private DbService dbService;

    @RequestMapping("/index")
    public String homePage(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");

        if (email == null)
            return "redirect:/";

        model.addAttribute("events", dbService.stampaEventiPerUtente(email));
        model.addAttribute("calendars", dbService.getCalendarsForEmail(email));

        return "index";
    }
    
    /*
     * deleteCalendarId
     */
    @RequestMapping(value = "/delete/{calendarId}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteCalendarId(@PathVariable("calendarId") String calendarId, HttpSession session) {
        return dbService.deleteCalendarById(Integer.parseInt(calendarId), (Integer) session.getAttribute("user_id"))
                ? "YES"
                : "NO";
    }

    /*
     * disconnectFromCalendar
     */
    @RequestMapping(value = "/disconnect/{calendarId}", method = RequestMethod.POST)
    @ResponseBody
    public String disconnectFromCalendarId(@PathVariable("calendarId") String calendarId, HttpSession session) {
        return dbService.disconnectMeByCalendar((Integer) session.getAttribute("user_id"), Integer.parseInt(calendarId))
                ? "YES"
                : "NO";
    }

    /*
     * insertNewCalendar
     */
    // se ritorna -1 significa che l'inserimento non � andato a buon fine
    // FIXME: nel path che verrà chiamato lato client togliere user_id perché adesso
    // viene preso dalla sessione
    @RequestMapping(value = "/insertNewCalendar", method = RequestMethod.POST)
    @ResponseBody
    public int insertNewCalendar(HttpSession session, @RequestParam String title, @RequestParam String description) {
        return dbService.insertNewCalendar((Integer) session.getAttribute("user_id"), title, description);
    }

    /*
     * updateCalendar
     */
    // FIXME: nel path che verrà chiamato lato client togliere user_id perché adesso
    // viene preso dalla sessione
    @RequestMapping(value = "/update/{calendar_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateCalendar(HttpSession session, @PathVariable("calendar_id") String calendar_id,
            @RequestParam String title, @RequestParam String description) {
        return dbService.updateCalendarById(Integer.parseInt(calendar_id), title, description,
                (Integer) session.getAttribute("user_id")) ? "YES" : "NO";
    }

    /*
     * insertNewEvent
     */
    // se ritorna -1 significa che l'inserimento non è andato a buon fine (manca la
    // ripetizione negli eventi)
    // FIXME: nel path che verrà chiamato lato client togliere user_id perché adesso
    // viene preso dalla sessione
    @RequestMapping(value = "/insertNewEvent/{calendar_id}", method = RequestMethod.POST)
    @ResponseBody
    public int insertNewEvent(HttpSession session, @PathVariable("calendar_id") String calendar_id,
            @RequestParam String title, @RequestParam String description, @RequestParam Date startTime,
            @RequestParam Date endTime, @RequestParam String c1, @RequestParam String c2) {
        return dbService.insertNewEvent(Integer.parseInt(calendar_id), (Integer) session.getAttribute("user_id"), title,
                description, startTime, endTime, c1, c2);
    }

    /*
     * insertNewMemo
     */
    // se ritorna -1 significa che l'inserimento non � andato a buon fine
    // FIXME: nel path che verrà chiamato lato client togliere user_id perché adesso
    // viene preso dalla sessione
    @RequestMapping(value = "/insertNewMemo", method = RequestMethod.POST)
    @ResponseBody
    public int insertNewMemo(HttpSession session, @RequestParam String title, @RequestParam Date data,
            @RequestParam String description, @RequestParam String c1) {
        return dbService.insertNewMemo((Integer) session.getAttribute("user_id"), title, data, description, c1);
    }

    /*
     * deleteOccurrenceId
     */
    @RequestMapping(value = "/deleteOccurrence/{occurrenceId}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOccurrenceId(@PathVariable("occurrenceId") String occurrenceId, HttpSession session) {
        return dbService.deleteOccurrenceById(Integer.parseInt(occurrenceId), (Integer) session.getAttribute("user_id"))
                ? "YES"
                : "NO";
    }

    /*
     * updateEvent
     */
    @RequestMapping(value = "/updateEvent/{occurrence_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateEvent(HttpSession session, @PathVariable("occurrence_id") String occurrence_id,
            @RequestParam String title, @RequestParam String description, @RequestParam Date startTime,
            @RequestParam Date endTime, @RequestParam String c1, @RequestParam String c2) {
        return dbService.updateEventById(Integer.parseInt(occurrence_id), title, description, startTime, endTime, c1,
                c2, (Integer) session.getAttribute("user_id")) ? "YES" : "NO";
    }

    /*
     * updateMemo
     */
    @RequestMapping(value = "/updateMemo/{memo_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateMemo(@PathVariable("memo_id") String memo_id, HttpSession session, @RequestParam String title,
            @RequestParam Date data, @RequestParam String description, @RequestParam String c1) {
        return dbService.updateMemoById(Integer.parseInt(memo_id), (Integer) session.getAttribute("user_id"), title,
                data, description, c1) ? "YES" : "NO";
    }

    /*
     * updateUser
     */
    // FIXME: nel path che verrà chiamato lato client togliere user_id perché adesso
    // viene preso dalla sessione
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public String updateUser(HttpSession session, @RequestParam String username, @RequestParam String password) {
        return dbService.updateUserById((Integer) session.getAttribute("user_id"), username, password) ? "YES" : "NO";
    }

    /*
     * sendInvitation
     */
    @RequestMapping(value = "/sendInvitation/{calendar_id}", method = RequestMethod.POST)
    @ResponseBody
    public String sendInvitation(HttpSession session, @RequestParam String receiver_email,
            @PathVariable("calendar_id") String calendar_id, @RequestParam String privilege) {
        return dbService.sendInvitation((Integer) session.getAttribute("user_id"), receiver_email,
                Integer.parseInt(calendar_id), privilege) ? "YES" : "NO";
    }
    
    /*
     * answerInvitation
     */
    @RequestMapping(value = "/answerInvitation/{invitation_id}", method = RequestMethod.POST)
    @ResponseBody
    public String answerInvitation(HttpSession session,@RequestParam String answer,
            @PathVariable("invitation_id") String invitation_id) {
        return dbService.answerInvitation(Integer.parseInt(invitation_id), (Integer) session.getAttribute("user_id"), answer);
    }
    

    /*
     * deleteMemoById
     */
    @RequestMapping(value = "/deleteMemo/{memo_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMemoById(@PathVariable("memo_id") String memo_id, HttpSession session) {
        return dbService.deleteMemoById(Integer.parseInt(memo_id), (Integer) session.getAttribute("user_id")) ? "YES"
                : "NO";
    }

    /*
     * addAlarm
     */
    @RequestMapping(value = "/addAlarm/{occurrence_id}", method = RequestMethod.POST)
    @ResponseBody
    public int addAlarm(HttpSession session, @RequestParam int minutes,
            @PathVariable("occurrence_id") String occurrence_id) {
        return dbService.addAlarm((Integer) session.getAttribute("user_id"), Integer.parseInt(occurrence_id), minutes);
    }

    // FIXME: alarm_id -> occurrence_id
    // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/updateAlarm/{alarm_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlarm(HttpSession session, @RequestParam int minutes,
            @PathVariable("alarm_id") String alarm_id) {
        return dbService.updateAlarm(Integer.parseInt(alarm_id), minutes) ? "YES" : "NO";
    }

    // FIXME: alarm_id -> occurrence_id
    // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/deleteAlarm/{alarm_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAlarm(HttpSession session,
            @PathVariable("alarm_id") String alarm_id) {
        return dbService.deleteAlarm(Integer.parseInt(alarm_id), (Integer) session.getAttribute("user_id")) ? "YES"
                : "NO";
    }

    /*
     * JSON_getAllCalendars
     */
    @RequestMapping(value = "/JSON_getAllMyCalendars", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getAllMyCalendars(HttpSession session) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getAllMyCalendars((String) session.getAttribute("email")));
    }

    /*
     * JSON_getMyEventsInPeriod
     */
    @RequestMapping(value = "/JSON_getMyEventsInPeriod/{calendar_id}", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getMyEventsInPeriod(HttpSession session, @PathVariable("calendar_id") String calendar_id,
            @RequestParam String start, @RequestParam String end) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getMyEventsInPeriod((String) session.getAttribute("email"),
                Integer.parseInt(calendar_id), start, end));
    }

    @RequestMapping(value = "/JSON_getMyAlarms", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getMyAlarms(HttpSession session) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getMyAlarms((Integer) session.getAttribute("user_id")));
    }

    /*
     * JSON_getAlarmForAnOccurrence
     */
    @RequestMapping(value = "/JSON_getAlarmForOccurrence/{occurrence_id}", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getAlarmForAnOccurrence(HttpSession session,
            @PathVariable("occurrence_id") String occurrence_id) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getTheAlarmForAnOccurrence((Integer) session.getAttribute("user_id"),
                Integer.parseInt(occurrence_id)));
    }

    /*
     * JSON_getUnsentNotifications called by user at login
     */
    @RequestMapping(value = "/resetSentStateByUserId", method = RequestMethod.POST)
    @ResponseBody
    public String resetSentStateByUserId(HttpSession session) {
    	 return dbService.resetSentState((Integer) session.getAttribute("user_id")) ? "YES"  : "NO";
        
    }
    
    
    /*
     * L'utente notifica che ha letto le notifiche
     */
    @RequestMapping(value = "/deleteNotifications", method = RequestMethod.POST)
    @ResponseBody
    public String deleteNotifications(HttpSession session) {
        return dbService.deleteNotifications((Integer) session.getAttribute("user_id")) ? "YES"  : "NO";
    }
    

    /*
     * JSON_getMyInvitations
     */
    @RequestMapping(value = "/JSON_getMyInvitations", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getMyInvitations(HttpSession session) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getMyInvitation((Integer) session.getAttribute("user_id")));
    }

    /*
     * JSON_getMyMemos
     */
    @RequestMapping(value = "/JSON_getMyMemos", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_getMyMemos(HttpSession session) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.getMyMemos((Integer) session.getAttribute("user_id")));
    }
    
    /*
     * SSE notification subscription
     */
    @RequestMapping(value = "/notifications")
    public void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        	response.setContentType("text/event-stream");
        	response.setCharacterEncoding("UTF-8");
        	
        	PrintWriter writer = response.getWriter();
        	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        	
        	if(((Integer) session.getAttribute("user_id")) != null) {
            	writer.write("data: " + gson.toJson(dbService.getUnsentNotifications((Integer) session.getAttribute("user_id"))) + "\n\n");
            	writer.flush();
        	}
        	writer.close();
    }
    
    /*
     * SSE invitations subscription
     */
    @RequestMapping(value = "/invitations")
    public void doGetInvitations(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        	response.setContentType("text/event-stream");
        	response.setCharacterEncoding("UTF-8");
        	
        	PrintWriter writer = response.getWriter();
        	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        	
        	if(((Integer) session.getAttribute("user_id")) != null) {
            	writer.write("data: " + gson.toJson(dbService.getUnsentInvitations((Integer) session.getAttribute("user_id"))) + "\n\n");
            	writer.flush();
        	}
        	writer.close();
    }
    
    /*
     * JSON_searchEmailInDb
     */
    @RequestMapping(value = "/JSON_searchEmailInDb", method = RequestMethod.POST)
    @ResponseBody
    public String JSON_searchEmailInDb( @RequestParam String emailToSearch) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(dbService.searchEmail(emailToSearch));
    }
    
    /*
     * insertNewRepetition
     */
    // se ritorna -1 significa che l'inserimento non � andato a buon fine 
    @RequestMapping(value = "/insertNewRepetition/{occurrence_id}", method = RequestMethod.POST)
    @ResponseBody
    public int insertNewRepetition(HttpSession session, @PathVariable("occurrence_id") String occurrence_id,
            @RequestParam int numR,@RequestParam String rType,@RequestParam Date sT,@RequestParam Date eT) {
    	   return dbService.insertNewRepetition(Integer.parseInt(occurrence_id),numR,rType,(Integer) session.getAttribute("user_id"),sT,eT);
    }
    
    /*
     * updateRepetition
     */
    // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/updateRepetition/{repetition_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateRepetition(HttpSession session, @RequestParam int numR, @RequestParam String rType,@RequestParam Date st,@RequestParam Date et,
            @PathVariable("repetition_id") String repetition_id) {
        return dbService.updateRepetition(Integer.parseInt(repetition_id), numR,rType,st,et,(Integer) session.getAttribute("user_id")) ? "YES" : "NO";
    }

    /*
     * deleteRepetition
     */
    // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/deleteRepetition/{repetition_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRepetition(HttpSession session,@PathVariable("repetition_id") String repetition_id) {
        return dbService.deleteRepetition(Integer.parseInt(repetition_id), (Integer) session.getAttribute("user_id")) ? "YES"
                : "NO";
    }

    
     /*
     * insertNewException
     */
    // se ritorna -1 significa che l'inserimento non � andato a buon fine 
    @RequestMapping(value = "/insertNewException/{repetition_id}", method = RequestMethod.POST)
    @ResponseBody
    public int insertNewException(HttpSession session, @PathVariable("repetition_id") String repetition_id,
            @RequestParam Date sT,@RequestParam Date eT) {
    	   return dbService.insertNewException(Integer.parseInt(repetition_id),sT,eT,(Integer) session.getAttribute("user_id"));
    }

    /*
     * updateException
     */
 // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/updateException/{exception_id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateException(HttpSession session, @RequestParam Date st,@RequestParam Date et,
            @PathVariable("exception_id") String exception_id) {
        return dbService.updateException(Integer.parseInt(exception_id), st,et,(Integer) session.getAttribute("user_id")) ? "YES" : "NO";
    }

    /*
     * deleteException
     */
    // TODO: Giuseppe aggiungi questo metodo!!
    @RequestMapping(value = "/deleteException/{exception_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteException(HttpSession session,@PathVariable("exception_id") String exception_id) {
        return dbService.deleteException(Integer.parseInt(exception_id), (Integer) session.getAttribute("user_id")) ? "YES"
                : "NO";
    }

}
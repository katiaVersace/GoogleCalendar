package it.unical.googlecalendar.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
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

	@RequestMapping(value = "/getFBData", method = RequestMethod.POST)
	@ResponseBody
	public String fbDataRequest(HttpServletRequest request, HttpSession session, HttpServletResponse response) {

		final JSONObject result = new JSONObject(request.getParameter("resultJSON"));

		String username = result.getString("name");
		String birthday = result.getString("birthday");
		String emailFB = result.getString("email");
		
		if (!loginService.existsUser(emailFB)) { // se l'utente non esiste gia
													// lo sto creando devo
													// creare il calendario

			if (loginService.creaUtenteFB(emailFB, username)) {
				int user_id = loginService.getId(emailFB);
				session.setAttribute("username", username);
				session.setAttribute("email", emailFB);
				session.setAttribute("user_id", user_id);
				// creazione calendario e birthday
				int calendar_id = loginService.createFbCalendar(user_id, "Facebook Calendar", "My events on Facebook");
				SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");

				try {
					loginService.insertNewFBEvent(calendar_id, user_id, "My Birthday", "Il mio compleanno",
							sdf.parse(birthday), sdf.parse(birthday), "#000000", "#000000");

					// Riempimento calendario con eventi fb
					SimpleDateFormat fbFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");

					JSONArray events = result.getJSONArray("events");

					final int n = events.length();
					for (int i = 0; i < n; i++) {
						
						loginService.insertNewFBEvent(calendar_id, user_id, events.getJSONObject(i).getString("name"),

								events.getJSONObject(i).getString("description"),
								fbFormat.parse(events.getJSONObject(i).getString("date")),
								fbFormat.parse(events.getJSONObject(i).getString("date")), "#000000", "#000000");

					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			
				//
				return "index";
			} else
				return "login";
		}

		// se l'utente esiste già svuoto calendario fb e lo riempio di eventi fb

		int user_id = loginService.getId(emailFB);
		
		Calendar fbCalendar = loginService.getFBCalendar(user_id);
		
		for (Occurrence o : fbCalendar.getOccurrences()) {
			loginService.deleteEventsById(o.getId(), user_id);

		}

		// riempimento
		SimpleDateFormat fbFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");

		JSONArray events = result.getJSONArray("events");

		final int n = events.length();
		for (int i = 0; i < n; i++) {
			
			try {
				loginService.insertNewFBEvent(fbCalendar.getId(), user_id, events.getJSONObject(i).getString("name"),
						events.getJSONObject(i).getString("description"),
						fbFormat.parse(events.getJSONObject(i).getString("date")),
						fbFormat.parse(events.getJSONObject(i).getString("date")), "#000000", "#000000");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		session.setAttribute("username", loginService.getUsername(emailFB));
		session.setAttribute("user_id", loginService.getId(emailFB));
		session.setAttribute("email", emailFB);

		return "index";

	}

}

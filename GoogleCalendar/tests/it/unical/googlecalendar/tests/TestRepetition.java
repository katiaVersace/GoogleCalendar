package it.unical.googlecalendar.tests;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unical.googlecalendar.configuration.AppConfiguration;
import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.ExceptionDAO;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.dao.RepetitionDAO;
import it.unical.googlecalendar.dao.UserDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Repetition;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class TestRepetition {

	@Autowired
	private UserDAOImpl udao;
	@Autowired
	private CalendarDAOImpl cdao;
	@Autowired
	private OccurrenceDAOImpl odao;
	@Autowired
	private RepetitionDAO rdao;
	@Autowired
	private ExceptionDAO edao;

	@Test
	public void saveTest() {

		User ciccino = new User("c@jj.it", "ciccino", "1234");
		udao.save(ciccino);
		Calendar ciccinoCalendar = new Calendar(ciccino, "ciccino'ss Calendar", "list of ciccino's events",false);
		Calendar ciccinoCalendar2 = new Calendar(ciccino, "ciccino'ss Calendar2", "list2 of ciccino's events",false);
		cdao.save(ciccinoCalendar);
		cdao.save(ciccinoCalendar2);

		// ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		String dateInStringR = "24-12-2017 16:20:00";
		String dateInStringREnd = "24-02-2018 16:20:00";
		String d1 = "2017-12-15 16:20:00";
		String d2 = "2017-12-15 16:20:00";
		String d3 = "2017-12-15 16:20:00";
		String d4 = "2017-12-25 16:20:00";
		// int minutes=5;
		Occurrence ev3 = null;
		Occurrence ev1 = null;
		Occurrence evR = null;
		Occurrence evR2 = null;
		try {

			ev3 = new Occurrence(ciccinoCalendar, ciccino, "Evento di ciccio", "Ricordati di comprare il latte",
					sdf.parse(dateInString), sdf.parse(dateInString2), Color.black.toString(), Color.BLUE.toString());
			odao.save(ev3);
			// System.out.println("id ev3: s"+ev3.getId());

			ev1 = new Occurrence(ciccinoCalendar, ciccino, "Evento 1 di ciccio", "Ricordati di comprare il 2latte",
					sdf.parse(dateInString), sdf.parse(dateInString2), Color.black.toString(), Color.BLUE.toString());
			odao.save(ev1);

			evR = new Occurrence(ciccinoCalendar, ciccino, "Evento con Ripetizione di ciccio",
					"Evento comn ripetizione", sdf.parse(dateInString), sdf.parse(dateInString), Color.black.toString(),
					Color.BLUE.toString());
			odao.save(evR);
			int r = rdao.insertNewRepetition(evR.getId(), 3, "DAY", ciccino.getId(), sdf.parse(dateInStringR),
					sdf.parse(dateInStringREnd));
			Repetition rep = rdao.getRepetitionById(r);
			edao.insertNewException(rep.getId(), sdf.parse(d1), sdf.parse(d2), ciccino.getId());
			
			
			
			
			
			evR2 = new Occurrence(ciccinoCalendar, ciccino, "Secondo Evento con Ripetizione di ciccio",
					"Evento 2comn ripetizione", sdf.parse(d1), sdf.parse(d2), Color.black.toString(),
					Color.BLUE.toString());
			odao.save(evR2);
			rdao.insertNewRepetition(evR2.getId(), 3, "DAY", ciccino.getId(), sdf.parse(d3), sdf.parse(d4));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// System.out.println(rdao.getAllRepetitions().size());

		String dateInStringst = "2018-01-01 16:20:00";
		String dateInStringend2 = "2018-01-30 16:20:00";

		List<Occurrence> events = odao.getOccurrenceByEmailInPeriod(ciccino.getEmail(), ciccinoCalendar.getId(),
				dateInStringst, dateInStringend2);
		// System.out.println("Size di eventi "+events.size());
		// for(Occurrence o:events){
		// System.out.println("Evento "+o.getTitle()+" "+o.getDescription());
		// }
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		try {
			evR = odao.getOccurrenceById(evR.getId());
//			System.out.println(evR.getRepetition().getId());
//			
//			System.out.println(gson.toJson(evR));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

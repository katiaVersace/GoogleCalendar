package it.unical.googlecalendar.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.googlecalendar.dao.CalendarDAOImpl;
import it.unical.googlecalendar.dao.OccurrenceDAOImpl;
import it.unical.googlecalendar.model.Calendar;
import it.unical.googlecalendar.model.Comment;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.Occurrence;
import it.unical.googlecalendar.model.Post;
import it.unical.googlecalendar.model.User;

@Service
public class BlogService {


	@Autowired
	private OccurrenceDAOImpl odao;
	
	
	public Collection<Occurrence> stampaTuttiGliEventi(){
		
		
		User katia=new User("Katia","1234");	
		Calendar katiaCalendar = new Calendar(katia,"katia's Calendar", "list of katia's events");
				
		
		
		//ora creo un evento e lo associo al mio calendario
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "21-01-2018 10:20:56";
		String dateInString2 = "24-01-2018 16:20:00";
		int minutes=5;
		Occurrence memo1=null;
		Occurrence memo2=null;
		try {
			memo1=new Memo(katiaCalendar,katia,"Comprare il latte",sdf.parse(dateInString),minutes * 60000);
			memo2=new Memo(katiaCalendar,katia,"Comprare il pane",sdf.parse(dateInString2),minutes * 60000);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
		
		
		odao.save(memo1);
		odao.save(memo2);
    	
		
		
		
		return odao.getAllOccurrences();
	}
}

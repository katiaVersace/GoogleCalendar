package it.unical.googlecalendar.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private int id;
	
	@Column(nullable=false)
	private String username;
	
	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String password;

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="users")
	 private List<Calendar> calendars=new ArrayList<Calendar>();
	
	//Occurrences di cui l'user è ospite
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="guests")
	 private List<Occurrence> occurrences=new ArrayList<Occurrence>();
	
	//Calendars di cui l'user è creatore
		@OneToMany(mappedBy = "creator")
		 private List<Calendar> calendarsCreated=new ArrayList<Calendar>();
	
	//Occurrences di cui l'user è creatore
	@OneToMany(mappedBy = "creator")
	 private List<Occurrence> occurrencesCreated=new ArrayList<Occurrence>();
	
	
	

	
	public User() {
		super();
	}

	public User(String email, String username, String password) {
		super();
		this.email=email;
		this.username = username;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Calendar> getCalendars() {
		return calendars;
	}
	
	public void setCalendars(List<Calendar> c){
		this.calendars=c;
	}
	
	public List<Occurrence> getOccurrences() {
		return occurrences;
	}
	
	public void setOccurrences(List<Occurrence> c){
		this.occurrences=c;
	}
	
	public List<Occurrence> getOccurrencesCreated() {
		return occurrencesCreated;
	}
	
	public void setOccurrencesCreated(List<Occurrence> c){
		this.occurrencesCreated=c;
	}
	

	public List<Calendar> getCalendarsCreated() {
		return calendarsCreated;
	}
	
	public void setCalendarsCreated(List<Calendar> c){
		this.calendarsCreated=c;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
	


}

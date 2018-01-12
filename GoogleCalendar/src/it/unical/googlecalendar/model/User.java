package it.unical.googlecalendar.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

	
		
		
	//Occurrences di cui l'user è creatore
	@OneToMany(mappedBy = "creator")
	 private List<Occurrence> occurrencesCreated=new ArrayList<Occurrence>();
	
	//Calendars di cui l'user è creatore

			@OneToMany(mappedBy = "creator")

			 private List<Calendar> calendarsCreated=new ArrayList<Calendar>();

		
	@OneToMany(mappedBy = "user")

    public List<Users_Calendars> users_calendars=new ArrayList<Users_Calendars>();
	

	
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
	public List<Calendar> getCalendarsCreated() {

		return calendarsCreated;

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
	
	
	public List<Users_Calendars> getUsers_Calendars() {
		return users_calendars;
	}
	
		
	public List<Occurrence> getOccurrencesCreated() {
		return occurrencesCreated;
	}
	
	public void setOccurrencesCreated(List<Occurrence> c){
		this.occurrencesCreated=c;
	}
	
	public String getPriviledgesForCalendar(Calendar c){
		for( Users_Calendars uc: users_calendars){
			if(c.getId()==uc.getCalendar().getId()){
				return uc.getPrivileges();
			}
			
	}
		return "";
		
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

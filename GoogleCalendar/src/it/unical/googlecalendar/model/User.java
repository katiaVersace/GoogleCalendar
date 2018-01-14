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

import org.hibernate.annotations.Where;



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

	//calendari a cui è associato l'utente
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    public List<Users_Calendars> users_calendars=new ArrayList<Users_Calendars>();
	    
    //invitation received
    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    public List<Invitation> receivedInvitations=new ArrayList<Invitation>();
       
    
	
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
	
	
	public List<Users_Calendars> getUsers_Calendars() {
		return users_calendars;
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

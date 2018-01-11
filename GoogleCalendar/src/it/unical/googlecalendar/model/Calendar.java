package it.unical.googlecalendar.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Calendar {

	@Id

	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "calendar_id")

	private int id;



	@Column(nullable = false)

	private String title;



	@Column(nullable = false)

	private String description;



	@ManyToOne(cascade = CascadeType.ALL)

	@JoinColumn(name = "user_id", nullable = false)

	private User creator;



	@OneToMany(mappedBy = "calendar")

	private List<Occurrence> occurrences = new ArrayList<Occurrence>();

	

	@OneToMany(mappedBy = "calendar")

    private List<Users_Calendars> users_calendars=new ArrayList<Users_Calendars>();

  

	public Calendar() {
		super();
	}

	public Calendar(User creator, String title, String description) {
		super();

		this.title = title;

		this.description = description;

		setCreator(creator);

		//many to many association tra user e calendar	

		creator.getCalendarsCreated().add(this);
		

	}
	
	//create association between User and Calendar
//	public boolean inviteUserToCalendar(User owner, User guest, String privilege){
//		if(users.contains(owner)&& getAssociationByUser(owner).getPrivileges().equals("ADMINISTRATOR") ){
//			users.add(guest);
//		
//			Users_Calendars association=new Users_Calendars(guest, this, privilege,Color.BLUE, this.title);
//			guest.getUsers_Calendars().add(association);
//			this.users_calendars.add(association);
//			return true;
//		}
//		else return false;
//		
//	}
//	
	public void setCreator(User creator) {

		this.creator = creator;



	}


	
	
	public Users_Calendars getAssociationByUser(User u){
		for( Users_Calendars uc: users_calendars){
			if(uc.getUser().getId()==u.getId())
			{
				return uc;
			}
			
	}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
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
		Calendar other = (Calendar) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

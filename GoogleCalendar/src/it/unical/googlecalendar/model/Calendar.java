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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"calendar_id"})})
public class Calendar {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "calendar_id",unique = true)
	private int id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@OneToMany(mappedBy = "calendar",orphanRemoval=true,cascade=CascadeType.REMOVE)
	private List<Occurrence> occurrences = new ArrayList<Occurrence>();

	// User che condividono questo calendario
	@OneToMany(mappedBy = "calendar",orphanRemoval=true,cascade=CascadeType.ALL)
	private List<Users_Calendars> users_calendars = new ArrayList<Users_Calendars>();

//	// invitation
	@OneToMany(mappedBy = "calendar",orphanRemoval=true,cascade=CascadeType.REMOVE)
	public List<Invitation> Invitations = new ArrayList<Invitation>();

	public Calendar() {
		super();
	}

	public Calendar(User creator, String title, String description) {
		super();

		this.title = title;

		this.description = description;
		Users_Calendars association = null;
		
			association = new Users_Calendars(creator, this, "ADMIN", Color.CYAN, this.title);
		
	}
	

	

	// elimina calendario(se sei ADMIN)
	// elimina R o RW(se sei ADMIN)

	

	public Users_Calendars getAssociationByUser(User u) {
		for (Users_Calendars uc : users_calendars) {
			if (uc.getUser().getId() == u.getId()) {
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

	public List<Users_Calendars> getUsers_calendars() {
		return users_calendars;
	}

	public void setUsers_calendars(List<Users_Calendars> users_calendars) {
		this.users_calendars = users_calendars;
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

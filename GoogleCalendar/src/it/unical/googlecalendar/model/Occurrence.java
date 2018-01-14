package it.unical.googlecalendar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Occurrence {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="occurrence_id")
	protected int id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Date date;

	@ManyToOne (cascade = CascadeType.ALL) 
	@JoinColumn(name="calendar_id", nullable=false)
	private Calendar calendar;
	
	@ManyToOne (cascade = CascadeType.ALL) 
	@JoinColumn(name="user_id", nullable=false)
	private User creator;

	
	
	public Occurrence(){
		super();
	}
	
	public Occurrence(Calendar calendar,User creator,String title, Date date){
		super();
		this.title=title;
		this.date=date;
		//many to one association
		setCalendar(calendar);
		setCreator(creator);
		//One to many association 
		calendar.getOccurrences().add(this);
		
	}
	
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	

	public void setCreator(User creator) {
		this.creator=creator;
		
	}
	
	public User getCreator() {
		return creator;
		
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
		Occurrence other = (Occurrence) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

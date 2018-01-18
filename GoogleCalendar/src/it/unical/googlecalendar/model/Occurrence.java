package it.unical.googlecalendar.model;

import java.awt.Color;
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
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"occurrence_id"})})

public class Occurrence {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="occurrence_id",unique = true)
	@Expose
	protected int id;

	@Column(nullable = false)
	@Expose
	private String title;

	@Column
	private Date startTime;

	@Column
	private Date endTime;
	
	@Column	
	private String description;
	
	@Column
	String primaryColor;
	
	@Column
	String secondaryColor;
	
@Column 
int alarm=0;
	
@ManyToOne (cascade=CascadeType.REFRESH)
	@JoinColumn(name="calendar_id", nullable=false)
	private Calendar calendar;
	
	@ManyToOne  (cascade=CascadeType.REFRESH)
	@JoinColumn(name="user_id", nullable=false)
	private User creator;
	
	public Occurrence(){
		super();
	}
	
	public Occurrence(Calendar calendar,User creator,String title, String description,Date startTime,Date endTime,String c1, String c2, int alarm){
		super();
		this.title=title;
		
		this.startTime=startTime;
		this.endTime=endTime;
		this.primaryColor=c1;
		this.secondaryColor=c2;
		//many to one association
		setCalendar(calendar);
		setCreator(creator);
		//One to many association 
		calendar.getOccurrences().add(this);
		this.description=description;
		this.alarm=alarm;
		
	}
	
	
	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

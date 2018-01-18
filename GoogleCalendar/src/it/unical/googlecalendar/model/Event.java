package it.unical.googlecalendar.model;

import java.awt.Color;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorValue(value = "Event")
public class Event extends Occurrence{
	
	@Column
	private Date startTime;

	@Column
	private Date endTime;
    
	@Column
	private String secondaryColor;
	
	@ManyToOne (cascade=CascadeType.REFRESH)
	@JoinColumn(name="calendar_id", nullable=false)
	private Calendar calendar;
	
	
	
	
	public Event(){
		super();
	}
	
	public Event (Calendar c,String title, String description,Date startTime,Date endTime,String c1,String c2){
		super(title,description,c1);
		this.startTime=startTime;
		this.endTime=endTime;
		this.secondaryColor=c2;
		this.calendar=c;
		c.getOccurrences().add(this);
	
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

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Event other = (Event) obj;
	if (id != other.id)
		return false;
	return true;
}


}

package it.unical.googlecalendar.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"exception_id"})})
public class EventException  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="exception_id",unique = true)
	@Expose
	protected int id;

	@Column(nullable = false)
	@Expose
	private Date startTime;

	@Column(nullable = false)
	@Expose
	private Date endTime;

	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "repetition_id",nullable = false)
	private  Repetition repetition;

	
	
		
	
	public EventException(){
		super();
	}
	
	public EventException (Repetition r, Date startTime,Date endTime){
		this.startTime=startTime;
		this.endTime=endTime;
		
		repetition=r;
		r.getExceptions().add(this);
		
	
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

	public Repetition getRepetition() {
		return repetition;
	}

	public void setRepetition(Repetition repetition) {
		this.repetition = repetition;
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
		EventException other = (EventException) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

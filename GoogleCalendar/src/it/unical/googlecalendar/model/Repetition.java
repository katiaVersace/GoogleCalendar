package it.unical.googlecalendar.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

@Entity
@Embeddable
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"repetition_id"})})
public class Repetition  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="repetition_id",unique = true)
	@Expose
	protected int id;


	@Column
	@Expose
	private int numRepetition;

	@Column
	@Expose
	private String repetitionType;
	
	@OneToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "occurrence_id",nullable = false)
	private Occurrence occurrence;
	
	@OneToMany(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "exception_id",nullable = false)
	private List<EventException> exceptions;

	@Column
	@Expose
	private Date startTime;
	
	@Column
	@Expose
	private Date endTime;
	
	
	public Repetition(){
		super();
	}
	
	public Repetition (Occurrence o, int numR, String rType, Date st, Date et){
		this.numRepetition=numR;
		this.repetitionType=rType;
		this.occurrence=o;
		this.startTime=st;
		this.endTime=et;
		//o.getRepetitions().add(this);
		o.setRepetition(this);
		
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

	public List<EventException> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<EventException> exceptions) {
		this.exceptions = exceptions;
	}

	public int getNumRepetition() {
		return numRepetition;
	}

	public void setNumRepetition(int numRepetition) {
		this.numRepetition = numRepetition;
	}

	public String getRepetitionType() {
		return repetitionType;
	}

	public void setRepetitionType(String repetitionType) {
		this.repetitionType = repetitionType;
	}

	public Occurrence getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(Occurrence occurrence) {
		this.occurrence = occurrence;
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
		Notification other = (Notification) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

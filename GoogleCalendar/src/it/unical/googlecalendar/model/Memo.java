package it.unical.googlecalendar.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorValue(value = "Memo")
public class Memo extends Occurrence {
	

	@Column
	private long repetition;
	
	
	public Memo(){
		super();
	}
	
	public Memo (Calendar calendar,User creator,String title, Date date, long repetition){
		super(calendar,creator,title,date);
		this.repetition=repetition;
	
	}

	public long getRepetition() {
		return repetition;
	}

	public void setRepetition(long repetition) {
		this.repetition = repetition;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Memo other = (Memo) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

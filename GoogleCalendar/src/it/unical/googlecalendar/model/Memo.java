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
	private String description;
	
	
	public Memo(){
		super();
	}
	
	public Memo (Calendar calendar,User creator,String title, Date date, String description){
		super(calendar,creator,title,date);
		this.description=description;
	
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

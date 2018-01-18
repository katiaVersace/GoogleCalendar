package it.unical.googlecalendar.model;

import java.awt.Color;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table
@DiscriminatorValue(value = "Memo")
public class Memo extends Occurrence {
	

	@Column(nullable = false)
	private Date creationDate;

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	
	
	public Memo(){
		super();
	}
	
	public Memo (User creator,String title, Date date, String description,String c1){
		super(title,description,c1);
		
		this.creationDate=date;
		this.user=creator;
		user.getMemos().add(this);
	
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		Memo other = (Memo) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

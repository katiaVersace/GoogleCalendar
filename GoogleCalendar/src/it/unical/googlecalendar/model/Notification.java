package it.unical.googlecalendar.model;

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

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"notification_id"})})
public class Notification  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="notification_id",unique = true)
	protected int id;


	@Column
	private String description;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	
	
	public Notification(){
		super();
	}
	
	public Notification (User user,String description){
		this.description=description;		
		
		this.user=user;
		user.getNotifications().add(this);
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		Notification other = (Notification) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

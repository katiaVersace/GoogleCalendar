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

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"memo_id"})})
public class Memo  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="memo_id",unique = true)
	protected int id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Date creationDate;

	@Column
	private String description;
	
	@Column
	private String primaryColor;

	
	@Column
	private String secondaryColor;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	
	
	public Memo(){
		super();
	}
	
	public Memo (User creator,String title, Date date, String description,String color1){
		this.title=title;
		this.creationDate=date;
		this.description=description;		
		this.primaryColor=color1;
		this.user=creator;
		user.getMemos().add(this);
	
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

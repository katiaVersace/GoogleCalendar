package it.unical.googlecalendar.model;

import java.awt.Color;

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

@Table(
       
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_calendar_id"),
                @UniqueConstraint(columnNames = {"user_id", "calendar_id"})
        }
)

public class Users_Calendars {

	@Id

	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "user_calendar_id",unique = true)
	private int id;

	// association

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "calendar_id",nullable = false)
	private Calendar calendar;

	// additional attributes
	@Column(nullable = false)

	private String privileges;

	@Column(nullable = false)

	private Color color;

	@Column(nullable = false)

	private String calendarName;

	public int getId() {

		return id;

	}

	public void setId(int id) {

		this.id = id;

	}

	public User getUser() {

		return user;

	}

	public void setUser(User user) {

		this.user = user;

	}

	public String getPrivileges() {

		return privileges;

	}

	public void setPrivileges(String privileges) {

		this.privileges = privileges;

	}

	public Color getColor() {

		return color;

	}

	public void setColor(Color color) {

		this.color = color;

	}

	public String getCalendarName() {

		return calendarName;

	}

	public void setCalendar(Calendar c) {

		this.calendar=c;

	}
	public void setCalendarName(String calendarName) {

		this.calendarName = calendarName;

	}

	public Users_Calendars() {

		super();

	}

	public Users_Calendars(User u, Calendar c, String privileges, Color color, String calendarName) {
super();
		this.user = u;

		this.calendar = c;

		this.privileges = privileges;

		this.color = color;

		this.calendarName = calendarName;
	u.getUsers_Calendars().add(this);
	c.getUsers_calendars().add(this);
	
	
	}

	public Calendar getCalendar() {

		return calendar;

	}

}
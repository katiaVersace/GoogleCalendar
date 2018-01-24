package it.unical.googlecalendar.model;

import java.awt.Color;
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
	@Table(
	       
	        uniqueConstraints = {
	                @UniqueConstraint(columnNames = "alarm_id"),
	                @UniqueConstraint(columnNames = {"user_id", "occurrence_id"})
	        }
	)

	public class Alarm {
		@Id

		@GeneratedValue(strategy = GenerationType.AUTO)

		@Column(name = "alarm_id",unique = true)
		@Expose
		
		private int id;

		// association

		@ManyToOne(cascade=CascadeType.REFRESH)
		@JoinColumn(name = "user_id",nullable = false)
		private User user;

		@ManyToOne(cascade=CascadeType.REFRESH)
		@JoinColumn(name = "occurrence_id",nullable = false)
		@Expose
		private Occurrence occurrence;
		
		@Expose
		public int minutes;

		// additional attributes
		@Column(nullable = false)
		@Expose
		private Date alarm;

				public Alarm() {

			super();

		}

		public Alarm(User u, Occurrence o, int minutes) {
	super();
			this.user = u;

			this.occurrence = o;

			alarm=new Date(o.getStartTime().getTime()-minutes*60*1000L);
			u.getAlarms().add(this);
		o.getAlarms().add(this);

		this.minutes=minutes;
		
		}

		
		public int getMinutes() {
			return minutes;
		}

		public void setMinutes(int minutes) {
			this.minutes = minutes;
			alarm=new Date(occurrence.getStartTime().getTime()-minutes*60*1000L);
		}

		public Occurrence getOccurrence() {
			return occurrence;
		}

		public void setOccurrence(Occurrence occurrence) {
			this.occurrence = occurrence;
		}

		public Date getAlarm() {
			return alarm;
		}

		public void setAlarm(int minutes) {
			alarm=new Date(occurrence.getStartTime().getTime()-minutes*60*1000L);
		}

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
}
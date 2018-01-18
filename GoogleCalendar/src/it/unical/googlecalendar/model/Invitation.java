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

//FIXME: mettere come chiave primaria ricevente e calendario, cosi che la stessa persona non possa essere invitata due volte 
//allo stesso calendario da due persone diverse.

@Entity

//chiave receiver_calendar, in questo modo un utente non può essere invitato due volte allo stesso calendario da due persone diverse
@Table(     
        uniqueConstraints = {
        		 @UniqueConstraint(columnNames = {"invitation_id"}),
                @UniqueConstraint(columnNames = {"senderId","user_id", "calendar_id"})
        }
)
public class Invitation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="invitation_id",unique = true)
	private int id;
	
@Column
	private Date timestamp;

	@Column
	public int senderId;

	

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "user_id", nullable = false)
	private User receiver;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "calendar_id", nullable = false)	
	private Calendar calendar;

	@Column(name="privilege")
	private String privilege;
	
	

	
	public Invitation() {
		super();
	}

	public Invitation(int sender, User receiver,Calendar calendar, String privilege) {
		super();
		this.senderId=sender;
		this.receiver=receiver;
		this.calendar=calendar;
		this.privilege=privilege;
		timestamp=new Date();
		
	//	receiver.receivedInvitations.add(this);
		calendar.Invitations.add(this);
	}
	
	
		

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
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
		Invitation other = (Invitation) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
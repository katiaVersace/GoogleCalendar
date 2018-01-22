package it.unical.googlecalendar.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
                @UniqueConstraint(columnNames = {"user_id", "calendar_id"})
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
	@ElementCollection(targetClass=Integer.class)
	public List<Integer> senderId=new ArrayList<>();
	
	@Column
	private boolean sent=false;

	

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
		senderId.add(sender);
		this.receiver=receiver;
		this.calendar=calendar;
		
		this.privilege=privilege;
		timestamp=new Date();
		
		
	//	receiver.receivedInvitations.add(this);
		calendar.Invitations.add(this);
		
	}
	
	
		

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege1) {
		
		if((this.privilege.equals("RW")&& privilege1.equals("ADMIN"))||(this.privilege.equals("R")&& (privilege1.equals("ADMIN")||privilege1.equals("RW")))||this.privilege==null)
			this.privilege=privilege1;
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
	
	public List<Integer> getSendersId() {
		return senderId;
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
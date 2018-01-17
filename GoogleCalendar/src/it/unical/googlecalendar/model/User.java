package it.unical.googlecalendar.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;



@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"user_id"})})
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id",unique = true)
	private int id;
	
	@Column(nullable=false)
	private String username;
	
	@Column(nullable=false,unique = true)
	private String email;
	
	@Column(nullable=false)
	private String password;


	//calendari a cui è associato l'utente
	@OneToMany(mappedBy = "user",orphanRemoval=true,cascade=CascadeType.ALL)
    public List<Users_Calendars> users_calendars=new ArrayList<Users_Calendars>();
    
    
    //promemoria
    @OneToMany(mappedBy = "user",orphanRemoval=true,cascade=CascadeType.ALL)
    private List<Memo> memos=new ArrayList<Memo>();
    
    

	    
   public List<Memo> getMemos() {
		return memos;
	}

	public void setMemos(List<Memo> memos) {
		this.memos = memos;
	}

	//separated by pipe
    @Column
    private String notifications="";   
    
	
	public User() {
		super();
	}

	public User(String email, String username, String password) {
		super();
		this.email=email;
		this.username = username;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
//	public List<Users_Calendars> getUsers_Calendars() {
//		return users_calendars;
//	}
	
	public String[] getNotifications(){
		if(notifications.isEmpty()){
			return null;
		}
		else{
			String[] notif=notifications.split("\\|");
			
			return notif;
		}
		
		
		
	}
		
//	public String getPriviledgesForCalendar(Calendar c){
//		for( Users_Calendars uc: users_calendars){
//			if(c.getId()==uc.getCalendar().getId()){
//				return uc.getPrivileges();
//			}
//			
//	}
//		return null;
//		
//	}
	
//	public void removeAssociationWithCalendar(Calendar c){
//		for(Users_Calendars uc: users_calendars){
//			if(uc.getCalendar()==c)
//				users_calendars.remove(c);
//		}
//	}

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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
	


}

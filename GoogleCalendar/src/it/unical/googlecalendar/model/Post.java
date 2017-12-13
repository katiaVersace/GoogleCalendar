package it.unical.googlecalendar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
	
	private int id;
	  
	private String text;
	  
	private String author;
	  
	private String title;
	  
	private List<Comment> comments = new ArrayList<>();
	private static int postsCounter = 0;
	
	private Date data;
	
	public Post(String text, String title, String author, Date data){
		setText(text);
		setTitle(title);
	    setAuthor(author);
	    id=postsCounter++;
	  this.data=data;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void addComment(Comment c) {
		this.comments.add(c);
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
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

}

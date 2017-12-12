package it.unical.myblog.model;

import java.util.Date;

public class Comment {
	private String text;
	private String author;
	private Date data;
	
	public Comment(String text, String author, Date data){
	setText(text);
	setAuthor(author);
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
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

}

package it.unical.googlecalendar.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.unical.googlecalendar.model.Comment;
import it.unical.googlecalendar.model.Post;

@Service
public class BlogService {

	private List<Post> blog ;
	
	@PostConstruct
	public void initialize() {
		blog= new ArrayList<>();
		Post post=new Post("Il primo post di Katia","Primo Post","Katia", new Date());
		blog.add(post);
		Post post2=new Post("Il secondo post di Katia","Secondo Post","Katia", new Date());
		blog.add(post2);
		blog.get(post.getId()).addComment(new Comment("sono katia e ho inserito il commento *_*","anonimo", new Date()));;
		}
	
	public void addPost(String title,String text,String username) {
		blog.add(new Post(text, title, username, new Date()));
		
		
	}
	
	public void addComment(int idPost,String text, String username) {
		blog.get(idPost).addComment(new Comment(text, username, new Date()));
		
	}
	
	public Collection<Post> getPosts(){
		return blog;
		
	}
public Post getPost(int id){
	return blog.get(id);
}
}

package it.unical.googlecalendar.dao;

import java.util.List;

import it.unical.googlecalendar.model.Author;

public interface AuthorDAO {
	
	void save(Author author);

	List<Author> getAllAuthors();
	
}

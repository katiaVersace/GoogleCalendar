package it.unical.googlecalendar.dao;

import it.unical.googlecalendar.model.Author;
import it.unical.googlecalendar.model.Book;

public interface BooksDAO {

	void save(Book book);
	
	Author getAuthorByTitle(String title);
}

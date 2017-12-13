package it.unical.asde.hibernate_first_example.tests;

import org.junit.Assert;
import org.junit.Test;

import it.unical.googlecalendar.dao.BooksDAO;
import it.unical.googlecalendar.dao.BooksDAOImpl;
import it.unical.googlecalendar.model.Author;
import it.unical.googlecalendar.model.Book;

public class BooksDAOTest {
	
	@Test
	public void basicTest() {
		BooksDAO dao = new BooksDAOImpl();
		
		dao.save(new Book("The lord of the rings", new Author("J.R.R. Tolkien", "English"), "Bompiani"));
		
		Author author = dao.getAuthorByTitle("The lord of the rings");
		
		Assert.assertEquals("J.R.R. Tolkien", author.getName());
	}

}

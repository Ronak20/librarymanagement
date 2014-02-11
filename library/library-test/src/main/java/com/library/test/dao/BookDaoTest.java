package com.library.test.dao;

import junit.framework.Assert;

import org.hibernate.Session;

import com.library.config.HibernateUtil;
import com.library.dao.BookDao;
import com.library.model.Book;

public class BookDaoTest {

	BookDao bookDao;
	Session session;

	public void setUp() throws Exception {
		this.session = HibernateUtil.getSessionFactory().openSession();
		this.bookDao = new BookDao(this.session);
	}

	public void tearDown() throws Exception {
		session.close();
	}

	public void test() {
		Book book = null;
		Assert.assertNull(book);
	}

	public void testAddBook() {
		Book book = new Book("MyFirstBook3", "MyFirstBookisbn3", 0);
		this.bookDao.saveOrUpdate(book);

		Assert.assertSame(bookDao.getBookByName(book.getBookName())
				.getBookName(), book.getBookName());
		System.out.println(book.getBookName());
		// bookDao.deleteBook(book);
	}

	public void testDeleteBook() {
		Book book = new Book("MyFirstBook10", "MyFirstBookisbn10", 10);
		bookDao.saveOrUpdate(book);
		book = bookDao.getBookByName("MyFirstBook10");
		bookDao.deleteBook(book);
		Assert.assertNull(bookDao.getBookByName(book.getBookName()));

		// Book book1 = new Book("1");
		// bookDao.deleteBook(book1);

	}

}

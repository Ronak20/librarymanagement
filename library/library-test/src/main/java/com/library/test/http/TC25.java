package com.library.test.http;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.library.config.Constant;
import com.library.config.HibernateUtil;
import com.library.dao.BookDao;
import com.library.model.Book;
import com.library.service.BookService;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class TC25 extends TestCase {
	private static Logger logger = Logger.getLogger(TC25.class);
	private String bookName = "MyBookName" + System.currentTimeMillis();
	private String isbn = "MyBookisbn" + System.currentTimeMillis();
	private String bookid = "";

	public TC25(String s) {
		super(s);
	}

	public void setUp() throws Exception {
		logger.info("Entered setUp for TC 25 Delete book with multiple copies");
		logger.info("Exit setUp for TC 25 Delete book with multiple copies");
	}

	public void tearDown() throws Exception {
	}

	public void testDeleteBookWithMultipleCopies() throws Exception {
		logger.info("Entered TC25 ");
		Session session = HibernateUtil.getSessionFactory().openSession();
		BookDao bookDao = new BookDao(session);
		BookService bookService = new BookService(bookDao);
		Book book = new Book(null, bookName, isbn, 2);// created 2 copies
		bookService.saveOrUpdate(book);
		bookid = book.getBookid();
		logger.info("Bookid created is: " + bookid);
		// now create loan for this user
		logger.info("trying to delete the bookiD: " + bookid);
		WebConversation conversation = new WebConversation();
		WebRequest requestDeleteBook = new GetMethodWebRequest(
				Constant.BOOK_DELETE_URL + bookid);
		WebResponse responseGetUser = conversation
				.getResponse(requestDeleteBook);
		WebTable bookListUpdatedTable = responseGetUser
				.getTableWithID("bookListTable");
		TableCell tableUpdatedCell = bookListUpdatedTable
				.getTableCellWithID(bookid);
		assertNull(tableUpdatedCell);
		session.close();
		logger.info("Exited TC 25 ");
	}
}

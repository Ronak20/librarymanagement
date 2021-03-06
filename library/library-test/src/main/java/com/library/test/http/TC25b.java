package com.library.test.http;

import java.util.Calendar;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.library.config.Constant;
import com.library.config.HibernateUtil;
import com.library.dao.BookDao;
import com.library.dao.LoanDao;
import com.library.dao.UserDao;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Role;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.LoanService;
import com.library.service.UserService;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class TC25b extends TestCase {
	private static Logger logger = Logger.getLogger(TC25b.class);
	private String bookName = "MyBookName" + System.currentTimeMillis();
	private String isbn = "MyBookisbn" + System.currentTimeMillis();
	private String bookid = "";
	private String userId = "";
	private String loanId = "";
	private Session session;
	private UserDao userDao;
	private BookDao bookDao;
	private LoanDao loanDao;
	private UserService userService;
	private BookService bookService;
	private LoanService loanService;

	public TC25b(String s) {
		super(s);
	}

	public void setUp() throws Exception {
		logger.info("Entered setUp for TC 25b Delete book with multiple copies");
		session = HibernateUtil.getSessionFactory().openSession();
		userDao = new UserDao(session);
		userService = new UserService(userDao);
		bookDao = new BookDao(session);
		loanDao = new LoanDao(session);
		loanService = new LoanService(loanDao);
		bookService = new BookService(bookDao, loanDao);
		logger.info("Exit setUp for TC 25b Delete book with multiple copies");
	}

	public void tearDown() throws Exception {
		// Delete the loan
		logger.info("Entered tear down for TC25b");
		logger.info("trying to delete the loanID: " + loanId);
		loanService.deleteLoanByLoanID(loanId);
		// delete the book
		logger.info("trying to delete the BookID: " + bookid);
		bookService.deleteBook(bookid);
		// delete the user
		logger.info("trying to delete the UserID: " + userId);
		userService.delete(userService.getUserbyUserID(userId));
		// close the session
		session.close();
		logger.info("Exit tear down for TC25b");

	}

	public void testDeleteBookWithMultipleCopies() throws Exception {
		logger.info("Entered TC25b");
		User user;
		String parameterUserName = "MyUser" + System.currentTimeMillis();
		user = new User("TestFirstName", "TestLastName", parameterUserName,
				"password", Role.STUDENT);
		userId = userService.saveOrUpdate(user);
		logger.info("User added" + user.getUsername());
		Book book = new Book(null, bookName, isbn, 2);// created 2 copies
		bookid = bookService.saveOrUpdate(book);
		logger.info("Bookid created is: " + bookid);
		// now create loan for this user
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 5);
		Loan loan1 = new Loan(user.getUserId(), bookid, now.getTime(), 0, 0,
				true);
		loanDao.saveOrUpdate(loan1);
		bookService.decreaseCopies(bookid);
		Loan loan2 = new Loan(user.getUserId(), bookid, now.getTime(), 0, 0,
				true);
		loanDao.saveOrUpdate(loan2);
		bookService.decreaseCopies(bookid);
		logger.info("Loan created are " + loan1.getLoanId() + ","
				+ loan2.getLoanId() + " created for user " + user.getUserId());
		session.refresh(book);
		logger.info("After the loan,Now Book " + bookid + "has "
				+ book.getCopies() + " copies");
		WebConversation conversation = new WebConversation();
		WebRequest requestReturnBook = new GetMethodWebRequest(
				Constant.UNRENT_BOOK_URL + loan1.getLoanId() + "&userid="
						+ user.getUserId());
		WebResponse responseGetUser = conversation
				.getResponse(requestReturnBook);
		session.refresh(book);
		logger.info("After the book is returned ,Now Book " + bookid + "has "
				+ book.getCopies() + " copies");
		logger.info("trying to delete the bookiD: " + bookid);
		WebRequest requestDeleteBook = new GetMethodWebRequest(
				Constant.BOOK_DELETE_URL + bookid);
		responseGetUser = conversation.getResponse(requestDeleteBook);
		WebTable bookListUpdatedTable = responseGetUser
				.getTableWithID("bookListTable");
		TableCell tableUpdatedCell = bookListUpdatedTable
				.getTableCellWithID(bookid);
		assertEquals(bookid, tableUpdatedCell.getText());
		logger.info("Exited TC 25b Delete book with multiple copies");
	}
}

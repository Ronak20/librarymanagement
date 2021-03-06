package com.library.test.http;

import java.io.IOException;
import java.util.UUID;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.xml.sax.SAXException;

import com.library.config.Constant;
import com.library.config.HibernateUtil;
import com.library.config.LogConstant;
import com.library.dao.BookDao;
import com.library.dao.LoanDao;
import com.library.dao.UserDao;
import com.library.model.Book;
import com.library.model.Role;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.LoanService;
import com.library.service.UserService;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;

public class TC11 extends TestCase {

	private static Logger logger = Logger.getLogger(TC11.class);

	private Session session;

	private String isbn;
	private String loanId;
	private String userID;
	private String bookID;

	private BookDao bookDao;
	private UserDao userDao;
	private LoanDao loanDao;

	private BookService bookService;
	private LoanService loanService;
	private UserService userService;
	private User user;

	public void setUp() throws Exception {
		logger.info("Entered setUp");
		UUID uuid = UUID.randomUUID();

		// add user
		session = HibernateUtil.getSessionFactory().openSession();

		loanDao = new LoanDao(session);
		loanService = new LoanService(loanDao);

		userDao = new UserDao(session);
		userService = new UserService(userDao, loanDao);
		user = new User("fName" + uuid, "lName" + uuid, "uName" + uuid, "pWord"
				+ uuid, Role.ADMIN);
		this.userID = userDao.saveOrUpdate(user);

		// add book
		bookDao = new BookDao(session);
		bookService = new BookService(bookDao, loanDao);

		this.isbn = "isbn" + uuid;
		Book book = new Book("bookname" + uuid, isbn, 10);
		this.bookID = bookDao.saveOrUpdate(book);

		loanService.addLoan(this.userID, this.bookID);
		loanId = this.loanService
				.getLoanByUserIdBookId(this.userID, this.bookID).get(0)
				.getLoanId();

		logger.info(LogConstant.EXITED);
	}

	public void tearDown() throws Exception {
		loanService.delete(this.userID, this.bookID);
		bookService.deleteBook(this.bookID);
		userService.delete(this.user);
		session.close();
	}

	public void testRenewExpiredLoan() throws InterruptedException,
			IOException, SAXException {
		logger.info("Entered testRenewExpiredLoan");

		Thread.sleep(6 * 60 * 1000);

		WebConversation conversation = new WebConversation();
		WebRequest requestBookList = new GetMethodWebRequest(
				Constant.getRenewLoanUrl(this.loanId, this.userID));
		conversation.getResponse(requestBookList);

		Assert.assertSame(0, this.loanService.getLoanByID(this.loanId)
				.getRenewalCount());

		logger.info("Exited testRenewExpiredLoan");
	}
}

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
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;

public class TC14a extends TestCase {

	private static Logger logger = Logger.getLogger(TC14a.class);

	private Session session;
	private User user;

	private BookService bookService;
	private LoanService loanService;
	private UserService userService;

	private String isbn;
	private String loanID;
	private String userID;
	private String bookID;

	private BookDao bookDao;
	private UserDao userDao;
	private LoanDao loanDao;

	public TC14a(String s) {
		super(s);
	}

	public void setUp() throws Exception {
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

		loanService = new LoanService(loanDao);
		loanService.addLoan(this.userID, this.bookID);

		Loan loan = loanDao.getLoanByUserIdBookId(userID, bookID).get(0);

		this.loanID = loan.getLoanId();

		logger.info("Exited setUp");
	}

	public void tearDown() throws Exception {
		loanService.deleteLoanByLoanID(loanID);
		bookService.deleteBook(bookID);
		userService.delete(user);
		session.close();
	}

	public void testTC14aReturnLoan() throws IOException, SAXException,
			InterruptedException {
		logger.info("Entered testTC14aReturnLoan");
		// Thread.sleep(4 * 60 * 1000);
		WebConversation conversation = new WebConversation();
		logger.info(loanID + "   " + userID);
		WebRequest requestReturnBook = new GetMethodWebRequest(
				Constant.getReturnBookUrl(loanID, userID));
		conversation.getResponse(requestReturnBook);

		Loan loan = this.loanService.getLoanByID(loanID);
		Assert.assertNull(loan);

		logger.info("Exited testTC14aReturnLoan");
	}
}

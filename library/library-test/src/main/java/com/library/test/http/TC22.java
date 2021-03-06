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
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;

public class TC22 extends TestCase {

	private static Logger logger = Logger.getLogger(TC22.class);

	private Session session;
	private LoanService loanService;
	private String isbn;
	private String loanID;
	private String userID;
	private String bookID;
	private BookDao bookDao;
	private UserDao userDao;
	private BookService bookService;
	private LoanDao loanDao;
	
	public TC22(String s)
	{
		super(s);
	}

	public void setUp() throws Exception {
		logger.info("Entered setUp");
		UUID uuid = UUID.randomUUID();

		// add user
		session = HibernateUtil.getSessionFactory().openSession();
		userDao = new UserDao(session);
		User user = new User("fName" + uuid, "lName" + uuid, "uName" + uuid,
				"pWord" + uuid, Role.ADMIN);
		this.userID = userDao.saveOrUpdate(user);

		// add book
		bookDao = new BookDao(session);
		bookService = new BookService(bookDao);

		this.isbn = "isbn" + uuid;
		Book book = new Book("bookname" + uuid, isbn, 10);
		this.bookID = bookDao.saveOrUpdate(book);

		loanDao = new LoanDao(session);
		loanService = new LoanService(loanDao);
		loanService.addLoan(this.userID, this.bookID);
		this.loanID = loanDao.getLoanByUserIdBookId(userID, bookID).get(0)
				.getLoanId();
		bookService.decreaseCopies(this.bookID);
		logger.info("Exited setUp");
	}

	public void tearDown() throws Exception {
		session.close();
	}

	 
	public void testTC22BorrowBookAfterPayingFine()
			throws InterruptedException, IOException, SAXException {
		logger.info("Entered testTC22BorrowBookAfterPayingFine");
		// Thread.sleep(4*60*1000);
		logger.info(" loanID : " + loanID + " bookID : " + bookID
				+ " userID : " + userID);
		WebConversation conversation = new WebConversation();
		WebRequest requestLoanRenewal = new GetMethodWebRequest(
				Constant.getRenewLoanUrl(loanID, userID));
		// renew the loan
		conversation.getResponse(requestLoanRenewal);
		// let the loan expire
		Thread.sleep(4 * 60 * 1000);
		// pay the late fee
		WebRequest requestPayFine = new GetMethodWebRequest(
				Constant.getPayFeeUrl(loanID, userID));
		conversation.getResponse(requestPayFine);
		// try to get the loan again
		WebRequest requestLoan = new GetMethodWebRequest(Constant.RENT_BOOK_URL
				+ bookID + "&userid=" + userID);
		conversation.getResponse(requestLoan);
		Loan loan = loanDao.getLoanByUserIdBookId(userID, bookID).get(0);
		logger.debug(loan);
		Assert.assertNotNull(loan);

		logger.info("Exited testTC22BorrowBookAfterPayingFine");
	}

}

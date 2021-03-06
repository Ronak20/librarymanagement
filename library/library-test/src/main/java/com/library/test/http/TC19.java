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

public class TC19 extends TestCase{

	private static Logger logger = Logger.getLogger(TC19.class);

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
	
	public TC19(String s)
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

		//create loan
		loanDao = new LoanDao(session);
		loanService = new LoanService(loanDao);
		loanService.addLoan(this.userID, this.bookID);
		loanID = this.loanDao.getLoanByUserIdBookId(userID, bookID).get(0).getLoanId(); 
		bookService.decreaseCopies(this.bookID);

		
		logger.info("Exited setUp");
	}

	public void tearDown() throws Exception {
		loanDao.deleteById(loanID);
		userDao.delete(userDao.getUserById(userID));
		bookDao.deleteBook(bookDao.getBookByID(bookID));
		session.close();
	}
	
	 
	public void testTC19PayFine() throws InterruptedException, IOException, SAXException
	{
			    
		logger.info("Entered testTC19PayFine");
		logger.info(" loanID : "+loanID+" bookID : "+bookID+" userID : "+userID);
		Thread.sleep(6 * 60 * 1000);
		WebConversation conversation = new WebConversation();
		WebRequest requestPayFine = new GetMethodWebRequest(
				Constant.getPayFeeUrl(loanID,userID ));
		conversation.getResponse(requestPayFine);
		Loan loan = loanDao.getLoanByUserIdBookId(userID, bookID).get(0);
		logger.debug(loan);
		
		Assert.assertTrue(loan.getIsLateFeePaid());
		Assert.assertEquals(0,loan.getLateFee());
		
		logger.info("Exited testTC19PayFine");
	}

}

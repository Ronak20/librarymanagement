package com.library.test.http;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.library.config.Constant;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class TC24 extends TestCase {
	private static Logger logger = Logger.getLogger(TC24.class);

	public void setUp() throws Exception {
		logger.info("Entered setUp");

		logger.info("Exited setUp");
	}

	public void tearDown() throws Exception {

	}

	public void testTC24RemoveTitle() throws Exception {
		logger.info("Entered testTC24RemoveTitle");
		WebConversation conversation = new WebConversation();
		WebRequest requestAdd = new GetMethodWebRequest(Constant.BOOK_ADD_URL);
		WebResponse responseAdd = conversation.getResponse(requestAdd);
		WebForm addBookForm = responseAdd.getFormWithID("addBookForm");
		addBookForm.setParameter("bookname",
				"Mybook" + System.currentTimeMillis());
		addBookForm.setParameter("copies",
				"" + (int) (System.currentTimeMillis()));
		String isbn = "testisbn" + (int) (System.currentTimeMillis());
		addBookForm.setParameter("isbn", isbn);
		addBookForm.submit();

		logger.debug("One book added");
		logger.debug("getting retriving book id");

		WebRequest request3 = new GetMethodWebRequest(Constant.BOOK_GET_URL);
		WebResponse response3 = conversation.getResponse(request3);
		WebTable bookListTable = response3.getTableWithID("bookListTable");
		TableCell tableCell = bookListTable.getTableCell(
				bookListTable.getRowCount() - 1, 0);
		String bookidDelete = tableCell.getText();
		logger.debug("bookidDelete : " + bookidDelete);
		TableCell tableDeleteCell = bookListTable.getTableCell(
				bookListTable.getRowCount() - 1,
				bookListTable.getColumnCount() - 1);

		logger.debug(tableDeleteCell.getText());

		// checking whether book is deleted or not
		logger.debug("checking deleted book");
		WebRequest requestGetBook = new GetMethodWebRequest(
				Constant.BOOK_DELETE_URL + bookidDelete);
		WebResponse responseGetBook = conversation.getResponse(requestGetBook);
		WebTable bookListUpdatedTable = responseGetBook
				.getTableWithID("bookListTable");
		TableCell tableUpdatedCell = bookListUpdatedTable.getTableCell(
				bookListUpdatedTable.getRowCount() - 1,
				bookListUpdatedTable.getColumnCount() - 3);
		logger.debug("Updated copies : " + tableUpdatedCell.getText());
		Assert.assertNotSame(isbn, tableUpdatedCell.getText());

		logger.info("Exited testTC24RemoveTitle");
	}
}

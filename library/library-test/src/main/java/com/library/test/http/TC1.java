package com.library.test.http;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.library.config.Constant;
import com.library.config.HibernateUtil;
import com.library.dao.UserDao;
import com.library.model.User;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class TC1 extends TestCase {
	private static Logger logger = Logger.getLogger(TC1.class);
	public String parameterUserName = "MyUser" + System.currentTimeMillis();

	public void setUp() throws Exception {
		logger.info("Entered setUp for CreateUserTest");
		logger.info("Exited setUp");
	}

	public void tearDown() throws Exception {
		logger.info("Entered teadDown of TC1 CreateUser");
		// delete the user created
		Session session = HibernateUtil.getSessionFactory().openSession();
		UserDao userDao = new UserDao(session);
		logger.info("trying to delete the user with UserName: "
				+ parameterUserName);
		User user = userDao.getUserByName(parameterUserName);
		userDao.delete(user);
		session.close();
		logger.info("Exited teadDown of TC1 CreateUser");
	}

	public void testCreateUser() throws Exception {
		logger.debug("Entered testTC1AddUser");
		WebConversation conversation = new WebConversation();
		WebRequest request = new GetMethodWebRequest(Constant.CREATE_USER_URL);
		WebResponse response = conversation.getResponse(request);
		WebForm createUserForm = response.getFormWithID("createUserForm");
		logger.debug("Create User Form : \n" + response.getText());
		// String parameterUserName = "MyUser" + System.currentTimeMillis();
		createUserForm.setParameter("username", parameterUserName);
		createUserForm.setParameter("firstname",
				"TestFirstName" + System.currentTimeMillis());
		createUserForm.setParameter("lastname",
				"TestLastName" + System.currentTimeMillis());
		createUserForm.setParameter("password",
				"password" + System.currentTimeMillis());
		createUserForm.setParameter("role", "Student");
		SubmitButton createUserSubmitButton = createUserForm
				.getSubmitButton("submitbutton");
		createUserForm.submit(createUserSubmitButton);

		WebRequest requestUserList = new GetMethodWebRequest(
				Constant.USER_GET_URL);
		WebResponse responseUserList = conversation
				.getResponse(requestUserList);
		WebTable userListTable = responseUserList
				.getTableWithID("userListTable");

		TableCell tableCell = userListTable
				.getTableCellWithID(parameterUserName);
		logger.info("parameterUserName" + " = " + tableCell.getText());
		Assert.assertEquals(parameterUserName, tableCell.getText());

		logger.info("Exited testTC1AddUser");
	}

}

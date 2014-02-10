package com.library.test.dao;

import junit.framework.Assert;

import org.hibernate.Session;

import com.library.config.HibernateUtil;
import com.library.dao.UserDao;
import com.library.model.Role;
import com.library.model.User;

public class UserDaoTest {

	UserDao userDao;
	Session session;

	public UserDaoTest() {

	}

	public void setUp() {
		this.session = HibernateUtil.getSessionFactory().openSession();
		this.userDao = new UserDao(this.session);
	}

	public void tearDown() {
		this.session.close();
	}

	public void testUserSave() {
		User user = new User("Admin", "Admin", "admin", "admin", Role.ADMIN);
		this.userDao.saveOrUpdate(user);

		Assert.assertNotNull(userDao.getAll());
	}

	public void testUserDelete() {
		User user = new User("2");
		this.userDao.delete(user);
	}

	public void getUserById() {

		User user = new User("getFirstTest", "getLastTest", "getByUNTest",
				"sompass", null);
		this.userDao.saveOrUpdate(user);

		Assert.assertSame(user, this.userDao.getUserByName("getByUNTest"));

		userDao.delete(user);
	}

}

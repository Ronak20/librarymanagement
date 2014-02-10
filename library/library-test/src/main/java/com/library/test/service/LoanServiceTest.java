package com.library.test.service;

import org.hibernate.Session;

import com.library.config.HibernateUtil;
import com.library.dao.LoanDao;
import com.library.service.LoanService;

public class LoanServiceTest {

	LoanService loanService;
	LoanDao loanDao;
	Session session;

	public void setUp() throws Exception {
		this.session = HibernateUtil.getSessionFactory().openSession();
		this.loanDao = new LoanDao(this.session);
		this.loanService = new LoanService(this.loanDao);
	}

	public void tearDown() throws Exception {
		this.session.close();
	}

}

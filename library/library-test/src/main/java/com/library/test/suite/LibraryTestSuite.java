package com.library.test.suite;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.library.test.http.TC1;
import com.library.test.http.TC10;
import com.library.test.http.TC11;
import com.library.test.http.TC12;
import com.library.test.http.TC13;
import com.library.test.http.TC14a;
import com.library.test.http.TC14c;
import com.library.test.http.TC15;
import com.library.test.http.TC16;
import com.library.test.http.TC17;
import com.library.test.http.TC18;
import com.library.test.http.TC19;
import com.library.test.http.TC2;
import com.library.test.http.TC20;
import com.library.test.http.TC21;
import com.library.test.http.TC22;
import com.library.test.http.TC23;
import com.library.test.http.TC24;
import com.library.test.http.TC25;
import com.library.test.http.TC25a;
import com.library.test.http.TC3;
import com.library.test.http.TC4;
import com.library.test.http.TC5;
import com.library.test.http.TC6;
import com.library.test.http.TC7;
import com.library.test.http.TC8;

public class LibraryTestSuite {

	public static void main(String[] args) {

		TestSuite suite = new TestSuite("Library Management");

		suite.addTest(new TestSuite(TC1.class));
		suite.addTest(new TestSuite(TC10.class));
		suite.addTest(new TestSuite(TC11.class));
		suite.addTest(new TestSuite(TC12.class));
		suite.addTest(new TestSuite(TC13.class));
		suite.addTest(new TestSuite(TC14a.class));
		suite.addTest(new TestSuite(TC14c.class));
		suite.addTest(new TestSuite(TC15.class));
		suite.addTest(new TestSuite(TC16.class));
		suite.addTest(new TestSuite(TC17.class));
		suite.addTest(new TestSuite(TC18.class));
		suite.addTest(new TestSuite(TC19.class));
		suite.addTest(new TestSuite(TC2.class));
		suite.addTest(new TestSuite(TC20.class));
		suite.addTest(new TestSuite(TC21.class));
		suite.addTest(new TestSuite(TC22.class));
		suite.addTest(new TestSuite(TC23.class));
		suite.addTest(new TestSuite(TC24.class));
		suite.addTest(new TestSuite(TC25.class));
		suite.addTest(new TestSuite(TC25a.class));
		suite.addTest(new TestSuite(TC3.class));
		suite.addTest(new TestSuite(TC4.class));
		suite.addTest(new TestSuite(TC5.class));
		suite.addTest(new TestSuite(TC6.class));
		suite.addTest(new TestSuite(TC7.class));
		suite.addTest(new TestSuite(TC8.class));

		TestResult testResult = TestRunner.run(suite);

		
	}
}

class Listener implements TestListener {
	long startTime = 0;
	long endTime = 0;
	long totalTime = 0;

	public void addError(Test arg0, Throwable arg1) {
		System.out.println("error : " + arg0.getClass().getName() + "  "
				+ arg1.getMessage());
		System.out.println(arg1.getStackTrace());
	}

	public void addFailure(Test arg0, AssertionFailedError arg1) {
		System.out.println("failed : " + arg0.getClass().getName());
	}

	public void endTest(Test arg0) {
		endTime = System.currentTimeMillis();
		System.out.println("ended : " + arg0.getClass().getName() + " at "
				+ new Date());
		totalTime = endTime - startTime;
		System.out.println(arg0.getClass().getName()
				+ " totalTime : "
				+ String.format(
						"%d min, %d sec",
						TimeUnit.MILLISECONDS.toMinutes(totalTime),
						TimeUnit.MILLISECONDS.toSeconds(totalTime)
								- TimeUnit.MINUTES
										.toSeconds(TimeUnit.MILLISECONDS
												.toMinutes(totalTime))));

	}

	public void startTest(Test arg0) {
		startTime = System.currentTimeMillis();
		System.out.println("started : " + arg0.getClass().getName() + " at "
				+ new Date());
	}
}

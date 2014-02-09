package com.library.test.suite;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import junit.framework.TestSuite;

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
import com.library.test.http.TC25b;
import com.library.test.http.TC3;
import com.library.test.http.TC4;
import com.library.test.http.TC5;
import com.library.test.http.TC6;
import com.library.test.http.TC7;
import com.library.test.http.TC8;

public class LibraryTestSuite {

	public static void main(String[] args) {

		TestSuite suite = new TestSuite();
		suite.addTest(new TC1(""));
		suite.addTest(new TC2());
		suite.addTest(new TC3(""));
		suite.addTest(new TC4(""));
		suite.addTest(new TC5(""));
		suite.addTest(new TC6());
		suite.addTest(new TC7());
		suite.addTest(new TC8());
		suite.addTest(new TC10(""));
		suite.addTest(new TC11());
		suite.addTest(new TC12());
		suite.addTest(new TC13());
		suite.addTest(new TC14a());
		suite.addTest(new TC14c());
		suite.addTest(new TC15());
		suite.addTest(new TC16(""));
		suite.addTest(new TC17(""));
		suite.addTest(new TC18(""));
		suite.addTest(new TC19());
		suite.addTest(new TC20());
		suite.addTest(new TC21());
		suite.addTest(new TC22());
		suite.addTest(new TC23());
		suite.addTest(new TC24(""));
		suite.addTest(new TC25(""));
		suite.addTest(new TC25a(""));
		suite.addTest(new TC25b(""));
		JUnitCore junitCore = new JUnitCore();
		Result result = junitCore.run(suite);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
	}

}

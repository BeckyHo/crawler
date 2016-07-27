package com.crawler.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {

	private ApplicationContext ctx = null;

	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Test
	public void testGetConnection() {
		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			System.out.println(dataSource.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

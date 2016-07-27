package com.crawler.bean;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFrameWorkTest {

	private ApplicationContext ctx = null;

	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Test
	public void testFirstDemo() {
		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			System.out.println(dataSource.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

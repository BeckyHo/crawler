package com.crawler.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {

	private ApplicationContext ctx = null;

	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Test
	public void testConfig() {
		SessionFactory sessionFactory = (SessionFactory) ctx
				.getBean("sessionFactory");
		Session session = sessionFactory.getCurrentSession();
		System.out.println(session);
	}
}

package com.engine.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplicationContext {

	private static ApplicationContext ctx;

	private SpringApplicationContext() {

	}

	public static ApplicationContext getApplicationContext() {
		if (ctx == null) {
			synchronized (SpringApplicationContext.class) {
				if (ctx == null) {
					ctx = new ClassPathXmlApplicationContext(
							"applicationContext.xml");
				}
			}
		}

		return ctx;
	}
}

package com.engine.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class ExtLogger {

	private static final String xmlConfig = "target/classes/logConfig.xml";
	private static Logger stdout;
	private static Logger daily;

	static {
		DOMConfigurator.configure(xmlConfig);
		stdout = Logger.getLogger("stdout");
		daily = Logger.getLogger("daily");
	}

	public static void info(String msg) {
		stdout.debug(msg);
	}

	public static void debug(String msg) {
		daily.debug(msg);
	}

	public static void line() {
		stdout.debug("-----------------------------------------------------");
	}
}

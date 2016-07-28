package com.engine.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class ExtLogger {

	private static final String xmlConfig = "classpath:logConfig.xml";
	private static Logger stdout;
	private static Logger daily;

	public static void initLogger() {
		DOMConfigurator.configure(xmlConfig);
		stdout = Logger.getLogger("STDOUT");
		daily = Logger.getLogger("daily");
	}

	public static void serverDebug(String msg) {
		// stdout.debug(msg);
	}

	public static void debug(String msg) {
		// daily.debug(msg);
	}
}

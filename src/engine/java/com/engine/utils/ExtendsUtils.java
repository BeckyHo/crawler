package com.engine.utils;

public class ExtendsUtils {

	public static final String HTTP_PREFIX = "http://";

	public static final String HTTPS_PREFIX = "https://";

	public static final char SEPARATOR = '/';

	public static String getHostName(String uri) {
		if (isHttpProtocol(uri)) {
			int fromIndex = uri.startsWith(HTTP_PREFIX) ? 7 : 8;
			int length = uri.length();
			for (int i = fromIndex; i < length; i++) {
				char ch = uri.charAt(i);
				if (ch == SEPARATOR) {
					return uri.substring(fromIndex, i);
				}
			}
			return uri.substring(fromIndex);
		}
		return null;
	}

	public static boolean isHttpProtocol(String uri) {
		if (uri.startsWith(HTTP_PREFIX) || uri.startsWith(HTTPS_PREFIX)) {
			return true;
		}
		return false;
	}

	public static boolean isEmptyString(String x) {
		if (x == null)
			return true;
		if (x.length() <= 0)
			return true;
		return false;
	}
}

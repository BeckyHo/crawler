package com.engine.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {

	public static String getMD5String(String text) {
		StringBuffer buffer = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] digests = md.digest();

			int i = 0;
			for (int index = 0; index < digests.length; index++) {
				i = digests[index];
				if (i < 0) {
					i += 256;
				}

				if (i < 16) {
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
}

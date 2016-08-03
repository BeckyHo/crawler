package com.engine.launch;

import java.io.IOException;

import com.engine.lucene.BuildLucene;

public class LuceneLaunch {

	public static void main(String[] args) {
		try {
			BuildLucene.getInstance().buildIndex();
			BuildLucene.getInstance().index("�人");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

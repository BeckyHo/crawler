package com.engine.launch;

import com.engine.lucene.BuildLucene;

public class LuceneLaunch {

	public static void main(String[] args) {

		BuildLucene.getInstance().buildIndex();
		BuildLucene.getInstance().index("Œ‰∫∫");
	}
}

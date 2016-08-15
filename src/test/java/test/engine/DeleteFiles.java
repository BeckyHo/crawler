package test.engine;

import java.io.File;

public class DeleteFiles {

	public static void main(String[] args) {
		File parent = new File(
				"C:/Users/yangguangyong/Documents/Github/crawler/src/engine/resources/snapshot");
		if (!parent.exists()) {
			return;
		}

		File[] files = parent.listFiles();
		if (files == null || files.length == 0) {
			return;
		}
		
		for (File file : files) {
			file.delete();
		}
	}
}

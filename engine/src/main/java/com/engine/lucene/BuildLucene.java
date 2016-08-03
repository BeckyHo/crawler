package com.engine.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;
import com.engine.utils.LuceneUtils;

public class BuildLucene {

	private static BuildLucene instance = null;

	private StandardAnalyzer analyzer = null;
	private Directory directory = null;
	private IndexWriterConfig config = null;
	private IndexWriter writer = null;

	private BuildLucene() {

	}

	private void init() throws IOException {
		analyzer = new StandardAnalyzer();
		directory = new RAMDirectory();
		config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(directory, config);
	}

	public void buildIndex() throws IOException {
		// 为该路劲下的所有文件内容建立索引
		init();
		String filePath = ConfigArgs.DOWNLOAD_PATH;
		File rootFile = new File(filePath);

		if (!rootFile.exists()) {
			ExtLogger.info(String.format(
					"<BuildLucene>.buildIndex, file path not exists, path=%s",
					filePath));
			return;
		}

		File[] files = rootFile.listFiles();
		for (File file : files) {
			ExtLogger
					.info(String.format("%s will build index", file.getName()));
			String content = LuceneUtils.readFileContent(file);
			checkNotNull(content);

			Document doc = new Document();
			doc.add(new Field("fieldName", content, TextField.TYPE_STORED));
			writer.addDocument(doc);
		}

		writer.close();
	}

	// 根据关键字检索
	public void index(String keyword) {
		try {
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser parser = new QueryParser("fieldName", analyzer);
			Query query = parser.parse(keyword);

			ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
			ExtLogger.info(String.format("%s search %s results, they are: ",
					keyword, hits.length));
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < hits.length; i++) {
				buffer.append(hits[i].doc).append(", ");
			}

			ExtLogger.info(String.format("%s", buffer.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void checkNotNull(String str) {
		if (str == null || str.equals("")) {
			throw new NullPointerException();
		}
	}

	public static synchronized BuildLucene getInstance() {
		if (instance == null) {
			instance = new BuildLucene();
		}

		return instance;
	}
}

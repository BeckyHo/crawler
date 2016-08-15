package com.engine.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.apache.lucene.store.MMapDirectory;

import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;
import com.engine.utils.LuceneUtils;

public class BuildLucene {

	private static BuildLucene instance = null;

	private StandardAnalyzer analyzer = null;
	private Directory directory = null;
	private IndexWriterConfig config = null;

	private static final String FIELD_URL = "url";
	private static final String FIELD_CONTENT = "jobMessage";
	private static final String FIELD_POST_TIME = "postTime";
	private static final int TOP_RANK = 10;

	private BuildLucene() {
		init();
	}

	private void init() {
		try {
			analyzer = new StandardAnalyzer();

			// store in memory, use RAMDirectory. or
			// directory = new RAMDirectory();
			// store in disk, you can use MMapDirectory
			Path path = FileSystems.getDefault()
					.getPath(ConfigArgs.LUCENE_PATH);
			directory = new MMapDirectory(path);
			config = new IndexWriterConfig(analyzer);
		} catch (IOException e) {
			ExtLogger
					.info("<BuildLucene>.init() init lucene attribute throws IOException");
		}
	}

	public void buildIndex() {
		// 为该路径下的所有文件内容创建索引
		String filePath = ConfigArgs.DOWNLOAD_PATH;
		File rootFile = new File(filePath);

		if (!rootFile.exists()) {
			ExtLogger.info(String.format(
					"<BuildLucene>.buildIndex, file path not exists, path=%s",
					filePath));
			return;
		}

		IndexWriter writer = null;
		File[] files = rootFile.listFiles();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String postTime = sdf.format(new Date());

		try {
			writer = new IndexWriter(directory, config);
			for (File file : files) {
				ExtLogger.info(String.format("%s will build index",
						file.getName()));

				String fileName = file.getName();
				String fileUrl = filePath + File.separator + fileName;
				String content = LuceneUtils.readFileContent(file);

				// 为不同域建立索引
				Document doc = new Document();
				doc.add(new Field(FIELD_URL, fileUrl, TextField.TYPE_STORED));
				doc.add(new Field(FIELD_CONTENT, content, TextField.TYPE_STORED));
				doc.add(new Field(FIELD_POST_TIME, postTime,
						TextField.TYPE_STORED));

				writer.addDocument(doc);
			}
		} catch (IOException e) {
			ExtLogger
					.info("<BuildLucene> buildIndex(). writer add doc failed , throws IOException");
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				ExtLogger
						.info("<BuildLucene> buildIndex(). close writer throws IOException");
			}
		}
	}

	// 根据关键字检索
	public void index(String keyword) {
		try {
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser parser = new QueryParser(FIELD_CONTENT, analyzer);
			Query query = parser.parse(keyword);

			ScoreDoc[] hits = searcher.search(query, TOP_RANK).scoreDocs;
			ExtLogger.info(String.format("%s search %s results, they are: ",
					keyword, hits.length));
			ExtLogger.info(String.format("%s search result: ", keyword));
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				// 根据文档ID得到对应的Document对象
				Document doc = searcher.doc(docId);
				String content = doc.get(FIELD_CONTENT);
				ExtLogger.info(String.format("docId=%s, content=%s", docId,
						content));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static synchronized BuildLucene getInstance() {
		if (instance == null) {
			instance = new BuildLucene();
		}

		return instance;
	}
}

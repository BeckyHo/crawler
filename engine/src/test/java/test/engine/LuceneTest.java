package test.engine;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneTest {

	public static void main(String[] args) throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer();

		// store the index in memory,
		// to store on disk, use FSDirectory instead
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);

		String[] texts = new String[] { "This is the text to be indexed",
				"That is an cat", "he is shit", "stupid women,so bitch" };
		for (String text : texts) {
			Document doc = new Document();
			doc.add(new Field("fieldName", text, TextField.TYPE_STORED));
			writer.addDocument(doc);
		}

		writer.close();

		// now search the index
		DirectoryReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		QueryParser parser = new QueryParser("fieldName", analyzer);
		Query query = parser.parse("shit");

		ScoreDoc[] hits = searcher.search(query, 5).scoreDocs;
		System.out.println(hits.length);

		for (int i = 0; i < hits.length; i++) {

			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("fieldName"));
		}

		reader.close();
		directory.close();
	}
}

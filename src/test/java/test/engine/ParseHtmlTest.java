package test.engine;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;

public class ParseHtmlTest {

	public static void testExtractDivNode(String url, String encode,
			Class<?> filterClass) throws Exception {
		Parser parser = new Parser(url);
		parser.setEncoding(encode);

		NodeFilter filter = new NodeClassFilter(filterClass);
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		for (int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.elementAt(i);
			if (node instanceof Div) {
				Div divNode = (Div) node;
				String classAttri = divNode.getAttribute("class");
				if (classAttri != null && classAttri.equals("articleinfor")) {
					testParserHtml(divNode);
					break;
				}
			}
		}
	}

	public static void testParserHtml(Node parentNode) {
		NodeList children = parentNode.getChildren();
		if (children != null) {
			for (int j = 0; j < children.size(); j++) {
				testParserHtml(children.elementAt(j));
			}
		} else {
			System.out.println(parentNode.getText());
		}
	}

	public static void main(String[] args) throws Exception {
		ParseHtmlTest.testExtractDivNode(
				"src/engine/resources/snapshot/14569692127428.html", "UTF-8",
				Div.class);
	}
}

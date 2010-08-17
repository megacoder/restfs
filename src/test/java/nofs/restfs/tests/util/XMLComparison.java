package nofs.restfs.tests.util;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLComparison {

	private static void Log(String value) {
		LogFactory.getLog(XMLComparison.class).info(value);
	}

	private static String ConvertToString(ByteBuffer buffer) {
		StringBuffer buff = new StringBuffer();
		byte[] data = buffer.array();
		for(int i = 0 ; i < buffer.position(); i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}

	public static void Compare(String xml1, ByteBuffer xml2) throws Exception {
		Compare(xml1, ConvertToString(xml2));
	}

	public static void Compare(String xml1, String xml2) throws Exception {
		Log("expected: [" + xml1.trim() + "]");
		Log("actual: [" + xml2.trim() + "]");
		Document document1 = BuildDocument(xml1);
		Document document2 = BuildDocument(xml2);
		Compare(document1, document2);
	}

	private static Document BuildDocument(String text) throws Exception {
		try {
			return DocumentHelper.parseText(text.trim());
		} catch(Exception e) {
			Log("failed to parse:");
			Log(text);
			throw e;
		}
	}
/*
	private static String GetPath(Node node) {
		Stack<String> stack = new Stack<String>();
        while(node != null) {
                stack.push(node.getName());
                node = node.getParent();
        }
        StringBuffer path = new StringBuffer();
        while(stack.size() > 0) {
                if(path.length() > 0) {
                        path.append("->");
                }
                path.append(stack.pop());
        }
        return path.toString();
	}
*/
	@SuppressWarnings("unchecked")
	private static List<Element> GetChildren(Branch node) throws Exception {
		List<Element> children = new LinkedList<Element>();
		for(Iterator iter = node.nodeIterator(); iter.hasNext();) {
			Object obj = iter.next();
			if(obj instanceof Element) {
				children.add((Element)obj);
			}
		}
		if(children.size() == 0) {
			throw new Exception("no children found");
		}
		return children;
	}

	private static void Compare(Branch branch1, Branch branch2) throws Exception {
		List<Element> elementsOf1 = GetChildren(branch1);
		List<Element> elementsOf2 = GetChildren(branch2);
		if(elementsOf1.size() != elementsOf2.size() || elementsOf2.size() != 1) {
			throw new Exception("only one root element allowed");
		}
		if(!Compare(elementsOf1.get(0), elementsOf2.get(0))) {
			throw new Exception("XML is not equivalent");
		}
	}

	private static boolean Compare(Attribute attr1, Attribute attr2) {
		return attr1.getName().compareTo(attr2.getName()) == 0 &&
		       attr1.getValue().compareTo(attr2.getValue()) == 0;
	}

	@SuppressWarnings("unchecked")
	private static boolean Compare(Element element1, Element element2) {
		boolean childrenAreEqual = false;
		boolean valuesAreEqual = false;
		boolean attributesAreEqual = false;
		if(element1.getName().compareTo(element2.getName()) == 0) {
			int matchCount = 0;
			for(Element childOf1 : (List<Element>)element1.elements()) {
				boolean matchFound = false;
				for(Element childOf2 : (List<Element>)element2.elements()) {
					if(Compare(childOf1, childOf2)) {
						matchFound = true;
						break;
					}
				}
				if(matchFound) {
					matchCount += 1;
				}
			}
			childrenAreEqual = (element1.elements().size() == element2.elements().size() && matchCount == element1.elements().size());
			matchCount = 0;
			for(Attribute attributeOf1 : (List<Attribute>)element1.attributes()) {
				boolean matchFound = false;
				for(Attribute attributeOf2 : (List<Attribute>)element2.attributes()) {
					if(Compare(attributeOf1, attributeOf2)) {
						matchFound = true;
						break;
					}
				}
				if(matchFound) {
					matchCount += 1;
				}
			}
			attributesAreEqual = (element1.attributes().size() == element2.attributes().size() && matchCount == element1.attributes().size());
			valuesAreEqual = element1.getText().trim().compareTo(element2.getText().trim()) == 0;
		}
		return childrenAreEqual && valuesAreEqual && attributesAreEqual;
	}
}

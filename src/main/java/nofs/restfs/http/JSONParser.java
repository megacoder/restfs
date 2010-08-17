package nofs.restfs.http;

import nofs.restfs.json.jsonLexer;
import nofs.restfs.json.jsonParser;
import nofs.restfs.json.jsonParser.object_return;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.httpclient.NameValuePair;

public class JSONParser {

	public boolean DataIsJSONData(byte[] data) {
		String stringValue = ConvertToString(data);
		jsonLexer lexer = new jsonLexer(new ANTLRStringStream(stringValue));
		CommonTokenStream tokenStream = new CommonTokenStream();
		tokenStream.setTokenSource(lexer);
		jsonParser parser = new jsonParser(tokenStream);
		try {
			parser.object();
			return true;
		} catch(RecognitionException re) {
			return false;
		}
	}
	
	public NameValuePair[] ParseJSONIntoPairs(byte[] data) {
		return null;
	}
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}
}

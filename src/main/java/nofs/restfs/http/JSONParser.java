package nofs.restfs.http;

import java.util.ArrayList;
import java.util.List;

import nofs.restfs.json.jsonLexer;
import nofs.restfs.json.jsonMember;
import nofs.restfs.json.jsonObject;
import nofs.restfs.json.jsonParser;
import nofs.restfs.json.jsonParser.object_return;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.httpclient.NameValuePair;

public class JSONParser {

	public boolean DataIsJSONData(byte[] data) throws Exception {
		try {
			return null != Parse(data);
		} catch(RecognitionException re) {
			return false;
		}
	}
	
	public NameValuePair[] ParseJSONIntoPairs(String formName, byte[] data) throws Exception {
		jsonObject obj = Parse(data);
		if(obj == null) {
			return new NameValuePair[0];
		} else {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(obj.Members.size());
			String prefix, suffix;
			if(formName != null && formName.length() > 0) {
				prefix = formName + "[";
				suffix = "]";
			} else {
				prefix = "";
				suffix = "";
			}
			for(jsonMember member : obj.Members) {
				pairs.add(new NameValuePair(prefix + member.Key + suffix, member.Value));
			}
			return pairs.toArray(new NameValuePair[0]);
		}
	}
	
	private static jsonObject Parse(byte[] data) throws Exception {
		String stringValue = ConvertToString(data);
		jsonLexer lexer = new jsonLexer(new ANTLRStringStream(stringValue));
		CommonTokenStream tokenStream = new CommonTokenStream();
		tokenStream.setTokenSource(lexer);
		jsonParser parser = new jsonParser(tokenStream);
		object_return obj = parser.object();
		int syntaxErrorCount = lexer.getNumberOfSyntaxErrors();
		int errorCount2 = parser.Errors.size();
		if(syntaxErrorCount == 0 && errorCount2 == 0) {
			return new jsonObject(obj);
		} else {
			return null;
		}
	}
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}
}

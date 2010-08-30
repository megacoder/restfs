package nofs.restfs.tests;

import nofs.restfs.http.JSONParser;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Assert;
import org.junit.Test;

public class JSONParserTest {

	@Test
	public void TestDataIsJSONData() throws Exception {
		TestDataIsJSON(false, "abcdefg");
		TestDataIsJSON(false, "a&f43asdf");
		TestDataIsJSON(true, "{\"a\" : \"b\"}");
	}
	
	@Test
	public void TestParseIntoJSONPairs() throws Exception {
		NameValuePair[] pair = new JSONParser().ParseJSONIntoPairs("x", "{\"a\" : \"b\"}");
		Assert.assertEquals(1, pair.length);
		Assert.assertEquals("x[a]", pair[0].getName());
		Assert.assertEquals("b", pair[0].getValue());
	}
	
	@Test
	public void TestSomeHtml() throws Exception {
		final String html = "<html>\\n<head>\\n   <title>Status page</title>\\n</head>\\n<body style=\"font-family: sans-serif;\">\\n<h3>The server encountered an unexpected condition which prevented it from fulfilling the request</h3><p>You can get technical details <a href=\"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1\">here</a>.<br>\\nPlease continue your visit at our <a href=\"/\">home page</a>.\\n</p>\\n</body>\\n</html>\\n";
		TestDataIsJSON(false, html);
		NameValuePair[] pairs = new JSONParser().ParseJSONIntoPairs("", html);
		Assert.assertEquals(0, pairs.length);
	}
	
	private static void TestDataIsJSON(boolean expectedValue, String value) throws Exception
	{
		Assert.assertEquals(
				value,
				expectedValue, 
				new JSONParser().DataIsJSONData(value));
	}
}

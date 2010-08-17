package nofs.restfs.tests;

import java.nio.ByteBuffer;

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
		NameValuePair[] pair = new JSONParser().ParseJSONIntoPairs("x", ToByteArray("{\"a\" : \"b\"}"));
		Assert.assertEquals(1, pair.length);
		Assert.assertEquals("x[a]", pair[0].getName());
		Assert.assertEquals("b", pair[0].getValue());
	}
	
	private static void TestDataIsJSON(boolean expectedValue, String value) throws Exception
	{
		byte[] data = ToByteArray(value);
		Assert.assertEquals(
				value,
				expectedValue, 
				new JSONParser().DataIsJSONData(data));
	}
	
	private static byte[] ToByteArray(String value) {
		ByteBuffer buffer = ByteBuffer.allocate(value.length());
		for(byte charValue : value.getBytes()) {
			buffer.put(charValue);
		}
		buffer.position(0);
		return buffer.array();
	}
}

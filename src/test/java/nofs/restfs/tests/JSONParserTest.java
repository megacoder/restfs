package nofs.restfs.tests;

import java.nio.ByteBuffer;

import nofs.restfs.http.JSONParser;

import org.junit.Assert;
import org.junit.Test;

public class JSONParserTest {

	@Test
	public void TestDataIsJSONData() {
		TestDataIsJSON(false, "abcdefg");
		TestDataIsJSON(false, "a&f43asdf");
		TestDataIsJSON(true, "{\"a\" : \"b\"}");
	}
	
	private static void TestDataIsJSON(boolean expectedValue, String value)
	{
		byte[] data = ToByteArray(value);
		Assert.assertEquals(
				value,
				expectedValue, 
				new JSONParser().DataIsJSONData(data));
	}
	
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
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

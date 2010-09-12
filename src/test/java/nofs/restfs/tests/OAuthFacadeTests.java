package nofs.restfs.tests;

import nofs.restfs.oauth.IOAuthFacade;
import nofs.restfs.oauth.impl.OAuthFacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class OAuthFacadeTests {
	
	private Properties _prop;
	@Before
	public void Setup() throws Exception {
		_prop = LoadPropertiesFile("twitter.properties");
	}
	
	@Test
	public void TestFacade() throws Exception {
		IOAuthFacade facade = new OAuthFacade(
				Key(), Secret(), RequestTokenURL(), 
				AuthorizeURL(), AccessTokenURL(), null);
		facade.beginAuthorization();
		while(!facade.waitForAuthorization(20)) {
		}
		Assert.assertEquals("", facade.getAccessToken());
	}
	
	private String AuthorizeURL() throws Exception {
		return GetProp("authorize_url");
	}
	
	private String AccessTokenURL() throws Exception {
		return GetProp("access_token_url");
	}
	
	private String RequestTokenURL() throws Exception {
		return GetProp("request_token_url");
	}
	
	private String Secret() throws Exception {
		return GetProp("consumer_secret");
	}
	
	private String Key() throws Exception {
		return GetProp("key");
	}

	private String GetProp(String key) throws Exception {
		String value = _prop.getProperty(key);
		if(value == null || value.isEmpty()) {
			throw new Exception("property " + key + " is not present");
		}
		return value;
	}
	
	private static Properties LoadPropertiesFile(String fileName) throws Exception {
		if(!new File(fileName).exists()) {
			throw new Exception("file: " + fileName + " does not exist.");
		}
		InputStream in = new FileInputStream("twitter.properties");
		try {
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		} finally {
			in.close();
		}
	}

}

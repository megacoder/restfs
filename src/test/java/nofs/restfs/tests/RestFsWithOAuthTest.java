package nofs.restfs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.restfs.http.GetAnswer;
import nofs.restfs.http.PostAnswer;
import nofs.restfs.http.WebDavFacade;
import nofs.restfs.oauth.IOAuthFacade;
import nofs.restfs.oauth.impl.OAuthFacade;
import nofs.restfs.tests.oauthexample.WebServerFixture;
import nofs.restfs.tests.util.BaseFuseTests;
import nofs.restfs.tests.util.RestFsTestHelper;
import nofs.restfs.tests.util.RestSettingHelper;
import nofs.restfs.tests.util.TemporaryTestFolder;

public class RestFsWithOAuthTest extends BaseFuseTests {
	private TemporaryTestFolder _tmpFolder;
	private NoFSFuseDriver _fs;
	private WebServerFixture _webFixture;
	private final int PORT = 8182;
	
	@Before
	public void Setup() throws Exception {
		_tmpFolder = new TemporaryTestFolder();
		_fs = RestFsTestHelper.BuildFS(_tmpFolder);
		_webFixture = new WebServerFixture(PORT);
		_webFixture.StartServer();
	}

	@After
	public void TearDown() throws Exception {
		if(_fs != null) {
			_fs.CleanUp();
		}
		if(_tmpFolder != null) {
			_tmpFolder.CleanUp();
		}
		if(_webFixture != null) {
			_webFixture.StopServer();
		}
	}

	//private final String Secret = "gadgetSecret";
	//private final String Key = "gadgetConsumer";
	private final String Secret = "noCallbackSecret";
	private final String Key = "noCallbackConsumer";
	private final String RequestTokenURL = "http://localhost:" + PORT + "/oauth-provider/request_token";
	private final String UserAuthURL = "http://localhost:" + PORT + "/oauth-provider/authorize";
	private final String AccessTokenURL = "http://localhost:" + PORT + "/oauth-provider/access_token";
	
	@Test
	public void TestOAuthFacade() throws Exception {
		IOAuthFacade facade = new OAuthFacade(Key, Secret, RequestTokenURL, UserAuthURL, AccessTokenURL, "none");
		facade.beginAuthorization();
		final int maxWait = 4000;
		boolean success = facade.waitForAuthorization(maxWait);
		Assert.assertEquals("", facade.getError());
		Assert.assertEquals("should not have succeeded yet", false, success);
		final String firstPart = "http://localhost:8182/oauth-provider/authorize?oauth_token=";
		final String lastPart = "&oauth_callback=none";
		Assert.assertTrue(facade.getAuthorizationURL(), facade.getAuthorizationURL().startsWith(firstPart));
		Assert.assertTrue(facade.getAuthorizationURL(), facade.getAuthorizationURL().endsWith(lastPart));
		Assert.assertTrue(facade.getAccessToken() == null || facade.getAccessToken().compareTo("") == 0);
	}
	
	@Test
	public void TestGetTokenWithRestfs() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		String xml = RestSettingHelper.CreateAuthXml(Key, "", Secret, "", AccessTokenURL, UserAuthURL, RequestTokenURL, "none");
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/config"), xml);
		Thread.sleep(2500);
		
		String tokenData = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		Assert.assertEquals("", tokenData);
		
		final String firstPart = "http://localhost:8182/oauth-provider/authorize?oauth_token=";
		final String lastPart = "&oauth_callback=none";
		
		String authURL = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/status"));
		Assert.assertTrue(authURL, authURL.startsWith(firstPart));
		Assert.assertTrue(authURL, authURL.endsWith(lastPart + "\n"));
		String resource = authURL.substring("http://localhost:8182".length()).trim();
		String oauth_token = authURL.substring(firstPart.length()).trim();
		oauth_token = oauth_token.substring(0, oauth_token.length() - lastPart.length()).trim();
		GetAnswer answer = WebDavFacade.Instance().GetMethod("localhost", "" + PORT, resource);
		String representation = ConvertToString(answer.getData());
		Assert.assertEquals(representation, 200, answer.getCode());
		Assert.assertTrue(representation != null && representation.length() > 0);
		
		//System.err.println(representation);
		
		PostAnswer postAnswer = WebDavFacade.Instance().PostMethod(
				"localhost", "" + PORT, "/oauth-provider/authorize.jsp", "authZForm", 
				ConvertToBytes(
						"{\"userId\" : \"foo\", " + 
						"\"oauth_token\" : \"" + oauth_token + "\", " +
						"\"oauth_callback\" : \"none\"" +
						"}"));
		representation = ConvertToString(postAnswer.getData());
		Assert.assertEquals(representation, 200, postAnswer.getCode());
		Assert.assertTrue(representation != null && representation.length() > 0);
		//Assert.assertEquals("", representation);
		System.err.println(representation);
		
		Thread.sleep(2500);
		
		Assert.assertEquals("", RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token")));
	}

	private static byte[] ConvertToBytes(String data) {
		byte[] bytes = new byte[data.length()];
		int i = (-1);
		for(char c : data.toCharArray()) {
			bytes[++i] = (byte)c;
		}
		return bytes;
	}
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}
}




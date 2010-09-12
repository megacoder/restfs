package nofs.restfs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.restfs.http.GetAnswer;
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
		IOAuthFacade facade = new OAuthFacade(Key, Secret, RequestTokenURL, UserAuthURL, AccessTokenURL, true);
		facade.beginAuthorization();
		final int maxWait = 4000;
		boolean success = facade.waitForAuthorization(maxWait);
		Assert.assertEquals("", facade.getError());
		Assert.assertEquals("should not have succeeded yet", false, success);
		Assert.assertTrue(facade.getAuthorizationURL(), facade.getAuthorizationURL().startsWith("http://localhost:8182/oauth-provider/authorize?oauth_token="));
		Assert.assertTrue(facade.getAuthorizationURL(), facade.getAuthorizationURL().endsWith("&oauth_callback=oob"));
		Assert.assertTrue(facade.getAccessToken() == null || facade.getAccessToken().compareTo("") == 0);
	}
	
	@Test
	public void TestGetTokenWithRestfs() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		String xml = RestSettingHelper.CreateAuthXml(Key, "", Secret, "", AccessTokenURL, UserAuthURL, RequestTokenURL);
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/config"), xml);
		Thread.sleep(2500);
		
		String tokenData = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		Assert.assertEquals("", tokenData);
		
		String authURL = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/status"));
		Assert.assertTrue(authURL, authURL.startsWith("http://localhost:8182/oauth-provider/authorize?oauth_token="));
		Assert.assertTrue(authURL, authURL.endsWith("&oauth_callback=oob\n"));
		String resource = authURL.substring("http://localhost:8182".length()).trim();
		GetAnswer answer = WebDavFacade.Instance().GetMethod("localhost", "" + PORT, resource);
		String representation = ConvertToString(answer.getData());
		Assert.assertEquals(representation, 200, answer.getCode());
		Assert.assertEquals("", representation);
	}

	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}
}




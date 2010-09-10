package nofs.restfs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.restfs.oauth.IOAuthFacade;
import nofs.restfs.oauth.impl.OAuthFacade;
import nofs.restfs.tests.oauthexample.WebServerFixture;
import nofs.restfs.tests.util.BaseFuseTests;
import nofs.restfs.tests.util.RestFsTestHelper;
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

	private final String Secret = "gadgetSecret";
	private final String Key = "gadgetConsumer";
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
		Assert.assertEquals("", facade.getAccessToken());
	}
}




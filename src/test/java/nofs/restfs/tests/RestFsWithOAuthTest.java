package nofs.restfs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
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
	public void TestOAuthServiceInteraction() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		final String authXml = RestSettingHelper
			.CreateAuthXml(Key, Secret, AccessTokenURL, UserAuthURL, RequestTokenURL, "none");
		final String blankTokenXml = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/config"), authXml);
		Thread.sleep(2500);
		
		WaitForFileToChange("/auth/x/status", new String[]{"", "Authorizing..."}, 50);
		final String authURL = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/status"));
		RestFsTestHelper.HandleAuthURL(authURL);
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/verifier"), RestSettingHelper.CreateVerifierXml(""));
		WaitForFileToChange("/auth/x/token", new String[]{"", blankTokenXml}, 5);
		final String tokenXml = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		Assert.assertTrue(tokenXml, tokenXml != null && tokenXml.length() > blankTokenXml.length());
		
		final String configXml = RestSettingHelper.CreateSettingsXml("utime", "GET", "", "/oauth-provider/echo", "localhost", "8182", Fix("/auth/x"));
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		RestFsTestHelper.WriteToFile(_fs, Fix("/.x"), configXml);
	
		AssertEquals(configXml, RestFsTestHelper.ReadFromFile(_fs, Fix("/.x")));
		
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		final String contents = RestFsTestHelper.ReadFromFile(_fs, Fix("/x"));
		Assert.assertTrue(contents, contents.startsWith("[Your UserId:foo]"));
	}
	
	@Test
	public void TestGetTokenWithRestfs() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		final String authXml = RestSettingHelper
			.CreateAuthXml(Key, Secret, AccessTokenURL, UserAuthURL, RequestTokenURL, "none");
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/config"), authXml);
		Thread.sleep(2500);
		
		final String blankTokenData = RestSettingHelper.CreateTokenXml("", "", "");
		String tokenData = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		AssertEquals(blankTokenData, tokenData);
		
		final String firstPart = "http://localhost:8182/oauth-provider/authorize?oauth_token=";
		final String lastPart = "&oauth_callback=none";
		
		WaitForFileToChange("/auth/x/status", new String[]{"", "Authorizing..."}, 50);
		
		String authURL = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/status"));
		Assert.assertTrue(authURL, authURL.startsWith(firstPart));
		Assert.assertTrue(authURL, authURL.endsWith(lastPart + "\n"));
		
		RestFsTestHelper.HandleAuthURL(authURL);
		
		RestFsTestHelper.WriteToFile(_fs, Fix("/auth/x/verifier"), RestSettingHelper.CreateVerifierXml(""));
		
		WaitForFileToChange("/auth/x/token", new String[]{""}, 5);
		String token = RestFsTestHelper.ReadFromFile(_fs, Fix("/auth/x/token"));
		Assert.assertTrue(token, token != null && token.length() > 0);
	}

	private void WaitForFileToChange(String path, String[] nullStates, int maxTries) throws Exception {
		boolean changed = false;
		for(int i = 0; i < maxTries; i++) {
			String data = RestFsTestHelper.ReadFromFile(_fs, Fix(path));
			boolean match = false;
			for(String value : nullStates) {
				if(value.compareTo(data) == 0) {
					match = true;
					break;
				}
			}
			if(!match) {
				changed = true;
				break;
			} else {
				Thread.sleep(2500);
			}
		}
		if(!changed) {
			Assert.fail("failed to change");
		}
	}
}




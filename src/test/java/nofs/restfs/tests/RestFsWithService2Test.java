package nofs.restfs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.restfs.tests.util.BaseFuseTests;
import nofs.restfs.tests.util.RestExampleRunner2;
import nofs.restfs.tests.util.RestFsTestHelper;
import nofs.restfs.tests.util.RestSettingHelper;
import nofs.restfs.tests.util.TemporaryTestFolder;

public class RestFsWithService2Test extends BaseFuseTests {
	private TemporaryTestFolder _tmpFolder;
	private NoFSFuseDriver _fs;
	private RestExampleRunner2 _app;

	@Before
	public void Setup() throws Exception {
		_tmpFolder = new TemporaryTestFolder();
		_fs = RestFsTestHelper.BuildFS(_tmpFolder);
		_app = new RestExampleRunner2();
		_app.StartRunner();
	}

	@After
	public void TearDown() throws Exception {
		_fs.CleanUp();
		_tmpFolder.CleanUp();
		_app.StopRunner();
	}
	
	@Test
	public void TestPostThenPutThenGetOnUTime() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "post", "", "/firstResource/items", "127.0.0.1", "8100", ""));
		RestFsTestHelper.WriteToFile(_fs, "/x", "{\"description\":\"1\",\"name\":\"foobar\"}");
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		String result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals("Item created", result);
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "get", "", "/firstResource/items/foobar", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><item><name>foobar</name><description>1</description></item>",
				result);
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "put", "", "/firstResource/items/foobar", "127.0.0.1", "8100", ""));
		RestFsTestHelper.WriteToFile(_fs, "/x", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><item><name>foobar</name><description>123</description></item>");
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals("", result);
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "get", "", "/firstResource/items/foobar", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><item><name>foobar</name><description>123</description></item>",
				result);
	}
	
	@Test
	public void TestPostThenGetOnUTime() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "post", "", "/firstResource/items", "127.0.0.1", "8100", ""));
		RestFsTestHelper.WriteToFile(_fs, "/x", "{\"description\":\"1\",\"name\":\"foobar\"}");
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		String result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals("Item created", result);
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "get", "", "/firstResource/items/foobar", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><item><name>foobar</name><description>1</description></item>",
				result);
	}
	
	@Test
	public void TestPostThenDeleteOnUTime() throws Exception { 
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "post", "", "/firstResource/items", "127.0.0.1", "8100", ""));
		RestFsTestHelper.WriteToFile(_fs, "/x", "{\"description\":\"1\",\"name\":\"foobar\"}");
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		
		RestFsTestHelper.WriteToFile(_fs, "/.x", RestSettingHelper.CreateSettingsXml("utime", "delete", "", "/firstResource/items/foobar", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		String result = RestFsTestHelper.ReadFromFile(_fs, "/x");
		Assert.assertEquals("", result);
	}
}

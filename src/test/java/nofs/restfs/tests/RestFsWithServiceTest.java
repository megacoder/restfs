package nofs.restfs.tests;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.Factories.IPersistenceFactory;
import nofs.metadata.AnnotationDriver.NoFSClassLoader;
import nofs.metadata.interfaces.IMetadataFactory;
import nofs.metadata.interfaces.INoFSClassLoader;
import nofs.restfs.tests.util.BaseFuseTests;
import nofs.restfs.tests.util.MockFuseOpenSetter;
import nofs.restfs.tests.util.RestExampleRunner;
import nofs.restfs.tests.util.RestSettingHelper;
import nofs.restfs.tests.util.TemporaryTestFolder;

public class RestFsWithServiceTest extends BaseFuseTests {
	private TemporaryTestFolder _tmpFolder;
	private NoFSFuseDriver _fs;
	private RestExampleRunner _app;

	@Before
	public void Setup() throws Exception {
		_tmpFolder = new TemporaryTestFolder();
		_fs = BuildFS();
		_app = new RestExampleRunner();
		_app.StartRunner();
	}

	@After
	public void TearDown() throws Exception {
		_fs.CleanUp();
		_tmpFolder.CleanUp();
		_app.StopRunner();
	}
	
	private void WriteToFile(String path, String xml) throws Exception {
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, _fs.truncate(Fix(path), 0));
		ByteBuffer buffer = WrapInBuffer(xml);
		Assert.assertEquals(0, _fs.write(path, handle.getFh(), false, buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix(path), handle.getFh(), 0));
	}
	
	private String ReadFromFile(String path) throws Exception {
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix(path), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix(path), handle.getFh(), 0));
		return ConvertToString(buffer);
	}
		
	private NoFSFuseDriver BuildFS() throws Exception {
		final String objectStore = _tmpFolder.getPath("fs.db");
		final String metaFile = _tmpFolder.getPath("meta.db");
		final String metaDataDriver = "nofs.metadata.AnnotationDriver.Factory";
		final String persistenceDriver = "nofs.Factories.Db4oPersistenceFactory";
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		INoFSClassLoader nofsLoader = new NoFSClassLoader(ClassLoader.getSystemClassLoader());
		IMetadataFactory metadataFactory = (IMetadataFactory)loader.loadClass(metaDataDriver).newInstance();
		IPersistenceFactory persistenceFactory = (IPersistenceFactory)loader.loadClass(persistenceDriver).newInstance();
		NoFSFuseDriver fs = new NoFSFuseDriver(nofsLoader, objectStore, metaFile, metadataFactory, persistenceFactory);
		fs.Init();
		return fs;
	}
	
	@Test
	public void TestPostUserOnUTime() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		WriteToFile("/.x", RestSettingHelper.CreateSettingsXml("utime", "post", "user", "/users/5", "127.0.0.1", "8100", ""));
		WriteToFile("/x", "{\"id\":\"1\",\"name\":\"foobar\"}");
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		String result = ReadFromFile("/x");
		Assert.assertEquals("{\"name\":\"foobar\"}", result);
		
		WriteToFile("/.x", RestSettingHelper.CreateSettingsXml("utime", "get", "", "/users/5", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		result = ReadFromFile("/x");
		Assert.assertEquals("{\"id\":\"1\",\"name\":\"name\"}", result);
	}
	
	@Test
	public void TestGetUserOnUtime() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		WriteToFile("/.x", RestSettingHelper.CreateSettingsXml("utime", "get", "", "/users/5", "127.0.0.1", "8100", ""));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
		String result = ReadFromFile("/x");
		Assert.assertEquals("{\"id\":\"1\",\"name\":\"name\"}", result);
	}
	
	@Test
	public void TestGetUserBeforeRead() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		WriteToFile("/.x", RestSettingHelper.CreateSettingsXml("beforeread", "get", "", "/users/5", "127.0.0.1", "8100", ""));
		String result = ReadFromFile("/x");
		Assert.assertEquals("{\"id\":\"1\",\"name\":\"name\"}", result);
	}
	
	@Test
	public void TestGetUserOpened() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		WriteToFile("/.x", RestSettingHelper.CreateSettingsXml("opened", "get", "", "/users/5", "127.0.0.1", "8100", ""));
		String result = ReadFromFile("/x");
		Assert.assertEquals("{\"id\":\"1\",\"name\":\"name\"}", result);
	}
}
